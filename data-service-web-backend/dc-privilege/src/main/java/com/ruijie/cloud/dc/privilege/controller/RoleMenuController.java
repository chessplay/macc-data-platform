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
import com.ruijie.cloud.dc.privilege.entity.business.Menu;
import com.ruijie.cloud.dc.privilege.entity.business.Role;
import com.ruijie.cloud.dc.privilege.entity.business.RoleMenu;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.MenuService;
import com.ruijie.cloud.dc.privilege.service.RoleMenuService;
import com.ruijie.cloud.dc.privilege.service.RoleService;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@RestController
@RequestMapping("/privilege/setting")
public class RoleMenuController extends BaseController {
	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/role/menu/list")
	public BaseResult getRoleModules(@RequestParam("role_id") String role) {
		ListResult<RoleMenu> rsp = new ListResult<>();
		List<RoleMenu> list = roleMenuService.getRoleMenus(role);
		rsp.setList(list);
		return rsp;
	}
	
	@PostMapping("/role/menu/add")
	public BaseResult addRoleModules(@RequestBody RoleMenu roleMenu) {
		BooleanResult rsp = new BooleanResult();
		if (StringUtils.isBlank(roleMenu.getRoleId())
				|| roleMenu.getMenuId() == 0) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "roleId or menuId");
		}
		
		if (PrivilegeUtils.isSuperAdminRole(roleMenu.getRoleId())) {
			return rsp.buildResult(SystemRestCode.ROLE_SUPERADMIN_NONEED);
		}
		
		Menu menu = menuService.getById(roleMenu.getMenuId());
		if (menu == null) {
			return rsp.buildResult(SystemRestCode.MENU_NOT_EXISTS, "roleId or menuId");
		}
		
		Role role = roleService.getRoleById(roleMenu.getRoleId());
		if (role == null) {
			return rsp.buildResult(SystemRestCode.ROLE_NOT_EXISTS);
		}
		
		roleMenuService.addRoleMenu(roleMenu);
		rsp.setResult(true);
		return rsp;
	}
	
	@DeleteMapping("/role/menu/delete")
	public BaseResult deleteRoleModules(@RequestParam("menu_id") int menuId, @RequestParam("role_id") String roleId) {
		ObjectResult<Boolean> rsp = new ObjectResult<Boolean>();
		if (StringUtils.isBlank(roleId)
				|| menuId == 0) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "role_id or menu_id");
		}
		
		
		roleMenuService.deleteRoleMenu(roleId, menuId);
		rsp.setData(true);
		return rsp;
	}
	
	
}
