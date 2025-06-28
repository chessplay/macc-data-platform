package com.ruijie.cloud.macc.dataplatform.metadata.utils;


import java.io.Closeable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.aliyun.odps.Odps;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.aliyun.AliyunOdpsDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceProperty;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceTestResult;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.JdbcDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Hui
 * @description: 数据源操作工具
 * @create: 2021/12/25 22:12
 **/
@Slf4j
public class DataSourceUtils {

	public static HikariDataSource createDataSource(String poolName, DataSourceType dbType, JdbcDataSourceModel model) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(model.getUrl());
		config.setDriverClassName(dbType.getDriverClass());
		if (StringUtils.isNotBlank(model.getUserName())) {
			config.setUsername(model.getUserName());
			config.setPassword(model.getPassword());
		}

		if (StringUtils.isNotBlank(model.getSchema())) {
			config.setSchema(model.getSchema());
		}

		if (!CollectionUtils.isEmpty(model.getProperties())) {
			Properties props = new Properties();
			for (DataSourceProperty prop : model.getProperties()) {
				if (StringUtils.isAnyBlank(prop.getKey(), prop.getValue())) {
					continue;
				}

				props.put(prop.getKey(), prop.getValue());
			}

			config.setDataSourceProperties(props);
		}

		// connection pool
		config.setPoolName(poolName);
		config.setMinimumIdle(1);
		config.setIdleTimeout(300000);
		config.setMaximumPoolSize(10);
		config.setMaxLifetime(1800000);
		config.setConnectionTimeout(30000);
		return new HikariDataSource(config);
	}

	public static boolean destroyDataSource(Object dataSource) {
		if (dataSource == null) {
			return true;
		}

		if (dataSource instanceof Closeable) {
			try {
				Closeable closeableDataSource = (Closeable) dataSource;
				closeableDataSource.close();
			} catch (Exception e) {
				log.error("close conection falied. ", e);
			}
		}

		return true;
	}

	public static DataSourceTestResult testConnection(DataSourceType dbType, JdbcDataSourceModel model) {
		DataSourceTestResult result = new DataSourceTestResult();
		String poolName = "test-connection";
		try (HikariDataSource dataSource = createDataSource(poolName, dbType, model);
				Connection connection = dataSource.getConnection()) {
			Statement statement = connection.createStatement();

			if (StringUtils.isNotBlank(dbType.getTestSql())) {
				statement.execute(dbType.getTestSql());
			}

			result.setState("enable");
		} catch (Exception e) {
			log.error("dataSourceInfo : {} test failed. ", model, e);
			result.setState("disable");
			result.setErrorMsg(e.getMessage());
		}

		return result;
	}

	public static Odps createAliyunOdps(AliyunOdpsDataSourceModel configuration) {
		Account account = new AliyunAccount(configuration.getAccessId(), configuration.getAccessKey());
		Odps odps = new Odps(account);
		odps.setEndpoint(configuration.getEndPoint());
		odps.setDefaultProject(configuration.getProjectId());
		return odps;

	}
}
