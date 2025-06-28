package com.ruijie.cloud.macc.dataplatform.task.entity;

import lombok.Data;

/**
 * 子任务列表查询的数据传输对象
 */
@Data
public class SubTaskQueryDto {

    private String nameFilter; // Add this line
    /**
     * 根据模板名称进行模糊筛选
     */
    private String templateNameFilter;

    /**
     * 根据子任务的提交状态进行精确筛选
     */
    private String submissionStatusFilter;

    private String instanceIdFilter;
}