<template>
  <div>
    <div class="header">
      <span>设备类别：</span>
      <a-select class="type" v-model="type" @change="changeSelect">
        <a-select-option
          v-for="(item, index) in commonType"
          :key="index"
          :value="commonType1[index]"
          >{{ item }}
        </a-select-option>
      </a-select>
      <span>序列号：</span>
      <a-input v-model="number" allow-clear style="width:120px;"></a-input>
      <a-button type="primary" icon="search" @click="query">查询</a-button>
    </div>
    <a-table
      size="middle"
      class="dc-tb-stripe"
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
    ></a-table>
  </div>
</template>
<script>
import TopoService from "@p/services/topoService.js";
const commonType = ["全部", "网关", "交换机", "AC", "AP", "无线路由器", "网桥"];
const commonType1 = ["全部", "GATEWAY", "SWITCH", "AC", "AP", "WR", "BRIDGE"];
const columns = [
  {
    title: "在线状态",
    dataIndex: "onlineStatus",
  },
  {
    title: "设备序列号",
    dataIndex: "serialNumber",
  },
  {
    title: "设备名称",
    dataIndex: "aliasName",
  },
  {
    title: "设备型号",
    dataIndex: "productClass",
  },
  {
    title: "管理地址",
    dataIndex: "localIp",
  },
  {
    title: "出口地址",
    dataIndex: "cpeIp",
  },
  {
    title: "软件版本",
    dataIndex: "softwareVersion",
  },
  {
    title: "最后离线时间",
    dataIndex: "lastOnline",
  },
];
const data = [];
const pagination = {
  pageSize: 10,
  showQuickJumper: true,
  showSizeChanger: true,
};
export default {
  props: {
    gid: String,
  },
  data() {
    return {
      number: "",
      infoData: [],
      commonType,
      commonType1,
      columns,
      data,
      pagination,
      type: commonType[0],
      groupId: "",
    };
  },
  created() {
    this.getData();
  },
  watch: {
    gid(v) {
      if (v) {
        this.getData();
      }
    },
  },
  methods: {
    async getData() {
      this.data = [];
      let dataRow = {
        url: "/service/api/device/network/list",
        httpMethod: "GET",
        queryParams: {
          group_id: this.gid,
        },
      };
      await TopoService.getTopoInfoTable(this.gid, dataRow).then((ret) => {
        this.infoData = ret.list;
        let lastOnline = [];
        if (ret.list) {
          for (let i = 0; i < this.infoData.length; i++) {
            lastOnline.push(this.setTime(this.infoData[i].lastOnline));
            if (this.type === "全部") {
              this.data.push({
                key: i,
                onlineStatus: this.infoData[i].onlineStatus,
                serialNumber: this.infoData[i].serialNumber,
                aliasName: this.infoData[i].aliasName,
                productClass: this.infoData[i].productClass,
                localIp: this.infoData[i].localIp,
                cpeIp: this.infoData[i].cpeIp,
                softwareVersion: this.infoData[i].softwareVersion,
                lastOnline: lastOnline[i],
              });
            } else if (this.type === this.infoData[i].commonType) {
              this.data.push({
                key: i,
                onlineStatus: this.infoData[i].onlineStatus,
                serialNumber: this.infoData[i].serialNumber,
                aliasName: this.infoData[i].aliasName,
                productClass: this.infoData[i].productClass,
                localIp: this.infoData[i].localIp,
                cpeIp: this.infoData[i].cpeIp,
                softwareVersion: this.infoData[i].softwareVersion,
                lastOnline: lastOnline[i],
              });
            }
          }
        }
      });
    },

    getZero(num) {
      if (parseInt(num) < 10) {
        num = "0" + num;
      }
      return num;
    },

    setTime(time) {
      var date = new Date(time);
      if (time) {
        return (
          date.getFullYear() +
          "/" +
          this.getZero(date.getMonth() + 1) +
          "/" +
          this.getZero(date.getDate()) +
          " " +
          this.getZero(date.getHours()) +
          ":" +
          this.getZero(date.getMinutes()) +
          ":" +
          this.getZero(date.getSeconds())
        );
      }
    },

    changeSelect(value) {
      this.type = value;
    },

    async query() {
      await this.getData();
      let holdData = this.data;
      for (let i = 0; i < this.data.length; i++) {
        if (this.data[i].serialNumber === this.number) {
          this.data = [];
          this.data.push(holdData[i]);
        }
      }
    },
  },
};
</script>
<style lang="less" scoped>
.header {
  padding: 10px 0 10px 0;
  span {
    font-size: 14px;
    margin-right: 10px;
  }
  .type {
    width: 120px;
    margin-right: 10px;
  }
  input {
    display: inline;
    margin-right: 10px;
  }
}
</style>
