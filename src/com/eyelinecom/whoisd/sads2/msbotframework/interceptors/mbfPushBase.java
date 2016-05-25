package com.eyelinecom.whoisd.sads2.msbotframework.interceptors;

import com.eyelinecom.whoisd.sads2.interceptor.BlankInterceptor;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

public abstract class MbfPushBase extends BlankInterceptor {

  private static final Logger log = Logger.getLogger(MbfPushBase.class);

  public static List<Element> getKeyboard(Document doc) {

    @SuppressWarnings("unchecked")
    final List<Element> buttons = (List<Element>) doc.getRootElement().elements("button");
    return isEmpty(buttons) ? null : buttons;
  }

}
