package com.eyelinecom.whoisd.sads2.msbotframework.interceptors;

import com.eyelinecom.whoisd.sads2.Protocol;
import com.eyelinecom.whoisd.sads2.common.DocumentUtils;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Activity;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.MbfAttachment;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments.HeroCard;
import com.eyelinecom.whoisd.sads2.msbotframework.registry.MbfBotDetails;
import com.google.common.collect.Iterables;
import org.dom4j.Document;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.eyelinecom.whoisd.sads2.Protocol.FACEBOOK;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class MbfPushInterceptorTest {

  @Test
  public void test1() throws Exception {
    final MbfPushInterceptor mbfPushInterceptor = new MbfPushInterceptor() {
      @Override
      Activity createActivityTemplate(SADSRequest request, MbfBotDetails bot) {
        return new Activity();
      }

      @Override
      protected String getFbSingleBubbleHeader(SADSRequest request) {
        return ".";
      }
    };

    final Document page = DocumentUtils.parseDocument(
        ("" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<page version=\"2.0\">\n" +
            "  <message>\n" +
            "    Введите персональный пароль\n" +
            "  </message>\n" +
            "    <button pageId=\"1.jsp\">Получить новый пароль в SMS</button>\n" +
            "    <button pageId=\"2.jsp\">Я отказываюсь использовать пароль</button>\n" +
            "</page>"
        ).getBytes(StandardCharsets.UTF_8)
    );

    final SADSRequest request = new SADSRequest() {
      @Override public Protocol getProtocol() { return FACEBOOK; }
      @Override public String getResourceURI() { return "/"; }
    };

    final List<Activity> rc =
        mbfPushInterceptor.createResponse(request, null, page, MbfPushInterceptor.getText(page));

    final Activity msg = Iterables.getOnlyElement(rc);
    assertNull(msg.getText());

    assertEquals(1, msg.getAttachments().length);

    final MbfAttachment a = msg.getAttachments()[0];

    final HeroCard card = (HeroCard) a.getContent();
    assertEquals(2, card.getButtons().length);

    assertEquals("Введите персональный пароль", card.getText());
  }

}
