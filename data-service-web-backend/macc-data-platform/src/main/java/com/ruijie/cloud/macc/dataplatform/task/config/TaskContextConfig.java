package com.ruijie.cloud.macc.dataplatform.task.config;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.sink.SinkBuilder;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.sink.SinkBuilderType;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.source.SourceBuilder;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.source.SourceBuilderType;

@Configuration
public class TaskContextConfig implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	public Map<DataSourceType, SourceBuilder> sourceBuilderMap(ApplicationContext applicationContext) {
		Map<String, Object> strategyBeans = applicationContext.getBeansWithAnnotation(SourceBuilderType.class);
		Map<DataSourceType, SourceBuilder> sourceBuilderMap = new HashMap<>();
		strategyBeans.forEach((name, bean) -> {
			SourceBuilderType typeAnnotation = bean.getClass().getAnnotation(SourceBuilderType.class);
			sourceBuilderMap.put(typeAnnotation.value(), (SourceBuilder) bean);
		});
		return sourceBuilderMap;
	}

	@Bean
	public Map<DataSourceType, SinkBuilder> sinkBuilderMap(ApplicationContext applicationContext) {
		Map<String, Object> strategyBeans = applicationContext.getBeansWithAnnotation(SinkBuilderType.class);
		Map<DataSourceType, SinkBuilder> sinkBuilderMap = new HashMap<>();
		strategyBeans.forEach((name, bean) -> {
			SinkBuilderType typeAnnotation = bean.getClass().getAnnotation(SinkBuilderType.class);
			sinkBuilderMap.put(typeAnnotation.value(), (SinkBuilder) bean);
		});
		return sinkBuilderMap;
	}



}
