/*
 * @Author: your name
 * @Date: 2021-10-13 13:42:57
 * @LastEditTime: 2021-10-19 16:55:27
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \baichuan-indicator\src\project\router\config.js
 */

const view = {
  tabs: () => import("@/layouts/tabs"),
  blank: () => import("@/layouts/BlankView"),
  page: () => import("@/layouts/PageView"),
};

// 路由配置
const options = {
  routes: [
    {
      path: "/login",
      name: "登录页",
      component: () => import("@/pages/login"),
    },
    {
      path: "/",
      name: "首页",
      component: view.tabs,
      redirect: "/query-project/query",
      children: [
        {
          path: "exception",
          name: "异常页",
          meta: {
            icon: "warning",
            invisible: true,
          },
          component: view.blank,
          children: [
            {
              path: "404",
              name: "Exp404",
              component: () => import("@/pages/exception/404"),
            },
            {
              path: "403",
              name: "Exp403",
              component: () => import("@/pages/exception/403"),
            },
            {
              path: "500",
              name: "Exp500",
              component: () => import("@/pages/exception/500"),
            },
          ],
        },
        // {
        //   path: "dashboard",
        //   name: "数据报表",
        //   meta: {
        //     icon: "line-chart",
        //   },
        //   component: view.blank,
        //   children: [
        //     {
        //       path: "/dashboard/workplace",
        //       name: "工作台",
        //       meta: {
        //         page: {
        //           closable: false,
        //         },
        //       },
        //       component: () => import("@p/views/dashboard/home.vue"),
        //     },
        //   ],
        // },
      ],
    },
  ],
};

export default options;
