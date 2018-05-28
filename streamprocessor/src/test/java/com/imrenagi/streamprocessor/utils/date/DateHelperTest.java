package com.imrenagi.streamprocessor.utils.date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class DateHelperTest {

  @Test
  public void shouldCreateCorrectyyyymmdd() {
    String dateTime = "11/15/2013 06:00:00+0000";
    DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ssZ");

    DateTime time = dtf.parseDateTime(dateTime);

    String output = DateHelper.toYyyyMmDd(time);
    assertThat(output, equalTo("20131115"));
  }

  @Test
  public void shouldCreateCorrectyyyymmddWithTimezone() {
    String dateTime = "11/15/2013 06:00:00+0700";
    DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ssZ");

    DateTime time = dtf.parseDateTime(dateTime);

    String output = DateHelper.toYyyyMmDd(time);
    assertThat(output, equalTo("20131114"));
  }

  @Test
  public void shouldCreateCorrectyyyymmddWithTimestampMillis() {
    DateTime dateTime = new DateTime(1525421002000l);

    String output = DateHelper.toYyyyMmDd(dateTime);
    assertThat(output, equalTo("20180504"));
  }

  @Test
  public void shouldCreateCorrecthhmm() {
    String dateTime = "11/15/2013 06:00:00+0700";
    DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ssZ");

    DateTime time = dtf.parseDateTime(dateTime);

    String output = DateHelper.toHHmm(time);
    assertThat(output, equalTo("2300"));
  }

  @Test
  public void shouldRoundToUpper15Minutes() {
    DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ssZ");
    int window = 15;

    assertThat(DateHelper.roundDateToHHmm(dtf.parseDateTime("11/15/2013 06:10:00+0000"), window),
        equalTo("0615"));
    assertThat(DateHelper.roundDateToHHmm(dtf.parseDateTime("11/15/2013 06:16:00+0000"), window),
        equalTo("0615"));
    assertThat(DateHelper.roundDateToHHmm(dtf.parseDateTime("11/15/2013 06:35:00+0000"), window),
        equalTo("0630"));
    assertThat(DateHelper.roundDateToHHmm(dtf.parseDateTime("11/15/2013 06:55:00+0000"), window),
        equalTo("0700"));
  }

}