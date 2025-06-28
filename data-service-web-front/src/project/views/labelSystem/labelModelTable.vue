<template>
  <div>
    <a-table
      class="dc-tb-stripe"
      :rowKey="(row) => row.labelId"
      :columns="columns"
      :pagination="pagination"
      :data-source="tableData"
      :loading="loading"
      @change="changePage"
      size="middle"
      tableLayout="fixed"
    >
      <template slot="createTime" slot-scope="text">
        <span>{{ formatTime(text, "yyyy年MM月dd日 hh:mm:ss") }}</span>
      </template>
      <template slot="labelType" slot-scope="text">
        <span>{{ formatterDesc(text, "type") }}</span>
      </template>
      <template slot="valueType" slot-scope="text">
        <span>{{ formatterDesc(text, "valueType") }}</span>
      </template>
      <template slot="updateTime" slot-scope="text">
        <span>{{ formatTime(text, "yyyy年MM月dd日 hh:mm:ss") }}</span>
      </template>
      <template slot="labelClassify" slot-scope="text">
        <span>{{ formatterDesc(text, "labelClassify") }}</span>
      </template>
      <span slot="operate" slot-scope="text, record">
        <button
          class="dc-tb-btn-edit"
          @click="showModel({ status: 'info', id: record.labelId })"
        >
          <a-icon type="info" />详情
        </button>
        <button
          class="dc-tb-btn-edit"
          @click="showModel({ status: 'edit', id: record.labelId })"
        >
          <a-icon type="edit" />编辑
        </button>
        <button class="dc-tb-btn-del" @click="deleteLabelModel(record.labelId)">
          <a-icon type="delete" />删除
        </button>
      </span>
    </a-table>
  </div>
</template>

<script>
const columns = [
  {
    title: "标签ID",
    dataIndex: "labelId",
    key: "labelId",
  },
  {
    title: "名称",
    dataIndex: "labelName",
    key: "labelName",
  },
  {
    title: "分类",
    dataIndex: "labelClassify",
    key: "labelClassify",
    scopedSlots: { customRender: "labelClassify" },
  },
  // {
  //   title: '描述',
  //   dataIndex: 'description',
  //   key: 'description',
  // },
  {
    title: "数据类别",
    dataIndex: "labelType",
    key: "labelType",
    scopedSlots: { customRender: "labelType" },
  },
  {
    title: "标签值类型",
    dataIndex: "valueType",
    key: "valueType",
    scopedSlots: { customRender: "valueType" },
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    key: "createTime",
    scopedSlots: { customRender: "createTime" },
  },
  {
    title: "创建人",
    dataIndex: "creator",
    key: "creator",
  },
  {
    title: "最后更新时间",
    dataIndex: "updateTime",
    key: "updateTime",
    scopedSlots: { customRender: "updateTime" },
  },
  {
    title: "最后更新人",
    dataIndex: "lastUpdater",
    key: "lastUpdater",
  },
  {
    title: "操作",
    width: "15%",
    dataIndex: "operate",
    key: "operate",
    align: "center",
    scopedSlots: { customRender: "operate" },
  },
];
import Service from "@p/services/labelService";
export default {
  name: "labelModelTable",
  props: {
    labelClassify: {
      type: Array,
      default: () => [],
    },
    type: {
      type: Array,
      default: () => [],
    },
    valueType: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      columns,
      loading: false,
      tableData: [],
      pagination: {},
    };
  },
  methods: {
    changePage(page) {
      this.pagination = page;
      console.log(page);
      this.getLabelModelList(page.current, page.pageSize);
    },
    async getLabelModelList(page_index, page_size, param) {
      this.loading = true;
      let obj = {};
      if (param) {
        obj = {
          page_index,
          page_size,
          ...param,
        };
      } else {
        obj = {
          page_index,
          page_size,
        };
      }

      let res = await Service.getLabelModelList(obj);
      this.loading = false;
      if (res.code == 0) {
        this.pagination = {
          current: page_index,
          pageSize: page_size,
          total: res.totalCount,
          showSizeChanger: true,
          showLessItems: true,
          showQuickJumper: true,
        };
        this.tableData = res.dataList;
      } else {
        this.$message.error(res.msg);
      }
    },
    deleteLabelModel(id) {
      let self = this;
      this.$confirm({
        title: "是否删除该标签模型？",
        async onOk() {
          self.loading = true;
          let page = self.pagination;
          let res = await Service.deleteLabelModel(id);
          self.loading = false;
          if (res.code == 0) {
            self.$message.success("删除成功");
            self.getLabelModelList(page.current, page.pageSize);
          } else {
            self.$message.error(res.msg);
          }
        },
      });
    },
    showModel(record) {
      this.$emit("showModel", record);
      console.log('11111111111',record);
    },
    formatTime(time, fmt) {
      let self = new Date(time);
      let o = {
        "M+": self.getMonth() + 1, //月份
        "d+": self.getDate(), //日
        "h+": self.getHours(), //小时
        "m+": self.getMinutes(), //分
        "s+": self.getSeconds(), //秒
        "q+": Math.floor((self.getMonth() + 3) / 3), //季度
        S: self.getMilliseconds(), //毫秒
      };
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (self.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      for (let k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
        }
      }
      return fmt;
    },
    formatterDesc(text, type) {
      const res = this[type].filter((item) => {
        return item.id == text;
      });
      if (res.length > 0) {
        return res[0].name;
      } else {
        return "未知";
      }
    },
  },
  mounted() {
    this.getLabelModelList(1, 10);
  },
};
</script>
<style scoped></style>
