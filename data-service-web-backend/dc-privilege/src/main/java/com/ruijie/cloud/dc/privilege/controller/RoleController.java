package com.ruijie.cloud.dc.privilege.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruijie.cloud.dc.core.response.BaseResult;
import com.ruijie.cloud.dc.core.response.BooleanResult;
import com.ruijie.cloud.dc.core.response.ListResult;
import com.ruijie.cloud.dc.privilege.entity.business.Role;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.AccountManageService;
import com.ruijie.cloud.dc.privilege.service.RoleMenuService;
import com.ruijie.cloud.dc.privilege.service.RoleModuleService;
import com.ruijie.cloud.dc.privilege.service.RoleService;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@RestController
@RequestMapping("/privilege/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleModuleService roleModuleService;
	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private AccountManageService accountManageService;
	
	@GetMapping("/list")
	public ListResult<Role> list() {
		ListResult<Role> rsp = new ListResult<>();
		List<Role> list = roleService.getAllRoles();
		rsp.setList(list);
		return rsp;
	}

	@PostMapping("/info")
	public BaseResult addModule(@RequestBody Role role) {
		BooleanResult rsp = new BooleanResult();
		if (StringUtils.isAnyBlank(role.getRoleId(), role.getRoleName())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "roleId or roleName");
		}

		roleService.addOrUpdateRole(role);
		rsp.setResult(true);
		return rsp;
	}

	@DeleteMapping("/delete")
	public BaseResult delete(@RequestParam("role_id") String roleId) {
		BooleanResult rsp = new BooleanResult();
		if (PrivilegeUtils.isSuperAdminRole(roleId)) {
			return rsp.buildResult(SystemRestCode.ROLE_SUPERADMIN_NOT_DELETE);
		}
		
		int userCount = accountManageService.getRoleUserCount(roleId);
		if (userCount > 0) {
			return rsp.buildResult(SystemRestCode.ROLE_USER_EXISTS);
		}
		
		roleService.deleteByRoleId(roleId);
		roleModuleService.deleteByRole(roleId);
		roleMenuService.deleteByRole(roleId);
		rsp.setResult(true);
		return rsp;
	}
}
