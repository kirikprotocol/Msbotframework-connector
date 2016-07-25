package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.ActivityType.DELETE_USER_DATA;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.ActivityType.MESSAGE;
import static com.eyelinecom.whoisd.sads2.msbotframework.api.model.ActivityType.PING;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ActivityTypeTest {

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"message", MESSAGE},
        {"ping", PING},
        {"deleteUserData", DELETE_USER_DATA},
    });
  }

  private String extValue;
  private ActivityType value;

  public ActivityTypeTest(String extValue, ActivityType value) {
    this.extValue = extValue;
    this.value = value;
  }

  @Test
  public void testMarshal() throws Exception {
    final Activity msg = new Activity() {{
      setType(value);
    }};

    assertEquals(msg.marshal(), formatMessage(extValue));
  }

  @Test
  public void testUnmarshal() throws Exception {
    final Activity msg =
        MarshalUtils.unmarshal(formatMessage(extValue), Activity.class);

    assertEquals(msg.getType(), value);
  }

  private String formatMessage(String typeStr) {
    return "{\"type\":\"" + typeStr + "\"}";
  }

}
