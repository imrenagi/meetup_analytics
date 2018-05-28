package com.imrenagi.streamprocessor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class Venue {

  public Venue() {
  }

  @Nullable
  @SerializedName(value = "venue_name", alternate = {"name"})
  private String name;

  @Nullable
  @SerializedName("venue_id")
  private Long venueId;

  @Nullable
  private Double lat;
  @Nullable
  private Double lon;

//  private String country;
//  private String city;
//
//  @SerializedName("address_1")
//  private String address;

  public Venue(String name, Double lat, Double lon, Long venueId) {
    this.name = name;
    this.lat = lat;
    this.lon = lon;
    this.venueId = venueId;
  }

  public String getName() {
    return name;
  }

  public Double getLat() {
    return lat;
  }

  public Double getLon() {
    return lon;
  }

  public Long getVenueId() {
    return venueId;
  }
//
//  public String getCountry() {
//    return country;
//  }
//
//  public String getCity() {
//    return city;
//  }
//
//  public String getAddress() {
//    return address;
//  }
}
