package com.imrenagi.streamprocessor.pipeline;

import com.google.cloud.bigtable.beam.CloudBigtableIO;
import com.google.cloud.bigtable.beam.CloudBigtableTableConfiguration;
import com.imrenagi.streamprocessor.dofn.KeyedReducerDoFn;
import com.imrenagi.streamprocessor.dofn.RsvpParserDoFn;
import com.imrenagi.streamprocessor.model.RSVP;
import com.imrenagi.streamprocessor.utils.date.DateHelper;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSVPBatchPipeline {

  private static final Logger LOG = LoggerFactory.getLogger(RSVPBatchPipeline.class);

  public static void main(String[] args) {

    Options options = PipelineOptionsFactory.fromArgs(args)
        .withValidation()
        .as(Options.class);
    options.setStreaming(false);
    options.setRunner(DataflowRunner.class);
    options.setJobName("testing-imre-gcs-to-bigtable-batch");

    DataflowPipelineOptions dataflowOptions = options.as(DataflowPipelineOptions.class);

    CloudBigtableTableConfiguration bigtableConfig = new CloudBigtableTableConfiguration.Builder()
        .withProjectId(options.getBigtableProjectId())
        .withInstanceId(options.getBigtableInstanceId())
        .withTableId(options.getBigtableTableId())
        .build();

    String bucket = String
        .format("gs://%s/%s", options.getInputBucket(), options.getInputPrefix());
    Pipeline p = Pipeline.create(dataflowOptions);

    p.apply("gcsRead", TextIO.read().from(bucket))
        .apply("rsvpParser", ParDo.of(new RsvpParserDoFn()))
        .apply("batchTransform", new BatchTransform())
        .apply("bigtableUpdate", ParDo.of(new GroupCountRSVPPut()))
        .apply("bigtableWrite", CloudBigtableIO.writeToTable(bigtableConfig));

    p.run();
  }

  private interface Options extends DataflowPipelineOptions {

    @Description("Bucket Name For Reading The Input Data")
    @Validation.Required
    String getInputBucket();

    void setInputBucket(String value);

    @Description("Bucket Preffix for the input data")
    @Validation.Required
    String getInputPrefix();

    void setInputPrefix(String value);

    @Description("Bigtable Project Id")
    @Validation.Required
    String getBigtableProjectId();

    void setBigtableProjectId(String bigtableProjectId);

    @Description("Bigtable Instance Id")
    @Validation.Required
    String getBigtableInstanceId();

    void setBigtableInstanceId(String bigtableInstanceId);

    @Description("Bigtable table id")
    @Validation.Required
    String getBigtableTableId();

    void setBigtableTableId(String bigtableTableId);

  }

  static class BatchTransform extends
      PTransform<PCollection<RSVP>, PCollection<KV<String, Integer>>> {

    @Override
    public PCollection<KV<String, Integer>> expand(PCollection<RSVP> rsvpCollection) {
      return rsvpCollection
          .apply("rsvpKeyify", ParDo.of(new KeyifyRSVP()))
          .apply("rsvpGroupByKey", GroupByKey.<String, RSVP>create())
          .apply("rsvpReducer", ParDo.of(new KeyedReducerDoFn<RSVP>()));
    }
  }

  private static class KeyifyRSVP extends DoFn<RSVP, KV<String, RSVP>> {

    @ProcessElement
    public void processElement(ProcessContext ctx) {
      RSVP rsvp = ctx.element();
      String date = DateHelper.toYyyyMmDd(new DateTime(rsvp.getTimestamp()));
      String hhmm = DateHelper.roundDateToHHmm(new DateTime(rsvp.getTimestamp()), 15);
      String key = date + "\t" + hhmm;
      ctx.output(KV.of(key, rsvp));
    }
  }

  private static class GroupCountRSVPPut extends DoFn<KV<String, Integer>, Mutation> {

    @ProcessElement
    public void processElement(ProcessContext ctx) {

      KV<String, Integer> element = ctx.element();
      String key = element.getKey().split("\t")[0];
      String col = element.getKey().split("\t")[1];
      TimeWindowPut put = new TimeWindowPut(key, col);
      put.insertCount(element.getValue());
      ctx.output(put);
    }

    private static class TimeWindowPut extends Put {

      private String col;
      private String rowkey;

      public TimeWindowPut(String rowkey, String col) {
        super(Bytes.toBytes(rowkey));
        this.rowkey = rowkey;
        this.col = col + "";
      }

      public void insertCount(int count) {
        LOG.info(String.format("%s -> count:%s -> %d", this.rowkey, this.col, count));
        this.addColumn(Bytes.toBytes("count"), Bytes.toBytes(this.col), Bytes.toBytes(count));
      }
    }
  }

}
