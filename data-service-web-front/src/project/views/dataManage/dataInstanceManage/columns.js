/*
 * @Author: 'chenmengni'
 * @Date: 2024-12-04 10:46:46
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2024-12-20 11:37:37
 * @FilePath: \data-service-web\src\project\views\dataManage\dataTaskManage\columns.js
 * @Description:
 */

// import i18n from '@/locales';
const columns = [
  {
    title: "任务实例名称",
    dataIndex: "name",
    key: "name",
    fixed: "left",
    width: 300,
    showColums: true,
  },
  {
    title: "任务状态",
    dataIndex: "state",
    key: "state",
    showColums: true,
    scopedSlots: { customRender: "state" },
  },
  {
    title: "最大重试次数",
    dataIndex: "maxTryTimes",
    key: "maxTryTimes",
    showColums: true,
    width: 150,
  },
  {
    title: "持续时间",
    dataIndex: "duration",
    key: "duration",
    showColums: true,
  },
  {
    title: "启动时间",
    dataIndex: "startTime",
    key: "startTime",
    scopedSlots: { customRender: "time" },
    showColums: true,
  },
  {
    title: "结束时间",
    dataIndex: "endTime",
    key: "endTime",
    scopedSlots: { customRender: "time" },
    showColums: true,
  },
  {
    title: "操作",
    fixed: "right",
    align: "center",
    scopedSlots: { customRender: "action" },
    width: 180,
  },
];

export { columns };
