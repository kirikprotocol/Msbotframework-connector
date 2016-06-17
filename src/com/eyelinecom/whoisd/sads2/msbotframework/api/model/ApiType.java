package com.eyelinecom.whoisd.sads2.msbotframework.api.model;

import com.eyelinecom.whoisd.sads2.common.TypeUtil;
import com.eyelinecom.whoisd.sads2.msbotframework.MbfException;
import com.eyelinecom.whoisd.sads2.msbotframework.util.MarshalUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class ApiType<T extends ApiType> {

  @JsonIgnore
  protected final Class<T> entityClass;

  public ApiType() {
    this.entityClass = getEntityClass();
  }

  protected Class<T> getEntityClass() {
    return TypeUtil.getGenericType(getClass(), 0);
  }

  public static <T extends ApiType> T unmarshal(JsonNode obj,
                                                Class<T> clazz) throws MbfException {

    try {
      return MarshalUtils.unmarshal(obj, clazz);

    } catch (Exception e) {
      throw new MbfException("Unable to unmarshal API object [" + obj + "]", e);
    }
  }

  public String marshal() throws MbfException {
    return marshal(this, false);
  }

  public String pretty() throws MbfException {
    return marshal(this, true);
  }

  protected static <T extends ApiType> String marshal(T obj) throws MbfException {
    return marshal(obj, false);
  }

  protected static <T extends ApiType> String marshal(T obj, boolean pretty) throws MbfException {
    try {
      return pretty ? MarshalUtils.pretty(obj) : MarshalUtils.marshal(obj);

    } catch (Exception e) {
      throw new MbfException("Unable to marshal API object [" + obj + "]", e);
    }
  }

  @Override
  public String toString() {
    try {
      return "ApiType{" +
          "entityClass=" + entityClass +
          ",json=" + marshal() +
          '}';
    } catch (Exception e) {
      return "ApiType{entityClass=" + entityClass + '}';
    }
  }
}
