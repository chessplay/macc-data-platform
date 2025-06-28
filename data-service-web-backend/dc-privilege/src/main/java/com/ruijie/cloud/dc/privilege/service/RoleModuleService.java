package com.ruijie.cloud.dc.privilege.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ruijie.cloud.dc.privilege.dao.RoleModuleDao;
import com.ruijie.cloud.dc.privilege.entity.business.RoleModule;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@Service
public class RoleModuleService {

	@Autowired
	private RoleModuleDao roleModuleDao;

	public RoleModule getByRoleAndModule(String roleId, String moduleId) {
		return roleModuleDao.selectByRoleAndModule(roleId, moduleId);
	}

	public List<RoleModule> getRoleModules(String role) {
		return roleModuleDao.selectByRoleId(role);
	}

	public int updateRoleModule(RoleModule roleModule) {
		if (PrivilegeUtils.isSuperAdminRole(roleModule.getRoleId())) {
			return 1;
		}

		RoleModule existsData = roleModuleDao.selectByRoleAndModule(roleModule.getRoleId(), roleModule.getModuleId());
		if (existsData != null) {
			if (existsData.getPrivilege() == roleModule.getPrivilege()) {
				return 1;
			}

			return roleModuleDao.updatePrivilege(existsData.getId(), roleModule.getPrivilege());
		}

		return roleModuleDao.insert(roleModule);
	}

	public int deleteRoleModule(int id) {
		return roleModuleDao.delete(Arrays.asList(id));
	}

	public int deleteByModule(String moduleId) {
		List<RoleModule> list = roleModuleDao.selectByModuleId(moduleId);
		if (CollectionUtils.isEmpty(list)) {
			return 1;
		}

		List<Integer> ids = list.stream().map(RoleModule::getId).collect(Collectors.toList());
		return roleModuleDao.delete(ids);
	}

	public int deleteByRole(String role) {
		List<RoleModule> list = roleModuleDao.selectByRoleId(role);
		if (CollectionUtils.isEmpty(list)) {
			return 1;
		}

		List<Integer> ids = list.stream().map(RoleModule::getId).collect(Collectors.toList());
		return roleModuleDao.delete(ids);
	}

	public int deleteByRoleAndModule(String roleId, String moduleId) {
		return roleModuleDao.deleteByRoleAndModule(roleId, moduleId);
	}
}
