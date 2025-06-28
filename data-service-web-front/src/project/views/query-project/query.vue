<template>
  <div>
    <!-- 过滤条件展示盒子 -->
    <query-filter class="queryfilter">
      <div slot="up">
        <a-form layout="inline">
          <a-form-item class="labelclass" label="条件">
            <a-select
              @change="clickTerm"
              style="width:200px;padding:4px 11px"
              v-model="termForm.termName"
            >
              <a-select-option value="ruiyi2.0"
                >使用睿易一体化2.0</a-select-option
              >
              <a-select-option value="vlan">配置多vlan</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item class="labelclass" label="项目ID" style="">
            <a-input
              style="width: 120px;"
              v-model="termForm.groupId"
              allow-clear
            />
          </a-form-item>
          <a-form-item
            class="labelclass"
            label="macc用户ID"
            v-show="termForm.termName !== 'vlan'"
          >
            <a-input
              style="width: 120px;"
              v-model="termForm.maccUserId"
              allow-clear
            />
          </a-form-item>
          <a-form-item
            class="labelclass"
            label="设备ID"
            v-show="termForm.termName !== 'vlan'"
          >
            <a-input
              style="width: 120px;"
              v-model="termForm.deviceId"
              allow-clear
            />
          </a-form-item>
          <a-form-item label="分类" v-show="termForm.termName !== 'vlan'">
            <div class="labelclass" style="width:200px;padding:4px 11px">
              <a-select
                @change="clickType1"
                style="padding:0 15px 0 0; width:100px;"
                v-model="termForm.type1"
              >
                <a-select-option value="">全部</a-select-option>
                <a-select-option
                  :key="i"
                  :value="k"
                  v-for="(v, k, i) in type"
                  >{{ k }}</a-select-option
                >
              </a-select>
              <a-select v-model="termForm.type2" style=" width:100px;">
                <a-select-option value>全部</a-select-option>
                <a-select-option
                  :key="i"
                  :value="v"
                  v-for="(v, i) in type[type1]"
                  >{{ v }}</a-select-option
                >
              </a-select>
            </div>
          </a-form-item>
          <a-form-item>
            <a-button @click="submait" type="primary">搜索</a-button>
          </a-form-item>
        </a-form>
      </div>
    </query-filter>
    <!-- 详细信息展示盒子 -->
    <div class="showbox">
      <project-list
        ref="prolist"
        style="width:280px"
        v-on:groupId="sendgroupid"
      />
      <a-tabs
        class="tablist"
        default-active-key="1"
        style="width:85vw;font-size: 16px;"
      >
        <a-tab-pane key="1" style="height:80vh;font-size: 16px;" tab="事件列表">
          <event-list :gid="gid" :type="type" />
        </a-tab-pane>
        <a-tab-pane
          :forceRender="true"
          key="2"
          style="height:80vh;font-size: 16px;"
          tab="项目详情"
        >
          <project-msg :gid="gid" />
        </a-tab-pane>
        <a-tab-pane
          :forceRender="true"
          key="3"
          style="height:80vh;font-size: 16px;"
          tab="设备拓扑"
        >
          <a-radio-group class="maccModel" v-model="deviceModel">
            <a-radio-button value="table">表格视图</a-radio-button>
            <a-radio-button value="topo">拓扑视图</a-radio-button>
          </a-radio-group>
          <div style="width:100%;height:100%">
            <div class="table-view" v-show="deviceModel == 'table'">
              <table-view :gid="gid"></table-view>
            </div>
            <div class="topology" v-show="deviceModel == 'topo'" ref="topology">
              <topology
                :topoWH="topoWH"
                :gid="gid"
                :deviceModel="deviceModel"
              />
            </div>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
import ProjectMsg from "./components/projectmsg.vue";
import EventList from "./components/eventlist.vue";
import ProjectList from "./components/projectlist.vue";
import QueryFilter from "./components/queryfilter.vue";
import TableView from "./components/tableView.vue";
import Topology from "./components/devTopo.vue";
import TypeService from "@p/services/typeService";

