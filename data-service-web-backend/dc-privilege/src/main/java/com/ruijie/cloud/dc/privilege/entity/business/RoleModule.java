package com.ruijie.cloud.dc.privilege.entity.business;

import com.ruijie.cloud.dc.privilege.core.PrivilegeOperation;

import lombok.Data;

@Data
public class RoleModule {
	private int id;
	private String system;
	private String roleId;
	private String moduleId;
	private PrivilegeOperation privilege;
}