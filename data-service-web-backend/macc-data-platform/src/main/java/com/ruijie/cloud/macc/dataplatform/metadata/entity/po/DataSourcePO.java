package com.ruijie.cloud.macc.dataplatform.metadata.entity.po;

import com.ruijie.cloud.dc.core.domain.BaseEntity;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataSourcePO extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private int id;
	private String DataSourceKey;
	private String name;
	private DataSourceType dbType;
	private String dbPropertiesJson;
	private String description;
	private String state;
}
