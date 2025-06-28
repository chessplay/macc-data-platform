package com.ruijie.cloud.macc.dataplatform.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.cloud.macc.dataplatform.security.entity.AccessAuthorization;
import com.ruijie.cloud.macc.dataplatform.security.entity.AccessAuthorizationState;
import com.ruijie.cloud.macc.dataplatform.security.mapper.AccessAuthorizationMapper;

@Service
public class PermissionControlService {
	@Autowired
	private AccessAuthorizationMapper accessAuthorizationMapper;

	public boolean isAccess(String accessKey) {
		if (StringUtils.isAnyBlank(accessKey)) {
			return false;
		}

		AccessAuthorization auth = accessAuthorizationMapper.selectByAccessKey(accessKey);
		if (auth == null) {
			return false;
		}

		return auth.getExpireTime().getTime() > System.currentTimeMillis()
				&& auth.getState() == AccessAuthorizationState.ENABLE;
	}
}
