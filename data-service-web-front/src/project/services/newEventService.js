import BaseService from "@/services/baseService";
import { request, METHOD } from "@/utils/request";
import { EVENT_PAGING_QUERY } from "@/services/api";

let Service = Object.assign({}, BaseService, {});

/**
 * @description:获取分页查询列表
 * @param
 * @returns
 */
Service.pagingQuery = function(val) {
  let str = "";
  for (let item in val) {
    str += `${item}=${val[item]}&`;
  }
  let url = `${EVENT_PAGING_QUERY}?${str.slice(0, str.length - 1)}`;
  return request(url, METHOD.GET);
};

export default Service;
