<!--
 * @Author: your name
 * @Date: 2021-08-19 16:57:47
 * @LastEditTime: 2021-12-17 11:31:43
 * @LastEditors: Please set LastEditors
 * @Description:  这个是段伪代码，在此不可运行，只供代码参考
 * @FilePath: \macc.admin.vue\src\views\monitor\devices\index.vue
-->
<template>
  <div>
    <div>
      <div>ant树</div>
      <a-tree :tree-data="treeData" :expandedKeys.sync="expandedKeys">
        <template #status="nodeData">
          <a-button type="primary">{{ nodeData.title }}</a-button>
          <!-- <p>{{ title }}}</p> -->
        </template>
      </a-tree>
    </div>
    <div class="devices">
      <div>这是设备列表页hhhhhhhhhhhhhhhhhhhhhh</div>
      交换机
      <icon-font type="ic-ic-port-POEabnormal" style="font-size: 50px; color: #f00"></icon-font>
      <devicePanel :panel-data="switchData.panelData" :common-type="'SWITCH'" :sn="'G1MWA5N011196'" :select-port="portselect"
        @childSelectPort="selectPort" ref="switchPanel">
        <template slot="legend"> 测试交换机图例 </template>
        <template slot="ports" slot-scope="{ port, index, sn }">
          <icon-font :type="
              port.mediumType == 'Fiber' ? 'ic-port-electric' : 'ic-port-light'
            " :rotate="index % 2 == 0 ? 0 : 180" :class="['port-icon', checkSpeed(port, sn), checkDisable(port, sn)]" />
        </template>
      </devicePanel>
      网关
      <devicePanel :panel-data="gatewayData.panelData" :common-type="'GATEWAY'" :sn="'H1LS33C000216'" :select-port="portselect"
        @childSelectPort="selectPort" class="gateway-panel">
        <template slot="legend"> 网关图例测试 </template>
        <template slot="ports" slot-scope="{ port, index, sn }">
          <icon-font :type="
              port.mediumType == 'Fiber' ? 'ic-port-electric' : 'ic-port-light'
            " :rotate="index % 2 == 0 ? 0 : 180" :class="['port-icon', checkSpeed(port, sn), checkDisable(port, sn)]"
            :disabled="true" />
        </template>
      </devicePanel>

      <topo :topo-data="topoData" :width="700" :height="500" :elem-id="'test' + this.$store.state.Group.groupId"
        :option="topOptions" ref="topo" @renderSuccess="handelrenderSuccess" />
    </div>
  </div>
</template>

<script>
const treeData = [
  {
    title: "parent 1",
    key: "0-0",
    scopedSlots: { title: "status" },
  },
];

import { getDevicePanelLayout } from "@utils/deviceTools";
import { devicePanel, topo } from "@components";
import { Switch, Gateway } from "@/test/devicePanel/index";

