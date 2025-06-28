package com.ruijie.cloud.macc.dataplatform.metadata.datasource.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelParser;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.JdbcDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.utils.DataSourceUtils;

public class JdbcDataSourceContextHolder {
	private static Map<String, DataSource> DATASOURCE_MAP = new HashMap<>();

	public static void regist(DataSourceModel dataSourceModel) {
		JdbcDataSourceModel jdbcModel = DataSourceModelParser.parseToJdbcDataSource(dataSourceModel);

		DataSource dataSource = DataSourceUtils.createDataSource(dataSourceModel.getDataSourceKey(),
				dataSourceModel.getDbType(), jdbcModel);
		if (dataSource != null) {
			DATASOURCE_MAP.put(dataSourceModel.getDataSourceKey(), dataSource);
		}
	}

	public static void removeAndClose(String dataSourceKey) {
		if (!DATASOURCE_MAP.containsKey(dataSourceKey)) {
			return;
		}

		DATASOURCE_MAP.remove(dataSourceKey);
	}

	public static DataSource getDataSource(String dataSourceKey) {
		if (!DATASOURCE_MAP.containsKey(dataSourceKey)) {
			return null;
		}

		return DATASOURCE_MAP.get(dataSourceKey);
	}

	public static boolean isDataSourceActive(String dataSourceKey) {
		return DATASOURCE_MAP.containsKey(dataSourceKey);
	}

	public static Connection getDataSourceConnection(String dataSourceKey) throws SQLException {
		DataSource dataSource = getDataSource(dataSourceKey);
		if (dataSource == null) {
			return null;
		}

		return dataSource.getConnection();
	}
}
