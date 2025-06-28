package com.ruijie.cloud.dc.privilege.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.entity.business.RoleNamespace;
import com.ruijie.cloud.dc.privilege.mapper.RoleNamespaceMapper;

@Repository
public class RoleNamespaceDao {
	@Autowired
	private RoleNamespaceMapper mapper;
	@Autowired
	private SystemConfig systemConfig;

	public List<RoleNamespace> selectByRoleId(String roleId) {
		return mapper.selectByRoleId(systemConfig.getSystem(), roleId);
	}

	public RoleNamespace selectByRoleAndNamespace(String roleId, String namespace) {
		return mapper.selectByRoleAndNamespace(systemConfig.getSystem(), roleId, namespace);
	}

	public int insert(RoleNamespace data) {
		data.setSystem(systemConfig.getSystem());
		return mapper.insert(data);
	}

	public int deleteByRoleId(String roleId) {
		return mapper.deleteByRoleId(systemConfig.getSystem(), roleId);
	}

	public int deleteByRoleAndNamespace(String roleId, String namespace) {
		return mapper.deleteByRoleAndNamespace(systemConfig.getSystem(), roleId, namespace);
	}
}
