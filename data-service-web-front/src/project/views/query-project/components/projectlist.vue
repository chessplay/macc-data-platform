<template>
  <div class="projectlist">
    <!-- 项目列表展示盒子 -->
    <!-- <div>
      <div @click="changeItem({groupId:24627})">模拟测试1： gid:24627</div>
      <div @click="changeItem({groupId:24628})">模拟测试2： gid:24628</div>
      <div @click="changeItem({groupId:'24681'})">模拟测试2： gid:24681</div>
      <div @click="changeItem({groupId:24553})">模拟测试1： gid:24553 </div>
    </div> -->
    <a-card
      title="项目列表"
      :bordered="false"
      :bodyStyle="{ padding: '20px 0 ', height: '80vh' }"
    >
      <a-list
        item-layout="vertical"
        size="large"
        :data-source="listData"
        v-if="listData.length > 0"
        class="prolist"
        @scroll="scrollGet($event)"
      >
        <a-list-item
          slot="renderItem"
          :key="item.groupId"
          slot-scope="item"
          class="listitem"
          @click="changeItem(item)"
        >
          <div :class="changekey === item.groupId ? 'changeitem' : 'item'">
            {{ [item.groupId] }}:
            <span v-if="(item.groupName + '').length <= 6">{{
              item.groupName
            }}</span>
            <span v-else-if="item.groupName == undefined"></span>
            <span v-else>
              <a-tooltip placement="right">
                <template slot="title">
                  <span>{{ item.groupName }}</span>
                </template>
                <span>{{ (item.groupName + "").substring(0, 6) + "..." }}</span>
              </a-tooltip>
            </span>
            <br />
            <span>
              {{ moment(item.begin_date).format("YYYY-MM-DD hh:mm:ss") }}
            </span>
          </div>
        </a-list-item>
      </a-list>
      <a-empty v-else />
    </a-card>
  </div>
</template>
<script>
import ApiService from "@/services/baseService";
import moment from "moment";

