package com.imrenagi.streamprocessor.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class GroupCategory {

  public GroupCategory() {
  }

  @Nullable
  private String name;
  @Nullable
  private Long id;
  @Nullable
  @SerializedName("shortname")
  private String shortName;

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public String getShortName() {
    return shortName;
  }
}
