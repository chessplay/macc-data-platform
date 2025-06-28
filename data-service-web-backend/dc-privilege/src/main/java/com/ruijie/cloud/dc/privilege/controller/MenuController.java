package com.ruijie.cloud.dc.privilege.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruijie.cloud.dc.core.response.BaseResult;
import com.ruijie.cloud.dc.core.response.BooleanResult;
import com.ruijie.cloud.dc.core.response.ListResult;
import com.ruijie.cloud.dc.privilege.entity.business.Menu;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.MenuService;
import com.ruijie.cloud.dc.privilege.service.RoleMenuService;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

@RestController
@RequestMapping("/privilege/menu")
public class MenuController {
	@Autowired
	private MenuService service;
	@Autowired
	private RoleMenuService roleMenuService;

	@GetMapping("/tree")
	public ListResult<Menu> list() {
		ListResult<Menu> rsp = new ListResult<>();
		String namespace = PrivilegeUtils.currentNamespace();
		rsp.setList(service.getMenuTree(namespace));
		return rsp;
	}

	@PostMapping("/add/{parentId}")
	public BaseResult add(@PathVariable("parentId") int parentId, @RequestBody Menu req) {
		BooleanResult rsp = new BooleanResult();

		if (StringUtils.isBlank(req.getName())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "name");
		}

		String namespace = PrivilegeUtils.currentNamespace();
		int ret = service.add(namespace, parentId, req);
		if (ret > 0) {
			rsp.setResult(true);
		}

		return rsp;
	}

	@PutMapping("/update")
	public BaseResult update(@RequestBody Menu req) {
		BooleanResult rsp = new BooleanResult();
		if (StringUtils.isBlank(req.getName())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "name");
		}

		if (req.getId() == 0) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "id");
		}

		service.update(req);
		return rsp;
	}

	@DeleteMapping("/delete")
	public BaseResult delete(@RequestParam("id") int id) {
		BooleanResult rsp = new BooleanResult();
		if (id == 0) {
			return rsp.buildResult(SystemRestCode.PARAM_INVALID, "id");
		}

		List<Menu> subMenus = service.getSubMenus(id);
		if (CollectionUtils.isEmpty(subMenus)) {
			boolean ret = service.delete(id);
			roleMenuService.deleteByMenu(id);
			rsp.setResult(ret);
			return rsp;
		} else {
			return rsp.buildResult(SystemRestCode.SUB_MENU_EXISTS);
		}
	}
}
