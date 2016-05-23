package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.common.Loader.Entity;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.BotData;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Message;

import java.util.HashMap;

import static com.eyelinecom.whoisd.sads2.common.HttpLoader.METHOD_POST;
import static com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils.unmarshal;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class BotApiClient {

  private static final String API_ROOT = "https://api.botframework.com";

  private final String botId;

  private final String appId;
  private final String appSecret;

  private final HttpDataLoader loader;

  public BotApiClient(String botId,
                      String appId,
                      String appSecret,
                      HttpDataLoader loader) {

    this.botId = botId;

    this.appId = appId;
    this.appSecret = appSecret;

    this.loader = loader;
  }

  public Message send(Message msg) throws MbfException {
    checkNotNull(msg);

    try {
      final Entity rc = post(getMessagesApi(), msg.marshal());
      return unmarshal(new String(rc.getBuffer(), UTF_8), Message.class);

    } catch (Exception e) {
      throw new MbfException("Failed sending message: [" + msg + "]", e);
    }
  }

  public BotData<?> getUserData(String userId) throws MbfException {
    checkNotNull(userId);

    try {
      final Entity rc = get(getUserDataApi(botId, userId));
      return unmarshal(new String(rc.getBuffer(), UTF_8), BotData.class);

    } catch (Exception e) {
      throw new MbfException("API request failed", e);
    }
  }

  public BotData<?> getConversationData(String conversationId) throws MbfException {
    checkNotNull(conversationId);

    try {
      final Entity rc = get(getConversationDataApi(botId, conversationId));
      return unmarshal(new String(rc.getBuffer(), UTF_8), BotData.class);

    } catch (Exception e) {
      throw new MbfException("API request failed", e);
    }
  }

  public BotData<?> getConversationUserData(String conversationId,
                                            String userId) throws MbfException {
    checkNotNull(conversationId);
    checkNotNull(userId);

    try {
      final Entity rc = get(getConversationUserDataApi(botId, conversationId, userId));
      return unmarshal(new String(rc.getBuffer(), UTF_8), BotData.class);

    } catch (Exception e) {
      throw new MbfException("API request failed", e);
    }
  }

  private Entity post(String path, String entity) throws Exception {
    return loader.load(
        path,
        entity,
        "application/json",
        "utf8",
        new HashMap<String, String>() {{
          put(
              "Authorization",
              "Basic " + printBase64Binary((appId + ":" + appSecret).getBytes(UTF_8)));
          put(
              "Ocp-Apim-Subscription-Key",
              appSecret);
          put(
              "Accept",
              "application/json; encoding=utf8");
        }},
        METHOD_POST);
  }

  private Entity get(String path) throws Exception {
    return loader.load(
        path,
        new HashMap<String, String>() {{
          put(
              "Authorization",
              "Basic " + printBase64Binary((appId + ":" + appSecret).getBytes(UTF_8)));
          put(
              "Ocp-Apim-Subscription-Key",
              appSecret);
        }});
  }

  private String getMessagesApi() {
    return API_ROOT + "/bot/v1.0/messages";
  }

  private String getUserDataApi(String botId, String userId) {
    return API_ROOT + "/bot/v1.0/bots/" + botId + "/users/" + userId;
  }

  private String getConversationDataApi(String botId, String conversationId) {
    return API_ROOT + "/bot/v1.0/bots/" + botId + "/conversations/" + conversationId;
  }

  private String getConversationUserDataApi(String botId, String conversationId, String userId) {
    return API_ROOT + "/bot/v1.0/bots/" + botId + "/conversations/" + conversationId + "/users/" + userId;
  }
}
