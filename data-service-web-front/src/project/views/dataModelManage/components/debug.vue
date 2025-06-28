<template>
  <div>
    <div>
      <a-form
        :form="form"
        :modal="myFormData"
        ref="debugForm"
        style="position:relative;"
      >
        <a-form-item style="margin-left:140px">
          <a-button type="primary" @click="submitDebug">
            测试运行数据模型
          </a-button>
        </a-form-item>
        <!-- <div class="title">
          <h1>测试运行数据模型</h1>
        </div> -->
        <div style="display:flex;">
          <div style="width:450px;margin:20px 0 0 130px;position:relative;">
            <a-form-item label="数据源名称" v-bind="formItemLayout">
              <a-select
                v-decorator="[
                  'dataSourceId',
                  {
                    initialValue: myFormData.dataSourceId,
                    rules: [{ required: true, message: '请输入数据源ID！' }],
                  },
                ]"
              >
                <a-select-option
                  v-for="item in dataSourceID"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="查询类型" v-bind="formItemLayout">
              <a-select
                @change="getType"
                v-decorator="[
                  'queryType',
                  {
                    initialValue: myFormData.queryType,
                    rules: [{ required: true, message: '请选择查询类型！' }],
                  },
                ]"
              >
                <a-select-option
                  v-for="item in queryType"
                  :key="item"
                  :value="item"
                >
                  {{ item }}
                </a-select-option>
              </a-select>
            </a-form-item>

            <!-- *************参数设置************* -->
            <div style="position: relative;margin-top:40px">
              <!-- <div class="center">
                <h3 style=" line-height:40px">Params 参数</h3>
              </div> -->
              <a-table
                :columns="columns"
                :data-source="params"
                size="middle"
                :pagination="false"
                style="width:450px;"
                :rowKey="
                  (record, index) => {
                    return index
                  }
                "
              >
                <div slot="SQL">
                  <div
                    :style="{
                      display: 'inline-block',
                      width: '80px',
                      margin: '0 2px',
                    }"
                  ></div>

                  <div
                    :style="{
                      display: 'inline-block',
                      width: '80px',
                      margin: '0 2px',
                    }"
                  ></div>
                </div>
              </a-table>

              <!-- key value -->
              <div class="key-value">
                <a-form-item
                  v-for="(k, index) in form.getFieldValue('keys')"
                  :key="k"
                >
                  <a-form-item
                    :style="{
                      display: 'inline-block',
                      width: '160px',
                      margin: '0 5px',
                      height: '23px',
                    }"
                  >
                    <a-input
                      v-decorator="[
                        `value[${k}]`,
                        {
                          rules: [
                            {
                              required: myFormData.params[index].require,
                              message: '',
                            },
                          ],
                        },
                      ]"
                      placeholder="value"
                      ref="myValue"
                    />
                  </a-form-item>
                  <a-form-item
                    :style="{
                      display: 'inline-block',
                      width: '0px',
                      visibility: 'hidden',
                      position: 'absolute',
                    }"
                  >
                    <a-input
                      ref="myKey"
                      v-decorator="[
                        `key[${k}]`,
                        {
                          initialValue: myFormData.params[index]
                            ? myFormData.params[index].name
                            : undefined,
                        },
                      ]"
                      placeholder="key"
                    />
                  </a-form-item>
                </a-form-item>
              </div>
              <!-- {rules: [{ required: (myFormData.params[index].require), message: 'Value值必传' }],} -->
            </div>
          </div>
          <div style="width: 700px;">
            <a-tabs>
              <a-tab-pane key="1" tab="SQL脚本（必填）">
                <a-form-item>
                  <a-textarea
                    class="SQL"
                    v-decorator="[
                      'sqlScript',
                      {
                        initialValue: myFormData.sqlScript,
                        rules: [{ required: true, message: '请输入SQL脚本！' }],
                      },
                    ]"
                  ></a-textarea>
                </a-form-item>
              </a-tab-pane>
              <a-tab-pane
                key="2"
                tab="计算数量的SQL脚本（必填）"
                v-if="isPage"
                force-render
              >
                <a-form-item v-if="isPage">
                  <a-textarea
                    class="SQL"
                    v-decorator="[
                      'countSqlScript',
                      {
                        initialValue: myFormData.countSqlScript,
                        rules: [
                          {
                            required: isPage,
                            message: '请输入计算数量的SQL脚本！',
                          },
                        ],
                      },
                    ]"
                  ></a-textarea>
                </a-form-item>
              </a-tab-pane>
            </a-tabs>
          </div>
        </div>
      </a-form>

      <div v-if="flag" style="margin-top:50px">
        <h2 class="center">测试成功返回数据</h2>
        <a-empty v-if="!dataList" />
        <div class="flex">
          <!-- <pre>{{dataList}}</pre> -->
          <pre v-html="dataList"></pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const columns = [
  {
    title: 'Params 参数',
    children: [
      {
        title: '字段名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '是否必传',
        dataIndex: 'require',
        key: 'require',
      },
      {
        title: '类型',
        dataIndex: 'type',
        key: 'type',
      },
      {
        title: '参数值',
        scopedSlots: { customRender: 'SQL' },
      },
    ],
  },
]
let id = 0
import Service from '@p/services/dbService'
export default {
  name: 'debug',
  props: {
    reData: {
      type: Object,
    },
    queryType: {
      type: Array,
    },
    dataSourceID: {
      type: Array,
    },
  },
  data() {
    return {
      myFormData: {},
      flag: false,
      columns,
      params: [],
      id,
      status: '',
      isPage: false,
      dataList: '',
      formItemLayout: {
        labelCol: {
          xs: { span: 18 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 12 },
          sm: { span: 12 },
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
      paramsStyle: {
        position: 'relative',
        display: 'inline-block',
        width: '90px',
      },
      paramsStyle3: {
        position: 'relative',
        display: 'inline-block',
        width: '60px',
        marginRight: '10px',
      },
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'form_in_modal' })
    this.form.getFieldDecorator('keys', { initialValue: [], preserve: true })
  },
  watch: {
    reData(v) {
      let params = JSON.stringify(v.params)
      params = JSON.parse(params)
      for (let item in params) {
        params[item].require = params[item].require ? '是' : '否'
      }
      this.params = params
      const form2 = this.$refs.debugForm.form
      form2.resetFields()
      if (v.queryType == 'PAGE') {
        this.getType('PAGE')
      } else {
        this.getType()
      }
      for (let item in v.params) {
        this.add()
      }
      this.myFormData = v
      this.flag = false
    },
  },
  methods: {
    // test
    remove(k) {
      const { form } = this
      // can use data-binding to get
      const keys = form.getFieldValue('keys')
      // We need at least one passenger
      if (keys.length === 0) {
        return
      }
      // can use data-binding to set
      form.setFieldsValue({
        keys: keys.filter((key) => key !== k),
      })
    },

    add() {
      const { form } = this
      // can use data-binding to get
      const keys = form.getFieldValue('keys')
      const nextKeys = keys.concat(id++)
      // can use data-binding to set
      // important! notify form to detect changes
      form.setFieldsValue({
        keys: nextKeys,
      })
    },
    getType(e) {
      this.isPage = e == 'PAGE' ? true : false
    },
    /**
     * @description:发送测试运行数据模型网络请求
     * @param
     * @returns
     */
    async submitDebug() {
      let form = this.$refs.debugForm.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        let params = {}
        for (let key in values.key) {
          params[values.key[key]] = values.value[key]
        }
        console.log(values)
        console.log(params)
        delete values.key
        delete values.keys
        delete values.value
        values.params = params
        let res = await Service.dynamicExecuteTest(values)
        // let page_index = 1; let page_size = 20
        // const res = await Service.getQuerymodelPaging({ page_index, page_size })
        // this.dataList = res.dataList
        if (res.code == 0) {
          this.$message.success('测试成功！')
          // this.dataList = res;
          this.dataList = this.jsonFormat(res)
          this.flag = true
        } else {
          this.$error({
            title: '错误',
            content: res.msg,
          })
          this.flag = false
        }
      })
    },
    jsonFormat(jsonTemp) {
      let json = ''
      try {
        if (typeof jsonTemp != 'string') {
          json = JSON.stringify(jsonTemp, undefined, 2)
        } else {
          json = JSON.stringify(JSON.parse(jsonTemp), undefined, 2)
        }
        let jsonObj = JSON.parse(json)
        if (typeof jsonObj === 'object') {
          json = json
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
          return json.replace(
            /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
            (match) => {
              let cls = 'json-number'
              if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                  cls = 'json-key'
                } else {
                  cls = 'json-string'
                }
              } else if (/true|false/.test(match)) {
                cls = 'json-boolean'
              } else if (/null/.test(match)) {
                cls = 'json-null'
              }
              return '<span class="' + cls + '">' + match + '</span>'
            }
          )
        } else {
          return jsonTemp
        }
      } catch (e) {
        return jsonTemp
      }
    },
  },
}
</script>
<style scoped>
.title {
  text-align: center;
  margin: 25px 0 40px 0;
  font-size: 18px;
}

.SQL {
  min-width: 350px;
  height: 460px;
}
.ant-form-item {
  margin: 6px 10px;
}
.center {
  text-align: center;
}
.tableHead {
  display: flex;
  width: 80%;
  margin-bottom: 8px;
  justify-content: center;
}
.tableHead div {
  flex: 1;
  font-weight: bold;
  font-size: 13px;
}
.flex {
  display: flex;
  justify-content: center;
  /* color: rgb(119, 39, 18); */
  margin-top: 25px;
}
.flex pre {
  padding: 25px;
  min-width: 500px;
  max-width: 800px;
  max-height: 600px;
  background-color: rgb(252, 252, 252);
  border: 1px solid rgb(225, 225, 232);
  /* white-space: pre-wrap;
  word-wrap: break-word; */
}
.key-value {
  position: absolute;
  top: 88px;
  right: 20px;
}
</style>

<style>
.json-number {
  color: #b57323;
}
.json-string {
  color: #008000;
}
.json-boolean {
  color: #831883;
}
.json-null {
  color: #cc33cc;
}
.json-key {
  color: #424456;
}
</style>
