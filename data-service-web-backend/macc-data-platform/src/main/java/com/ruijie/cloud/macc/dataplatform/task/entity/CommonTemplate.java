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
public class CommonTemplate {

    private static final long serialVersionUID = 1L;


    private Integer parallelism;

    private String mode;

    private Integer interval;

    private String queue;



}
