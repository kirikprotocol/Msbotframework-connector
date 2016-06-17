package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GenericTemplate extends Template {

  /**
   * Data for each bubble in a message
   */
  private Bubble[] elements;

  public GenericTemplate() {
  }

  public GenericTemplate(Bubble[] elements) {
    setElements(elements);
  }

  public GenericTemplate(List<Bubble> elements) {
    setElements(elements);
  }

  public Bubble[] getElements() {
    return elements;
  }

  public void setElements(Bubble[] elements) {
    this.elements = elements;
  }

  @JsonProperty("elements")
  @JsonIgnore
  public void setElements(List<Bubble> elements) {
    setElements(elements.toArray(new Bubble[0]));
  }
}
