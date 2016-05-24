package com.eyelinecom.whoisd.sads2.msbotframework.registry;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class MbfBotDetails {

  private final String appId;
  private final String appSecret;

  public MbfBotDetails(String appId, String appSecret) {
    this.appId = checkNotNull(appId);
    this.appSecret = appSecret;
  }

  public String getAppId() {
    return appId;
  }

  public String getAppSecret() {
    return appSecret;
  }

  boolean matches(String appId) {
    return Objects.equals(this.appId, appId);
  }

  @Override
  public boolean equals(Object o) {
    return this == o ||
        o instanceof MbfBotDetails &&
            Objects.equals(getAppId(), ((MbfBotDetails) o).getAppId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAppId());
  }

  @Override
  public String toString() {
    return "MbfBotDetails{appId='" + appId + "', appSecret='" + appSecret + "'}";
  }
}
