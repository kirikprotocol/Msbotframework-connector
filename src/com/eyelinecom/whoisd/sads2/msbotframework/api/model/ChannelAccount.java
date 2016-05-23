package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Information needed to route a message.
 */
public class ChannelAccount {

  /**
   * Optional.
   * Display friendly name of the user.
   */
  private String name;

  /**
   * Channel Id that the channelAccount is to be communicated with (Example: GroupMe.)
   */
  @JsonSerialize(using = ChannelType.Serializer.class)
  @JsonDeserialize(using = ChannelType.Deserializer.class)
  private ChannelType channelId;

  /**
   * Channel Address for the channelAccount (Example: @thermous.)
   */
  private String address;

  /**
   * Optional.
   * Id - global intercom id.
   */
  private String id;

  /**
   * Optional.
   * Is this account id an bot?
   */
  @JsonProperty("isBot")
  private Boolean bot;

  public ChannelAccount() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ChannelType getChannelId() {
    return channelId;
  }

  public void setChannelId(ChannelType channelId) {
    this.channelId = channelId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getBot() {
    return bot;
  }

  public void setBot(Boolean bot) {
    this.bot = bot;
  }
}
