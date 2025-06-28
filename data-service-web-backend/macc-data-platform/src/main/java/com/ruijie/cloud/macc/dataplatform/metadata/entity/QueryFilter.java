package com.ruijie.cloud.macc.dataplatform.metadata.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/03/26 11:24
 **/
@Data
@NoArgsConstructor
public class QueryFilter {
	private QueryFilterType type;
	private String field;
	private List<Object> value;

	@JsonCreator
	public QueryFilter(@JsonProperty("type") QueryFilterType type, @JsonProperty("field") String field,
			@JsonProperty("value") List<Object> value) {
		this.type = type;
		this.field = field;
		this.value = value;
	}
}
