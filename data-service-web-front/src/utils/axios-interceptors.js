/*
 * @Author: your name
 * @Date: 2021-10-15 17:15:03
 * @LastEditTime: 2021-11-25 10:59:09
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \baichuan-web\src\utils\axios-interceptors.js
 */
import Cookie from "js-cookie";
import SysConfig from "./sysConfig";

// 401拦截
const resp401 = {
  /**
   * 响应出错时执行
   * @param error 错误对象
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {Promise<never>}
   */
  onRejected(error, options) {
    const { message } = options;
    const { response } = error;
    if (response.status === 401) {
      message.error("无此权限");
    }
    return Promise.reject(error);
  },
};

const resp403 = {
  onRejected(error, options) {
    const { message } = options;
    const { response } = error;
    if (response.status === 403) {
      message.error("请求被拒绝");
    }
    return Promise.reject(error);
  },
};

//通用解析返回数据
const respCommon = {
  onFulfilled(response, options) {
    const { message } = options;
    let ret = response.data || {};
    ret.ok = ret.code === SysConfig.SuccessCode; // 返回操作是否成功的判断

    //拦截未登录状态
    if (ret.code == 11001) {
      const backUrl = window.location.origin + window.location.pathname;
      window.location = `${window.location.origin}${process.env.VUE_APP_API_BASE_URL}/sso/login?back=${backUrl}`;
      //清除之前得tab缓存
      sessionStorage.removeItem(process.env.VUE_APP_TBAS_KEY);
      return;
    }

    //拦截系统错误状态
    if (SysConfig.errorMap[ret.code]) {
      message.error(SysConfig.errorMap[ret.code]);
      return;
    }

    return ret;
  },
};

const reqCommon = {
  /**
   * 发送请求之前做些什么
   * @param config axios config
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {*}
   */
  onFulfilled(config, options) {
    const { message } = options;
    const { url, xsrfCookieName } = config;
    if (
      url.indexOf("login") === -1 &&
      xsrfCookieName &&
      !Cookie.get(xsrfCookieName)
    ) {
      // message.warning('认证 token 已过期，请重新登录')
    }
    return config;
  },
  /**
   * 请求出错时做点什么
   * @param error 错误对象
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {Promise<never>}
   */
  onRejected(error, options) {
    const { message } = options;
    message.error(error.message);
    return Promise.reject(error);
  },
};

export default {
  request: [reqCommon], // 请求拦截
  response: [resp401, resp403, respCommon], // 响应拦截
};
