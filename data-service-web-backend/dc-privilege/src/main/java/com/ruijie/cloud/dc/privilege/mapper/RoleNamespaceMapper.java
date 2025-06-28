package com.ruijie.cloud.dc.privilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.entity.business.RoleNamespace;

public interface RoleNamespaceMapper {
	List<RoleNamespace> selectByRoleId(@Param("system") String system, @Param("roleId") String roleId);

	RoleNamespace selectByRoleAndNamespace(@Param("system") String system, @Param("roleId") String roleId,
			@Param("namespace") String namespace);

	int insert(RoleNamespace data);

	int deleteByRoleId(@Param("system") String system, @Param("roleId") String roleId);

	int deleteByRoleAndNamespace(@Param("system") String system, @Param("roleId") String roleId,
			@Param("namespace") String namespace);
}
