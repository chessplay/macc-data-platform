<template>
  <div>
    <a-button icon="plus" type="primary" @click="moduleadd('')"
      >添加模块</a-button
    >
    <a-table
      tableLayout="fixed"
      size="middle"
      class="dc-tb-stripe"
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
    >
      <span slot="deleteaction" slot-scope="text, record">
        <button class="dc-tb-btn-edit" @click="moduleadd(record)">
          <a-icon type="edit" />
        </button>
        <button class="dc-tb-btn-del" @click="moduledelete(record)">
          <a-icon type="delete" />
        </button>
      </span>
    </a-table>
    <Menumoduleadd
      ref="modal"
      :visible="visible"
      :rowData="rowData"
      @success="cancel('success')"
      @cancel="cancel"
      :next="next"
    ></Menumoduleadd>
  </div>
</template>

<script>
import EventService from "@p/services/eventService";
import Menumoduleadd from "@p/views/powerMgt/roleMgt/modal/menumodule/rolemoduleadd.vue";
const columns = [
  {
    title: "模块ID",
    dataIndex: "id",
  },
  {
    title: "模块名称",
    dataIndex: "name",
  },
  {
    title: "模块状态",
    dataIndex: "type",
  },
  {
    title: "描述信息",
    dataIndex: "description",
  },
  {
    title: "操作",
    dataIndex: "deleteaction",
    scopedSlots: { customRender: "deleteaction" },
  },
];
const pagination = {
  defaultPageSize: 50,
  hideOnSinglePage: true,
};
export default {
  components: {
    Menumoduleadd,
  },
  data() {
    return {
      data: [],
      columns,
      pagination,
      checkedList: "",
      // antionvalue: [],
      visible: false,
      rowData: "",
      next: {},
      checkedListkey: 0,
    };
  },
  props: {
    rolemenu: {
      type: String,
    },
    rolemenuname: {
      type: String,
    },
  },
  computed: {
    rolemenudata: function() {
      let value = this.rolemenu;
      return value;
    },
  },
  watch: {},
  mounted() {
    this.show();
  },
  methods: {
    show() {
      this.data = [];
      EventService.modulelist().then((ret) => {
        for (let i = 0; i < ret.list.length; i++) {
          let rolemenu = {};
          rolemenu.key = i;
          rolemenu.id = ret.list[i].moduleId;
          rolemenu.name = ret.list[i].moduleName;
          rolemenu.type = ret.list[i].type;
          rolemenu.description = ret.list[i].description;
          this.data.push(rolemenu);
        }
      });
    },
    cancel(value) {
      const that = this;
      if (value) {
        const form = this.$refs.modal.form;
        form.validateFields((err, values) => {
          if (err) {
            return;
          }
          EventService.addModule(
            values.moduleid,
            values.modulename,
            values.moduleparameter
          ).then((ret) => {
            EventService.editCallBack(ret, ret.msg);
            that.show();
            that.visible = false;
            console.log(ret);
          });
        });
      } else {
        that.visible = false;
      }
    },
    moduleadd(record) {
      this.visible = true;
      this.rowData = String(record.key);
      if (record !== "") {
        this.next = this.data[record.key];
      } else {
        this.next = {};
      }
    },
    moduledelete(record) {
      const that = this;
      this.$confirm({
        title: "删除模块?",
        content: (h) => (
          <div style="color:red;">确定要删除{record.id}模块吗？</div>
        ),
        onOk() {
          EventService.moduledelete(record.id).then((ret) => {
            EventService.editCallBack(ret, ret.msg);
            that.show();
          });
        },
        onCancel() {},
        class: "test",
      });
    },
    vsubSet(arr1, arr2) {
      let set1 = new Set(arr1);
      let set2 = new Set(arr2);
      let subset = [];
      for (let item of set1) {
        if (!set2.has(item)) {
          subset.push(item);
        }
      }
      return subset;
    },
  },
};
</script>

<style lang="less" scoped></style>
