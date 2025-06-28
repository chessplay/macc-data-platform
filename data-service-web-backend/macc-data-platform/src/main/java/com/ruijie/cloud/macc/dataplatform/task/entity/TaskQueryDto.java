package com.ruijie.cloud.macc.dataplatform.task.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2023/12/11 9:48
 **/
@Data
public class TaskQueryDto {



    private String name;

    private String sourceTableName;

    private String sinkTableName;



    private String sourceDataKey;

    private String sinkDataKey;


}
