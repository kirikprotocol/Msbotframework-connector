package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;


public class ImageAttachment extends Attachment {

  /**
   * URL of image.
   */
  private String url;

  public ImageAttachment() {
    super("image");
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
