package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use       = JsonTypeInfo.Id.NAME,
    include   = JsonTypeInfo.As.PROPERTY,
    property  = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TemplateAttachment.class,  name = "template"),
    @JsonSubTypes.Type(value = ImageAttachment.class,     name = "image")
})
public abstract class Attachment {

  private String type;

  public Attachment() {}

  public Attachment(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
