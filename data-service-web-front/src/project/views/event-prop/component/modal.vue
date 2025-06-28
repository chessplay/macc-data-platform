<template>
  <a-modal :visible="visible" title="新增属性" okText="确定" @cancel="handleCancel" @ok="handleCreate">
    <a-form layout="vertical" :form="form">
      <a-form-item v-bind="formItemLayout" label="名称：">
        <a-input v-decorator="[
            'name',
            {
              rules: [{ required: true, message: '请输入名称!' }],
            },
          ]" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="类别：">
        <a-select v-decorator="[
            'type',
            { rules: [{ required: true, message: '请选择类型！' }] },
          ]" placeholder="请选择类型！" @change="handleSelectChange">
          <a-select-option value="int"> int </a-select-option>
          <a-select-option value="double"> double </a-select-option>
          <a-select-option value="string"> string </a-select-option>
          <a-select-option value="json"> json </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="是否需要转化：">
        <a-switch v-decorator="['needParse', { valuePropName: 'checked' }]" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="默认值：">
        <a-input v-decorator="[
            'defaultValue',
            {
              rules: [{ required: true, message: '请输入默认值!' }],
            },
          ]" />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="备注：">
        <a-input v-decorator="['remark']" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
export default {
  props: {
    visible: {
      default: false,
    },
  },
  data() {
    return {
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
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'form_in_modal' })
  },
  methods: {
    handleSelectChange(value) {
      if (value === 'int') {
        this.form.setFieldsValue({
          defaultValue: 0,
        })
      } else if (value === 'double') {
        this.form.setFieldsValue({
          defaultValue: '0.0',
        })
      } else if (value === 'string') {
        this.form.setFieldsValue({
          defaultValue: '""',
        })
      } else if (value === 'json') {
        this.form.setFieldsValue({
          defaultValue: '[]',
        })
      }
    },

    handleCancel() {
      this.$emit('cancel')
    },

    handleCreate() {
      this.$emit('create')
    },
  },
}
</script>