package com.ruijie.cloud.macc.dataplatform.metadata.entity;


import com.alibaba.fastjson.JSONObject;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceConnectionType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;

import lombok.Data;

@Data
public class DataSourceModel {

	private int id;
	private String name;
	private String dataSourceKey;
	private DataSourceType dbType;
	private JSONObject dbProperties;
	private String state;
	private String description;

	public DataSourceConnectionType connectionType() {
		return dbType.getConnectionType();
	}
}
