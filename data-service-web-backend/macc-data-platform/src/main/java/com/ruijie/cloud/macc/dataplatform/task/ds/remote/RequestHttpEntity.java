package com.ruijie.cloud.macc.dataplatform.task.ds.remote;

import com.ruijie.cloud.macc.dataplatform.task.ds.util.JacksonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Data
@Slf4j
public class RequestHttpEntity {

  private Header header;

  private Query query;

  private Object body;

  public RequestHttpEntity() {}

  public RequestHttpEntity(Header header, Query query) {
    this.header = header;
    this.query = query;
  }

  public RequestHttpEntity(Header header, Query query, Object body) {
    this.header = header;
    this.query = query;
    this.body = body;
  }

  public RequestHttpEntity(Header header, Object body) {
    this.header = header;
    this.body = body;
  }

  /**
   * cast object to map when the object is instance of map
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, String> castBodyToMap() {
    if (ifBodyIsMap()) {
      return (Map<String, String>) body;
    }
    throw new UnsupportedOperationException(
        "the body is not instance of map,do not use this method");
  }

  /**
   * judge if body instance of map
   *
   * @return
   */
  public boolean ifBodyIsMap() {
    return body instanceof Map;
  }

  /**
   * param object to json string
   *
   * @return
   */
  public String bodyToJson() {
    return JacksonUtils.toJSONString(this.body);
  }

  /**
   * param object to map by reflect
   *
   * @return map
   */
  public Map<String, String> bodyToMap() {
    if (Objects.isNull(body)) {
      return null;
    }

    Map<String, String> map = new LinkedHashMap<>();
    Field[] declaredFields = body.getClass().getDeclaredFields();
    for (Field field : declaredFields) {
      field.setAccessible(true);
      try {
        Optional.ofNullable(field.get(body))
            .ifPresent(value -> map.put(field.getName(), value.toString()));
      } catch (IllegalAccessException e) {
        log.error("object to map fail", e);
      }
    }
    return map;
  }
}
