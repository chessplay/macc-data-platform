package com.ruijie.cloud.macc.dataplatform.metadata.entity;

import java.util.List;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;

import lombok.Data;

@Data
public class TablePkQueryConstraint {
	private String field;
	private List<QueryFilterType> fileterType;
}
