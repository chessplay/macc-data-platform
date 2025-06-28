package com.ruijie.cloud.macc.dataplatform.security.mapper;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.macc.dataplatform.security.entity.AccessAuthorization;

public interface AccessAuthorizationMapper {
	AccessAuthorization selectByAccessKey(@Param("accessKey") String accessKey);
}
