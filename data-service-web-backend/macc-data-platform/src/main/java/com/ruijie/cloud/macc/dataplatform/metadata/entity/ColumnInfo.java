package com.ruijie.cloud.macc.dataplatform.metadata.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ColumnInfo {
	private String columnName;
	private String columnType;
	private IndexInfo indexInfo;
	private String columnComment;
}
