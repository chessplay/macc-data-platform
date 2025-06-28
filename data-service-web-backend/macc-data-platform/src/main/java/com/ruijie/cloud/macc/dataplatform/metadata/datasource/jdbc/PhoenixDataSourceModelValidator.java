package com.ruijie.cloud.macc.dataplatform.metadata.datasource.jdbc;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelParser;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelValidator;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceProperty;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.JdbcDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.dto.DataSourceSample;

@Component
public class PhoenixDataSourceModelValidator implements DataSourceModelValidator {
	private static DataSourceSample PHOENIX_SAMPLE;

	@Override
	public boolean matchType(DataSourceType dbType) {
		return DataSourceType.PHOENIX.equals(dbType);
	}

	@Override
	public boolean validate(DataSourceModel model) {
		JdbcDataSourceModel jdbcModel = DataSourceModelParser.parseToJdbcDataSource(model);
		if (StringUtils.isAnyBlank(jdbcModel.getUrl(), jdbcModel.getSchema())) {
			return false;
		}

		return true;
	}

	@Override
	public DataSourceSample propertiesSample() {
		if (PHOENIX_SAMPLE == null) {
			JdbcDataSourceModel model = new JdbcDataSourceModel();
			model.setUrl("jdbc:phoenix:thin:url=http://{ip}:8765;serialization=PROTOBUF");
			model.setSchema("MACC");
			List<DataSourceProperty> properties =
					Arrays.asList(new DataSourceProperty("phoenix.schema.isNamespaceMappingEnabled", "true"),
							new DataSourceProperty("phoenix.schema.mapSystemTablesToNamespace", "true"));
			model.setProperties(properties);

			PHOENIX_SAMPLE = new DataSourceSample(DataSourceType.PHOENIX, model);
		}

		return PHOENIX_SAMPLE;
	}
}
