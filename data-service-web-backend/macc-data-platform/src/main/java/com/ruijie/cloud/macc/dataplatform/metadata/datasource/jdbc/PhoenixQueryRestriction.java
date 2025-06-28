package com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.restriction.SourceQueryRestriction;
import com.ruijie.cloud.macc.dataplatform.metadata.service.TableMetaDataService;

@Component
public class PhoenixQueryRestriction implements SourceQueryRestriction {
	@Autowired
	private TableMetaDataService tableMetaDataService;

	@Override
	public boolean queryFilter(String dataSourceKey, String tableName, List<QueryFilter> queryFilter) {
		TableInfo tableInfo = tableMetaDataService.tableInfo(dataSourceKey, tableName);
		if (tableInfo == null) {
			return false;
		}

		return false;
	}

}
