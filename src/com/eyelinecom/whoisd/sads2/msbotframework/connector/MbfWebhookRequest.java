package com.eyelinecom.whoisd.sads2.msbotframework.connector;

import com.eyelinecom.whoisd.sads2.common.StoredHttpRequest;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Message;
import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.MESSAGE;
import static com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils.parse;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * Microsoft Bot Framework WebHook request.
 * <br/>
 * Expected to land at {@literal <MOBILIZER_ROOT>/<MBF_CONNECTOR>/<mbf.app.id>}.
 */
public class MbfWebhookRequest extends StoredHttpRequest {

  private Message message;
  private final String appId;

  MbfWebhookRequest(HttpServletRequest request) {
    super(request);

    final String[] parts = getRequestURI().split("/");
    appId = parts[parts.length - 1];
  }

  public Message asMessage() throws IOException, MbfException {
    if (message == null) {
      message = MarshalUtils.unmarshal(parse(getContent()), Message.class);
    }

    return message;
  }

  public String getMessageText() {
    return message.getType() == MESSAGE ? trimToNull(message.getText()) : null;
  }

  /**
   * Extract application ID as a part of registered WebHook URL.
   */
  public String getAppId() {
    return appId;
  }
}
