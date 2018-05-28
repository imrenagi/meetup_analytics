package com.imrenagi.streamprocessor.dofn;

import java.util.Iterator;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class KeyedReducerDoFn<T> extends DoFn<KV<String, Iterable<T>>, KV<String, Integer>> {

  @ProcessElement
  public void processElement(ProcessContext ctx) {
    KV<String, Iterable<T>> element = ctx.element();
    int count = 0;

    Iterator<T> iter = element.getValue().iterator();
    while (iter.hasNext()) {
      T obj = iter.next();
      if (obj != null) {
        count++;
      }
    }

    ctx.output(KV.of(element.getKey(), count));
  }

}
