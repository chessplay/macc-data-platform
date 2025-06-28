<!--
 * @Author: wulongjiang
 * @Date: 2022-01-14 10:54:25
 * @LastEditors: wulongjiang
 * @LastEditTime: 2022-01-20 16:36:39
 * @Description: 
 * @FilePath: \baichuan-web\src\project\views\intelligentConfig\components\configLogDetails.vue
-->
<template>
  <div style="border: 1px solid #f0f0f0;">
    <div
      style="text-align:right;padding:13px;border-bottom: 1px solid #f0f0f0;"
    >
      <a-radio-group
        default-value="current"
        v-model="isCurrent"
        button-style="solid"
        @change="onChange"
      >
        <a-radio-button value="current">
          当前
        </a-radio-button>
        <a-radio-button value="history">
          历史
        </a-radio-button>
      </a-radio-group>
    </div>
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      :rowKey="
        (record, index) => {
          return index;
        }
      "
      @change="onPageChange"
    ></a-table>
  </div>
</template>

<script>
import Service from "@p/services/configService";

const columns = [
  {
    title: "配置项",
    dataIndex: "conf_item",
    key: "conf_item",
  },
  {
    title: "执行状态",
    dataIndex: "status",
    key: "status",
  },
  {
    title: "日志ID",
    dataIndex: "id",
    key: "id",
  },
  {
    title: "消息",
    dataIndex: "error_msg",
    key: "error_msg",
  },
];
export default {
  props: {
    sn: {
      typeof: String,
    },
  },
  data() {
    return {
      data: [], //日志表的数据
      columns, //table
      pagination: {
        totalCount: 0, //分页的总数
      },
      isCurrent: "current", //选择当前还是历史
    };
  },
  watch: {
    sn: {
      async handler(newV, oldV) {
        if (!newV) {
          this.data = [];
          return;
        }
        this.getConfigLog(
          `scene-conf-configlog-${this.isCurrent}`,
          newV,
          1,
          10
        );
      },
    },
  },
  methods: {
    /**
     * @description: 切换当前还是日式的回调
     * @param {*} a
     * @return {*}
     */

    onChange(a) {
      this.getConfigLog(
        `scene-conf-configlog-${this.isCurrent}`,
        this.sn,
        1,
        10
      );
    },
    /**
     * @description: 调取接口拿到数据的方法
     * @param {*} modelId 模型id
     * @param {*} sn sn
     * @param {*} pageIndex 页码
     * @param {*} pageSize  页数
     * @return {*}
     */

    async getConfigLog(modelId, sn, pageIndex, pageSize) {
      const { code, dataList, msg, totalCount } = await Service.queryDynamic(
        modelId,
        {
          sn,
        },
        pageIndex,
        pageSize
      );
      if (code == 0) {
        this.data = dataList;
        this.pagination.totalCount = totalCount;
      } else {
        this.$message.warn(msg);
      }
    },
    /**
     * @description: 分页
     * @param {*} pagination
     * @return {*}
     */

    onPageChange(pagination) {
      const pager = { ...this.pagination };
      pager.current = pagination.current;
      this.pagination = pager;
      this.getConfigLog(
        `scene-conf-configlog-${this.isCurrent}`,
        this.sn,
        pagination.current,
        10
      );
    },
  },
};
</script>

<style></style>
