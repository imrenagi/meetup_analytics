package com.imrenagi.streamprocessor.dofn;

import com.google.gson.Gson;
import com.imrenagi.streamprocessor.model.OpenEvent;
import org.apache.beam.sdk.transforms.DoFn;

public class OpenEventParseDoFn extends DoFn<String, OpenEvent> {

  @ProcessElement
  public void processElement(ProcessContext ctx) {
    Gson gson = new Gson();
    OpenEvent event = gson.fromJson(ctx.element(), OpenEvent.class);
    ctx.output(event);
  }
}
