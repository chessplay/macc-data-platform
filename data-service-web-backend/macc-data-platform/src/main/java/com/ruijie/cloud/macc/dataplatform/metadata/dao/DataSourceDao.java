package com.ruijie.cloud.macc.dataplatform.metadata.dao;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.po.DataSourcePO;
import com.ruijie.cloud.macc.dataplatform.metadata.mapper.DataSourceMapper;

@Component
public class DataSourceDao {
	@Autowired
	private DataSourceMapper mapper;

	public List<DataSourceModel> selectList(String keyword, DataSourceType dbType) {
		List<DataSourcePO> poList = mapper.selectList(keyword, dbType);
		if (CollectionUtils.isEmpty(poList)) {
			return Arrays.asList();
		}

		return poList.stream().map(this::transferToDto).collect(Collectors.toList());
	}

	public DataSourceModel selectByDataSourceKey(String dataSourceKey) {
		DataSourcePO po = mapper.selectByDataSourceKey(dataSourceKey);
		if (po == null) {
			return null;
		}

		return transferToDto(po);
	}

	public DataSourceModel selectById(int id) {
		DataSourcePO po = mapper.selectById(id);
		if (po == null) {
			return null;
		}

		return transferToDto(po);
	}

	public int insert(DataSourceModel data) {
		return mapper.insert(transferToPo(data));
	}

	public int update(DataSourceModel data) {
		return mapper.update(transferToPo(data));
	}

	public int deleteByDataSourceKey(String dataSourceKey) {
		return mapper.deleteByDataSourceKey(dataSourceKey);
	}

	private DataSourceModel transferToDto(DataSourcePO po) {
		DataSourceModel model = new DataSourceModel();
		model.setId(po.getId());
		model.setName(po.getName());
		model.setDataSourceKey(po.getDataSourceKey());
		model.setDbType(po.getDbType());
		model.setDbProperties(JSON.parseObject(po.getDbPropertiesJson()));
		return model;
	}

	private DataSourcePO transferToPo(DataSourceModel dto) {
		DataSourcePO po = new DataSourcePO();
		po.setId(dto.getId());
		po.setName(dto.getName());
		po.setDataSourceKey(dto.getDataSourceKey());
		po.setDbType(dto.getDbType());
		po.setDbPropertiesJson(dto.getDbProperties().toJSONString());
		if (StringUtils.isBlank(dto.getDescription())) {
			po.setDescription("");
		} else {
			po.setDescription(dto.getDescription());
		}
		return po;
	}
}
