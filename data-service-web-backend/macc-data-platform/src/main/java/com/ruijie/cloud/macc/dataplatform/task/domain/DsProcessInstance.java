package com.ruijie.cloud.macc.dataplatform.task.domain;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * DolphinScheduler 流程实例表 (t_ds_process_instance) 的简化实体映射
 * 只包含 fetchInstanceIdWithRetry 需要的字段
 */


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data; // 确保引入 Lombok

import java.util.Date;

/**
 * DolphinScheduler 流程实例表 (t_ds_process_instance) 的简化实体映射
 * 包含 fetchInstanceIdWithRetry 和 getProcessInstanceStatusFromDS 需要的字段
 */
@Data // Lombok 注解，自动生成 getter, setter, toString 等
@TableName("t_ds_process_instance") // 指定数据库表名
public class DsProcessInstance {

    @TableId("id") // 标记主键字段，并映射到数据库的 'id' 列
    private Integer id; // DS 的实例 ID 类型通常是 INT

    @TableField("process_definition_code") // 映射到 'process_definition_code' 列
    private Long processDefinitionCode;

    @TableField("command_param") // 映射到 'command_param' 列
    private String commandParam;

    @TableField("start_time") // 映射到 'start_time' 列
    private Date startTime;

    // --- 新增的 state 字段 ---
    /**
     * 实例状态，映射到数据库中的 'state' 列。
     * 可能的值如：SUCCESS, FAILURE, RUNNING_EXECUTION 等 (具体取决于 DS 版本和枚举定义)
     */
    @TableField("state") // 告诉 MybatisPlus 这个字段对应数据库的 'state' 列
    private String state;
    // ------------------------

    // @Data 注解会自动为此字段生成 getState() 和 setState() 方法

    // 您仍然可以在这里根据需要添加其他字段...
}