package com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.TableMetadataFetcher;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.context.JdbcDataSourceContextHolder;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.ColumnInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.IndexInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TablePkQueryConstraint;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;


@Component
public class PhoenixJdbcMetadataFetcher implements TableMetadataFetcher {
	@Override
	public List<String> getTableNameList(DataSourceModel dataSourceModel) throws SQLException {
		try (Connection connection =
				JdbcDataSourceContextHolder.getDataSourceConnection(dataSourceModel.getDataSourceKey())) {
			if (connection == null) {
				throw new IllegalStateException("get connection failed");
			}

			DatabaseMetaData metaData = connection.getMetaData();
			String catalog = connection.getCatalog();
			String schema = connection.getSchema();
			ResultSet tablesResultSet = metaData.getTables(catalog, schema, "%", new String[] {"TABLE"});

			List<String> tableNameList = new ArrayList<>();
			while (tablesResultSet.next()) {
				String tableName = tablesResultSet.getString(MetaDataConstant.TABLE_NAME);

				tableNameList.add(tableName);
			}

			return tableNameList;
		}
	}

	@Override
	public TableInfo getTableDetail(DataSourceModel dataSourceModel, String tableName) throws SQLException {
		try (Connection connection =
				JdbcDataSourceContextHolder.getDataSourceConnection(dataSourceModel.getDataSourceKey())) {
			if (connection == null) {
				throw new IllegalStateException("get connection failed");
			}

			String catalog = connection.getCatalog();
			String schema = connection.getSchema();

			Map<String, List<String>> allTablePkMap = getAllTablePrimaryKeys(connection, schema);
			Map<String, List<ColumnInfo>> tableColumnsMap = getAllTableColumns(connection, catalog, schema);
			Map<String, List<IndexInfo>> tableIndexMap = getTableIndexes(connection, catalog, schema);

			TableInfo info = new TableInfo();
			info.setTableName(tableName);
			info.setColumns(tableColumnsMap.get(tableName));
			info.setIndexes(tableIndexMap.get(tableName));
			info.setPrimaryKeys(allTablePkMap.get(tableName));

			info.setQueryConstraint(pkQueryConstrain(info.getPrimaryKeys()));
			return info;
		}
	}

	public Map<String, List<ColumnInfo>> getAllTableColumns(Connection connection, String catalog, String schema)
			throws SQLException {
		Map<String, List<ColumnInfo>> tableColumnsMap = new HashMap<>();

		ResultSet columnsResultSet = connection.getMetaData().getColumns(catalog, schema, "%", null);
		while (columnsResultSet.next()) {
			ColumnInfo columnInfo = new ColumnInfo();
			String columnName = columnsResultSet.getString(MetaDataConstant.COLUMN_NAME);
			columnInfo.setColumnName(columnName);
			String columnType = columnsResultSet.getString(MetaDataConstant.TYPE_NAME).trim().replaceAll("\\s+", "_");
			columnInfo.setColumnType(columnType);

			String columnComment = columnsResultSet.getString(MetaDataConstant.COLUMN_REMARKS);
			columnInfo.setColumnComment(columnComment);

			// 按表名存放到map
			String tableName = columnsResultSet.getString(MetaDataConstant.TABLE_NAME);
			if (tableColumnsMap.containsKey(tableName)) {
				tableColumnsMap.get(tableName).add(columnInfo);
			} else {
				List<ColumnInfo> columns = new ArrayList<>();
				columns.add(columnInfo);
				tableColumnsMap.put(tableName, columns);
			}
		}
		return tableColumnsMap;
	}

	public Map<String, List<IndexInfo>> getTableIndexes(Connection connection, String catalog, String schema)
			throws SQLException {
		Map<String, List<IndexInfo>> tableIndexMap = new HashMap<>();

		ResultSet indexesResultSet = connection.getMetaData().getIndexInfo(catalog, schema, "%", false, false);
		while (indexesResultSet.next()) {
			IndexInfo indexInfo = new IndexInfo();
			indexInfo.setIndexName(indexesResultSet.getString(MetaDataConstant.INDEX_NAME));
			String columns = indexesResultSet.getString(MetaDataConstant.COLUMN_NAME);
			indexInfo.setColumnName(Arrays.asList(columns.split(",")));
			indexInfo.setIndexType(indexesResultSet.getString("TYPE") == null ? MetaDataConstant.UNKNOWN_TYPE
					: indexesResultSet.getString("TYPE"));

			// 按表名存放到map
			String tableName = indexesResultSet.getString(MetaDataConstant.TABLE_NAME);
			if (tableIndexMap.containsKey(tableName)) {
				tableIndexMap.get(tableName).add(indexInfo);
			} else {
				List<IndexInfo> indexes = new ArrayList<>();
				indexes.add(indexInfo);
				tableIndexMap.put(tableName, indexes);
			}
		}

		return tableIndexMap;
	}

	public Map<String, List<String>> getAllTablePrimaryKeys(Connection connection, String schema) throws SQLException {
		Map<String, List<String>> tablePrimaryKeyMap = new HashMap<>();

		List<PhonenixCataLog> tablePkCatalogs = getTablePkCatalog(connection, schema);
		Map<String, List<PhonenixCataLog>> tablePkCatalogsMap =
				tablePkCatalogs.stream().collect(Collectors.groupingBy(PhonenixCataLog::getTableName));

		for (Map.Entry<String, List<PhonenixCataLog>> entry : tablePkCatalogsMap.entrySet()) {
			String tableName = entry.getKey();
			List<PhonenixCataLog> catalogList = entry.getValue();

			// 按Key_SEQ 排序
			Collections.sort(catalogList, (a, b) -> a.getKeySeq() > b.getKeySeq() ? 1 : -1);

			tablePrimaryKeyMap.put(tableName,
					catalogList.stream().map(PhonenixCataLog::getColumnName).collect(Collectors.toList()));
		}

		return tablePrimaryKeyMap;
	}

	public List<PhonenixCataLog> getTablePkCatalog(Connection connection, String schema) throws SQLException {
		List<PhonenixCataLog> tablePkCatalogs = new ArrayList<>();
		try (PreparedStatement ps =
				connection.prepareStatement(PhoenixMetaDataQueryConstant.ALL_TABLE_PRIMARY_KEY_SQL)) {
			ps.setString(1, schema);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String tableName = rs.getString(MetaDataConstant.TABLE_NAME);
					String columnName = rs.getString(MetaDataConstant.COLUMN_NAME);
					int keySeq = rs.getInt(MetaDataConstant.KEY_SEQ);
					tablePkCatalogs.add(new PhonenixCataLog(tableName, columnName, keySeq));
				}
			}
		}

		return tablePkCatalogs;
	}

	private List<TablePkQueryConstraint> pkQueryConstrain(List<String> pkList) {
		if (CollectionUtils.isEmpty(pkList)) {
			return null;
		}

		TablePkQueryConstraint info = new TablePkQueryConstraint();
		info.setField(pkList.get(0));
		info.setFileterType(Arrays.asList(QueryFilterType.EQ, QueryFilterType.IN));
		return Arrays.asList(info);
	}
}
