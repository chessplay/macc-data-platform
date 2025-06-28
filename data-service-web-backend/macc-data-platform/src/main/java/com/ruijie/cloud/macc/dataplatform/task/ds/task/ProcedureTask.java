package com.ruijie.cloud.macc.dataplatform.task.ds.task;

import com.ruijie.cloud.macc.dataplatform.task.ds.process.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcedureTask extends AbstractTask {

  /** datasource type */
  private String type;

  /** datasource id */
  private Integer datasource;

  private String method;

  /** resource list */
  private List<TaskResource> resourceList = Collections.emptyList();

  private List<Parameter> localParams = Collections.emptyList();

  @Override
  public String getTaskType() {
    return "PROCEDURE";
  }
}
