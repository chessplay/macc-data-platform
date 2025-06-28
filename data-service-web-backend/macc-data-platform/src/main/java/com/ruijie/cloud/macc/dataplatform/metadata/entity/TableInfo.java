package com.ruijie.cloud.macc.dataplatform.metadata.entity;


import java.util.List;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TableInfo {
	private DataSourceType dataSourceType;
	private String tableName;
	private List<ColumnInfo> columns;
	private List<IndexInfo> indexes;
	private List<String> primaryKeys;
	private List<TablePkQueryConstraint> queryConstraint;
	private List<String> requiredColumns;
	private String description;
}
