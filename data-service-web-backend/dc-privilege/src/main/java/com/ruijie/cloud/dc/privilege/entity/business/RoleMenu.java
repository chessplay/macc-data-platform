package com.ruijie.cloud.dc.privilege.entity.business;

import java.util.Date;

import lombok.Data;

@Data
public class RoleMenu {
	private int id;
	private String system;
	private String roleId;
	private int menuId;
	private Date createTime;
}
