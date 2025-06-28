/*
 * @Author: 'chenmengni'
 * @Date: 2024-12-04 10:46:46
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2024-12-05 09:31:50
 * @FilePath: \data-service-web\src\project\views\dataManage\dataSourceManage\columns.js
 * @Description:数据源-表格columns
 */

// import i18n from '@/locales';
const columns = [
  {
    title: "数据源名称",
    dataIndex: "name",
    key: "name",
    width: 150,
  },
  {
    title: "数据源键",
    dataIndex: "dataSourceKey",
    key: "dataSourceKey",
    width: 150,
  },
  {
    title: "数据源类型",
    dataIndex: "dbType",
    key: "dbType",
    width: 150,
  },
  {
    title: "数据源属性",
    dataIndex: "dbProperties",
    key: "dbProperties",
    scopedSlots: { customRender: "dbProperties" },
  },
  {
    title: "操作",
    width: 150,
    fixed: "right",
    scopedSlots: { customRender: "action" },
  },
];

export { columns };
