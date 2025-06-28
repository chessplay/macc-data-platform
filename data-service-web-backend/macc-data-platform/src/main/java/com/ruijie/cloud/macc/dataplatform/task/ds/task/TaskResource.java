package com.ruijie.cloud.macc.dataplatform.task.ds.task;

import com.ruijie.cloud.macc.dataplatform.task.ds.remote.RequestHttpEntity;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * used by shell,python,spark,flink.... task
 *
 * <p>used when define task
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResource {

  private Long id;

  /**
   * must rewrite,then {@link RequestHttpEntity#bodyToMap()} can transfer object to json string
   *
   * @return object json string
   */
  @Override
  public String toString() {
    return JacksonUtils.toJSONString(this);
  }
}
