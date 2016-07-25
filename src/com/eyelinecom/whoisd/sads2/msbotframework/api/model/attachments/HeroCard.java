package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.eyelinecom.whoisd.sads2.msbotframework.api.model.MbfAttachment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class HeroCard extends Card<HeroCard> {

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

  @JsonProperty
  private CardImage[] images;

  /**
   * Set of actions applicable to the current card
   */
  @JsonProperty
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

  /**
   * Generates buttons from options.
   */
  public static MbfAttachment fromOptions(String text, List<String> buttons) {
    final List<CardAction> actions = new ArrayList<>();
    for (String button : buttons) {
      final CardAction action = new CardAction();
      action.setTitle(button);
      action.setType(ActionType.IM_BACK);
      action.setValue(button);
      actions.add(action);
    }

    final HeroCard card = new HeroCard();
    card.setText(text);
    card.setButtons(actions);
    return card.toAttachment();
  }

  @Override
  @JsonIgnore
  protected final String getContentType() {
    return "application/vnd.microsoft.card.hero";
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("title", title)
        .add("subtitle", subtitle)
        .add("text", text)
        .add("images", images)
        .add("buttons", buttons)
        .add("tap", tap)
        .toString();
  }
}
