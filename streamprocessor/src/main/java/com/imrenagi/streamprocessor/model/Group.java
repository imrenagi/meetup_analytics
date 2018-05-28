package com.imrenagi.streamprocessor.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;


@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class Group {

  public Group() {
  }

  @Nullable
  @SerializedName("group_topics")
  private List<GroupTopic> groupTopics;

  @Nullable
  @SerializedName(value = "group_city", alternate = {"city"})
  private String city;

  @Nullable
  @SerializedName(value = "group_country", alternate = {"country"})
  private String country;

  @Nullable
  @SerializedName("join_mode")
  private String joinMode;

  @Nullable
  @SerializedName(value = "group_id", alternate = {"id"})
  private Long id;

  @Nullable
  @SerializedName(value = "group_name", alternate = {"name"})
  private String name;

  @Nullable
  @SerializedName("group_lon")
  private Double lon;

  @Nullable
  @SerializedName(value = "group_urlname", alternate = {"urlname"})
  private String urlName;

  @Nullable
  @SerializedName("group_state")
  private String state;

  @Nullable
  @SerializedName("group_lat")
  private Double lat;
  @Nullable
  private GroupCategory category;

  public String getJoinMode() {
    return joinMode;
  }

  public GroupCategory getCategory() {
    return category;
  }

  public List<GroupTopic> getGroupTopics() {
    return groupTopics;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getLon() {
    return lon;
  }

  public String getUrlName() {
    return urlName;
  }

  public String getState() {
    return state;
  }

  public Double getLat() {
    return lat;
  }
}
