/*
 * @Author: your name
 * @Date: 2021-08-20 15:03:19
 * @LastEditTime: 2021-11-25 11:07:05
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \poseidon-web\src\bootstrap.js
 */
import { loadRoutes, loadGuards, setAppOptions } from "@/utils/routerUtil";
import { loadInterceptors, request, METHOD } from "@/utils/request";
import { API_USER_INFO } from "@/services/api";
import "@/utils/dateUtil";
import guards from "@/router/guards";
import { getAsyncMenu } from "@/router/async/getAsyncRouter";
import interceptors from "@/utils/axios-interceptors";

/**
 * 启动引导方法
 * 应用启动时需要执行的操作放在这里
 * @param router 应用的路由实例
 * @param store 应用的 vuex.store 实例
 * @param i18n 应用的 vue-i18n 实例
 * @param i18n 应用的 message 实例
 */
async function bootstrap({ router, store, i18n, message }) {
  // 设置应用配置
  setAppOptions({ router, store, i18n });
  // 加载 axios 拦截器
  loadInterceptors(interceptors, { router, store, i18n, message });

  const res = await request(API_USER_INFO, METHOD.GET);
  if (res.code == 0) {
    store.commit("account/setUser", res.data);
  }
  // 加载路由守卫
  loadGuards(guards, { router, store, i18n, message });

  //加载异步路由
  await getAsyncMenu();
  // // 加载路由
  // loadRoutes();
}

export default bootstrap;
