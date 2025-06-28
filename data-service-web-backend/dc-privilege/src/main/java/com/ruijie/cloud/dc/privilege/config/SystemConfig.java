package com.ruijie.cloud.dc.privilege.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfig {

	@Value("${server.system}")
	private String system;

	@Value("${server.servlet.contextPath}")
	private String contextPath;

	@Value("${baichuan.namespace.enable:false}")
	private boolean namespaceEnable;

	public String getSystem() {
		return system;
	}

	public boolean namespaceEnable() {
		return namespaceEnable;
	}
}
