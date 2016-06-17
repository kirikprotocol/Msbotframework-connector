package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Button {

  /**
   * Value is {@code web_url} or {@code postback}.
   */
  @JsonProperty(required = true)
  private String type;

  /**
   * Button title
   */
  @JsonProperty(required = true)
  private String title;

  /**
   * For {@code web_url} buttons, this URL is opened in a mobile browser when the button is tapped.
   */
  private String url;

  /**
   * For {@code postback} buttons, this data will be sent back to you via webhook.
   */
  private String payload;

  public Button(String type, String title) {
    this.type = type;
    this.title = title;
  }

  public Button() {
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public static Button url(String title, String url) {
    final Button btn = new Button("web_url", title);
    btn.setUrl(url);
    return btn;
  }

  public static Button postback(String title) {
    final Button btn = new Button("postback", title);
    btn.setPayload(title);
    return btn;
  }
}
