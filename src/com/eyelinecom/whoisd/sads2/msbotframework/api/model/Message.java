package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A communication message recieved from a User or sent out of band from a Bot.
 */
public class Message {

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

}
