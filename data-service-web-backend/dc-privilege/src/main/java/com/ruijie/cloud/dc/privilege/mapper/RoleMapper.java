package com.ruijie.cloud.dc.privilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.entity.business.Role;

public interface RoleMapper {
	List<Role> selectAll(@Param("system") String system);

	Role selectByRoleId(@Param("system") String system, @Param("roleId") String roleId);

	int update(Role role);
	
	int insert(Role role);

	int delete(@Param("system") String system, @Param("roleId") String roleId);
}
