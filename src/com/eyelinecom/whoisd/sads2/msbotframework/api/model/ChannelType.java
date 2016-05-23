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
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.upperCase;


/**
 * Channel type that the {@link ChannelAccount} is to be communicated with
 */
public enum ChannelType {

  TEST,
  EMULATOR,
  EMAIL,
  GROUP_ME,
  FACEBOOK,
  SKYPE,
  SLACK,
  SMS,
  TELEGRAM,
  WEB;

  private static final Map<String, ChannelType> CHANNEL_NAMES = unmodifiableMap(
      new HashMap<String, ChannelType>() {{
        for (ChannelType _ : ChannelType.values()) {
          put(StringUtils.remove(_.name(), '_'), _);
        }
      }});

  public static ChannelType deserialize(String input) {
    return isBlank(input) ? null : CHANNEL_NAMES.get(upperCase(input));
  }

  public String serialize() {
    return remove(lowerCase(name()), '_');
  }


  //
  //
  //

  static class Serializer extends JsonSerializer<ChannelType> {

    @Override
    public void serialize(ChannelType value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(value.serialize());
    }
  }


  //
  //
  //

  static class Deserializer extends JsonDeserializer<ChannelType> {

    @Override
    public ChannelType deserialize(JsonParser p,
                                   DeserializationContext ctxt) throws IOException {
      return ChannelType.deserialize(p.getValueAsString());
    }
  }

}
