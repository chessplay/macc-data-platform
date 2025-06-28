package com.ruijie.cloud.macc.dataplatform.task.ds.process;

import com.ruijie.cloud.macc.dataplatform.task.ds.remote.RequestHttpEntity;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TaskLocation {

  private Long taskCode;

  private int x;

  private int y;

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
