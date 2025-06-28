package com.ruijie.cloud.dc.privilege.core;
public enum PrivilegeOperation {
	READ(1, "读"), WRITE(3, "写"), NONE(0, "无");

	private int id;

	private String desc;

	private PrivilegeOperation(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	public int getOperation() {
		return getId() == 0 ? PrivilegeOperation.READ.getId() : getId();
	}
}
