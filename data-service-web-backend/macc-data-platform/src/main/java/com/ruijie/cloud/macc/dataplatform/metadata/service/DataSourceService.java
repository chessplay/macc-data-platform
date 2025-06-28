package com.ruijie.cloud.macc.dataplatform.metadata.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.macc.dataplatform.metadata.dao.DataSourceDao;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelValidator;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.context.JdbcDataSourceContextHolder;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceConnectionType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.dto.DataSourceSample;

@Component
public class DataSourceService {
	private static List<DataSourceSample> SAMPLE_LIST;

	@Autowired
	private DataSourceDao dao;

	@Resource
	private List<DataSourceModelValidator> dataSourceValidators;

	public boolean isDataSourceActive(String dataSourceKey) {
		DataSourceModel model = selectByDataSourceKey(dataSourceKey);
		if (model == null) {
			return false;
		}

		if (model.getDbType().getConnectionType() == DataSourceConnectionType.ALIYUN_MAXCOMPUTE) {
			return true;
		}

		return JdbcDataSourceContextHolder.isDataSourceActive(dataSourceKey);
	}

	public boolean isDataSourceValid(DataSourceModel dataSourceModel) {
		DataSourceModelValidator validator = dataSourceValidators.stream()
				.filter(v -> v.matchType(dataSourceModel.getDbType())).findAny().orElseGet(null);
		if (validator == null) {
			return false;
		}

		return validator.validate(dataSourceModel);
	}

	public boolean isDataSourceExists(String dataSourceKey) {
		return dao.selectByDataSourceKey(dataSourceKey) != null;
	}

	public List<DataSourceModel> getAll() {
		return dao.selectList(null, null);
	}

	public List<DataSourceModel> getList(String keyword, DataSourceType dbType) {
		return dao.selectList(keyword, dbType);
	}

	public DataSourceModel selectByDataSourceKey(String dataSourceKey) {
		return dao.selectByDataSourceKey(dataSourceKey);
	}

	public DataSourceModel getById(int id) {
		return dao.selectById(id);
	}

	public int add(DataSourceModel data) {
		return dao.insert(data);
	}

	public int update(DataSourceModel data) {
		return dao.update(data);
	}

	public int deleteByDataSourceKey(String dataSourceKey) {
		return dao.deleteByDataSourceKey(dataSourceKey);
	}

	public List<DataSourceSample> getDataSourcePropertiesSample() {
		if (SAMPLE_LIST == null) {
			SAMPLE_LIST = new ArrayList<>();

			dataSourceValidators.forEach(validator -> {
				SAMPLE_LIST.add(validator.propertiesSample());
			});
		}

		return SAMPLE_LIST;
	}

}
