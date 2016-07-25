package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

public class MbfAttachment extends ApiType<MbfAttachment> {

  /**
   * MIME type / Content Type for the file
   */
  private String contentType;

  /**
   * Content Url.
   */
  private String contentUrl;

  /**
   * Embedded content
   */
  private Object content;

  /**
   * (OPTIONAL) The name of the attachment
   */
  private String name;

  /**
   * (OPTIONAL) Thumbnail associated with attachment.
   */
  private String thumbnailUrl;

  public MbfAttachment(String contentType, String contentUrl) {
    this.contentType = contentType;
    this.contentUrl = contentUrl;
  }

  public MbfAttachment() {}

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getContentUrl() {
    return contentUrl;
  }

  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
}
