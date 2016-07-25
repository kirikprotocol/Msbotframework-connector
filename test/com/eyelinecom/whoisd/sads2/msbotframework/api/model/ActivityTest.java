package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments.HeroCard;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class ActivityTest {

  @Test
  public void test1() throws MbfException {

    final Activity msg = new Activity();
    msg.setType(ActivityType.MESSAGE);
    msg.setText("hello");

    msg.setFrom(new ChannelAccount("123", null));
    msg.setRecipient(new ChannelAccount("456", null));

    msg.setAttachments(new ArrayList<MbfAttachment>() {{
      add(HeroCard.fromOptions("ok", new ArrayList<String>() {{
        add("1");
        add("2");
      }}));
    }});

    assertEquals(
        "{\"type\":\"message\",\"from\":{\"id\":\"123\"},\"recipient\":{\"id\":\"456\"},\"text\":\"hello\",\"attachments\":[{\"contentType\":\"application/vnd.microsoft.card.hero\",\"content\":{\"text\":\"ok\",\"buttons\":[{\"type\":\"imBack\",\"title\":\"1\",\"value\":\"1\"},{\"type\":\"imBack\",\"title\":\"2\",\"value\":\"2\"}]}}]}",
        msg.marshal()
    );
  }

}
