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

import com.ruijie.cloud.dc.core.controller.BaseController;
import com.ruijie.cloud.dc.core.response.BaseResult;
import com.ruijie.cloud.dc.core.response.BooleanResult;
import com.ruijie.cloud.dc.core.response.ListResult;
import com.ruijie.cloud.dc.core.response.ObjectResult;
import com.ruijie.cloud.dc.privilege.core.PrivilegeOperation;
import com.ruijie.cloud.dc.privilege.entity.business.Role;
import com.ruijie.cloud.dc.privilege.entity.business.RoleModule;
import com.ruijie.cloud.dc.privilege.entity.constant.ModuleType;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.ModuleService;
import com.ruijie.cloud.dc.privilege.service.RoleModuleService;
import com.ruijie.cloud.dc.privilege.service.RoleService;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@RestController
@RequestMapping("/privilege/setting")
public class RoleModuleController extends BaseController {
	@Autowired
	private RoleModuleService roleModuleService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/role/module/list")
	public BaseResult getRoleModules(@RequestParam("role_id") String roleId) {
		ListResult<RoleModule> rsp = new ListResult<>();
		if (StringUtils.isBlank(roleId)) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "roleId");
		}
		
		List<RoleModule> list = roleModuleService.getRoleModules(roleId);
		rsp.setList(list);
		return rsp;
	}
	
	@PostMapping("/role/module/info")
	public BaseResult addRoleModules(@RequestBody RoleModule roleModule) {
		BooleanResult rsp = new BooleanResult();
		if (StringUtils.isAnyBlank(roleModule.getModuleId(), roleModule.getRoleId())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "moduleId or roleId");
		}
		
		if (roleModule.getPrivilege() == null) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "privilege");
		}
		
		if (PrivilegeUtils.isSuperAdminRole(roleModule.getRoleId())) {
			return rsp.buildResult(SystemRestCode.ROLE_SUPERADMIN_NONEED);
		}
		
		ModuleType moduleType = moduleService.moduleType(roleModule.getModuleId());
		if (moduleType == null) {
			return rsp.buildResult(SystemRestCode.MODULE_NOT_EXISTS);
		}
		
		Role role = roleService.getRoleById(roleModule.getRoleId());
		if (role == null) {
			return rsp.buildResult(SystemRestCode.ROLE_NOT_EXISTS);
		}
		
		if (roleModule.getPrivilege() == PrivilegeOperation.NONE) {
			roleModuleService.deleteByRoleAndModule(roleModule.getRoleId(), roleModule.getModuleId());
		} else {
			roleModuleService.updateRoleModule(roleModule);
		}
		
		rsp.setResult(true);
		return rsp;
	}
	
	@DeleteMapping("/role/module/delete")
	public BaseResult deleteRoleModules(@RequestParam("id") int id) {
		ObjectResult<Boolean> rsp = new ObjectResult<Boolean>();
		if (id <= 0) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "id");
		}
		
		int ret = roleModuleService.deleteRoleModule(id);
		rsp.setData(ret > 0);
		return rsp;
	}
}
