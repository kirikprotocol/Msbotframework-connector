package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Template attachments have 3 different types of templates: generic, button and receipt.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use       = JsonTypeInfo.Id.NAME,
    include   = JsonTypeInfo.As.PROPERTY,
    property  = "template_type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = GenericTemplate.class,  name = "generic"),
    // And other template types should go here.
})
public abstract class Template {

  @JsonProperty("template_type")
  private String templateType;

  public Template() {
  }

  public Template(String templateType) {
    this.templateType = templateType;
  }

  public String getTemplateType() {
    return templateType;
  }

  public void setTemplateType(String templateType) {
    this.templateType = templateType;
  }
}
