package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Message;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.session.SessionManager;

public interface MbfApi {

  Message send(SessionManager sessionManager,
               MbfBotDetails bot,
               Message message) throws MbfException;

  /**
   * Checks if the supplied credentials are valid.
   *
   * @throws MbfException in case validation failed for whatever reason.
   */
  void checkCredentials(MbfBotDetails bot) throws MbfException;

}
