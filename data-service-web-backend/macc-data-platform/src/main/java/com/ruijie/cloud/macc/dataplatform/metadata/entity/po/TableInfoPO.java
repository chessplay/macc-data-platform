package com.ruijie.cloud.macc.dataplatform.metadata.entity.po;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TableInfoPO {
	private int id;
	private String dataSourceKey;
	private String tableName;
	private String columnsJson;
	private String indexJson;
	private String primaryKeyJson;
	private String description;
}
