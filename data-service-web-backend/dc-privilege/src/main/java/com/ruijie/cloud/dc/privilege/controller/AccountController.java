package com.ruijie.cloud.dc.privilege.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruijie.cloud.dc.core.controller.BaseController;
import com.ruijie.cloud.dc.core.response.BaseResult;
import com.ruijie.cloud.dc.core.response.ListResult;
import com.ruijie.cloud.dc.core.response.ObjectResult;
import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.business.Menu;
import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.AccountManageService;
import com.ruijie.cloud.dc.privilege.service.MenuService;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

import cn.dev33.satoken.stp.StpUtil;

@RestController
@RequestMapping("/user")
public class AccountController extends BaseController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private AccountManageService accountManageService;

	@GetMapping("/info")
	public BaseResult info() {
		ObjectResult<AccountDetail> rsp = new ObjectResult<AccountDetail>();
		AccountDetail detail = accountManageService.getAccountDetail(StpUtil.getLoginIdAsInt());
		if (detail == null) {
			return rsp.buildResult(SystemRestCode.RECORD_NOT_EXISTS);
		}

		detail.setRoleId(null);
		rsp.setData(detail);
		return rsp;
	}

	@GetMapping("/menu/tree")
	public BaseResult userMenus() {
		ListResult<Menu> rsp = new ListResult<>();
		AccountDetail at = accountManageService.getAccountDetail(StpUtil.getLoginIdAsInt());
		if (at == null) {
			return rsp.buildResult(SystemRestCode.RECORD_NOT_EXISTS);
		}

		if (AccountState.ACCESS != at.getState()) {
			return rsp.buildResult(SystemRestCode.NOT_ACCESS);
		}

		String namespace = PrivilegeUtils.currentNamespace();
		List<Menu> menuTree = menuService.getUserMenuTree(namespace, at.getRoleId());
		rsp.setList(menuTree);
		return rsp;
	}

	@GetMapping("/menu/tree11")
	public BaseResult userNamespaceMenus(@RequestParam("namespace") String namespace) {
		ListResult<Menu> rsp = new ListResult<>();
		AccountDetail at = accountManageService.getAccountDetail(StpUtil.getLoginIdAsInt());
		if (at == null) {
			return rsp.buildResult(SystemRestCode.RECORD_NOT_EXISTS);
		}

		if (AccountState.ACCESS != at.getState()) {
			return rsp.buildResult(SystemRestCode.NOT_ACCESS);
		}

		List<Menu> menuTree = menuService.getUserMenuTree(namespace, at.getRoleId());
		rsp.setList(menuTree);
		return rsp;
	}
}
