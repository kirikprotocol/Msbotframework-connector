package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ChannelAccount;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.Message;
import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;
import org.junit.Test;

import java.io.IOException;

import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.ChannelType.FACEBOOK;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook.Button.postback;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ChannelDataTest {

  @Test
  public void test1() throws MbfException, IOException {
    final Message msg = new Message() {{
      setFrom(new ChannelAccount(FACEBOOK, "123"));
      setTo(new ChannelAccount(FACEBOOK, "456"));
    }};

    final GenericTemplate template = new GenericTemplate(
        asList(
            new Bubble() {{
              setTitle("Bubble 1");
              setButtons(asList(
                  postback("Label 1"),
                  postback("Label 2"),
                  postback("Label 3")
              ));
            }},
            new Bubble() {{
              setTitle("Bubble 2");
              setButtons(asList(
                  postback("Label 4"),
                  postback("Label 5"),
                  postback("Label 6")
              ));
            }}
        )
    );

    msg.setChannelData(
        new FacebookChannelData() {{
          setAttachment(new TemplateAttachment(template));
        }}
    );

    assertEquals(
        "{\n" +
            "  \"from\" : {\n" +
            "    \"channelId\" : \"facebook\",\n" +
            "    \"address\" : \"123\"\n" +
            "  },\n" +
            "  \"to\" : {\n" +
            "    \"channelId\" : \"facebook\",\n" +
            "    \"address\" : \"456\"\n" +
            "  },\n" +
            "  \"channelData\" : {\n" +
            "    \"attachment\" : {\n" +
            "      \"type\" : \"template\",\n" +
            "      \"payload\" : {\n" +
            "        \"template_type\" : \"generic\",\n" +
            "        \"elements\" : [ {\n" +
            "          \"title\" : \"Bubble 1\",\n" +
            "          \"buttons\" : [ {\n" +
            "            \"type\" : \"postback\",\n" +
            "            \"title\" : \"Label 1\",\n" +
            "            \"payload\" : \"Label 1\"\n" +
            "          }, {\n" +
            "            \"type\" : \"postback\",\n" +
            "            \"title\" : \"Label 2\",\n" +
            "            \"payload\" : \"Label 2\"\n" +
            "          }, {\n" +
            "            \"type\" : \"postback\",\n" +
            "            \"title\" : \"Label 3\",\n" +
            "            \"payload\" : \"Label 3\"\n" +
            "          } ]\n" +
            "        }, {\n" +
            "          \"title\" : \"Bubble 2\",\n" +
            "          \"buttons\" : [ {\n" +
            "            \"type\" : \"postback\",\n" +
            "            \"title\" : \"Label 4\",\n" +
            "            \"payload\" : \"Label 4\"\n" +
            "          }, {\n" +
            "            \"type\" : \"postback\",\n" +
            "            \"title\" : \"Label 5\",\n" +
            "            \"payload\" : \"Label 5\"\n" +
            "          }, {\n" +
            "            \"type\" : \"postback\",\n" +
            "            \"title\" : \"Label 6\",\n" +
            "            \"payload\" : \"Label 6\"\n" +
            "          } ]\n" +
            "        } ]\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}",
        msg.pretty());

    final Message unmarshalled = MarshalUtils.unmarshal(msg.pretty(), Message.class);
    assertNull(
        "Channel data should be ignored during unmarshalling as it's of no use to us" +
            " (and this also simplifies JSON mapping)",
        unmarshalled.getChannelData());
    assertNotNull(unmarshalled.getFrom());
  }

}
