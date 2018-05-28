package com.imrenagi.streamprocessor.utils.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateHelper {

  public static String toYyyyMmDd(DateTime dateTime) {
    DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyyMMdd");
    DateTime utcTime = dateTime.toDateTime(DateTimeZone.UTC);
    return dtfOut.print(utcTime);
  }

  public static String toHHmm(DateTime dateTime) {
    DateTimeFormatter dtfOut = DateTimeFormat.forPattern("HHmm");
    DateTime utcTime = dateTime.toDateTime(DateTimeZone.UTC);
    return dtfOut.print(utcTime);
  }

  private static DateTime roundDate(final DateTime dateTime, final int minutes) {
    if (minutes < 1 || 60 % minutes != 0) {
      throw new IllegalArgumentException("minutes must be a factor of 60");
    }

    final DateTime hour = dateTime.hourOfDay().roundFloorCopy();
    final long millisSinceHour = new Duration(hour, dateTime).getMillis();
    final int roundedMinutes = ((int) Math.round(
        millisSinceHour / 60000.0 / minutes)) * minutes;
    return hour.plusMinutes(roundedMinutes);
  }

  public static String roundDateToHHmm(final DateTime dateTime, final int minutes) {
    return toHHmm(roundDate(dateTime, minutes));
  }

}
