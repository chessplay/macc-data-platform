<template>
  <div>
    <!-- <div class="title">
      <h1>{{winTitle}}</h1>
    </div> -->
    <a-form :form="form" :modal="myFormData" ref="formone" style="position:relative;">
      <div style="display:flex;width:600px;justify-content:center">
        <a-form-item class="center">
          <a-button type="primary" @click="handleOk">
            {{winTitle}}
          </a-button>
        </a-form-item>
        <a-form-item class="center">
          <a-button :ghost="true" type="primary" @click="toTest()">测试运行数据模型</a-button>
        </a-form-item>

      </div>
      <div style="display:flex;margin-left:7vw">
        <div style="width:450px;margin-top:30px">
          <a-form-item v-bind="formItemLayout" class="inline" label="数据模型ID：">
            <a-input :disabled="!!myFormData.modelId" v-decorator="[
              'modelId',
              {
                initialValue:myFormData.modelId,
                rules: [{ required: true, message: '请输入数据模型ID！' }]
              }
            ]" />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" class="inline" label="数据源名称：">
            <a-select v-decorator="[
              'dataSourceId',
              {
                initialValue:myFormData.dataSourceId,
                rules: [{ required: true, message: '请输入数据源ID！' }],
              },
            ]">
              <a-select-option v-for="item in dataSourceID" :key="item.id" :value="item.id">
                {{item.name}}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" class="inline" label="数据模型名：">
            <a-input v-decorator="[
              'modelName',
              {
                initialValue:myFormData.modelName,
                rules: [{ required: true, message: '请输入数据模型名称！' }],
              },
            ]" />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" class="inline" label="所属模块ID：">
            <a-select v-decorator="[
              'moduleId',
              {
                initialValue:myFormData.moduleId,
                rules: [{ required: true, message: '请选择所属模块ID！' }]
              }
            ]">
              <a-select-option v-for="item in moduleId" :key="item" :value="item">
                {{item}}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" class="inline" label="查询类型：">
            <a-select @change="getType" v-decorator="[
              'queryType',
              {
                initialValue:myFormData.queryType,
                rules: [{ required: true, message: '请选择查询类型！' }],
              },
            ]">
              <a-select-option v-for="item in queryType" :key="item" :value="item">
                {{item}}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" class="inline" label="备 注 ： ">
            <a-textarea v-decorator="[
              'descript',
              {
                initialValue:myFormData.description,
              }
            ]"></a-textarea>
          </a-form-item>
          <!-- *************参数设置************* -->
          <div>
            <div style="display:flex;margin:10px 0 5px 24px ">
              <h3 style="line-height:40px">Params 参数设置</h3>
              <a-form-item style="text-align:center">
                <a-button type="dashed" @click="add" style="width:100%;margin-left:15px">
                  <a-icon type="plus" /> 添加Params参数
                </a-button>
              </a-form-item>
            </div>

            <div style="margin-left:-50px">
              <div class="tableHead">
                <div class="require">字段名</div>
                <div class="require">必传</div>
                <div class="require">类型</div>
                <div>描述</div>
              </div>
              <a-form-item v-for="(k, index) in form.getFieldValue('keys')" :key="k">
                <div style="margin:0;padding:0">
                  <a-form-item :style="paramsStyle" style="margin-right:10px">
                    <a-input v-decorator="[
              `name[${k}]`,
              {
                initialValue:((myFormData.params && myFormData.params[index]) ? myFormData.params[index].name : undefined),
                rules: [{ required: true, message: '字段名必填' }],
              },
            ]" />
                  </a-form-item>
                  <a-form-item :style="paramsStyle3">
                    <a-radio-group name="radioGroup" v-decorator="[
              `require[${k}]`,
              {
                initialValue:((myFormData.params && myFormData.params[index]) ? myFormData.params[index].require : undefined),
                rules: [{ required: true, message: '选择是否必传' }]
              },
            ]">
                      <a-radio :value="true">
                        是
                      </a-radio>
                      <a-radio :value="false">
                        否
                      </a-radio>
                    </a-radio-group>

                  </a-form-item>
                  <a-form-item :style="paramsStyle">
                    <a-select v-decorator="[
              `type[${k}]`,
              {
                initialValue:((myFormData.params && myFormData.params[index] && myFormData.params[index].name) ? myFormData.params[index].type : undefined),
                rules: [{ required: true, message: '字段类型必选' }],
              },
            ]">
                      <a-select-option v-for="item in parmasType" :key="item" :value="item">
                        {{item}}
                      </a-select-option>
                    </a-select>

                  </a-form-item>
                  <a-form-item :style="paramsStyle2">
                    <a-input v-decorator="[
             `description[${k}]`,
              {
                initialValue:((myFormData.params && myFormData.params[index]) ? myFormData.params[index].description : undefined),
              },
            ]"></a-input>
                    <a-icon v-if="form.getFieldValue('keys').length > 1" class="icon" type="minus-circle-o"
                      :disabled="form.getFieldValue('keys').length === 1" @click="() => remove(k)" />
                  </a-form-item>
                </div>
              </a-form-item>
            </div>
          </div>

        </div>

        <div style="width: 700px;">
          <a-tabs>
            <a-tab-pane key="1" tab="SQL脚本（必填）">
              <a-form-item>
                <a-textarea class="SQL" v-decorator="[
              'sqlScript',
              {
                initialValue:myFormData.sqlScript,
                rules: [{ required: true, message: '请输入SQL脚本！' }],
              }
            ]"></a-textarea>
              </a-form-item>
            </a-tab-pane>
            <a-tab-pane key="2" tab="计算数量的SQL脚本（必填）" v-if="isPage" force-render>
              <a-form-item v-if="isPage">
                <a-textarea class="SQL" v-decorator="[
              'countSqlScript',
              {
                initialValue:myFormData.countSqlScript,
                rules: [{ required: isPage, message: '请输入计算数量的SQL脚本！' }],
              },
            ]"></a-textarea>
              </a-form-item>
            </a-tab-pane>
          </a-tabs>
        </div>
      </div>
    </a-form>
  </div>
