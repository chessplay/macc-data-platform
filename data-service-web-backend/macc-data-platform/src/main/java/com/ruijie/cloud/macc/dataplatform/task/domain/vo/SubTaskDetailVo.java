package com.ruijie.cloud.macc.dataplatform.task.domain.vo;



import lombok.Data;
import java.util.Date;

@Data
public class SubTaskDetailVo {
    private Long logId; // mdp_task_submission_log 表的主键
    private String correlationId;
    private String name; // Add this line
    private Long templateId;
    private String taskInstanceId;
    private String requestParamsJson; // 提交给此子任务的参数
    private Integer dsInstanceId;    // DolphinScheduler 实例ID
    private String submissionStatus; // 应用内部的提交状态 (e.g., QUEUED, TRACKING)
    private String finalDsStatus;    // DS返回的原始状态码
    private String dsStatusString;   // finalDsStatus 的用户友好描述
    private Integer retryCount;
    private String errorMessage;
    private Date createTime;         // 子任务（日志条目）创建时间
    private Date updateTime;         // 子任务（日志条目）最后更新时间
    private String yarnApplicationId;
    private boolean yarnApplicationIdKnown;
    private String yarnLogUrl;       // 指向YARN RM上该App日志的直接链接
    // 可以根据需要添加从 ProcessInstanceQueryResp 中获取的其他DS实例信息
    // private String dsInstanceName;
    // private Date dsStartTime;
    // private Date dsEndTime;
}