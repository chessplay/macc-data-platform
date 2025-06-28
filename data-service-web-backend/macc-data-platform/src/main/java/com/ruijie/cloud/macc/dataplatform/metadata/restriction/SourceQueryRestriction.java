package com.ruijie.cloud.macc.dataplatform.metadata.restriction;

import java.util.List;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;

public interface SourceQueryRestriction {

	boolean queryFilter(String dataSourceKey, String tableName, List<QueryFilter> queryFilter);
}
