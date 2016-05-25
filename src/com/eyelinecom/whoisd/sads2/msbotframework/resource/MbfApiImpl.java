package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.common.HttpDataLoader;
import com.eyelinecom.whoisd.sads2.common.RateLimiter;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Message;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import com.eyelinecom.whoisd.sads2.session.SessionManager;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.log4j.Logger;

import java.util.Properties;

@SuppressWarnings("unused")
public class MbfApiImpl implements MbfApi {

  private static final Logger log = Logger.getLogger(MbfApiImpl.class);

  private final HttpDataLoader loader;

  /**
   * Maximal allowed messages per second, overall.
   */
  private final RateLimiter messagesPerSecondLimit;

  /**
   * {@code true} iff MBF token should be validated on change.
   * As the validation logic is unreliable, we might need to disable this.
   */
  private final boolean performEagerTokenCheck;

  private MbfApiImpl(HttpDataLoader loader, Properties properties) {
    this.loader = loader;

    final float limitMessagesPerSecond =
        Float.parseFloat(properties.getProperty("mbf.limit.messages.per.second", "30"));
    this.messagesPerSecondLimit = RateLimiter.create(limitMessagesPerSecond);

    this.performEagerTokenCheck =
        Boolean.parseBoolean(properties.getProperty("mbf.validate.token", "true"));
  }

  @Override
  public Message send(SessionManager sessionManager,
                      MbfBotDetails bot,
                      Message message) throws MbfException {

    messagesPerSecondLimit.acquire();

    return getClient(bot).send(message);
  }

  @Override
  public void checkCredentials(MbfBotDetails bot) throws MbfException {
    messagesPerSecondLimit.acquire();

    if (performEagerTokenCheck) {
      getClient(bot).checkCredentials();
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
      return new MbfApiImpl(loader, properties);
    }

    @Override public boolean isHeavyResource() { return false; }
  }
}
