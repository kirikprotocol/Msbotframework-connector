package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.common.Loader.Entity;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.eyelinecom.whoisd.sads2.common.HttpLoader.METHOD_POST;
import static com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils.unmarshal;
import static com.google.common.base.Preconditions.checkArgument;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.join;

public class BotApiClient {

  private static final Logger log = Logger.getLogger(BotApiClient.class);

  private static final String API_ROOT        = "https://api.botframework.com";

  private static final String SKYPE_API_ROOT  = "https://skype.botframework.com";
  private static final String FB_API_ROOT     = "https://facebook.botframework.com";

  private final String appId;
  private final String appSecret;

  private final HttpDataLoader loader;

  public BotApiClient(String appId,
                      String appSecret,
                      HttpDataLoader loader) {

    this.appId = appId;
    this.appSecret = appSecret;

    this.loader = loader;
  }

  /**
   * @return {@code <token, lifetime delta>}
   */
  Pair<String, Integer> requestToken() throws MbfException {
    log.debug("Requesting token: appId = [" + appId + "], appSecret = [" + appSecret + "]");

    try {
      final Entity rc = loader.load(
          "https://login.microsoftonline.com/common/oauth2/v2.0/token",
          join(
              new String[] {
                  "client_id=" + appId,
                  "client_secret=" + appSecret,
                  "grant_type=client_credentials",
                  "scope=" + encode("https://graph.microsoft.com/.default", "UTF-8")
              },
              "&"
          ),
          null,
          null,
          new HashMap<String, String>() {{
            put("Cache-Control", "no-cache");
            put("Content-Type", "application/x-www-form-urlencoded");
          }},
          METHOD_POST
      );

      final JsonNode json = MarshalUtils.parse(new String(rc.getBuffer(), StandardCharsets.UTF_8));
      return ImmutablePair.of(
          json.get("access_token").textValue(),
          json.get("expires_in").intValue()
      );

    } catch (Exception e) {
      throw new MbfException("Failed requesting auth token", e);
    }
  }

  private String guessServiceUrl(Activity msg) {
    checkArgument(msg.getServiceUrl() == null,
        "Internal service URL should be used if available");

    switch (msg.getProtocol()) {
      case SKYPE:     return SKYPE_API_ROOT;
      case FACEBOOK:  return FB_API_ROOT;

      default:
        log.warn("Cannot determine API ROOT for activity [" + msg + "]");
        return API_ROOT;
    }
  }

  public Activity send(String token, String serviceUrl, Activity msg) throws MbfException {
    if (serviceUrl == null) {
      serviceUrl = guessServiceUrl(msg);
      msg.setServiceUrl(serviceUrl);
    }

    final String conversationResource =
        serviceUrl + "/v3/conversations/" + msg.getConversation().getId() + "/activities";

    try {
      final Entity rc = post(token, conversationResource, msg.marshal());
      return unmarshal(new String(rc.getBuffer(), UTF_8), Activity.class);

    } catch (Exception e) {
      throw new MbfException("Failed sending message: [" + msg + "]", e);
    }
  }

  private Entity post(final String token, String path, String entity) throws Exception {
    return loader.load(
        path,
        entity,
        "application/json",
        "UTF-8",
        new HashMap<String, String>() {{
          put("Authorization", "Bearer " + token);
          put(
              "Accept",
              "application/json; charset=UTF-8");
        }},
        METHOD_POST);
  }

}
