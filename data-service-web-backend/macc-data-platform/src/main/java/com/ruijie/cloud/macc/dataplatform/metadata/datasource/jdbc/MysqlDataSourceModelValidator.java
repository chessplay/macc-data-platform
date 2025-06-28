package com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelParser;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelValidator;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.JdbcDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.dto.DataSourceSample;

@Component
public class MysqlDataSourceModelValidator implements DataSourceModelValidator {
	private static DataSourceSample MYSQL_SAMPLE;

	@Override
	public boolean matchType(DataSourceType dbType) {
		return DataSourceType.MYSQL.equals(dbType);
	}

	@Override
	public boolean validate(DataSourceModel model) {
		JdbcDataSourceModel jdbcModel = DataSourceModelParser.parseToJdbcDataSource(model);
		if (StringUtils.isAnyBlank(jdbcModel.getUrl(), jdbcModel.getUserName(), jdbcModel.getPassword())) {
			return false;
		}
		return true;
	}

	@Override
	public DataSourceSample propertiesSample() {
		if (MYSQL_SAMPLE == null) {
			JdbcDataSourceModel model = new JdbcDataSourceModel();
			model.setUrl("jdbc:mysql://{ip}:{port}/{db}");
			model.setUserName("");
			model.setPassword("xxxx");

			MYSQL_SAMPLE = new DataSourceSample(DataSourceType.MYSQL, model);
		}

		return MYSQL_SAMPLE;
	}
}
