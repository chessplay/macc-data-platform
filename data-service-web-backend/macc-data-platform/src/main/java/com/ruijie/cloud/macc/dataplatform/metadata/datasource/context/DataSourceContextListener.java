package com.ruijie.cloud.macc.dataplatform.metadata.datasource.context;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceConnectionType;
import com.ruijie.cloud.macc.dataplatform.metadata.service.DataSourceService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class DataSourceContextListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<DataSourceModel> dataSourceModels = dataSourceService.getAll();

		if (CollectionUtils.isEmpty(dataSourceModels)) {
			return;
		}

		// 注册jdbc连接池
		for (DataSourceModel model : dataSourceModels) {
			if (model.connectionType() != DataSourceConnectionType.JDBC) {
				continue;
			}

			try {
				JdbcDataSourceContextHolder.regist(model);
			} catch (Exception e) {
				log.warn("datamodel : {} init failed.", model, e);
			}
		}

		// 注册阿里云 odps连接池
	}

}
