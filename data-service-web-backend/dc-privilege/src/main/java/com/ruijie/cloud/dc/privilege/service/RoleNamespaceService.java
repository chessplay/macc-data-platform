package com.ruijie.cloud.dc.privilege.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.cloud.dc.privilege.dao.RoleNamespaceDao;
import com.ruijie.cloud.dc.privilege.entity.business.RoleNamespace;

@Service
public class RoleNamespaceService {
	@Autowired
	private RoleNamespaceDao roleNamespaceDao;

	public RoleNamespace getByRoleAndNamespace(String roleId, String namespace) {
		return roleNamespaceDao.selectByRoleAndNamespace(roleId, namespace);
	}

	public List<RoleNamespace> getByRoleId(String roleId) {
		return roleNamespaceDao.selectByRoleId(roleId);
	}

	public int addRoleNamepace(RoleNamespace data) {
		RoleNamespace dbData = getByRoleAndNamespace(data.getRoleId(), data.getNamespace());
		if (dbData != null) {
			return 1;
		}

		return roleNamespaceDao.insert(data);
	}

	public int deleteByRoleId(String roleId) {
		return roleNamespaceDao.deleteByRoleId(roleId);
	}

	public int deleteByRoleAndNamespace(String roleId, String namespace) {
		return roleNamespaceDao.deleteByRoleAndNamespace(roleId, namespace);
	}
}
