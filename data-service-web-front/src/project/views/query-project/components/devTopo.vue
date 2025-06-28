<!-- /*
 * @Author: lilinlin
 * @Date: 2021-12-20 11:19:52
 * @LastEditTime: 
 * @LastEditors: Please set LastEditors
 * @Description: 新设备拓扑
 * @FilePath: \baichuan-web\src\project\components\topology\services\index.js
 */ -->
<template>
  <topo
    class="topo"
    :topo-data="topoData"
    :width="topoWH.W"
    :height="topoWH.H"
    :elem-id="'test' + gid"
    :option="topOptions"
    ref="topo"
  >
  </topo>
</template>

<script>
import topoDataMixin from "@p/components/topology/topoData-mixin.js";
import topo from "@p/components/topology/topo.vue";
import Service from "@p/components/topology/services/index.js";
import { mapMutations } from "vuex";
export default {
  mixins: [topoDataMixin],
  name: "devTopo",
  components: {
    topo,
  },
  props: {
    gid: {
      typeof: String,
    },
    deviceModel: {
      typeof: String,
    },
    topoWH: {
      typeof: Object,
    },
  },
  data() {
    return {
      group_id: "",
      withWiredTerminal: true,
      tempTopoData: {},
      devicesName: [],
    };
  },
  watch: {
    gid(v) {
      if (v) {
        this.group_id = v;
        // 在vuex储存group_id
        this.SET_GROUP_ID(this.group_id);
        //this.initTopo(true);
        this.init(this.group_id);
      }
    },
  },
  async activated() {
    debugger;
    if (this.gid) {
      //this.initTopo(true);
      await this.init(this.group_id);
    }
  },
  methods: {
    ...mapMutations({
      SET_GROUP_ID: "devices/SET_GROUP_ID",
    }),
    /**
     * @description:初始化拓扑
     * @param
     * @returns
     */
    init(gid) {
      this.initTopo(this.withWiredTerminal, gid);
      // this.devicesName = [];
      // this.getDevicesName(this.tempTopoData.children, this.devicesName);
      // await this.getTopoDevicePanelPortsData(this.devicesName);
      // await this.formatterTopoData(this.tempTopoData, this.portsMap);
      // this.topoData = this.tempTopoData;
    },

    /**
     * @description:获取返回数据中的设备名字
     * @param
     * @returns
     */
    getDevicesName(data, arr) {
      for (let item of data) {
        arr.push(item.deviceSn);
        if (item.children && item.children.length) {
          this.getDevicesName(item.children, arr);
        }
      }
      return arr;
    },
    /**
     * @description:获取拓扑数据
     * @param {boolean} [withWiredTerminal=false]
     * @returns
     */
    async devGetTopoData(withWiredTerminal, gid) {
      // this.group_id = gid;
      let res = await Service.getTopoData(withWiredTerminal, gid);
      if (res.data) {
        this.tempTopoData = res.data;
      } else {
        this.$message.error(res.msg);
      }
    },
  },
};
</script>
<style scoped></style>
