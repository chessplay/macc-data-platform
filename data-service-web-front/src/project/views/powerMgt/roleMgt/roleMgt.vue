<template>
  <div class="dc-page-card">
    <div class="conf">
      <div class="rolelist">
        <a-button
          icon="plus"
          style="width:100%;border-radius: 0;"
          @click="newlyadd('')"
          >新增角色</a-button
        >
        <div
          id="role"
          v-for="item in data"
          :key="item.key"
          @click="showrelemenu(item)"
          :class="{ version: rolemenukey == item.key }"
        >
          <div class="action">
            <button class="dc-tb-btn-edit" @click="newlyadd(item.key)">
              <a-icon type="edit" />
            </button>
            <button class="dc-tb-btn-del" @click="newlydelete(item.id)">
              <a-icon type="delete" />
            </button>
          </div>
          <div class="roleinformation">
            <span
              ><h5>{{ columns[0].title }}：</h5>
              <p>{{ item.id }}</p></span
            >
            <span
              ><h5>{{ columns[1].title }}：</h5>
              <p>{{ item.name }}</p></span
            >
            <span
              ><h5>{{ columns[2].title }}：</h5>
              <p>{{ item.information }}</p></span
            >
          </div>
        </div>
      </div>
      <Rolemenu
        style="margin-left:15px;width: 30%;"
        :rolemenu="rolemenu"
        :rolemenuname="rolemenuname"
      ></Rolemenu>
      <Rolemodule style="width:40%" :rolemenu="rolemenu"></Rolemodule>
    </div>

    <Newlyadd
      ref="modal"
      :visible="newlyaddvisible"
      :rowData="rowData"
      @success="success"
      @cancel="cancel"
      :columns="columns"
      :next="next"
    ></Newlyadd>
  </div>
</template>
<script>
import EventService from "@p/services/eventService";
import Newlyadd from "@p/views/powerMgt/roleMgt/modal/newlyadd.vue";
import Rolemenu from "@p/views/powerMgt/roleMgt/modal/rolemenu.vue";
import Rolemodule from "@p/views/powerMgt/roleMgt/modal/rolemodule.vue";
const columns = [
  {
    title: "角色ID",
    dataIndex: "id",
  },
  {
    title: "角色名称",
    dataIndex: "name",
  },
  {
    title: "描述信息",
    dataIndex: "information",
  },
];

export default {
  name: "roleMgt",
  components: {
    Newlyadd,
    Rolemenu,
    Rolemodule,
  },
  data() {
    return {
      columns,
      data: [],
      rowData: "",
      newlyaddvisible: false,
      next: {},
      rolemenu: "super_admin",
      rolemenuname: "超级管理员",
      rolemenukey: 0,
    };
  },
  computed: {},
  mounted() {
    this.show();
  },
  methods: {
    show() {
      this.data = [];
      EventService.rolelist().then((ret) => {
        let newdata = [];
        for (let i = 0; i < ret.list.length; i++) {
          let sum = {};
          sum.key = i;
          sum.id = ret.list[i].roleId;
          sum.name = ret.list[i].roleName;
          (sum.information = ret.list[i].description), newdata.push(sum);
        }
        this.data = newdata;
      });
    },
    newlyadd(record) {
      this.newlyaddvisible = true;
      this.rowData = String(record);
      if (record !== "") {
        this.next = this.data[record];
      } else {
        this.next = {};
      }
    },
    newlydelete(item) {
      const that = this;
      this.$confirm({
        title: "删除角色?",
        content: (h) => (
          <div style="color:red;">确定要删除{item}该角色吗？</div>
        ),
        onOk() {
          EventService.roledelete(item).then((ret) => {
            EventService.editCallBack(ret, ret.msg);
            that.show();
          });
        },
        onCancel() {},
        class: "test",
      });
    },
    showrelemenu(value) {
      this.rolemenukey = value.key;
      this.rolemenu = value.id;
      this.rolemenuname = value.name;
    },
    success() {
      const that = this;
      const form = this.$refs.modal.form;
      form.validateFields((err, values) => {
        if (err) {
          return;
        }
        EventService.postroleinfo(
          values.id,
          values.name,
          values.information
        ).then((ret) => {
          EventService.editCallBack(ret, ret.msg);
          that.show();
          this.newlyaddvisible = false;
        });
      });
    },
    cancel() {
      this.newlyaddvisible = false;
    },
  },
};
</script>

<style lang="less" scoped>
.conf {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
}
.rolelist {
  width: 30%;
  max-height: 750px;
  overflow: scroll;
  #role {
    width: 100%;
    display: flex;
    margin-top: 10px;
    background-color: #f0f2f5;
    border: 2px solid rgba(24, 144, 255, 0);
    transition: 0.3s;
    .action {
      display: flex;
      flex-direction: column;
      width: 15%;
      button {
        height: 50%;
      }
      button:hover {
        background-color: #fff;
      }
    }
    .roleinformation {
      span {
        display: flex;
      }
    }
  }
  #role:hover {
    border: 2px solid #1890ff;
    cursor: pointer;
  }
}
.version {
  border: 2px solid rgba(24, 144, 255) !important;
}
</style>
