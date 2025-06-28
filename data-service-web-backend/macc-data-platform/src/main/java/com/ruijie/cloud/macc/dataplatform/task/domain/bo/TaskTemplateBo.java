package com.ruijie.cloud.macc.dataplatform.task.domain.bo;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruijie.cloud.dc.core.validate.AddGroup;
import com.ruijie.cloud.dc.core.validate.EditGroup;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskTemplateBo {

    private static final long serialVersionUID = 1L;


    @Null(message = "新增不需要任务id",groups = {AddGroup.class})
    @NotNull(message = "任务模板id不能为空", groups = { EditGroup.class })
    private Long id;

    private String name;

    private String sourceTableName;

    private String sinkTableName;

    @NotNull(message = "新增必须要传过滤条件",groups = {AddGroup.class})
    private List<QueryFilterTemplate> sourceQueryFilter;

    private String sourceDataKey;

    private String sinkDataKey;

    private LinkedHashMap<String,String> fieldMapping;


    private String description;

    @Null(message = "新增不需要工作流码",groups = {AddGroup.class})
    @JsonProperty("process_code")
    @NotNull(message = "工作流码不能为空", groups = { EditGroup.class })
    private Long processCode;

    @Null(message = "新增不需要任务码",groups = {AddGroup.class})
    @NotNull(message = "任务码不能为空", groups = { EditGroup.class })
    @JsonProperty("task_code")
    private Long taskCode;

    private Integer defaultPriorityLevel;

    private String defaultDsPriority;
}
