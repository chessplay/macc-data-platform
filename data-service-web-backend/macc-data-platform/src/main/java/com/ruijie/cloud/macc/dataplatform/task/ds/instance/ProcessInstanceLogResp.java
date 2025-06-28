package com.ruijie.cloud.macc.dataplatform.task.ds.instance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

/** copied from org.apache.dolphinscheduler.dao.entity.ProcessInstance */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInstanceLogResp {

  /** id */
  private int lineNum;

  private String message;
}
