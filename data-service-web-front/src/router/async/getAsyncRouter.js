/*
 * @Author: your name
 * @Date: 2021-10-18 18:11:51
 * @LastEditTime: 2021-11-25 11:05:06
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \baichuan-indicator\src\router\async\getAsyncRouter.js
 */

import EventService from "@p/services/eventService";
//import BlankView from "@/layouts/BlankView";
import baseRouterConfig from "@p/router/config";
import { loadRoutes } from "@/utils/routerUtil";

// 视图组件
const view = {
  tabs: () => import("@/layouts/tabs"),
  blank: () => import("@/layouts/BlankView"),
  page: () => import("@/layouts/PageView"),
};

async function getAsyncMenu() {
  let routerList = [];
  const ret = await EventService.usermenutree();
  ret.list.map((item) => {
    if (!item.subList) {
      routerList.push({
        path: `${item.url}`,
        name: item.name,
        component: () => import(`@p/views${item.url}.vue`),
        meta: {
          icon: item.icon,
        },
      });
    } else {
      let childrenTemp = [];
      item.subList.map((childrenItem) => {
        childrenTemp.push({
          path: `${childrenItem.url}`,
          name: childrenItem.name,
          component: () => import(`@p/views${childrenItem.url}.vue`),
          meta: {
            icon: childrenItem.icon,
          },
        });
      });
      routerList.push({
        path: `${item.url}`,
        name: item.name,
        component: view.blank,
        meta: {
          icon: item.icon,
        },
        children: childrenTemp,
      });
    }
  });
  console.log(ret.list, "请求返回的原始数据");

  baseRouterConfig.routes[1].redirect =
    (routerList[0] &&
      routerList[0].children &&
      routerList[0].children[0] &&
      routerList[0].children[0].path) ||
    "";
  console.log(routerList, "routerList", baseRouterConfig);
  baseRouterConfig.routes[1].children = baseRouterConfig.routes[1].children.concat(
    routerList
  );
  console.log("baseRouterConfig", baseRouterConfig);
  loadRoutes(baseRouterConfig.routes);
}

export { getAsyncMenu };
