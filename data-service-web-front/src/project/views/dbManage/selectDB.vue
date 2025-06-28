<template>
  <div class="dc-page-card">
    <div>
      <header>
        <a-button type="primary" icon="plus" @click="showModel()"
          >添加数据源</a-button
        >
        <add-datasource
          :getDBType="getDBType"
          :formData="rowData"
          :visible="visible"
          @toTest="toTest()"
          @cancel="modalCancel()"
          @addData="handleAdd"
          ref="formone"
        >
        </add-datasource>
      </header>

      <a-table
        class="dc-tb-stripe"
        tableLayout="fixed"
        size="middle"
        rowKey="id"
        :columns="columns"
        :pagination="pagination"
        style="position:relative;z-index:0"
        :data-source="data"
        :loading="isLoad"
      >
        <span slot="conState" slot-scope="text, record">
          <a-icon
            type="check-circle"
            v-if="record.state == 'enable'"
            theme="twoTone"
            two-tone-color="#52c41a"
          />
          <a-icon
            type="close-circle"
            v-else
            theme="twoTone"
            two-tone-color="#f81d22"
          />
        </span>

        <span slot="deleteData" slot-scope="text, record">
          <button
            class="dc-tb-btn-edit"
            v-if="data.length"
            @click="showModel(record)"
          >
            <a-icon type="edit" />编辑
          </button>
          <button
            class="dc-tb-btn-del"
            v-if="data.length"
            @click="deleteData(record.id)"
          >
            <a-icon type="delete" />删除
          </button>
        </span>
      </a-table>
    </div>
  </div>
</template>

<script>
import Service from '@p/services/dbService'
import addDatasource from './components/addDatasource.vue'
const columns = [
  // {
  //   title: 'ID',
  //   dataIndex: 'id',
  //   key: 'id',
  // },
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'jdbc连接地址',
    dataIndex: 'jdbcUrl',
    key: 'jdbcUrl',
  },
  {
    title: 'jdbc schema',
    dataIndex: 'dbschema',
    key: 'dbschema',
  },
  {
    title: '数据库类型',
    dataIndex: 'dbType',
    key: 'dbType',
  },
  {
    title: '连接用户名',
    dataIndex: 'userName',
    key: 'userName',
  },
  // {
  //   title: '最大存活时间（ms）',
  //   dataIndex: 'maxLifeTime',
  //   key: 'maxLifeTime',
  // },
  // {
  //   title: '连接超时时间（ms）',
  //   dataIndex: 'connectionTimeout',
  //   key: 'connectionTimeout',
  // },
  // {
  //   title: '最大连接数',
  //   dataIndex: 'maximumPoolSize',
  //   key: 'maximumPoolSize',
  // },
  // {
  //   title: '最小空闲连接数',
  //   dataIndex: 'minimumIdle',
  //   key: 'minimumIdle',
  // },
  {
    title: '连接状态',
    dataIndex: 'state',
    key: 'state',
    scopedSlots: { customRender: 'conState' },
  },
  {
    title: '备注',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '操作',
    dataIndex: 'deleteData',
    key: 'deleteData',
    scopedSlots: { customRender: 'deleteData' },
  },
]
const pagination = {
  pageSize: 10,
  showQuickJumper: true,
  showSizeChanger: true,
}
export default {
  components: { addDatasource },
  name: 'selectDB',
  data() {
    return {
      columns,
      data: [],
      visible: false,
      pagination,
      rowData: {},
      getDBType: [],
      isLoad: false,
    }
  },
  created() {
    this.query()
    this.getDatabaseType()
  },
  methods: {
    /**
     * @description:获取支持的数据库类型
     * @param
     * @returns
     */
    async getDatabaseType() {
      let res = await Service.getDatabaseType()
      if (res.code == 0) {
        this.getDBType = res.list
      } else {
        this.$message.error(res.msg)
      }
    },
    /**
     * @description:取消数据源modal
     * @param
     * @returns
     */
    modalCancel() {
      this.visible = false
      const form = this.$refs.formone.form
      form.resetFields()
    },
    /**
     * @description:测试数据库
     * @param
     * @returns
     */
    async test(value) {
      let res = await Service.testData(value)
      if (res.code == 0) {
        if (res.data.state == 'enable') {
          this.$success({
            title: '连接成功',
          })
        } else {
          this.$error({
            title: '连接失败',
            content: '错误信息：' + res.data.errorMsg,
          })
        }
      } else {
        this.$message.error(res.msg)
      }
    },
    /**
     * @description:展示数据源modal状态
     * @param value 表单的值
     * @returns
     */
    async showModel(val) {
      if (val) {
        this.isLoad = true
        let res = await Service.getDatasourceInfo(val.id)
        this.isLoad = false
        if (res.data.driverPropertiesJson) {
          let driverProperties = JSON.parse(res.data.driverPropertiesJson)
          res.data.driverProperties = driverProperties
        }
        val = res.data
      }
      this.rowData = val || {}
      this.visible = true
    },
    /**
     * @description:添加/更新数据源请求
     * @param
     * @returns
     */
    handleAdd() {
      const form = this.$refs.formone.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        let driverProperties = []
        for (let key in values.key) {
          let obj = {}
          obj.key = values.key[key]
          obj.value = values.value[key]
          driverProperties.push(obj)
        }
        delete values.key
        delete values.keys
        delete values.value
        values.driverProperties = driverProperties
        this.$refs.formone.confirmLoading = true
        let res = await Service.addDatasource(values)
        this.$refs.formone.confirmLoading = false
        if (res.code == 0) {
          this.$message.success('操作成功！')
          this.visible = false
          // 清除表单信息
          form.resetFields()
          this.query()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    /**
     * @description:获取数据库列表
     * @param
     * @returns
     */
    async query() {
      let res = await Service.getDBList()
      if (res.code == 0) {
        this.data = res.list
      } else {
        this.$message.error(res.msg)
      }
    },
    /**
     * @description:删除数据源
     * @param id
     * @returns
     */
    deleteData(id) {
      let self = this
      this.$confirm({
        title: '提示',
        content: '确定要删除这条数据吗？',
        async onOk() {
          self.isLoad = true
          let res = await Service.deleteDatasource(id)
          self.isLoad = false
          if (res.code == 0) {
            self.$message.success('删除成功')
            self.query()
          } else {
            self.$message.error(res.msg)
          }
        },
        onCancel() {},
      })
    },

    toTest() {
      const form = this.$refs.formone.form

      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        this.test(values)
      })
    },
  },
}
</script>
<style scoped></style>
