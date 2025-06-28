package com.ruijie.cloud.macc.dataplatform.task.ds.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ConditionTask extends AbstractTask {

  @Override
  public String getTaskType() {
    return "CONDITIONS";
  }
}
