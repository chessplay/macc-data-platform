package com.ruijie.cloud.dc.privilege.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.entity.business.Menu;
import com.ruijie.cloud.dc.privilege.mapper.MenuMapper;

@Repository
public class MenuDao {

	@Autowired
	private MenuMapper mapper;
	@Autowired
	private SystemConfig systemConfig;

	public List<Menu> selectByNamespace(String namespace) {
		return mapper.selectByNamespace(systemConfig.getSystem(), namespace);
	}

	public List<Menu> selectByIds(Collection<Integer> ids) {
		return mapper.selectByIds(systemConfig.getSystem(), ids);
	}

	public Menu selectById(int id) {
		return mapper.selectById(systemConfig.getSystem(), id);
	}

	public List<Menu> selectByParentPath(String parentPath) {
		return mapper.selectByParentPath(systemConfig.getSystem(), parentPath);
	}

	public int insert(Menu menu) {
		menu.setSystem(systemConfig.getSystem());
		return mapper.insert(menu);
	}

	public int update(Menu menu) {
		menu.setSystem(systemConfig.getSystem());
		return mapper.update(menu);
	}

	public int delete(int id) {
		return mapper.delete(systemConfig.getSystem(), id);
	}
}