</template>

<script>
let id = 0
export default {
  name: 'addDatasource',
  props: {
    visible: {
      default: false,
    },
    queryType: {
      type: Array
    },
    formData: {
      type: Object
    },
    parmasType: {
      type: Array
    },
    dataSourceID: {
      type: Array
    },
    moduleId: {
      type: Array
    }
  },
  data () {
    return {
      paramsStyle: { position: 'relative', display: 'inline-block', width: '90px', },
      paramsStyle2: {
        display: 'inline-block', position: 'relative', width: '130px', marginLeft: '5px'
      },
      paramsStyle3: {
        position: 'relative', display: 'inline-block', width: '105px', padding: '0'
      },
      confirmLoading: false,
      formItemLayout: {
        labelCol: {
          xs: { span: 18 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 12 },
          sm: { span: 15 },
        },
      },
      formItemLayout2: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 7 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 13 },
        },
      },
      // formItemLayout3: {
      //   labelCol: {
      //     xs: { span: 18 },
      //     sm: { span: 0 },
      //   },
      //   wrapperCol: {
      //     xs: { span: 18 },
      //     sm: { span: 5 },
      //   },
      // },
      // formItemLayout4: {
      //   labelCol: {
      //     xs: { span: 12 },
      //     sm: { span: 4 },
      //   },
      //   wrapperCol: {
      //     xs: { span: 12 },
      //     sm: { span: 16 },
      //   },
      // },
      isPage: false,
      myFormData: {
        dataSourceId: "",
        modelId: "",
        modelName: "",
        moduleId: "",
        params: [],
        queryType: "",
        sqlScript: "",
        countSqlScript: "",
        description: "",
      }
    };
  },
  beforeCreate () {
    this.form = this.$form.createForm(this, { name: 'form_in_modal' })
    this.form.getFieldDecorator('keys', { initialValue: [], preserve: true });
  },
  watch: {
    formData: {
      immediate: true,
      handler (newv, oldv) {
        this.$nextTick(() => {
          if (newv.params) {
            // let params = JSON.parse(newv.queryParams)
            // newv.params = params
            for (let item in newv.params) {
              if (newv.params[item].name) {
                this.add()
              }
            }
          } else {
            this.add()
          }
        })
        if (newv.queryType == 'PAGE') {
          this.getType('PAGE')
        } else {
          this.getType()
        }
        this.myFormData = newv
      }
    }

  },
  computed: {
    winTitle: function () {
      return !this.formData.modelId ? "添加数据模型" : "更新数据模型"
    },
  },
  methods: {
    getType (e) {
      this.isPage = e == "PAGE" ? true : false
    },
    handleCancel () {
      this.$emit('cancel')
    },

    handleOk () {
      this.$emit('addData')
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

    toTest () {
      this.$emit('toTest')
    },

  },
}

</script>
<style scoped>
.ant-form-item {
  margin-bottom: 3px;
}
#testBtn {
  position: absolute;
  bottom: 10px;
}
.title {
  text-align: center;
  margin: 25px 0 40px 0;
  font-size: 18px;
}
.center {
  margin: 10px;
}
.icon {
  position: absolute;
  top: 0px;
  right: -35px;
}
.inline {
  display: inline-block;
  margin-bottom: 15px;
  width: 400px;
}
.SQL {
  min-width: 350px;
  height: 460px;
}
.ant-radio-wrapper {
  margin: 0;
}
.tableHead {
  display: flex;
  width: 89%;
  margin-bottom: 8px;
  justify-content: center;
}
.tableHead div {
  flex: 1;
  font-weight: bold;
  font-size: 13px;
}
.require::before {
  display: inline-block;
  margin-right: 4px;
  color: #f5222d;
  font-size: 14px;
  content: '*';
  line-height: 1;
  font-family: SimSun, sans-serif;
}
</style>
