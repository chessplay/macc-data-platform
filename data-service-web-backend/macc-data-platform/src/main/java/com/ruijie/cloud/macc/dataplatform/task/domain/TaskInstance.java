package com.ruijie.cloud.macc.dataplatform.task.domain;



import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务实例表 (mdp_task_instance) 实体类
 * 代表一个任务模板的一次完整运行，是所有子任务的父级。
 */
@Data
@Accessors(chain = true)
@TableName("mdp_task_instance")
public class TaskInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务实例的唯一ID (UUID)
     */
    @TableField("instance_id")
    private String instanceId;

    /**
     * 所属任务模板ID
     */
    @TableField("template_id")
    private Long templateId;


    /**
     * 任务实例名称
     */
    @TableField("name")
    private String name;

    /**
     * 任务实例的总体状态
     */
    @TableField("overall_status")
    private String overallStatus;

    /**
     * 子任务总数
     */
    @TableField("total_sub_tasks")
    private Integer totalSubTasks;

    /**
     * 成功完成的子任务数
     */
    @TableField("completed_sub_tasks")
    private Integer completedSubTasks;

    /**
     * 永久失败的子任务数
     */
    @TableField("failed_sub_tasks")
    private Integer failedSubTasks;

    /**
     * 启动时的原始请求参数
     */
    @TableField("initial_request_params_json")
    private String initialRequestParamsJson;

    /**
     * 创建者
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    /**
     * 提交时间
     */
    @TableField(value = "submission_time", fill = FieldFill.INSERT)
    private Date submissionTime;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}