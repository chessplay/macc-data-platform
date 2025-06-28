package com.ruijie.cloud.macc.dataplatform.task.domain.vo;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ruijie.cloud.dc.core.domain.BaseEntity;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Data要素视图对象 llm_mp_data_factor
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskTemplateVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String sourceTableName;

    private String sinkTableName;

    private List<QueryFilterTemplate> sourceQueryFilter;

    private String sourceDataKey;

    private String sinkDataKey;

    private LinkedHashMap<String,String> fieldMapping;

    @TableField(typeHandler = JacksonTypeHandler.class)
    // 【修改】将 startParams 的类型从 HashMap<String, String> 修改为 List<ParameterDescriptor>
    private List<ParameterDescriptor> startParams;

    private String requestExample;

    private String description;

    private Long processCode;

    private Long taskCode;

    private Integer defaultPriorityLevel;
    private String defaultDsPriority;

}
