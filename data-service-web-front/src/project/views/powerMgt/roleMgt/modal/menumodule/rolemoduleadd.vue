<template>
  <a-modal :visible="visible" :title="winTitle" @ok="handleOk" @cancel="handleCancel">
      <a-form layout="vertical" :form="form">
      <a-form-item label="模块ID" v-bind="formItemLayout">
        <a-input
        v-decorator="[
              `moduleid`,
              {
                rules: [
                  { required: true, message: '输入框不能为空!' },
                  {validator: moduleidchange}
                ],
                initialValue: nextdata.id || '',
              },
            ]"/>
      </a-form-item>
      <a-form-item label="模块名称" v-bind="formItemLayout">
        <a-input 
        v-decorator="[
              `modulename`,
              {
                rules: [
                  { required: true, message: '输入框不能为空!' },
                  {validator: modulenamechange}
                ],
                initialValue: nextdata.name || '',
              },
            ]"/>
      </a-form-item>
      <a-form-item label="参数" v-bind="formItemLayout">
        <a-textarea placeholder="多行输入" :rows="4"
        v-decorator="[
              `moduleparameter`,
              {
                rules: [
                  { required: true, message: '输入框不能为空!' },
                ],
                initialValue: nextdata.description || '',
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
    rowData:{
      type:String,
    },
    next:{
      type:Object,
    }
  },
  computed:{
    winTitle: function () {
      return this.rowData!='undefined' ? '编辑模块' : '新增模块'
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
    moduleidchange(rule, value, callback){
      if(value == "admin"){
        callback("该角色ID已存在！！！")
      }else{
        callback()
      }
    },
    modulenamechange(rule, value, callback){
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