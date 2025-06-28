package com.ruijie.cloud.macc.dataplatform.task.domain.bo;


import com.ruijie.cloud.dc.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Data要素视图对象 llm_mp_data_factor
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceBo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long processCode;

}
