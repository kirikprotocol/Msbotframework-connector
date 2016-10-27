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


  @Test
  public void test2() throws Exception {
    final MbfPushInterceptor mbfPushInterceptor = new MbfPushInterceptor() {
      @Override
      Activity createActivityTemplate(SADSRequest request, MbfBotDetails bot) {
        return new Activity();
      }
    };

    final Document page = DocumentUtils.parseDocument(
        ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<page><message>С услугой «Звони бесплатно на МТС России 100» абоненты тарифных планов «Супер МТС» и «Супер МТС 042014» могут бесплатно общаться со своими родными и близкими.\n" +
            "\n" +
            "  Абонентам предоставляется 100 бесплатных минут в сутки на звонки абонентам МТС в домашнем регионе, а также 100 бесплатных минут в сутки на звонки абонентам МТС всей России.\n" +
            "\n" +
            "  Стоимость подключения — 3,50 руб.\n" +
            "\n" +
            "  Ежесуточная плата — 3,50 руб.\n" +
            "\n" +
            "  Подробное описание: http://www.mts.ru/mob_connect/tariffs/discounts/free_call_100/ </message><button href=\"request_2_portal.jsp?cmd=$MSISDN$*call_free_russia*activate\" row=\"1\" index=\"1\">Подключить</button><button href=\"request_2_portal.jsp?cmd=$MSISDN$*call_free_russia*deactivate\" row=\"1\" index=\"2\">Отключить</button><button href=\"discount_calls.jsp\" row=\"2\" index=\"3\">⬅ Назад</button><button href=\"index.jsp\" row=\"2\" index=\"4\">Главное меню</button></page>"
        ).getBytes(StandardCharsets.UTF_8)
    );

    final SADSRequest request = new SADSRequest() {
      @Override public Protocol getProtocol() { return FACEBOOK; }
      @Override public String getResourceURI() { return "/"; }
    };

    final List<Activity> rc =
        mbfPushInterceptor.createResponse(request, null, page, MbfPushInterceptor.getText(page));

    assertEquals(4, rc.size());
  }

  @Test
  public void test3() throws Exception {
    final MbfPushInterceptor mbfPushInterceptor = new MbfPushInterceptor() {
      @Override
      Activity createActivityTemplate(SADSRequest request, MbfBotDetails bot) {
        return new Activity();
      }
    };

    final Document page = DocumentUtils.parseDocument(
        ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<page><message>«Выгодный межгород» - услуга, которая позволяет экономить при звонках своим родным и близким в другие города.\n" +
            "\n" +
            "  Подключайте опцию «Выгодный межгород» и звоните всего за 3 руб./мин. на номера операторов мобильной и фиксированной связи любого города России.\n" +
            "\n" +
            "  Ежемесячная плата за опцию – 40 руб.\n" +
            "\n" +
            "  Плата за первый месяц списывается при подключении опции.\n" +
            "\n" +
            "  Подробное описание: http://www.mts.ru/mob_connect/roaming/calls_across_russia/discounts/vyigodnyiy_mezhgorod/ </message><button href=\"request_2_portal.jsp?cmd=$MSISDN$*calls_city*activate\" row=\"1\" index=\"1\">Подключить</button><button href=\"request_2_portal.jsp?cmd=$MSISDN$*calls_city*deactivate\" row=\"1\" index=\"2\">Отключить</button><button href=\"discount_calls.jsp\" row=\"2\" index=\"3\">⬅ Назад</button><button href=\"index.jsp\" row=\"2\" index=\"4\">Главное меню</button></page>"
        ).getBytes(StandardCharsets.UTF_8)
    );

    final SADSRequest request = new SADSRequest() {
      @Override public Protocol getProtocol() { return FACEBOOK; }
      @Override public String getResourceURI() { return "/"; }
    };

    final List<Activity> rc =
        mbfPushInterceptor.createResponse(request, null, page, MbfPushInterceptor.getText(page));

    assertEquals(3, rc.size());
  }

}
