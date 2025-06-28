<template>
  <a-modal
    :visible="visible"
    :title="winTitle"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form layout="vertical" :form="form">
      <a-form-item v-bind="formItemLayout">
        <span
          v-decorator="[
            'accountid',
            {
              initialValue: nextdata.accountid,
            },
          ]"
        >
        </span>
      </a-form-item>
      <a-form-item :label="columns[1].title" v-bind="formItemLayout">
        <span
          class="ant-form-text"
          style="font-size:25px"
          v-decorator="[
            'accountname',
            {
              initialValue: nextdata.accountname,
            },
          ]"
        >
          {{ nextdata.accountname }}
        </span>
      </a-form-item>
      <a-form-item :label="columns[2].title" v-bind="formItemLayout">
        <a-select
          v-decorator="[
            'accountstate',
            {
              initialValue: nextdata.accountstate || '',
              rules: [{ required: true, message: '请选择类别!' }],
            },
          ]"
        >
          <a-select-option
            v-for="(item, index) in statearr"
            :key="index"
            :value="item"
            >{{ item }}</a-select-option
          >
        </a-select>
      </a-form-item>
      <a-form-item :label="columns[3].title" v-bind="formItemLayout">
        <a-select
          v-decorator="[
            'roleid',
            {
              initialValue: nextdata.roleid || '',
              rules: [{ required: true, message: '请选择类别!' }],
            },
          ]"
        >
          <a-select-option
            v-for="(item, index) in roleidarr"
            :key="index"
            :value="item.roleId"
            >{{ item.roleId }}</a-select-option
          >
        </a-select>
      </a-form-item>
      <a-form-item :label="columns[4].title" v-bind="formItemLayout">
        <a-textarea
          placeholder="多行输入"
          :rows="4"
          v-decorator="[
            `accountdescribe`,
            {
              initialValue: nextdata.accountdescribe || '',
            },
          ]"
        />
      </a-form-item>
      <a-form-item :label="columns[5].title" v-bind="formItemLayout">
        <span class="ant-form-text">
          {{ nextdata.accounttime }}
        </span>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
// import EventService from '@p/services/eventService.js'
const statearr = ["ACCESS", "DENY", "UNCONFIRMED"];
export default {
  data() {
    return {
      statearr,
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
    };
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "form_in_modal" });
  },
  props: {
    visible: {
      default: false,
    },
    columns: {
      type: Array,
    },
    rowData: {
      type: String,
    },
    accountnext: {
      type: Object,
    },
    roleidarr: {
      type: Array,
    },
  },
  computed: {
    winTitle: function() {
      return this.rowData != "undefined" ? "账号详情" : "新增账号";
    },
    nextdata: function() {
      let value = Object.assign({}, this.accountnext);
      console.log(this.accountnext, 1111);
      return value;
    },
  },
  methods: {
    handleOk() {
      this.$emit("success");
    },
    handleCancel() {
      this.$emit("cancel");
    },
  },
};
</script>

<style lang="less" scoped></style>
