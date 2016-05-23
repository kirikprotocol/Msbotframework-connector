package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

/**
 * Type of a message.
 */
public enum MessageType {

  /**
   * A simple communication between a user & bot
   */
  MESSAGE,

  /**
   * A system request to test system availability
   */
  PING,

  /**
   * The user has requested to have their data deleted.
   */
  DELETE_USER_DATA,

  /**
   * The bot has been added to a conversation.
   */
  BOT_ADDED_TO_CONVERSATION,

  /**
   * The bot has been removed from a conversation.
   */
  BOT_REMOVED_FROM_CONVERSATION,

  /**
   * A user has joined a conversation monitored by the bot.
   */
  USER_ADDED_TO_CONVERSATION,

  /**
   * A user has left a conversation monitored by the bot.
   */
  USER_REMOVED_FROM_CONVERSATION,

  /**
   * The user has elected to end the current conversation.
   */
  END_OF_CONVERSATION;

  private static final Map<String, MessageType> JSON_NAMES = unmodifiableMap(
      new HashMap<String, MessageType>() {{
        for (MessageType _ : values()) {
          put(StringUtils.remove(_.name(), '_'), _);
        }
      }});

  public static MessageType deserialize(String value) {
    return isBlank(value) ? null : JSON_NAMES.get(upperCase(value));
  }

  public String serialize() {
    return remove(capitalizeFully(name(), '_'), '_');
  }


  //
  //
  //

  static class Serializer extends JsonSerializer<MessageType> {

    @Override
    public void serialize(MessageType value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(value.serialize());
    }
  }


  //
  //
  //

  static class Deserializer extends JsonDeserializer<MessageType> {

    @Override
    public MessageType deserialize(JsonParser p,
                                   DeserializationContext ctxt) throws IOException {
      return MessageType.deserialize(p.getValueAsString());
    }
  }
}
