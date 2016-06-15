package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static junit.framework.Assert.assertEquals;

public class MessageTest {

  @Test
  public void test1() throws MbfException {

    final Message msg = new Message(
        "hello",
        singletonList(
            new MbfAttachment(new Action[]{
                new Action("1", "1!"), new Action("2", "2!"),
            })
        ))
    {{
      setFrom(new ChannelAccount(ChannelType.FACEBOOK, "123"));
      setTo(new ChannelAccount(ChannelType.FACEBOOK, "456"));
    }};

    assertEquals(
        "{\"text\":\"hello\",\"attachments\":[{\"actions\":[{\"title\":\"1\",\"message\":\"1!\"},{\"title\":\"2\",\"message\":\"2!\"}]}],\"from\":{\"channelId\":\"facebook\",\"address\":\"123\"},\"to\":{\"channelId\":\"facebook\",\"address\":\"456\"}}",
        msg.marshal());
  }

}
