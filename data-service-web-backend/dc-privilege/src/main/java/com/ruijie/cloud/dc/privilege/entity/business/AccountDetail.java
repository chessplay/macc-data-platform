package com.ruijie.cloud.dc.privilege.entity.business;

import java.util.Date;

import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;

import lombok.Data;

@Data
public class AccountDetail {
	private String system;
	private int userId;
	private String userName;
	private String roleId;
	private AccountState state;
	private String defaultNamespace;
	private String description;
	private Date createTime;
}

