package com.ruijie.cloud.macc.dataplatform.task.ds.task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.JacksonUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class AbstractTask {

  protected ObjectNode dependence = JacksonUtils.createObjectNode();
  protected ObjectNode conditionResult;
  protected ObjectNode waitStartTimeout = JacksonUtils.createObjectNode();
  protected ObjectNode switchResult = JacksonUtils.createObjectNode();

  public abstract String getTaskType();
}
