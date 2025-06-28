<!--
 * @Author: 'chenmengni'
 * @Date: 2024-12-10 11:06:29
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2025-01-23 18:07:01
 * @FilePath: \data-service-web\src\project\views\dataManage\dataTaskTemplateManage\templateSetModal.vue
 * @Description: 模板设置界面
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
          <div class="one-area-top">
            <span class="one-title">表选择：</span>
          </div>
          <div class="area-one">
            <div style="width: 49%">
              <a-form-model-item label="模板名称" prop="name">
                <a-input
                  v-model="form.name"
                  autocomplete="off"
                  :disabled="!!form.id"
                />
              </a-form-model-item>
            </div>
            <div class="map-area">
              <div class="one-area">
                <div class="table-area">
                  <!-- 源表 -->
                  <a-form-model-item label="源数据源键" prop="sourceDataKey">
                    <a-select v-model="form.sourceDataKey">
                      <a-select-option
                        v-for="item in dbTypeJson"
                        :key="item.key"
                        :value="item.key"
                      >
                        {{ item.value }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </div>
                <div class="table-area">
                  <a-form-model-item label="源表" prop="sourceTableName">
                    <div style="display: flex; align-items: center; gap: 12px">
                      <a-select
                        v-model="form.sourceTableName"
                        allowClear
                        showSearch
                        :disabled="sourceTableSelectJson.length < 1"
                        @popupScroll="(e) => handlePopupScroll(e, 'source')"
                        @search="(value) => handleSearch(value, 'source')"
                        @select="(value) => handleSelect(value, 'source')"
                      >
                        <a-select-option
                          v-for="item in sourceRenderedOptions"
                          :key="`source_${timestamp}${item.key}`"
                          :value="item.value"
                        >
                          <a-tooltip>
                            <template slot="title"> {{ item.value }} </template>
                            {{ item.value }}
                          </a-tooltip>
                        </a-select-option>
                      </a-select>
                      <a-tooltip>
                        <template slot="title"> 刷新 </template>
                        <a-icon
                          @click="refresh('source')"
                          class="action reload-one"
                          type="reload"
                        />
                      </a-tooltip>
                    </div>
                  </a-form-model-item>
                </div>
              </div>
              <div class="one-area">
                <div class="table-area">
                  <a-form-model-item label="目标数据源键" prop="sinkDataKey">
                    <a-select v-model="form.sinkDataKey">
                      <a-select-option
                        v-for="item in dbTypeJson"
                        :key="item.key"
                        :value="item.key"
                      >
                        {{ item.value }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </div>
                <div class="table-area">
                  <a-form-model-item label="目标表" prop="sinkTableName">
                    <div style="display: flex; align-items: center; gap: 12px">
                      <a-select
                        v-model="form.sinkTableName"
                        allowClear
                        showSearch
                        :disabled="sinkTableSelectJson.length < 1"
                        @popupScroll="(e) => handlePopupScroll(e, 'sink')"
                        @search="(value) => handleSearch(value, 'sink')"
                        @select="(value) => handleSelect(value, 'sink')"
                      >
                        <a-select-option
                          v-for="item in sinkRenderedOptions"
                          :key="`sink_${timestamp}${item.key}`"
                          :value="item.value"
                        >
                          <a-tooltip>
                            <template slot="title"> {{ item.value }} </template>
                            {{ item.value }}
                          </a-tooltip>
                        </a-select-option>
                      </a-select>
                      <a-tooltip>
                        <template slot="title"> 刷新 </template>
                        <a-icon
                          @click="refresh('sink')"
                          class="action reload-one"
                          type="reload"
                        />
                      </a-tooltip>
                    </div>
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
                  <div class="handle-btn" v-if="false">
                    <a-button
                      @click="moreFilter = !moreFilter"
                      :disabled="showSourceParamSelectJson.length < 1"
                    >
                      过滤条件
                      <a-icon
                        type="up"
                        :class="!moreFilter ? 'not-show-more' : ''"
                        style="font-size: 10px; padding: 0 5px"
                      />
                    </a-button>
                  </div>
                  <!-- 过滤条件 -->
                  <div
                    class="filter-part"
                    v-if="showSourceParamSelectJson.length > 0"
                  >
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
                        :disabled="
                          item.isPrimaryKeys ||
                          showSourceParamSelectJson.length < 1
                        "
                        @change="changeParam"
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
                        :disabled="showSourceParamSelectJson.length < 1"
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
                        :disabled="showSourceParamSelectJson.length < 1"
                      />

                      <div class="item-col">
                        <div class="icon-box">
                          <a-icon
                            type="delete"
                            class="reduce-item-icon"
                            v-if="!item.isPrimaryKeys"
                            @click="deleteFilterItem(item, index)"
                          />
                          <a-icon
                            type="plus"
                            class="add-item-icon"
                            v-if="index == 0"
                            @click="addFilterItem"
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                  <a-empty v-else :image="simpleImage">
                    <span slot="description"> 请选择源表 </span>
                  </a-empty>
                </div>
              </div>
            </div>
            <div class="filter-right">
              <div class="one-area-top">
                <a-button type="link" style="height: 21px" @click="showExample">
                  查看实例
                </a-button>
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
            <a-button class="add-param" @click="addNewParam">
              新增字段
            </a-button>
          </div>
          <div class="area-one map-param-area">
            <template v-if="mapJsonData.length > 0">
              <div class="filter-head">
                <div>
                  <span>源表字段</span>
                  <a-tooltip>
                    <template slot="title"> 刷新 </template>
                    <a-icon
                      @click="refreshParam('source')"
                      class="action reload-one"
                      type="reload"
                      style="margin-left: 16px"
                    />
                  </a-tooltip>
                </div>
                <div>
                  <span>目标表字段</span>
                  <a-tooltip>
                    <template slot="title"> 刷新 </template>
                    <a-icon
                      @click="refreshParam('sink')"
                      class="action reload-one"
                      type="reload"
                      style="margin-left: 16px"
                    />
                  </a-tooltip>
                </div>
              </div>
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
                  <a-form-model-item
                    :prop="'mapJsonData.' + index + '.sourceParma'"
                    :rules="{
                      required: true,
                      message: '请选择',
                      trigger: ['change', 'blur'],
                    }"
                    :wrapperCol="{ span: 24 }"
                  >
                    <a-select v-model="mapJsonData[index]['sourceParma']">
                      <a-select-option
                        v-for="item in showSourceParamSelectJson"
                        :key="`source${index}_${item.timestamp}`"
                        :value="item.columnName"
                        :label="`${item.columnName}（${item.columnType}）`"
                      >
                        {{ item.columnName }}（{{ item.columnType }}）
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </div>
                <div class="singleArrow"></div>
                <div class="param-area">
                  <a-form-model-item
                    :prop="'mapJsonData.' + index + '.sinkParma'"
                    :rules="{
                      required: true,
                      message: '请选择',
                      trigger: ['change', 'blur'],
                    }"
                    :wrapperCol="{ span: 24 }"
                  >
                    <a-select v-model="mapJsonData[index]['sinkParma']">
                      <a-select-option
                        v-for="item in showsinkParamSelectJson"
                        :key="`sink${index}_${item.timestamp}`"
                        :value="item.columnName"
                        :label="`${item.columnName}（${item.columnType}）`"
                      >
                        {{ item.columnName }}（{{ item.columnType }}）
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </div>
                <div style="width: 70px; line-height: 35px">
                  <button
                    class="dc-tb-btn-del"
                    @click="deleteParam(item, index)"
                  >
                    删除
                  </button>
                </div>
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
          <a-button type="primary" @click="handleOk"> 确定 </a-button>
        </a-space>
      </div>
    </a-drawer>
  </div>
