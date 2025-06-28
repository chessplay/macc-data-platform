package com.ruijie.cloud.macc.dataplatform.task.domain;



import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 子任务实例表 (mdp_subtask_instance) 实体类
 * 由原 TaskSubmissionLog 重构而来，记录每个子任务的执行详情。
 */
@Data
@Accessors(chain = true)
// [改动点] 表名已更新
@TableName("mdp_subtask_instance")
public class SubtaskInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 子任务实例名称
     */
    @TableField("name")
    private String name;
    /**
     * 关联ID (UUID)，用于单个子任务的唯一追踪
     */
    @TableField("correlation_id")
    private String correlationId;

    /**
     * [改动点] 所属任务实例ID (原 batch_id)
     * 用于关联到 mdp_task_instance 表的 instance_id 字段
     */
    @TableField("task_instance_id")
    private String taskInstanceId;

    /**
     * 任务模板ID
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 提交给DS的实际参数(JSON)
     */
    @TableField("request_params_json")
    private String requestParamsJson;

    /**
     * 成功获取到的DolphinScheduler实例ID
     */
    @TableField("ds_instance_id")
    private Integer dsInstanceId;

    /**
     * 提交与跟踪状态 (应用内部状态)
     */
    @TableField("submission_status")
    private String submissionStatus;

    /**
     * 从DS获取的最终实例状态
     */
    @TableField("final_ds_status")
    private String finalDsStatus;

    /**
     * YARN Application ID
     */
    @TableField("yarn_application_id")
    private String yarnApplicationId;

    /**
     * 当前重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // [补充] 建议保留原有的状态常量，它们对于业务逻辑依然有效
    public static final String STATUS_QUEUED = "QUEUED";
    public static final String STATUS_SUBMITTING = "SUBMITTING";
    public static final String STATUS_SUBMIT_FAILED_NO_ID = "SUBMIT_FAILED_NO_ID";
    public static final String STATUS_SUBMIT_FAILED_API = "SUBMIT_FAILED_API";
    public static final String STATUS_TRACKING = "TRACKING";

    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED_PERMANENTLY = "FAILED_PERMANENTLY";
    public static final String STATUS_STATUS_CHECK_ERROR = "STATUS_CHECK_ERROR";
    public static final String STATUS_RETRY_SCHEDULED = "RETRY_SCHEDULED";
    public static final String STATUS_STOPPED = "STOPPED";
}