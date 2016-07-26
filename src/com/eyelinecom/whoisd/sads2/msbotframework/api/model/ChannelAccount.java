package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.google.common.base.MoreObjects;

/**
 * Channel account information needed to route a message.
 */
public class ChannelAccount {

  /**
   * Channel id for the user or bot on this channel
   * (Example: joe@smith.com, or @joesmith or 123456)
   */
  private String id;

  /**
   * Display friendly name.
   */
  private String name;

  public ChannelAccount() {}

  public ChannelAccount(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public ChannelAccount(String id) {
    this(id, null);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("id", id)
        .add("name", name)
        .toString();
  }
}
