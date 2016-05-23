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

/**
 * Microsoft Translator Language Codes (as of October 2015).
 *
 * @see <a href="https://msdn.microsoft.com/en-us/library/hh456380.aspx">Translator Language Codes</a>
 */
public enum Language {
  ARABIC("ar", "Arabic"),
  BOSNIAN("bs-Latn", "Bosnian (Latin)"),
  BULGARIAN("bg", "Bulgarian"),
  CATALAN("ca", "Catalan"),
  CHINESE_SIMPLIFIED("zh-CHS", "Chinese Simplified"),
  CHINESE_TRADITIONAL("zh-CHT", "Chinese Traditional"),
  CROATIAN("hr", "Croatian"),
  CZECH("cs", "Czech"),
  DANISH("da", "Danish"),
  DUTCH("nl", "Dutch"),
  ENGLISH("en", "English"),
  ESTONIAN("et", "Estonian"),
  FINNISH("fi", "Finnish"),
  FRENCH("fr", "French"),
  GERMAN("de", "German"),
  GREEK("el", "Greek"),
  HAITIAN_CREOLE("ht", "Haitian Creole"),
  HEBREW("he", "Hebrew"),
  HINDI("hi", "Hindi"),
  HMONG_DAW("mww", "Hmong Daw"),
  HUNGARIAN("hu", "Hungarian"),
  INDONESIAN("id", "Indonesian"),
  ITALIAN("it", "Italian"),
  JAPANESE("ja", "Japanese"),
  KISWAHILI("sw", "Kiswahili"),
  KLINGON("tlh", "Klingon"),
  KLINGON_PIQAD("tlh-Qaak", "Klingon (pIqaD)"),
  KOREAN("ko", "Korean"),
  LATVIAN("lv", "Latvian"),
  LITHUANIAN("lt", "Lithuanian"),
  MALAY("ms", "Malay"),
  MALTESE("mt", "Maltese"),
  NORWEGIAN("no", "Norwegian"),
  PERSIAN("fa", "Persian"),
  POLISH("pl", "Polish"),
  PORTUGUESE("pt", "Portuguese"),
  QUERETARO_OTOMI("otq", "Querétaro Otomi"),
  ROMANIAN("ro", "Romanian"),
  RUSSIAN("ru", "Russian"),
  SERBIAN_CYRILLIC("sr-Cyrl", "Serbian (Cyrillic)"),
  SERBIAN_LATIN("sr-Latn", "Serbian (Latin)"),
  SLOVAK("sk", "Slovak"),
  SLOVENIAN("sl", "Slovenian"),
  SPANISH("es", "Spanish"),
  SWEDISH("sv", "Swedish"),
  THAI("th", "Thai"),
  TURKISH("tr", "Turkish"),
  UKRAINIAN("uk", "Ukrainian"),
  URDU("ur", "Urdu"),
  VIETNAMESE("vi", "Vietnamese"),
  WELSH("cy", "Welsh"),
  YUCATEC_MAYA("yua", "Yucatec Maya");

  static final Map<String, Language> LANGUAGE_CODE_MAP = unmodifiableMap(
      new HashMap<String, Language>() {{
        for (Language _ : Language.values()) {
          put(_.code, _);
        }
      }});

  private final String code;
  private final String name;

  Language(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public static Language deserialize(String code) {
    return (StringUtils.isBlank(code) ? null : LANGUAGE_CODE_MAP.get(code));
  }

  public String serialize() {
    return code;
  }

  public String getName() {
    return name;
  }


  //
  //
  //

  static class Serializer extends JsonSerializer<Language> {

    @Override
    public void serialize(Language value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
      gen.writeString(value.serialize());
    }
  }


  //
  //
  //

  static class Deserializer extends JsonDeserializer<Language> {

    @Override
    public Language deserialize(JsonParser p,
                                DeserializationContext ctxt) throws IOException {
      return Language.deserialize(p.getValueAsString());
    }
  }
}
