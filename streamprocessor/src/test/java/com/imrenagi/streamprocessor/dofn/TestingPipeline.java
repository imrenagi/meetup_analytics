package com.imrenagi.streamprocessor.dofn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.SlidingWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TimestampedValue;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestingPipeline {

  private static Iterable<Integer> parseList(String... entries) {
    List<Integer> all = new ArrayList<Integer>();
    for (String s : entries) {
      String[] countValue = s.split(":");
      all.add(Integer.parseInt(countValue[1]));
    }
    return all;
  }

  @Test
  public void test() {
    List<TimestampedValue<String>> words = Arrays.asList(
        TimestampedValue.of("xA", new Instant(1)),
        TimestampedValue.of("xA", new Instant(1)),
        TimestampedValue.of("xB", new Instant(1)),
        TimestampedValue.of("xB", new Instant(2)),
        TimestampedValue.of("xB", new Instant(2)));

    Pipeline p = TestPipeline.create();

    PCollection<KV<String, Integer>> input = p
        .apply(Create.of(words))
        .apply(new ReifyTimestamps<String>())
        .apply(new TestMap());

    PCollection<KV<String, Iterable<Integer>>> output =
        input.apply(Window.<KV<String, Integer>>into(SlidingWindows.of(new Duration(2))))
            .apply(GroupByKey.<String, Integer>create());

    PAssert.that(output).containsInAnyOrder(
        //[0,2)
        KV.of("xA", parseList(":1", ":1")),
        KV.of("xB", parseList("a:1")),

        //[1,3)
        KV.of("xA", parseList(":1", ":1")),
        KV.of("xB", parseList(":1", ":1", ":1")),

        //[2,4)
        KV.of("xB", parseList(":1", ":1"))
    );

    PCollection<KV<String, Integer>> testOut = output.apply(new TestingTransform());

    PAssert.that(testOut).containsInAnyOrder(
        //[0,2)
        KV.of("xA", 2),
        KV.of("xB", 1),

        //[1,3)
        KV.of("xA", 2),
        KV.of("xB", 3),

        //[2,4)
        KV.of("xB", 2)
    );

    p.run();
  }

  private static class ReifyTimestamps<T>
      extends PTransform<PCollection<TimestampedValue<T>>, PCollection<T>> {


    public PCollection<T> apply(PCollection<TimestampedValue<T>> input) {
      return input.apply(ParDo.of(new DoFn<TimestampedValue<T>, T>() {
        @ProcessElement
        public void processElement(ProcessContext c) {
          c.outputWithTimestamp(c.element().getValue(), c.element().getTimestamp());
        }
      }));
    }

    @Override
    public PCollection<T> expand(PCollection<TimestampedValue<T>> timestampedValuePCollection) {
      return null;
    }
  }

  private static class TestMap
      extends PTransform<PCollection<String>, PCollection<KV<String, Integer>>> {


    public PCollection<KV<String, Integer>> apply(PCollection<String> input) {
      return input.apply(ParDo.of(new DoFn<String, KV<String, Integer>>() {
        @ProcessElement
        public void processElement(ProcessContext c) {
          c.output(KV.of(c.element(), 1));
        }
      }));
    }

    @Override
    public PCollection<KV<String, Integer>> expand(PCollection<String> stringPCollection) {
      return null;
    }
  }

  private static class TestingTransform extends
      PTransform<PCollection<KV<String, Iterable<Integer>>>, PCollection<KV<String, Integer>>> {

    public PCollection<KV<String, Integer>> apply(
        PCollection<KV<String, Iterable<Integer>>> input) {
      return input.apply(ParDo.of(new DoFn<KV<String, Iterable<Integer>>, KV<String, Integer>>() {
        @ProcessElement
        public void processElement(ProcessContext ctx) {
          Integer sum = 0;
          Iterator<Integer> iterator = ctx.element().getValue().iterator();
          while (iterator.hasNext()) {
            sum += iterator.next();
          }
          ctx.output(KV.of(ctx.element().getKey(), sum));
        }
      }));
    }

    @Override
    public PCollection<KV<String, Integer>> expand(
        PCollection<KV<String, Iterable<Integer>>> kvpCollection) {
      return null;
    }
  }

}
