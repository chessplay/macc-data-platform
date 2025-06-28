<template>
  <div class="dc-page-card">
    <div class="conf">
      <div class="header">
        <div class="operation">
          <a-button type="primary" @click="showModal()" icon="plus">新增</a-button>
          <a-button type="primary" :disabled="!hasSelected" @click="remove" icon="delete">删除</a-button>
        </div>
        <div class="search" style="position: relative;
  z-index: 0;">
          <span>应用：</span>
          <a-select defaultValue="全部" class="appid-select" @change="changeAppId">
            <a-select-option value="全部">全部</a-select-option>
            <a-select-option value="79060">MACC (79060)</a-select-option>
            <a-select-option value="72620">APP (72620)</a-select-option>
          </a-select>
          <span>分类：</span>
          <a-cascader :options="type1Options" @change="handleChangeOne" :defaultValue="['全部', '全部']" :allowClear="false" />
          <a-cascader class="select" :options="type2Options" @change="handleChangeTwo" :allowClear="false"
            :defaultValue="['全部', '全部']" />
          <a-input class="name-input" v-model="name" placeholder="关键字"></a-input>
          <a-button type="primary" @click="search" icon="search">搜索</a-button>
        </div>
      </div>
      <a-table tableLayout="fixed" size="middle" class="dc-tb-stripe" :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }" :rowKey="
        (record, index) => {
          return index;
        }
      " :columns="columns" :data-source="data" :pagination="pagination" @change="onPageChange">
        <span slot="name" slot-scope="text">{{ text }}</span>
        <span slot="action" slot-scope="text, record">
          <button class="dc-tb-btn-edit" v-if="data.length" @click="showModal(record)">
            <a-icon type="edit" />编辑
          </button>
          <a-divider type="vertical" />
          <button class="dc-tb-btn-del" v-if="data.length" @click="showConfirm(record)">
            <a-icon type="delete" />删除
          </button>
        </span>
      </a-table>
    </div>
    <Modal ref="modal" :rowData="rowData" :visible="visible" @success="handleCreate" @cancel="handleCancel"></Modal>
  </div>
</template>
<script>
import EventService from "@p/services/eventService.js";
import TypeService from '@p/services/typeService'
import newEventService from "@p/services/newEventService.js";
import Modal from "@p/views/event-conf/components/modal.vue";
import moment from "moment";
const columns = [
  {
    title: "appid",
    dataIndex: "appid",
  },
  {
    title: "名称",
    dataIndex: "name",
    scopedSlots: { customRender: "name" },
  },
  {
    title: "事件别名",
    dataIndex: "alias",
  },
  {
    title: "一级分类",
    dataIndex: "type1",
  },
  {
    title: "二级分类",
    dataIndex: "type2",
  },
  {
    title: "埋点上线版本",
    dataIndex: "version",
  },
  {
    title: "埋点上线时间",
    dataIndex: "time",
  },
  {
    title: "扩展列",
    dataIndex: "extColumns",
  },
  {
    title: "备注",
    dataIndex: "remark",
  },
  {
    title: "操作",
    dataIndex: "action",
    scopedSlots: { customRender: "action" },
  },
];
const data = [];
const initPage = {
  page_index: 1,
  page_size: 10
}
// const pagination = {
//   pageSize: 10,
//   showQuickJumper: true,
//   showSizeChanger: true,
// };

const type1Options = [];

