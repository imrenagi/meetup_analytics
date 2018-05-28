package com.imrenagi.streamprocessor.dofn;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.imrenagi.streamprocessor.model.RSVP;
import com.imrenagi.streamprocessor.utils.date.DateHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.beam.sdk.transforms.DoFnTester;
import org.apache.beam.sdk.values.KV;
import org.joda.time.DateTime;
import org.junit.Test;

public class KeyedRSVPDoFnTest {

  private KeyedRSVPDoFn doFn = new KeyedRSVPDoFn();
  private DoFnTester fnTester = DoFnTester.of(doFn);

  @Test
  public void shouldGetCorrectKey() throws Exception {
    RSVP rsvp = new RSVP(1l, 1525424484000l, "yes");

    List<RSVP> input = new ArrayList<>();
    input.add(rsvp);

    List<KV<String, RSVP>> output = fnTester.processBundle(input);

    KV<String, RSVP> outElement = output.get(0);

    DateTime now = new DateTime();

    assertThat(outElement.getKey(), equalTo(DateHelper.toYyyyMmDd(now)));
    assertThat(outElement.getValue().getId(), equalTo(1l));
    assertThat(outElement.getValue().getResponse(), equalTo("yes"));
  }

}