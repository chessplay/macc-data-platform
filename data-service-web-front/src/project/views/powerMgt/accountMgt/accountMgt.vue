<template>
  <div class="dc-page-card">
    <a-table :columns="columns" :data-source="accountdata" bordered>
      <span slot="accountaction" slot-scope="text, record">
        <button class="dc-tb-btn-edit" @click="accountedit(record)">
          <a-icon type="edit" />
        </button>
        <button class="dc-tb-btn-del" @click="accountdelete(record)">
          <a-icon type="delete" />
        </button>
      </span>
    </a-table>
    <Accountaddmodal
      ref="modal"
      :visible="visible"
      :columns="columns"
      :rowData="rowData"
      :accountnext="accountnext"
      @success="cancel('success')"
      @cancel="cancel"
      :roleidarr="roleidarr"
    ></Accountaddmodal>
  </div>
</template>
<script>
import EventService from "@p/services/eventService";
import Accountaddmodal from "@p/views/powerMgt/accountMgt/accountmodal/accountaddmodal.vue";
import DateUtil from "@/utils/dateUtil.js";
const columns = [
  {
    title: "账号ID",
    dataIndex: "accountid",
    scopedSlots: { customRender: "accountid" },
  },
  {
    title: "账号名称",
    dataIndex: "accountname",
  },
  {
    title: "状态",
    dataIndex: "accountstate",
    customRender: (text, record) => {
      if (text == "ACCESS") return "通过";
      else if (text == "DENY") return "拒绝访问";
      else return "待确认";  
    },
  },
  {
    title: "角色ID",
    dataIndex: "roleid",
  },
  {
    title: "描述",
    dataIndex: "accountdescribe",
  },
  {
    title: "创建时间",
    dataIndex: "accounttime",
  },
  {
    title: "操作",
    dataIndex: "accountaction",
    scopedSlots: { customRender: "accountaction" },
  },
];
export default {
  name: "accountMgt",
  components: {
    Accountaddmodal,
  },
  data() {
    return {
      columns,
      accountdata: [],
      visible: false,
      rowData: "",
      accountnext: {},
      roleidarr: [],
    };
  },
  computed: {},
  methods: {
    show() {
      this.accountdata = [];
      EventService.accountpage(1, 10).then((ret) => {
        for (let i = 0; i < ret.dataList.length; i++) {
          let localdata = {
            key: i,
            accountid: ret.dataList[i].userId,
            accountname: ret.dataList[i].userName,
            accountstate: ret.dataList[i].state,
            roleid: ret.dataList[i].roleId,
            accountdescribe: ret.dataList[i].description,
            accounttime: new Date(ret.dataList[i].createTime).Format(
              "YYYY-MM-DD hh:mm:ss"
            ),
          };
          this.accountdata.push(localdata);
        }
      });
    },
    accountedit(record) {
      this.visible = true;
      this.rowData = String(record.accountname);
      if (record != "") {
        this.accountnext = this.accountdata[record.key];
      } else {
        this.accountnext = {};
      }
    },
    accountdelete(record) {
      const that = this;
      this.$confirm({
        title: "删除账号信息?",
        content: (h) => (
          <div style="color:red;">
            确定要删除 [ {record.accountname} ] 的账号信息吗？
          </div>
        ),
        onOk() {
          EventService.accountdelete(record.accountid).then((ret) => {
            EventService.editCallBack(ret, ret.msg);
            that.show();
          });
        },
        onCancel() {},
        class: "test",
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
          EventService.accountupdata(
            values.accountid,
            values.accountname,
            values.roleid,
            values.accountstate
          ).then((ret) => {
            EventService.editCallBack(ret, ret.msg);
            this.show();
            form.resetFields();
            that.visible = false;
          });
        });
      } else {
        that.visible = false;
      }
    },
    stateshow() {
      //拿到所有的设备类型
      this.roleidarr = [];
      EventService.rolelist().then((ret) => {
        this.roleidarr = ret.list;
      });
    },
  },
  created() {},
  mounted() {
    this.show();
    this.stateshow();
  },
};
</script>

<style lang="less" scoped></style>
