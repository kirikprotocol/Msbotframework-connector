package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Channel account information for a conversation.
 */
public class ConversationAccount {

  /**
   * Is this a reference to a group.
   */
  @JsonProperty("isGroup")
  private Boolean isGroup;

  /**
   * Channel id for the user or bot on this channel
   * (Example: joe@smith.com, or @joesmith or 123456)
   */
  private String id;

  /**
   * Display friendly name.
   */
  private String name;

  public ConversationAccount() {}

  public ConversationAccount(String id) {
    this.id = id;
  }

  public Boolean getGroup() {
    return isGroup;
  }

  public void setGroup(Boolean group) {
    isGroup = group;
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
}
