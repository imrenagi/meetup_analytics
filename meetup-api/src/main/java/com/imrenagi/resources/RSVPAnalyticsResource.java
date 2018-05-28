package com.imrenagi.resources;

import java.io.IOException;
import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


@Produces(MediaType.APPLICATION_JSON)
public class RSVPAnalyticsResource {

  private Table table;

  public RSVPAnalyticsResource(Table table) {
    this.table = table;
  }

  @GET
  @Path("/isHealty")
  public String isHealthy() {
    return "ok";
  }

  @GET
  @Path("/rsvp/count")
  public String getRSVPCount(@QueryParam("start_date") String startDate) throws IOException {

    final int NUM_COLUM = 96;

    DateTimeFormatter df = DateTimeFormat.forPattern("dd MM yyyy HH:mm:ss.SSS Z");
    DateTime temp = df.withOffsetParsed().parseDateTime("01 01 2012 00:00:00.000 +0000");
    DateTime dateTime = new DateTime(temp);

    ResultScanner results = this.table
        .getScanner(new Scan(Bytes.toBytes(startDate)));

    StringBuilder builder = new StringBuilder();

    Iterator<Result> iter = results.iterator();
    while (iter.hasNext()) {
      Result result = iter.next();
      for (int i = 0; i < NUM_COLUM; i++) {
        String time = toHHmm(dateTime);
        String rowKey = Bytes.toString(result.getRow());
        try {
          Integer value = Bytes.toInt(result.getValue(Bytes.toBytes("count"), Bytes.toBytes(time)));
          if (value != null) {
            builder.append(rowKey + ":" + time + " = " + value).append("\n");
          }
        } catch (Exception e) {
        }
        dateTime = dateTime.plusMinutes(15);
      }
    }
    return builder.toString();
  }

  private String toHHmm(DateTime dateTime) {
    DateTimeFormatter dtfOut = DateTimeFormat.forPattern("HHmm");
    DateTime utcTime = dateTime.toDateTime(DateTimeZone.UTC);
    return dtfOut.print(utcTime);
  }

}
