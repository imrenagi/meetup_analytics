package com.imrenagi.streamprocessor.pipeline;

import com.imrenagi.streamprocessor.model.RSVP;
import java.util.Arrays;
import java.util.List;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;

public class RSVPBatchPipelineTest {

  @Rule
  public final transient TestPipeline p = TestPipeline.create();

  @Test
  public void shouldReturnCorrectCount() {

    RSVP rsvp1 = new RSVP(1l, 1525424484000l, "yes");
    RSVP rsvp2 = new RSVP(2l, 1525424485000l, "yes");
    RSVP rsvp3 = new RSVP(3l, 1525424489000l, "yes");

    List<RSVP> values = Arrays.asList(rsvp1, rsvp2, rsvp3);

    KV<String, Integer> kv = KV.of("20180504\t0900", 3);

    PCollection<KV<String, Integer>> output = p.apply(Create.of(values))
        .apply("test", new RSVPBatchPipeline.BatchTransform());

    PAssert.that(output)
        .containsInAnyOrder(kv);

    p.run();

  }

}
