package com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.TableMetadataFetcher;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.context.JdbcDataSourceContextHolder;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.ColumnInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.IndexInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;

@Component
public class DefaultJdbcMetaFetcher implements TableMetadataFetcher {

	@Override
	public List<String> getTableNameList(DataSourceModel dataSourceModel) throws SQLException {
		Connection connection = JdbcDataSourceContextHolder.getDataSourceConnection(dataSourceModel.getDataSourceKey());
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

	@Override
	public TableInfo getTableDetail(DataSourceModel dataSourceModel, String tableName) throws SQLException {
		Connection connection = JdbcDataSourceContextHolder.getDataSourceConnection(dataSourceModel.getDataSourceKey());
		if (connection == null) {
			throw new IllegalStateException("get connection failed");
		}

		String catalog = connection.getCatalog();
		String schema = connection.getSchema();

		ResultSet tablesResultSet =
				connection.getMetaData().getTables(catalog, schema, tableName, new String[] {"TABLE"});

		while (tablesResultSet.next()) {
			TableInfo info = new TableInfo();
			info.setTableName(tableName);
			info.setColumns(getTableColumns(connection, catalog, schema, tableName));
			info.setIndexes(getTableIndexes(connection, catalog, schema, tableName));
			info.setPrimaryKeys(getTablePrimaryKeys(connection, catalog, schema, tableName));
			return info;
		}

		return null;
	}

	private List<ColumnInfo> getTableColumns(Connection connection, String catalog, String dbSchema, String tableName)
			throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		List<ColumnInfo> columns = new ArrayList<>();

		ResultSet columnsResultSet = metaData.getColumns(catalog, dbSchema, tableName, null);
		while (columnsResultSet.next()) {
			ColumnInfo columnInfo = new ColumnInfo();
			String columnName = columnsResultSet.getString(MetaDataConstant.COLUMN_NAME);
			columnInfo.setColumnName(columnName);
			String columnType = columnsResultSet.getString(MetaDataConstant.TYPE_NAME).trim().replaceAll("\\s+", "_");
			columnInfo.setColumnType(columnType);

			String columnComment = columnsResultSet.getString(MetaDataConstant.COLUMN_REMARKS);
			columnInfo.setColumnComment(columnComment);
			columns.add(columnInfo);
		}

		return columns;
	}

	private List<IndexInfo> getTableIndexes(Connection connection, String catalog, String dbSchema, String tableName)
			throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		List<IndexInfo> indexes = new ArrayList<>();
		// 使用提供的 DatabaseMetaData 获取索引信息
		ResultSet indexesResultSet = metaData.getIndexInfo(catalog, dbSchema, tableName, false, false);
		while (indexesResultSet.next()) {
			IndexInfo indexInfo = new IndexInfo();
			indexInfo.setIndexName(indexesResultSet.getString(MetaDataConstant.INDEX_NAME));
			String columns = indexesResultSet.getString(MetaDataConstant.COLUMN_NAME);
			indexInfo.setColumnName(Arrays.asList(columns.split(",")));
			indexInfo.setIndexType(indexesResultSet.getString("TYPE") == null ? MetaDataConstant.UNKNOWN_TYPE
					: indexesResultSet.getString("TYPE"));
			indexes.add(indexInfo);
		}

		return indexes;
	}

	private List<String> getTablePrimaryKeys(Connection connection, String catalog, String dbSchema, String tableName)
			throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		List<String> primaryKeys = new ArrayList<>();
		ResultSet primaryKeysResultSet = metaData.getPrimaryKeys(catalog, dbSchema, tableName);
		while (primaryKeysResultSet.next()) {
			primaryKeys.add(primaryKeysResultSet.getString(MetaDataConstant.COLUMN_NAME));
		}
		return primaryKeys;
	}
}
