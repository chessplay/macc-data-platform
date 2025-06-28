package com.ruijie.cloud.dc.privilege.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;
import com.ruijie.cloud.dc.privilege.mapper.AccountDetailMapper;

@Repository
public class AccountDetailDao {

	@Autowired
	private AccountDetailMapper mapper;
	
	public AccountDetail selectByUserId(String system, int userId) {
		return mapper.selectByUserId(system, userId);
	}
	
	public List<AccountDetail> selectPage(String system, AccountState state, String userName, String roleId, int skipCount, int size) {
		return mapper.selectPage(system, state, userName, roleId, skipCount, size);
	}
	
	public int selectCount(String system, AccountState state, String userName, String roleId) {
		return mapper.selectCount(system, state, userName, roleId);
	}
	
	public int insert(AccountDetail detail) {
		return mapper.insert(detail);
	}
	
	public int update(AccountDetail detail) {
		return mapper.update(detail);
	}
	
	public int delete(String system, int userId) {
		return mapper.delete(system, userId);
	}
}
