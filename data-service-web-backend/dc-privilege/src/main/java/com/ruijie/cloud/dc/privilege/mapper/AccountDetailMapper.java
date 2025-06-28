package com.ruijie.cloud.dc.privilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;

public interface AccountDetailMapper {

	List<AccountDetail> selectPage(@Param("system") String system, @Param("state") AccountState state,
			@Param("userName") String userName, @Param("roleId") String roleId, @Param("skipCount") int skipCount,
			@Param("size") int size);

	int selectCount(@Param("system") String system, @Param("state") AccountState state, @Param("userName")  String userName, @Param("roleId") String roleId);

	AccountDetail selectByUserId(@Param("system") String system, @Param("userId") int userId);

	int insert(AccountDetail detail);

	int update(AccountDetail detail);

	int delete(@Param("system") String system, @Param("userId") int userId);
}
