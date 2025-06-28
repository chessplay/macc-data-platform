package com.ruijie.cloud.dc.privilege.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ruijie.cloud.dc.privilege.core.LoginUserInfo;
import com.ruijie.cloud.dc.privilege.entity.business.Role;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRole;

public class PrivilegeUtils {
	private static ThreadLocal<LoginUserInfo> threadLocalUser = new ThreadLocal<>();

	public static boolean isSuperAdminRole(String role) {
		return StringUtils.equals(SystemRole.SUPER_ADMIN.getRoleId(), role);
	}

	public static List<Role> systemRoles() {
		List<Role> list = new ArrayList<>();
		for (SystemRole systemRole : SystemRole.values()) {
			Role role = new Role();
			role.setRoleId(systemRole.getRoleId());
			role.setRoleName(systemRole.getRoleName());
			list.add(role);
		}
		return list;
	}

	public static Role systemRole(String roleId) {
		for (SystemRole systemRole : SystemRole.values()) {
			if (StringUtils.equals(roleId, systemRole.getRoleId())) {
				Role role = new Role();
				role.setRoleId(systemRole.getRoleId());
				role.setRoleName(systemRole.getRoleName());
				return role;
			}
		}

		return null;
	}

	public static boolean isSystemRole(String roleId) {
		for (SystemRole systemRole : SystemRole.values()) {
			if (StringUtils.equals(roleId, systemRole.getRoleId())) {
				return true;
			}
		}

		return false;
	}

	public static String defaultNamespace() {
		return "MACC";
	}

	public static String currentNamespace() {
		LoginUserInfo loginUserInfo = threadLocalUser.get();
		if (loginUserInfo == null || StringUtils.isBlank(loginUserInfo.getNamespace())) {
			throw new IllegalStateException("no login user");
		}

		if (StringUtils.isBlank(loginUserInfo.getNamespace())) {
			throw new IllegalStateException("user namespace is empty");
		}

		return loginUserInfo.getNamespace();
	}

	public static LoginUserInfo getLoginUser() {
		return threadLocalUser.get();
	}

	public static void setLoginUser(LoginUserInfo user) {
		threadLocalUser.set(user);
	}

	public static void clearLoginUser() {
		threadLocalUser.remove();
	}
}

