package com.ruijie.cloud.macc.dataplatform.metadata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.po.DataSourcePO;


public interface DataSourceMapper {
	List<DataSourcePO> selectList(@Param("keyword") String keyword, @Param("dbType") DataSourceType dbType);

	DataSourcePO selectByDataSourceKey(@Param("dataSourceKey") String dataSourceKey);

	DataSourcePO selectById(@Param("id") int id);

	int insert(DataSourcePO data);

	int update(DataSourcePO data);

	int deleteByDataSourceKey(@Param("dataSourceKey") String dataSourceKey);
}
