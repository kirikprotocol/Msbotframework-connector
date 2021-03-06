package com.eyelinecom.whoisd.sads2.msbotframework.registry;

import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import com.eyelinecom.whoisd.sads2.common.StringUtils;
import com.eyelinecom.whoisd.sads2.exception.ConfigurationException;
import com.eyelinecom.whoisd.sads2.exception.NotFoundServiceException;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.resource.MbfApi;
import com.eyelinecom.whoisd.sads2.registry.Config;
import com.eyelinecom.whoisd.sads2.registry.ServiceConfig;
import com.eyelinecom.whoisd.sads2.registry.ServiceConfigListener;
import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.Properties;

import static com.google.common.base.Predicates.isNull;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.all;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.trimToNull;

@SuppressWarnings("unused")
public class MbfServiceRegistry extends ServiceConfigListener {

  public static final String CONF_TOKEN = "msbotframework.token";

  private final Logger log = Logger.getLogger(MbfServiceRegistry.class);

  private final BiMap<String /* serviceId */, MbfBotDetails> services = HashBiMap.create();
  private final MbfApi api;

  public MbfServiceRegistry(MbfApi api) {
    this.api = api;
  }

  @Override
  protected void process(Config config) throws ConfigurationException {
    final String serviceId = config.getId();

    if (config.isEmpty()) {
      unregister(serviceId);

    } else if (config instanceof ServiceConfig) {
      final Properties attributes = ((ServiceConfig) config).getAttributes();

      final String token =
          trimToNull(InitUtils.getString(CONF_TOKEN, null, attributes));

      final Pair<String, String> idAndSecret = StringUtils.parsePair(token);
      final String appId = idAndSecret.getKey();
      final String appSecret = idAndSecret.getValue();

      if (all(asList(appId, appSecret), not(isNull()))) {
        // Both App ID & secret successfully obtained.
        final MbfBotDetails prevDetails = services.get(serviceId);

        if (prevDetails != null && !prevDetails.matches(appId)) {
          unregister(serviceId);
        }

        register(serviceId, appId, appSecret);

      } else if (!all(asList(appId, appSecret), isNull())) {
        throw new ConfigurationException(serviceId,
            "MsBotFramework connection details are invalid: " + CONF_TOKEN + " = [" + token + "]");

      } else {
        // No MBF properties - unregistering.
        unregister(serviceId);
      }
    }

  }

  private void register(String serviceId,
                        String appId,
                        String appSecret) throws ConfigurationException {

    final MbfBotDetails details = new MbfBotDetails(appId, appSecret);

    // Check if these credentials are already used in another service.
    {
      final String otherSid = services.inverse().get(details);
      if (otherSid != null && !Objects.equals(otherSid, serviceId)) {
        throw new ConfigurationException(serviceId,
            "MsBotFramework application ID is already used in service [" + otherSid + "]:" +
                " " + CONF_TOKEN + " = [" + appId + ":" + appSecret + "]");
      }
    }

    // Check if Application ID & secret are valid.
    try {
      api.checkCredentials(details);

    } catch (MbfException e) {
      throw new ConfigurationException(serviceId, "Credentials check failed", e);
    }

    services.put(serviceId, details);
  }

  private void unregister(String serviceId) throws ConfigurationException {
    services.remove(serviceId);
  }

  public String findService(String appId) throws NotFoundServiceException {
    final String serviceId = services.inverse().get(new MbfBotDetails(appId, null));
    if (serviceId == null) {
      throw new NotFoundServiceException("[by MsBotFramework application ID = " + appId + "]");
    }
    return serviceId;
  }

  public MbfBotDetails findBotByMbfToken(String mbfToken) {
    try {
      mbfToken = trimToNull(mbfToken);

      final Pair<String, String> idAndSecret = StringUtils.parsePair(mbfToken);
      final String appId = idAndSecret.getLeft();
      return getBot(findService(appId));

    } catch (Exception e) {
      log.error("Lookup by mbfToken = [" + mbfToken + "] failed", e);
      return null;
    }
  }

  public MbfBotDetails getBot(String serviceId) {
    return services.get(serviceId);
  }

  @SuppressWarnings("unused")
  public static class Factory implements ResourceFactory {

    @Override
    public MbfServiceRegistry build(String id,
                                    Properties properties,
                                    HierarchicalConfiguration config) throws Exception {
      final MbfApi api = SADSInitUtils.getResource("msbotframework-api", properties);
      return new MbfServiceRegistry(api);
    }

    @Override
    public boolean isHeavyResource() {
      return false;
    }
  }
}