const type2Options = [];
export default {
  components: {
    Modal,
  },
  data () {
    return {
      visible: false,
      columns,
      data,
      pagination: {
        total: 0,
      },
      type1Options,
      value1: "全部",
      type2Options,
      value2: "全部",
      selectedRowKeys: [],
      typeList: [],
      rowData: {},
      time: "",
      appidValue: "全部",
      temporaryData: [],
      name: "",
      isSelect: false
    };
  },

  computed: {
    hasSelected () {
      return this.selectedRowKeys.length > 0;
    },
  },

  mounted () {
    this.query(initPage);
    this.getType();
  },
  methods: {
    async getType () {
      let res = await TypeService.query()
      this.typeList = this.dealData(res.list);
      this.type1Options.push({
        value: "全部",
        label: "全部",
      });
      this.type2Options.push({
        value: "全部",
        label: "全部",
      });
      for (let i = 0; i < this.typeList.length; i++) {
        this.type1Options.push({
          value: this.typeList[i].type1,
          label: this.typeList[i].type1,
        });
      }
    },
    dealData (data) {
      let listArr = [];
      data.forEach(function (el, index) {
        for (let i = 0; i < listArr.length; i++) {
          if (listArr[i].type1 == el.type1) {
            listArr[i].type2list.push(el.type2);
            return;
          }
        }
        listArr.push({
          type1: el.type1,
          type2list: [el.type2],
        });
      });
      return listArr;
    },
    /**
      * @description: 如果点击分页
      * @param {*}pagination
      * @return {*}
      */
    async onPageChange (pagination) {
      const pager = { ...this.pagination };
      pager.current = pagination.current;
      this.pagination = pager;
      let val = this.filterSearch(pager.current)
      let ret = await newEventService.pagingQuery(val)
      this.pagination.total = ret.totalCount
      this.data = ret.dataList
    },
    filterSearch (current) {
      let val = {
        type1: this.value1,
        type2: this.value2,
        keyword: this.name,
        appid: this.appidValue,
        ...initPage
      }
      let arr = [null, undefined, '全部', '', NaN]
      for (let i in val) {
        if (arr.includes(val[i])) {
          delete val[i]
        }
      }
      if (!this.isSelect) {
        val = {
          ...initPage
        }
      }
      if (current) {
        val.page_index = current
      }
      return val
    },
    async query (val) {
      this.data = [];
      this.type1Options = [];
      let ret = await newEventService.pagingQuery(val)
      this.pagination.total = ret.totalCount
      for (let i = 0; i < ret.dataList.length; i++) {
        this.data.push({
          key: i,
          appid: ret.dataList[i].appid,
          name: ret.dataList[i].name,
          alias: ret.dataList[i].alias,
          type1: ret.dataList[i].type1,
          type2: ret.dataList[i].type2,
          version: ret.dataList[i].version,
          time: ret.dataList[i].time,
          extColumns: ret.dataList[i].extColumns,
          remark: ret.dataList[i].remark,
        });
      }
      this.temporaryData = this.data;
      // this.typeList = this.dealData(this.data);

    },

    handleChangeOne (value) {
      this.type2Options = [];
      this.value1 = value[0];
      this.value2 = "全部";
      this.type2Options.push({
        value: "全部",
        label: "全部",
      });
      for (let i = 0; i < this.typeList.length; i++) {
        if (value[0] === this.typeList[i].type1) {
          let newtype2 = Array.from(new Set(this.typeList[i].type2list));
          for (let j = 0; j < newtype2.length; j++) {
            this.type2Options.push({
              value: newtype2[j],
              label: newtype2[j],
            });
          }
        }
      }
    },

    handleChangeTwo (value) {
      this.value2 = value[0];
    },

    changeAppId (value) {
      this.appidValue = value;
    },

    search () {
      this.data = [];
      this.isSelect = true;
      let val = this.filterSearch()
      newEventService.pagingQuery(val).then((ret) => {
        this.pagination.total = ret.totalCount
        this.pagination.current = 1
        for (let i = 0; i < ret.dataList.length; i++) {
          if (this.name === "") {
            if (this.appidValue === "全部" && this.value1 === "全部") {
              this.data.push(this.temporaryData[i]);
            } else if (
              this.appidValue === "全部" &&
              this.value1 === ret.dataList[i].type1 &&
              this.value2 === "全部"
            ) {
              this.data.push(this.temporaryData[i]);
            } else if (
              this.appidValue === "全部" &&
              this.value1 === ret.dataList[i].type1 &&
              this.value2 === ret.dataList[i].type2
            ) {
              this.data.push(this.temporaryData[i]);
            } else if (
              this.appidValue === ret.dataList[i].appid &&
              this.value1 === "全部"
            ) {
              this.data.push(this.temporaryData[i]);
            } else if (
              this.appidValue === ret.dataList[i].appid &&
              this.value1 === ret.dataList[i].type1 &&
              this.value2 === "全部"
            ) {
              this.data.push(this.temporaryData[i]);
            } else if (
              this.appidValue === ret.dataList[i].appid &&
              this.value1 === ret.dataList[i].type1 &&
              this.value2 === ret.dataList[i].type2
            ) {
              this.data.push(this.temporaryData[i]);
            }
          } else {
            this.data.push(ret.dataList[i]);
          }
        }
      });
    },

    showConfirm (key) {
      let that = this;
      this.$confirm({
        title: "提示",
        content: "确定要删除这条数据吗？",
        onOk () {
          EventService.delete({
            name_list: key.name,
          }).then((res) => {
            EventService.editCallBack(res, "操作成功");
            that.query(initPage);
            that.pagination.current = 1
          });
        },
        onCancel () { },
      });
    },

    remove () {
      var that = this;
      let name_list = [];
      for (let i = 0; i < this.selectedRowKeys.length; i++) {
        let key = this.selectedRowKeys[i];
        name_list.push(this.data[key].name);
      }
      this.$confirm({
        title: "提示",
        content: "确定要删除这" + this.selectedRowKeys.length + "条数据吗？",
        onOk () {
          EventService.delete({
            name_list: name_list,
          }).then((res) => {
            EventService.editCallBack(res, "操作成功");
            that.query(initPage);
            that.pagination.current = 1
          });
        },
        onCancel () { },
      });
      this.selectedRowKeys = [];
    },

    showModal (record) {
      this.rowData = record || {};
      this.visible = true;
    },

    handleCancel () {
      this.visible = false;
      const form = this.$refs.modal.form;
      form.resetFields();
    },

    handleCreate () {
      const form = this.$refs.modal.form;
      form.validateFields((err, values) => {
        if (err) {
          return;
        }
        if (!values.alias) {
          values.alias = "";
        }
        if (!values.version) {
          values.version = "";
        }
        this.time = moment(values.time).format("YYYY-MM-DD");
        EventService.addOrUpdate({
          appid: values.appid,
          name: values.name,
          alias: values.alias,
          type1: values.type1,
          type2: values.type2,
          version: values.version,
          time: this.time,
          extColumns: values.extColumns,
          remark: values.remark,
        }).then((ret) => {
          EventService.editCallBack(ret, "操作成功");
          this.query(initPage);
          this.pagination.current = 1
        });
        form.resetFields();
        this.visible = false;
      });
    },

    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
  },
};
</script>
<style lang="less" scoped>
.header {
  padding: 10px 0 10px 0;
  margin: 10px 0 10px 0;
}
.operation {
  display: inline-block;
}
.name-input {
  display: inline-block;
  width: 150px;
  margin-left: 10px;
}
.appid-select {
  width: 140px;
  margin-right: 10px;
}
.search {
  float: right;
}

.operation button {
  margin-right: 20px;
}

.search button {
  margin-left: 20px;
}

.select {
  margin-left: 8px;
}
</style>
