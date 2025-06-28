package com.ruijie.cloud.macc.dataplatform.metadata.entity;

import java.util.List;

import lombok.Data;

@Data
public class JdbcDataSourceModel {
	private String url;
	private String schema;
	private String userName;
	private String password;
	private String description;
	private List<DataSourceProperty> properties;
}
