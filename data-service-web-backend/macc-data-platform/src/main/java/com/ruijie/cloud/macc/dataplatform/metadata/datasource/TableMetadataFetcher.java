package com.ruijie.cloud.macc.dataplatform.metadata.datasource;

import java.util.List;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;

public interface TableMetadataFetcher {

	List<String> getTableNameList(DataSourceModel dataSourceModel) throws Exception;

	TableInfo getTableDetail(DataSourceModel dataSourceModel, String tableName) throws Exception;
}
