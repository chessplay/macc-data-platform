<template>
  <div class="dc-page-card">
    <a-tabs
      v-model="activeKey"
      style="position:relative;z-index:0"
      @change="changeTabs"
    >
      <a-tab-pane key="1" tab="数据模型列表">
        <div>
          <header>
            <a-button type="primary" icon="plus" @click="showModel()"
              >添加</a-button
            >
            <a-button
              type="primary"
              :disabled="!hasSelected"
              @click="deleteData()"
              icon="delete"
              style="margin-left:20px"
              >删除
            </a-button>
            <a-input-search
              @search="onSearch"
              placeholder="数据模型关键字搜索"
              style="width: 200px;height:29px;margin-left:20px;z-index: 0;"
            />
          </header>

          <a-table
            class="dc-tb-stripe"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
            }"
            @change="onPageChange"
            tableLayout="fixed"
            size="middle"
            :rowKey="
              (record, index) => {
                return index
              }
            "
            :columns="columns"
            :pagination="pagination"
            style="position:relative;z-index:0"
            :data-source="data"
            :loading="isLoad"
          >
            <span slot="controlData" slot-scope="text, record">
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
                @click="toDebug(record)"
              >
                <a-icon type="export" />调试
              </button>
            </span>
          </a-table>
        </div>
      </a-tab-pane>
      <a-tab-pane key="2" tab="操作数据模型" force-render>
        <add-data-model
          :queryType="queryType"
          :parmasType="parmasType"
          :moduleId="moduleId"
          :formData="rowData"
          :dataSourceID="dataSourceID"
          @toTest="toTest()"
          @cancel="modalCancel()"
          @addData="handleAdd"
          ref="formone"
        >
        </add-data-model>
      </a-tab-pane>
      <a-tab-pane key="3" tab="调试" force-render :disabled="disabled">
        <debug
          :reData="record"
          :queryType="queryType"
          :dataSourceID="dataSourceID"
          ref="debugForm"
        >
        </debug>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import Service from '@p/services/dbService'
