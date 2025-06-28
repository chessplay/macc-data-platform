package com.ruijie.cloud.macc.dataplatform.task.ds.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.JacksonUtils;

import java.lang.reflect.Type;

public class TypeReferenceHttpResult<T> extends TypeReference<HttpRestResult<T>> {

  protected final Type type;

  public TypeReferenceHttpResult(Class<?>... clazz) {
    type =
        JacksonUtils.getObjectMapper()
            .getTypeFactory()
            .constructParametricType(HttpRestResult.class, clazz);
  }

  @Override
  public Type getType() {
    return type;
  }
}