export default {
  mixins: [topoDataMethods],
  data () {
    return {
      portselect: [],
      topOptions: {
        filterClass: ["AP_GROUP"], //过滤不可选中的设备类型
        multipleSelect: false, //是否可以多选节点
        //拓扑节点设备拦截规则,$node为dom对象，d为节点数据,返回true为不可选中，返回false为可以选中
        filterClickCallback: function ($node, d) {
          console.log("进来了");
          debugger;
          //console.log("点击拦截",d,d.onlineStatus !="ON")
          return true;
        },
      },
      switchData: {
        panelData: [],
        portDataMap: {},
      },
      gatewayData: {
        panelData: [],
        portDataMap: [],
      },
      treeData,
      expandedKeys: ["0-0-0", "0-0-1"],
    };
  },
  components: {
    devicePanel,
    topo,
  },
  async mounted () {
    this.initTopo(false);

    this.initPanel();
    //获取设备布局
    const slotsLayout = await getDevicePanelLayout(
      "1534567890329", //设备序列号
      "NBS7006", //设备型号
      this.$store //store缓存
    );
    console.log(slotsLayout);
  },
  methods: {
    async initPanel () {
      const switchData = await this.getSwitchPanelData();
      this.switchData.panelData = [...switchData.panelData];
      this.switchData.portDataMap = this.formatterPanelDataMap(
        switchData.portsList
      );

      const gatewayData = await this.getGatewayPanelData();
      this.gatewayData.panelData = [...gatewayData.panelData];
      this.gatewayData.portDataMap = this.formatterPanelDataMap(
        gatewayData.portsList
      );
    },
    formatterPanelDataMap (portData) {
      const renderData = {};
      for (let i = 0; i < portData.length; i++) {
        const item = portData[i];
        let orderId = "";
        if (
          item.devOrder != undefined &&
          item.slotOrder != undefined &&
          item.order != undefined
        ) {
          //交换机数据格式
          orderId = `${item.devOrder}_${item.slotOrder}_${item.order}`;
        } else {
          //网关端口列表
          orderId = `${item.port}`;
        }

        const itemData = { mediumType: item.mediumType };

        itemData.portData = item;

        renderData[orderId] = itemData;
      }
      return renderData;
    },
    /**
     * @description: 设置端口状态
     * @param {*} port
     * @return {*}
     */
    checkDisable (port, sn) {
      let disableClass = "";

      let portData;
      if (sn === "G1MWA5N011196") {
        portData = this.switchData.portDataMap[port.portorder];
      } else {
        portData = this.gatewayData.portDataMap[port.portorder];
      }
      //判断
      if (portData) {
        //判断光电复用口
        if (portData.mediumType != port.mediumType) {
          disableClass = "disbaled";
        }
        //判断端口是否关闭
        if (portData.portData.enable == "false") {
          disableClass = "disbaled";
        }
      } else {
        disableClass = "disbaled";
      }

      return disableClass;
    },
    /**
     * @description: 设置端口颜色
     * @param {*} port
     * @return {*}
     */
    checkSpeed (port, sn) {
      let speedClass = "";

      if (sn === "G1MWA5N011196") {
        const portSwInfo = this.switchData.portDataMap[port.portorder];
        speedClass = this.formatterSwSpeed(portSwInfo);
      } else {
        const portGwInfo = this.gatewayData.portDataMap[port.portorder];
        speedClass = this.formatterGwSpeed(portGwInfo);
      }
      return speedClass;
    },
    formatterSwSpeed (portInfo) {
      let speedClass = "";
      if (portInfo && portInfo.portData.enable == "true") {
        const speed = portInfo.portData.speed;
        const status = portInfo.portData.status;
        if (status == "Up") {
          if (["1000M", "2500M", "10G", "20G"].indexOf(speed) > -1) {
            speedClass = "green";
          } else {
            speedClass = "yellow";
          }
        } else if (status == "Down") {
          //portObject.bgClass = "black";
          speedClass = "black";
        } else if (status == "Dormant") {
          //portObject.bgClass = "black";
        } else if (status == "Unknown") {
          speedClass = "red";
        }
      }

      return speedClass;
    },
    formatterGwSpeed (portInfo) {
      let speedClass = "";
      if (
        portInfo.portData.linestatus == "true" &&
        portInfo.portData.adminstatus == "true"
      ) {
        const speed = portInfo.portData.speed;
        if (["1000M", "2500M", "10G", "20G"].indexOf(speed) > -1) {
          speedClass = "green";
        } else {
          speedClass = "yellow";
        }
      } else if (portInfo.portData.adminstatus != "true") {
        //关闭的端口
        speedClass = "disbaled";
      } else {
        speedClass = "black";
      }
      return speedClass;
    },
    /**
     * @description: 处理选中接口
     * @param {*} data
     * @return {*}
     */
    selectPort (data) {
      console.log("父组件获取的选中接口", data.selectPorts);
      this.portselect = data.selectPorts;
      this.getSelectPortData(data.sn);
    },
    /**
     * @description: 获取选中接口数据
     * @param {*}
     * @return {*}
     */
    getSelectPortData (sn) {
      const selectPortData = this.portselect.map((item) => {
        if (sn == "G1MWA5N011196") {
          return this.switchData.portDataMap[item].portData;
        } else {
          return this.gatewayData.portDataMap[item].portData;
        }
      });
      console.log("选中得接口数据", selectPortData);
    },
    getSwitchPanelData () {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ ...Switch });
        }, 200);
      });
    },
    getGatewayPanelData () {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({ ...Gateway });
        }, 200);
      });
    },
    handelrenderSuccess () {
      const $svg = this.$refs.topo.BASE.svg;
      console.log("******拓扑加载完毕******", this.$refs.topo, $svg);
      debugger;
      //d3.js API查看https://github.com/mbostock/d3/wiki/API--%E4%B8%AD%E6%96%87%E6%89%8B%E5%86%8C
      $svg
        .select("#node_NACK0047H0007") //拓扑节点命名规则为node_设备序列号
        .classed("test", true); //d3.js语法，给dom节点添加一个class

      //给设备添加个标签
      $svg
        .select("#node_NACK0047H0007")
        .classed("enabledFlage", true)
        .append("svg:foreignObject")
        .classed("dhcpsnoopingflage", true)
        .attr("width", 50)
        .attr("height", 20)
        .attr("transform", `translate(160,-10)`).html(`
          <div>开启</div>
      `);
    },
  },
};
</script>

<style lang="less" scoped>
.devices {
  height: 100%;
  width: 100%;
  background: aquamarine;
}

.port-icon {
  color: @portBlack;
  font-size: 20px;
  margin: 0px 5px;
  &.green {
    color: @portGreen;
  }
  &.yellow {
    color: @portYellow;
  }
  &.red {
    color: @portRed;
  }
  &.disbaled {
    color: @portDisabled;
  }
}
</style>
<style scoped>
.gateway-panel >>> .port {
  width: 50px;
}
</style>
<style>
.test .devices-box {
  fill: aqua;
}
.dhcpsnoopingflage {
  border: 1px solid #000;
  background-color: #fff;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.5);
}
</style>
