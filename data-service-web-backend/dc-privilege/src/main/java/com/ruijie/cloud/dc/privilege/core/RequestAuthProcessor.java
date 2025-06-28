package com.ruijie.cloud.dc.privilege.core;

import javax.servlet.http.HttpServletRequest;

import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;

public interface RequestAuthProcessor {
	boolean hasPrivilege(HttpServletRequest request, Object handler, AccountDetail account);
}
