package com.ruijie.cloud.macc.dataplatform.metadata.entity.constant;

import com.ruijie.cloud.dc.core.response.ResultCode;

public enum RestCode implements ResultCode {

	SUCCESS(0, "OK."),
	PARAM_EMPTY(1, "参数{}为空"),
	PARAM_INVALID(2, "参数{}不合法"),
	UNAUTH(3, "请求未授权"),
	INVALID_TOKEN(4, "token无效"),
	PERMISSION_DENIED(5, "权限不足"),

	DATA_SOURCE_EXISTS(30, "数据源已存在"),
	DATA_SOURCE_INACTIVE(31, "数据源不存在或者不可用"),

	API_REQUEST_ERROR(28, "请求异常：{}"),

	PERMISSION_DENY(90, "无权限访问"),

	UNKNOWN_ERROR(100, "未知错误； {}"),;

	private int id;
	private String msg;

	private RestCode(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public int code() {
		return id;
	}

	@Override
	public String msg() {
		return msg;
	}
}
