package com.imrenagi.streamprocessor.dofn;

import com.imrenagi.streamprocessor.model.RSVP;
import com.imrenagi.streamprocessor.utils.date.DateHelper;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.joda.time.DateTime;

public class KeyedRSVPDoFn extends DoFn<RSVP, KV<String, RSVP>> {

  @ProcessElement
  public void processElement(ProcessContext ctx) {
    RSVP rsvp = ctx.element();
//    String key = DateHelper.toYyyyMmDd(new DateTime(rsvp.getTimestamp()));
    String key = DateHelper.toYyyyMmDd(new DateTime());
    ctx.output(KV.of(key, rsvp));
  }

}
