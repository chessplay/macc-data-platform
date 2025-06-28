package com.ruijie.cloud.dc.privilege.entity;

import java.util.Date;

import lombok.Data;

@Data
public class SystemMenu {
	private long id;
	private String system;
	private long parentId;
	private String menuName;
	private int priority;
	private String type;
	private String params;
	private Date createTime;
	private Date updateTime;
}
