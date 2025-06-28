package com.ruijie.cloud.dc.privilege.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruijie.cloud.dc.privilege.config.SystemConfig;
import com.ruijie.cloud.dc.privilege.entity.business.Namespace;
import com.ruijie.cloud.dc.privilege.mapper.NamespaceMapper;

/**
 * @author Hui
 * @description: TODO
 * @create: 2022/08/29 14:08
 **/
@Component
public class NamespaceDao {
	@Autowired
	private NamespaceMapper mapper;
	@Autowired
	private SystemConfig systemConfig;

	public List<Namespace> selectAll() {
		return mapper.selectAll(systemConfig.getSystem());
	}

	public Namespace selectByNamespace(String namespace) {
		return mapper.selectByNamespace(systemConfig.getSystem(), namespace);
	}

	public int update(Namespace info) {
		info.setSystem(systemConfig.getSystem());
		return mapper.update(info);
	}

	public int insert(Namespace info) {
		info.setSystem(systemConfig.getSystem());
		return mapper.insert(info);
	}

	public int delete(String namespace) {
		return mapper.delete(systemConfig.getSystem(), namespace);
	}

}
