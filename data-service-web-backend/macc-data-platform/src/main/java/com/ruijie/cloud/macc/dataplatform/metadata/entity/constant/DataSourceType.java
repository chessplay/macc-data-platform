package com.ruijie.cloud.macc.dataplatform.metadata.entity.constant;

import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceConnectionType.ALIYUN_MAXCOMPUTE;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceConnectionType.JDBC;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.TableMetadataFetcher;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.aliyun.AliyunOdpsTableFetcher;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc.DefaultJdbcMetaFetcher;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc.PhoenixJdbcMetadataFetcher;

public enum DataSourceType {
	MYSQL("mysql", "com.mysql.cj.jdbc.Driver", JDBC, DefaultJdbcMetaFetcher.class, "select 1 from dual"),
	CLICKHOUSE("clickHouse", "ru.yandex.clickhouse.ClickHouseDriver", JDBC, DefaultJdbcMetaFetcher.class,
		"select now()"),
	PHOENIX("phoenix", "org.apache.phoenix.queryserver.client.Driver", JDBC, PhoenixJdbcMetadataFetcher.class,
		"select now()"),
	MAX_COMPUTE("maxcompute", "", ALIYUN_MAXCOMPUTE, AliyunOdpsTableFetcher.class, "select now()");

	private String id;
	private String driverClass;
	private DataSourceConnectionType connectionType;
	private Class<? extends TableMetadataFetcher> dbMetaFetcher;
	private String testSql;

	private DataSourceType(String id, String driverClass, DataSourceConnectionType connectionType,
			Class<? extends TableMetadataFetcher> dbMetaFetcher, String testSql) {
		this.id = id;
		this.driverClass = driverClass;
		this.connectionType = connectionType;
		this.dbMetaFetcher = dbMetaFetcher;
		this.testSql = testSql;
	}

	public String getId() {
		return id;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getTestSql() {
		return testSql;
	}

	public DataSourceConnectionType getConnectionType() {
		return connectionType;
	}

	public Class<? extends TableMetadataFetcher> getDbMetaFetcher() {
		return dbMetaFetcher;
	}

}