export default {
  data() {
    return {
      listData: [], //项目列表
      termFrom: {}, //表单
      changekey: "", //项目列表选择的标识
      pageIndex: 1, //存放分页的索引
    };
  },
  methods: {
    moment,
    /**
     * 点击项目事件将gid发送到evenlist组件
     * @param {Objrct} item 项目信息
     */
    changeItem(item) {
      this.changekey = item.groupId;
      this.$emit("groupId", item.groupId);
    },
    /**
     * 获取相关的项目列表
     * @param {Objrct} value 传输过来的表单数据
     */
    getFormList(value) {
      this.listData = [];
      let filter = [];
      if (value.groupId !== "") {
        filter.push({
          alias: "groupId",
          op: "EQ",
          value: [value.groupId],
        });
      }
      if (value.termName == "ruiyi2.0") {
        if (value.type1 !== "") {
          filter.push({
            alias: "type1",
            op: "EQ",
            value: [value.type1],
          });
        }
        if (value.type2 !== "") {
          filter.push({
            alias: "type2",
            op: "EQ",
            value: [value.type2],
          });
        }
        if (value.deviceId !== "") {
          filter.push({
            alias: "deviceId",
            op: "EQ",
            value: [value.deviceId],
          });
        }
        if (value.maccUserId !== "") {
          filter.push({
            alias: "userId",
            op: "EQ",
            value: [value.maccUserId],
          });
        }
        this.$set(value.dataRow, "filters", filter);
        this.pageIndex = 1;
        this.queryVlanList(value.dataRow, 1, 25);
      } else {
        this.$set(value.dataRow, "filters", filter);
        this.pageIndex = 1;
        this.queryRuiYiList(value.dataRow, 1, 25);
      }
      this.$emit("groupId", "");
    },
    /**
     * 根据gid获取gname,使gid与gname对应
     * @param {Objrct} dataRow 相应的匹配规则
     * @param {Array} list gid数组
     */
    querylistname(dataRow, list) {
      ApiService.queryDynamic(dataRow.modelId, {
        buildingIds: dataRow.filters[0].value,
      }).then((ret) => {
        for (var i of ret.list) {
          for (var j in list) {
            if (list[j].groupId == i.groupId) {
              this.$set(list[j], "groupName", i.name);
            }
          }
        }
      });
      this.listData = this.listData.concat(list);
    },
    /**
     * 获取相关的项目列表
     * @param {Objrct} value 传输过来的表单数据
     */
    getTermFrom(value) {
      this.termFrom = value;
      this.listData = [];
      this.pageIndex = 1;
      if (value.termName == "ruiyi2.0") {
        this.queryVlanList(value.dataRow, 1, 25);
      } else {
        this.queryRuiYiList(value.dataRow, 1, 25);
      }
      this.changekey = "";
    },
    /**
     * 获取项目列表
     * @param {Objrct} dataRow 相应的匹配规则
     */
    queryRuiYiList(dataRow, pageIndex, pageSize) {
      let params = {};
      dataRow.filters.forEach((item) => {
        if (item.alias == "groupId") {
          params[item.alias] = Number(item.value[0]);
        } else {
          params[item.alias] = item.value[0];
        }
      });
      ApiService.queryDynamic(
        dataRow.modelId,
        params,
        pageIndex,
        pageSize
      ).then((ret) => {
        for (let item of ret.dataList) {
          this.$set(item, "groupId", item.groupId);
        }
        this.listData = this.listData.concat(ret.dataList);
      });
    },
    /**
     * 获取项目列表
     * @param {Objrct} dataRow 相应的匹配规则
     */
    queryVlanList(dataRow, pageIndex, pageSize) {
      let params = {};
      dataRow.filters.forEach((item) => {
        if (item.alias == "groupId" || item.alias == "userId") {
          params[item.alias] = Number(item.value[0]);
        } else {
          params[item.alias] = item.value[0];
        }
      });
      ApiService.queryDynamic(
        dataRow.modelId,
        params,
        pageIndex,
        pageSize
      ).then((ret) => {
        if (ret.dataList.length > 0) {
          let list = ret.dataList;
          let glist = [];
          for (let item of ret.dataList) {
            glist.push(item.groupId);
          }
          let getNameDataRow = {
            modelId: "macc-network-name",
            filters: [
              {
                alias: "groupId",
                op: "IN",
                value: glist,
              },
            ],
          };
          this.querylistname(getNameDataRow, list);
        } else {
          this.listData = this.listData.concat([]);
        }
      });
      this.changekey = "";
    },
    /**
     *滚动获取事件
     *@param {Object} e 页面dom对象
     */
    scrollGet(e) {
      if (e.target.scrollTop + e.target.clientHeight >= e.target.scrollHeight) {
        this.pageIndex++;
        this.termFrom.dataRow.skip = this.listData.length;
        if (this.termFrom.termName == "ruiyi2.0") {
          this.queryVlanList(this.termFrom.dataRow, this.pageIndex, 25);
        } else {
          this.queryRuiYiList(this.termFrom.dataRow, this.pageIndex, 25);
        }
      }
    },
  },
  created() {},
};
</script>
<style scoped lang="less">
.projectlist {
  display: inline-block;
  background-color: #fff;
  margin: 2vh 1vw 0 0;
  width: 14vw;
}
.listitem {
  padding: 0vh 1vw 1vh 1vw;
  border-bottom: none;
}
.item,
.changeitem {
  border-bottom: 1px solid #e8e8e8;
  padding: 0 3px 3px 3px;
  font-size: 14px;
  cursor: pointer;
}
.changeitem {
  background-color: #f0f0f0;
  border: none;
  color: #2b6afd;
}
.prolist {
  overflow-y: scroll;
  height: 80vh;
}
/deep/ .projectlist {
  & .ant-card-head-title {
    padding-bottom: 12px;
    padding-top: 20px;
  }
}
</style>
