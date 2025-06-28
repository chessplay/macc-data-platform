package com.ruijie.cloud.macc.dataplatform.task.entity;



import lombok.Data;

@Data
public class TaskInstanceQueryDto {

    /**
     * 根据模板名称进行模糊筛选
     */
    private String templateNameFilter;

    /**
     * 根据批次的总体状态进行筛选
     */
    private String statusFilter;

    private String instanceIdFilter;
}