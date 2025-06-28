package com.ruijie.cloud.macc.dataplatform.metadata.entity;

import com.fasterxml.jackson.annotation.*;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/03/26 11:24
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryFilterTemplate {

	private QueryFilterType type;
	private String field;
	private String value;
	private String columnType;  // 新增字段
	private String constrained; // 新增字段

	@JsonCreator
	public QueryFilterTemplate(@JsonProperty("type") QueryFilterType type,
							   @JsonProperty("field") String field,
							   @JsonProperty("value") String value,
							   @JsonProperty("columnType") String columnType,   // 新增字段
							   @JsonProperty("constrained") String constrained) { // 新增字段
		this.type = type;
		this.field = field;
		this.value = value;
		this.columnType = columnType;
		this.constrained = constrained;
	}
}