</template>
 
 <script>
import { request } from "@/utils/request";

import {
  METADATE_DATASOURCE_NAME_LIST_GET,
  METADATE_TABLE_NAME_GET,
  METADATE_TABLE_DETAIL_GET,
  METADATE_TASK_TEMPLATE_ONE_POST,
  QUERY_CONSTRAINT_TYPE_GET,
  TASK_TEMPLATE_FETCH_POST,
} from "@/services/api";
import moment from "moment";
import { Empty } from "ant-design-vue";
import _ from "lodash";

const LOAD_NUM = 30; // 加载条数--可自定义

let form = {
  id: "",
  name: "", //模板名称
  sourceDataKey: "", //数据源键，
  sinkDataKey: "", //数据源键，
  sourceTableName: "", //源表名称
  sinkTableName: "", //目标表
  fieldMapping: {}, //字段映射
  sourceQueryFilter: [], //过滤条件
  process_code: "",
  task_code: "",
};
let sourceInitData = [
  {
    field: [],
    type: [],
    value: "",
    columnType: "",
    constrained: "",
    fileterType: [],
    isPrimaryKeys: false,
  },
];
export default {
  components: {},
  data() {
    return {
      simpleImage: Empty.PRESENTED_IMAGE_SIMPLE,
      timeout: 8000,
      timestamp: moment(),
      ifShowModal: false,
      confirmLoading: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 17 },
      form: { ...form },
      submitForm: { ...form }, //表单数据是为了处理多次提交接口会有时间差，导致数据获取可能会错乱
      rules: {
        name: [
          {
            required: true,
            message: "请输入",
            trigger: ["blur", "change"],
          },
        ],
        sourceDataKey: [
          {
            required: true,
            message: "请选择",
            trigger: ["blur", "change"],
          },
        ],
        sinkDataKey: [
          {
            required: true,
            message: "请选择",
            trigger: ["blur", "change"],
          },
        ],
        sourceTableName: [
          {
            required: true,
            message: "请选择",
            trigger: ["blur", "change"],
          },
        ],
        sinkTableName: [
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
      sourceQueryFilter: [...sourceInitData],
      constraintList: [],
      realInterfaceData: "{}", //过滤条件对外显示json数据
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
  watch: {
    "form.sourceDataKey": {
      handler: _.debounce(function (newval, oldVal) {
        if (newval != oldVal && newval) {
          // 当源数据源类型变化时，需要去调用接口获取表列表数据
          this.handleDateSourceChange("source");
        }
      }, 500),
    },
    "form.sinkDataKey": {
      handler: _.debounce(function (newval, oldVal) {
        if (newval != oldVal && newval) {
          // 当目标数据源类型变化时，需要去调用接口获取表列表数据
          this.handleDateSourceChange("sink");
        }
      }, 500),
    },
    "form.sourceTableName": {
      handler: _.debounce(function (newval, oldVal) {
        if (newval != oldVal && newval) {
          // 当源表变化时，需要去调用接口获取字段列表数据
          this.handleTableChange("source");
        }
      }, 500),
    },
    "form.sinkTableName": {
      handler: _.debounce(function (newval, oldVal) {
        if (newval != oldVal && newval) {
          // 当目标表变化时，需要去调用接口获取字段列表数据
          this.handleTableChange("sink");
        }
      }, 500),
    },
  },
  created() {},
  mounted() {
    this.initGetData();
  },
  methods: {
    async initGetData() {
      await this.getConstraintList();
      await this.getSourceList();
    },
    initPage() {
      this.form = {};
      this.submitForm = {};
      this.sourceTableSelectJson = [];
      this.sinkTableSelectJson = [];
      this.sourceParamSelectJson = [];
      this.sinkParamSelectJson = [];
      this.mapJsonData = [];
      this.sourceQueryFilter = [];
      this.realInterfaceData = "{}";
      this.$refs.form.clearValidate();
    },
    openModal(state, data) {
      this.handleState = state;
      this.sourceQueryFilter = [...sourceInitData];
      if (data) {
        this.form = {
          id: data.id,
          name: data.name, //模板名称
          sourceDataKey: data.sourceDataKey, //数据源键，
          sinkDataKey: data.sinkDataKey, //数据源键，
          sourceTableName: data.sourceTableName, //源表名称
          sinkTableName: data.sinkTableName, //目标表
          fieldMapping: data.fieldMapping, //字段映射
          sourceQueryFilter: data.sourceQueryFilter, //过滤条件
          process_code: data.processCode,
          task_code: data.taskCode,
        };
        this.sourceQueryFilter = this.form.sourceQueryFilter;
        //将初始状态身为true，需要回填映射关系数据
        this.ifInit = true;
        setTimeout(() => {
          this.ifInit = false;
        }, 3000);
      } else {
        this.form = { ...form };
        //将初始状态还原，不需要回填映射关系数据
        this.ifInit = false;
      }
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
          // 校验过滤条件数据
          let errInfoFilter = this.checkFilterReName();
          if (errInfoFilter) {
            return this.$message.warning(errInfoFilter);
          }
          let errInfo = this.checkFilterEmpty();
          if (errInfo) {
            return this.$message.warning(errInfo);
          }
          // 校验源表字段是否有重复的。目标表字段不用处理
          let errRows = this.checkDataCorrectness();
          if (errRows.length) {
            return this.$message.warning({
              content: () =>
                errRows.map(function (item) {
                  return <div style="text-align:left">{item}</div>;
                }),
            });
          }
          let params = {
            ...this.form,
            sourceQueryFilter: this.sourceQueryFilter,
            description: "",
          };
          if (params.id) {
            // 修改
            this.updateMethod(params);
          } else {
            // 新增
            delete params.id;
            delete params.process_code;
            delete params.task_code;
            delete params.mapJsonData;
            this.addMethod(params);
          }
        } else {
          this.$message.warning("请将表单数据补充完整");
        }
      });
    },
    // 验证过滤条件是否有重复的变量名
    checkFilterReName() {
      let sourceQueryFilter = this.sourceQueryFilter;
      let errorInfo = "";
      let filterArray = [];
      sourceQueryFilter.map((item) => {
        filterArray.push(item.value);
      });
      const duplicates = this.findDuplicates(filterArray);
      for (let key in duplicates) {
        if (key == "") {
          continue;
        } else {
          let len = duplicates[key].length;
          if (len > 1) {
            let lineContent = "第";
            duplicates[key].map((item) => {
              lineContent += `${item + 1}、`;
            });
            lineContent =
              lineContent.substring(0, lineContent.length - 1) + " 行";
            errorInfo += `变量名： ${key} 在${lineContent}重复，`;
            if (errorInfo != "") {
              errorInfo = errorInfo.substring(0, errorInfo.length - 1);
            }
            break;
          }
        }
      }
      return errorInfo;
    },
    // 验证过滤条件是否为空
    checkFilterEmpty() {
      let sourceQueryFilter = this.sourceQueryFilter;
      let errorInfo = "";
      for (let i = 0; i < sourceQueryFilter.length; i++) {
        let item = sourceQueryFilter[i];
        if (!item.field || item.field.length == 0) {
          errorInfo = `请选择过滤条件:第${i + 1}行字段`;
          break;
        }
        if (!item.fileterType || item.fileterType.length == 0) {
          errorInfo = `第${i + 1}行字段不支持过滤`;
          break;
        }
        if (!item.type || item.type.length == 0) {
          errorInfo = `请选择第${i + 1}行操作类型`;
          break;
        }
        if (!item.value || item.value == "") {
          errorInfo = `请输入第${i + 1}行变量名`;
          break;
        }
        const regex = /^((?![A-Z]).)*$/;
        if (item.value && !regex.test(item.value)) {
          errorInfo = `第${i + 1}行变量名不能有大写`;
          break;
        }
      }
      return errorInfo;
    },
    // 校验源表字段是否有重复的。目标表字段不用处理
    checkDataCorrectness() {
      let soucreArray = [];
      this.mapJsonData.map((item) => {
        soucreArray.push(item.sourceParma);
      });
      const duplicates = this.findDuplicates(soucreArray);
      let errRows = [];
      for (let key in duplicates) {
        let errInfo = "";
        if (key == "") {
          continue;
        } else {
          let len = duplicates[key].length;
          if (len > 1) {
            let lineContent = "第";
            duplicates[key].map((item) => {
              lineContent += `${item + 1}、`;
            });
            lineContent =
              lineContent.substring(0, lineContent.length - 1) + " 行";
            errInfo += `字段： ${key} 在${lineContent}重复，`;

            if (errInfo != "") {
              errInfo = errInfo.substring(0, errInfo.length - 1);
              if (errRows.length == 0) {
                errRows.push("源表存在重复字段");
              }
              errRows.push(`${errInfo}`);
            }
          }
        }
      }
      return errRows;
    },
    // 获取当前数据中重复下标
    findDuplicates(arr) {
      let i = 0;
      const jsondata = arr.reduce((pre, cur) => {
        if (pre[cur]) {
          pre[cur].push(i);
        } else {
          pre[cur] = [i];
        }
        i++;
        return pre;
      }, {});
      return jsondata;
    },
    // 获取约束条件列表
    async getConstraintList() {
      let params = {
        api: QUERY_CONSTRAINT_TYPE_GET,
        method: "get",
        querys: {},
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      if (!res.code) {
        this.constraintList = res.data;
      }
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
    // 获取数据源下的表列表
    async getSourceTable(ops, refresh) {
      ops == "source"
        ? (this.submitForm.sourceDataKey = this.form.sourceDataKey)
        : (this.submitForm.sinkDataKey = this.form.sinkDataKey);

      let params = {
        api: METADATE_TABLE_NAME_GET,
        method: "get",
        querys: {
          datasource_key:
            ops == "source" ? this.form.sourceDataKey : this.form.sinkDataKey,
          disable_cache: refresh,
        },
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      let handleData = false;
      if (ops == "source") {
        handleData =
          this.submitForm.sourceDataKey == params.querys.datasource_key;
      } else {
        handleData =
          this.submitForm.sinkDataKey == params.querys.datasource_key;
      }
      if (handleData) {
        if (!res.code) {
          this.getTableTypeKeyJson(res.list, ops);
        } else {
          if (ops == "source") {
            this.sourceTableSelectJson = [];
            this.form.sourceTableName = "";
          } else {
            this.sinkTableSelectJson = [];
            this.form.sinkTableName = "";
          }
        }
      }
    },
    getTableTypeKeyJson(data, ops) {
      let json = [];
      let jsondata = data;
      if (jsondata) {
        jsondata.map((item) => {
          json.push({
            key: item,
            value: item,
          });
        });
      }
      if (ops == "source") {
        this.sourceTableSelectJson = json;
        if (!this.ifInit) {
          this.form.sourceTableName = "";
        }
      } else {
        this.sinkTableSelectJson = json;
        if (!this.ifInit) {
          this.form.sinkTableName = "";
        }
      }
      // 首次需要手动触发下下拉
      this.loadMoreData(json, ops);
    },
    // 获取该数据源下的字段列表
    async getParamsListByKey(ops, refresh) {
      ops == "source"
        ? (this.submitForm.sourceTableName = this.form.sourceTableName)
        : (this.submitForm.sinkTableName = this.form.sinkTableName);

      let params = {
        api: METADATE_TABLE_DETAIL_GET,
        method: "get",
        querys: {
          datasource_key:
            ops == "source" ? this.form.sourceDataKey : this.form.sinkDataKey,
          table_name:
            ops == "source"
              ? this.form.sourceTableName
              : this.form.sinkTableName,
          disable_cache: refresh,
        },
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      let handleData = false;
      if (ops == "source") {
        handleData =
          this.submitForm.sourceTableName == params.querys.table_name;
      } else {
        handleData = this.submitForm.sinkTableName == params.querys.table_name;
      }
      if (handleData) {
        if (!res.code) {
          this.setMapJsonData(res.data, ops);
          this.setFilterJsonData(res.data, ops);
        }
      }
    },

    setMapJsonData(data, source) {
      // 新增时，需要重组mapjsondata数据，
      let columns = data?.columns ? data.columns : [];
      if (source == "source") {
        this.sourceParamSelectJson = columns;
      } else {
        this.sinkParamSelectJson = columns;
      }
      // 当状态为编辑、拷贝时（ifInit == true）需要回显映射关系数据
      if (!this.ifInit) {
        let tempMapJson = this.mapJsonData;

        columns.map((item, index) => {
          if (!Object.hasOwnProperty.call(tempMapJson, index)) {
            if (source == "source") {
              tempMapJson.push({
                key: "param_" + index + "_" + moment(),
                sourceParma: item.columnName,
                sinkParma: "",
              });
            } else {
              tempMapJson.push({
                key: "param_" + index + "_" + moment(),
                sinkParma: item.columnName,
                sourceParma: "",
              });
            }
          } else {
            if (source == "source") {
              tempMapJson[index]["sourceParma"] = item.columnName;
            } else {
              tempMapJson[index]["sinkParma"] = item.columnName;
            }
          }
        });

        // 当新获取的param长度少于映射列表长度时，多出部分应置空
        if (columns.length < tempMapJson.length) {
          tempMapJson.map((item, index) => {
            if (index + 1 > columns.length) {
              if (source == "source") {
                tempMapJson[index]["sourceParma"] = "";
              } else {
                tempMapJson[index]["sinkParma"] = "";
              }
            }
          });
        }
      } else {
        // 编辑状态下回显数据
        let mapDataJson = this.form.fieldMapping;
        this.mapJsonData = [];
        let tempMapJson = this.mapJsonData;
        const mapDataKeys = Object.keys(mapDataJson);
        mapDataKeys.forEach((key, index) => {
          tempMapJson.push({
            key: "param_" + index + "_" + moment(),
            sourceParma: key,
            sinkParma: mapDataJson[key],
          });
        });
      }
      this.form = { ...this.form, mapJsonData: this.mapJsonData };
    },
    // 清除-选中的表数据
    cleanTableData(ops) {
      if (ops == "source") {
        // 级联数据清空
        this.form.sourceTableName = "";
        this.sourceTableSelectJson = [];
        this.sourceRenderedOptions = [];
      } else {
        // 级联数据清空
        this.form.sinkTableName = "";
        this.sinkTableSelectJson = [];
        this.sinkRenderedOptions = [];
      }
    },
    // 清除对应的字段映射数据
    cleanParamMapdata(ops) {
      let tempMapJson = this.mapJsonData;
      if (ops == "source") {
        this.sourceParamSelectJson = [];
        tempMapJson.map((item) => {
          item.sourceParma = "";
        });
      } else {
        this.sinkParamSelectJson = [];
        tempMapJson.map((item) => {
          item.sinkParma = "";
        });
      }
    },
    // 数据源数据切换-获取数据源下的表list数据
    handleDateSourceChange(source, refresh = false) {
      // 切换数据源时。需要清空表数据，但特殊情况不需要清，在编辑或克隆初始化(ifInit == false)显示界面的时候不需要清,要显示相关表单数据；
      if (!this.ifInit) {
        this.cleanTableData(source);
        this.cleanParamMapdata(source);
      }
      // 获取库列表数据
      this.getSourceTable(source, refresh);
    },
    //  表数据切换-获取表下的字段list数据
    handleTableChange(source, refresh = false) {
      // 需要重置下拉框内容
      if (source == "source") {
        this.sourceSearchVal = "";
        this.sourceRenderedOptions = [];
        this.loadMoreData(this.sourceTableSelectJson, source);
      } else {
        this.sinkSearchVal = "";
        this.sinkRenderedOptions = [];
        this.loadMoreData(this.sinkTableSelectJson, source);
      }
      // 获取该表下所有字段数据
      this.getParamsListByKey(source, refresh);
    },

    // 映射表-新增字段
    addNewParam() {
      if (this.form.sourceTableName == "" && this.form.sinkTableName == "") {
        this.$message.warn("请选择表");
        return false;
      }
      let mapJsonData = this.mapJsonData;
      mapJsonData.push({
        key: "param_" + this.mapJsonData.length + "_" + moment(),
        sourceParma: "",
        sinkParma: "",
      });
      this.form = { ...this.form, mapJsonData: this.mapJsonData };
    },

    deleteParam(item, index) {
      if (this.mapJsonData.length < 2) {
        this.$message.warn("至少有一条数据");
        return false;
      }
      this.mapJsonData.splice(index, 1);
    },
    // 生成接口所需要的map键值格式数据
    setFieldMapping(data) {
      let mappingJson = {};
      data.map((item) => {
        if (item.sourceParma != "" && item.sinkParma != "") {
          mappingJson[item.sourceParma] = item.sinkParma;
        }
      });
      return mappingJson;
    },

    // 任务模板编辑
    async updateMethod(param) {
      param.fieldMapping = this.setFieldMapping(this.mapJsonData);
      if (JSON.stringify(param.fieldMapping) == "{}") {
        this.$message.warn("至少有一条映射数据");
      }
      this.confirmLoading = true;
      let params = {
        api: METADATE_TASK_TEMPLATE_ONE_POST,
        method: "put",
        querys: param,
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      this.confirmLoading = false;

      if (res.code == 200) {
        this.$message.success(res.msg);
        this.$emit("updateTemplateCancelCallback");
      } else {
        this.$message.error(res.msg);
      }
    },
    // 任务模板新增
    async addMethod(param) {
      param.fieldMapping = this.setFieldMapping(this.mapJsonData);
      if (JSON.stringify(param.fieldMapping) == "{}") {
        this.$message.warn("至少有一条映射数据");
      }
      this.confirmLoading = true;

      let params = {
        api: METADATE_TASK_TEMPLATE_ONE_POST,
        method: "post",
        querys: param,
      };
      let res = await request(params.api, params.method, params.querys, {
        baseURL: "/maccdata/api",
      });
      this.confirmLoading = false;

      if (res.code == 200) {
        this.$message.success(res.msg);
        this.initPage();
        this.$emit("updateTemplateCancelCallback");
      } else {
        this.$message.error(res.msg);
      }
    },

    refresh(state) {
      // 刷新，
      this.handleDateSourceChange(state, true);
    },
    refreshParam(state) {
      // 刷新
      this.handleTableChange(state, true);
    },

    /* ---------- 过滤条件 start ------------- */
    // 设置过滤条件下拉框数据列表
    setFilterJsonData(data, source) {
      if (source == "source") {
        let queryConstraint = data?.queryConstraint ? data.queryConstraint : [];
        if (!this.ifInit) {
          this.sourceQueryFilter = [];
          for (let i = 0; i < queryConstraint.length; i++) {
            let item = queryConstraint[i];
            // 非回显状态，直接新增
            this.sourceQueryFilter.push({
              field: item.field,
              type: item.type,
              value: _.toLower(item.field),
              columnType: "", // 当前约束下，不需要
              isPrimaryKeys: true,
              constrained: "primaryKey",
              fileterType: item.fileterType,
            });
          }
        } else {
          for (let i = 0; i < queryConstraint.length; i++) {
            let queryFilterItem = queryConstraint[i];
            this.sourceQueryFilter.map((item) => {
              item.constrained == "primaryKey"
                ? (item.isPrimaryKeys = true)
                : (item.isPrimaryKeys = false);
              if (item.columnType && item.columnType != "") {
                item.fileterType = this.constraintList[item.columnType];
              } else {
                if (queryFilterItem.field == item.field) {
                  item.fileterType = queryFilterItem.fileterType;
                }
              }
            });
          }
        }
        if (this.sourceQueryFilter.length == 0) {
          this.sourceQueryFilter = [...sourceInitData];
        }
      }
    },
    // 删除过滤条件
    deleteFilterItem(item, index) {
      this.sourceQueryFilter.splice(index, 1);
    },
    // 过滤条件新增
    addFilterItem() {
      // 需校验当前条件长度。不能超过6个，不包括表的约束字段；
      let primartJson = this.sourceQueryFilter.filter((item) => {
        return !item.isPrimaryKeys;
      });
      if (primartJson.length > 5) {
        this.$message.error("最多只能添加6条！");
        return false;
      }
      let sourceInitJson = { ...sourceInitData[0] };
      this.sourceQueryFilter.push(sourceInitJson);
    },
    // 过滤条件-field变更时，变量名初始与字段名一致
    changeParam(data, e) {
      if (JSON.stringify(this.constraintList) != "{}") {
        let index = e.data.attrs["data-index"];
        let columnType = e.data.attrs["data-columnType"];
        let num = 0;
        this.sourceQueryFilter.map((sourceItem) => {
          sourceItem.field == data ? num++ : "";
        });
        if (num == 1) {
          this.sourceQueryFilter[index].value = _.toLower(
            this.sourceQueryFilter[index].field
          );
        } else {
          this.sourceQueryFilter[index].value = _.toLower(
            this.sourceQueryFilter[index].field + num
          );
        }
        this.sourceQueryFilter[index].fileterType =
          this.constraintList[columnType];
        this.sourceQueryFilter[index].columnType = columnType;
      }
    },
    async showExample() {
      // 校验过滤条件数据
      let errInfo = this.checkFilterEmpty();
      if (errInfo) {
        return this.$message.warning(errInfo);
      }
      let param = {};
      let templateId = this.handleState == "edit" ? this.form.id : "-1";
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
        this.$message.success("操作成功");
        this.realInterfaceData = res.msg;
      } else {
        this.$message.error(res.msg);
      }
    },
    /* ---------- 过滤条件 end ------------- */

    /*  -----------数据源-表列表数据过长处理 start------------------ */

    // 加载更多数据到select框 为了缓解select下拉框数据过多，导致渲染卡顿问题，
    loadMoreData(dataList, state) {
      let renderedOption = [];
      state == "source"
        ? (renderedOption = this.sourceRenderedOptions)
        : (renderedOption = this.sinkRenderedOptions);

      const renderedLen = renderedOption.length; // 已渲染的下拉列表长度
      const totalLen = dataList.length; // 当前数据源的长度
      let addList = [];
      if (renderedLen < totalLen) {
        if (renderedLen + LOAD_NUM <= totalLen) {
          addList = dataList.slice(renderedLen, renderedLen + LOAD_NUM);
        } else {
          addList = dataList.slice(
            renderedLen,
            renderedLen + (totalLen % LOAD_NUM)
          );
        }
        renderedOption = renderedOption.concat(addList);
        state == "source"
          ? (this.sourceRenderedOptions = renderedOption)
          : (this.sinkRenderedOptions = renderedOption);
      }
    },
    // 文本框值变化时触发 -- 从数据源中过滤出含搜索内容的数据，并取过滤结果的前n条作为下拉列表的可选项
    handleSearch(val, state) {
      let dataList = [];
      let filterList = [];
      let renderedOptions = [];

      if (state == "source") {
        this.sourceSearchVal = val;
        dataList = this.sourceTableSelectJson;
      } else {
        this.sinkSearchVal = val;
        dataList = this.sinkTableSelectJson;
      }

      if (val) {
        filterList = dataList.filter((item) => item.value.indexOf(val) > -1);
      } else {
        filterList = dataList;
      }
      state == "source"
        ? (this.sourceFilterDataList = filterList)
        : (this.sinkFilterDataList = filterList);

      renderedOptions =
        filterList.length < LOAD_NUM
          ? filterList
          : filterList.slice(0, LOAD_NUM);

      state == "source"
        ? (this.sourceRenderedOptions = renderedOptions)
        : (this.sinkRenderedOptions = renderedOptions);
    },
    // 被选中时调用，参数为选中项的 value (或 key) 值
    handleSelect(val, state) {
      let searchVal = "";
      let oriDataList = "";
      let renderedOptions = "";

      if (state == "source") {
        searchVal = this.sourceSearchVal;
        oriDataList = this.sourceTableSelectJson;
        renderedOptions = this.sourceRenderedOptions;
      } else {
        searchVal = this.sinkSearchVal;
        oriDataList = this.sinkTableSelectJson;
        renderedOptions = this.sinkRenderedOptions;
      }
      if (searchVal) {
        const selectedArr = oriDataList.filter((item) => item.value === val); // 从数据源中过滤出下拉框选中的值，并返回一个数组
        const restList = oriDataList.filter((item) => item.value !== val); // 从数据源中过滤出其他的值，返回一个数组
        const newList = selectedArr.concat(restList).slice(0, LOAD_NUM); // 将选中的元素放到下拉列表的第一位
        renderedOptions = newList; // 更新已渲染的下拉列表
        oriDataList = selectedArr.concat(restList); // 更新数据源

        if (state == "source") {
          this.sourceSearchVal == ""; // 因为触发handleSelect函数时，会自动清空用户输入的内容。因此，sourceSearchVal需要重置。
          this.sourceTableSelectJson = oriDataList;
          this.sourceRenderedOptions = renderedOptions;
        } else {
          this.sinkSearchVal == "";
          this.sinkTableSelectJson = oriDataList;
          this.sinkRenderedOptions = renderedOptions;
        }
      }
    },

    // 滚动时触发（防止抖动）
    handlePopupScroll: _.debounce(function (e, state) {
      let searceVal = "";
      state == "source"
        ? (searceVal = this.sourceSearchVal)
        : (searceVal = this.sinkSearchVal);

      if (searceVal === "") {
        if (state == "source") {
          this.loadMoreData(this.sourceTableSelectJson, state);
        } else {
          this.loadMoreData(this.sinkTableSelectJson, state);
        }
      } else {
        if (state == "source") {
          this.loadMoreData(this.sourceFilterDataList, state);
        } else {
          this.loadMoreData(this.sinkFilterDataList, state);
        }
      }
    }, 400),
    /*  -----------数据源-表列表数据过长处理 end------------------ */
  },
};
</script>
 <style lang='less' scoped>
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
.reload-one {
  color: #2b6afd;
  cursor: pointer;
}
</style>
