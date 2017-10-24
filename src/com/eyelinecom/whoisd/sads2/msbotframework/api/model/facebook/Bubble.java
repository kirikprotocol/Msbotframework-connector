package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Bubble {

  /**
   * Bubble title. Mandatory.
   */
  @JsonProperty(required = true)
  private String title;

  /**
   * URL that is opened when bubble is tapped. Optional.
   */
  @JsonProperty("item_url")
  private String itemUrl;

  /**
   * Bubble image. Optional.
   */
  @JsonProperty("image_url")
  private String imageUrl;

  /**
   * Default action
   */
  @JsonProperty("default_action")
  private DefaultAction defaultAction;

  /**
   * Bubble subtitle. Optional.
   */
  private String subtitle;

  /**
   * Set of buttons that appear as call-to-actions. Optional.
   */
  private Button[] buttons;

  public Bubble() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getItemUrl() {
    return itemUrl;
  }

  public void setItemUrl(String itemUrl) {
    this.itemUrl = itemUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public Button[] getButtons() {
    return buttons;
  }

  public DefaultAction getDefaultAction() {
    return defaultAction;
  }

  public void setDefaultAction(DefaultAction defaultAction) {
    this.defaultAction = defaultAction;
  }

  public void setButtons(Button[] buttons) {
    this.buttons = buttons;
  }

  @JsonProperty
  @JsonIgnore
  public void setButtons(List<Button> buttons) {
    setButtons(buttons.toArray(new Button[0]));
  }

  public static class DefaultAction {

    @JsonProperty("type")
    private String type;

    @JsonProperty("url")
    private String url;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }

}
