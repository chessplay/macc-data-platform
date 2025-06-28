/**
 * 事件
 */
import {
  SSOLOGOUT_API,
  USERMENUTREE_API,
  MENUDELETE_API,
  MENUUPDATE_API,
  ACCOUNTDELETE_API,
  ACCOUNTUPDATE_API,
  ACCOUNTPAGE_API,
  MODULEDELETE_API,
  MENUADD_API,
  MODULEINFO_API,
  MODULELIST_API,
  ROLEMODULELIST_API,
  ROLEMENUDELETE_API,
  ROLEMENUADD_API,
  MENUTREE_API,
  ROLEMENULIST_API,
  ROLEINFO_API,
  ROLEDELETE_API,
  ROLELIST_API,
  API_EVENT,
  API_ModuleList,
  API_ModuleAdd,
} from "@/services/api";
import BaseService from "@/services/baseService";
import { request, METHOD } from "@/utils/request";

let Service = Object.assign({}, BaseService, { urlPrefix: API_EVENT });

//新增模块
Service.addModule = function(moduleid, modulename, description) {
  let url = API_ModuleAdd;
  let body = {
    moduleId: moduleid,
    moduleName: modulename,
    description: description,
  };
  return request(url, METHOD.POST, body);
};
//查看角色
Service.rolelist = function() {
  let url = ROLELIST_API;
  return request(url, METHOD.GET);
};
//删除角色
Service.roledelete = function(params) {
  let url = ROLEDELETE_API + `?role_id=${params}`;
  return request(url, METHOD.DELETE);
};
//新增或修改角色
Service.postroleinfo = function(id, name, information) {
  let url = ROLEINFO_API;
  let body = {
    roleId: id,
    roleName: name,
    description: information,
  };
  return request(url, METHOD.POST, body);
};
//查看角色权限
Service.rolemenulist = function(id) {
  let url = ROLEMENULIST_API + `?role_id=${id}`;
  return request(url, METHOD.GET);
};
//查看菜单树
Service.menutree = function() {
  let url = MENUTREE_API;
  return request(url, METHOD.GET);
};
//添加角色菜单
Service.rolemenuadd = function(roleid, menuid) {
  let url = ROLEMENUADD_API;
  let body = {
    roleId: roleid,
    menuId: menuid,
  };
  return request(url, METHOD.POST, body);
};
//删除角色菜单
Service.rolemenudelete = function(roleid, menuid) {
  let url = ROLEMENUDELETE_API;
  let body = {
    menu_id: menuid,
    role_id: roleid,
  };
  return request(url, METHOD.DELETE, body);
};
//查看角色模块权限
Service.rolemodulelist = function(id) {
  let url = ROLEMODULELIST_API + `?role_id=${id}`;
  return request(url, METHOD.GET);
};
//查看角色模块
Service.modulelist = function() {
  let url = MODULELIST_API;
  return request(url, METHOD.GET);
};
//修改模块权限
Service.moduleinfo = function(roleid, moduleid, privilege) {
  let url = MODULEINFO_API;
  let body = {
    roleId: roleid,
    moduleId: moduleid,
    privilege: privilege,
  };
  return request(url, METHOD.POST, body);
};
//新增菜单
Service.menuadd = function(
  menuid,
  menuname,
  menuurl,
  menuicon,
  menuserial,
  menuparameter
) {
  let url = MENUADD_API + `/${menuid}`;
  let body = {
    name: menuname,
    url: menuurl,
    icon: menuicon,
    priority: menuserial,
    params: menuparameter,
  };
  return request(url, METHOD.POST, body);
};
//删除模块
Service.moduledelete = function(menuid) {
  let url = MODULEDELETE_API;
  let body = {
    module_id: menuid,
  };
  return request(url, METHOD.DELETE, body);
};
//分页查看账号列表
Service.accountpage = function(
  page_index,
  page_size,
  user_name,
  state,
  role_id
) {
  let url = ACCOUNTPAGE_API;
  let body = {
    page_index,
    page_size,
    user_name,
    state,
    role_id,
  };
  return request(url, METHOD.GET, body);
};
//修改账号信息
Service.accountupdata = function(accountid, accountname, roleid, accountstate) {
  let url = ACCOUNTUPDATE_API;
  let body = {
    userId: accountid,
    userName: accountname,
    roleId: roleid,
    state: accountstate,
  };
  return request(url, METHOD.POST, body);
};
//删除账号信息
Service.accountdelete = function(accountid) {
  let url = ACCOUNTDELETE_API;
  let body = {
    user_id: accountid,
  };
  return request(url, METHOD.DELETE, body);
};
//更新菜单
Service.menuupdata = function(
  menuid,
  menuname,
  menuurl,
  menuicon,
  menuserial,
  menuparameter
) {
  let url = MENUUPDATE_API;
  let body = {
    id: menuid,
    name: menuname,
    url: menuurl,
    icon: menuicon,
    priority: menuserial,
    params: menuparameter,
  };
  return request(url, METHOD.PUT, body);
};
//删除菜单
Service.menudelete = function(menuid) {
  let url = MENUDELETE_API;
  let body = {
    id: menuid,
  };
  return request(url, METHOD.DELETE, body);
};
//查看角色菜单树
Service.usermenutree = function() {
  let url = USERMENUTREE_API;
  return request(url, METHOD.GET);
};
//登录和退出
Service.ssologout = function() {
  let url = SSOLOGOUT_API;
  return request(url, METHOD.GET);
};

export default Service;
