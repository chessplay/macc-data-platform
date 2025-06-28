package com.ruijie.cloud.macc.dataplatform.metadata.datasource;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.dto.DataSourceSample;

/**
 * 校验数据数据源是否合法
 * 
 * @author wuchaohui
 */
public interface DataSourceModelValidator {
	boolean matchType(DataSourceType dbType);

	boolean validate(DataSourceModel model);

	DataSourceSample propertiesSample();
}
