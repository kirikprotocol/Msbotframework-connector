package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.BOT_ADDED_TO_CONVERSATION;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.BOT_REMOVED_FROM_CONVERSATION;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.DELETE_USER_DATA;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.END_OF_CONVERSATION;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.MESSAGE;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.PING;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.USER_ADDED_TO_CONVERSATION;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.MessageType.USER_REMOVED_FROM_CONVERSATION;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class MessageTypeTest {

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"Message", MESSAGE},
        {"Ping", PING},
        {"DeleteUserData", DELETE_USER_DATA},
        {"BotAddedToConversation", BOT_ADDED_TO_CONVERSATION},
        {"BotRemovedFromConversation", BOT_REMOVED_FROM_CONVERSATION},
        {"UserAddedToConversation", USER_ADDED_TO_CONVERSATION},
        {"UserRemovedFromConversation", USER_REMOVED_FROM_CONVERSATION},
        {"EndOfConversation", END_OF_CONVERSATION}
    });
  }

  private String extValue;
  private MessageType value;

  public MessageTypeTest(String extValue, MessageType value) {
    this.extValue = extValue;
    this.value = value;
  }

  @Test
  public void testMarshal() throws Exception {
    final Message msg = new Message() {{
      setType(value);
    }};

    assertEquals(msg.marshal(), formatMessage(extValue));
  }

  @Test
  public void testUnmarshal() throws Exception {
    final Message msg =
        MarshalUtils.unmarshal(formatMessage(extValue), Message.class);

    assertEquals(msg.getType(), value);
  }

  private String formatMessage(String typeStr) {
    return "{\"type\":\"" + typeStr + "\"}";
  }

}
