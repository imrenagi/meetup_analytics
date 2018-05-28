package com.imrenagi.streamprocessor.dofn;

import com.google.gson.Gson;
import com.imrenagi.streamprocessor.model.RSVP;
import org.apache.beam.sdk.transforms.DoFn;

public class RsvpParserDoFn extends DoFn<String, RSVP> {

  @ProcessElement
  public void processElement(ProcessContext ctx) {
    Gson gson = new Gson();
    RSVP rsvp = gson.fromJson(ctx.element(), RSVP.class);
    ctx.output(rsvp);
  }

}
