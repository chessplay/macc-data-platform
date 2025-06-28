package com.ruijie.cloud.macc.dataplatform.task.entity;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Data
public class FieldMapperTransformTemplate {

    private static final long serialVersionUID = 1L;


    private String sourceTableName;

    private String sinkTableName;

    private LinkedHashMap<String,String> fieldMapping;

}
