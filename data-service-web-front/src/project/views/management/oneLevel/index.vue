<template>
  <div>
    <!-- 详细信息展示盒子 -->
    <div class="showbox">
      <management-list
        ref="prolist"
        style="width: 350px"
        @sendOneLevelData="sendOneLevelData"
      />
      <a-tabs
        class="tablist"
        default-active-key="1"
        style="width: 85vw; font-size: 16px"
      >
        <a-tab-pane
          key="1"
          style="height: 80vh; font-size: 16px"
          tab="员工列表"
        >
          <employee-list :company_id="oneLevelData.cmpny_id"></employee-list>
        </a-tab-pane>
        <a-tab-pane
          :forceRender="true"
          key="3"
          style="height: 80vh; font-size: 16px"
          tab="工程商信息"
        >
          <project-list :company_id="oneLevelData.cmpny_id"></project-list>
        </a-tab-pane>
        <a-tab-pane
          :forceRender="true"
          key="4"
          style="height: 80vh; font-size: 16px"
          tab="地县核信息"
        >
          <twoLevel :company_id="oneLevelData.cmpny_id"></twoLevel>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
import EmployeeList from './employeeList.vue'
import managementList from './managementList.vue'
import projectList from './projectList.vue'
import twoLevel from './twoLevel.vue'
import companyDetail from './companyDetail.vue'
export default {
  name: 'index',
  components: {
    managementList,
    EmployeeList,
    projectList,
    twoLevel,
    companyDetail,
  },
  data() {
    return {
      oneLevelData: {},
    }
  },
  methods: {
    //  获取点击一级渠道后的数据
    sendOneLevelData(item) {
      this.oneLevelData = item
    },
  },
}
</script>
<style lang="less">
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

.table-view {
  height: 100%;
  width: 100%;
  overflow: auto;
}
</style>
