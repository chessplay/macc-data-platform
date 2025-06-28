package com.ruijie.cloud.dc.privilege.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.cloud.dc.privilege.core.InterfaceModule;
import com.ruijie.cloud.dc.privilege.core.ModuleAuth;
import com.ruijie.cloud.dc.privilege.dao.ModuleDao;
import com.ruijie.cloud.dc.privilege.entity.business.Module;
import com.ruijie.cloud.dc.privilege.entity.constant.ModuleType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ModuleService {
	private static List<Module> STATIC_MODULES;

	@Autowired
	private ModuleDao dao;

	public List<Module> getAllModules() {
		List<Module> modules = dao.selectAll();
		modules.forEach(m -> m.setType(ModuleType.DYNAMIC));

		List<Module> staticModules = getStaticModule();
		modules.addAll(staticModules);
		return modules;
	}

	public int update(Module module) {
		if (StringUtils.isBlank(module.getDescription())) {
			module.setDescription("");
		}
		module.setType(ModuleType.DYNAMIC);
		return dao.update(module);
	}

	public int add(Module module) {
		if (StringUtils.isBlank(module.getDescription())) {
			module.setDescription("");
		}
		module.setType(ModuleType.DYNAMIC);
		return dao.insert(module);
	}

	public int delModule(String moduleId) {
		return dao.delete(moduleId);
	}

	public ModuleType moduleType(String moduleId) {
		List<Module> staticModules = getStaticModule();
		for (Module staticModule : staticModules) {
			if (StringUtils.equals(moduleId, staticModule.getModuleId())) {
				return ModuleType.STATIC;
			}
		}

		Module m = dao.selectByModuleId(moduleId);
		return m != null ? ModuleType.DYNAMIC : null;
	}

	private List<Module> getStaticModule() {
		if (STATIC_MODULES != null) {
			return STATIC_MODULES;
		}

		Reflections reflections = new Reflections("com.ruijie.cloud");
		Set<Class<? extends InterfaceModule>> moduleClass = reflections.getSubTypesOf(InterfaceModule.class);
		List<Module> modules = new ArrayList<>();
		for (Class<? extends InterfaceModule> clazz : moduleClass) {
			if (!clazz.isEnum()) {
				log.error("class : {} is invalid", clazz);
				continue;
			}

			Enum<?>[] staticModules = (Enum<?>[]) clazz.getEnumConstants();
			for (Object obj : staticModules) {
				InterfaceModule im = (InterfaceModule) obj;
				Module module = new Module();
				module.setModuleId(im.moduleId());
				module.setModuleName(im.moduleName());
				module.setDescription(im.description());
				module.setType(ModuleType.STATIC);
				modules.add(module);
			}
		}

		STATIC_MODULES = modules;
		return modules;
	}

	private List<Module> getModuleAuthModules() {
		if (STATIC_MODULES != null) {
			return STATIC_MODULES;
		}

		Reflections reflections = new Reflections("com.ruijie.cloud", new MethodAnnotationsScanner());
		Set<Method> methods = reflections.getMethodsAnnotatedWith(ModuleAuth.class);
		List<Module> modules = new ArrayList<>();
		Set<String> existsModules = new HashSet<>();
		for (Method method : methods) {
			ModuleAuth ano = method.getAnnotation(ModuleAuth.class);
			String moduleId = ano.moduleId();
			if (existsModules.contains(moduleId)) {
				continue;
			}

			existsModules.add(moduleId);
			Module module = new Module();
			module.setModuleId(moduleId);
			module.setModuleName(moduleId);
			module.setType(ModuleType.STATIC);
			modules.add(module);
		}

		STATIC_MODULES = modules;
		return STATIC_MODULES;
	}

}
