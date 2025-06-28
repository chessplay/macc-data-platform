package com.ruijie.cloud.macc.dataplatform.metadata.datasource.aliyun;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelParser;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelValidator;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.dto.DataSourceSample;

@Component
public class AliyunOdpsValidator implements DataSourceModelValidator {
	private static DataSourceSample ODPS_SAMPLE;

	@Override
	public boolean matchType(DataSourceType dbType) {
		return DataSourceType.MAX_COMPUTE.equals(dbType);
	}

	@Override
	public boolean validate(DataSourceModel model) {
		if (model.getDbProperties() == null) {
			return false;
		}

		AliyunOdpsDataSourceModel odpsModel = DataSourceModelParser.parseToAliyunOdpsDataSource(model);
		if (StringUtils.isAnyBlank(odpsModel.getAccessId(), odpsModel.getAccessKey(), odpsModel.getEndPoint(),
				odpsModel.getProjectId())) {
			return false;
		}

		return true;
	}

	@Override
	public DataSourceSample propertiesSample() {
		if (ODPS_SAMPLE == null) {
			AliyunOdpsDataSourceModel model = new AliyunOdpsDataSourceModel();
			model.setAccessId("");
			model.setAccessKey("");
			model.setEndPoint("http://service.cn-hangzhou.maxcompute.aliyun.com/api");
			model.setProjectId("");

			ODPS_SAMPLE = new DataSourceSample(DataSourceType.MAX_COMPUTE, model);
		}
		return ODPS_SAMPLE;
	}
}
