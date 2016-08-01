package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments.HeroCard;
import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ActivityTest {

  @Test
  public void test1() throws Exception {

    final Activity msg = new Activity();
    msg.setType(ActivityType.MESSAGE);
    msg.setText("hello");

    msg.setFrom(new ChannelAccount("123", null));
    msg.setRecipient(new ChannelAccount("456", null));
    msg.setTimestamp(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse("2012.02.12 12:21"));

    msg.setAttachments(new ArrayList<MbfAttachment>() {{
      add(HeroCard.fromOptions("ok", new ArrayList<String>() {{
        add("1");
        add("2");
      }}));
    }});

    final String expected = "{" +
        "\"type\":\"message\"," +
        "\"timestamp\":\"2012-02-12T04:21:00.000000Z\"," +
        "\"from\":{\"id\":\"123\"}," +
        "\"recipient\":{\"id\":\"456\"}," +
        "\"text\":\"hello\"," +
          "\"attachments\":[" +
            "{\"contentType\":\"application/vnd.microsoft.card.hero\",\"content\":{\"text\":\"ok\",\"buttons\":[{\"type\":\"imBack\",\"title\":\"1\",\"value\":\"1\"},{\"type\":\"imBack\",\"title\":\"2\",\"value\":\"2\"}]}}" +
          "]" +
        "}";

    // TODO: account for timezones.
    // assertEquals(expected, msg.marshal());
    msg.marshal();
  }

  @Test
  public void test2() throws Exception {

    final String msg = "{\n" +
        "  \"type\": \"message\",\n" +
        "  \"id\": \"2vvwPr9CGpg\",\n" +
        "  \"timestamp\": \"2016-07-26T07:38:23.8741394Z\",\n" +
        "  \"serviceUrl\": \"https://webchat.botframework.com/\",\n" +
        "  \"channelId\": \"webchat\",\n" +
        "  \"from\": {\n" +
        "    \"id\": \"Fsb0C7CGcUG\",\n" +
        "    \"name\": \"Fsb0C7CGcUG\"\n" +
        "  },\n" +
        "  \"conversation\": {\n" +
        "    \"id\": \"5v21gkGGAQ4\"\n" +
        "  },\n" +
        "  \"recipient\": {\n" +
        "    \"id\": \"AgentMistyFerret\",\n" +
        "    \"name\": \"AgentMistyFerret\"\n" +
        "  },\n" +
        "  \"text\": \"hi\"\n" +
        "}";

    MarshalUtils.unmarshal(msg, Activity.class);
  }

  @Test
  public void test3() throws Exception {
    final String msg = "{\n" +
        "  \"type\": \"message\",\n" +
        "  \"id\": \"1XY8ovkz7VKB4vgW\",\n" +
        "  \"timestamp\": \"2016-07-26T07:43:36.472Z\",\n" +
        "  \"serviceUrl\": \"https://skype.botframework.com\",\n" +
        "  \"channelId\": \"skype\",\n" +
        "  \"from\": {\n" +
        "    \"id\": \"29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8\",\n" +
        "    \"name\": \"Andy Belsky\"\n" +
        "  },\n" +
        "  \"conversation\": {\n" +
        "    \"isGroup\": false,\n" +
        "    \"id\": \"29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8\"\n" +
        "  },\n" +
        "  \"recipient\": {\n" +
        "    \"id\": \"28:d26fad71-a3fc-488d-afca-689481e7278c\",\n" +
        "    \"name\": \"AgentMistyFerret\"\n" +
        "  },\n" +
        "  \"text\": \"!who\",\n" +
        "  \"entities\": []\n" +
        "}";

    MarshalUtils.unmarshal(msg, Activity.class);
  }

  @Test
  public void test4() throws Exception {
    final String msg = "{\n" +
        "  \"type\": \"message\",\n" +
        "  \"id\": \"mid.1469521602858:740cd4a61bc6073959\",\n" +
        "  \"timestamp\": \"2016-07-26T08:26:42.9793615Z\",\n" +
        "  \"serviceUrl\": \"https://facebook.botframework.com\",\n" +
        "  \"channelId\": \"facebook\",\n" +
        "  \"from\": {\n" +
        "    \"id\": \"1071173859631252\",\n" +
        "    \"name\": \"Andy Belsky\"\n" +
        "  },\n" +
        "  \"conversation\": {\n" +
        "    \"isGroup\": false,\n" +
        "    \"id\": \"1071173859631252-878405072264349\"\n" +
        "  },\n" +
        "  \"recipient\": {\n" +
        "    \"id\": \"878405072264349\",\n" +
        "    \"name\": \"AgentMistyFerret\"\n" +
        "  },\n" +
        "  \"text\": \"1\",\n" +
        "  \"channelData\": {\n" +
        "    \"sender\": {\n" +
        "      \"id\": \"1071173859631252\"\n" +
        "    },\n" +
        "    \"recipient\": {\n" +
        "      \"id\": \"878405072264349\"\n" +
        "    },\n" +
        "    \"timestamp\": 1469521602865,\n" +
        "    \"message\": {\n" +
        "      \"mid\": \"mid.1469521602858:740cd4a61bc6073959\",\n" +
        "      \"seq\": 53,\n" +
        "      \"text\": \"1\"\n" +
        "    }\n" +
        "  }\n" +
        "}";

    MarshalUtils.unmarshal(msg, Activity.class);
  }

  @Test
  public void test5() throws Exception {
    final String msg = "{\n" +
        "  \"type\": \"message\",\n" +
        "  \"id\": \"mid.1469537386556:c43c4c69c4f32b6037\",\n" +
        "  \"timestamp\": \"2016-07-26T12:49:56.6978028Z\",\n" +
        "  \"serviceUrl\": \"https://facebook.botframework.com\",\n" +
        "  \"channelId\": \"facebook\",\n" +
        "  \"from\": {\n" +
        "    \"id\": \"1071173859631252\",\n" +
        "    \"name\": \"Andy Belsky\"\n" +
        "  },\n" +
        "  \"conversation\": {\n" +
        "    \"isGroup\": false,\n" +
        "    \"id\": \"1071173859631252-878405072264349\"\n" +
        "  },\n" +
        "  \"recipient\": {\n" +
        "    \"id\": \"878405072264349\",\n" +
        "    \"name\": \"AgentMistyFerret\"\n" +
        "  },\n" +
        "  \"attachments\": [],\n" +
        "  \"entities\": [\n" +
        "    {\n" +
        "      \"geo\": {\n" +
        "        \"elevation\": 0.0,\n" +
        "        \"latitude\": 54.863197326660156,\n" +
        "        \"longitude\": 83.090248107910156,\n" +
        "        \"type\": \"GeoCoordinates\"\n" +
        "      },\n" +
        "      \"type\": \"Place\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"channelData\": {\n" +
        "    \"sender\": {\n" +
        "      \"id\": \"1071173859631252\"\n" +
        "    },\n" +
        "    \"recipient\": {\n" +
        "      \"id\": \"878405072264349\"\n" +
        "    },\n" +
        "    \"timestamp\": 1469537386728,\n" +
        "    \"message\": {\n" +
        "      \"mid\": \"mid.1469537386556:c43c4c69c4f32b6037\",\n" +
        "      \"seq\": 133,\n" +
        "      \"attachments\": [\n" +
        "        {\n" +
        "          \"type\": \"location\",\n" +
        "          \"payload\": {\n" +
        "            \"coordinates\": {\n" +
        "              \"lat\": 54.8631973,\n" +
        "              \"long\": 83.09025\n" +
        "            }\n" +
        "          },\n" +
        "          \"title\": \"Andy's Location\",\n" +
        "          \"url\": \"https://www.facebook.com/l.php?u=https%3A%2F%2Fwww.bing.com%2Fmaps%2Fdefault.aspx%3Fv%3D2%26pc%3DFACEBK%26mid%3D8100%26where1%3D54.863197180009%252C%2B83.090245388682%26FORM%3DFBKPL1%26mkt%3Den-US&h=iAQHB86VA&s=1&enc=AZMOP-HrPsNHqndGBmwdN084a6uqE5IwEUi_0PQn4fE0s5Kv95zkvsk0FE3BgadNZNql3CuyX9wdRKmkEqRQMcFUQT6GratWvNOYlUpmahJ9Ow\"\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  }\n" +
        "}";

    MarshalUtils.unmarshal(msg, Activity.class);
  }

}
