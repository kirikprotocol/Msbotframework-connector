package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

public enum FacebookNotificationType {
  REGULAR, SILENT_PUSH, NO_PUSH;

  private static final Map<String, FacebookNotificationType> NOTIFICATION_TYPES = unmodifiableMap(
      new HashMap<String, FacebookNotificationType>() {{
        for (FacebookNotificationType _ : FacebookNotificationType.values()) {
          put(_.name(), _);
        }
      }});

  private static FacebookNotificationType deserialize(String input) {
    return isBlank(input) ? null : NOTIFICATION_TYPES.get(input);
  }

  private String serialize() {
    return this.name();
  }


  //
  //
  //

  static class Serializer extends JsonSerializer<FacebookNotificationType> {

    @Override
    public void serialize(FacebookNotificationType value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(value.serialize());
    }
  }


  //
  //
  //

  static class Deserializer extends JsonDeserializer<FacebookNotificationType> {

    @Override
    public FacebookNotificationType deserialize(JsonParser p,
                                   DeserializationContext ctxt) throws IOException {
      return FacebookNotificationType.deserialize(p.getValueAsString());
    }
  }

}
