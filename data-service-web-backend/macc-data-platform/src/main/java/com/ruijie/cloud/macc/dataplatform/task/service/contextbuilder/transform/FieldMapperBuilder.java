package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.transform;

import com.ruijie.cloud.macc.dataplatform.task.entity.FieldMapperTransformTemplate;
import org.apache.velocity.VelocityContext;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/07/29 13:52
 **/

public class FieldMapperBuilder  {

    public void buildContext(VelocityContext context, FieldMapperTransformTemplate tranformTemplate) {

        context.put("sourceTableName", tranformTemplate.getSourceTableName());
        context.put("resultTableName", tranformTemplate.getSinkTableName());

        context.put("fieldMapper", tranformTemplate.getFieldMapping());
    }
}