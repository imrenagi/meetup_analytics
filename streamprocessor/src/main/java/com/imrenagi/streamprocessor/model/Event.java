package com.imrenagi.streamprocessor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class Event {

  public Event() {
  }

  @Nullable
  @SerializedName("event_name")
  private String name;

  @Nullable
  @SerializedName("event_id")
  private String id;

  @Nullable
  @SerializedName("time")
  private Long timestamp;

  @Nullable
  @SerializedName("event_url")
  private String url;

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public String getUrl() {
    return url;
  }
}
