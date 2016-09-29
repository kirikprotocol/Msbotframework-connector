package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.common.RateLimiter;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.eventstat.DetailedStatLogger;
import com.eyelinecom.whoisd.sads2.executors.connector.Context;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import com.eyelinecom.whoisd.sads2.session.SessionManager;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;

@SuppressWarnings("unused")
public class MbfApiImpl implements MbfApi {

  private static final String API_ROOT        = "https://api.botframework.com";

  private static final String SKYPE_API_ROOT  = "https://skype.botframework.com";
  private static final String FB_API_ROOT     = "https://facebook.botframework.com";

  private static final Logger log = Logger.getLogger(MbfApiImpl.class);

  private final HttpDataLoader loader;
  private final DetailedStatLogger detailedStatLogger;

  /**
   * Maximal allowed messages per second, overall.
   */
  private final RateLimiter messagesPerSecondLimit;

  private MbfApiImpl(HttpDataLoader loader, DetailedStatLogger detailedStatLogger, Properties properties) {
    this.loader = loader;
    this.detailedStatLogger = detailedStatLogger;

    final float limitMessagesPerSecond =
        Float.parseFloat(properties.getProperty("mbf.limit.messages.per.second", "30"));
    this.messagesPerSecondLimit = RateLimiter.create(limitMessagesPerSecond);
  }

  @Override
  public Activity send(SessionManager sessionManager,
                       MbfBotDetails bot,
                       Activity activity) throws MbfException {

    messagesPerSecondLimit.acquire();

    final BotApiClient client = getClient(bot);

    if (bot.shouldRefreshToken()) {
      final Pair<String, Integer> authToken = client.requestToken();
      bot.setToken(authToken.getKey(), authToken.getValue());
    }

    if (activity.getServiceUrl() == null) {
      activity.setServiceUrl(guessServiceUrl(activity));
    }

    if (Context.getSadsRequest() != null) {
      detailedStatLogger.onOuterResponse(Context.getSadsRequest(), activity);
    }

    return client.send(bot.getToken(), activity);
  }

  @Override
  public void checkCredentials(MbfBotDetails bot) throws MbfException {
    messagesPerSecondLimit.acquire();

    final BotApiClient client = getClient(bot);

    final Pair<String, Integer> authToken = client.requestToken();
    if (bot.shouldRefreshToken()) {
      bot.setToken(authToken.getKey(), authToken.getValue());
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

  private BotApiClient getClient(MbfBotDetails bot) {
    return new BotApiClient(bot.getAppId(), bot.getAppSecret(), loader);
  }

  @SuppressWarnings("unused")
  public static class Factory implements ResourceFactory {

    @Override
    public MbfApi build(String id,
                        Properties properties,
                        HierarchicalConfiguration config) throws Exception {

      final HttpDataLoader loader = SADSInitUtils.getResource("loader", properties);
      final DetailedStatLogger detailedStatLogger = SADSInitUtils.getResource("detailed-stat-logger", properties);
      return new MbfApiImpl(loader, detailedStatLogger, properties);
    }

    @Override public boolean isHeavyResource() { return false; }
  }
}
