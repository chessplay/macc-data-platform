package com.ruijie.cloud.macc.dataplatform.security.entity;

import java.util.Date;

import lombok.Data;

@Data
public class AccessAuthorization {
	private String accessKey;
	private AccessAuthorizationState state;
	private String businessModule;
	private String businessUser;
	private String description;
	private Date expireTime;
}
