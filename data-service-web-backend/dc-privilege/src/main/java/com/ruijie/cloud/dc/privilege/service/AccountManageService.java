package com.ruijie.cloud.dc.privilege.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.dao.AccountDetailDao;
import com.ruijie.cloud.dc.privilege.entity.business.AccountDetail;
import com.ruijie.cloud.dc.privilege.entity.business.LoginUserInfo;
import com.ruijie.cloud.dc.privilege.entity.constant.AccountState;

import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountManageService {
	
	@Autowired
	private AccountDetailDao accountDetailDao;
	@Autowired
	private SystemConfig systemConfig;
	
	public void processAccountLogin(int userId) {
		LoginUserInfo userInfo = getLoginUserInfo(userId);
		
		AccountDetail ad = accountDetailDao.selectByUserId(systemConfig.getSystem(), userId);
		if (ad == null) {
			ad = new AccountDetail();
			ad.setSystem(systemConfig.getSystem());
			ad.setState(AccountState.UNCONFIRMED);
			ad.setUserId(userId);
			ad.setDescription("");
			ad.setRoleId("");
			if (userInfo != null) {
				ad.setUserName(userInfo.getUserName());
			}
			accountDetailDao.insert(ad);
		}
		
		if (userInfo == null) {
			return;
		}
		
		ad.setSystem(systemConfig.getSystem());
		ad.setUserName(userInfo.getUserName());
		accountDetailDao.update(ad);
	}
	
	private LoginUserInfo getLoginUserInfo(int userId) {
		try {
			Object userInfo = SaSsoUtil.getUserinfo(StpUtil.getLoginId());
			if (userInfo == null) {
				return null;
			}
			
			LoginUserInfo info = JSON.parseObject((String) userInfo, LoginUserInfo.class);
			if (info != null && info.getCode() == 200) {
				return info;
			}
			
			return null;
		} catch (Exception e) {
			log.error("userId : {} get userInfo failed", userId, e);
			return null;
		}
	}
	
	public AccountDetail getAccountDetail(int userId) {
		return accountDetailDao.selectByUserId(systemConfig.getSystem(), userId);
	}
	
	public List<AccountDetail> selectPage(AccountState state, String userName, String roleId, int pageIndex, int pageSize) {
		int skip = (pageIndex - 1) * pageSize;
		return accountDetailDao.selectPage(systemConfig.getSystem(), state, userName, roleId, skip, pageSize);
	}
	
	public int selectCount(AccountState state, String userName, String roleId) {
		return accountDetailDao.selectCount(systemConfig.getSystem(), state, userName, roleId);
	}
	
	public int getRoleUserCount(String roleId) {
		return accountDetailDao.selectCount(systemConfig.getSystem(), null, null, roleId);
	}
	
	public int update(AccountDetail detail) {
		if (StringUtils.isBlank(detail.getDescription())) {
			detail.setDescription("");
		}
		
		if (AccountState.ACCESS != detail.getState()) {
			detail.setRoleId("");
		}
		
		detail.setSystem(systemConfig.getSystem());
		return accountDetailDao.update(detail);
	}
	
	public int delete(int userId) {
		return accountDetailDao.delete(systemConfig.getSystem(), userId);
	}
}
