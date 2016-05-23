package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

/**
 * Bot state in BotConnector persistent storage.
 */
public class BotData<T> extends ApiType<BotData> {

  /**
   * Optional. State data.
   */
  private T data;

  /**
   * Optional.
   */
  private String eTag;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String geteTag() {
    return eTag;
  }

  public void seteTag(String eTag) {
    this.eTag = eTag;
  }
}
