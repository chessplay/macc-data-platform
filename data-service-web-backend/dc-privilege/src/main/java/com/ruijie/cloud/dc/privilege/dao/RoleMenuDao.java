package com.ruijie.cloud.dc.privilege.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.entity.business.RoleMenu;
import com.ruijie.cloud.dc.privilege.mapper.RoleMenuMapper;

@Repository
public class RoleMenuDao {
	@Autowired
	private SystemConfig systemConfig;
	@Autowired
	private RoleMenuMapper mapper;

	public List<RoleMenu> selectByRoleId(String roleId) {
		return mapper.selectByRoleId(systemConfig.getSystem(), roleId);
	}

	public List<RoleMenu> selectByMenuId(int menuId) {
		return mapper.selectByMenuId(systemConfig.getSystem(), menuId);
	}

	public RoleMenu selectByRoleAndMenu(String roleId, int menuId) {
		return mapper.selectByRoleAndMenu(systemConfig.getSystem(), roleId, menuId);
	}

	public int insert(RoleMenu data) {
		data.setSystem(systemConfig.getSystem());
		return mapper.insert(data);
	}

	public int delete(List<Integer> ids) {
		return mapper.delete(systemConfig.getSystem(), ids);
	}

	public int deleteByRoleAndMenu(String roleId, int menuId) {
		return mapper.deleteByRoleAndMenu(systemConfig.getSystem(), roleId, menuId);
	}
}
