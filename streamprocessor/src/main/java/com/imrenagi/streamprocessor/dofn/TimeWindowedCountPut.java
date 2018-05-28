package com.imrenagi.streamprocessor.dofn;

import com.imrenagi.streamprocessor.utils.date.DateHelper;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;

public class TimeWindowedCountPut extends DoFn<KV<String, Integer>, Mutation> {

  @ProcessElement
  public void processElement(ProcessContext ctx) {
    KV<String, Integer> element = ctx.element();
    TimeWindowPut put = new TimeWindowPut(element.getKey());
    put.insertCount(element.getValue());

    ctx.output(put);
  }

  private static class TimeWindowPut extends Put {

    public TimeWindowPut(String rowkey) {
      super(Bytes.toBytes(rowkey));
    }

    public void insertCount(int count) {
      String hhmm = DateHelper.roundDateToHHmm(new DateTime(), 15);
      this.addColumn(Bytes.toBytes("count"), Bytes.toBytes(hhmm), Bytes.toBytes(count));
    }

  }

}
