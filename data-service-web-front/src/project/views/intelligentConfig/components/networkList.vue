<!--
 * @Author: wulongjiang
 * @Date: 2022-01-14 10:53:39
 * @LastEditors: wulongjiang
 * @LastEditTime: 2022-01-20 16:48:01
 * @Description: 
 * @FilePath: \baichuan-web\src\project\views\intelligentConfig\components\networkList.vue
-->
<template>
  <div>
    <a-card
      title="网络列表："
      :bodyStyle="{ padding: '0px' }"
      style="min-width: 180px"
    >
      <a-list
        size="large"
        :data-source="data"
        style="height: 80vh;padding:0 10px;overflow-y:auto"
        @scroll="scrollMore"
      >
        <a-list-item
          :class="current == index ? 'changeitem' : ''"
          slot="renderItem"
          slot-scope="item, index"
          style="cursor:pointer;display:flex;align-items:center"
          @click="changeItem(item, index)"
        >
          <div
            style="margin-right:10px;width:15px;height:15px;border-radius:50%;background: green;"
            :style="item.abnormal == 'true' ? 'background:red' : ''"
          />
          [{{ item.groupId }}] {{ item.name }}<br />
          {{ moment(item.begin_date).format("YYYY-MM-DD HH:mm:ss") }}
        </a-list-item>
      </a-list>
    </a-card>
  </div>
</template>

<script>
import Service from "@p/services/configService";
import moment from "moment";
export default {
  data() {
    return {
      data: [], //得到的网络数据列表
      pageIndex: 1, //当前的页码数
      params: {}, //存放父组件传来的params参数
      current: -1, //当前选择的网络item的索引
    };
  },
  methods: {
    /**
     * @description:moment库
     * @param {*}
     * @return {*}
     */

    moment,
    /**
     * @description:  获取网络数据列表
     * @param {*} pageIndex 页码
     * @param {*} pageSize 页数
     * @param {*} params 参数
     * @return {*}
     */

    async getGroupIdList(pageIndex, pageSize, params) {
      if (Object.keys(params).length) {
        this.params = params;
        this.pageIndex = 1;
      } else {
        this.params = {};
      }
      let { code, dataList, msg } = await Service.queryDynamic(
        "scene-conf-network-page",
        params,
        pageIndex,
        pageSize
      );
      if (code == 0) {
        if (!dataList.length) {
          this.data = [];
          return;
        }
        const groupIdlist = dataList.map((item) => {
          item.name = "";
          return item.groupId;
        });
        let result = await Service.queryDynamic("macc-network-name", {
          buildingIds: groupIdlist,
        });
        if (result.code == 0) {
          dataList.forEach((i) => {
            let findName = result.list.find((item, index) => {
              return i.groupId == item.groupId;
            });
            if (findName) {
              i.name = findName.name;
            }
          });
          if (pageIndex == 1) {
            this.data = dataList;
          } else {
            this.data = this.data.concat(dataList);
          }
        } else {
          this.$message.warn(result.msg);
        }
      } else {
        this.$message.warn(msg);
      }
    },
    /**
     * @description: 切换网络数据的时候
     * @param {*} item 网络数据的详情
     * @param {*} index 索引
     * @return {*}
     */

    changeItem(item, index) {
      this.current = index;
      this.$emit("changeNetwork", item);
    },
    /**
     * @description: 下拉到底部的时候加载更多
     * @param {*} e
     * @return {*}
     */

    scrollMore(e) {
      if (e.target.scrollTop + e.target.clientHeight >= e.target.scrollHeight) {
        this.pageIndex += 1;
        this.getGroupIdList(this.pageIndex, 20, this.params);
      }
    },
  },
};
</script>

<style scoped>
.changeitem {
  background-color: #f0f0f0;
  border: none;
  color: #2b6afd;
}
</style>
