package com.ruijie.cloud.dc.privilege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruijie.cloud.dc.privilege.entity.business.Module;

public interface ModuleMapper {

	List<Module> selectAll(@Param("system") String system);
	
	Module selectByModuleId(@Param("system") String system, @Param("moduleId") String moduleId);
	
	int insert(Module module);
	
	int update(Module module);
	
	int delete(@Param("system") String system, @Param("moduleId") String moduleId);
}
