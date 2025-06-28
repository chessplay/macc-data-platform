package com.ruijie.cloud.dc.privilege.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruijie.cloud.dc.privilege.service.AccountManageService;

import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class SsoController {
	@Autowired
	private AccountManageService accountManageService;
	
	@RequestMapping("/sso/*")
	public Object ssoRequest() {
		boolean isLogin = StpUtil.isLogin();
		Object rsp = SaSsoHandle.clientRequest();
		
		if (!isLogin && StpUtil.isLogin()) {
			//登录过程在此处完成，同步用户信息
			int userId = StpUtil.getLoginIdAsInt();
			accountManageService.processAccountLogin(userId);
			log.warn("userId : {} login.", StpUtil.getLoginId());
		}
		
		return rsp;
	}
}
