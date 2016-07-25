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
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

/**
 * Type of a activity.
 *
 */
public enum ActivityType {

  /**
   * Message from a user -> bot or bot -> User
   */
  MESSAGE,

  /**
   * This notification is sent when the conversation's properties change,
   * for example the topic name, or when user joins or leaves the group.
   */
  CONVERSATION_UPDATE,

  /**
   * Bot added or removed to contact list. See <see cref="ContactRelationUpdateActionTypes"/> for possible values.
   */
  CONTACT_RELATION_UPDATE,

  /**
   * A from is typing.
   */
  TYPING,

  /**
   * Delete user data
   */
  DELETE_USER_DATA,

  /**
   * Ping message.
   */
  PING;

  private static final Map<String, ActivityType> JSON_NAMES = unmodifiableMap(
      new HashMap<String, ActivityType>() {{
        for (ActivityType _ : ActivityType.values()) {
          put(StringUtils.remove(_.name(), '_'), _);
        }
      }});

  public static ActivityType deserialize(String value) {
    return isBlank(value) ? null : JSON_NAMES.get(upperCase(value));
  }

  public String serialize() {
    return remove(uncapitalize(capitalizeFully(name(), '_')), '_');
  }


  //
  //
  //

  static class Serializer extends JsonSerializer<ActivityType> {

    @Override
    public void serialize(ActivityType value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(value.serialize());
    }
  }


  //
  //
  //

  static class Deserializer extends JsonDeserializer<ActivityType> {

    @Override
    public ActivityType deserialize(JsonParser p,
                                    DeserializationContext ctxt) throws IOException {
      return ActivityType.deserialize(p.getValueAsString());
    }
  }
}
