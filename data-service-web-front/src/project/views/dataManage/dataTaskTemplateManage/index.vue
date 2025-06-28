<!--
 * @Author: 'chenmengni'
 * @Date: 2024-12-03 16:57:20
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2024-12-26 13:50:06
 * @FilePath: \data-service-web\src\project\views\dataManage\dataTaskTemplateManage\index.vue
 * @Description: 任务模板界面
-->

<template>
  <div class="operation-wrapper">
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
          <a-input
            placeholder="任务模板名称"
            allowClear
            v-model="filterObj.name"
          />

          <a-button @click="onShowMore">
            <div class="btn-content">
              <span>高级筛选</span>
              <a-icon
                type="up"
                :class="!ifShowMore ? 'not-show-more' : ''"
                style="font-size: 10px; padding: 0 5px"
              />
            </div>
          </a-button>
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
          <label>源数据源键</label>
          <a-input
            class="adv-search-ipt"
            allowClear
            v-model="filterObj.sourceDataKey"
          />
        </div>
        <div class="adv-search-item">
          <label>源表名称</label>
          <a-input
            class="adv-search-ipt"
            allowClear
            v-model="filterObj.sourceTableName"
          />
        </div>

        <div class="adv-search-item">
          <label>目标数据源键</label>
          <a-input
            class="adv-search-ipt"
            allowClear
            v-model="filterObj.sinkDataKey"
          />
        </div>
        <div class="adv-search-item">
          <label>目标表名称</label>
          <a-input
            class="adv-search-ipt"
            allowClear
            v-model="filterObj.sinkTableName"
          />
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
      :scroll="{ x: 2000 }"
    >
      <!-- 时间 -->
      <div slot="time" slot-scope="text, record">
        {{ formatterTime(text, record) }}
      </div>
      <!-- 操作 -->
      <span slot="action" slot-scope="text, record">
        <button class="dc-tb-btn-edit" @click="operandRecord('edit', record)">
          <a-icon type="edit" />编辑
        </button>
        <button class="dc-tb-btn" @click="operandRecord('clone', record)">
          拷贝
        </button>
        <button class="dc-tb-btn-del" @click="operandRecord('delete', record)">
          <a-icon type="delete" />删除
        </button>
      </span>
    </a-table>
    <TemplateSetModal
      ref="templateSetModalRef"
      @updateTemplateCancelCallback="updateTemplateCancelCallback"
    ></TemplateSetModal>
  </div>
</template>

<script>
import { request } from "@/utils/request";
import {
  METADATE_TASK_TEMPLATE_GET,
  METADATE_TASK_TEMPLATE_DELETE_DELETE,
} from "@/services/api";
import { columns } from "./columns.js";
import moment from "moment";
import TemplateSetModal from "./templateSetModal.vue";
import { debounce, cloneDeep } from "lodash";

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
      filterObj: {
        name: "", //任务模板名称
        sourceTableName: "", //源表名称
        sinkTableName: "", //目标表名称
        sourceDataKey: "", //源数据源键
        sinkDataKey: "", //目标数据源键
        lastUpdater: "", //最后更新者
        updateTime: null, //更新时间
        createTime: null, //创建时间
      },
      columns,
      columnsData: columns,
      selectColumnsData: [],
      data: [],
      ifShowMore: false,
    };
  },
  computed: {},
  watch: {
    "filterObj.name": {
      handler: debounce(function () {
        this.queryData();
      }, 500),
    },
  },
  created() {},
  mounted() {
    this.queryData();
    this.changeColums();
  },
  methods: {
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
      this.filterObj.updateTime = null;
      this.filterObj.createTime = null;
      this.filterObj.lastUpdater = "";

      this.filterObj.sourceTableName = "";
      this.filterObj.sinkTableName = "";
      this.filterObj.sourceDataKey = "";
      this.filterObj.sinkDataKey = "";
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

    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
    // 接口传参的参数  isAll：获取所有的数据
    getQueryParamObj(paramObj = { isAll: false }) {
      const queryObj = {
        name: this.filterObj.name,
        sourceTableName: this.filterObj.sourceTableName,
        sinkTableName: this.filterObj.sinkTableName,
        sourceDataKey: this.filterObj.sourceDataKey,
        sinkDataKey: this.filterObj.sinkDataKey,
        lastUpdater: this.filterObj.lastUpdater,
      };

      if (paramObj.isAll) {
        queryObj.pageSize = 1000;
        queryObj.pageNum = 1;
      } else {
        queryObj.pageSize = this.pagination.pageSize;
        queryObj.pageNum = this.pagination.current;
      }

      if (this.filterObj.updateTime && this.filterObj.updateTime.length > 0) {
        queryObj.updateStartTime = moment(
          this.filterObj.updateTime[0]
        ).valueOf();
        queryObj.updateEndTime = moment(this.filterObj.updateTime[1]).valueOf();
      }

      if (this.filterObj.createTime && this.filterObj.createTime.length > 0) {
        queryObj.createStartTime = moment(
          this.filterObj.createTimes[0]
        ).valueOf();
        queryObj.createEndTime = moment(
          this.filterObj.createTimes[1]
        ).valueOf();
      }

      return queryObj;
    },
    // 获取表格数据  gotoPage1：返回值第一页
    async queryData(paramObj = { gotoPage1: false }) {
      if (paramObj.gotoPage1) {
        this.pagination.current = 1;
      }
      let params = {
        api: METADATE_TASK_TEMPLATE_GET,
        method: "get",
        querys: this.getQueryParamObj(),
      };
      this.loading = true;
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      this.loading = false;
      if (res.code == "200") {
        let { rows, total } = res;
        this.data = rows.map((item) => ({
          ...item,
        }));
        // this.pagination.total = totalCount;
        this.pagination.total = total ? total : rows.length;
      } else {
        this.$message.error(res.msg);
      }
    },
    // 显示添加弹窗
    showAddModal() {
      // 显示添加模板弹窗
      this.$refs.templateSetModalRef.openModal("add");
    },
    // 批量删除
    batchDelete() {
      this.$confirm({
        title: "提示",
        content: "确认删除这些数据吗？",
        onOk: async () => {
          let list = [...this.selectedRowKeys];
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
          let list = [row.id];
          this.delData(list);
        },
      });
    },
    async delData(list) {
      let params = {
        params: {
          ids: list.join(","),
        },
        api: METADATE_TASK_TEMPLATE_DELETE_DELETE,
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
    edit(row) {
      let data = cloneDeep(row);
      this.$refs.templateSetModalRef.openModal("edit", data);
    },

    oneClone(row) {
      let data = cloneDeep(row);
      data.id = "";
      data.name = row.name + "_克隆";
      this.$refs.templateSetModalRef.openModal("clone", data);
    },

    operandRecord(state, row) {
      switch (state) {
        case "edit":
          this.edit(row);
          break;
        case "clone":
          this.oneClone(row);
          break;
        case "delete":
        default:
          this.oneDel(row);
      }
    },
    updateTemplateCancelCallback() {
      this.$refs.templateSetModalRef.handleCancel();
      this.queryData();
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
</style>