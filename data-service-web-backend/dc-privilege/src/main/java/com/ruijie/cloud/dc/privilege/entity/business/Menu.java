package com.ruijie.cloud.dc.privilege.entity.business;

import java.util.List;

import lombok.Data;

@Data
public class Menu {
	private int id;
	private String system;
	private String namespace;
	private String parentPath;
	private String name;
	private String url;
	private String icon;
	private int priority;
	private String params;

	private List<Menu> subList;
}
