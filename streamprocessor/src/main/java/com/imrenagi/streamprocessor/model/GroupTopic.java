package com.imrenagi.streamprocessor.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.apache.avro.reflect.Nullable;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
@JsonInclude(Include.NON_NULL)
public class GroupTopic {

  public GroupTopic() {
  }

  @Nullable
  @SerializedName("urlkey")
  private String key;

  @Nullable
  @SerializedName("topic_name")
  private String topic;

  public GroupTopic(String key, String topic) {
    this.key = key;
    this.topic = topic;
  }

  public String getKey() {
    return key;
  }

  public String getTopic() {
    return topic;
  }


}
