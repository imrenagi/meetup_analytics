package com.imrenagi;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class MeetupApiConfiguration extends Configuration {
  @NotEmpty
  private String template;

  @NotEmpty
  private String defaultName = "Stranger";

  @Valid
  @NotNull
  private String bigTableProjectId;

  @Valid
  @NotNull
  private String bigTableInstanceId;

  @Valid
  private String rsvpCountTable;

  public String getRsvpCountTable() {
    return rsvpCountTable;
  }

  public void setRsvpCountTable(String rsvpCountTable) {
    this.rsvpCountTable = rsvpCountTable;
  }

  public String getBigTableProjectId() {
    return bigTableProjectId;
  }

  public void setBigTableProjectId(String bigTableProjectId) {
    this.bigTableProjectId = bigTableProjectId;
  }

  public String getBigTableInstanceId() {
    return bigTableInstanceId;
  }

  public void setBigTableInstanceId(String bigTableInstanceId) {
    this.bigTableInstanceId = bigTableInstanceId;
  }

  @JsonProperty
  public String getTemplate() {
    return template;
  }

  @JsonProperty
  public void setTemplate(String template) {
    this.template = template;
  }

  @JsonProperty
  public String getDefaultName() {
    return defaultName;
  }

  @JsonProperty
  public void setDefaultName(String name) {
    this.defaultName = name;
  }
}
