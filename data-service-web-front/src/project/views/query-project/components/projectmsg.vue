<template>
  <div class="promsg">
    <!-- 项目详情展示盒子 -->
    <div class="tablebox">
      <div v-if="Object.keys(testdata).length !== 0">
        <a-descriptions :column="4" bordered>
          <a-descriptions-item
            v-for="(value, key, index) in itemlist"
            :key="index"
            :label="value.name"
            :span="2"
          >
            <div v-if="value.type === 'string'">
              {{ testdata[key] }}
            </div>
            <div v-if="value.type === 'time'">
              {{ getTime(testdata[key]) }}
            </div>
            <div v-else-if="value.type === 'bool'">
              <a-checkbox
                name="type"
                :checked="testdata[key] == 'yes'"
              ></a-checkbox>
            </div>
            <div
              v-else-if="value.type === 'list'"
              v-html="
                testdata[key]
                  .replace(new RegExp(',', 'g'), ',<br/>')
                  .replace(new RegExp(':', 'g'), '　')
                  .replace(new RegExp('_', 'g'), '　')
              "
            ></div>
          </a-descriptions-item>
        </a-descriptions>
      </div>
      <a-empty v-else class="nullData" />
    </div>
  </div>
</template>
<script>
import ApiService from "@/services/baseService";
import moment from "moment";

export default {
  props: {
    gid: {
      typeof: Number,
    }, //由父组件传入的项目id
  },
  data() {
    return {
      testdata: {}, //项目详情
      itemlist: {
        group_name: { name: "项目名称：", type: "string" },
        building_id: { name: "网络ID：", type: "string" },
        network_create_time: { name: "网络创建时间：", type: "time" },
        create_time: { name: "解析日期：", type: "time" },
        classify_c1: { name: "场景类型：", type: "string" },
        gw_count: { name: "网关数量：", type: "string" },
        gw_type: { name: "网关型号：", type: "list" },
        gw_type_check: { name: "网关类型：", type: "string" },
        gw_subinterface_count: { name: "网关子接口数量：", type: "string" },
        gw_interface_layer3_count: { name: "网关三层口数量：", type: "string" },
        gw_vlan1: { name: "网关是否配置vlan1：", type: "bool" },
        sw_dhcp: { name: "交换是否配置dhcp：", type: "bool" },
        sw_dhcp_replay: { name: "交换是否配置DHCP中继：", type: "bool" },
        gw_interface_info: { name: "网关接口信息：", type: "list" },
        gw_vlan_dhcp: { name: "网关地址池情况：", type: "list" },
        gw_vlan_list: { name: "网关子接口vlan：", type: "list" },
        gw_2_sw_info: { name: "网关下联交换机：", type: "list" },
        sw_mult_vlan_type: { name: "配置多vlan交换机：", type: "list" },
        sw_mult_vlan_list: { name: "交换配置的vlan：", type: "list" },
        sw_2_sw_info: { name: "核心下联交换机：", type: "list" },
      }, //项目详情项对应名称
      filedata: {
        modelId: "rui-network-label",
        filters: [
          {
            alias: "groupid",
            op: "EQ",
            value: [],
          },
          {
            alias: "create_time",
            op: "LTE",
            value: [],
          },
        ],
        limit: 1,
      }, //获取项目详情的data-row
    };
  },
  watch: {
    /**
     * 监听由父组件传入的项目id:
     * @param {String} val 项目id
     */
    gid(val) {
      if (val == "") {
        this.testdata = {};
      } else {
        this.testdata = {};
        this.filedata.filters[0].value = [val];
        this.filedata.filters[1].value = [
          moment(new Date()).format("YYYY-MM-DD"),
        ];
        this.querypro();
      }
    },
  },
  methods: {
    /**
     * 获取项目详情
     */
    querypro() {
      let groupId = Number(this.gid);
      ApiService.queryDynamic("rui-network-label", { groupId }).then((ret) => {
        if (ret.data == null) {
          this.testdata = {};
        } else {
          this.testdata = ret.data;
        }
      });
    },
    /**
     * 格式化时间
     *@param {String} time 原始时间
     *@return {String} newtime 新格式时间
     */
    getTime(time) {
      return moment(new Date(time)).format("YYYY-MM-DD HH:mm");
    },
  },
  created() {},
};
</script>
<style scoped lang="less">
.promsg {
  //border: 1px solid rgb(139, 139, 139);
  background-color: #fff;
  width: 100%;
  height: 100%;
}
/deep/ .tablebox {
  padding: 10px;
  border: none;
  & .ant-descriptions-item-label,
  & .ant-descriptions-item-content {
    padding: 1px;
    background-color: rgb(255, 255, 255);
    border-bottom: 1px solid #f0f0f0;
    border-right: none;
    border-top: none;
    border-left: none;
    height: 6vh;
    font-size: 14px;
  }
  & .ant-descriptions-row:last-child > td,
  & .ant-descriptions-row:last-child > th {
    border: none !important;
  }
  & .ant-descriptions-item-label {
    width: 200px;
  }
  & .ant-descriptions-bordered,
  & .ant-descriptions-view {
    border: none;
  }
}
</style>
