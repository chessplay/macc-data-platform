package com.ruijie.cloud.dc.privilege.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.entity.business.Role;
import com.ruijie.cloud.dc.privilege.mapper.RoleMapper;

@Repository
public class RoleDao {
	@Autowired
	private RoleMapper mapper;
	@Autowired
	private SystemConfig systemConfig;

	public List<Role> selectAll() {
		return mapper.selectAll(systemConfig.getSystem());
	}

	public Role selectByRoleId(String roleId) {
		return mapper.selectByRoleId(systemConfig.getSystem(), roleId);
	}

	public int update(Role data) {
		data.setSystem(systemConfig.getSystem());
		return mapper.update(data);
	}

	public int insert(Role data) {
		data.setSystem(systemConfig.getSystem());
		return mapper.insert(data);
	}

	public int delete(String roleId) {
		return mapper.delete(systemConfig.getSystem(), roleId);
	}
}
