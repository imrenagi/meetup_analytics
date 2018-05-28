package com.imrenagi.streamprocessor.dofn;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


import com.imrenagi.streamprocessor.model.GroupTopic;
import com.imrenagi.streamprocessor.model.RSVP;
import java.util.ArrayList;
import java.util.List;

import org.apache.beam.sdk.transforms.DoFnTester;
import org.hamcrest.Matchers;
import org.junit.Test;

public class RsvpParserDoFnTest {

  private RsvpParserDoFn doFn = new RsvpParserDoFn();
  private DoFnTester fnTester = DoFnTester.of(doFn);

  @Test
  public void shouldConvertStringToRsvpObject() throws Exception {
    String test = "{\"venue\":{\"venue_name\":\"Drinking fountain next to bridge on walking path\",\"lon\":-93.304031,\"lat\":44.951366,\"venue_id\":24348919},\"visibility\":\"public\",\"response\":\"no\",\"guests\":0,\"member\":{\"member_id\":12962773,\"photo\":\"https:\\/\\/secure.meetupstatic.com\\/photos\\/member\\/3\\/3\\/c\\/b\\/thumb_273193259.jpeg\",\"member_name\":\"Goran\"},\"rsvp_id\":1725979366,\"mtime\":1525258081878,\"event\":{\"event_name\":\"Celebrate the new spring!  Come for an evening walk around Isles and Calhoun.\",\"event_id\":\"250221668\",\"time\":1525302000000,\"event_url\":\"https:\\/\\/www.meetup.com\\/Twin-Cities-Hikers\\/events\\/250221668\\/\"},\"group\":{\"group_topics\":[{\"urlkey\":\"hiking\",\"topic_name\":\"Hiking\"},{\"urlkey\":\"adventure\",\"topic_name\":\"Adventure\"},{\"urlkey\":\"social\",\"topic_name\":\"Social\"},{\"urlkey\":\"volunteer-trail-maintenance\",\"topic_name\":\"Volunteer Trail Maintenance\"},{\"urlkey\":\"outdoor-fitness\",\"topic_name\":\"Outdoor  Fitness\"},{\"urlkey\":\"snowshoeing\",\"topic_name\":\"Snowshoeing\"},{\"urlkey\":\"socialnetwork\",\"topic_name\":\"Social Networking\"},{\"urlkey\":\"wilderness-hiking\",\"topic_name\":\"Wilderness Hiking\"},{\"urlkey\":\"newintown\",\"topic_name\":\"New In Town\"},{\"urlkey\":\"excercise\",\"topic_name\":\"Exercise\"},{\"urlkey\":\"camping\",\"topic_name\":\"Camping\"}],\"group_city\":\"Minneapolis\",\"group_country\":\"us\",\"group_id\":233380,\"group_name\":\"Twin Cities Hiking Meetup\",\"group_lon\":-93.23,\"group_urlname\":\"Twin-Cities-Hikers\",\"group_state\":\"MN\",\"group_lat\":44.9}}\n"
        + "\n";

    List<String> input = new ArrayList<String>();
    input.add(test);

    List<RSVP> rsvps = fnTester.processBundle(test);

    assertEquals(rsvps.size(), 1);

    RSVP rsvp = rsvps.get(0);

    assertThat(rsvp.getId(), is(1725979366l));
    assertThat(rsvp.getTimestamp(), is(1525258081878l));
    assertThat(rsvp.getNumGuest(), is(0));
    assertThat(rsvp.getVisibility(), is("public"));
    assertThat(rsvp.getResponse(), is("no"));

    assertThat(rsvp.getVenue().getName(), is("Drinking fountain next to bridge on walking path"));
    assertThat(rsvp.getVenue().getLon(), closeTo(-93.304031, 0.1));
    assertThat(rsvp.getVenue().getLat(), closeTo(44.951366, 0.1));
    assertThat(rsvp.getVenue().getVenueId(), is(24348919l));

    assertThat(rsvp.getMember().getId(), is(12962773l));
    assertThat(rsvp.getMember().getName(), is("Goran"));
    assertThat(rsvp.getMember().getPhoto(), is("https://secure.meetupstatic.com/photos/member/3/3/c/b/thumb_273193259.jpeg"));

    assertThat(rsvp.getEvent().getId(), is("250221668"));
    assertThat(rsvp.getEvent().getName(), is("Celebrate the new spring!  Come for an evening walk around Isles and Calhoun."));
    assertThat(rsvp.getEvent().getTimestamp(), is(1525302000000l));
    assertThat(rsvp.getEvent().getUrl(), is("https://www.meetup.com/Twin-Cities-Hikers/events/250221668/"));

    assertThat(rsvp.getGroup().getGroupTopics(), Matchers.<GroupTopic>hasSize(11));
    assertThat(rsvp.getGroup().getId(), is(233380l));
    assertThat(rsvp.getGroup().getCity(), is("Minneapolis"));
    assertThat(rsvp.getGroup().getCountry(), is("us"));
    assertThat(rsvp.getGroup().getLat(), closeTo(44.9, 0.01));
    assertThat(rsvp.getGroup().getLon(), closeTo(-93.23, 0.01));
    assertThat(rsvp.getGroup().getUrlName(), is("Twin-Cities-Hikers"));
    assertThat(rsvp.getGroup().getName(), is("Twin Cities Hiking Meetup"));

  }


}