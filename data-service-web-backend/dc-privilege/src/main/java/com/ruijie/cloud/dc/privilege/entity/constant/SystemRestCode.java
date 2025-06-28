package com.ruijie.cloud.dc.privilege.entity.constant;

import com.ruijie.cloud.dc.core.response.ResultCode;

public enum SystemRestCode implements ResultCode {
	OK(0, "ok."),
	PARAM_INVALID(1, "参数 {} 无效"),
	PARAM_EMPTY(2, "参数 {} 为空"),
	RECORD_NOT_EXISTS(3, "记录不存在"),
	RECORD_EXISTS(4, "记录已存在"),
	MODULE_STATIC_NOT_ALLOW_MODIFY(5, "静态模块不允许修改"),
	MODULE_NOT_EXISTS(6, "模块不存在"),
	ROLE_STATIC_NOT_ALLOW_MODIFY(7, "静态角色不允许修改"),
	ROLE_NOT_EXISTS(8, "角色不存在"),
	ROLE_SUPERADMIN_NOT_DELETE(9, "超级管理员角色不允许删除"),
	ROLE_USER_EXISTS(10, "该角色仍有用户，不允许删除"),
	SUB_MENU_EXISTS(11, "有子菜单不允许删除"),
	MENU_NOT_EXISTS(12, "菜单不存在"),
	ROLE_SUPERADMIN_NONEED(13, "超级管理员拥有所有权限，无需权限分配"),

	LOGMODEL_INVALID_ACCESS_TYPE(13, "超级管理员拥有所有权限，无需权限分配"),

	NAMESPACE_NOT_EXISTS(21, "空间不存在"),

	NOT_LOGIN(1001, "未登录"),
	NOT_ACCESS(1002, "账号状态不可用，请联系管理员授权"),
	NOT_PERMISSION(1003, "权限不足，模块:{} 权限：{}"),
	SUPERADMIN_PERMISSION(1004, "权限不足，需要超级管理员角色"),

	;

	private int code;
	private String msg;

	private SystemRestCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public int code() {
		return code + 10000;
	}

	@Override
	public String msg() {
		return msg;
	}

}
