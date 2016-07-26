package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use       = JsonTypeInfo.Id.NAME,
    include   = JsonTypeInfo.As.PROPERTY,
    property  = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Entity.Mention.class,          name = "mention"),
    @JsonSubTypes.Type(value = Entity.Place.class,            name = "Place"),
    @JsonSubTypes.Type(value = Entity.GeoCoordinates.class,   name = "GeoCoordinates")
})
public abstract class Entity extends ApiType<Entity> {

  private String type;

  public Entity() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  /**
   * <p>Many communication clients have mechanisms to "mention" someone.
   * Knowing that someone is mentioned can be an important piece of information for a bot
   * that the channel knows and needs to be able to pass to you.
   *
   * <p>Frequently a bot needs to know that they were mentioned, but with some channels they don't
   * always know what their name is on that channel.
   *
   * <p>NOTE: Mentions go both ways.
   * A bot may want to mention a user in a reply to a conversation. If they fill out
   * the Mentions object with the mention information then it allows the Channel to map it
   * to the mentioning semantics of the channel.
   */
  public static class Mention extends Entity {

    /**
     * ChannelAccount of the person or user who was mentiond
     */
    private ChannelAccount mentioned;

    /**
     * The text in the Activity.Text property which represents the mention.
     * (this can be empty or null)
     */
    private String text;

    public Mention() {}

    public ChannelAccount getMentioned() {
      return mentioned;
    }

    public void setMentioned(ChannelAccount mentioned) {
      this.mentioned = mentioned;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }


  /**
   * Place represents information from https://schema.org/Place.
   */
  public static class Place extends Entity {

    /**
     * String description or PostalAddress (future)
     */
    private String address;

    private GeoCoordinates geo;

    /**
     * URL to a map or complex "Map" object (future)
     */
    private String hasMap;

    /**
     * Name of the place
     */
    private String name;

    public Place() {}

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public GeoCoordinates getGeo() {
      return geo;
    }

    public void setGeo(GeoCoordinates geo) {
      this.geo = geo;
    }

    public String getHasMap() {
      return hasMap;
    }

    public void setHasMap(String hasMap) {
      this.hasMap = hasMap;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }


  public static class GeoCoordinates extends Entity {

    /**
     * Name of the place
     */
    private String name;

    /**
     * Longitude of the location WGS 84
     */
    private Double longitude;

    /**
     * Latitude of the location WGS 84
     */
    private Double latitude;

    /**
     * Elevation of the location WGS 84
     */
    private Double elevation;

    public GeoCoordinates() {}

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Double getLongitude() {
      return longitude;
    }

    public void setLongitude(Double longitude) {
      this.longitude = longitude;
    }

    public Double getLatitude() {
      return latitude;
    }

    public void setLatitude(Double latitude) {
      this.latitude = latitude;
    }

    public Double getElevation() {
      return elevation;
    }

    public void setElevation(Double elevation) {
      this.elevation = elevation;
    }
  }
}
