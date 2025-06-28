<template>
  <div>
    <a-modal :title="winTitle" :maskClosable="false" :width="800" :visible="visible" :confirm-loading="confirmLoading"
      @ok="handleOk" @cancel="handleCancel">
      <a-form :form="form" :modal="myFormData" ref="formone">
        <a-tabs default-active-key="1">
          <a-tab-pane key="1" tab="基本参数">
            <a-form-item v-bind="formItemLayout" v-if="myFormData.id" label="数据源ID：">
              <a-input :disabled="!!myFormData.id" v-decorator="[
              'id',
              {
                initialValue:myFormData.id,
                rules: [{ required: true, message: '请输入数据源ID！' }]
              }
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="数据源名称：">
              <a-input v-decorator="[
              'name',
              {
                initialValue:myFormData.name,
                rules: [{ required: true, message: '请输入数据源名称！' }],
              },
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="jdbc连接地址：">
              <a-input v-decorator="[
              'jdbcUrl',
              {
                initialValue:myFormData.jdbcUrl,
                rules: [{ required: true, message: '请输入jdbc连接地址！' }],
              },
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="数据库类型：">
              <a-select v-decorator="[
              'dbType',
              {
                initialValue:myFormData.dbType,
                rules: [{ required: true, message: '请选择数据库类型！' }],
              },
            ]">
                <a-select-option v-for="item in getDBType" :key="item" :value="item">
                  {{item}}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="用 户 名 ：">
              <a-input v-decorator="[
              'userName',
              {
                initialValue:myFormData.userName,
              }
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="密 码 ：">
              <a-input v-decorator="[
              'password',
              {
                initialValue:myFormData.password,
              }
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label=" 	jdbc schema：">
              <a-input v-decorator="[
              'dbschema',
              {
                initialValue:myFormData.dbschema,
              },
            ]" />
            </a-form-item>
            <!-- *********动态增加 驱动属性***********-->
            <label style="margin-left:123px">驱动属性：</label>
            <a-form-item v-for="(k,index) in form.getFieldValue('keys')" :key="k"
              :style="{textAlign:'left',margin:'0 0  0 190px'}">
              <a-form-item :style="{display:'inline-block',width:'calc(60% - 10px)',margin:'0 10px'}">
                <a-input ref="myKey"
                  v-decorator="[
              `key[${k}]`,{initialValue:((myFormData.driverProperties && myFormData.driverProperties[index]) ? myFormData.driverProperties[index].key : undefined),rules:[{ required: true, message: '请输入key值！' }]}]"
                  placeholder="key" />
              </a-form-item>

              <a-form-item :style="{display:'inline-block',width:'calc(50% - 178px)',margin:'0 10px'}">
                <a-input
                  v-decorator="[
              `value[${k}]`,{initialValue:((myFormData.driverProperties && myFormData.driverProperties[index]) ? myFormData.driverProperties[index].value : undefined),rules:[{ required: true, message: '请输入value值！' }]}]"
                  placeholder="value" ref="myValue" />
              </a-form-item>

              <a-icon v-if="form.getFieldValue('keys').length > 0" type="minus-circle-o" :style="{margin:'15px'}"
                :disabled="form.getFieldValue('keys').length === 0" @click="() => remove(k)" />
            </a-form-item>

            <a-form-item v-bind="formItemLayoutWithOutLabel">
              <a-button type="dashed" @click="add" style="width:100%">
                <a-icon type="plus" /> 添加键值对
              </a-button>
            </a-form-item>

            <!-- ************************************** -->
            <a-form-item v-bind="formItemLayout" label="备 注 ： ">
              <a-textarea v-decorator="[
              'description',
              {
                initialValue:myFormData.description,
              }
            ]"></a-textarea>
            </a-form-item>
          </a-tab-pane>
          <a-tab-pane key="2" tab="更多参数" force-render>
            <a-form-item v-bind="formItemLayout2" label="空闲存活时间(ms) : idle-timeout">
              <a-input v-decorator="[
              'idleTimeout',
              {
                initialValue:(myFormData.idleTimeout ? myFormData.idleTimeout : 600000),
                rules: [{ required: true, message: '请输入空闲连接存活时间！' }],
              },
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout2" label="最大存活时间(ms) : max-lifetime">
              <a-input v-decorator="[
              'maxLifeTime',
              {
                initialValue:(myFormData.maxLifeTime ? myFormData.maxLifeTime : 1800000),
                rules: [{ required: true, message: '请输入最大存活时间！' }],
              },
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout2" label="连接超时时间(ms) :  connection-timeout">
              <a-input v-decorator="[
              'connectionTimeout',
              {
                initialValue:(myFormData.connectionTimeout ? myFormData.connectionTimeout : 30000),
                rules: [{ required: true, message: '请输入连接超时时间！' }],
              },
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout2" label="最大连接数 ： maximum-pool-size">
              <a-input v-decorator="[
              'maximumPoolSize',
              {
                initialValue:(myFormData.maximumPoolSize ? myFormData.maximumPoolSize : 5),
                rules: [{ required: true, message: '请输入最大连接数' }],
              },
            ]" />
            </a-form-item>
            <a-form-item v-bind="formItemLayout2" label="最小空闲连接数： minimum-idle">
              <a-input v-decorator="[
              'minimumIdle',
              {
                initialValue:(myFormData.minimumIdle ? myFormData.minimumIdle : 1),
                rules: [{ required: true, message: '请输入最小空闲连接数！' }],
              },
            ]" />
            </a-form-item>
          </a-tab-pane>
        </a-tabs>
      </a-form>
      <a-button id="testBtn" :ghost="true" type="primary" @click="toTest()">测试数据源可用性</a-button>
    </a-modal>
  </div>
