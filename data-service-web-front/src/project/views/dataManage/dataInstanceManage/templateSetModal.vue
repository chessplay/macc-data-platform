<!--
 * @Author: 'chenmengni'
 * @Date: 2024-12-16 13:41:17
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2025-02-08 15:24:00
 * @FilePath: \data-service-web\src\project\views\dataManage\dataInstanceManage\templateSetModal.vue
 * @Description: 
-->

<template>
  <div class="">
    <a-drawer
      width="1000px"
      :drawerStyle="{
        display: 'flex',
        'flex-direction': 'column',
      }"
      :bodyStyle="{
        flex: 1,
        padding: 0,
        height: 0,
        display: 'flex',
        'flex-direction': 'column',
      }"
      :title="getTitle"
      :visible="ifShowModal"
      :confirm-loading="confirmLoading"
      @close="handleCancel"
      @cancel="handleCancel"
      @ok="handleOk"
    >
      <div class="conf-warp">
        <a-form-model
          ref="form"
          :model="form"
          :rules="rules"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <div style="width: 65%">
            <a-form-model-item label="模板" prop="choosedTemplate">
              <a-select
                show-search
                option-filter-prop="children"
                :filter-option="filterOption"
                v-model="form.choosedTemplate"
                @change="changeTemplate"
              >
                <a-select-option
                  v-for="(item, index) in templateList"
                  :key="item.id"
                  :value="item.id"
                  :data-index="index"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
            <span class="step-title-left" v-show="startParamsArray.length > 0"
              >过滤条件</span
            >
            <!-- startParamsArray -->
            <!-- 0124调整- 根据数据类型限制对应的输入数据 -->
            <a-form-model-item
              v-for="item in startParamsArray"
              :key="item.key"
              :prop="item.key"
              :rules="getRulesByType(item)"
              :label="getLabelByKey(item)"
            >
              <template v-if="item.key == 'partition_spec'">
                <a-input
                  v-model="form[item.key]"
                  autocomplete="off"
                  :placeholder="item.type"
                />
              </template>

              <template v-else-if="item.type == 'IN' || item.type == 'LIST'">
                <a-input
                  v-model="form[item.key]"
                  autocomplete="off"
                  :placeholder="'以，隔开'"
                />
              </template>
              <template v-else-if="item.type == 'NULL'">
                <a-input v-model="form[item.key]" autocomplete="off" disabled />
              </template>
              <template
                v-else-if="item.type == 'bool' || item.type == 'BOOLEAN'"
              >
                <a-select
                  style="width: 100%"
                  v-model="form[item.key]"
                  placeholder="请选择"
                >
                  <a-select-option value="true"> TRUE </a-select-option>
                  <a-select-option value="false"> FALSE </a-select-option>
                </a-select>
              </template>

              <template
                v-else-if="item.type == 'TIMESTAMP' || item.type == 'DATE'"
              >
                <a-date-picker
                  show-time
                  style="width: 100%"
                  @change="
                    (date, dateStr) => timeDateChange(date, dateStr, item.key)
                  "
                />
              </template>

              <template v-else>
                <a-input
                  v-model="form[item.key]"
                  autocomplete="off"
                  :placeholder="'请输入'"
              /></template>
            </a-form-model-item>
          </div>
          <div class="one-area-top">
            <span class="one-title">表选择：</span>
          </div>
          <div class="area-one">
            <div class="map-area">
              <div class="one-area">
                <div class="table-area">
                  <!-- 源表 -->
                  <a-form-model-item label="源数据源键" prop="sourceDataKey">
                    {{ form.sourceDataKey }}
                  </a-form-model-item>
                </div>
                <div class="table-area">
                  <a-form-model-item label="源表" prop="sourceTableName">
                    {{ form.sourceTableName }}
                  </a-form-model-item>
                </div>
              </div>
              <div class="one-area">
                <div class="table-area">
                  <a-form-model-item label="目标数据源键" prop="sinkDataKey">
                    {{ form.sinkDataKey }}
                  </a-form-model-item>
                </div>
                <div class="table-area">
                  <a-form-model-item label="目标表" prop="sinkTableName">
                    {{ form.sinkTableName }}
                  </a-form-model-item>
                </div>
              </div>
            </div>
          </div>
          <div style="display: flex; gap: 16px">
            <div class="filter-left">
              <div class="one-area-top">
                <span class="one-title">过滤条件：</span>
              </div>
              <div>
                <div class="area-one" style="min-height: 160px">
                  <!-- 过滤条件 -->
                  <div class="filter-part">
                    <div class="filter-head">
                      <div>字段</div>
                      <div>操作类型</div>
                      <div>变量名</div>
                    </div>
                    <div
                      class="filter-one"
                      v-for="(item, index) in sourceQueryFilter"
                      :key="'filter' + index"
                    >
                      <a-select
                        v-model="sourceQueryFilter[index].field"
                        allowClear
                        showSearch
                        placeholder="请选择字段"
                        disabled
                      >
                        <a-select-option
                          v-for="paramItem in showSourceParamSelectJson"
                          :key="`filter${index}_${paramItem.timestamp}`"
                          :value="paramItem.columnName"
                          :data-columnType="paramItem.columnType"
                          :data-index="index"
                        >
                          {{ paramItem.columnName }}
                        </a-select-option>
                      </a-select>

                      <a-select
                        v-model="sourceQueryFilter[index].type"
                        allowClear
                        showSearch
                        placeholder="请选择操作类型"
                        disabled
                      >
                        <a-select-option
                          v-for="typeItem in item.fileterType"
                          :key="typeItem"
                          :value="typeItem"
                        >
                          {{ typeItem }}
                        </a-select-option>
                      </a-select>

                      <a-input
                        v-model="sourceQueryFilter[index].value"
                        autocomplete="off"
                        placeholder="请输入变量名"
                        disabled
                      />

                      <div class="item-col">
                        <div class="icon-box"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="filter-right">
              <div class="one-area-top" style="height: 21px">
                <!-- <a-button type="link" style="height: 21px" @click="showExample">
                  查看实例
                </a-button> -->
              </div>
              <div class="area-one" style="min-height: 160px">
                <span class="show-json-data" style="word-break: break-all">
                  {{ realInterfaceData }}
                </span>
              </div>
            </div>
          </div>

          <div class="one-area-top">
            <span class="one-title">映射关系：</span>
          </div>
          <div class="area-one map-param-area">
            <template v-if="mapJsonData.length > 0">
              <div style="height: 15px"></div>
              <div
                v-for="(item, index) in mapJsonData"
                class="one-map"
                :key="item.key"
              >
                <span
                  style="display: inline-block; width: 60px; line-height: 30px"
                  >第{{ index + 1 }}行</span
                >
                <div class="param-area">
                  <!-- <a-form-model-item
                    :prop="'mapJsonData.' + index + '.sourceParma'"
                    :rules="{
                      required: true,
                      message: '请选择',
                      trigger: ['change', 'blur'],
                    }"
                    :wrapperCol="{ span: 24 }"
                  >
                    <a-select
                      v-model="mapJsonData[index]['sourceParma']"
                      disabled
                    >
                      <a-select-option
                        v-for="item in showSourceParamSelectJson"
                        :key="`source${index}_${item.timestamp}`"
                        :value="item.columnName"
                      >
                        {{ item.columnName }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item> -->
                  <div class="mapping-param">
                    {{ mapJsonData[index]["sourceParma"] }}
                  </div>
                </div>
                <div class="singleArrow"></div>
                <div class="param-area">
                  <!-- <a-form-model-item
                    :prop="'mapJsonData.' + index + '.sinkParma'"
                    :rules="{
                      required: true,
                      message: '请选择',
                      trigger: ['change', 'blur'],
                    }"
                    :wrapperCol="{ span: 24 }"
                  >
                    <a-select
                      v-model="mapJsonData[index]['sinkParma']"
                      disabled
                    >
                      <a-select-option
                        v-for="item in shosinkParamSelectJsonw"
                        :key="`sink${index}_${item.timestamp}`"
                        :value="item.columnName"
                      >
                        {{ item.columnName }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item> -->
                  <div class="mapping-param">
                    {{ mapJsonData[index]["sinkParma"] }}
                  </div>
                </div>
                <div style="width: 70px; line-height: 35px"></div>
              </div>
            </template>
            <template v-else>
              <a-empty :image="simpleImage">
                <span slot="description"> 暂无 </span>
              </a-empty>
            </template>
          </div>
        </a-form-model>
      </div>
      <div class="foot">
        <a-space>
          <a-button @click="handleCancel"> 取消 </a-button>
          <a-button type="primary" @click="handleOk" :loading="formloading">
            确定
          </a-button>
        </a-space>
      </div>
    </a-drawer>
  </div>
</template>
 
 <script>
import { request } from "@/utils/request";

import {
  METADATE_DATASOURCE_NAME_LIST_GET,
  TASK_TEMPLATE_FETCH_POST,
  METADATE_TASK_TEMPLATE_GET,
  METADATE_TASK_START_POST,
} from "@/services/api";
import moment from "moment";
import { Empty } from "ant-design-vue";

let form = {
  id: "",
  sourceDataKey: "", //数据源键，
  sinkDataKey: "", //数据源键，
  sourceTableName: "", //源表名称
  sinkTableName: "", //目标表
  fieldMapping: {}, //字段映射
  mapJsonData: [],
  sourceQueryFilter: [], //过滤条件
  choosedTemplate: "",
};
export default {
  components: {},
  data() {
    return {
      simpleImage: Empty.PRESENTED_IMAGE_SIMPLE,
      timeout: 8000,
      timestamp: moment(),
      ifShowModal: false,
      confirmLoading: false,
      formloading: false,
      labelCol: { span: 8 },
      wrapperCol: { span: 16 },
      form: { ...form },
      submitForm: { ...form }, //表单数据是为了处理多次提交接口会有时间差，导致数据获取可能会错乱
      rules: {
        choosedTemplate: [
          {
            required: true,
            message: "请选择",
            trigger: ["blur", "change"],
          },
        ],
      },
      dbTypeJson: [], //数据源表数据
      sourceTableSelectJson: [], //表数据-源
      sinkTableSelectJson: [], //表数据-目标
      sourceParamSelectJson: [], //源表字段数据
      sinkParamSelectJson: [], //目标表字段数据
      mapJsonData: [], //界面映射显示数据

      sourceSearchVal: "", // 搜索的内容
      sourceFilterDataList: [], // 过滤的数据列表 -- 从dataList中过滤出含搜索内容的数据
      sourceRenderedOptions: [], // 已渲染的下拉列表

      sinkSearchVal: "", // 搜索的内容
      sinkFilterDataList: [], // 过滤的数据列表 -- 从dataList中过滤出含搜索内容的数据
      sinkRenderedOptions: [], // 已渲染的下拉列表

      handleState: "add", //当前操作状态  -add(新增)、edit（编辑）、clone（拷贝）

      ifInit: false, //编辑或拷贝状态进入，用户判断数据是否显示完毕，显示完毕后，需要将编辑、拷贝的映射数据回填界面
      moreFilter: true,
      sourceQueryFilter: [
        {
          field: [],
          type: [],
          columnType: "",
          constrained: "",
          value: "",
          isPrimaryKeys: false,
        },
      ],
      constraintList: [],
      realInterfaceData: "{}", //过滤条件对外显示json数据
      templateList: [],
      startParamsArray: [], //用户输入的过滤条件
    };
  },
  computed: {
    getTitle() {
      if (this.handleState == "clone") {
        return "拷贝";
      } else if (this.handleState == "edit") {
        return "编辑";
      } else {
        return "新增";
      }
    },
    // 获取源字段列表数据
    showSourceParamSelectJson() {
      this.sourceParamSelectJson.map((item) => {
        item["timestamp"] = moment();
      });
      return this.sourceParamSelectJson;
    },
    // 获取目标字段列表数据
    showsinkParamSelectJson() {
      this.sinkParamSelectJson.map((item) => {
        item["timestamp"] = moment();
      });
      return this.sinkParamSelectJson;
    },
  },
  watch: {},
  created() {},
  mounted() {},
  methods: {
    async getAllTemplateList() {
      const queryObj = {
        pageSize: 1000,
        pageNum: 1,
      };
      let params = {
        api: METADATE_TASK_TEMPLATE_GET,
        method: "get",
        querys: queryObj,
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      if (res.code == "200") {
        let { rows } = res;
        this.templateList = rows.map((item) => ({
          ...item,
        }));
      }
    },
    initPage() {
      this.form = {};
      this.submitForm = {};
      this.sourceTableSelectJson = [];
      this.sinkTableSelectJson = [];
      this.sourceParamSelectJson = [];
      this.sinkParamSelectJson = [];
      this.mapJsonData = [];
      this.startParamsArray = [];
      this.sourceQueryFilter = [];
      this.realInterfaceData = "{}";
      this.$refs.form.clearValidate();
    },
    openModal(state) {
      this.getAllTemplateList();
      // this.getSourceList();
      this.handleState = state;
      this.ifShowModal = true; //显示
      this.$refs.form?.clearValidate();
    },
    handleCancel() {
      this.initPage();
      this.moreFilter = false;
      this.ifShowModal = false;
    },
    handleOk() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          let params = {
            ...this.form,
            sourceQueryFilter: this.sourceQueryFilter,
            description: "",
            taskParams: {},
          };
          // 特殊处理   startParamsArray
          this.startParamsArray.map((item) => {
            if (item.type == "IN" || item.type == "LIST") {
              params[item.key] = params[item.key].split(",");
            }
            params.taskParams[item.key] = params[item.key];
          });
          // 新增
          delete params.id;
          this.addMethod(params);
        } else {
          this.$message.warning("请填写数据");
        }
      });
    },
    // 获取数据源列表
    async getSourceList() {
      let params = {
        api: METADATE_DATASOURCE_NAME_LIST_GET,
        method: "get",
        querys: {},
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      if (!res.code) {
        this.getSourceTypeKeyJson(res.list);
      }
    },
    getSourceTypeKeyJson(data) {
      let json = [];
      let jsondata = data;
      if (jsondata) {
        jsondata.map((item) => {
          json.push({
            key: item.dataSourceKey,
            value: item.name,
          });
        });
      }
      this.dbTypeJson = json;
    },
    async addMethod(param) {
      this.confirmLoading = true;
      this.formloading = true;
      let params = {
        api: METADATE_TASK_START_POST + "?id=" + this.form.choosedTemplate,
        method: "post",
        querys: {
          taskParams: param.taskParams,
        },
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      this.confirmLoading = false;
      this.formloading = false;
      if (res.code == 200) {
        this.$message.success(res.msg);
        this.$emit("updateTemplateCancelCallback");
        this.handleCancel();
      } else {
        this.$message.error(res.msg);
      }
    },
    async showExample() {
      // 校验过滤条件数据
      if (this.sourceQueryFilter.length == 0) {
        return false;
      }

      let param = {};
      let templateId = this.form.choosedTemplate;
      this.sourceQueryFilter.map((item) => {
        param[item.field] = item.value;
      });
      let params = {
        api: TASK_TEMPLATE_FETCH_POST + "?id=" + templateId,
        method: "post",
        querys: param,
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      this.confirmLoading = false;

      if (res.code == 200) {
        this.realInterfaceData = res.msg;
      } else {
        this.$message.error(res.msg);
      }
    },
    changeTemplate(item, e) {
      let index = e.data.attrs["data-index"];
      let templateData = this.templateList[index];
      let tempData = {
        sourceDataKey: templateData.sourceDataKey, //数据源键，
        sinkDataKey: templateData.sinkDataKey, //数据源键，
        sourceTableName: templateData.sourceTableName, //源表名称
        sinkTableName: templateData.sinkTableName, //目标表
        fieldMapping: templateData.fieldMapping, //字段映射
        sourceQueryFilter: templateData.sourceQueryFilter, //过滤条件
        startParams: templateData.startParams,
        choosedTemplate: templateData.id,
      };
      delete tempData.id;
      this.setModalData(tempData);
      this.showExample();
    },
    setModalData(data) {
      this.sourceQueryFilter = [
        {
          field: [],
          type: [],
          columnType: "",
          constrained: "",
          value: "",
          isPrimaryKeys: false,
        },
      ];
      this.form.sourceDataKey = data.sourceDataKey;
      this.form.sinkDataKey = data.sinkDataKey;
      this.form.sourceTableName = data.sourceTableName;
      this.form.sinkTableName = data.sinkTableName;

      this.form.fieldMapping = data.fieldMapping;
      this.form.sourceQueryFilter = data.sourceQueryFilter;

      this.sourceQueryFilter = this.form.sourceQueryFilter;
      //将初始状态身为true，需要回填映射关系数据
      this.ifInit = true;
      let mapDataJson = this.form.fieldMapping;
      this.mapJsonData = [];
      const mapDataKeys = Object.keys(mapDataJson);
      mapDataKeys.forEach((key, index) => {
        this.mapJsonData.push({
          key: "param_" + index + "_" + moment(),
          sourceParma: key,
          sinkParma: mapDataJson[key],
        });
      });
      this.form.mapJsonData = this.mapJsonData;

      this.startParamsArray = [];
      let startParams = data.startParams;
      Object.keys(startParams).map((key) => {
        let param = {};
        if (
          startParams[key] == "IN" ||
          startParams[key] == "LIST" ||
          startParams[key] == "BOOLEAN" ||
          startParams[key] == "bool"
        ) {
          param[key] = undefined;
        } else {
          param[key] = "";
        }
        if (key == "partition_spec" || key == "overwrite") {
          this.startParamsArray.unshift({
            key: key,
            type: startParams[key],
          });
        } else {
          this.startParamsArray.push({
            key: key,
            type: startParams[key],
          });
        }

        this.form = {
          ...this.form,
          ...param,
        };
      });
      this.$refs.form.clearValidate();
    },
    timeDateChange(date, dateStr, param) {
      // this.form[param] = moment(date).valueOf();
      this.form[param] = moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    },

    getRulesByType(item) {
      // 先校验当前是否是分区参数，分区参数非必填，NULL非必填
      const key = item.key;
      if (key != "partition_spec" && key != "overwrite") {
        const type = item.type;
        if (type != "NULL") {
          return {
            required: true,
            message: "请输入",
            trigger: ["change", "blur"],
          };
        }
      }
    },
    getLabelByKey(item) {
      const key = item.key;
      if (key == "partition_spec") {
        return "分区参数";
      } else if (key == "overwrite") {
        return "是否覆盖数据";
      } else {
        return key;
      }
    },
  },
};
</script>
 <style lang='less' scoped>
/deep/.ant-form-item-label {
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 8px;
}
.map-area {
  display: flex;
  flex-direction: row;
  gap: 32px;
  justify-content: flex-end;
  .one-area {
    width: 100%;
  }
}
.map-param-area {
  border: solid 1px #ccc;
  height: 400px;
  overflow-y: auto;
  padding: 16px;
  .one-map {
    display: flex;
    width: 100%;
    justify-content: space-around;
    gap: 6px;
    margin-bottom: 16px;
  }
  .param-area {
    width: 40%;
  }
}
.singleArrow {
  width: 140px;
  height: 2px;
  position: relative;
  background-color: #ccc;
  /* transform: rotate(-40deg); */
  top: 15px;
}
.singleArrow::after {
  content: "";
  display: block;
  position: absolute;
  right: -5px; /* 箭头位置 */
  top: -3px; /* 箭头位置 */
  border-top: 4px solid transparent; /* 箭头高低 */
  border-bottom: 4px solid transparent; /* 箭头高低 */
  border-left: 8px solid #ccc; /* 箭头长度*/
}
.filter-part {
  display: flex;
  flex-direction: column;
  padding: 12px;
  background: @gray-4;
  border-radius: 4px;
  margin-bottom: 12px;
  gap: 11px;
  .filter-one {
    display: flex;
    gap: 12px;
    align-items: center;
  }
}
/deep/.item-col {
  display: flex;
  gap: 8px;
}
.icon-box {
  display: flex;
  width: 40px;
  gap: 8px;
  .reduce-item-icon {
    font-size: 16px;
    color: @error-color;
  }
  .add-item-icon {
    font-size: 16px;
    color: #2b6afd;
  }
}
.handle-btn {
  text-align: right;
  margin-bottom: 16px;
  margin-right: 20px;
  display: flex;
  justify-content: flex-end;
}
.conf-warp {
  flex: 1;
  padding: 12px;
  overflow-y: auto;
  .area-one {
    border: solid 1px #ccc;
    margin-bottom: 26px;
    padding: 12px 30px;
    border-radius: 6px;
    /deep/.ant-form-item {
      margin-bottom: 6px;
    }
  }
  .filter-left {
    width: 65%;
  }
  .filter-right {
    flex: 1;
  }
}
.not-show-more {
  transform: rotate(180deg);
}
.foot {
  text-align: right;
  padding: 12px;
}
.one-area-top {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.filter-head {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  margin-right: 50px;
}
.mapping-param {
  border: solid 1px #ccc;
  border-radius: 4px;
  padding-left: 12px;
  height: 32px;
  line-height: 32px;
}
.step-title-left {
  display: inline-block;
  padding-left: 10px;
  border-left: solid 3px #2b6afd;
  font-weight: bold;
  font-size: 14px;
  height: 14px;
  line-height: 14px;
  margin-left: 20px;
}
</style>
