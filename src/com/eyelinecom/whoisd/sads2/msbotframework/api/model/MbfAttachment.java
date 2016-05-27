package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

/**
 * Three types of message attachments are currently supported (_action_, _image/file_,
 * and _card_.)
 * <br/>
 * The valid fields very by attachment type:
 * <ul>
 *  <li>action.
 *    An action for the user to take (typically rendered as a button.)
 *    Valid fields: [actions].
 *  <li>image/file.
 *    An image or a file to send the user.
 *    Valid fields: [contentType], [contentUrl].
 *  <li>card.
 *    A rich card displayed to the user.
 *    Valid fields: [actions], [fallbackText], [title], [titleLink], [text], [thumbnailUrl].
 * </ul>
 *
 * <p>
 * An image:
 * <pre><code>{
 *     contentType: 'image/png',
 *     contentUrl: 'https://upload.wikimedia.org/wikipedia/en/a/a6/Bender_Rodriguez.png'
 * }
 * </code></pre>
 * </p>
 *
 * <p>
 * A card with actions:
 * <pre><code>{
 *     text: 'Pick one:',
 *     actions: [
 *         { title: "Willy's Cheeseburger", message: "CB" },
 *         { title: "Curley Fries", message: "F" },
 *         { title: "Chocolate Shake", message: "S" }
 *     ]
 * }
 * </code></pre>
 * </p>
 */
public class MbfAttachment {

  /**
   * List of actions to map to buttons in the clients UI. Valid for _action_ & _card_ attachments.
   */
  private Action[] actions;

  /**
   * The mimetype/ContentType of the [contentUrl](#contenturl). Valid for _image/file_ attachments.
   */
  private String contentType;

  /**
   * A link to the actual file. Valid for _image/file_ attachments.
   */
  private String contentUrl;

  /**
   * Fallback text used for downlevel clients, should be simple markup with links. Valid for _card_ attachments.
   */
  private String fallbackText;

  /**
   * Title of the card. Valid for _card_ attachments.
   */
  private String title;

  /**
   * Link for the [title](#title). Valid for _card_ attachments.
   */
  private String titleLink;

  /**
   * Text of the card. Valid for _card_ attachments.
   */
  private String text;

  /**
   * Image to put on the card. Valid for _card_ attachments.
   */
  private String thumbnailUrl;

  public MbfAttachment() {
  }

  /**
   * Action.
   */
  public MbfAttachment(Action[] actions) {
    this.actions = actions;
  }

  /**
   * Image/file.
   */
  public MbfAttachment(String contentType, String contentUrl) {
    this.contentType = contentType;
    this.contentUrl = contentUrl;
  }

  public Action[] getActions() {
    return actions;
  }

  public void setActions(Action[] actions) {
    this.actions = actions;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getContentUrl() {
    return contentUrl;
  }

  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }

  public String getFallbackText() {
    return fallbackText;
  }

  public void setFallbackText(String fallbackText) {
    this.fallbackText = fallbackText;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitleLink() {
    return titleLink;
  }

  public void setTitleLink(String titleLink) {
    this.titleLink = titleLink;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
}
