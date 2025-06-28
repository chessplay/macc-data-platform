package com.ruijie.cloud.macc.dataplatform.security.entity;

import com.ruijie.cloud.dc.privilege.core.InterfaceModule;

public enum MaccData implements InterfaceModule {
	DATA_SOURCE(Module.DATA_SOURCE, "日志检索", ""),
	OPS_API(Module.OPS_API, "运维API", ""),;

	public class Module {
		public static final String DATA_SOURCE = "dataSource";
		public static final String OPS_API = "opsApi";
		public static final String WEBHOOK = "webhook";
		public static final String NOTIFY = "notify";
		public static final String SETTING = "setting";
	}

	private String moduleId;
	private String moduleName;
	private String description;

	private MaccData(String moduleId, String moduleName, String description) {
		this.moduleId = moduleId;
		this.moduleName = moduleName;
		this.description = description;
	}

	@Override
	public String moduleId() {
		return moduleId;
	}

	@Override
	public String moduleName() {
		return moduleName;
	}

	@Override
	public String description() {
		return description;
	}
}
