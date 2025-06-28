<template>
  <a-modal :visible="visible" :title="winTitle" okText="确定" @cancel="handleCancel" @ok="handleCreate">
    <a-form :model="showData" layout="vertical" :form="form" ref="modal">
      <a-form-item v-bind="formItemLayout" label="appid：">
        <a-select v-decorator="[
              'appid',
              {
                initialValue: showData.appid,
                rules: [{ required: true, message: '请选择appid!' }],
              },
            ]">
          <a-select-option value='79060'>MACC (79060)</a-select-option>
          <a-select-option value='72620'>APP (72620)</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="名称：">
        <a-input :readOnly="!!showData.name" v-decorator="[
            'name',
            {
              initialValue: showData.name,
              rules: [{ required: true, message: '请输入名称!' }],
            },
          ]" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="事件别名：">
        <a-input v-decorator="[
            'alias',
            {
              initialValue: showData.alias||null,
            },
          ]" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="类别：">
        <a-form-item class="type-style">
          <a-select v-decorator="[
              'type1',
              {
                initialValue: showData.type1,
                rules: [{ required: true, message: '请选择一级类别!' }],
              },
            ]" @change="handleSelectChange" @focus="type1Focus">
            <a-select-option v-for="(item, index) in type1" :key="index" :value="item">{{ item }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item class="type-style">
          <a-select v-decorator="[
              'type2',
              {
                initialValue: showData.type2,
                rules: [{ required: true, message: '请选择二级类别!' }],
              },
            ]">
            <a-select-option v-for="(item, index) in type2" :key="index" :value="item">{{ item }}</a-select-option>
          </a-select>
        </a-form-item>
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="埋点上线版本：">
        <a-input v-decorator="[
            'version',
            {
              initialValue: showData.version||null,
            },
          ]" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="埋点上线时间">
        <a-date-picker style="width: 100%" v-decorator="[
            'time',
            {
              initialValue: showData.time,
              rules: [{ required: true, message: '请选择埋点上线时间!' }],
            },
            
          ]" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="扩展列：">
        <a-input v-decorator="['extColumns', { initialValue: showData.extColumns }]" placeholder="多个逗号隔开" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="备注：">
        <a-textarea v-decorator="['remark', { initialValue: showData.remark }]"></a-textarea>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import TypeService from '@p/services/typeService.js'
export default {
  props: {
    rowData: {
      type: Object,
    },
    visible: {
      default: false,
    },
  },
  data () {
    return {
      type1: [],
      type2: [],
      typeData: [],
      dataString: [],
      showData: {
        appid: '',
        name: '',
        alias: '',
        type1: '',
        type2: '',
        version: '',
        time: '',
        remark: '',
        extColumns: '',
      },
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
    }
  },

  computed: {
    winTitle: function () {
      return this.showData.name ? '编辑事件' : '新增事件'
    },
  },

  watch: {
    rowData (v) {
      this.showData = v
      this.handleSelectChange(v.type1)
    },
  },

  beforeCreate () {
    this.form = this.$form.createForm(this, { name: 'form_in_modal' })
  },

  created () {
    this.getType()
  },

  methods: {
    getType () {
      this.type1 = []
      var type1list = []
      TypeService.query().then((ret) => {
        this.typeData = ret.list
        for (var i = 0; i < ret.list.length; i++) {
          type1list.push(ret.list[i].type1)
        }
        for (var i = 0; i < type1list.length; i++) {
          this.type1 = Array.from(new Set(type1list))
        }
      })
    },
    handleSelectChange (value) {
      this.type2 = []
      for (var i = 0; i < this.typeData.length; i++) {
        if (value === this.typeData[i].type1) {
          this.type2.push(this.typeData[i].type2)
        }
      }
    },

    type1Focus () {
      this.form.setFieldsValue({
        type2: [],
      })
    },

    handleCancel () {
      this.$emit('cancel')
    },

    handleCreate () {
      this.$emit('success')
    },
  },
}
</script>
<style lang="less" scoped>
.type-style {
  width: 50%;
  float: left;
  margin-bottom: 0;
}
</style>
