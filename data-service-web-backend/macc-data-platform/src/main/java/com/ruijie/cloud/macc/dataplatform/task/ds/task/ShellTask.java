package com.ruijie.cloud.macc.dataplatform.task.ds.task;

import com.ruijie.cloud.macc.dataplatform.task.ds.process.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ShellTask extends AbstractTask {

  /** resource list */
  private List<TaskResource> resourceList = Collections.emptyList();

  private List<Parameter> localParams = Collections.emptyList();

  /** shell script */
  private String rawScript;

  @Override
  public String getTaskType() {
    return "SHELL";
  }
}
