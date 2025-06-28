/**
 * 数据库管理接口
 */
import BaseService from "@/services/baseService";
import { request, METHOD } from "@/utils/request";
import {
  GET_DB_LIST,
  TEST_DATA,
  DELETE_DATASOURCE_INFO,
  GET_SUPPORT_DB_TYPE,
  ADD_DATASOURCE,
  GET_DATASOURCE_INFO,
  GET_QUERYMODEL_INFO,
  GEY_QUERYMODEL_PAGING,
  ADD_QUERYMODEL_INFO,
  DELETE_QUERYMODEL_INFO,
  GET_SUPPORT_QUERYTYPE,
  GET_SUPPORT_PRARAMTYPE,
  DYNAMIC_EXECUTE_TEST,
  DYNAMIC_EXECUTE_QUERY,
  PRIVILEGE_MODULE_LIST
} from "@/services/api";

let Service = Object.assign({}, BaseService, {});

/**
 * @description:获取数据库列表
 * @param
 * @returns
 */
Service.getDBList = function() {
  let url = GET_DB_LIST;
  return request(url, METHOD.GET);
};
/**
 * @description:测试数据库数据
 * @param
 * @returns
 */
Service.testData = function(value) {
  let url = TEST_DATA;
  let body = value;
  return request(url, METHOD.POST, body);
};

/**
 * @description:删除数据源
 * @param
 * @returns
 */
Service.deleteDatasource = function(id) {
  let url = `${DELETE_DATASOURCE_INFO}/${id}`;
  return request(url, METHOD.DELETE);
};
/**
 * @description:获取支持的数据库类型
 * @param
 * @returns
 */
Service.getDatabaseType = function() {
  let url = GET_SUPPORT_DB_TYPE;
  return request(url, METHOD.GET);
};

/**
 * @description:添加/更新数据源
 * @param
 * @returns
 */
Service.addDatasource = function(value) {
  let url = ADD_DATASOURCE;
  let body = value;
  return request(url, METHOD.POST, body);
};

/**
 * @description:按id查询指定数据源
 * @param
 * @returns
 */
Service.getDatasourceInfo = function(id) {
  let url = `${GET_DATASOURCE_INFO}/${id}`;
  return request(url, METHOD.GET);
};
// ***********************************数据模型************************
/**
 * @description:用模型id查询指定数据模型
 * @param
 * @returns
 */
Service.getQuerymodelInfo = function(id) {
  let url = `${GET_QUERYMODEL_INFO}?model_id=${id}`;
  return request(url, METHOD.GET);
};
/**
 * @description:数据模型分页查询
 * @param
 * @returns
 */
Service.getQuerymodelPaging = function({ page_index, page_size, keyword }) {
  let temp = "";
  if (keyword) {
    temp = `page_index=${page_index}&page_size=${page_size}&keyword=${keyword}`;
  } else {
    temp = `page_index=${page_index}&page_size=${page_size}`;
  }
  let url = `${GEY_QUERYMODEL_PAGING}?${temp}`;
  return request(url, METHOD.GET);
};
/**
 * @description:添加数据模型
 * @param
 * @returns
 */
Service.addQuerymodelInfo = function(value) {
  let url = `${ADD_QUERYMODEL_INFO}`;
  let body = value;
  return request(url, METHOD.POST, body);
};
/**
 * @description:删除数据模型(可多个,多个以,分隔)
 * @param
 * @returns
 */
Service.deleteQuerymodelInfo = function(modelIds) {
  let temp = modelIds.toString();
  let url = `${DELETE_QUERYMODEL_INFO}/${temp}`;
  return request(url, METHOD.DELETE);
};
/**
 * @description:支持的查询类型
 * @param
 * @returns
 */
Service.getSupportQuerytype = function() {
  let url = `${GET_SUPPORT_QUERYTYPE}`;
  return request(url, METHOD.GET);
};

/**
 * @description:支持的参数类型
 * @param
 * @returns
 */
Service.getSupportPraramType = function() {
  let url = `${GET_SUPPORT_PRARAMTYPE}`;
  return request(url, METHOD.GET);
};

/**
 * @description:获取所属模块id
 * @param
 * @returns
 */
Service.privilegeModuleList = function() {
  let url = `${PRIVILEGE_MODULE_LIST}`;
  return request(url, METHOD.GET);
};

/**
 * @description:测试运行数据模型
 * @param
 * @returns
 */
Service.dynamicExecuteTest = function(val) {
  let url = `${DYNAMIC_EXECUTE_TEST}`;
  let body = val;
  return request(url, METHOD.POST, body);
};
/**
 * @description:执行动态查询
 * @param
 * @returns
 */
Service.dynamicExecuteQuery = function(id, val) {
  let url = `${DYNAMIC_EXECUTE_QUERY}/${id}`;
  let body = val;
  return request(url, METHOD.POST, body);
};

export default Service;
