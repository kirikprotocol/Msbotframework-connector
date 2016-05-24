package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

/**
 * A GEO location.
 *
 * @see <a href="https://dev.onedrive.com/facets/location_facet.htm">Location facet</a>
 */
public class Location {

  /**
   * Optional. Altitude.
   */
  private Double altitude;

  /**
   * Latitude for the user when the message was created.
   */
  private double latitude;

  /**
   * Longitude for the user when the message was created.
   */
  private double longitude;

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
