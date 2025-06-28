package com.ruijie.cloud.dc.privilege.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserInfo {
	private int userId;
	private String userName;
	private String roleId;
	private String namespace;
}
