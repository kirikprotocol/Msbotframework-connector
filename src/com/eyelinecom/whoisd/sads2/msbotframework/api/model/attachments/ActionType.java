package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Type of an action.
 */
public enum ActionType {

  /**
   * Client will open given url in the built-in browser.
   */
  OPEN_URL("openUrl"),

  /**
   * Client will post message to bot, so all other participants will see that was posted
   * to the bot and who posted this.
   */
  IM_BACK("imBack"),

  /**
   * Client will post message to bot privately, so other participants inside conversation
   * will not see that was posted.
   */
  POST_BACK("postBack"),

  /**
   * Playback audio container referenced by url.
   */
  PLAY_AUDIO("playAudio"),

  /**
   * Playback video container referenced by url.
   */
  PLAY_VIDEO("playVideo"),

  /**
   * Show image referenced by url.
   */
  SHOW_IMAGE("showImage"),

  /**
   * Download file referenced by url.
   */
  DOWNLOAD_FILE("downloadFile"),

  /**
   * Sign In button.
   */
  SIGN_IN("signin");

  private final String value;

  ActionType(String value) { this.value = value; }


  //
  //
  //

  static class Serializer extends JsonSerializer<ActionType> {

    @Override
    public void serialize(ActionType value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(value.value);
    }
  }


  //
  //
  //

  static class Deserializer extends JsonDeserializer<ActionType> {

    @Override
    public ActionType deserialize(JsonParser p,
                                  DeserializationContext ctxt) throws IOException {
      final String jsonValue = p.getValueAsString();

      if (isNotBlank(jsonValue)) {
        for (ActionType actionType : ActionType.values()) {
          if (StringUtils.equals(actionType.value, jsonValue)) {
            return actionType;
          }
        }
      }

      return null;
    }
  }
}
