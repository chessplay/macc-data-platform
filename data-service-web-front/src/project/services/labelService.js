/**
 * 标签管理接口
 */
import BaseService from "@/services/baseService";
import { request, METHOD } from "@/utils/request";
import {
  LABEL_TYPE_TREE_GET,
  LABEL_TYPE_TREE_POST,
  LABEL_TYPE_TREE_DELETE,
  LABEL_MODEL_LIST_GET,
  LABEL_MODEL_ADD_POST,
  LABEL_MODEL_VALUETYPE_GET,
  LABEL_MODEL_CLASSIFY_GET,
  LABEL_MODEL_TYPE_GET,
  LABEL_MODEL_GRANULARITY_GET,
  LABEL_MODEL_MODEL_DELETE,
  LABEL_MODEL_UPDATA_POST,
  LABEL_MODEL_INFO_GET,
} from "@/services/api";

let Service = Object.assign({}, BaseService, {});

/**
 * @description:获取标签类型树
 * @param
 * @returns
 */
Service.getLabelTree = () => {
  const url = LABEL_TYPE_TREE_GET;
  return request(url, METHOD.GET);
};

/**
 * @description:添加标签类型
 * @param
 * @returns
 */
Service.addLabelType = (data) => {
  const url = LABEL_TYPE_TREE_POST;
  const body = data;
  return request(url, METHOD.POST, body);
};

/**
 * @description:删除标签类型
 * @param
 * @returns
 */
Service.deleteLabelType = (data) => {
  const url = `${LABEL_TYPE_TREE_DELETE}/${data}`;
  return request(url, METHOD.DELETE);
};

/**
 * @description:分页查询标签模型
 * @param
 * @returns
 */
Service.getLabelModelList = (data) => {
  // const {
  //   page_index,
  //   page_size,
  //   label_id,
  //   data_granularity,
  //   label_type,
  //   keyword,
  // } = data
  let url = `${LABEL_MODEL_LIST_GET}?`;
  let key = Object.keys(data);
  for (let i of key) {
    if (data[i]) {
      url += `${i}=${data[i]}&`;
    }
  }
  // const url = `${LABEL_MODEL_LIST_GET}?page_index=${page_index}&page_size=${page_size}&label_id=${label_id}&data_granularity=${data_granularity}&label_type=${label_type}&keyword=${keyword}`
  return request(url, METHOD.GET);
};

/**
 * @description:新增标签模型
 * @param
 * @returns
 */
Service.addLabelModel = (data) => {
  const url = `${LABEL_MODEL_ADD_POST}`;
  const body = data;
  return request(url, METHOD.POST, body);
};

/**
 * @description:更新标签模型
 * @param
 * @returns
 */
Service.updataLabelModel = (id, data) => {
  const url = `${LABEL_MODEL_UPDATA_POST}/${id}`;
  const body = data;
  return request(url, METHOD.POST, body);
};
/**
 * @description:标签值类型列表
 * @param
 * @returns
 */
Service.getValueType = () => {
  const url = `${LABEL_MODEL_VALUETYPE_GET}`;
  return request(url, METHOD.GET);
};

/**
 * @description:标签数据分类
 * @param
 * @returns
 */
Service.getClassIfy = () => {
  const url = `${LABEL_MODEL_CLASSIFY_GET}`;
  return request(url, METHOD.GET);
};

/**
 * @description:标签数据分类
 * @param
 * @returns
 */
Service.getType = () => {
  const url = `${LABEL_MODEL_TYPE_GET}`;
  return request(url, METHOD.GET);
};

/**
 * @description:标签数据粒度
 * @param
 * @returns
 */
Service.getGranularity = () => {
  const url = `${LABEL_MODEL_GRANULARITY_GET}`;
  return request(url, METHOD.GET);
};

/**
 * @description:删除标签模型
 * @param
 * @returns
 */
Service.deleteLabelModel = (data) => {
  const url = `${LABEL_MODEL_MODEL_DELETE}/${data}`;
  return request(url, METHOD.DELETE);
};

/**
 * @description:查询指定标签模型
 * @param
 * @returns
 */
Service.getLabelModelInfo = (data) => {
  const url = `${LABEL_MODEL_INFO_GET}/${data}`;
  return request(url, METHOD.GET);
};

export default Service;
