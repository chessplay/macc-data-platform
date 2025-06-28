package com.ruijie.cloud.macc.dataplatform.metadata.datasource.aliyun;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.aliyun.odps.Column;
import com.aliyun.odps.Odps;
import com.aliyun.odps.Table;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelParser;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.TableMetadataFetcher;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.ColumnInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;

@Component
public class AliyunOdpsTableFetcher implements TableMetadataFetcher {

	@Override
	public List<String> getTableNameList(DataSourceModel dataSourceModel) {
		Odps odps = getOdps(dataSourceModel);
		List<String> tableNames = new ArrayList<>();
		for (Table t : odps.tables()) {
			tableNames.add(t.getName());
		}

		return tableNames;
	}

	@Override
	public TableInfo getTableDetail(DataSourceModel dataSourceModel, String tableName) {
		Odps odps = getOdps(dataSourceModel);
		for (Table t : odps.tables()) {
			if (!StringUtils.equals(t.getName(), tableName)) {
				continue;
			}

			return parseTableInfo(t);
		}

		return null;
	}

	private TableInfo parseTableInfo(Table t) {
		TableInfo tableInfo = new TableInfo();
		tableInfo.setTableName(t.getName());
		tableInfo.setDescription(t.getComment());
		tableInfo.setPrimaryKeys(t.getPrimaryKey());

		List<ColumnInfo> columns = new ArrayList<>();
		for (Column column : t.getSchema().getColumns()) {
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setColumnName(column.getName());
			columnInfo.setColumnType(column.getTypeInfo().getTypeName());
			columnInfo.setColumnComment(column.getComment());
			columns.add(columnInfo);
		}

		tableInfo.setColumns(columns);
		return tableInfo;
	}

	private Odps getOdps(DataSourceModel dataSourceModel) {
		AliyunOdpsDataSourceModel odpsModel = DataSourceModelParser.parseToAliyunOdpsDataSource(dataSourceModel);

		Account account = new AliyunAccount(odpsModel.getAccessId(), odpsModel.getAccessKey());
		Odps odps = new Odps(account);
		odps.setEndpoint(odpsModel.getEndPoint());
		odps.setDefaultProject(odpsModel.getProjectId());
		return odps;
	}
}
