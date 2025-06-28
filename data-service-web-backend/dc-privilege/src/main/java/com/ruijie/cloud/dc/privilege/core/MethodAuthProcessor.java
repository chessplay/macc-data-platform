package com.ruijie.cloud.dc.privilege.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;

import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.service.PrivilegeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MethodAuthProcessor implements RequestAuthProcessor {
	@Value("${sa-token.enabled}")
	private boolean ssoEnable;
	@Value("${server.servlet.contextPath}")
	private String contextPath;
	private String privilegePath;

	@Autowired
	private PrivilegeService privilegeService;

	@Override
	public boolean hasPrivilege(HttpServletRequest request, Object handler, AccountDetail account) {
		int userId = account.getUserId();

		// 权限管理接口需要超级管理员
		if (isPrivilegeRequest(request.getRequestURI())) {
			if (privilegeService.isSuperAdmin(userId)) {
				return true;
			}

			log.warn("user :{} is not superadmin, permission deny");
			return false;
		}

		if (!(handler instanceof HandlerMethod)) {
			log.warn("request : {} not intercept, class : {} ", request.getRequestURI(), handler.getClass());
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		ModuleAuth ano = handlerMethod.getMethodAnnotation(ModuleAuth.class);
		if (ano == null) {
			log.info("request : {} module not set", request.getRequestURI());
			return true;
		}

		String moduleId = ano.moduleId();
		PrivilegeOperation po = ano.privilegeOperation();
		if (!privilegeService.hasPrivilege(userId, moduleId, po)) {
			log.warn("user :{} permission deny at module :{} privilege :{}", userId, moduleId, po);
			return false;
		}

		return true;
	}

	private boolean isPrivilegeRequest(String requestUri) {
		if (privilegePath == null) {
			privilegePath = contextPath + "/privilege/";
		}

		return StringUtils.startsWith(requestUri, privilegePath);
	}
}
