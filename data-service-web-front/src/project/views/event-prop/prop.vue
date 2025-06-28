<template>
  <div class="dc-page-card">
    <div class="prop">
      <div class="header">
        <a-button type="primary" @click="showModal" icon="plus">新增</a-button>
        <collection-create-form ref="collectionForm" :visible="visible" @cancel="handleCancel" @create="handleCreate" />
        <a-button type="primary" :disabled="!hasSelected" @click="remove" icon="delete">删除</a-button>
      </div>
      <a-table size="middle" class="dc-tb-stripe" :pagination="false" :scroll="{ y: 600 }" :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }" :columns="columns" :data-source="data">
        <span slot="name" slot-scope="text">{{ text }}</span>
        <span slot="action" slot-scope="text, record">
          <button class="dc-tb-btn-del" v-if="data.length" @click="showConfirm(record.key)">
            <a-icon type="delete" />删除
          </button>
        </span>
      </a-table>
    </div>
  </div>
</template>

<script>
import PropService from '@p/services/propService'
import CollectionCreateForm from '@p/views/event-prop/component/modal.vue'
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '类别',
    dataIndex: 'type',
  },
  {
    title: '是否需要转化',
    dataIndex: 'needParse',
  },
  {
    title: '默认值',
    dataIndex: 'defaultValue',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '操作',
    dataIndex: 'action',
    scopedSlots: { customRender: 'action' },
  },
]
const data = []

export default {
  name: 'prop',
  components: {
    CollectionCreateForm,
  },
  data() {
    return {
      visible: false,
      data,
      columns,
      selectedRowKeys: [],
    }
  },
  computed: {
    hasSelected() {
      return this.selectedRowKeys.length > 0
    },
  },

  mounted() {
    this.query()
  },

  methods: {
    query() {
      this.data = []
      PropService.query().then((ret) => {
        for (let i = 0; i < ret.list.length; i++) {
          if (ret.list[i].needParse === 1) {
            ret.list[i].needParse = '是'
          } else {
            ret.list[i].needParse = '否'
          }
          this.data.push({
            key: i,
            name: ret.list[i].name,
            type: ret.list[i].type,
            needParse: ret.list[i].needParse,
            defaultValue: ret.list[i].defaultValue,
            remark: ret.list[i].remark,
          })
        }
      })
    },

    remove() {
      var that = this
      let name_list = []
      for (let i = 0; i < this.selectedRowKeys.length; i++) {
        let key = this.selectedRowKeys[i]
        name_list.push(this.data[key].name)
      }
      this.$confirm({
        title: '提示',
        content: '确定要删除这' + this.selectedRowKeys.length + '条数据吗？',
        onOk() {
          PropService.delete({
            name_list: name_list,
          }).then((res) => {
            PropService.editCallBack(res, '操作成功')
            that.query()
          })
        },
        onCancel() {},
      })
      this.selectedRowKeys = []
    },

    showModal() {
      this.visible = true
    },

    handleCancel() {
      this.visible = false
    },

    handleCreate() {
      const form = this.$refs.collectionForm.form
      form.validateFields((err, values) => {
        if (err) {
          return
        }
        if (values.needParse === true) {
          values.needParse = 1
        } else {
          values.needParse = 0
        }
        PropService.addOrUpdate({
          name: values.name,
          type: values.type,
          needParse: values.needParse,
          defaultValue: values.defaultValue,
          remark: values.remark,
        }).then((ret) => {
          PropService.editCallBack(ret, '操作成功')
          this.query()
        })
        form.resetFields()
        this.visible = false
      })
    },

    showConfirm(key) {
      let that = this
      this.$confirm({
        title: '提示',
        content: '确定要删除此类别吗？',
        onOk() {
          PropService.delete({
            name_list: that.data[key].name,
          }).then((res) => {
            PropService.editCallBack(res, '操作成功')
            that.query()
          })
        },
        onCancel() {},
      })
    },

    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
  },
}
</script>
<style lang="less" scoped>
.header {
  padding: 10px 0 10px 0;
  margin: 10px 0 10px 0;
}
.header > button {
  margin-right: 20px;
}
</style>