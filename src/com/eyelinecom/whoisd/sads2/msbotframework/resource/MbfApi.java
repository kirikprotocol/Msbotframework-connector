package com.eyelinecom.whoisd.sads2.msbotframework.resource;

import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Message;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.eyelinecom.whoisd.sads2.session.SessionManager;

public interface MbfApi {

  Message send(SessionManager sessionManager,
               MbfBotDetails bot,
               Message message) throws MbfException;

}
