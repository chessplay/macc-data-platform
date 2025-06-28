<template>
  <div class="dc-page-card">

    <a-button type="primary" @click="showAddModule()"> 新增 </a-button>

    <a-modal v-model="addVisible" title="新增模块" :footer="null">
      <a-form-model>
        <a-form-model-item>
          <a-input />
        </a-form-model-item>

        <a-form-model-item>
          <a-input />
        </a-form-model-item>
      
        <a-form-model-item style="margin-bottom:0px;">
          <a-button type="primary">
            Submit
          </a-button>
          <a-button style="margin-left: 10px">
            Reset
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <a-table
      rowKey="moduleId"
      :columns="modCol"
      :data-source="modList"
      :rowSelection="rowSelection"
      bordered
      :loading="loading"
      :pagination="false"
    >
      <template slot="description" slot-scope="text, record">
        <a-popover
          v-if="record.type == 'DYNAMIC'"
          v-model="decVisible"
          trigger="click"
        >
          <template slot="content">
            <a-input placeholder="Basic usage" />
          </template>
          <a style="border-bottom: 1px dashed">{{ record.description }}</a>
        </a-popover>
        <a v-if="record.type == 'STATIC'">Empty</a>
      </template>
      <template slot="opt">
        <span>
          <a style="margin: 0 5px">详情</a>
          <a style="margin: 0 5px">修改</a>
          <a style="margin: 0 5px">删除</a>
        </span>
      </template>
      <template slot="footer">
        <a-pagination
          style="text-align: right"
          v-model="currentPage"
          :page-size-options="pageSizeOptions"
          :total="totalCount"
          show-size-changer
          :page-size="pageSize"
          @showSizeChange="onShowSizeChange"
        >
        </a-pagination>
      </template>
    </a-table>
  </div>
</template>
<script>
import EventService from '@p/services/eventService'
import { tr } from 'date-fns/locale'
export default {
  name: 'moduleMgt',
  data() {
    return {
      ruleForm: {
        pass: '',
        checkPass: '',
        age: '',
      },

      addVisible: false,
      decVisible: false,
      loading: false,
      selectedRows: [],
      selectedRowKeys: [],
      modList: [],
      currentPage: 1,
      pageSize: 10,
      totalCount: 2,
      pageSizeOptions: ['10', '20', '30', '40'],
      modCol: [
        {
          title: '序号',
          dataIndex: 'index',
          key: 'index',
          align: 'center',
          customRender: (text, record, index) => `${index + 1}`,
        },
        {
          title: '模块名称',
          dataIndex: 'moduleName',
          key: 'moduleName',
          align: 'center',
        },
        {
          title: '模块状态',
          dataIndex: 'type',
          key: 'type',
          align: 'center',
          customRender: (text, record) => {
            if (record.type == 'STATIC') {
              return '静态'
            } else {
              return '动态'
            }
          },
        },
        {
          title: '描述信息',
          key: 'description',
          scopedSlots: { customRender: 'description' },
          align: 'center',
        },
        {
          title: '操作',
          key: 'opt',
          scopedSlots: { customRender: 'opt' },
          align: 'center',
        },
      ],
    }
  },
  computed: {
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedRows = selectedRows
          this.selectedRowKeys = selectedRowKeys
        },
        getCheckboxProps: (record) => {
          return {
            props: {
              name: record.aliasName,
            },
          }
        },
      }
    },
  },
  methods: {
    showAddModule() {
      this.addVisible = true
    },
    addModule() {
      let params = {
        moduleId: 'newOne',
        moduleName: 'newBoy',
        description: 'this is a nice one',
      }
      EventService.addModule(params).then((ret) => {
        if (ret.code == 0) {
          console.log(ret)
        }
      })
    },
    // 获取模块信息列表
    getModuleList() {
      EventService.getModuleList().then((ret) => {
        if (ret.code == 0) {
          this.modList = ret.list
          console.log(this.modList)
        } else {
          console.log(ret.msg)
        }
      })
    },
    //分页大小监听
    onShowSizeChange(current, pageSize) {
      this.pageSize = pageSize
    },
    hide() {
      this.decVisible = false
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
  },
  created() {
    this.getModuleList()
  },
  mounted() {},
}
</script>

<style lang="less" scoped>
</style>
