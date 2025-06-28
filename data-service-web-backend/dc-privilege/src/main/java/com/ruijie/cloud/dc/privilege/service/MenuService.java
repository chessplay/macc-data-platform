package com.ruijie.cloud.dc.privilege.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ruijie.cloud.dc.privilege.dao.MenuDao;
import com.ruijie.cloud.dc.privilege.dao.RoleMenuDao;
import com.ruijie.cloud.dc.privilege.entity.business.Menu;
import com.ruijie.cloud.dc.privilege.entity.business.RoleMenu;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuService {
	@Autowired
	private MenuDao dao;
	@Autowired
	private RoleMenuDao roleMenuDao;

	public List<Menu> getUserMenuTree(String namespace, String roleId) {
		if (PrivilegeUtils.isSuperAdminRole(roleId)) {
			return getMenuTree(namespace);
		}

		List<RoleMenu> roleMenus = roleMenuDao.selectByRoleId(roleId);
		Set<Integer> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());

		List<Menu> allList = dao.selectByNamespace(namespace);
		List<Menu> userMenuList =
				allList.stream().filter(m -> menuIds.contains(m.getId())).collect(Collectors.toList());

		List<Menu> treeNodes = new ArrayList<>();
		for (Menu m : allList) {
			String mPath = m.getParentPath() + m.getId() + "/";
			for (Menu um : userMenuList) {
				String umPath = um.getParentPath() + um.getId() + "/";
				// 找到用户有权限的对应的父节点和子节点
				if (StringUtils.startsWith(mPath, umPath) || StringUtils.startsWith(umPath, mPath)) {
					treeNodes.add(m);
					break;
				}
			}
		}

		return buildTree(treeNodes);
	}

	public Menu getById(int id) {
		return dao.selectById(id);
	}

	public int add(String namespace, int parentMenuId, Menu menu) {
		String parentPath = "";
		if (parentMenuId == 0) {
			parentPath = "0/";
		} else {
			Menu parentMenu = dao.selectById(parentMenuId);
			// 上级菜单不存在或者不属于当前操作空间，不执行
			if (parentMenu == null || !StringUtils.equals(namespace, parentMenu.getNamespace())) {
				return 0;
			}

			parentPath = parentMenu.getParentPath() + parentMenuId + "/";
		}

		menu.setNamespace(namespace);
		menu.setParentPath(parentPath);
		if (StringUtils.isBlank(menu.getIcon())) {
			menu.setIcon("");
		}
		if (StringUtils.isBlank(menu.getUrl())) {
			menu.setUrl("");
		}
		if (StringUtils.isBlank(menu.getParams())) {
			menu.setParams("");
		}
		return dao.insert(menu);
	}

	public int update(Menu menu) {
		if (StringUtils.isBlank(menu.getIcon())) {
			menu.setIcon("");
		}
		if (StringUtils.isBlank(menu.getUrl())) {
			menu.setUrl("");
		}
		if (StringUtils.isBlank(menu.getParams())) {
			menu.setParams("");
		}
		return dao.update(menu);
	}

	public boolean delete(int id) {
		return dao.delete(id) > 0;
	}

	public List<Menu> getSubMenus(int id) {
		Menu menu = dao.selectById(id);
		if (menu == null) {
			return null;
		}

		String menuPath = menu.getParentPath() + id + "/";
		return dao.selectByParentPath(menuPath);
	}

	public List<Menu> getMenuTree(String namespace) {
		List<Menu> list = dao.selectByNamespace(namespace);
		if (CollectionUtils.isEmpty(list)) {
			return Arrays.asList();
		}

		return buildTree(list);
	}

	private List<Menu> buildTree(List<Menu> list) {
		Collections.sort(list, new Comparator<Menu>() {
			@Override
			public int compare(Menu o1, Menu o2) {
				return o1.getPriority() - o2.getPriority();
			}
		});

		List<Menu> rootMenus = new ArrayList<>();
		Map<Integer, Menu> menuMap = list.stream().collect(Collectors.toMap(Menu::getId, m -> m));
		for (Menu menu : list) {
			Integer parentId = getParentId(menu.getParentPath());
			if (parentId == null) {
				continue;
			}

			if (parentId == 0) {
				rootMenus.add(menu);
				continue;
			}

			Menu p = menuMap.get(parentId);
			if (p == null) {
				log.error("menu : {} parentMenu not exists", menu);
				continue;
			}

			if (p.getSubList() == null) {
				p.setSubList(new ArrayList<>());
			}

			p.getSubList().add(menu);
		}

		return rootMenus;
	}

	public Integer getParentId(String parentPath) {
		if (!parentPath.matches("([\\d]+/)+")) {
			log.error("invalid parentPath : {}", parentPath);
			return null;
		}

		String[] split = parentPath.split("/");
		return Integer.parseInt(split[split.length - 1]);
	}
}
