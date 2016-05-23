package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

/**
 * A GEO location.
 */
public class Location {

  /**
   * Optional. Altitude.
   */
  Double altitude;

  /**
   * Latitude for the user when the message was created.
   */
  double latitude;

  /**
   * Longitude for the user when the message was created.
   */
  double longitude;

  public Location() {}

  public Double getAltitude() {
    return altitude;
  }

  public void setAltitude(Double altitude) {
    this.altitude = altitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
