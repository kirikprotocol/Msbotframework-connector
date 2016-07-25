package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ApiType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;

public class CardAction extends ApiType<CardAction> {

  /**
   * Defines the type of action implemented by this button.
   *
   * Possible values for this property include:
   * <ul>
   *   <li>'openUrl',
   *   <li>'imBack',
   *   <li>'postBack',
   *   <li>'playAudio',
   *   <li>'playVideo',
   *   <li>'showImage',
   *   <li>'downloadFile'
   * </ul>
   */
  @JsonSerialize(using = ActionType.Serializer.class)
  @JsonDeserialize(using = ActionType.Deserializer.class)
  private ActionType type;

  /**
   * Text description which appear on the button.
   */
  private String title;

  /**
   * URL Picture which will appear on the button, next to text label.
   *
   * Media hosted on 3rd party domains will be automatically hosted on
   * auth protected CDN. Auth protection will be used to ensure that content
   * uploaded to Skype CDN will only be accessible by Microsoft Clients.
   */
  private String image;

  /**
   * Supplementary parameter for action.
   * Content of this property depends on the ActionType.
   */
  private String value;

  public CardAction() {}

  public ActionType getType() {
    return type;
  }

  public void setType(ActionType type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("type", type)
        .add("title", title)
        .add("image", image)
        .add("value", value)
        .toString();
  }
}
