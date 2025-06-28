package com.ruijie.cloud.dc.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.cloud.dc.core.constant.UserConstant;
import com.ruijie.cloud.dc.core.domain.R;
import com.ruijie.cloud.dc.core.utils.StringUtils;

public abstract class BaseController {
	@Autowired
	private HttpServletRequest request;

	protected int userId() {
		Object obj = request.getAttribute(UserConstant.USER_ID_KEY);
		if (obj == null) {
			return 0;
		}
		return (int) obj;
	}

	protected String userName() {
		Object obj = request.getAttribute(UserConstant.USER_NAME_KEY);

		if (obj == null) {
			return "";
		}

		return (String) obj;
	}

	protected String namespace() {
		return "DEFAULT";
	}

	protected String environment() {
		return "cloud";
	}


	/**
	 * 响应返回结果
	 *
	 * @param rows 影响行数
	 * @return 操作结果
	 */
	protected R<Void> toAjax(int rows) {
		return rows > 0 ? R.ok() : R.fail();
	}

	/**
	 * 响应返回结果
	 *
	 * @param result 结果
	 * @return 操作结果
	 */
	protected R<Void> toAjax(boolean result) {
		return result ? R.ok() : R.fail();
	}

	/**
	 * 页面跳转
	 */
	public String redirect(String url) {
		return StringUtils.format("redirect:{}", url);
	}


}
