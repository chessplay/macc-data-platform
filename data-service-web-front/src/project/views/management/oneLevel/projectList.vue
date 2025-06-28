<template>
  <div>
    <header v-if="xianshi">
      <a-input
        class="yiji"
        placeholder="一级区域"
        :allow-clear="true"
        v-model="l1_zone"
        style="width: 150px; min-width: 100px"
      ></a-input>
      <a-input
        class="erji"
        placeholder="二级区域"
        :allow-clear="true"
        v-model="l2_zone"
        style="width: 150px; min-width: 100px"
      ></a-input>
      <a-input
        class="sanji"
        placeholder="关键字"
        :allow-clear="true"
        v-model="keyword"
        style="width: 150px; min-width: 100px"
      ></a-input>
      <a-button type="primary" @click="search" icon="search">搜索</a-button>
    </header>
    <a-table
      class="dc-tb-stripe"
      :rowKey="(row) => row.cmpny_owner_user_id"
      :columns="columns"
      :data-source="tableData"
      :loading="loading"
      tableLayout="fixed"
    >
    </a-table>
  </div>
</template>

<script>
const columns = [
  {
    title: '公司拥有者ID',
    align: 'center',
    dataIndex: 'cmpny_owner_user_id',
    key: 'cmpny_owner_user_id',
  },
  {
    title: '公司名称',
    align: 'center',
    dataIndex: 'cmpny_name',
    key: 'cmpny_name',
  },
  {
    title: '公司类型',
    align: 'center',
    dataIndex: 'cmpny_type',
    key: 'cmpny_type',
  },
  {
    title: '公司子类型',
    align: 'center',
    dataIndex: 'cmpny_sub_type',
    key: 'cmpny_sub_type',
  },
  {
    title: '一级区域',
    align: 'center',
    dataIndex: 'l1_zone',
    key: 'l1_zone',
  },
  {
    title: '二级区域',
    align: 'center',
    dataIndex: 'l2_zone',
    key: 'l2_zone',
  },
  {
    title: '城市名称',
    align: 'center',
    dataIndex: 'city_name',
    key: 'city_name',
  },
  {
    title: '城市编码',
    align: 'center',
    dataIndex: 'cmpny_city_code',
    key: 'cmpny_city_code',
  },
  {
    title: '销售',
    align: 'center',
    dataIndex: 'sale_name',
    key: 'sale_name',
  },
  {
    title: '添加公司方式',
    align: 'center',
    dataIndex: 'add_cmpny_method',
    key: 'add_cmpny_method',
  },
]
// 员工列表页面
import Service from '@p/services/managementService'
export default {
  name: 'employeeList',
  props: ['company_id'],
  data() {
    return {
      xianshi: false,
      columns,
      loading: false,
      tableData: [],
      pageIndex: 1,
      totalCount: 0,
      company_type: '工程商',
      keyword: '',
      l1_zone: '',
      l2_zone: '',
      searchId: '',
    }
  },
  watch: {
    company_id(val) {
      this.searchId = val
      this.getEmployeeList(val, this.company_type, '')
    },
  },
  methods: {
    //获取员工列表的数据
    async getEmployeeList(supply_company_id, company_type, keyword) {
      this.loading = true
      const obj = {
        params: {
          supply_company_id,
          company_type,
          keyword,
        },
        pageIndex: this.pageIndex,
        pageSize: 20,
      }
      let res = await Service.managementDynamic('um-sub-company-page', obj)
      this.loading = false
      if (res.code == 0) {
        this.tableData = res.dataList
        this.totalCount = res.totalCount
        this.xianshi = true
      } else {
        this.$message.error(res.msg)
      }
    },
    //搜索
    search() {
      this.getEmployeeList(this.searchId, this.company_type, this.keyword)
    },
  },
}
</script>
<style scoped>
header {
  text-align: right;
  position: relative;
}
.yiji {
  position: absolute;
  left: 54%;
}
.erji {
  position: absolute;
  left: 67%;
}
.sanji {
  position: absolute;
  left: 80%;
}
</style>
