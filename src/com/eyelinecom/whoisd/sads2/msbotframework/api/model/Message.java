package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A communication message received from a User or sent out of band from a Bot.
 */
public class Message extends ApiType<Message> {

  /**
   * What kind of message is this.
   */
  @JsonSerialize(using = MessageType.Serializer.class)
  @JsonDeserialize(using = MessageType.Deserializer.class)
  private MessageType type;

  /**
   * Bot.Connector Id for the message (always assigned by transport.)
   */
  private String id;

  /**
   * Bot.Connector ConverationId id for the conversation (always assigned by transport.)
   */
  private String conversationId;

  /**
   * Timestamp of when the message was created.
   */
  private String created;

  /** (if translated) The OriginalText of the message. */
  private String sourceText;

  /** (if translated) The language of the OriginalText of the message. */
  @JsonSerialize(using = Language.Serializer.class)
  @JsonDeserialize(using = Language.Deserializer.class)
  private Language sourceLanguage;

  /**
   * The language that the Text is expressed in.
   */
  @JsonSerialize(using = Language.Serializer.class)
  @JsonDeserialize(using = Language.Deserializer.class)
  private Language language;

  /**
   * The text of the message (this will be target language depending on flags and destination.)
   */
  private String text;

  /**
   * Array of attachments that can be anything.
   */
  private Attachment[] attachments;

  /**
   * ChannelIdentity that sent the message.
   */
  private ChannelAccount from;

  /**
   * ChannelIdentity the message is sent to.
   */
  private ChannelAccount to;

  /**
   * Account to send replies to (for example, a group account that the message was part of.)
   */
  private ChannelAccount replyTo;

  /**
   * The message Id that this message is a reply to.
   */
  private String replyToMessageId;

  /**
   * List of ChannelAccounts in the conversation
   * (NOTE: this is not for delivery means but for information.)
   */
  private ChannelAccount[] participants;

  /**
   * Total participants in the conversation.  2 means 1:1 message.
   */
  private Integer totalParticipants;

  /**
   * Array of mentions from the channel context.
   */
  private Mention[] mentions;

  /**
   * Place in user readable format: For example: "Starbucks, 140th Ave NE, Bellevue, WA"
   * */
  private String place;

  /**
   * Channel Message Id.
   */
  private String channelMessageId;

  /**
   * Channel Conversation Id.
   */
  private String channelConversationId;

  /**
   * Channel specific properties.
   * For example: Email channel may pass the Subject field as a property.
   */
  private Object channelData;

  /**
   * Location information (see https://dev.onedrive.com/facets/location_facet.htm)
   */
  private Location location;

  /**
   * Hashtags for the message.
   */
  private String[] hashtags;

  /**
   * Required to modify messages when manually reading from a store.
   */
  private String eTag;

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getConversationId() {
    return conversationId;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getSourceText() {
    return sourceText;
  }

  public void setSourceText(String sourceText) {
    this.sourceText = sourceText;
  }

  public Language getSourceLanguage() {
    return sourceLanguage;
  }

  public void setSourceLanguage(Language sourceLanguage) {
    this.sourceLanguage = sourceLanguage;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Attachment[] getAttachments() {
    return attachments;
  }

  public void setAttachments(Attachment[] attachments) {
    this.attachments = attachments;
  }

  public ChannelAccount getFrom() {
    return from;
  }

  public void setFrom(ChannelAccount from) {
    this.from = from;
  }

  public ChannelAccount getTo() {
    return to;
  }

  public void setTo(ChannelAccount to) {
    this.to = to;
  }

  public ChannelAccount getReplyTo() {
    return replyTo;
  }

  public void setReplyTo(ChannelAccount replyTo) {
    this.replyTo = replyTo;
  }

  public String getReplyToMessageId() {
    return replyToMessageId;
  }

  public void setReplyToMessageId(String replyToMessageId) {
    this.replyToMessageId = replyToMessageId;
  }

  public ChannelAccount[] getParticipants() {
    return participants;
  }

  public void setParticipants(ChannelAccount[] participants) {
    this.participants = participants;
  }

  public Integer getTotalParticipants() {
    return totalParticipants;
  }

  public void setTotalParticipants(Integer totalParticipants) {
    this.totalParticipants = totalParticipants;
  }

  public Mention[] getMentions() {
    return mentions;
  }

  public void setMentions(Mention[] mentions) {
    this.mentions = mentions;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getChannelMessageId() {
    return channelMessageId;
  }

  public void setChannelMessageId(String channelMessageId) {
    this.channelMessageId = channelMessageId;
  }

  public String getChannelConversationId() {
    return channelConversationId;
  }

  public void setChannelConversationId(String channelConversationId) {
    this.channelConversationId = channelConversationId;
  }

  public Object getChannelData() {
    return channelData;
  }

  public void setChannelData(Object channelData) {
    this.channelData = channelData;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String[] getHashtags() {
    return hashtags;
  }

  public void setHashtags(String[] hashtags) {
    this.hashtags = hashtags;
  }

  public String geteTag() {
    return eTag;
  }

  public void seteTag(String eTag) {
    this.eTag = eTag;
  }

  public Message createReply() {
    final Message reply = new Message();

    reply.setConversationId(this.getConversationId());
    reply.setFrom(this.getTo());
    reply.setLanguage(this.getLanguage());
    reply.setTo(this.getFrom());

    return reply;
  }

  public Message createReply(String replyText) {
    final Message reply = createReply();
    reply.setText(replyText);
    return reply;
  }
}
