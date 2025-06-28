package com.ruijie.cloud.dc.privilege.entity.business;

import com.ruijie.cloud.dc.privilege.entity.constant.ModuleType;

import lombok.Data;

@Data
public class Module {
	
	private String system;
	private String moduleId;
	private String moduleName;
	private ModuleType type;
	private String description;
}
