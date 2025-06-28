package com.ruijie.cloud.dc.privilege.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.business.RoleNamespace;
import com.ruijie.cloud.dc.privilege.service.RoleNamespaceService;

@Component
@Order(1)
public class NamespaceAuthProcessor implements RequestAuthProcessor {
	private static final String REQUEST_NAMESPACE = "request_ns";
	@Autowired
	private RoleNamespaceService roleNamespaceService;

	@Override
	public boolean hasPrivilege(HttpServletRequest request, Object handler, AccountDetail account) {
		String namespace = request.getParameter(REQUEST_NAMESPACE);
		if (StringUtils.isBlank(namespace)) {
			return true;
		}

		RoleNamespace roleNamespace = roleNamespaceService.getByRoleAndNamespace(account.getRoleId(), namespace);
		return roleNamespace != null;
	}

}
