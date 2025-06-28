<template>
  <div>
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
  {
    title: '上级公司ID',
    align: 'center',
    dataIndex: 'sppr_cmpny_id',
    key: 'sppr_cmpny_id',
  },
  {
    title: '上级公司名称',
    align: 'center',
    dataIndex: 'sppr_cmpny_name',
    key: 'sppr_cmpny_name',
  },
  {
    title: '上级公司类型',
    align: 'center',
    dataIndex: 'sppr_cmpny_type',
    key: 'sppr_cmpny_type',
  },
  {
    title: '上级公司子类型',
    align: 'center',
    dataIndex: 'sppr_cmpny_sub_type',
    key: 'sppr_cmpny_sub_type',
  },
  {
    title: '上级公司名称',
    align: 'center',
    dataIndex: 'sppr_cmpny_chnnl_type',
    key: 'sppr_cmpny_chnnl_type',
  },
  {
    title: '上级公司管理者ID',
    align: 'center',
    dataIndex: 'sppr_cmpny_manager_user_id',
    key: 'sppr_cmpny_manager_user_id',
  },
]
import Service from '@p/services/managementService'
export default {
  name: 'projectList',
  props: ['company_id'],
  data() {
    return {
      columns,
      loading: false,
      tableData: [], //表单的数据
      projectArray: [], //tableData数组形式
    }
  },
  watch: {
    //父传子，将公司的id穿过来
    company_id(val) {
      this.getDetail(val)
    },
  },
  methods: {
    //查询公司详情的列表
    async getDetail(company_id) {
      this.loading = true
      const obj = {
        params: {
          company_id,
        },
      }
      let res = await Service.managementDynamic('um-company-detail', obj)
      console.log('getDetail:', res)
      this.loading = false
      if (res.code == 0) {
        this.projectArray.push(res.data)
        this.tableData = this.projectArray
        this.projectArray = []
      } else {
        this.$message.error(res.msg)
      }
    },
  },
}
</script>
<style scoped></style>
