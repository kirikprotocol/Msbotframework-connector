package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ThumbnailCard extends Card<ThumbnailCard> {

  /**
   * Title of the card
   */
  private String title;

  /**
   * Subtitle of the card
   */
  private String subtitle;

  /**
   * Text for the card
   */
  private String text;

  private CardImage[] images;

  /**
   * Set of actions applicable to the current card.
   */
  private CardAction[] buttons;

  /**
   * This action will be activated when user taps on the card itself
   */
  private CardAction tap;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public CardImage[] getImages() {
    return images;
  }

  public void setImages(CardImage[] images) {
    this.images = images;
  }

  @JsonIgnore
  public void setImages(List<CardImage> images) {
    this.setImages(images.toArray(new CardImage[0]));
  }

  public CardAction[] getButtons() {
    return buttons;
  }

  public void setButtons(CardAction[] buttons) {
    this.buttons = buttons;
  }

  @JsonIgnore
  public void setButtons(List<CardAction> buttons) {
    this.setButtons(buttons.toArray(new CardAction[0]));
  }

  public CardAction getTap() {
    return tap;
  }

  public void setTap(CardAction tap) {
    this.tap = tap;
  }

  @Override
  @JsonIgnore
  protected final String getContentType() {
    return "application/vnd.microsoft.card.thumbnail";
  }
}
