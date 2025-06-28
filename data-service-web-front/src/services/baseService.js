import { request, METHOD } from "@/utils/request";
import {
  API_COM_QRY_LIST,
  API_COM_QRY_MAP,
  API_COM_QRY_OBJ,
  API_COM_QRY_DYNAMIC,
} from "@/services/api";
import Vue from "vue";
/**
 * Service基类，用于抽象公用方法
 */
let BaseService = {
  urlPrefix: "", //子类需要扩展该对象
};

// 返回一个空的PageList
BaseService.emptyList = function() {
  return {
    rows: 0,
    page: 1,
    pagesize: 10,
    data: [],
  };
};

/**
 *操作类通用callback方法，如果操作后返回的code是S000，提示操作成功并返回true
 * @param ret 结果对象{
 *  ok : true | false  是否操作成功
 *  msg: 结果信息
 * }
 @param message: 自定义提示信息
 *  */
BaseService.editCallBack = function(ret, message) {
  let { ok, msg } = ret;
  message = message || msg;
  if (ok) {
    Vue.prototype.$message.success(message);
  } else {
    Vue.prototype.$message.warning(message);
  }
};

/**
 *网络分组api代理
 */
BaseService.groupApiAgent = function(gid, datarow) {
  return request("/service/agent/group/" + gid, METHOD.POST, datarow);
};

/**
 * 查询实体信息，支持分页查询
 * queryData{
    查询字段信息
 * } 
 */
BaseService.query = function(queryData) {
  return request(this.urlPrefix, METHOD.GET, queryData);
};

/**
 * 通用列表查询
 * @param {
 *   modelId: Strng,模块id
 *   filters: Array,过滤条件
 *   orderby: Array,排序规则
 *   skip: int,跳过多少条数据， 配合limit，可用于实现分页
 *   limit: int, 返回的数据行数
 * }
 */
BaseService.queryList = function(queryData) {
  return request(API_COM_QRY_LIST, METHOD.POST, queryData);
};

/**
 * 通用查询单条记录
 * @param{
 * 同queryList
 * }
 */
BaseService.queryMap = function(queryData) {
  return request(API_COM_QRY_MAP, METHOD.POST, queryData);
};

/**
 * 通用查询单个字段
 * @param{
 * 同queryList
 * }
 */
BaseService.queryObject = function(queryData) {
  return request(API_COM_QRY_OBJ, METHOD.POST, queryData);
};

/**
 * 通用动态查询
 * @param{
 * modelId
 * }
 */
BaseService.queryDynamic = function(modelId, params, pageIndex, pageSize) {
  let url = API_COM_QRY_DYNAMIC + `/${modelId}`;
  let body = {
    params,
    pageIndex,
    pageSize,
  };
  return request(url, METHOD.POST, body);
};

/**
 * 新增或者修改信息
 * @param {
    data:  
 * }
 */
BaseService.addOrUpdate = function(data) {
  return request(this.urlPrefix, METHOD.POST, data);
};

/**
 *
 * @param{
 * ids:{
 *  键:值
 *  }
 * }
 *
 */
BaseService.delete = function(ids) {
  ids = typeof ids === "string" || typeof ids === "number" ? [ids] : ids;
  let params = ids;
  for (let p in ids) {
    if (typeof ids[p] === "object") {
      //数组
      params[p] = ids[p].join(",");
    }
  }
  return request(this.urlPrefix, METHOD.DELETE, params);
};

export default BaseService;
