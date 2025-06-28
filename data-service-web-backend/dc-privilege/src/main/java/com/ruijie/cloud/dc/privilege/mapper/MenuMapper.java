package com.ruijie.cloud.dc.privilege.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.entity.business.Menu;

public interface MenuMapper {
	List<Menu> selectByNamespace(@Param("system") String system, @Param("namespace") String namespace);

	List<Menu> selectByIds(@Param("system") String system, @Param("ids") Collection<Integer> ids);

	Menu selectById(@Param("system") String system, @Param("id") int id);

	List<Menu> selectByParentPath(@Param("system") String system, @Param("parentPath") String parentPath);

	int insert(Menu menu);

	int update(Menu menu);

	int delete(@Param("system") String system, @Param("id") int id);
}
