package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateAttachment extends Attachment {

  @JsonProperty
  private Template payload;

  public TemplateAttachment() {
  }

  public TemplateAttachment(Template payload) {
    this.payload = payload;
  }

  public Template getPayload() {
    return payload;
  }

  public void setPayload(Template payload) {
    this.payload = payload;
  }
}
