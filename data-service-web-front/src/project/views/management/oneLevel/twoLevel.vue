<template>
  <div>
    <header v-if="xianshi">
      <a-select
        class="yiji"
        :allowClear="true"
        placeholder="公司子类型选择"
        style="width: 150px; min-width: 100px"
      >
        <a-select-option value="cmpny_sub_type1">
          地市核心分销商
        </a-select-option>
        <a-select-option value="cmpny_sub_type2">
          县级核心分销商
        </a-select-option>
      </a-select>
      <a-input
        class="erji"
        placeholder="一级区域"
        :allow-clear="true"
        v-model="l1_zone"
        style="width: 150px; min-width: 100px"
      ></a-input>
      <a-input
        class="sanji"
        placeholder="二级区域"
        :allow-clear="true"
        v-model="l2_zone"
        style="width: 150px; min-width: 100px"
      ></a-input>
      <a-input
        class="siji"
        placeholder="关键字"
        :allow-clear="true"
        v-model="keyword"
        style="width: 150px; min-width: 100px"
      ></a-input>
      <a-button type="primary" @click="search" icon="search">搜索</a-button>
    </header>
    <a-table
      :pagination="true"
      class="dc-tb-stripe"
      :rowKey="(row) => row.cmpny_owner_user_id"
      :columns="columns"
      :data-source="tableData"
      :loading="loading"
      tableLayout="fixed"
    >
    </a-table>
    <!-- <a-pagination :current="pageIndex" :total="50" @change="onChange" /> -->
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
      pageSize: 5,
      keyword: '', //查询关键字
      isQueryKeyword: '', //已搜索过得关键字
      totalCount: 0,
      company_type: '二级渠道',
      dataIsFull: false,
      searchId: '', //当前公司的ID
      l1_zone: '',
      l2_zone: '',
    }
  },
  watch: {
    company_id(val) {
      this.searchId = val
      this.tableData = []
      this.pageIndex = 1
      this.getEmployeeList(val, this.company_type, '')
    },
  },
  methods: {
    onChange(current) {
      this.current = current
    },
    //获取地县核的数据
    async getEmployeeList(supply_company_id, company_type, keyword) {
      this.loading = true
      const obj = {
        params: {
          supply_company_id,
          company_type,
          // company_sub_type,
          // l1_zone,
          // l2_zone,
          keyword,
        },
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }
      let res = await Service.managementDynamic('um-sub-company-page', obj)
      this.loading = false
      if (res.code == 0) {
        this.dataIsFull = res.dataList.length ? false : true
        res.dataList.forEach((v) => {
          this.tableData.push(v)
        })
        this.totalCount = res.totalCount
        this.xianshi = true
      } else {
        this.$message.error(res.msg)
      }
    },
    //搜索页面
    search() {
      //console.log('this.searchId,this.keyword', this.searchId, this.l1_zone)
      this.pageIndex = 1
      this.isQueryKeyword = this.keyword
      this.tableData = []
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
  left: 40%;
}
.erji {
  position: absolute;
  left: 54%;
}
.sanji {
  position: absolute;
  left: 67%;
}
.siji {
  position: absolute;
  left: 80%;
}
</style>
