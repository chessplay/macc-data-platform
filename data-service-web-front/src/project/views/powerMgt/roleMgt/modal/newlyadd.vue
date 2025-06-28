<template>
  <a-modal :visible="visible" :title="winTitle" @ok="handleOk" @cancel="handleCancel">
      <a-form layout="vertical" :form="form">
      <a-form-item :label="columns[0].title" v-bind="formItemLayout">
        <a-input
        :readOnly="!!nextdata.id"
        v-decorator="[
              `id`,
              {
                rules: [
                  {required: true, message: '输入框不能为空!' },
                  {validator: changeinfotype}
                ],
                initialValue: nextdata.id || '',
              },
            ]"/>
      </a-form-item>
      <a-form-item :label="columns[1].title" v-bind="formItemLayout">
        <a-input
        v-decorator="[
              `name`,
              {
                rules: [
                  { required: true, message: '输入框不能为空!' },
                ],
                initialValue: nextdata.name || '',
              },
            ]"/>
      </a-form-item>
      <a-form-item :label="columns[2].title" v-bind="formItemLayout">
        <a-textarea placeholder="多行输入" :rows="4" 
        v-decorator="[
              `information`,
              {
                rules: [
                  { required: true, message: '输入框不能为空!' },
                ],
                initialValue: nextdata.information || '',
              },
            ]"/>
      </a-form-item>
      </a-form>
  </a-modal>
</template>

<script>
export default {
  data(){
    return{
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
  props:{
    visible:{
      default:false
    },
    columns:{
      type: Array,
    },
    rowData:{
      type:String,
    },
    next:{
      type:Object,
    }
  },
  computed:{
    winTitle: function () {
      return this.rowData!='' ? '修改角色' : '新增角色'
    },
    nextdata: function () {
      let value = Object.assign({}, this.next)
      return value
    },
  },
  methods:{
    handleOk() {
      this.$emit('success')   
    },
    handleCancel() {
      this.$emit('cancel')
    },
    changeinfotype(rule, value, callback){
      if(value == "admin"){
        callback("该角色ID已存在！！！")
      }else{
        callback()
      }
    },
  }
}
</script>

<style lang="less" scoped>

</style>