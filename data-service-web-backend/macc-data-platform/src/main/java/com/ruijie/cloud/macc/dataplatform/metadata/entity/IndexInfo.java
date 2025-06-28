package com.ruijie.cloud.macc.dataplatform.metadata.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IndexInfo {
	private String indexName;
	private List<String> columnName;
	private String indexType;


}
