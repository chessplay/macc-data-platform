/*
 * @Author: your name
 * @Date: 2021-08-20 15:03:18
 * @LastEditTime: 2021-09-26 12:29:01
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \poseidon-web\src\services\user.js
 */
import { LOGIN, ROUTES, SSOLOGOUT_API } from "@/services/api";
import { request, METHOD, removeAuthorization } from "@/utils/request";

/**
 * 登录服务
 * @param name 账户名
 * @param password 账户密码
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function login(name, password) {
  return request(LOGIN, METHOD.POST, {
    name: name,
    password: password,
  });
}

export async function getRoutesConfig() {
  return request(ROUTES, METHOD.GET);
}

/**
 * 退出登录
 */
export function logout() {
  // localStorage.removeItem(process.env.VUE_APP_ROUTES_KEY)
  // localStorage.removeItem(process.env.VUE_APP_PERMISSIONS_KEY)
  // localStorage.removeItem(process.env.VUE_APP_ROLES_KEY)
  // removeAuthorization()
  return request(SSOLOGOUT_API, METHOD.GET);
}
export default {
  login,
  logout,
  getRoutesConfig,
};
