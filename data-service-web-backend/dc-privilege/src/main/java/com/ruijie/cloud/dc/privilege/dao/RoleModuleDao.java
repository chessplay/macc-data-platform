package com.ruijie.cloud.dc.privilege.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.core.PrivilegeOperation;
import com.ruijie.cloud.dc.privilege.entity.business.RoleModule;
import com.ruijie.cloud.dc.privilege.mapper.RoleModuleMapper;

@Repository
public class RoleModuleDao {
	@Autowired
	private RoleModuleMapper mapper;
	@Autowired
	private SystemConfig systemConfig;

	public List<RoleModule> selectByRoleId(String roleId) {
		return mapper.selectByRoleId(systemConfig.getSystem(), roleId);
	}

	public List<RoleModule> selectByModuleId(String moduleId) {
		return mapper.selectByModuleId(systemConfig.getSystem(), moduleId);
	}

	public RoleModule selectByRoleAndModule(String roleId, String moduleId) {
		return mapper.selectByRoleAndModule(systemConfig.getSystem(), roleId, moduleId);
	}

	public int updatePrivilege(int id, PrivilegeOperation privilege) {
		return mapper.updatePrivilege(systemConfig.getSystem(), id, privilege);
	}

	public int insert(RoleModule data) {
		data.setSystem(systemConfig.getSystem());
		return mapper.insert(data);
	}

	public int delete(List<Integer> ids) {
		return mapper.delete(systemConfig.getSystem(), ids);
	}

	public int deleteByRoleAndModule(String roleId, String moduleId) {
		return mapper.deleteByRoleAndModule(systemConfig.getSystem(), roleId, moduleId);
	}
}
