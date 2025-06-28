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
import com.ruijie.cloud.dc.privilege.entity.business.RoleNamespace;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.NamespaceService;
import com.ruijie.cloud.dc.privilege.service.RoleNamespaceService;
import com.ruijie.cloud.dc.privilege.service.RoleService;

@RestController
@RequestMapping("/privilege/setting/role/namespace")
public class RoleNamespaceController extends BaseController {
	@Autowired
	private RoleNamespaceService roleNamespaceService;
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private RoleService roleService;

	@GetMapping("/list")
	public BaseResult getList(@RequestParam("role_id") String roleId) {
		ListResult<RoleNamespace> rsp = new ListResult<>();
		if (StringUtils.isBlank(roleId)) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "roleId");
		}

		List<RoleNamespace> list = roleNamespaceService.getByRoleId(roleId);
		rsp.setList(list);
		return rsp;
	}

	@PostMapping("/add")
	public BaseResult addRoleNamespace(@RequestBody RoleNamespace data) {
		BooleanResult rsp = new BooleanResult();
		if (StringUtils.isAnyBlank(data.getNamespace(), data.getRoleId())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "namespace or roleId");
		}

		if (!namespaceService.namespaceExists(data.getNamespace())) {
			return rsp.buildResult(SystemRestCode.NAMESPACE_NOT_EXISTS);
		}

		if (roleService.roleExists(data.getRoleId())) {
			return rsp.buildResult(SystemRestCode.ROLE_NOT_EXISTS);
		}

		roleNamespaceService.addRoleNamepace(data);
		rsp.setResult(true);
		return rsp;
	}

	@DeleteMapping("/delete")
	public BaseResult deleteRoleNamespace(@RequestParam("role_id") String roleId,
			@RequestParam("namespace") String namespace) {
		ObjectResult<Boolean> rsp = new ObjectResult<Boolean>();
		roleNamespaceService.deleteByRoleAndNamespace(roleId, namespace);
		rsp.setData(true);
		return rsp;
	}
}
