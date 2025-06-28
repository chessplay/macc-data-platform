package com.ruijie.cloud.dc.privilege.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ejlchina.okhttps.OkHttps;
import com.ruijie.cloud.dc.privilege.core.ModuleAuthInterceptor;

import cn.dev33.satoken.config.SaTokenConfig;

@Component
public class InterceptorConfiguration implements WebMvcConfigurer {
	@Autowired
	private ModuleAuthInterceptor authInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).addPathPatterns("/**");
	}

	// 配置SSO相关参数
	@Autowired
	private void configSso(SaTokenConfig cfg) {
		cfg.sso.setSendHttp(url -> {
			return OkHttps.sync(url).get().getBody().toString();
		});
	}
}
