package com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhonenixCataLog {
	private String tableName;
	private String columnName;
	private int keySeq;
}
