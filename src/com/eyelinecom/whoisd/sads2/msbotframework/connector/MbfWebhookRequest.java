package com.eyelinecom.whoisd.sads2.msbotframework.connector;

import com.eyelinecom.whoisd.sads2.common.StoredHttpRequest;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.ActivityType.MESSAGE;
import static com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils.parse;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * Microsoft Bot Framework WebHook request.
 * <br/>
 * Expected to land at {@literal <MOBILIZER_ROOT>/<MBF_CONNECTOR>/<mbf.app.id>}.
 */
public class MbfWebhookRequest extends StoredHttpRequest {

  private Activity activity;
  private final String appId;

  MbfWebhookRequest(HttpServletRequest request) {
    super(request);

    final String[] parts = getRequestURI().split("/");
    appId = parts[parts.length - 1];
  }

  public Activity asMessage() throws IOException, MbfException {
    if (activity == null) {
      activity = MarshalUtils.unmarshal(parse(getContent()), Activity.class);
    }

    return activity;
  }

  public String getMessageText() {
    return activity.getType() == MESSAGE ? trimToNull(activity.getText()) : null;
  }

  /**
   * Extract application ID as a part of registered WebHook URL.
   */
  public String getAppId() {
    return appId;
  }
}
