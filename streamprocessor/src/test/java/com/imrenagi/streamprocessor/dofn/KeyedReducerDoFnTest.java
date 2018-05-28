package com.imrenagi.streamprocessor.dofn;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.imrenagi.streamprocessor.model.RSVP;
import java.util.Arrays;
import java.util.List;
import org.apache.beam.sdk.transforms.DoFnTester;
import org.apache.beam.sdk.values.KV;
import org.junit.Test;

public class KeyedReducerDoFnTest {

  private KeyedReducerDoFn<RSVP> doFn = new KeyedReducerDoFn();
  private DoFnTester fnTester = DoFnTester.of(doFn);

  @Test
  public void shouldGetCorrectCount() throws Exception {
    RSVP rsvp1 = new RSVP(1l, 1525424484000l, "yes");
    RSVP rsvp2 = new RSVP(2l, 1525424485000l, "yes");
    RSVP rsvp3 = new RSVP(3l, 1525424489000l, "yes");

    List<RSVP> values = Arrays.asList(rsvp1, rsvp2, rsvp3);

    KV<String, Iterable<RSVP>> kv = KV.of("20180504", values);

    List<KV<String, Integer>> output = fnTester.processBundle(kv);

    KV<String, Integer> outElement = output.get(0);
    assertThat(outElement.getKey(), equalTo("20180504"));
    assertThat(outElement.getValue(), equalTo(3));
  }

}