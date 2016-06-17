package com.eyelinecom.whoisd.sads2.msbotframework.api.model.facebook;

import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ChannelData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Facebook-specific message properties.
 */
public class FacebookChannelData implements ChannelData {

  @JsonProperty("notification_type")
  @JsonSerialize(using = FacebookNotificationType.Serializer.class)
  @JsonDeserialize(using = FacebookNotificationType.Deserializer.class)
  private FacebookNotificationType notificationType;

  private Attachment attachment;

  public FacebookNotificationType getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(FacebookNotificationType notificationType) {
    this.notificationType = notificationType;
  }

  public Attachment getAttachment() {
    return attachment;
  }

  public void setAttachment(Attachment attachment) {
    this.attachment = attachment;
  }
}
