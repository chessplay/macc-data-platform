package com.ruijie.cloud.macc.dataplatform.metadata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruijie.cloud.dc.core.response.ListResult;
import com.ruijie.cloud.dc.core.response.ObjectResult;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.RestCode;
import com.ruijie.cloud.macc.dataplatform.metadata.service.DataSourceService;
import com.ruijie.cloud.macc.dataplatform.metadata.service.TableMetaDataService;

@RestController
@RequestMapping("/metadata/table")
public class TableMetaDataController {

	@Autowired
	private TableMetaDataService tableMetaDataService;
	@Autowired
	private DataSourceService dataSourceService;

	@GetMapping("/name/list")
	public ListResult<String> tableNameList(@RequestParam("datasource_key") String dataSourceKey,
			@RequestParam(value = "disable_cache", required = false, defaultValue = "false") boolean disableCache) {
		ListResult<String> rsp = new ListResult<>();
		if (!dataSourceService.isDataSourceActive(dataSourceKey)) {
			rsp.buildResult(RestCode.DATA_SOURCE_INACTIVE);
			return rsp;
		}

		List<String> tableList = tableMetaDataService.tableNameList(dataSourceKey, disableCache);
		rsp.setList(tableList);
		return rsp;
	}

	@GetMapping("/detail")
	public ObjectResult<TableInfo> tableNameDetail(@RequestParam("datasource_key") String dataSourceKey,
			@RequestParam("table_name") String tableName,
			@RequestParam(value = "disable_cache", required = false, defaultValue = "false") boolean disableCache) {
		ObjectResult<TableInfo> rsp = new ObjectResult<>();
		if (!dataSourceService.isDataSourceActive(dataSourceKey)) {
			rsp.buildResult(RestCode.DATA_SOURCE_INACTIVE);
			return rsp;
		}

		TableInfo tableInfo = tableMetaDataService.tableInfo(dataSourceKey, tableName, disableCache);
		rsp.setData(tableInfo);
		return rsp;
	}

}
