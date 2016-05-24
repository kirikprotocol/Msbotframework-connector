package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

/**
 * An action is a representation of information that a user can use to take action. On many channels
 * actions get mapped to buttons, while on other channels they simply become a list of options
 * displayed to the user.
 *
 * Regardless, a user can perform the action by clicking on a button or typing in the content as a
 * response.
 */
public class Action {

  /** Label of the action (button.) */
  private String title;

  /** Message which will be sent for the user when they click the button. */
  private String message;

  /**
   * Instead of a message when someone clicks on a button it should take them to a Url
   * (Not all channels support URL based actions.)
   * */
  private String url;

  /** Url to an image to put on the card (Not all channels will show an image.) */
  private String image;

  public Action(String title, String message) {
    this.title = title;
    this.message = message;
  }

  public Action() {}

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}