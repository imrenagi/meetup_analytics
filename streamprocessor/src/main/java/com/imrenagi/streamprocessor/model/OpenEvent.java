package com.imrenagi.streamprocessor.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class OpenEvent {

  public OpenEvent() {
  }

  @Nullable
  @SerializedName("utc_offset")
  private Long utcOffset;

  @Nullable
  private Venue venue;

  @Nullable
  @SerializedName("rsvp_limit")
  private Integer rsvpLimit;

  @Nullable
  @SerializedName("venue_visibility")
  private String venueVisibility;

  @Nullable
  @SerializedName("visibility")
  private String visibility;

  @Nullable
  @SerializedName("maybe_rsvp_count")
  private Integer maybeRsvpCount;

  @Nullable
  @SerializedName("description")
  private String description;

  @Nullable
  @SerializedName("mtime")
  private Long createdTimestamp;

  @Nullable
  @SerializedName("event_url")
  private String eventUrl;

  @Nullable
  @SerializedName("yes_rsvp_count")
  private Integer yesRsvpCount;

  @Nullable
  @SerializedName("duration")
  private Long duration;

  @Nullable
  @SerializedName("payment_required")
  private String paymentRequired;

  @Nullable
  @SerializedName("name")
  private String name;

  @Nullable
  @SerializedName("id")
  private Long id;

  @Nullable
  @SerializedName("time")
  private Long eventTime;

  @Nullable
  private Group group;

  @Nullable
  private String status;

  public Long getUtcOffset() {
    return utcOffset;
  }

  public Venue getVenue() {
    return venue;
  }

  public Integer getRsvpLimit() {
    return rsvpLimit;
  }

  public String getVenueVisibility() {
    return venueVisibility;
  }

  public String getVisibility() {
    return visibility;
  }

  public Integer getMaybeRsvpCount() {
    return maybeRsvpCount;
  }

  public String getDescription() {
    return description;
  }

  public Long getCreatedTimestamp() {
    return createdTimestamp;
  }

  public String getEventUrl() {
    return eventUrl;
  }

  public Integer getYesRsvpCount() {
    return yesRsvpCount;
  }

  public Long getDuration() {
    return duration;
  }

  public String getPaymentRequired() {
    return paymentRequired;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public Long getEventTime() {
    return eventTime;
  }

  public Group getGroup() {
    return group;
  }

  public String getStatus() {
    return status;
  }
}
