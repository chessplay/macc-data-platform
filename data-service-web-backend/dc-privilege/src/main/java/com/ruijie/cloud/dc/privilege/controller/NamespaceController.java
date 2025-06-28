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
import com.ruijie.cloud.dc.privilege.entity.business.Namespace;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.NamespaceService;

/**
 * @author Hui
 * @description: 命名空间管理
 * @create: 2022/08/29 14:08
 **/
@RestController
@RequestMapping("/privilege/namespace")
public class NamespaceController {

	@Autowired
	private NamespaceService service;

	@GetMapping("/list")
	public ListResult<Namespace> list() {
		ListResult<Namespace> rsp = new ListResult<>();
		List<Namespace> list = service.getAllNamespace();
		rsp.setList(list);
		return rsp;
	}

	@PostMapping("/info")
	public BaseResult addModule(@RequestBody Namespace data) {
		BooleanResult rsp = new BooleanResult();
		if (StringUtils.isAnyBlank(data.getName(), data.getNamespace())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "roleId or roleName");
		}

		service.addOrUpdate(data);
		rsp.setResult(true);
		return rsp;
	}

	@DeleteMapping("/delete")
	public BaseResult delete(@RequestParam("namespace") String namespace) {
		BooleanResult rsp = new BooleanResult();
		service.delete(namespace);
		rsp.setResult(true);
		return rsp;
	}
}
