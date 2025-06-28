package com.ruijie.cloud.dc.privilege.entity.business;

import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;

import lombok.Data;

@Data
public class LoginUserInfo {
	private int code;
	private String msg;
	
	private int userId;
	private String userName;
	private String account;
	private AccountState state;
}
