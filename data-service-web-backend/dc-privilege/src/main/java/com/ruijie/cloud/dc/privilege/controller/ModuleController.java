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
import com.ruijie.cloud.dc.privilege.entity.business.Module;
import com.ruijie.cloud.dc.privilege.entity.constant.ModuleType;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.ModuleService;
import com.ruijie.cloud.dc.privilege.service.RoleModuleService;

@RestController
@RequestMapping("/privilege/module")
public class ModuleController {
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private RoleModuleService roleModuleService;
	
	@GetMapping("/list")
	public ListResult<Module> list() {
		ListResult<Module> rsp = new ListResult<>();
		List<Module> list = moduleService.getAllModules();
		rsp.setList(list);
		return rsp;
	}
	
	@PostMapping("/info")
	public BaseResult addModule(@RequestBody Module module) {
		BooleanResult rsp = new BooleanResult();
		if (StringUtils.isAnyBlank(module.getModuleId(), module.getModuleName())) {
			return rsp.buildResult(SystemRestCode.PARAM_EMPTY, "moduleId or moduleName");
		}
		
		ModuleType moduleType = moduleService.moduleType(module.getModuleId());
		if (moduleType == ModuleType.STATIC) {
			return rsp.buildResult(SystemRestCode.MODULE_STATIC_NOT_ALLOW_MODIFY);
		}
		
		if (moduleType != null) {
			moduleService.update(module);
		} else {
			moduleService.add(module);
		}
		rsp.setResult(true);
		return rsp;
	}
	
	@DeleteMapping("/delete")
	public BaseResult delete(@RequestParam("module_id") String moduleId) {
		BooleanResult rsp = new BooleanResult();
		ModuleType moduleType = moduleService.moduleType(moduleId);
		if (moduleType == null) {
			return rsp.buildResult(SystemRestCode.RECORD_NOT_EXISTS);
		}
		
		if (moduleType == ModuleType.STATIC) {
			return rsp.buildResult(SystemRestCode.MODULE_STATIC_NOT_ALLOW_MODIFY);
		}
		
		moduleService.delModule(moduleId);
		roleModuleService.deleteByModule(moduleId);
		rsp.setResult(true);
		return rsp;
	}
}
