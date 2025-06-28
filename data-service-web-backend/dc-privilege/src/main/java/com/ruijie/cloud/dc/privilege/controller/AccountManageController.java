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
import com.ruijie.cloud.dc.core.response.PageResult;
import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.business.Role;
import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.AccountManageService;
import com.ruijie.cloud.dc.privilege.service.RoleService;

@RestController
@RequestMapping("/privilege/account")
public class AccountManageController {

	@Autowired
	private AccountManageService accountManageService;
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/page")
	public BaseResult page(@RequestParam("page_index") int pageIndex, 
			@RequestParam("page_size") int pageSize,
			@RequestParam(value = "user_name", required = false) String userName, 
			@RequestParam(value = "state", required = false) AccountState state,
			@RequestParam(value = "role_id", required = false) String roleId) {
		PageResult<AccountDetail> rsp = new PageResult<>();
		if (pageIndex <= 0 || pageSize <= 0 || pageSize > 1000) {
			return rsp.buildResult(SystemRestCode.PARAM_INVALID, "page");
		}
		
		List<AccountDetail> list = accountManageService.selectPage(state, userName, roleId, pageIndex, pageSize);
		int totalCount = accountManageService.selectCount(state, userName, roleId);
		rsp.setDataList(list);
		rsp.setTotalCount(totalCount);
		return rsp;
	}
	
	@PostMapping("/update")
	public BaseResult update(@RequestBody AccountDetail detail) {
		BooleanResult rsp = new BooleanResult();
		if (detail.getState() == null || detail.getUserId() == 0) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "state or userId");
		}
		
		if (StringUtils.isBlank(detail.getUserName())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "userName");
		}
		
		if (detail.getState() == AccountState.ACCESS && StringUtils.isBlank(detail.getRoleId())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "roleId");
		}
		
		if (StringUtils.isNotBlank(detail.getRoleId())) {
			Role role = roleService.getRoleById(detail.getRoleId());
			if (role == null) {
				return rsp.buildResult(SystemRestCode.ROLE_NOT_EXISTS);
			}
		}
		
		accountManageService.update(detail);
		rsp.setResult(true);
		return rsp;
	}
	
	@DeleteMapping("/delete")
	public BaseResult delete(@RequestParam("user_id") int userId) {
		BooleanResult rsp = new BooleanResult();
		accountManageService.delete(userId);
		rsp.setResult(true);
		return rsp;
	}
}
