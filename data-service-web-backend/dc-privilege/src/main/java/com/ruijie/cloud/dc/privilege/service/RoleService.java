package com.ruijie.cloud.dc.privilege.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.cloud.dc.privilege.dao.RoleDao;
import com.ruijie.cloud.dc.privilege.entity.business.Role;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@Service
public class RoleService {
	@Autowired
	private RoleDao dao;

	public boolean roleExists(String roleId) {
		return getRoleById(roleId) != null;
	}

	public List<Role> getAllRoles() {
		List<Role> list = new ArrayList<>();
		List<Role> systemRoles = PrivilegeUtils.systemRoles();
		List<Role> dbRoles = dao.selectAll();
		list.addAll(systemRoles);
		list.addAll(dbRoles);
		return list;
	}

	public Role getRoleById(String roleId) {
		Role role = PrivilegeUtils.systemRole(roleId);
		if (role != null) {
			return role;
		}

		return dao.selectByRoleId(roleId);
	}

	public int addOrUpdateRole(Role role) {
		if (PrivilegeUtils.isSystemRole(role.getRoleId())) {
			return 0;
		}

		if (role.getDescription() == null) {
			role.setDescription("");
		}

		Role existsRole = dao.selectByRoleId(role.getRoleId());
		if (existsRole != null) {
			return dao.update(role);
		} else {
			return dao.insert(role);
		}
	}

	public int deleteByRoleId(String roleId) {
		if (PrivilegeUtils.isSystemRole(roleId)) {
			return 0;
		}

		return dao.delete(roleId);
	}
}