export default {
  name: "prodetails",
  components: {
    Topology,
    EventList,
    ProjectList,
    QueryFilter,
    ProjectMsg,
    TableView,
  },
  data() {
    return {
      termForm: {
        termName: "ruiyi2.0",
        groupId: "",
        type1: "",
        type2: "",
        deviceId: "",
        maccUserId: "",
        dataRow: {
          modelId: "zg-latest-operate-networks-page",
          filters: [],
          orderby: [
            {
              alias: "begin_date",
              sortType: "desc",
            },
          ],
          skip: 0,
          limit: 25,
        },
      },
      type: {},
      type1: "",
      gid: "",
      deviceModel: "topo",
      topoWH: {
        W: 700,
        H: 400,
      },
    };
  },
  methods: {
    /**
     * 选择条件修改termForm
     */
    clickTerm(value) {
      (this.termForm.maccUserId = ""),
        (this.termForm.deviceId = ""),
        (this.gid = ""),
        (this.termForm.groupId = "");
      if (value == "ruiyi2.0") {
        this.$set(this.termForm, "dataRow", {
          modelId: "zg-latest-operate-networks-page",
          filters: [],
          orderby: [
            {
              alias: "begin_date",
              sortType: "desc",
            },
          ],
          skip: 0,
          limit: 25,
        });
      } else {
        this.$set(this.termForm, "dataRow", {
          modelId: "ruiyi-network-page",
          filters: [],
          skip: 0,
          limit: 25,
        });
      }
      this.$refs["prolist"].getTermFrom(this.termForm);
    },
    clickType1(v) {
      this.type1 = v;
      this.termForm.type2 = "";
    },
    /**
     * 提交表单进行搜索
     */
    submait() {
      // if (
      //   this.termForm.groupId == "" &&
      //   this.termForm.type1 == "" &&
      //   this.termForm.type2 == "" &&
      //   this.termForm.maccUserId == "" &&
      //   this.termForm.deviceId == ""
      // ) {
      //   this.$refreshPage("/dashboard/workplace");
      // } else {
      //   this.termForm.dataRow.skip = 0;
      //   this.$refs["prolist"].getFormList(this.termForm);
      // }
      this.termForm.dataRow.skip = 0;
      this.$refs["prolist"].getFormList(this.termForm);
    },

    /**
     * 由子组件穿过来的gid
     */
    sendgroupid(id) {
      this.gid = id + "";
    },
    /**
     * 获取到type1,type2
     */
    querytype() {
      TypeService.query().then((ret) => {
        let typeset = new Set();
        for (var i in ret.list) {
          typeset.add(ret.list[i].type1);
        }
        for (var j of typeset) {
          //this.$set(type,j,[])
          let arr = [];
          for (var k in ret.list) {
            if (ret.list[k].type1 === j) {
              arr.push(ret.list[k].type2);
            }
          }
          this.$set(this.type, j, arr);
        }
      });
    },
  },
  created() {
    this.querytype();
  },
  mounted() {
    this.topoWH.H = this.$refs.topology.clientHeight;
    this.topoWH.W = this.$refs.topology.clientWidth;
    this.$refs["prolist"].getTermFrom(this.termForm);
  },
};
</script>
<style lang="less">
.queryfilter {
  width: 100%;
  height: 10vh;
  background-color: rgb(255, 255, 255);
  display: flex;
  align-items: center;
  padding: 20px;
}
.showbox {
  display: flex;
  flex-direction: row;
}
.labelclass {
  display: flex;
  justify-content: center;
}
.tablist {
  background-color: #fff;
  margin: 2vh 0vw 0 0;
  height: 100%;
  padding: 11px;
}
/deep/ .showbox {
  & .ant-tabs-nav {
    font-size: 16px;
  }
}
.maccModel {
  position: absolute;
  z-index: 1000;
  right: 40px;
  top: 80px;
  min-width: 100px;
  min-height: 100px;
}
.maccModelTable {
  float: right;
}
.topology {
  height: 100%;
  width: 100%;
  background-image: linear-gradient(
      90deg,
      rgba(220, 220, 220, 0.185) 10%,
      rgba(0, 0, 0, 0) 10%
    ),
    linear-gradient(rgba(220, 220, 220, 0.185) 10%, rgba(0, 0, 0, 0) 10%);
  background-size: 10px 10px;
}

.table-view {
  height: 100%;
  width: 100%;
  overflow: auto;
}
</style>
