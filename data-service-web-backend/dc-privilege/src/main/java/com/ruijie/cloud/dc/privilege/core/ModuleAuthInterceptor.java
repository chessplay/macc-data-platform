package com.ruijie.cloud.dc.privilege.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ruijie.cloud.dc.core.constant.UserConstant;
import com.ruijie.cloud.dc.core.response.BaseResult;
import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.business.RoleNamespace;
import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;
import com.ruijie.cloud.dc.privilege.entity.constant.SystemRestCode;
import com.ruijie.cloud.dc.privilege.service.PrivilegeService;
import com.ruijie.cloud.dc.privilege.service.RoleNamespaceService;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ModuleAuthInterceptor implements HandlerInterceptor {
	private static final String REQUEST_NAMESPACE = "bc_request_ns";

	@Autowired
	private SystemConfig systemConfig;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private RoleNamespaceService roleNamespaceService;

	@Value("${sa-token.enabled}")
	private boolean ssoEnable;
	@Value("${server.servlet.contextPath}")
	private String contextPath;

	private String ssoPath;

	private String privilegePath;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!ssoEnable) {
			return true;
		}

		// sso请求由控制器处理，此处无需权限控制
		if (isSSORequest(request.getRequestURI())) {
			return true;
		}

		// 是否忽略认证
		if (ignoreAuth(request, handler)) {
			return true;
		}

		if (!StpUtil.isLogin()) {
			writeResponse(response, BaseResult.build(SystemRestCode.NOT_LOGIN));
			return false;
		}

		int userId = StpUtil.getLoginIdAsInt();
		AccountDetail accountDetail = privilegeService.accountInfo(userId);
		if (accountDetail == null || AccountState.ACCESS != accountDetail.getState()) {
			if (accountDetail == null) {
				StpUtil.logout();
			}

			writeResponse(response, BaseResult.build(SystemRestCode.NOT_ACCESS));
			return false;
		}

		// 判断用户是否有空间权限
		if (!hasNamespacePrivilege(request, accountDetail.getRoleId())) {
			writeResponse(response, BaseResult.build(SystemRestCode.NOT_ACCESS));
			return false;
		}

		// 设置登录用户信息
		String namespace = systemConfig.namespaceEnable() ? request.getParameter(REQUEST_NAMESPACE) : "MACC";
		LoginUserInfo.LoginUserInfoBuilder loginUserBuilder =
				new LoginUserInfo.LoginUserInfoBuilder().userId(accountDetail.getUserId())
						.roleId(accountDetail.getRoleId()).userName(accountDetail.getUserName()).namespace(namespace);
		PrivilegeUtils.setLoginUser(loginUserBuilder.build());

		request.setAttribute(UserConstant.USER_ID_KEY, accountDetail.getUserId());
		request.setAttribute(UserConstant.USER_NAME_KEY, accountDetail.getUserName());
		request.setAttribute(UserConstant.USER_NAME_ROLE, accountDetail.getRoleId());

		// 权限管理接口需要超级管理员
		if (isPrivilegeRequest(request.getRequestURI())) {
			if (privilegeService.isSuperAdmin(userId)) {
				return true;
			}

			writeResponse(response, BaseResult.build(SystemRestCode.SUPERADMIN_PERMISSION));
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
			writeResponse(response, BaseResult.build(SystemRestCode.NOT_PERMISSION, moduleId, po));
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		// 清理线程变量中的登录用户信息
		PrivilegeUtils.clearLoginUser();
	}


	private boolean isSSORequest(String requestUri) {
		if (ssoPath == null) {
			ssoPath = contextPath + "/sso/";
		}

		return StringUtils.startsWith(requestUri, ssoPath);
	}

	private boolean ignoreAuth(HttpServletRequest request, Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			log.warn("request : {} not intercept, class : {} ", request.getRequestURI(), handler.getClass());
			return false;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		IgnoreAuth ano = handlerMethod.getMethodAnnotation(IgnoreAuth.class);
		return ano != null;
	}

	private boolean isPrivilegeRequest(String requestUri) {
		if (privilegePath == null) {
			privilegePath = contextPath + "/privilege/";
		}

		return StringUtils.startsWith(requestUri, privilegePath);
	}

	private void writeResponse(HttpServletResponse response, BaseResult rsp) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().append(JSON.toJSONString(rsp));
	}

	private boolean hasNamespacePrivilege(HttpServletRequest request, String userRoleId) {
		String namespace = request.getParameter(REQUEST_NAMESPACE);
		if (StringUtils.isBlank(namespace)) {
			return true;
		}

		RoleNamespace roleNamespace = roleNamespaceService.getByRoleAndNamespace(userRoleId, namespace);
		return roleNamespace != null;
	}
}