import addDataModel from './components/addDataModel.vue'
import Debug from './components/debug.vue'
const columns = [
  {
    title: '数据模型ID',
    dataIndex: 'modelId',
    key: 'modelId',
  },
  {
    title: '数据源ID',
    dataIndex: 'dataSourceId',
    key: 'dataSourceId',
  },
  {
    title: '数据模型名称',
    dataIndex: 'modelName',
    key: 'modelName',
  },
  {
    title: '所属模块ID',
    dataIndex: 'moduleId',
    key: 'moduleId',
  },
  {
    title: '返回值类型',
    dataIndex: 'resultType',
    key: 'resultType',
  },
  {
    title: '备注',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '操作',
    dataIndex: 'controlData',
    key: 'controlData',
    scopedSlots: { customRender: 'controlData' },
  },
]
export default {
  components: { addDataModel, Debug },
  name: 'dataModel',
  data() {
    return {
      columns,
      data: [],
      // visible: false,
      pagination: {
        total: 0,
      },
      activeKey: '1',
      selectedRowKeys: [], //多选id
      rowData: {},
      moduleId: [],
      queryType: [],
      parmasType: [],
      record: {},
      isLoad: false,
      disabled: true,
      keyword: '',
      dataSourceID: [],
    }
  },
  created() {
    this.query({ page_index: 1, page_size: 10 })
    this.getQueryType()
    this.getSupportPraramType()
    this.getDataSourceID()
    this.privilegeModuleList()
  },
  computed: {
    hasSelected() {
      return this.selectedRowKeys.length > 0
    },
  },
  methods: {
    changeTabs(key) {
      if (key == '1') {
        this.modalCancel()
        this.rowData = {}
        this.$refs.formone.getType()
      }
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    /**
     * @description: 如果点击分页
     * @param {*}pagination
     * @return {*}
     */
    onPageChange(pagination) {
      const pager = { ...this.pagination }
      pager.current = pagination.current
      this.pagination = pager
      this.query({
        page_index: pagination.current,
        page_size: 10,
        keyword: this.keyword,
      })
    },
    onSearch(val) {
      this.keyword = val
      this.query({ page_index: 1, page_size: 10, keyword: val })
      this.pagination.current = 1
    },
    /**
     * @description:清空表单modal
     * @param
     * @returns
     */
    modalCancel() {
      // this.visible = false;
      const form = this.$refs.formone.form
      form.resetFields()
      // this.$nextTick(() => {
      //   const form2 = this.$refs.debugForm.form;
      //   form2.resetFields()
      // })
    },

    /**
     * @description:添加/更新数据源请求
     * @param
     * @returns
     */
    handleAdd() {
      const form = this.$refs.formone.form
      form.validateFields(async (err, values) => {
        if (values.queryType == 'PAGE' && !values.countSqlScript) {
          this.$warning({
            title: '警告',
            content: '当查询类型为PAGE时，“计算数量的SQL脚本”不可为空',
          })
          return
        }
        if (err) {
          return
        }
        values = this.fitterData(values)
        this.$refs.formone.confirmLoading = true
        let res = await Service.addQuerymodelInfo(values)
        this.$refs.formone.confirmLoading = false
        if (res.code == 0) {
          this.$success({
            title: '操作成功',
          })
          this.pagination.current = 1
          this.activeKey = '1'
          form.resetFields()
          this.query({ page_index: 1, page_size: 10 })
        } else {
          this.$error({
            title: '操作失败',
            content: '错误信息：' + res.msg,
          })
        }
      })
    },

    /**
     * @description:支持的查询类型
     * @param
     * @returns
     */
    async getQueryType() {
      let res = await Service.getSupportQuerytype()
      if (res.code == 0) {
        this.queryType = res.list
      } else {
        this.$message.error(res.msg)
      }
    },
    /**
     * @description:支持的查询类型
     * @param
     * @returns
     */
    async getSupportPraramType() {
      let res = await Service.getSupportPraramType()
      if (res.code == 0) {
        this.parmasType = res.list
      } else {
        this.$message.error(res.msg)
      }
    },

    /**
     * @description:展示数据模型modal状态
     * @param val 表单的值
     * @returns
     */
    async showModel(val) {
      if (val) {
        this.isLoad = true
        let res = await Service.getQuerymodelInfo(val.modelId)
        this.isLoad = false
        val = res.data
      }
      this.$refs.formone.getType()
      this.modalCancel()
      this.rowData = val || {}
      this.activeKey = '2'
    },

    /**
     * @description:获取数据模型列表
     * @param
     * @returns
     */
    async query({ page_index, page_size, keyword }) {
      const res = await Service.getQuerymodelPaging({
        page_index,
        page_size,
        keyword,
      })
      this.pagination.total = res.totalCount
      if (res.code == 0) {
        this.data = res.dataList
      } else {
        this.$message.error(res.msg)
      }
    },
    /**
     * @description:删除数据模型(可多个)
     * @param id
     * @returns
     */
    deleteData() {
      let self = this
      let temp = []
      for (let i = 0; i < this.selectedRowKeys.length; i++) {
        let key = this.selectedRowKeys[i]
        temp.push(this.data[key].modelId)
      }
      this.$confirm({
        title: '提示',
        content: '确定要删除吗？',
        async onOk() {
          self.isLoad = true
          let res = await Service.deleteQuerymodelInfo(temp.toString())
          self.isLoad = false
          if (res.code == 0) {
            self.$message.success('删除成功')
            self.selectedRowKeys = []
            this.pagination.current = 1
            self.query({ page_index: 1, page_size: 10 })
          } else {
            self.$message.error(res.msg)
          }
        },
        onCancel() {},
      })
    },
    /**
     * @description:过滤和处理表单返回的数据
     * @param
     * @returns
     */
    fitterData(values) {
      let params = []
      for (let key of values.keys) {
        let obj = {}
        obj.description = values.description[key]
        obj.name = values.name[key]
        obj.require = values.require[key]
        obj.type = values.type[key]
        params.push(obj)
      }
      let description = values.descript
      delete values.descript
      delete values.description
      delete values.keys
      delete values.name
      delete values.require
      delete values.type
      values.dataSourceId = parseInt(values.dataSourceId)
      values.description = description
      values.params = params
      return values
    },
    /**
     * @description:测试运行数据模型
     * @param
     * @returns
     */
    toTest() {
      const form = this.$refs.formone.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        let data = JSON.parse(JSON.stringify(values))
        values = this.fitterData(data)
        this.toDebug(values)
      })
    },
    /**
     * @description:前往调试
     * @param
     * @returns
     */
    toDebug(val) {
      // this.$nextTick(() => {
      //   const form2 = this.$refs.debugForm.form;
      //   form2.resetFields()
      // })
      this.record = val
      this.activeKey = '3'
    },
    /**
     * @description:获取数据源id
     * @param
     * @returns
     */
    async getDataSourceID() {
      let res = await Service.getDBList()
      if (res.code == 0) {
        for (let item in res.list) {
          let dataSource = {}
          dataSource.id = res.list[item].id
          dataSource.name = res.list[item].name
          this.dataSourceID.push(dataSource)
        }
      }
    },
    /**
     * @description:获取所属模块id
     * @param
     * @returns
     */
    async privilegeModuleList() {
      let res = await Service.privilegeModuleList()
      for (let item in res.list) {
        this.moduleId.push(res.list[item].moduleId)
      }
      console.log(this.moduleId)
    },
  },
}
</script>
<style scoped>
header {
  padding: 0 0 10px 0;
  margin: 0 0 10px 0;
}
</style>
