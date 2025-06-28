package com.ruijie.cloud.dc.privilege.entity.constant;

public enum SystemRole {
	SUPER_ADMIN("super_admin", "超级管理员"),
	;
	
	private String roleId;
	private String roleName;
	
	private SystemRole(String roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public String getRoleId() {
		return roleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
}
