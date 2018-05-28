package com.imrenagi.streamprocessor.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class Member {

  public Member() {
  }

  @Nullable
  @SerializedName("member_id")
  private Long id;

  @Nullable
  @SerializedName("photo")
  private String photo;

  @Nullable
  @SerializedName("member_name")
  private String name;

  public Long getId() {
    return id;
  }

  public String getPhoto() {
    return photo;
  }

  public String getName() {
    return name;
  }
}
