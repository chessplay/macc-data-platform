package com.ruijie.cloud.dc.privilege.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.entity.business.Module;
import com.ruijie.cloud.dc.privilege.mapper.ModuleMapper;

@Repository
public class ModuleDao {

	@Autowired
	private ModuleMapper mapper;
	@Autowired
	private SystemConfig systemConfig;

	public List<Module> selectAll() {
		return mapper.selectAll(systemConfig.getSystem());
	}

	public Module selectByModuleId(String moduleId) {
		return mapper.selectByModuleId(systemConfig.getSystem(), moduleId);
	}

	public int insert(Module data) {
		data.setSystem(systemConfig.getSystem());
		return mapper.insert(data);
	}

	public int update(Module data) {
		data.setSystem(systemConfig.getSystem());
		return mapper.update(data);
	}

	public int delete(String moduleId) {
		return mapper.delete(systemConfig.getSystem(), moduleId);
	}
}
