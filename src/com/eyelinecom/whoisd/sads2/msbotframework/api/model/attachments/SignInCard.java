package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * A card representing a request to Sign In.
 */
public class SignInCard extends Card<SignInCard> {

  /**
   * Text for Sign In request
   */
  private String text;

  /**
   * Action to use to perform Sign In
   */
  private CardAction[] buttons;

  public SignInCard() {}

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public CardAction[] getButtons() {
    return buttons;
  }

  public void setButtons(CardAction[] buttons) {
    this.buttons = buttons;
  }

  public void setButtons(List<CardAction> buttons) {
    this.setButtons(buttons.toArray(new CardAction[0]));
  }

  @Override
  @JsonIgnore
  protected final String getContentType() {
    //noinspection SpellCheckingInspection
    return "application/vnd.microsoft.card.signin";
  }
}
