package com.eyelinecom.whoisd.sads2.msbotframework.interceptors;

import com.eyelinecom.whoisd.sads2.common.StringUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

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

}
