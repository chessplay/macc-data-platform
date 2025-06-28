<template>
  <div class="modalone">
    <a-modal :visible="visible" title="新增一级类别" okText="确定" @cancel="handleCancel" @ok="handleCreate">
      <a-form layout="vertical" :form="form" ref="formone">
        <a-form-item v-bind="formItemLayout" label="一级类别：">
          <a-input v-decorator="[
              'type1',
              {
                rules: [{ required: true, message: '请输入类别名称！' }],
              },
            ]" />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="二级类别：">
          <a-input v-decorator="[
              'type2',
              {
                rules: [{ required: true, message: '请输入类别名称！' }],
              },
            ]" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
export default {
  props: {
    visible: {
      default: false,
    },
  },
  data () {
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

  beforeCreate () {
    this.form = this.$form.createForm(this, { name: 'form_in_modal' })
  },

  methods: {
    handleCancel () {
      this.$emit('cancel')
    },

    handleCreate () {
      this.$emit('create')
    },
  },
}
</script>
<style >
.ant-form-vertical .ant-form-item-label {
  text-align: right;
  padding-right: 20px;
}
</style>