<!--
 * @Author: wulongjiang
 * @Date: 2022-01-14 10:54:08
 * @LastEditors: wulongjiang
 * @LastEditTime: 2022-01-20 16:35:52
 * @Description: 
 * @FilePath: \baichuan-web\src\project\views\intelligentConfig\components\deviceList.vue
-->
<template>
  <div>
    <a-card :bodyStyle="{ padding: '0px' }" style="min-width: 180px">
      <div style="padding:8px 10px;border-bottom: 1px solid #f0f0f0;">
        配置耗时：{{ Math.floor(confItem.conf_usetime) || 0 }}秒<br />
        等待耗时：{{ Math.floor(confItem.ready_usetime) || 0 }}秒
      </div>
      <a-table
        :columns="columns"
        :data-source="data"
        :customRow="onClickRow"
        :rowKey="
          (record, index) => {
            return index;
          }
        "
        :pagination="false"
        style="height:80vh;overflow:hidden"
        :scroll="{ y: 'calc(80vh - 53px)' }"
        bordered
      >
        <template slot="cosTime" slot-scope="item">
          <div
            :style="
              item.cosTime >= 100 && item.cosTime < 300
                ? 'color:purple;text-align:center;'
                : item.cosTime >= 300
                ? 'color:red;text-align:center;'
                : 'text-align:center'
            "
          >
            {{ item.cosTime }}
          </div>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script>
const columns = [
  {
    title: "sn",
    dataIndex: "sn",
    key: "sn",
    align: "center",
  },
  {
    title: "产品型号",
    dataIndex: "product_type",
    key: "product_type",
  },
  {
    title: "配置耗时",
    key: "cosTime",
    scopedSlots: { customRender: "cosTime" },
  },
];
import moment from "moment";
import Service from "@p/services/configService";
export default {
  props: {
    confItem: Object,
  },
  data() {
    return {
      data: [], //显示的数据
      current: 0, //当前选择的索引
      columns, //table
    };
  },
  watch: {
    confItem: {
      deep: true,
      async handler(newV, oldV) {
        if (!Object.keys(newV).length) {
          this.data = [];
          return;
        }
        const { code, list, msg } = await Service.queryDynamic(
          "scene-conf-device-list",
          {
            groupId: newV.groupId,
            startConfTime: newV.startConfTime,
            endConfTime: newV.endConfTime,
          }
        );
        if (code == 0) {
          this.data = list;
          if (this.data.length) {
            this.changeItem(this.data[0].sn, 0);
          }
        } else {
          this.$message.warn(msg);
        }
      },
    },
  },
  methods: {
    moment,
    /**
     * @description: 点击行事件
     * @param {*} record
     * @param {*} index
     * @return {*}
     */

    onClickRow(record, index) {
      return {
        style: {
          cursor: "pointer",
          // 字体颜色
          color: index === this.current ? "#2b6afd" : "",
          // 背景颜色
          "background-color": index === this.current ? "#f0f0f0" : "",
        },
        on: {
          click: (event) => {
            // 当前点击的行
            this.changeItem(record.sn, index);
          },
        },
      };
    },
    /**
     * @description: 切换当前选择时，将数据sn传给父组件
     * @param {*} sn sn
     * @param {*} index 索引
     * @return {*}
     */
    changeItem(sn, index) {
      this.current = index;
      this.$emit("changeDevice", sn);
    },
  },
  mounted() {},
};
</script>

<style scoped></style>
