<!--
 * @Author: 'chenmengni'
 * @Date: 2024-12-04 11:37:01
 * @LastEditors: 'chenmengni'
 * @LastEditTime: 2024-12-26 13:50:22
 * @FilePath: \data-service-web\src\project\views\dataManage\dataSourceManage\addModal.vue
 * @Description: 数据源新增弹窗
-->
<template>
  <div>
    <a-modal
      width="700px"
      :title="form.id ? '编辑' : '新增'"
      :visible="ifShowModal"
      :maskClosable="true"
      :confirm-loading="confirmLoading"
      @cancel="handleCancel"
      @ok="handleOk"
    >
      <a-form-model
        ref="form"
        :model="form"
        :rules="rules"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <a-form-model-item label="数据源名称" prop="name">
          <a-input v-model="form.name" autocomplete="off" />
        </a-form-model-item>

        <a-form-model-item label="数据源键" prop="dataSourceKey">
          <a-input
            v-model="form.dataSourceKey"
            autocomplete="off"
            :disabled="!!form.id"
          />
        </a-form-model-item>

        <a-form-model-item label="数据源类型" prop="dbType">
          <a-select
            v-model="form.dbType"
            :disabled="!!form.id"
            @change="(value, option) => handleChange(value, option)"
          >
            <a-select-option
              v-for="item in dbTypeJson"
              :key="item.key"
              :value="item.key"
            >
              {{ item.value }}
            </a-select-option>
          </a-select>
        </a-form-model-item>

        <a-form-model-item label="数据源属性" prop="dbProperties">
          <a-textarea
            :rows="14"
            v-model="form.dbProperties"
            placeholder='JSON对象{"Group":"group"}'
          ></a-textarea>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { request } from "@/utils/request";
import {
  METADATE_DATASOURCE_ADD_POST,
  METADATE_DATASOURCE_UPDATE_POST,
} from "@/services/api";

let form = {
  id: "",
  name: "", //数据源名称
  dbType: [], //数据源类型
  dataSourceKey: "",
  dbProperties: "", //数据源属性
};

export default {
  components: {},
  data() {
    return {
      ifShowModal: false,
      confirmLoading: false,
      labelCol: { span: 4 },
      wrapperCol: { span: 20 },
      inputStyle: {
        width: "150px",
      },
      form: { ...form },
      rules: {
        name: [
          {
            required: true,
            message: "请输入",
            trigger: ["blur", "change"],
          },
        ],
        dbType: [
          {
            required: true,
            message: "请选择",
            trigger: ["blur", "change"],
          },
        ],
        dataSourceKey: [
          {
            required: true,
            message: "请输入",
            trigger: ["blur", "change"],
          },
          {
            validator: this.validateDataSourceKey,
            trigger: ["blur", "change"],
          },
        ],
        dbProperties: [
          {
            required: true,
            message: "请输入",
            trigger: ["blur", "change"],
          },
          {
            validator: this.validateProperties,
            trigger: ["blur", "change"],
          },
        ],
      },
      loading: false,
      mealsList: [],
      dbTypeJson: [],
    };
  },
  computed: {},
  mounted() {},
  watch: {},
  methods: {
    openModal(data) {
      this.ifShowModal = true; //显示
      this.$refs.form?.clearValidate(); //处理新增没提交后，编辑会出现表单输入提示问题
      if (data) {
        this.form = {
          ...data,
          dbProperties: JSON.stringify(data.dbProperties, null, 2),
        };
      }
    },
    handleCancel() {
      this.form = { ...form };
      this.$refs.form?.clearValidate();
      this.ifShowModal = false;
    },
    handleOk() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          let params = {
            ...this.form,
            dbProperties: JSON.parse(this.form.dbProperties),
          };
          if (params.id) {
            // 修改
            this.updateMethod(params);
          } else {
            // 新增
            delete params.id;
            this.addMethod(params);
          }
        }
      });
    },
    handleChange(value) {
      this.form.dbProperties = this.getPropertiesByKey(value);
    },
    async updateMethod(params) {
      let config = {
        api: METADATE_DATASOURCE_UPDATE_POST,
        method: "post",
        params,
      };
      this.loading = true;
      let res = await request(config.api, config.method, config.params, {
        baseURL: "/maccdata/api",
      });
      this.loading = false;
      if (!res.code) {
        this.$message.success(res.msg);
        this.handleCancel();
        this.$emit("ok");
      } else {
        this.$message.error(res.msg);
      }
    },

    async addMethod(params) {
      let config = {
        api: METADATE_DATASOURCE_ADD_POST,
        method: "post",
        params,
      };
      this.loading = true;
      let res = await request(config.api, config.method, config.params, {
        baseURL: "/maccdata/api",
      });
      this.loading = false;
      if (!res.code) {
        this.$message.success(res.msg);
        this.handleCancel();
        this.$emit("ok");
      } else {
        this.$message.error(res.msg);
      }
    },

    getPropertiesByKey(key) {
      let properties = {};
      if (this.dbTypeJson) {
        this.dbTypeJson.map((item) => {
          let type = item.key;
          if (type == key) {
            properties = item.properties;
          }
        });
      }
      return JSON.stringify(properties, null, 2);
    },

    // rules
    isJSON(str) {
      if (typeof str !== "string") {
        return false;
      }
      try {
        const obj = JSON.parse(str);
        return obj && typeof obj === "object";
      } catch (e) {
        return false;
      }
    },

    validateDataSourceKey(rule, value, callback) {
      let regex = /^[a-zA-Z][a-zA-Z0-9_-]*$/;
      if (value) {
        if (!regex.test(value)) {
          callback(new Error("请输入正确的格式"));
        } else {
          callback();
        }
      }
    },
    validateProperties(rule, value, callback) {
      if (value) {
        if (this.isJSON(value)) {
          callback();
        } else {
          callback(new Error("请输入json格式数据"));
        }
      }
    },
  },
};
</script>

<style lang="less" scoped>
.item {
  display: block;
}

/deep/ .ant-form-item {
  margin-bottom: 16px;

  &.ant-form-item-with-help {
    margin-bottom: 0;
  }
}

.more-wrapper {
  width: 25%;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 4px;
  margin-bottom: 16px;

  i {
    font-size: 16px;
    transition: transform 0.3s;

    &.not-show-more {
      transform: rotate(180deg);
    }
  }
}

.date-wrapper {
  margin-left: 25%;
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
  align-items: center;
}

.mb-0 {
  margin-bottom: 0;
}
</style>
