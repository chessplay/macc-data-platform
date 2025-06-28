<template>
  <div class="dc-page-card">
    <div class="main">
      <header>
        <div>
          <a-button type="primary" icon="plus" @click="showModel({})"
            >添加</a-button
          >
        </div>
        <div>
          <a-space>
            <a-select
              :allowClear="true"
              placeholder="标签值类型"
              v-model="query.valuetype"
              style="width:180px;min-width:100px"
            >
              <a-select-option
                v-for="(item, index) in valuetype"
                :key="item.id + '_' + index"
                :value="item.id"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>

            <a-select
              :allowClear="true"
              placeholder="标签数据分类"
              v-model="query.label_classify"
              style="width:180px;min-width:100px"
            >
              <a-select-option
                v-for="(item, index) in classify"
                :key="item.id + '_' + index"
                :value="item.id"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>

            <a-select
              :allowClear="true"
              placeholder="标签数据类别"
              v-model="query.label_type"
              style="width:180px;min-width:100px"
            >
              <a-select-option
                v-for="(item, index) in type"
                :key="item.id + '_' + index"
                :value="item.id"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>

            <a-tree-select
              :allowClear="true"
              :replaceFields="replaceFields"
              style="width:180px;min-width:100px"
              :tree-data="treeData"
              placeholder="请选择业务类型"
              tree-default-expand-all
              v-model="query.business_type_id"
            >
            </a-tree-select>
            <a-input
              placeholder="搜索名称关键字"
              :allow-clear="true"
              v-model="query.keyword"
              style="width:180px;min-width:100px"
            ></a-input>
            <a-button type="primary" @click="search" icon="search"
              >搜索</a-button
            >
          </a-space>
        </div>
      </header>
      <label-model-table
        ref="label_model_table"
        :labelClassify="classify"
        :type="type"
        :valueType="valuetype"
        @showModel="showModel"
      ></label-model-table>
      <a-modal
        :title="
          selectData.status == 'info'
            ? '标签模型详情'
            : selectData.labelId
            ? '编辑标签模型'
            : '添加标签模型'
        "
        :visible="visible"
        :confirm-loading="confirmLoading"
        width="50vw"
        @ok="handleOk"
        @cancel="handleCancel"
      >
        <a-form
          :form="form"
          :modal="selectData"
          v-if="selectData.status != 'info'"
        >
          <a-form-item
            label="标签ID"
            v-bind="formItemLayout"
            v-if="selectData.labelId ? true : false"
          >
            <a-input
              placeholder="请输入标签ID"
              :disabled="true"
              v-decorator="[
                'labelId',
                {
                  rules: [{ required: true, message: '请输入标签ID' }],
                  initialValue: selectData.labelId,
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="标签名称" v-bind="formItemLayout">
            <a-input
              placeholder="请输入标签名称"
              v-decorator="[
                'labelName',
                {
                  rules: [{ required: true, message: '请输入标签名称' }],
                  initialValue: selectData.labelName,
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="分类" v-bind="formItemLayout">
            <a-select
              :allowClear="true"
              :disabled="selectData.labelId ? true : false"
              placeholder="请选择分类"
              v-decorator="[
                'labelClassify',
                {
                  rules: [{ required: true, message: '请选择分类' }],
                  initialValue: selectData.labelClassify,
                },
              ]"
            >
              <a-select-option
                v-for="(item, index) in classify"
                :key="item.id + '_' + index"
                :value="item.id"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="数据类别" v-bind="formItemLayout">
            <a-select
              :allowClear="true"
              placeholder="请选择数据类别"
              :disabled="selectData.labelId ? true : false"
              v-decorator="[
                'labelType',
                {
                  rules: [{ required: true, message: '请选择数据类别' }],
                  initialValue: selectData.labelType,
                },
              ]"
            >
              <a-select-option
                v-for="(item, index) in type"
                :key="item.id + '_' + index"
                :value="item.id"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="业务类型" v-bind="formItemLayout">
            <a-tree-select
              :replaceFields="replaceFields"
              :tree-data="treeData"
              placeholder="请选择业务类型"
              tree-default-expand-all
              :disabled="selectData.labelId ? true : false"
              v-decorator="[
                'businessTypeId',
                {
                  rules: [{ required: true, message: '请选择业务类型' }],
                  initialValue: selectData.businessTypeId,
                },
              ]"
            >
            </a-tree-select>
          </a-form-item>

          <a-form-item label="标签数值类型" v-bind="formItemLayout">
            <a-select
              placeholder="请选择标签数值类型"
              v-decorator="[
                'valueType',
                {
                  rules: [{ required: true, message: '请选择标签数值类型' }],
                  initialValue: selectData.valueType,
                },
              ]"
            >
              <a-select-option
                v-for="item in valuetype"
                :key="item.id"
                :value="item.id"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="标签列名" v-bind="formItemLayout">
            <a-input
              placeholder="请输入标签列名"
              v-decorator="[
                'tableColumn',
                {
                  initialValue: selectData.tableColumn,
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="标签计算逻辑" v-bind="formItemLayout">
            <a-textarea
              placeholder="请输入标签计算逻辑"
              v-decorator="[
                'labelLogic',
                {
                  initialValue: selectData.labelLogic,
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="标签描述" v-bind="formItemLayout">
            <a-textarea
              placeholder="请输入标签描述"
              v-decorator="[
                'description',
                {
                  initialValue: selectData.description,
                },
              ]"
            />
          </a-form-item>
        </a-form>

        <a-form :form="form" :modal="selectDataInfo" v-else class="info">
          <a-form-item label="标签ID" v-bind="formItemLayout">
            <span>{{ selectDataInfo.labelId }}</span>
          </a-form-item>
          <a-form-item label="标签名称" v-bind="formItemLayout">
            <span>{{ selectDataInfo.labelName }}</span>
          </a-form-item>

          <a-form-item label="标签类型ID" v-bind="formItemLayout">
            <span>{{ selectDataInfo.labelType }}</span>
          </a-form-item>
          <a-form-item label="标签值类型" v-bind="formItemLayout">
            <span>{{ selectDataInfo.valueType }}</span>
          </a-form-item>
          <a-form-item label="创建时间" v-bind="formItemLayout">
            <span>{{
              formatTime(selectDataInfo.createTime, "yyyy年MM月dd日 hh:mm:ss")
            }}</span>
          </a-form-item>
          <a-form-item label="创建人" v-bind="formItemLayout">
            <span v-if="selectDataInfo.creator">{{
              selectDataInfo.creator
            }}</span>
            <span class="no-info" v-else>暂无数据</span>
          </a-form-item>
          <a-form-item label="最后更新时间" v-bind="formItemLayout">
            <span v-if="selectDataInfo.updateTime">{{
              formatTime(selectDataInfo.updateTime, "yyyy年MM月dd日 hh:mm:ss")
            }}</span>
            <span class="no-info" v-else>暂无数据</span>
          </a-form-item>
          <a-form-item label="最后更新人" v-bind="formItemLayout">
            <span v-if="selectDataInfo.lastUpdater">{{
              selectDataInfo.lastUpdater
            }}</span>
            <span class="no-info" v-else>暂无数据</span>
          </a-form-item>
          <a-form-item label="标签列名" v-bind="formItemLayout">
            <span v-if="selectDataInfo.tableColumn">{{
              selectDataInfo.tableColumn
            }}</span>
            <span class="no-info" v-else>暂无数据</span>
          </a-form-item>
          <a-form-item label="标签计算逻辑" v-bind="formItemLayout">
            <span v-if="selectDataInfo.labelLogic">{{
              selectDataInfo.labelLogic
            }}</span>
            <span class="no-info" v-else>暂无数据</span>
          </a-form-item>
          <a-form-item label="标签描述" v-bind="formItemLayout">
            <span v-if="selectDataInfo.description">{{
              selectDataInfo.description
            }}</span>
            <span class="no-info" v-else>暂无数据</span>
          </a-form-item>
        </a-form>
      </a-modal>
    </div>
    <a-modal
      :title="'添加成功'"
      :visible="successVisible"
      @ok="handleSuccessClose"
      @cancel="handleSuccessClose"
    >
      <a-result
        status="success"
        :title="'标签创建成功，标签ID:' + successLabelId"
      >
      </a-result>
    </a-modal>
  </div>
</template>

<script>
import labelModelTable from "./labelModelTable.vue";
import Service from "@p/services/labelService";
export default {
  components: { labelModelTable },
  name: "labelModel",
  data() {
    return {
      visible: false,
      form: this.$form.createForm(this, { name: "lableModel" }),
      confirmLoading: false,
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 21 },
        },
      },
      treeData: [],
      checkedKeys: [],
      selectData: {},
      selectDataInfo: {},
      replaceFields: {
        children: "subList",
        title: "typeName",
        key: "id",
        value: "id",
      },
      valuetype: [],
      classify: [],
      type: [],
      query: {
        valuetype: undefined,
        business_type_id: undefined,
        label_classify: undefined,
        label_type: undefined,
        keyword: undefined,
      },
      successVisible: false,
      successLabelId: null,
    };
  },
  async mounted() {
    await this.getValueType();
    await this.getClassIfy();
    await this.getType();
    await this.getLabelTree();
  },

  methods: {
    search() {
      this.$refs.label_model_table.getLabelModelList(1, 10, this.query);
    },
    formatTime(time, fmt) {
      let self = new Date(time);
      let o = {
        "M+": self.getMonth() + 1, //月份
        "d+": self.getDate(), //日
        "h+": self.getHours(), //小时
        "m+": self.getMinutes(), //分
        "s+": self.getSeconds(), //秒
        "q+": Math.floor((self.getMonth() + 3) / 3), //季度
        S: self.getMilliseconds(), //毫秒
      };
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (self.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      for (let k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
        }
      }
      return fmt;
    },
    async getLabelTree() {
      let res = await Service.getLabelTree();
      if (res.code == 0) {
        this.treeData = this.deepClone(res.list);
        console.log(this.treeData);
      } else {
        this.$message.error(res.msg);
      }
    },
    deepClone(obj) {
      //可传入对象 或 数组
      //  判断是否为 null 或 undefined 直接返回该值即可,
      if (obj === null || !obj) return obj;
      // 判断 是要深拷贝 对象 还是 数组
      if (Object.prototype.toString.call(obj) === "[object Object]") {
        //对象字符串化的值会为 "[object Object]"
        let target = {}; //生成新的一个对象
        target.scopedSlots = { title: "subList" };
        const keys = Object.keys(obj); //取出对象所有的key属性 返回数组 keys = [ ]
        //遍历复制值, 可用 for 循环代替性能较好
        keys.forEach((key) => {
          if (obj[key] && typeof obj[key] === "object")
            //如果遇到的值又是 引用类型的 [ ] {} ,得继续深拷贝
            target[key] = this.deepClone(obj[key]);
          //递归
          else target[key] = obj[key];
        });
        return target; //返回新的对象
      } else if (Array.isArray(obj)) {
        // 数组同理
        let arr = [];
        obj.forEach((item, index) => {
          if (item && typeof item === "object")
            arr[index] = this.deepClone(item);
          else arr[index] = item;
        });
        return arr;
      }
    },
    async getValueType() {
      let res = await Service.getValueType();
      if (res.code == 0) {
        this.valuetype = res.list;
      } else {
        this.$message.error(res.msg);
      }
    },
    async getClassIfy() {
      let res = await Service.getClassIfy();
      if (res.code == 0) {
        this.classify = res.list;
      } else {
        this.$message.error(res.msg);
      }
    },
    async getType() {
      let res = await Service.getType();
      if (res.code == 0) {
        this.type = res.list;
      } else {
        this.$message.error(res.msg);
      }
    },

    showModel(record) {
      console.log('添加------',record);
      if (record && record.id) {
        this.getSelectDataInfo(record.id, record.status);
      } else {
        this.visible = true;
      }
    },
    async getSelectDataInfo(id, type) {
      let res = await Service.getLabelModelInfo(id);
      if (res.code == 0) {
        if (type == "info") {
          this.selectData = { status: "info" };
          this.selectDataInfo = res.data;
        } else {
          this.selectData = res.data;
          this.selectDataInfo = {};
        }

        this.visible = true;
      } else {
        this.$message.error(res.msg);
      }
    },
    handleOk() {
      this.form.validateFields(async (err, values) => {
        if (err) {
          return;
        }
        let self = this;
        let page = this.$refs.label_model_table.pagination;
        console.log('this.selectData.labelId',this.selectData.labelId);
        if (this.selectData.labelId) {
          values.labelId = this.selectData.labelId;
          this.$confirm({
            title: "是否更新标签模型？",
            async onOk() {
              let res = await Service.updataLabelModel(
                self.selectData.labelId,
                values
              );
              if (res.code == 0) {
                self.$message.success("更新成功");
                self.$refs.label_model_table.getLabelModelList(
                  page.current,
                  page.pageSize
                );
                self.form.resetFields();
                self.visible = false;
              } else {
                self.$message.error(res.msg);
              }
            },
          });
        } else {
          this.$confirm({
            title: "是否添加新的标签模型？",
            async onOk() {
              let res = await Service.addLabelModel(values);
              if (res.code == 0) {
                // self.$message.success("添加成功");
                self.successLabelId = res.data;
                self.successVisible = true;
                // self.$refs.label_model_table.getLabelModelList(
                //   page.current,
                //   page.pageSize
                // );
                self.form.resetFields();
                self.visible = false;
              } else {
                self.$message.error(res.msg);
              }
            },
          });
        }
      });
    },
    handleCancel() {
      this.form.resetFields();
      this.selectData={};
      this.visible = false;
    },
    handleSuccessClose() {
      this.successVisible = false;
    },
  },
};
</script>
<style scoped>
header {
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
}
.info {
  font-weight: bold;
}
.info span {
  font-weight: 400;
  color: rgb(0, 0, 0);
}
.info .ant-form-item {
  margin-bottom: 5px;
}
.no-info {
  color: rgb(151, 145, 145) !important;
}
</style>
