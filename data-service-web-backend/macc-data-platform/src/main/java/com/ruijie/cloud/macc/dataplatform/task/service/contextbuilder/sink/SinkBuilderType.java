package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.sink;


import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component // 让Spring自动识别为Bean
public @interface SinkBuilderType {
    DataSourceType value();
}

