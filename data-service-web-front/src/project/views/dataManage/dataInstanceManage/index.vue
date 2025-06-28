<!--
 * @Author: 'chenmengni'
 * @Date: 2024-12-03 16:56:49
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2025-02-08 14:56:27
 * @FilePath: \data-service-web\src\project\views\dataManage\dataInstanceManage\index.vue
 * @Description: 任务列表
-->
<template>
  <div class="operation-wrapper" ref="operationWrapper">
    <div class="header operation-header">
      <div class="operation">
        <a-button type="primary" @click="showAddModal()" icon="plus"
          >新增</a-button
        >
        <a-button @click="batchDelete" :disabled="!selectedRowKeys.length"
          >批量删除</a-button
        >
      </div>
      <div class="search">
        <a-space :size="16">
          <a-select
            v-model="filterObj.id"
            placeholder="请选择"
            style="width: 200px"
            allowClear
          >
            <a-select-option
              v-for="(item, index) in templateList"
              :key="item.processCode"
              :value="item.processCode"
              :data-index="index"
            >
              {{ item.name }}
            </a-select-option>
          </a-select>
          <a-popover
            placement="bottom"
            overlayClassName="table-popover"
            title="列选择"
            trigger="click"
          >
            <template slot="content">
              <p v-for="item in columns" :key="item.title">
                <a-checkbox
                  :checked="item.showColums != false"
                  @click="(e) => changeColums(e, item)"
                >
                  {{ item.title || item.titleTxt || item.key }}
                </a-checkbox>
              </p>
            </template>
            <a-icon type="appstore" />
          </a-popover>
        </a-space>
      </div>
    </div>
    <div class="table-adv-content" v-show="ifShowMore">
      <div class="adv-search-area">
        <div class="adv-search-item">
          <label>启动时间</label>
          <a-range-picker
            class="adv-search-ipt"
            v-model="filterObj.startTime"
            format="YYYY-MM-DD"
          >
            <a-icon slot="suffixIcon" type="calendar" />
          </a-range-picker>
        </div>
      </div>
      <div>
        <div class="adv-search-btn">
          <a-button type="primary" @click="queryData"> 查询 </a-button>
          <a-button @click="onReset"> 重置 </a-button>
        </div>
      </div>
    </div>
    <a-table
      tableLayout="fixed"
      size="middle"
      class="dc-tb-stripe"
      :columns="selectColumnsData"
      :data-source="data"
      :pagination="pagination"
      :rowSelection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange,
      }"
      :rowKey="
        (record, index) => {
          return record.id;
        }
      "
      :scroll="{ x: 1200 }"
    >
      <!-- 时间 -->
      <div slot="time" slot-scope="text">{{ formatterTime(text) }}</div>
      <!-- 状态 -->
      <div slot="state" slot-scope="text">
        <div class="dot" :class="getClassByState(text)">
          {{ text }}
        </div>
      </div>
      <!-- 操作 -->
      <span slot="action" slot-scope="text, record">
        <button class="dc-tb-btn" @click="operandRecord('rerun', record)">
          重跑
        </button>
        <button
          class="dc-tb-btn"
          :class="
            record.processInstanceTasksQueryResp.taskList.length < 1
              ? 'dc-tb-btn-none'
              : ''
          "
          @click="operandRecord('showLog', record)"
        >
          查看日志
        </button>
        <a-popover placement="bottom">
          <template slot="content">
            <div class="popover-btn">
              <button class="dc-tb-btn" @click="operandRecord('pause', record)">
                暂停
              </button>
              <!-- <button class="dc-tb-btn" @click="operandRecord('stop', record)">
                停止
              </button> -->
              <button
                class="dc-tb-btn dc-tb-btn-delete"
                @click="operandRecord('delete', record)"
              >
                删除
              </button>
            </div>
          </template>
          更多
        </a-popover>
      </span>
    </a-table>
    <a-modal
      width="1000px"
      title="查看日志"
      :visible="showLogModal"
      :maskClosable="true"
      :get-container="() => this.$refs.operationWrapper"
      @cancel="handleShowLogModal"
      @ok="handleShowLogModal"
    >
      <div class="log-header">
        <span style="font-weight: 600">实例名称：{{ rowTaskName }}</span>
        <div>
          <span>显示行数：</span>
          <a-input-search
            v-model="logPagination.limit"
            placeholder="显示行数，默认20"
            style="width: 100px; margin-right: 16px"
          />
          <span>起始行：</span>
          <a-input-search
            v-model="logPagination.skipLineNum"
            placeholder="从第几行开始"
            style="width: 100px"
          />
        </div>
      </div>
      <div v-if="taskLogs">
        <div class="log-html" v-html="taskLogs"></div>
      </div>
      <a-spin v-else tip="Loading..." class="loading-log"> </a-spin>
    </a-modal>
    <TemplateSetModal
      ref="templateSetModalRef"
      @updateTemplateCancelCallback="updateTemplateCancelCallback"
    ></TemplateSetModal>
  </div>
