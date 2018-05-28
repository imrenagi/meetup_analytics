package com.imrenagi.streamprocessor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class RSVP {

  public RSVP() {
  }

  @Nullable
  @SerializedName("rsvp_id")
  private Long id;

  @Nullable
  @SerializedName("mtime")
  private Long timestamp;

  @Nullable
  private String response;

  @Nullable
  @SerializedName("guests")
  private Integer numGuest;

  @Nullable
  private String visibility;

  @Nullable
  private Venue venue;

  @Nullable
  private Member member;

  @Nullable
  private Event event;

  @Nullable
  private Group group;

  public RSVP(Long id, Long timestamp, String response) {
    this.id = id;
    this.timestamp = timestamp;
    this.response = response;
  }

  public Group getGroup() {
    return group;
  }

  public Event getEvent() {
    return event;
  }

  public Member getMember() {
    return member;
  }

  public Long getId() {
    return id;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public String getResponse() {
    return response;
  }

  public Integer getNumGuest() {
    return numGuest;
  }

  public String getVisibility() {
    return visibility;
  }

  public Venue getVenue() {
    return venue;
  }
}
