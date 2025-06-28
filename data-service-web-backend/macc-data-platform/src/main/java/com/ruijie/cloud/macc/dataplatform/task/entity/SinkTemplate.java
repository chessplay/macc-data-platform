package com.ruijie.cloud.macc.dataplatform.task.entity;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import lombok.Data;

import java.util.List;


/**
 *
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Data
public class SinkTemplate {

    private static final long serialVersionUID = 1L;


    private String tableName;


    private DataSourceModel dataSourceModel;


}
