package com.eyelinecom.whoisd.sads2.msbotframework.api;

import com.eyelinecom.whoisd.sads2.common.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;

import java.net.URLDecoder;

public class LocationExtractor {

  /**
   * @return Latitude and longitude (in this specific order).
   */
  public Pair<Double, Double> extractLocation(Log log, String fbUrl) {
    try {
      final String bingUrl = URLDecoder.decode(UrlUtils.getParameter(fbUrl, "u"), "UTF-8");
      final String where = UrlUtils.getParameter(bingUrl, "where1");

      final String[] latLon = StringUtils.split(where, ",");
      return ImmutablePair.of(Double.parseDouble(latLon[0]), Double.parseDouble(latLon[1]));

    } catch (Exception e) {
      log.warn("Failed extracting location from URL [" + fbUrl + "]", e);
      return null;
    }
  }

}
