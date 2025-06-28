package com.ruijie.cloud.dc.privilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.entity.business.Namespace;

/**
 * @author Hui
 * @description: TODO
 * @create: 2022/08/29 14:08
 **/
public interface NamespaceMapper {
	List<Namespace> selectAll(@Param("system") String system);

	Namespace selectByNamespace(@Param("system") String system,
			@Param("namespace") String namespace);

	int update(Namespace info);

	int insert(Namespace info);

	int delete(@Param("system") String system, @Param("namespace") String namespace);

}
