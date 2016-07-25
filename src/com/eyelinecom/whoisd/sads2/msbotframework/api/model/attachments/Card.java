package com.eyelinecom.whoisd.sads2.msbotframework.api.model.attachments;

import com.eyelinecom.whoisd.sads2.msbotframework.api.model.ApiType;
import com.eyelinecom.whoisd.sads2.msbotframework.api.model.MbfAttachment;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Card<T extends Card> extends ApiType<T> {

  @JsonIgnore
  protected abstract String getContentType();

  public MbfAttachment toAttachment() {
    final MbfAttachment attachment = new MbfAttachment();
    attachment.setContentType(getContentType());
    attachment.setContent(this);

    return attachment;
  }

}
