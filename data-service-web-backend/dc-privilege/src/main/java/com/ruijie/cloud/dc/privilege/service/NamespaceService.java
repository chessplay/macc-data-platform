package com.ruijie.cloud.dc.privilege.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.cloud.dc.privilege.dao.NamespaceDao;
import com.ruijie.cloud.dc.privilege.entity.business.Namespace;

/**
 * @author Hui
 * @description: TODO
 * @create: 2022/08/29 14:08
 **/
@Service
public class NamespaceService {
	@Autowired
	private NamespaceDao dao;

	public boolean namespaceExists(String namespace) {
		return getNamespace(namespace) != null;
	}

	public List<Namespace> getAllNamespace() {
		return dao.selectAll();
	}

	public Namespace getNamespace(String namespace) {
		return dao.selectByNamespace(namespace);
	}

	public int addOrUpdate(Namespace data) {
		data.setDescription("");

		Namespace dbData = dao.selectByNamespace(data.getNamespace());
		if (dbData != null) {
			return dao.update(data);
		} else {
			return dao.insert(data);
		}
	}

	public int delete(String namespace) {
		return dao.delete(namespace);
	}
}
