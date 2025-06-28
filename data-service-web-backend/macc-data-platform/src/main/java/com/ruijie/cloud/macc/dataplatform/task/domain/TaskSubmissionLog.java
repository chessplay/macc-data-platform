package com.ruijie.cloud.macc.dataplatform.task.domain;



import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务提交与状态跟踪日志 实体
 *
 * @author YourName
 * @date 2025-04-21 // 假设日期
 */
@Data
@Accessors(chain = true) // 支持链式调用 setter
@TableName("mdp_task_submission_log") // 对应数据库表名
public class TaskSubmissionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联ID (UUID)
     */
    @TableField("correlation_id")
    private String correlationId;

    /**
     * 批次ID (可选)
     */
    @TableField("batch_id")
    private String batchId;

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
     * 提交与跟踪状态
     */
    @TableField("submission_status")
    private String submissionStatus;

    /**
     * 从DS获取的最终实例状态
     */
    @TableField("final_ds_status")
    private String finalDsStatus;

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
    @TableField(value = "create_time", fill = FieldFill.INSERT) // MP 自动填充创建时间
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // MP 自动填充更新时间
    private Date updateTime;

    @TableField("yarn_application_id")
    private String yarnApplicationId;

    // --- 定义状态常量 (推荐使用枚举，这里用字符串示例) ---
    public static final String STATUS_QUEUED = "QUEUED";
    public static final String STATUS_SUBMITTING = "SUBMITTING";
    public static final String STATUS_SUBMIT_FAILED_NO_ID = "SUBMIT_FAILED_NO_ID"; // 提交成功但无法获取ID
    public static final String STATUS_SUBMIT_FAILED_API = "SUBMIT_FAILED_API";     // 调用API失败
    public static final String STATUS_TRACKING = "TRACKING";                       // 已获取ID，正在跟踪
    public static final String STATUS_COMPLETED = "COMPLETED";                     // 达到最终状态(成功/停止等)
    public static final String STATUS_FAILED_PERMANENTLY = "FAILED_PERMANENTLY";   // 重试耗尽后最终失败
    public static final String STATUS_STATUS_CHECK_ERROR = "STATUS_CHECK_ERROR";   // 检查状态时出错
    public static final String STATUS_RETRY_SCHEDULED = "RETRY_SCHEDULED";         // 已安排重试

}