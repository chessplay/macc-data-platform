<template>
  <a-modal
    :visible="visible"
    :title="winTitle"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form layout="vertical" :form="form" ref="modal">
      <a-form-item v-bind="formItemLayout" v-show="false">
        <span
          v-decorator="[
            `menuid`,
            {
              initialValue: fromData.id || '',
            },
          ]"
        ></span>
      </a-form-item>
      <a-form-item label="名称" v-bind="formItemLayout">
        <a-input
          v-decorator="[
            `menuname`,
            {
              rules: [{ required: true, message: '输入框不能为空!' }],
              initialValue: fromData.name || '',
            },
          ]"
        />
      </a-form-item>
      <a-form-item label="路由" v-bind="formItemLayout">
        <a-input
          v-decorator="[
            `menuurl`,
            {
              rules: [{ required: true, message: '输入框不能为空!' }],
              initialValue: fromData.url || '',
            },
          ]"
        />
      </a-form-item>
      <a-form-item label="图标" v-bind="formItemLayout">
        <a-input
          v-decorator="[
            `menuicon`,
            {
              rules: [{ required: true, message: '输入框不能为空!' }],
              initialValue: fromData.icon || '',
            },
          ]"
        />
      </a-form-item>
      <a-form-item label="序号" v-bind="formItemLayout">
        <a-input
          v-decorator="[
            `menuserial`,
            {
              rules: [{ required: true, message: '输入框不能为空!' }],
              initialValue: fromData.priority || '',
            },
          ]"
        />
      </a-form-item>
      <a-form-item label="参数" v-bind="formItemLayout">
        <a-textarea
          placeholder="多行输入"
          :rows="4"
          v-decorator="[
            `menuparameter`,
            {
              rules: [{ required: true, message: '输入框不能为空!' }],
              initialValue: fromData.params || '',
            },
          ]"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
export default {
  data() {
    return {
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'form_in_modal' })
  },
  props: {
    visible: {
      default: false,
    },
    dataRef: {
      type: Object,
    },
    rowData: {
      type: String,
    },
  },
  computed: {
    winTitle: function () {
      return this.rowData != 'undefined' ? '修改菜单' : '新增菜单'
    },
    fromData() {
      return Object.assign({}, this.dataRef)
    },
  },
  watch: {
    fromData(newValue) {
      this.form.setFieldsValue({
        menuid: newValue.id,
        menuname: newValue.name,
        menuurl: newValue.url,
        menuicon: newValue.icon,
        menuserial: newValue.priority,
        menuparameter: newValue.params,
      })
    },
  },
  methods: {
    handleOk() {
      this.$emit('success')
    },
    handleCancel() {
      this.$emit('cancel')
    },
  },
}
</script>


<style lang="less" scoped>
</style>
