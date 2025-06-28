<template>
  <div class="dc-page-card">
    <div class="types">
      <div class="type-one">
        <div class="header">
          <a-button type="primary" @click="showModal" icon="plus">新增</a-button>
          <modal-one :visible="visible" @cancel="handleCancel" @create="handleCreate" ref="formone" />
        </div>
        <a-table class="dc-tb-stripe" size="middle" :pagination="false" :scroll="{ y: 600 }" :columns="columns"
          :data-source="data">
          <span slot="action" slot-scope="text, record">
            <button class="dc-tb-btn-del" v-if="data.length" @click="showConfirm(record.key)">
              <a-icon type="delete" />删除
            </button>
          </span>
        </a-table>
      </div>
    </div>
  </div>
</template>

<script>
import ModalOne from '@p/views/event-type/components/modal1.vue'
import TypeService from '@p/services/typeService'
const columns = [
  {
    title: '一级类别',
    dataIndex: 'type1',
    key: 'type1',
  },
  {
    title: '二级类别',
    dataIndex: 'type2',
    key: 'type2',
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: 'action' },
  },
]

export default {
  name: 'types',
  components: {
    ModalOne,
  },
  data () {
    return {
      typeList: [],
      visible: false,
      data: [],
      columns,
      showData: '',
      customKey: 0,
    }
  },

  created () {
    this.query()
  },

  methods: {
    query () {
      this.customKey = 0
      this.data = []
      TypeService.query().then((ret) => {
        this.typeList = ret.list
        for (let i = 0; i < this.typeList.length; i++) {
          this.data.push({
            key: i,
            type1: this.typeList[i].type1,
            type2: this.typeList[i].type2,
          })
        }
      })
    },

    showModal () {
      this.visible = true
    },

    handleCancel () {
      this.visible = false
    },

    showConfirm (key) {
      let that = this
      this.$confirm({
        title: '提示',
        content: '确定要删除这条数据吗？',
        onOk () {
          TypeService.delete({
            type1: [that.data[key].type1],
            type2: [that.data[key].type2],
          }).then((res) => {
            TypeService.editCallBack(res, '操作成功')
            that.query()
          })
        },
        onCancel () { },
      })
    },

    handleCreate () {
      let that = this
      const form = this.$refs.formone.form
      form.validateFields((err, values) => {
        console.log(values)
        if (err) {
          return
        }
        TypeService.addOrUpdate({
          type1: values.type1,
          type2: values.type2,
        }).then((res) => {
          TypeService.editCallBack(res, '操作成功')
          that.query()
        })
        form.resetFields()
        this.visible = false
      })
    },
  },
}
</script>
<style lang="less" scoped>
.types {
  height: 100vh;
  display: flex;
  justify-content: space-between;
}
.type-one {
  width: 100%;
}
.header {
  background-color: #fff;
  height: 50px;
  padding: 10px 0 10px 0;
  button {
    float: left;
  }
}
.title {
  float: left;
  font-size: 16px;
}
</style>

