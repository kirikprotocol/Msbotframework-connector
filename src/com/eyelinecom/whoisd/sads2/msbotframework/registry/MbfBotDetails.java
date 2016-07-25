package com.eyelinecom.whoisd.sads2.msbotframework.registry;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class MbfBotDetails {

  /**
   * Refresh OAuth token a little bit before it expires to account for any possible lag.
   */
  private static final long TOKEN_REFRESH_MARGIN_MILLIS = TimeUnit.MINUTES.toMillis(10);

  /** MSA identifier. */
  private final String appId;
  private final String appSecret;

  private transient String token;

  /** Date of token acquisition + the value of {@literal expires_in} header. */
  private transient Date expiresIn;

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

  public boolean shouldRefreshToken() {
    return
        token == null ||
            expiresIn == null ||
            expiresIn.before(new Date(System.currentTimeMillis() + TOKEN_REFRESH_MARGIN_MILLIS));
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token, long expiresInSeconds) {
    this.token = checkNotNull(token);
    this.expiresIn =
        new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expiresInSeconds));
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
