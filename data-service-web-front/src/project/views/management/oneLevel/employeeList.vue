<template>
  <div>
    <header style="margin-left: 55px">
      <span>公司拥有者ID:{{ this.company_detal.cmpny_owner_user_id }} </span>
      <span class="span1">公司名称:{{ this.company_detal.cmpny_name }}</span>
      <span class="span2">公司类型:{{ this.company_detal.cmpny_type }}</span>
      <br />
      <span>公司子类型:{{ this.company_detal.cmpny_sub_type }}</span>
      <span class="span1">一级区域:{{ this.company_detal.l1_zone }}</span>
      <span class="span2">二级区域:{{ this.company_detal.l2_zone }}</span>
      <br />
      <span>城市名称:{{ this.company_detal.city_name }}</span>
      <span class="span1"
        >城市编码:{{ this.company_detal.cmpny_city_code }}</span
      >
      <span class="span2">销售:{{ this.company_detal.sale_name }}</span>
      <br />
      <span>添加公司方式:{{ this.company_detal.add_cmpny_method }}</span>
      <span class="span1"
        >上级公司ID:{{ this.company_detal.sppr_cmpny_id }}</span
      >
      <span class="span2"
        >上级公司名称:{{ this.company_detal.sppr_cmpny_name }}</span
      >
      <br />
      <span>上级公司类型:{{ this.company_detal.sppr_cmpny_type }}</span>
      <span class="span1"
        >上级公司子类型:{{ this.company_detal.sppr_cmpny_sub_type }}</span
      >
      <span class="span2"
        >上级公司名称:{{ this.company_detal.sppr_cmpny_chnnl_type }}</span
      >
      <br />
      <hr />
    </header>
    <a-table
      class="dc-tb-stsiripe"
      :rowKey="(row) => row.emply_accnt_id"
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
    title: '账号ID',
    align: 'center',
    dataIndex: 'emply_accnt_id',
    key: 'emply_accnt_id',
  },
  {
    title: '租户ID',
    align: 'center',
    dataIndex: 'emply_tenant_id',
    key: 'emply_tenant_id',
  },
  {
    title: '用户名',
    align: 'center',
    dataIndex: 'emply_fn',
    key: 'emply_fn',
  },
  {
    title: '账号角色',
    align: 'center',
    dataIndex: 'emply_role',
    key: 'emply_role',
  },
  {
    title: '手机号',
    align: 'center',
    dataIndex: 'emply_phone',
    key: 'emply_phone',
  },
]
// 员工列表页面
import Service from '@p/services/managementService'
export default {
  name: 'employeeList',
  props: ['company_id'],
  data() {
    return {
      columns,
      loading: false,
      tableData: [],
      company_detal: {},
    }
  },
  watch: {
    company_id(val) {
      this.getEmployeeList(val)
      this.getDetail(val)
    },
  },
  methods: {
    //获取员工列表的数据
    async getEmployeeList(company_id) {
      this.loading = true
      const obj = {
        params: {
          company_id,
        },
      }
      let res = await Service.managementDynamic('um-company-userlist', obj)
      this.loading = false
      if (res.code == 0) {
        this.tableData = res.list
      } else {
        this.$message.error(res.msg)
      }
    },
    //获取公司详细信息数据
    async getDetail(company_id) {
      this.loading = true
      const obj = {
        params: {
          company_id,
        },
      }
      let res = await Service.managementDynamic('um-company-detail', obj)
      this.loading = false
      //console.log('员工列表页面的公司详情信息:', res)
      if (res.code == 0) {
        this.company_detal = res.data
      } else {
        this.$message.error(res.msg)
      }
    },
  },
}
</script>
<style scoped>
header {
  position: relative;
}
.span1 {
  position: absolute;
  left: 200px;
}
.span2 {
  position: absolute;
  left: 500px;
}
</style>
