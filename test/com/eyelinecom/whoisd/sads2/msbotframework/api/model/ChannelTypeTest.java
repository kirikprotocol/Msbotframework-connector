package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ChannelTypeTest {

  @Test
  public void test1() {
    assertEquals(ChannelType.WEB_CHAT, ChannelType.deserialize("webchat"));
  }

}
