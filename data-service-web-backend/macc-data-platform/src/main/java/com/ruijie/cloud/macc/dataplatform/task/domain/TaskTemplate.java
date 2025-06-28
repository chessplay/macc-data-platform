package com.ruijie.cloud.macc.dataplatform.task.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ruijie.cloud.dc.core.domain.BaseEntity;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.ParameterDescriptor;
import com.ruijie.cloud.macc.dataplatform.task.handler.QueryFilterTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName(value = "mdp_task_template",autoResultMap = true)
public class TaskTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String sourceTableName;

    private String sinkTableName;

    @TableField(typeHandler = QueryFilterTypeHandler.class)
    private List<QueryFilterTemplate> sourceQueryFilter;

    private String sourceDataKey;

    private String sinkDataKey;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private LinkedHashMap<String,String> fieldMapping;

    @TableField(typeHandler = JacksonTypeHandler.class)
    // 【修改】将字段类型从 HashMap<String,String> 修改为 List<ParameterDescriptor>
    private List<ParameterDescriptor> startParams;

    private String requestExample;

    private String description;

    private Long processCode;

    private Long taskCode;

    private Integer defaultPriorityLevel;

    /**
     * 默认DolphinScheduler实例优先级 (HIGHEST, HIGH, MEDIUM, LOW, LOWEST)
     */
    private String defaultDsPriority;

}
