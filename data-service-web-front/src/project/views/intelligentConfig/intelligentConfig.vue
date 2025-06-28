<!--
 * @Author: wulongjiang
 * @Date: 2022-01-14 10:39:25
 * @LastEditors: wulongjiang
 * @LastEditTime: 2022-01-20 16:43:34
 * @Description: 极速智能配置入口页面
 * @FilePath: \baichuan-web\src\project\views\intelligentConfig\intelligentConfig.vue
-->
<template>
  <div class="dc-page-card">
    <!-- 搜索盒子 -->
    <div style="text-align:right;margin-bottom:10px">
      <a-form layout="inline">
        <a-form-item label="网络ID：">
          <a-input style="width: 120px;" v-model.number="groupId" />
        </a-form-item>
        <a-form-item label="用户ID：">
          <a-input style="width: 120px;" v-model.number="userId" />
        </a-form-item>
        <a-form-item label="时间：">
          <a-range-picker
            :showTime="{
              defaultValue: [
                moment('00:00:00', 'HH:mm:ss'),
                moment('23:59:59', 'HH:mm:ss'),
              ],
            }"
            format="YYYY/MM/DD HH:mm:ss"
            style="width: 320px;text-align:center"
            v-model="dateChoose"
          />
        </a-form-item>
        <a-form-item label="配置耗时大于：">
          <a-input style="width: 120px;" v-model.number="configUseTime" />
        </a-form-item>
        <a-form-item>
          <a-button @click="submit" type="primary">搜索</a-button>
        </a-form-item>
      </a-form>
    </div>
    <!-- 列表盒子 -->
    <div style="display:flex">
      <!-- 网络列表 -->
      <Network-List ref="networkList" @changeNetwork="changeNetwork" />
      <!-- 设备列表 -->
      <Device-List :confItem="confItem" @changeDevice="changeDevice" />
      <!-- 配置日志详情 -->
      <Config-Log-Details :sn="sn" style="flex:0 0 55%" />
    </div>
  </div>
</template>

<script>
import moment from "moment";
import ConfigLogDetails from "./components/configLogDetails.vue";
import DeviceList from "./components/deviceList.vue";
import NetworkList from "./components/networkList.vue";
export default {
  components: {
    ConfigLogDetails,
    DeviceList,
    NetworkList,
  },
  data() {
    return {
      confItem: {}, //当前点击的网络列表的配置信息
      groupId: "", //输入框的网络ID
      userId: "", //输入框的用户ID
      sn: "", //当前选择的设备列表（组件deviceList）的SN
      dateChoose: [
        //输入框日期的事件
        moment("00:00:00", "HH:mm:ss"),
        moment("23:59:59", "HH:mm:ss"),
      ],
      configUseTime: "", //输入框配置耗时
    };
  },
  mounted() {
    this.submit();
  },
  methods: {
    /**
     * @description: moment库  时间处理库
     * @param {*}
     * @return {*}
     */

    moment,
    /**
     * @description: 当点击切换网络列表时
     * @param {*} item 子组件传来的值
     * @return {*}
     */

    changeNetwork(item) {
      this.confItem = item;
    },
    /**
     * @description: 当点击切换设备列表时
     * @param {*} sn 子组件传来的值
     * @return {*}
     */

    changeDevice(sn) {
      this.sn = sn;
    },
    /**
     * @description: 点击搜索按钮
     * @param {*}
     * @return {*}
     */

    async submit() {
      this.sn = "";
      this.confItem = {};
      let { groupId, userId, configUseTime, dateChoose } = this;
      let startTime = new Date(dateChoose[0]).getTime();
      let endTime = new Date(dateChoose[1]).getTime();
      let params = {
        groupId,
        userId,
        startTime,
        endTime,
        configUseTime,
      };
      for (const key in params) {
        if (Object.hasOwnProperty.call(params, key)) {
          if (!params[key]) {
            delete params[key];
          }
        }
      }
      this.$refs.networkList.getGroupIdList(1, 25, params);
    },
  },
};
</script>

<style></style>
