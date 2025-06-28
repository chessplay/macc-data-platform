<template>
  <div>
    <a-table
      class="dc-tb-stripe"
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
import Service from '@p/services/managementService'
export default {
  name: 'employeeList',
  props: ['company_id'],
  data() {
    return {
      columns,
      loading: false,
      tableData: [],
    }
  },
  watch: {
    company_id(val) {
      this.getEmployeeList(val)
    },
  },
  methods: {
    //获取左边列表对应的员工列表
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
  },
}
</script>
<style scoped></style>
