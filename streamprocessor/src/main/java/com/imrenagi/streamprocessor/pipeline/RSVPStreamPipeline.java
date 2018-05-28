package com.imrenagi.streamprocessor.pipeline;

import com.google.cloud.bigtable.beam.CloudBigtableConfiguration;
import com.google.cloud.bigtable.beam.CloudBigtableIO;
import com.google.cloud.bigtable.beam.CloudBigtableTableConfiguration;
import com.imrenagi.streamprocessor.dofn.KeyedRSVPDoFn;
import com.imrenagi.streamprocessor.dofn.KeyedReducerDoFn;
import com.imrenagi.streamprocessor.dofn.RsvpParserDoFn;
import com.imrenagi.streamprocessor.dofn.TimeWindowedCountPut;
import com.imrenagi.streamprocessor.model.RSVP;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.AfterProcessingTime;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RSVPStreamPipeline {

  public static void main(String[] args) {

    Options options = PipelineOptionsFactory.fromArgs(args)
        .withValidation()
        .as(Options.class);
    options.setStreaming(true);
    options.setRunner(DataflowRunner.class);
    options.setJobName("testing-imre-pubsub-to-gcs");

    DataflowPipelineOptions dataflowOptions = options.as(DataflowPipelineOptions.class);

    CloudBigtableTableConfiguration bigtableConfig = new CloudBigtableTableConfiguration.Builder()
        .withProjectId(options.getBigtableProjectId())
        .withInstanceId(options.getBigtableInstanceId())
        .withTableId(options.getBigtableTableId())
        .build();

    Pipeline p = Pipeline.create(dataflowOptions);

    PCollection<String> rsvpInput = p.apply("pubsubReader", PubsubIO.readStrings().fromSubscription(options.getSubscriptionId()));

    String gcsBucketPath = String.format("gs://%s", options.getOutputBucket());

    DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy/MM/dd");

    rsvpInput.apply(ParDo.of(new DoFn<String, String>() {
      @ProcessElement
      public void processElement(ProcessContext processContext) throws Exception {
        processContext.outputWithTimestamp(processContext.element(),
            new Instant());
      }
    }))
        .apply("gcsWriterWindow", Window.<String>into(FixedWindows.of(Duration.standardMinutes(10))))
        .apply("gcsWriterExec", TextIO.write()
            .to(String
                .format("%s/rsvp-stream/%s/rsvp-", gcsBucketPath, dtfOut.print(new DateTime())))
            .withSuffix(".json")
            .withWindowedWrites()
            .withNumShards(10)
        );

    rsvpInput.apply("rsvpParser", ParDo.of(new RsvpParserDoFn()))
        .apply("rsvpGroupWindow", Window.<RSVP>into(FixedWindows.of(Duration.standardMinutes(15))))
        .apply("rsvpKeyify",ParDo.of(new KeyedRSVPDoFn()))
        .apply("rsvpGroupByKey", GroupByKey.<String, RSVP>create())
        .apply("rsvpReducer", ParDo.of(new KeyedReducerDoFn<RSVP>()))
        .apply("rsvpToBigtablePut", ParDo.of(new TimeWindowedCountPut()))
        .apply("bigtableWrite", CloudBigtableIO.writeToTable(bigtableConfig));

    p.run();
  }

  private interface Options extends DataflowPipelineOptions {

    @Description("Bucket Name For Writing The Output Data")
    @Validation.Required
    String getOutputBucket();
    void setOutputBucket(String value);

    @Description("RSVP subscription name")
    @Validation.Required
    String getSubscriptionId();
    void setSubscriptionId(String subscriptionId);

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

}
