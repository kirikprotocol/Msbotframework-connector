package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ApiType;

public class CardImage extends ApiType<CardImage> {

  /**
   * URL Thumbnail image for major content property.
   */
  private String url;

  /**
   * Image description intended for screen readers
   */
  private String alt;

  /**
   * Action assigned to specific Attachment,
   * e.g. navigate to specific URL or play/open media content.
   */
  private CardAction tap;

  public CardImage() {}

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getAlt() {
    return alt;
  }

  public void setAlt(String alt) {
    this.alt = alt;
  }

  public CardAction getTap() {
    return tap;
  }

  public void setTap(CardAction tap) {
    this.tap = tap;
  }
}
