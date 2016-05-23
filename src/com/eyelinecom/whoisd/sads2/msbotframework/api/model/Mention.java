package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

/**
 * Mention information.
 */
public class Mention {

  /**
   * Optional.
   *
   * The mentioned user.
   */
  private ChannelAccount mentioned;

  /**
   * Sub Text which represents the mention (can be null or empty.)
   */
  private String text;

  public Mention() {}

  public ChannelAccount getMentioned() {
    return mentioned;
  }

  public void setMentioned(ChannelAccount mentioned) {
    this.mentioned = mentioned;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
