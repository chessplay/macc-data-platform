package com.ruijie.cloud.dc.privilege.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ruijie.cloud.dc.privilege.dao.RoleMenuDao;
import com.ruijie.cloud.dc.privilege.entity.business.RoleMenu;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@Service
public class RoleMenuService {
	@Autowired
	private RoleMenuDao roleMenuDao;

	public List<RoleMenu> getRoleMenus(String role) {
		return roleMenuDao.selectByRoleId(role);
	}

	public int addRoleMenu(RoleMenu roleMenu) {
		if (PrivilegeUtils.isSuperAdminRole(roleMenu.getRoleId())) {
			return 1;
		}

		RoleMenu existsData = roleMenuDao.selectByRoleAndMenu(roleMenu.getRoleId(), roleMenu.getMenuId());
		if (existsData != null) {
			return 1;
		}
		return roleMenuDao.insert(roleMenu);
	}

	public int deleteRoleMenu(String roleId, int menuId) {
		return roleMenuDao.deleteByRoleAndMenu(roleId, menuId);
	}

	public boolean deleteByRole(String role) {
		List<RoleMenu> list = roleMenuDao.selectByRoleId(role);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}

		List<Integer> ids = list.stream().map(RoleMenu::getId).collect(Collectors.toList());
		roleMenuDao.delete(ids);
		return true;
	}

	public boolean deleteByMenu(int menuId) {
		List<RoleMenu> list = roleMenuDao.selectByMenuId(menuId);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}

		List<Integer> ids = list.stream().map(RoleMenu::getId).collect(Collectors.toList());
		roleMenuDao.delete(ids);
		return true;
	}
}
