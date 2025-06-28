package com.ruijie.cloud.macc.dataplatform.task.ds.instance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/** copied from org.apache.dolphinscheduler.dao.entity.ProcessInstance */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInstanceTasksQueryResp {

  private String processInstanceState;
  private List<Task> taskList;

  @Data
  @Accessors(chain = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Task {
    private Long id;
    private String name;
    private String taskType;
    private Long processInstanceId;
    private Long taskCode;
    private Integer taskDefinitionVersion;
    private String processInstanceName;
    private Integer taskGroupPriority;
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String firstSubmitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String endTime;
    private String host;
    private String executePath;
    private String logPath;
    private Integer retryTimes;
    private String alertFlag;

  }
}
