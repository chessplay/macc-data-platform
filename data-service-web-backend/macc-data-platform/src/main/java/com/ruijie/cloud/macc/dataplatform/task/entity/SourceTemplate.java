package com.ruijie.cloud.macc.dataplatform.task.entity;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.*;
import lombok.Data;

import java.util.List;


/**
 *
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Data
public class SourceTemplate  {

    private static final long serialVersionUID = 1L;



    private TableInfo tableInfo;


    private List<QueryFilterTemplate> sourceQueryFilter;

    private DataSourceModel dataSourceModel;

    private String partitionColumn;

    private Integer partitionNum;


}
