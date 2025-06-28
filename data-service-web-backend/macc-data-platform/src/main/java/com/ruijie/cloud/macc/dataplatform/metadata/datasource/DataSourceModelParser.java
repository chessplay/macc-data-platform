package com.ruijie.cloud.macc.dataplatform.metadata.datasource;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.aliyun.AliyunOdpsDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceProperty;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.JdbcDataSourceModel;

public class DataSourceModelParser {

	public static JdbcDataSourceModel parseToJdbcDataSource(DataSourceModel model) {
		JdbcDataSourceModel jdbcModel = new JdbcDataSourceModel();
		JSONObject dbProperties = model.getDbProperties();
		jdbcModel.setUrl(dbProperties.getString("url"));
		jdbcModel.setSchema(dbProperties.getString("schema"));
		jdbcModel.setUserName(dbProperties.getString("userName"));
		jdbcModel.setPassword(dbProperties.getString("password"));

		if (dbProperties.containsKey("properties")) {
			jdbcModel.setProperties(dbProperties.getJSONArray("properties").toJavaList(DataSourceProperty.class));
		}

		return jdbcModel;
	}

	public static AliyunOdpsDataSourceModel parseToAliyunOdpsDataSource(DataSourceModel model) {
		AliyunOdpsDataSourceModel odpsModel = new AliyunOdpsDataSourceModel();
		JSONObject dbProperties = model.getDbProperties();

		odpsModel.setAccessId(dbProperties.getString("accessId"));
		odpsModel.setAccessKey(dbProperties.getString("accessKey"));
		odpsModel.setEndPoint(dbProperties.getString("endPoint"));
		odpsModel.setProjectId(dbProperties.getString("projectId"));
		return odpsModel;
	}
}
