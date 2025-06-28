<template>
  <div class="projectList_two_level">
    <!-- <a-table
      class="dc-tb-stripe"
      style="width:300px"
      :rowKey="(row) => row.cmpny_id"
      :columns="columns"
      :data-source="tableData"
      :loading="loading"
      tableLayout="fixed"
    >
    </a-table> -->
    <a-spin :spinning="loading">
      <a-list
        item-layout="vertical"
        size="large"
        :data-source="listData"
        v-if="listData.length > 0"
        class="prolist"
        @scroll="scroll($event)"
      >
        <a-list-item
          slot="renderItem"
          slot-scope="item"
          :key="item.cmpny_name"
          class="listitem"
          :class="{ active: item.cmpny_id == classFlag }"
          @click="changeItem(item)"
        >
          <a-list-item-meta>
            <span slot="title"
              ><span style="font-weight: bold">[{{ item.cmpny_id }}]</span>
              {{ item.cmpny_name }}</span
            >
          </a-list-item-meta>
        </a-list-item>
      </a-list>
      <a-empty v-else />
    </a-spin>
  </div>
</template>

<script>
import Service from '@p/services/managementService'
export default {
  name: 'projectList',
  props: ['company_id'],
  data() {
    return {
      loading: false,
      classFlag: 0,
      listData: [],
      keyword: '',
      isQueryKeyword: '',
      pageIndex: 1,
      totalCount: 0,
      dataIsFull: false,
    }
  },
  watch: {
    company_id(val) {
      this.listData = []
      this.pageIndex = 1
      this.getProjectList(val, '')
    },
  },
  methods: {
    // 获取工程商信息
    async getProjectList(company_id, keyword) {
      this.loading = true
      const obj = {
        pageIndex: this.pageIndex,
        pageSize: 20,
        params: {
          company_id: `${company_id}`,
          keyword,
        },
      }
      let res = await Service.managementDynamic('um-eb-company-page', obj)
      this.loading = false
      if (res.code == 0) {
        this.dataIsFull = res.dataList.length ? false : true
        res.dataList.forEach((v) => {
          this.listData.push(v)
        })
        this.totalCount = res.totalCount
      } else {
        this.$message.error(res.msg)
      }
    },
    //统计工程总数
    // async totalProjectList(company_id) {
    //   this.loading = true
    //   const obj = {
    //     params: {
    //       company_id,
    //     },
    //   }
    //   let res = await Service.managementDynamic('um-company-statistic', obj)
    //   this.loading = false
    //   console.log('##########', res)
    //   // if (res.code == 0) {
    //   //   console.log('********************')
    //   // } else {
    //   //   this.$message.error(res.msg)
    //   // }
    // },
    changeItem(item) {
      this.classFlag = item.cmpny_id
    },
    async scroll(e) {
      if (e.target.scrollTop + e.target.clientHeight >= e.target.scrollHeight) {
        if (
          this.loading ||
          this.totalCount == this.listData.length ||
          this.dataIsFull
        ) {
          return
        }
        this.pageIndex++
        this.getProjectList(this.company_id, this.isQueryKeyword)
      }
    },
  },
}
</script>
<style scoped lang="less">
.prolist {
  overflow-y: scroll;
  height: 77vh;
  width: 300px;
}
/deep/ .projectlist {
  & .ant-card-head-title {
    padding-bottom: 12px;
    padding-top: 20px;
  }
}
.ant-list-vertical .ant-list-item-meta {
  margin-bottom: 3px;
}
.ant-list-vertical .ant-list-item-meta-title {
  margin-bottom: 3px;
}
</style>
<style></style>
