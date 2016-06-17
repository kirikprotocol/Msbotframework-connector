package com.eyelinecom.whoisd.sads2.msbotframework.api;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class LocationAttachmentTest {

  @Test
  public void test1() {
    final String bingUrl = "https://www.facebook.com/l.php?" +
        "u=https%3A%2F%2Fwww.bing.com%2Fmaps%2Fdefault.aspx%3Fv%3D2%26pc%3DFACEBK%26mid%3D8100%26where1%3D54.863134617968%252C%2B83.090189024883%26FORM%3DFBKPL1%26mkt%3Den-US&h=nAQGJzw8c&s=1&enc=AZNlpPhYZBKLElwx2Hvxsw62qTxcYY6EyDWJNDEV008pbbYLtftsEQejl00VdV5Q8lpScr7r3FxGyFusyTn15IH8lvFehYK9Fh_G22xog_JmlQ";

    assertEquals(
        ImmutablePair.of(54.863134617968, 83.090189024883),
        new LocationExtractor().extractLocation(null, bingUrl)
    );
  }
}
