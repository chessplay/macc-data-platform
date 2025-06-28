package com.ruijie.cloud.macc.dataplatform.metadata.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruijie.cloud.dc.core.response.ListResult;
import com.ruijie.cloud.dc.core.response.ObjectResult;
import com.ruijie.cloud.dc.privilege.core.ModuleAuth;
import com.ruijie.cloud.dc.privilege.core.PrivilegeOperation;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.RestCode;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.dto.DataSourceSample;
import com.ruijie.cloud.macc.dataplatform.metadata.service.DataSourceService;
import com.ruijie.cloud.macc.dataplatform.security.entity.MaccData;

@RestController
@RequestMapping("/metadata/datasource")
public class DataSourceManageController {
	@Autowired
	private DataSourceService dataSourceService;

	@GetMapping("/name/list")
	@ModuleAuth(moduleId = MaccData.Module.DATA_SOURCE)
	public ListResult<DataSourceModel> nameList(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "db_type", required = false) DataSourceType dbType) {
		ListResult<DataSourceModel> rsp = new ListResult<>();
		List<DataSourceModel> list = dataSourceService.getList(keyword, dbType);
		list.forEach(model -> model.setDbProperties(null));
		rsp.setList(list);
		return rsp;
	}

	@GetMapping("/list")
	@ModuleAuth(moduleId = MaccData.Module.DATA_SOURCE, privilegeOperation = PrivilegeOperation.WRITE)
	public ListResult<DataSourceModel> list(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "db_type", required = false) DataSourceType dbType) {
		ListResult<DataSourceModel> rsp = new ListResult<>();
		List<DataSourceModel> list = dataSourceService.getList(keyword, dbType);
		rsp.setList(list);
		return rsp;
	}

	@PostMapping("/add")
	@ModuleAuth(moduleId = MaccData.Module.DATA_SOURCE, privilegeOperation = PrivilegeOperation.WRITE)
	public ObjectResult<Boolean> addJdbc(@RequestBody DataSourceModel model) {
		ObjectResult<Boolean> rsp = new ObjectResult<>();
		if (StringUtils.isAnyBlank(model.getDataSourceKey(), model.getName())) {
			rsp.buildResult(RestCode.PARAM_EMPTY, "dataSourceKey or name");
			return rsp;
		}

		if (model.getDbType() == null) {
			rsp.buildResult(RestCode.PARAM_EMPTY, "dbType");
			return rsp;
		}

		if (!dataSourceService.isDataSourceValid(model)) {
			rsp.buildResult(RestCode.PARAM_INVALID, "连接信息");
			return rsp;
		}

		if (dataSourceService.isDataSourceExists(model.getDataSourceKey())) {
			rsp.buildResult(RestCode.DATA_SOURCE_EXISTS);
			return rsp;
		}

		dataSourceService.add(model);
		return rsp;
	}


	@PostMapping("/update")
	@ModuleAuth(moduleId = MaccData.Module.DATA_SOURCE, privilegeOperation = PrivilegeOperation.WRITE)
	public ObjectResult<Boolean> update(@RequestBody DataSourceModel model) {
		ObjectResult<Boolean> rsp = new ObjectResult<>();
		if (StringUtils.isAnyBlank(model.getDataSourceKey(), model.getName())) {
			rsp.buildResult(RestCode.PARAM_EMPTY, "dataSourceKey or name");
			return rsp;
		}

		DataSourceModel dbModel = dataSourceService.selectByDataSourceKey(model.getDataSourceKey());
		if (dbModel == null) {
			rsp.buildResult(RestCode.DATA_SOURCE_INACTIVE);
			return rsp;
		}

		model.setDbType(dbModel.getDbType());
		if (!dataSourceService.isDataSourceValid(model)) {
			rsp.buildResult(RestCode.PARAM_INVALID, "连接信息");
			return rsp;
		}

		dataSourceService.update(model);
		return rsp;
	}

	@DeleteMapping("/delete")
	@ModuleAuth(moduleId = MaccData.Module.DATA_SOURCE, privilegeOperation = PrivilegeOperation.WRITE)
	public ObjectResult<Boolean> update(@RequestParam("datasource_key") String dataSourceKey) {
		ObjectResult<Boolean> rsp = new ObjectResult<>();
		dataSourceService.deleteByDataSourceKey(dataSourceKey);
		rsp.setData(true);
		return rsp;
	}

	@GetMapping("/properties/sample")
	@ModuleAuth(moduleId = MaccData.Module.DATA_SOURCE, privilegeOperation = PrivilegeOperation.WRITE)
	public ListResult<DataSourceSample> dataSourcePropertiesSample() {
		ListResult<DataSourceSample> rsp = new ListResult<>();
		List<DataSourceSample> dataList = dataSourceService.getDataSourcePropertiesSample();
		rsp.setList(dataList);
		return rsp;
	}
}
