package com.ruijie.cloud.dc.privilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.core.PrivilegeOperation;
import com.ruijie.cloud.dc.privilege.entity.business.RoleModule;

public interface RoleModuleMapper {
	List<RoleModule> selectByRoleId(@Param("system") String system, @Param("roleId") String roleId);
	
	List<RoleModule> selectByModuleId(@Param("system") String system, @Param("moduleId") String moduleId);
	
	RoleModule selectByRoleAndModule(@Param("system") String system, @Param("roleId") String roleI, @Param("moduleId") String moduleId);
	
	int updatePrivilege(@Param("system") String system, @Param("id") int id, @Param("privilege") PrivilegeOperation privilege);
	
	int insert(RoleModule roleModule);
	
	int delete(@Param("system") String system, @Param("ids") List<Integer> ids);
	
	int deleteByRoleAndModule(@Param("system") String system, @Param("roleId") String roleI, @Param("moduleId") String moduleId);
}
