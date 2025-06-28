/*
 * @Author: lilinlin
 * @Date: 2021-12-17 11:19:52
 * @LastEditTime: 2021-12-29 16:03:21
 * @LastEditors: Please set LastEditors
 * @Description: 拓扑serveice
 * @FilePath: \baichuan-web\src\project\components\topology\services\index.js
 */
import {
  GET_DEVICE_LINECARDSWITCH,
  POST_DEVICE_PORT_INFO,
  GET_TOPOLOGY_TREEDATA,
  GET_DEVICE_PANELPOERTS,
  GET_TERMINAL_CUSTOM,
  TOPO_TRIGGER_API,
  GET_TOPO_GENERATION_RECORD,
  GET_DEVICE_NETWORK_LIST,
} from "@/services/api";
import BaseService from "@/services/baseService";
import store from "../store/devices";
let Service = Object.assign({}, BaseService, {});
/**
 * @description:获取设备line_card开关
 * @param {*} group_id
 * @return {*}
 */
Service.getDeviceLinecardswitch = function(group_id) {
  // 判断有没有传group_id
  if (!group_id) {
    group_id = store.state.group_id;
  }
  let datarow = {
    url: `/service/api${GET_DEVICE_LINECARDSWITCH}`,
    httpMethod: "GET",
    queryParams: {},
  };
  return BaseService.groupApiAgent(group_id, datarow);
};

/**
 * @description:获取设备端口信息 问题：传的参数是个list数组，接口需要的是个group_id
 * @param group_id
 * @return {*}
 */
Service.getDevicePortInfo = function(group_id) {
  // 判断有没有传group_id
  group_id = store.state.group_id;
  let datarow = {
    url: `/service/api${POST_DEVICE_PORT_INFO}`,
    httpMethod: "GET",
    queryParams: {
      group_id,
    },
  };
  return BaseService.groupApiAgent(group_id, datarow);
};

/**
 * @description:获取拓扑数据
 * @param {boolean} [excludeap=false] group_id
 * @returns
 */
Service.getTopoData = function(with_wired_terminal = false, group_id) {
  let params = {};

  if (with_wired_terminal) {
    params.with_wired_terminal = with_wired_terminal;
  }
  // 判断有没有传group_id
  if (!group_id) {
    group_id = store.state.group_id;
  }
  let datarow = {
    url: `/service/api${GET_TOPOLOGY_TREEDATA}/${group_id}`,
    httpMethod: "GET",
    queryParams: params,
  };
  return BaseService.groupApiAgent(group_id, datarow);
};

/**
 * @description:获取多个序列号的端口信息状态
 * @param sns
 * @returns
 */
Service.getTopoDevicePanelPortsData = function(sns) {
  // let sn = [];
  // sn.push(sns);
  let datarow = {
    url: `/service/api${GET_DEVICE_PANELPOERTS}`,
    httpMethod: "POST",
    requestBody: {
      snList: sns,
    },
  };
  return BaseService.groupApiAgent(store.state.group_id, datarow);
};

/**
 * @description:分类获取所有可标注设备类型
 * @param params
 * @returns
 */
Service.getDevicePanelpoerts = function(params = {}) {
  let url = `/service/api${GET_TERMINAL_CUSTOM}`;
  const { source_type, device_class } = params;
  let datarow = {
    url,
    httpMethod: "GET",
    queryParams: { source_type, device_class },
  };
  return BaseService.groupApiAgent(store.state.group_id, datarow);
};

/**
 * @description:触发拓扑更新状态
 * @param group_id
 * @returns
 */
Service.getTopoTrigger = function(group_id) {
  // 判断有没有传group_id
  if (!group_id) {
    group_id = store.state.group_id;
  }
  let datarow = {
    url: `/service/api${TOPO_TRIGGER_API}/${group_id}`,
    httpMethod: "GET",
    queryParams: {},
  };
  return BaseService.groupApiAgent(group_id, datarow);
};

/**
 * @description:获取拓扑生成记录
 * @param group_id
 * @returns
 */
Service.getTopoRecord = function(group_id) {
  // 判断有没有传group_id
  if (!group_id) {
    group_id = store.state.group_id;
  }
  let datarow = {
    url: `/service/api${GET_TOPO_GENERATION_RECORD}/${group_id}`,
    httpMethod: "GET",
    queryParams: {},
  };
  return BaseService.groupApiAgent(group_id, datarow);
};

/**
 * @description:网络设备列表查询
 * @param params对象包含参数 => group_id (必选) || online_status || common_type(通用类型， AP, SWITCH, AC, GATEWAY) || search_key(查询关键字， 别名、sn、备注、mac 四个字段模糊查询)
 * 传参样式：let params = { group_id: 70807, common_type: 'GATEWAY' }
 * @returns
 */
Service.getDeviceNetworkList = function(params) {
  const { group_id, online_status, common_type, search_key } = params;
  // 判断有没有传group_id
  if (!group_id) {
    group_id = store.state.group_id;
  }
  let datarow = {
    url: `/service/api${GET_DEVICE_NETWORK_LIST}`,
    httpMethod: "GET",
    queryParams: { group_id, online_status, common_type, search_key },
  };
  return BaseService.groupApiAgent(group_id, datarow);
};

export default Service;
