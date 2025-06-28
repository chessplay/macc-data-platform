package com.ruijie.cloud.macc.dataplatform.task.ds.task;

import com.ruijie.cloud.macc.dataplatform.task.ds.process.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/** copied from org.apache.dolphinscheduler.plugin.task.api.parameters.SqlParameters */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SeaTunnelTask extends AbstractTask {


  private List<Parameter> localParams = Collections.emptyList();

  private List<TaskResource> resourceList = Collections.emptyList();

  private String startupScript;

  private Boolean useCustom;

  private String deployMode;

  private String runMode;

  /** shell script */
  private String rawScript;

  private String others;

  @Override
  public String getTaskType() {
    return "SEATUNNEL";
  }
}
