package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

/**
 * Set of key-value pairs.
 *
 * Advantage of this section is that key and value properties will be rendered with default
 * style information with some delimiter between them.
 * So there is no need for developer to specify style information.
 */
public class Fact {

  private String key;

  private String value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
