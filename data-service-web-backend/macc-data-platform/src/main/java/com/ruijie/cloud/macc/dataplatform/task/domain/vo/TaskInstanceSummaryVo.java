package com.ruijie.cloud.macc.dataplatform.task.domain.vo;



import lombok.Data;
import java.util.Date;
import java.util.Map;

@Data
public class TaskInstanceSummaryVo {
    // 来自 mdp_task_instance 表的核心字段
    private String instanceId;
    private Long templateId;
    private String name;
    private String overallStatus;
    private Integer totalSubTasks;
    private Integer completedSubTasks;
    private Integer failedSubTasks;
    private Date submissionTime;
    private Date startTime;
    private Date endTime;

    // 来自 mdp_task_template 表的冗余字段，用于前端直接显示
    private String templateName;
    private String initialRequestParamsJson;
}