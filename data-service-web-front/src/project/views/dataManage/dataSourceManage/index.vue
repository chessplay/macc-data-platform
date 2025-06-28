<!--
 * @Author: 'chenmengni'
 * @Date: 2024-12-03 16:56:49
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2024-12-26 13:50:16
 * @FilePath: \data-service-web\src\project\views\dataManage\dataSourceManage\index.vue
 * @Description: 数据源界面
-->

<template>
  <div class="dc-page-card">
    <div class="header operation-wrapper">
      <div class="operation">
        <a-button type="primary" @click="showAddModal()" icon="plus"
          >新增</a-button
        >
      </div>

      <div class="search">
        <a-space :size="16">
          <a-input
            placeholder="数据源名称/数据源键"
            v-model="filterObj.keyword"
            allowClear
          />
          <a-select
            v-model="filterObj.dbType"
            placeholder="数据源类型"
            style="width: 200px"
            allowClear
          >
            <a-select-option
              v-for="item in dbTypeJson"
              :key="item.key"
              :value="item.key"
            >
              {{ item.value }}
            </a-select-option>
          </a-select>
        </a-space>
      </div>
    </div>
    <a-table
      tableLayout="fixed"
      size="middle"
      class="dc-tb-stripe"
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      :rowKey="
        (record, index) => {
          return index;
        }
      "
    >
      <!-- 数据源属性 -->
      <div slot="dbProperties" slot-scope="text, record">
        <div v-html="formatterProperties(record)"></div>
      </div>
      <!-- 操作 -->
      <span slot="action" slot-scope="text, record">
        <button class="dc-tb-btn-edit" @click="operandRecord('edit', record)">
          <a-icon type="edit" />编辑
        </button>
        <button class="dc-tb-btn-del" @click="operandRecord('delete', record)">
          <a-icon type="delete" />删除
        </button>
      </span>
    </a-table>
    <AddModal ref="addModalRef" @ok="queryData"></AddModal>
  </div>
</template>

<script>
import { request } from "@/utils/request";
import {
  METADATE_DATASOURCE_LIST_GET,
  METADATE_DATASOURCE_DELETE_DELETE,
  METADATE_DATASOURCE_SAMPLE_GET,
} from "@/services/api";
import { columns } from "./columns.js";
import AddModal from "./addModal.vue";
import { debounce } from "lodash";

export default {
  components: {
    AddModal,
  },
  data() {
    return {
      // selectedRowKeys: [],
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
        keyword: "", //数据源名称/数据源键
        dbType: [], //数据源类型
      },
      columns,
      data: [],
      dbTypeJson: [],
    };
  },
  computed: {},
  watch: {
    "filterObj.keyword": {
      handler: debounce(function () {
        this.queryData();
      }, 500),
    },
    "filterObj.dbType": {
      handler: debounce(function () {
        this.queryData();
      }, 500),
    },
  },
  created() {},
  mounted() {
    this.queryData();
    this.getSample();
  },
  methods: {
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
    // 接口传参的参数  isAll：获取所有的数据
    getQueryParamObj(paramObj = { isAll: false }) {
      const queryObj = {
        db_type: this.filterObj.dbType,
        keyword: this.filterObj.keyword,
      };
      if (paramObj.isAll) {
        queryObj.pageSize = 1000;
        queryObj.pageNum = 1;
      } else {
        queryObj.pageSize = this.pagination.pageSize;
        queryObj.pageNum = this.pagination.current;
      }

      return queryObj;
    },
    // 获取表格数据  gotoPage1：返回值第一页
    async queryData(paramObj = { gotoPage1: false }) {
      if (paramObj.gotoPage1) {
        this.pagination.current = 1;
      }
      let params = {
        api: METADATE_DATASOURCE_LIST_GET,
        method: "GET",
        querys: this.getQueryParamObj(),
      };
      this.loading = true;
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      this.loading = false;
      if (!res.code) {
        let { list, totalCount } = res;
        this.data = list.map((item) => ({
          ...item,
        }));
        // this.pagination.total = totalCount;
        this.pagination.total = totalCount ? totalCount : list.length;
      } else {
        this.$message.error(res.msg);
      }
    },
    async getSample() {
      let params = {
        api: METADATE_DATASOURCE_SAMPLE_GET,
        method: "GET",
        querys: {},
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      if (!res.code) {
        this.getDbTypeKeyJson(res.list);
      }
    },
    // onSelectChange(selectedRowKeys) {
    //   this.selectedRowKeys = selectedRowKeys;
    // },

    formatterProperties(row) {
      let dbProperties = JSON.stringify(row.dbProperties, null, 2);
      // let html = "";
      // for (var key in dbProperties) {
      //   if (typeof dbProperties[key] == "object") {
      //     html += `${key} : `;
      //     let objectData = dbProperties[key];
      //     for (var key2 in objectData) {
      //       html += `${key2} : ${JSON.stringify(objectData[key2])}` + " <br>";
      //     }
      //   } else {
      //     html += `${key} : ${dbProperties[key]}` + " <br>";
      //   }
      // }
      return dbProperties;
    },
    getDbTypeKeyJson(data) {
      let json = [];
      let jsondata = data;
      if (jsondata) {
        jsondata.map((item) => {
          json.push({
            key: item.type,
            value: item.type,
            properties: item.properties,
          });
        });
      }
      this.dbTypeJson = json;
    },
    // 显示添加弹窗
    showAddModal() {
      // 显示添加数据源弹窗
      this.$refs.addModalRef.dbTypeJson = this.dbTypeJson;
      this.$refs.addModalRef.openModal();
    },
    // // 批量删除
    // batchDelete() {
    //   this.$confirm({
    //     title: "提示",
    //     content: "确认删除这些数据吗？",
    //     onOk: async () => {
    //       let list = [...this.selectedRowKeys];
    //       await this.delData(list);
    //     },
    //   });
    // },
    // 单条删除
    oneDel(row) {
      this.$confirm({
        title: "提示",
        content: "确定要删除这条数据吗？",
        onOk: async () => {
          let params = {
            datasource_key: row.dataSourceKey,
          };
          this.delData(params);
        },
      });
    },
    async delData(list) {
      let params = {
        params: list,
        api: METADATE_DATASOURCE_DELETE_DELETE,
        method: "delete",
      };
      let res = await request(params.api, params.method, params.params, {
        baseURL: "/maccdata/api",
      });
      if (!res.code) {
        this.$message.success(res.msg);
        // this.selectedRowKeys = [];
        this.queryData({ gotoPage1: true });
      } else {
        this.$message.error(res.msg);
      }
    },
    // 编辑
    edit(row) {
      let data = { ...row };
      this.$refs.addModalRef.dbTypeJson = this.dbTypeJson;
      this.$refs.addModalRef.openModal(data);
    },
    operandRecord(state, row) {
      if (state == "edit") {
        this.edit(row);
      } else if (state == "delete") {
        this.oneDel(row);
      }
    },
  },
};
</script>
<style lang='less' scoped>
.header {
  padding: 10px 0 10px 0;
  margin: 10px 0 10px 0;
}
.operation-wrapper {
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
</style>