package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.common;

import com.ruijie.cloud.macc.dataplatform.task.entity.CommonTemplate;
import org.apache.velocity.VelocityContext;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/07/29 15:03
 **/
public class Commonbuilder {
    public void buildContext(VelocityContext context, CommonTemplate commonTemplate) {
        context.put("parallelism", commonTemplate.getParallelism());
        context.put("mode", commonTemplate.getMode());
        context.put("interval", commonTemplate.getInterval());
        context.put("queue", commonTemplate.getQueue());
    }
}