</template>

<script>
let id = 0;
export default {
  name: 'addDatasource',
  props: {
    visible: {
      default: false,
    },
    getDBType: {
      type: Array
    },
    formData: {
      type: Object
    }
  },
  data () {
    return {
      confirmLoading: false,
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 20 },
          sm: { span: 16 },
        },
      },
      formItemLayout2: {
        labelCol: {
          xs: { span: 12 },
          sm: { span: 10 },
        },
        wrapperCol: {
          xs: { span: 10 },
          sm: { span: 11 },
        },
      },
      formItemLayoutWithOutLabel: {
        wrapperCol: {
          xs: { span: 12 },
          sm: { span: 12, offset: 8 },
        },
      },
      myFormData: {
        name: "",
        dbType: "",
        jdbcUrl: "",
        userName: "",
        password: "",
        minimumIdle: "",
        maximumPoolSize: "",
        idleTimeout: "",
        connectionTimeout: "",
        maxLifeTime: "",
        description: "",
        driverProperties: [],
        driverPropertiesJson: "",
        dbschema: ""
      }
    };
  },
  beforeCreate () {
    this.form = this.$form.createForm(this, { name: 'form_in_modal' })
    this.form.getFieldDecorator('keys', { initialValue: [], preserve: true });
  },
  watch: {
    deep: true,
    formData (v) {
      this.myFormData = v
      if (v.driverPropertiesJson) {
        // let driverProperties = JSON.parse(v.driverPropertiesJson)
        // this.myFormData.driverProperties = driverProperties
        for (let item in v.driverProperties) {
          this.add()
          // this.$nextTick(() => {
          //   this.$refs.myKey[item].value = driverProperties[item].key
          //   this.$refs.myValue[item].value = driverProperties[item].value
          // })
        }
      }
    }
  },
  computed: {
    winTitle: function () {
      return !this.formData.id ? "添加数据源" : "更新数据源"
    },
  },
  methods: {
    handleCancel () {
      this.$emit('cancel')
    },

    handleOk () {
      this.$emit('addData')
    },

    toTest () {
      this.$emit('toTest')
    },

    // test
    remove (k) {
      const { form } = this;
      // can use data-binding to get
      const keys = form.getFieldValue('keys');
      // We need at least one passenger
      if (keys.length === 0) {
        return;
      }
      // can use data-binding to set
      form.setFieldsValue({
        keys: keys.filter(key => key !== k),
      });
    },

    add () {
      const { form } = this;
      // can use data-binding to get
      const keys = form.getFieldValue('keys');
      const nextKeys = keys.concat(id++);
      // can use data-binding to set
      // important! notify form to detect changes
      form.setFieldsValue({
        keys: nextKeys,
      });
    },
  },
}

</script>
<style scoped>
.ant-form-item {
  margin: 8px 10px;
}
#testBtn {
  position: absolute;
  bottom: 10px;
}
/* .driverProperties{

} */
</style>
