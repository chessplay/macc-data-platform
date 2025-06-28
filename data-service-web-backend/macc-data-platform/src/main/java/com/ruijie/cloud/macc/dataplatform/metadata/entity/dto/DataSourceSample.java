package com.ruijie.cloud.macc.dataplatform.metadata.entity.dto;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataSourceSample {
	private DataSourceType type;
	private Object properties;
}
