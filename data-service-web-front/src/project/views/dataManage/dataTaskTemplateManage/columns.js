/*
 * @Author: 'chenmengni'
 * @Date: 2024-12-04 10:46:46
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2024-12-17 16:27:30
 * @FilePath: \data-service-web\src\project\views\dataManage\dataTaskTemplateManage\columns.js
 * @Description:数据源-表格columns
 */

// import i18n from '@/locales';
const columns = [
  {
    title: "任务模板名称",
    dataIndex: "name",
    key: "name",
    showColums:true,
    // scopedSlots: { customRender: "showTemplate" },
  },
  {
    title: "源数据源键",
    dataIndex: "sourceDataKey",
    key: "sourceDataKey",
    showColums:true,
    width: 150,
  },
  {
    title: "源数据源表",
    dataIndex: "sourceTableName",
    key: "sourceTableName",
    showColums:true,
  },
  {
    title: "目标数据源键",
    dataIndex: "sinkDataKey",
    key: "sinkDataKey",
    showColums:true,
    width: 150,
  },
  {
    title: "目标数据源表",
    dataIndex: "sinkTableName",
    key: "sinkTableName",
    showColums:true,
  },
  {
    title: "最后更新者",
    dataIndex: "lastUpdater",
    key: "lastUpdater",
    showColums:true,
    width: 150,
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    key: "updateTime",
    scopedSlots: { customRender: "time" },
    showColums:true,
  },
  {
    title: "任务创建时间",
    dataIndex: "createTime",
    key: "createTime",
    scopedSlots: { customRender: "time" },
    showColums:true,
  },
  {
    title: "操作",
    width: 200,
    fixed: "right",
    scopedSlots: { customRender: "action" },
  },
];

export { columns };
