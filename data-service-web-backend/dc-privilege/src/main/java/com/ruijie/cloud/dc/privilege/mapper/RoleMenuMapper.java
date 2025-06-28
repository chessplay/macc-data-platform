package com.ruijie.cloud.dc.privilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.entity.business.RoleMenu;

public interface RoleMenuMapper {
	List<RoleMenu> selectByRoleId(@Param("system") String system, @Param("roleId") String roleId);
	
	List<RoleMenu> selectByMenuId(@Param("system") String system, @Param("menuId") int menuId);
	
	RoleMenu selectByRoleAndMenu(@Param("system") String system, @Param("roleId") String roleId, @Param("menuId") int menuId);
	
	int insert(RoleMenu roleMenu);
	
	int delete(@Param("system") String system, @Param("ids") List<Integer> ids);
	
	int deleteByRoleAndMenu(@Param("system") String system, @Param("roleId") String roleId, @Param("menuId") int menuId);
}
