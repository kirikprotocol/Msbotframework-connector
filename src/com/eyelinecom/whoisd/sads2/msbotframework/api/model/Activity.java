package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.eyelinecom.whoisd.sads2.Protocol;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * A communication message received from a User or sent out of band from a Bot.
 */
public class Activity extends ApiType<Activity> {

  /**
   * What kind of activity is this.
   */
  @JsonSerialize(using = ActivityType.Serializer.class)
  @JsonDeserialize(using = ActivityType.Deserializer.class)
  private ActivityType type;

  /**
   * Bot.Connector Id for the message.
   */
  private String id;

  /**
   * Time when message was sent.
   * Example: {@literal 2016-07-18T12:56:35.242Z}.
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
  private Date timestamp;

  /**
   * Service endpoint, the url to use for sending activities back.
   *
   * <strong>
   *  Note: Even though ServiceUrl values may seem stable bots should not rely on that
   *  and instead always use the ServiceUrl value.
   * </strong>
   */
  private String serviceUrl;

  /**
   * ChannelId the activity was on
   */
  private String channelId;

  /**
   * Sender address.
   */
  private ChannelAccount from;

  /**
   * Conversation.
   */
  private ConversationAccount conversation;

  /**
   * (Outbound to bot only) Bot's address that received the message.
   */
  private ChannelAccount recipient;

  /**
   * Format of text fields [plain|markdown].
   *
   * <p>Default:markdown.
   */
  private String textFormat;

  /**
   * AttachmentLayout - hint for how to deal with multiple attachments.
   * Values: [list|carousel]
   *
   * <p>Default:list
   */
  private String attachmentLayout;

  /**
   * Array of addresses added.
   */
  private ChannelAccount[] membersAdded;

  /**
   * Array of addresses removed.
   */
  private ChannelAccount[] membersRemoved;

  /**
   * Conversations new topic name.
   */
  private String topicName;

  /**
   * The previous history of the channel was disclosed
   */
  @JsonProperty("historyDisclosed")
  private Boolean historyDisclosed;

  /**
   * The language code of the Text field
   */
  private String locale;

  /**
   * Content for the message.
   */
  private String text;

  /**
   * Text to display if you can't render cards
   */
  private String summary;

  /**
   * Attachments
   */
  @JsonProperty
  private MbfAttachment[] attachments;

  /**
   * Collection of Entity which contain metadata about this activity
   * (each is typed)
   */
  @JsonProperty
  private Entity[] entities;

  /**
   * Channel specific payload
   */
  @JsonIgnore
  private ChannelData channelData;

  /**
   * ContactAdded/Removed action.
   */
  private String action;

  /**
   * The original id this message is a response to
   */
  private String replyToId;

  public ActivityType getType() {
    return type;
  }

  public void setType(ActivityType type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getServiceUrl() {
    return serviceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public ChannelAccount getFrom() {
    return from;
  }

  public void setFrom(ChannelAccount from) {
    this.from = from;
  }

  public ConversationAccount getConversation() {
    return conversation;
  }

  public void setConversation(ConversationAccount conversation) {
    this.conversation = conversation;
  }

  public ChannelAccount getRecipient() {
    return recipient;
  }

  public void setRecipient(ChannelAccount recipient) {
    this.recipient = recipient;
  }

  public String getTextFormat() {
    return textFormat;
  }

  public void setTextFormat(String textFormat) {
    this.textFormat = textFormat;
  }

  public String getAttachmentLayout() {
    return attachmentLayout;
  }

  public void setAttachmentLayout(String attachmentLayout) {
    this.attachmentLayout = attachmentLayout;
  }

  public ChannelAccount[] getMembersAdded() {
    return membersAdded;
  }

  public void setMembersAdded(ChannelAccount[] membersAdded) {
    this.membersAdded = membersAdded;
  }

  public ChannelAccount[] getMembersRemoved() {
    return membersRemoved;
  }

  public void setMembersRemoved(ChannelAccount[] membersRemoved) {
    this.membersRemoved = membersRemoved;
  }

  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public Boolean getHistoryDisclosed() {
    return historyDisclosed;
  }

  public void setHistoryDisclosed(Boolean historyDisclosed) {
    this.historyDisclosed = historyDisclosed;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getText() {
    return text;
  }

  public Activity setText(String text) {
    this.text = text;
    return this;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  @JsonProperty
  public MbfAttachment[] getAttachments() {
    return attachments;
  }

  @JsonProperty
  public void setAttachments(MbfAttachment[] attachments) {
    this.attachments = attachments;
  }

  @JsonIgnore
  public void setAttachments(List<MbfAttachment> attachments) {
    this.setAttachments(attachments.toArray(new MbfAttachment[0]));
  }

  public Entity[] getEntities() {
    return entities;
  }

  public void setEntities(Entity[] entities) {
    this.entities = entities;
  }

  @JsonIgnore
  public void setEntities(List<Entity> entities) {
    this.setEntities(entities.toArray(new Entity[0]));
  }

  @JsonProperty
  public ChannelData getChannelData() {
    return channelData;
  }

  @JsonIgnore
  public Activity setChannelData(ChannelData channelData) {
    this.channelData = channelData;
    return this;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getReplyToId() {
    return replyToId;
  }

  public void setReplyToId(String replyToId) {
    this.replyToId = replyToId;
  }

  public Activity createReply(Date now) {
    final Activity reply = new Activity();
    reply.setType(ActivityType.MESSAGE);
    reply.setTimestamp(now);
    reply.setFrom(this.getRecipient());
    reply.setRecipient(this.getFrom());
    reply.setReplyToId(this.getId());
    reply.setConversation(this.getConversation());

    // Don't need to send in a reply.
    reply.getConversation().setGroup(null);

    // Not required according to the spec, but helps in async pushes.
    reply.setChannelId(this.getChannelId());
    reply.setServiceUrl(this.getServiceUrl());

    return reply;
  }

  public Activity createReply(Date now, String replyText) {
    final Activity reply = createReply(now);
    reply.setText(replyText);
    return reply;
  }

  public Protocol getProtocol() {
    return asProtocol(getChannelId());
  }


  //
  //

  public static String asChannelId(Protocol protocol) {
    if (protocol == null) {
      return null;
    }

    switch (protocol) {
      case SKYPE:     return "skype";
      case FACEBOOK:  return "facebook";
      default:        return null;
    }
  }

  public static Protocol asProtocol(String channelId) {
    if (channelId == null) {
      return null;
    }

    if ("facebook".equalsIgnoreCase(channelId)) {
      return Protocol.FACEBOOK;

    } else if ("skype".equalsIgnoreCase(channelId)) {
      return Protocol.SKYPE;
    }

    // Note: we don't have a pre-defined list of supported channels.
    return null;
  }


}