</template>

<script>
import { request } from "@/utils/request";
import {
  METADATE_TASK_LIST_GET,
  METADATE_TASK_START_POST,
  METADATE_TASK_STOP_POST,
  METADATE_TASK_RERUN_POST,
  METADATE_TASK_PAUSE_POST,
  METADATE_TASK_DELETE_DELETE,
  METADATE_TASK_LOG_GET,
  METADATE_TASK_TEMPLATE_GET,
} from "@/services/api";
import { columns } from "./columns.js";
import moment from "moment";
import TemplateSetModal from "./templateSetModal.vue";
import { debounce } from "lodash";

export default {
  components: {
    TemplateSetModal,
  },
  data() {
    return {
      selectedRowKeys: [],
      pagination: {
        showSizeChanger: true,
        pageSizeOptions: ["10", "20", "30", "50"],
        total: 0,
        current: 1,
        pageSize: 10,
        showTotal: (total) => `共${total}条`,
        onChange: (page, pageSize) => this.pageChange(page, pageSize),
        onShowSizeChange: (current, pageSize) =>
          this.sizeChange(current, pageSize),
      },
      logPagination: {
        limit: 20,
        skipLineNum: 0,
      },
      filterObj: {
        id: [],
        name: "", //实例名称
        startTime: null, //启动时间
      },
      columns,
      columnsData: columns,
      selectColumnsData: [],
      data: [],
      ifShowMore: false,
      showLogModal: false,
      taskLogs: "", //实例任务操作日志
      rowLog: {},
      rowTaskName: "",
      templateList: [],
    };
  },
  computed: {
    getContainer() {
      return "#operation-wrapper";
    },
  },
  watch: {
    "filterObj.id": {
      handler: debounce(function () {
        this.queryData();
      }, 500),
    },

    "logPagination.limit": {
      handler: debounce(function () {
        this.showLog(this.rowLog);
      }, 500),
    },
    "logPagination.skipLineNum": {
      handler: debounce(function () {
        this.showLog(this.rowLog);
      }, 500),
    },
  },
  created() {},
  mounted() {
    this.queryData();
    this.changeColums();
    this.getAllTemplateList();
  },
  methods: {
    async getAllTemplateList() {
      const queryObj = {
        pageSize: 1000,
        pageNum: 1,
      };
      let params = {
        api: METADATE_TASK_TEMPLATE_GET,
        method: "get",
        querys: queryObj,
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      if (res.code == "200") {
        let { rows } = res;
        this.templateList = rows.map((item) => ({
          ...item,
        }));
      }
    },
    changeColums(e, info) {
      if (info) {
        this.columnsData.forEach((item) => {
          if (item.dataIndex == info.dataIndex) {
            item.showColums = e.target.checked;
          }
        });
      }
      this.columnsData = [...this.columnsData];
      const currentSelectColums = this.columns.filter((item) => {
        return item.showColums != false;
      });
      this.selectColumnsData = [...currentSelectColums];
    },
    // 显隐高级筛选
    async onShowMore() {
      this.ifShowMore = !this.ifShowMore;
    },
    // 高级筛选重置
    onReset() {
      this.filterObj.startTime = null;
    },
    // 切换页数
    pageChange(page) {
      this.pagination.current = page;
      this.queryData();
    },
    // 切换每页数量
    sizeChange(current, pageSize) {
      this.pagination.current = 1;
      this.pagination.pageSize = pageSize;
      this.queryData();
    },

    formatterTime(value) {
      return value ? moment(value).format("YYYY-MM-DD HH:mm:ss") : "--";
    },
    // 接口传参的参数  isAll：获取所有的数据
    getQueryParamObj(paramObj = { isAll: false }) {
      const queryObj = {
        // name: this.filterObj.name,
        id: this.filterObj.id,
      };
      if (paramObj.isAll) {
        queryObj.pageSize = 1000;
        queryObj.pageNum = 1;
      } else {
        queryObj.pageSize = this.pagination.pageSize;
        queryObj.pageNum = this.pagination.current;
      }

      if (this.filterObj.startTime && this.filterObj.startTime.length > 0) {
        queryObj.createStartTime = moment(
          this.filterObj.startTime[0]
        ).valueOf();
        queryObj.createEndTime = moment(this.filterObj.startTime[1]).valueOf();
      }

      return queryObj;
    },
    // 获取表格数据  gotoPage1：返回值第一页
    async queryData(paramObj = { gotoPage1: false }) {
      if (paramObj.gotoPage1) {
        this.pagination.current = 1;
      }
      let params = {
        api: METADATE_TASK_LIST_GET,
        method: "GET",
        querys: this.getQueryParamObj(),
      };
      this.loading = true;
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      this.loading = false;
      if (res.code == "200") {
        let { totalList, total } = res.data;
        this.data = totalList.map((item) => ({
          ...item,
        }));
        // this.pagination.total = totalCount;
        this.pagination.total = total ? total : totalList.length;
      } else {
        this.$message.error(res.msg);
      }
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
    // 显示添加弹窗
    showAddModal() {
      // 显示添加数据源弹窗
      this.$refs.templateSetModalRef.openModal();
    },
    async handleTaskMethod(row, state) {
      let url = METADATE_TASK_START_POST;
      let method = "post";
      switch (state) {
        case "start":
          url = METADATE_TASK_START_POST + "?id=" + row.id;
          method = "post";
          break;
        case "stop":
          url = METADATE_TASK_STOP_POST + "?id=" + row.id;
          method = "put";
          break;
        case "rerun":
          url = METADATE_TASK_RERUN_POST + "?id=" + row.id;
          method = "put";
          break;
        case "pause":
          url = METADATE_TASK_PAUSE_POST + "?id=" + row.id;
          method = "put";
          break;
        default:
      }
      let params = {
        params: {
          id: row.id,
        },
        api: url,
        method: method,
      };
      let res = await request(params.api, params.method, params.params, {
        baseURL: "/maccdata/api",
      });
      if (res.code == 200) {
        this.$message.success(res.msg);
        this.queryData({ gotoPage1: true });
      } else {
        this.$message.error(res.msg);
      }
    },
    startTask(row) {
      this.$confirm({
        title: "提示",
        content: "确认启动任务吗？",
        onOk: async () => {
          await this.handleTaskMethod(row, "start");
        },
      });
    },

    stopTask(row) {
      this.$confirm({
        title: "提示",
        content: "确认停止任务吗？",
        onOk: async () => {
          await this.handleTaskMethod(row, "stop");
        },
      });
    },
    rerunTask(row) {
      this.$confirm({
        title: "提示",
        content: "确认重跑任务吗？",
        onOk: async () => {
          await this.handleTaskMethod(row, "rerun");
        },
      });
    },
    pauseTask(row) {
      this.$confirm({
        title: "提示",
        content: "确认暂停任务吗？",
        onOk: async () => {
          await this.handleTaskMethod(row, "pause");
        },
      });
    },

    // 批量删除
    batchDelete() {
      this.$confirm({
        title: "提示",
        content: "确认删除这些数据吗？",
        onOk: async () => {
          let list = {
            ids: this.selectedRowKeys.join(","),
          };
          await this.delData(list);
        },
      });
    },
    // 单条删除
    oneDel(row) {
      this.$confirm({
        title: "提示",
        content: "确定要删除这条数据吗？",
        onOk: async () => {
          let params = {
            ids: row.id,
          };
          this.delData(params);
        },
      });
    },
    async delData(list) {
      let params = {
        params: list,
        api: METADATE_TASK_DELETE_DELETE,
        method: "delete",
      };
      let res = await request(params.api, params.method, params.params, {
        baseURL: "/maccdata/api",
      });
      if (res.code == 200) {
        this.$message.success(res.msg);
        this.selectedRowKeys = [];
        this.queryData({ gotoPage1: true });
      } else {
        this.$message.error(res.msg);
      }
    },
    // 查看当前任务实例的操作日志
    async showLog(row) {
      if (row.processInstanceTasksQueryResp.taskList.length < 1) {
        return;
      }
      this.taskLogs = "";
      this.rowTaskName = row.name;
      let logId = row.processInstanceTasksQueryResp.taskList[0].id;

      let params = {
        params: {
          id: logId,
          limit: this.logPagination.limit,
          skipLineNum: this.logPagination.skipLineNum,
        },
        api: METADATE_TASK_LOG_GET,
        method: "get",
      };
      this.showLogModal = true;
      let res = await request(params.api, params.method, params.params, {
        baseURL: "/maccdata/api",
      });
      if (res.code == 200) {
        this.taskLogs = res.data.message;
      } else {
        this.$message.error(res.msg);
      }
    },
    handleShowLogModal() {
      this.showLogModal = false;
    },
    operandRecord(state, row) {
      switch (state) {
        case "start":
          this.startTask(row);
          break;
        case "stop":
          this.stopTask(row);
          break;
        case "delete":
          this.oneDel(row);
          break;
        case "rerun":
          this.rerunTask(row);
          break;
        case "pause":
          this.pauseTask(row);
          break;
        case "showLog":
        default:
          this.rowLog = row;
          this.logPagination.limit = 20;
          this.showLog(row);
      }
    },
    updateTemplateCancelCallback() {
      this.queryData();
    },
    getClassByState(state) {
      let className = "";
      switch (state) {
        case "SUCCESS":
          className = "success";
          break;
        case "PAUSE":
        case "STOP":
          className = "error";
          break;
        case "RUNNING":
          className = "blue";
          break;
        default:
          className = "";
      }
      return className;
    },
  },
};
</script>
<style lang='less' scoped>
.not-show-more {
  transform: rotate(180deg);
}
.header {
  padding: 10px 0 10px 0;
  margin: 10px 0 10px 0;
}
.operation-wrapper {
  background: @gray-1;
  padding: 8px;
}
.operation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  box-sizing: border-box;
}
.operation {
  display: flex;
  gap: 20px;
}
.search {
  display: flex;
  gap: 16px;
}
.dc-tb-stripe {
  .dot {
    &::before {
      display: inline-block;
      width: 6px;
      height: 6px;
      content: "";
      background: @gray-7;
      border-radius: 50%;
      vertical-align: middle;
      margin: 0 2px;
    }
  }

  .success {
    &::before {
      content: "";
      background: #018903;
    }
  }
  .error {
    &::before {
      content: "";
      background: #fa5050;
    }
  }
  .blue {
    &::before {
      content: "";
      background: #20b0ee;
    }
  }
}
.dc-tb-btn {
  color: #2b6afd;
  cursor: pointer;
  background-color: transparent;
  border-color: transparent;
  i {
    margin-right: 2px;
  }
  &:hover {
    text-decoration: underline;
  }
  &:focus,
  &:active {
    outline: 0;
  }
}

.dc-tb-btn-none {
  color: #ccc;
  cursor: auto;
  &:hover {
    text-decoration: none;
  }
}
.dc-tb-btn-delete {
  color: #de321f;
}
.table-adv-content {
  padding: 12px;
  background: @gray-4;
  border-radius: 4px;
  margin-bottom: 12px;
  .adv-search-area {
    display: flex;
    flex-wrap: wrap;
    gap: 12px 0;
    .adv-search-item {
      display: flex;
      gap: 16px;
      align-items: center;
      justify-content: flex-end;
      width: 518px;
      label {
        white-space: nowrap;
        width: 160px;
        text-align: right;
      }
    }
  }
  .adv-search-ipt {
    width: 75%;
  }
  .adv-search-btn {
    display: flex;
    gap: 16px;
    justify-content: flex-end;
    margin-top: 16px;
  }
}
.popover-btn {
  > button {
    display: block;
    margin: 6px 0;
  }
}
.loading-log {
  display: flex;
  justify-content: center;
  min-height: 100px;
  align-items: center;
  flex-direction: column;
  gap: 12px;
}
.log-header {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
}
.log-html {
  max-height: 400px;
  overflow-y: auto;
  white-space: pre-wrap;
}
</style>