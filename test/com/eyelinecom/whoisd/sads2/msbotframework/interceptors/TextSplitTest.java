package com.eyelinecom.whoisd.sads2.msbotframework.interceptors;

import com.eyelinecom.whoisd.sads2.common.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class TextSplitTest {

  @Test
  public void test1() {
    final String str1 = "\n-uno\n-dos\n-tres\n";
    assertEquals("[-uno, -dos, -tres]", StringUtils.splitText(str1, '\n', 5).toString());

    final String str2 = "\n\n-uno\n\n-dos\n-tres\n\n\n";
    assertEquals("[-uno, -dos, -tres]", StringUtils.splitText(str2, '\n', 5).toString());
  }

  @Test
  public void test2() {
    {
      final String str = "\n\n-uno\n\n-dos\n-tres\n\n\n";
      assertEquals("[-uno\n\n-dos, -tres]", StringUtils.splitText(str, '\n', 10).toString());
    }

    {
      final String str = "\n\n    -uno\n\n    -dos\n    -tres\n\n\n";
      assertEquals("[    -uno\n\n    -dos,     -tres]", StringUtils.splitText(str, '\n', 18).toString());
    }

    {
      final String str = "\n\n-uno\n\n-dos\n-tres\n\n\n";
      assertEquals("[-uno, -dos, -tres]", StringUtils.splitText(str, '\n', 4).toString());
    }

    {
      final String str = "\n\n-uno\n\n-dos\n-tres\n\n\n";
      assertEquals("[-uno\n\n-dos\n-tres]", StringUtils.splitText(str, '\n', 20).toString());
    }
  }

  @Test
  public void test3() {
    final String str = "300 рублей в месяц. В ежемесячную плату включено:\n\n" +
        "• 2 Гб мобильного Интернета;\n\n" +
        "• Безлимитные звонки на МТС;\n\n" +
        "• 250 минут на все сети России;\n\n" +
        "• 250 SMS абонентам домашнего региона;\n\n" +
        "• Трафик в пределах ресурсов «ВКонтакте», «Одноклассники&#8901;ru», Facebook – 0 рублей;\n\n" +
        "• Тариф действует на территории всей России.\n\n" +
        "Подробное описание: http://www.mts.ru/mob_connect/tariffs/tariffs/smart_mini/";

    final List<String> parts = StringUtils.splitText(str, '\n', 320);
    for (String part : parts) {
      assertTrue(part.length() < 320);
    }

    final List<String> smallerParts = StringUtils.splitText(str, '\n', 300);
    for (String part : smallerParts) {
      assertTrue(part.length() < 300);
    }

    final Pair<String, String> cutFromEnd = StringUtils.chopTail(str, '\n', 80);

    //noinspection ConstantConditions
    assertEquals(
        "Подробное описание: http://www.mts.ru/mob_connect/tariffs/tariffs/smart_mini/",
        cutFromEnd.getRight()
    );
  }

}
