/**
 * 标签管理接口
 */
import BaseService from '@/services/baseService'
import { request, METHOD } from '@/utils/request'
import { API_COM_QRY_DYNAMIC } from '@/services/api'

let Service = Object.assign({}, BaseService, {})
/**
 * 通用动态查询
 * @param{
 * modelId
 * }
 */
Service.managementDynamic = (modelId, params) => {
  const url = API_COM_QRY_DYNAMIC + `/${modelId}`
  const body = {
    ...params,
  }
  return request(url, METHOD.POST, body)
}

export default Service
