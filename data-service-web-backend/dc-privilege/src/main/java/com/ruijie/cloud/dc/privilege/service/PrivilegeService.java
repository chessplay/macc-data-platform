package com.ruijie.cloud.dc.privilege.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.cloud.dc.privilege.core.PrivilegeOperation;
import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.business.RoleModule;
import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@Service
public class PrivilegeService {

	@Autowired
	private AccountManageService accountManageService;
	@Autowired
	private RoleModuleService roleModuleService;

	public AccountDetail accountInfo(int userId) {
		return accountManageService.getAccountDetail(userId);
	}

	public boolean hasPrivilege(int userId, String moduleId, PrivilegeOperation privilegeOperation) {
		if (privilegeOperation == null) {
			privilegeOperation = PrivilegeOperation.READ;
		}

		AccountDetail at = accountManageService.getAccountDetail(userId);
		if (at == null) {
			return false;
		}

		if (PrivilegeUtils.isSuperAdminRole(at.getRoleId())) {
			return true;
		}

		RoleModule rm = roleModuleService.getByRoleAndModule(at.getRoleId(), moduleId);
		if (rm == null) {
			return false;
		}

		return rm.getPrivilege() == PrivilegeOperation.WRITE || privilegeOperation == PrivilegeOperation.READ;
	}

	public boolean isSuperAdmin(int userId) {
		AccountDetail at = accountManageService.getAccountDetail(userId);
		if (at == null) {
			return false;
		}

		if (PrivilegeUtils.isSuperAdminRole(at.getRoleId())) {
			return true;
		}

		return false;
	}

	public String accountRole(int userId) {
		AccountDetail at = accountManageService.getAccountDetail(userId);
		if (at == null || at.getState() != AccountState.ACCESS) {
			return null;
		}

		return at.getRoleId();
	}
}
