/*
 * @Author: your name
 * @Date: 2021-08-27 09:50:13
 * @LastEditTime: 2024-12-25 15:40:53
 * @LastEditors: 'chenmengni'
 * @Description: In User Settings Edit
 * @FilePath: \data-service-web\src\services\api.js
 */
//跨域代理前缀
// const API_PROXY_PREFIX='/datacenter'
//const BASE_URL = process.env.NODE_ENV === 'production' ? process.env.VUE_APP_API_BASE_URL : API_PROXY_PREFIX
const BASE_URL = process.env.VUE_APP_API_BASE_URL;
module.exports = {
  BASE_URL,
  API_USER_INFO: "/user/info",
  BASICINFO_API: "/product",
  SPEINFO_API: "/product/data",
  ALLTYPES_API: "/product/type",
  SPEINFODEMO_API: "/product/data/publish",
  BASICINFODEMO_API: "/product/publish",
  SPEINFOHIST_API: "/product/data/history",
  INFOTYPE_API: "/product-type/data/type",
  VERSIONNUM_API: "/product/data/version",
  SPETYPEDESC_API: "/data/type",
  BASINFOHIST_API: "/product/history",
  TELEVISION_API: "/product/type",
  DESC_API: "/data/type",
  VERSIONDATA_API: "/product/version",
  EXIST_API: "/product/exist",

  API_EVENT: "/metadata/zhugeio/event",
  API_PROP: "/metadata/zhugeio/column",
  API_TYPE: "/metadata/zhugeio/event/type",
  //通用查询接口
  API_COM_QRY_LIST: "/query/list",
  //通用查询单条记录
  API_COM_QRY_MAP: "/query/map",
  //查询单个字段
  API_COM_QRY_OBJ: "/query/object",
  //通用执行动态查询
  API_COM_QRY_DYNAMIC: "/dynamic/execute/query",
  // LOGIN: `${BASE_URL}/login`,
  // ROUTES: `${BASE_URL}/routes`,
  // GOODS: `${BASE_URL}/goods`,
  // GOODS_COLUMNS: `${BASE_URL}/columns`,

  //===========================通用权限模块================================
  //查看角色
  ROLELIST_API: "/privilege/role/list",
  //删除角色
  ROLEDELETE_API: "/privilege/role/delete",
  //新增或修改角色
  ROLEINFO_API: "/privilege/role/info",
  //查看角色菜单权限
  ROLEMENULIST_API: "/privilege/setting/role/menu/list",
  //查看菜单树：
  MENUTREE_API: "/privilege/menu/tree",
  //添加角色菜单
  ROLEMENUADD_API: "/privilege/setting/role/menu/add",
  //删除角色菜单
  ROLEMENUDELETE_API: "/privilege/setting/role/menu/delete",
  //查看角色模块权限
  ROLEMODULELIST_API: "/privilege/setting/role/module/list",
  //查看模块
  MODULELIST_API: "/privilege/module/list",
  //修改模块权限
  MODULEINFO_API: "/privilege/setting/role/module/info",
  //新增菜单
  MENUADD_API: "/privilege/menu/add",
  //删除模块
  MODULEDELETE_API: "/privilege/module/delete",
  //分页查看账号列表
  ACCOUNTPAGE_API: "/privilege/account/page",

  //修改账号信息
  ACCOUNTUPDATE_API: "/privilege/account/update",
  //删除账号信息
  ACCOUNTDELETE_API: "/privilege/account/delete",
  //更新菜单
  MENUUPDATE_API: "/privilege/menu/update",
  //删除菜单
  MENUDELETE_API: "/privilege/menu/delete",
  //获取用户菜单树
  USERMENUTREE_API: "/user/menu/tree",
  //登录和退出
  SSOLOGOUT_API: "/sso/logout",

  // 设备拓扑模块====================================
  GET_DEVICE_LINECARDSWITCH: "/switch/line_card", //获取设备line_card开关
  POST_DEVICE_PORT_INFO: "/device/network/port_info", //获取设备端口信息
  GET_TOPOLOGY_TREEDATA: "/topology/info", //获取拓扑数据
  GET_DEVICE_PANELPOERTS: "/maint/devices/port", //获取多个序列号的端口信息状态
  GET_TERMINAL_CUSTOM: "/itmgt/port_link_type_manager/types_enum", //分类获取所有可标注设备类型
  TOPO_TRIGGER_API: "/topology/trigger", //触发拓扑更新状态
  GET_TOPO_GENERATION_RECORD: "/topology/generation/record", //获取拓扑生成记录
  GET_DEVICE_NETWORK_LIST: "/device/network/list", //按网络id查询设备列表

  // 数据库操作模块===============================
  GET_DB_LIST: "/manage/datasource/list", //查询数据源列表
  TEST_DATA: "/manage/datasource/connection/test", //测试能否连接数据库
  DELETE_DATASOURCE_INFO: "/manage/datasource/info", //删除数据源
  GET_DATASOURCE_INFO: "/manage/datasource/info", //按id查询指定数据源
  GET_SUPPORT_DB_TYPE: "/manage/datasource/support/databases",
  ADD_DATASOURCE: "/manage/datasource/info", //添加/更新数据源

  GET_QUERYMODEL_INFO: "/manage/querymodel/info", //用模型id查询指定数据模型
  GEY_QUERYMODEL_PAGING: "/manage/querymodel/paging", //数据模型分页查询
  ADD_QUERYMODEL_INFO: "/manage/querymodel/info", //添加数据模型
  DELETE_QUERYMODEL_INFO: "/manage/querymodel/info", //删除数据模型
  GET_SUPPORT_QUERYTYPE: "/manage/querymodel/support/querytype", //支持的查询类型
  GET_SUPPORT_PRARAMTYPE: "/manage/querymodel/support/praramtype", //支持的参数类型
  DYNAMIC_EXECUTE_TEST: "/dynamic/execute/test", //测试运行数据模型
  PRIVILEGE_MODULE_LIST: "privilege/module/list", //获取所属模块id

  //  标签系统
  LABEL_MODEL_LIST_GET: "/label/model/paging", //分页查询标签模型
  LABEL_MODEL_ADD_POST: "/label/model/info", //新增标签模型
  LABEL_MODEL_UPDATA_POST: "/label/model/info", //更新标签模型
  LABEL_MODEL_INFO_GET: "/label/model/info", //查询指定标签模型
  LABEL_MODEL_VALUETYPE_GET: "/label/metadata/valuetype", //标签值类型列表
  LABEL_MODEL_CLASSIFY_GET: "/label/metadata/classify", //标签数据分类
  LABEL_MODEL_TYPE_GET: "/label/metadata/type", //标签数据类别
  LABEL_MODEL_GRANULARITY_GET: "/label/metadata/granularity", //标签值类型列表
  LABEL_MODEL_MODEL_DELETE: "/label/model/info", //删除标签模型
  LABEL_TYPE_TREE_GET: "/label/business/type/tree", //查询标签类型树
  LABEL_TYPE_TREE_POST: "/label/business/type/info", //新增标签类型
  LABEL_TYPE_TREE_DELETE: "/label/business/type/info/", //删除标签类型

  // // 客户经营
  // MANAGEMENT_LIST_QUERY_FIRST: '', // 查询一级渠道公司信息

  // 转化事件
  EVENT_PAGING_QUERY: "/metadata/zhugeio/event/paging", //转化事件（分页查询）

  // 数据管理
  METADATE_DATASOURCE_LIST_GET: "/metadata/datasource/list", //数据源列表获取
  METADATE_DATASOURCE_ADD_POST: "/metadata/datasource/add", //数据源-新增
  METADATE_DATASOURCE_UPDATE_POST: "/metadata/datasource/update", //数据源-编辑
  METADATE_DATASOURCE_DELETE_DELETE: "/metadata/datasource/delete", //数据源-删除
  METADATE_DATASOURCE_SAMPLE_GET: "/metadata/datasource/properties/sample", //数据源-模板

  //  任务模板列表
  METADATE_DATASOURCE_NAME_LIST_GET: "/metadata/datasource/name/list", //数据源列表获取-简洁
  METADATE_TASK_TEMPLATE_GET: "/task/template/list", //任务模板列表获取
  METADATE_TASK_TEMPLATE_DELETE_DELETE: "/task/template", //模板删除
  METADATE_TASK_TEMPLATE_ONE_GET: "/task/template", //单个模板获取
  METADATE_TASK_TEMPLATE_ONE_POST: "/task/template", //单个模板设置
  METADATE_TASK_TEMPLATE_ONE_PUT: "/task/template", //单个模板更新

  METADATE_TABLE_NAME_GET: "/metadata/table/name/list", //查询表数据
  METADATE_TABLE_DETAIL_GET: "/metadata/table/detail", //查询指定表下字段数据
  QUERY_CONSTRAINT_TYPE_GET: "/query/constraint/types", //字段类型查询约束条件
  TASK_TEMPLATE_FETCH_POST: "/task/template/fetch-request-example", //获取请求参数示例

  //  任务列表
  METADATE_TASK_LIST_GET: "/task/instance/page", //任务列表获取
  METADATE_TASK_START_POST: "/task/instance/start", //任务-启动
  METADATE_TASK_STOP_POST: "/task/instance/stop", //任务-停止
  METADATE_TASK_DELETE_DELETE: "/task/instance/delete", //任务-删除
  METADATE_TASK_RERUN_POST: "/task/instance/rerun", //任务-重跑
  METADATE_TASK_PAUSE_POST: "/task/instance/pause", //任务-暂停
  METADATE_TASK_LOG_GET: "/task/instance/log", //任务-查看日志
  
  METADATE_TASK_POST: "/task", //任务-新增

};
