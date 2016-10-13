package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;

public interface MbfApi {

  Activity send(MbfBotDetails bot,
                Activity activity) throws MbfException;

  /**
   * Checks if the supplied credentials are valid.
   *
   * @throws MbfException in case validation failed for whatever reason.
   */
  void checkCredentials(MbfBotDetails bot) throws MbfException;

}
