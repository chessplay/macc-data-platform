<template>
  <div class="dc-page-card">
    <div class="main">
      <header>
        <a-button type="primary" icon="plus" @click="addLabel({})"
          >添加同级标签类型</a-button
        >
      </header>

      <a-tree
        ref="tree"
        class="label-tree"
        v-model="checkedKeys"
        v-if="treeData.length"
        defaultExpandAll
        :tree-data="treeData"
        :replace-fields="replaceFields"
        showLine
        blockNode
      >
        <template slot="subList" slot-scope="treeData">
          <div class="btn">
            <span>{{ treeData.typeName }}</span>
            <a-button-group>
              <a-button
                size="small"
                @click="addLabel(treeData)"
                icon="plus-circle"
                title="添加标签类型"
              ></a-button>
              <!-- <a-button
                size="small"
                @click="modifyLabel(treeData)"
                icon="form"
                title="修改标签类型"
              ></a-button> -->
              <a-button
                size="small"
                @click="deleteLabel(treeData)"
                icon="close-circle"
                title="删除标签类型"
              ></a-button>
            </a-button-group>
          </div>
        </template>
      </a-tree>

      <a-modal
        :title="selectData.typeId ? '添加子标签类型' : '添加同级标签类型'"
        :maskClosable="false"
        :visible="visible"
        :confirm-loading="confirmLoading"
        @ok="handleOk"
        @cancel="handleCancel"
      >
        <a-form :form="form" :modal="selectData">
          <a-form-item label="标签类型ID" v-bind="formItemLayout">
            <a-input
              v-decorator="[
                'typeId',
                {
                  rules: [{ required: true, message: '请输入标签类型ID' }],
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="标签类型名称" v-bind="formItemLayout">
            <a-input
              v-decorator="[
                'typeName',
                {
                  rules: [{ required: true, message: '请输入标签类型名称' }],
                },
              ]"
            />
          </a-form-item>
          <!-- <a-form-item label="标签类型英文名称" v-bind="formItemLayout">
            <a-input
              v-decorator="[
                'typeEn',
                {
                  rules: [
                    { required: true, message: '请输入标签类型英文名称' },
                  ],
                },
              ]"
            />
          </a-form-item> -->
          <a-form-item
            label="父级标签类型ID"
            v-bind="formItemLayout"
            v-if="selectData.id"
            v-show="false"
          >
            <a-input
              v-decorator="[
                'parentId',
                {
                  initialValue: selectData.id,
                },
              ]"
              disabled
            />
          </a-form-item>
          <a-form-item label="描 述" v-bind="formItemLayout">
            <a-textarea v-decorator="['description']" />
          </a-form-item>
        </a-form>
      </a-modal>
    </div>
  </div>
</template>

<script>
import Service from "@p/services/labelService";
export default {
  name: "labelType",
  data() {
    return {
      treeData: [],
      form: this.$form.createForm(this, { name: "lableTypeList" }),
      replaceFields: {
        children: "subList",
        title: "typeName",
        key: "typeId",
      },
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 14 },
        },
      },
      checkedKeys: [],
      selectData: {},
      visible: false,
      confirmLoading: false,
    };
  },
  mounted() {
    this.getLabelTree();
  },
  methods: {
    async getLabelTree() {
      let res = await Service.getLabelTree();
      if (res.code == 0) {
        this.treeData = this.deepClone(res.list);
      } else {
        this.$message.error(res.msg);
      }
    },
    addLabel(data) {
      this.selectData = data;
      this.visible = true;
    },
    handleOk() {
      this.form.validateFields(async (err, values) => {
        if (err) {
          return;
        }
        let self = this;
        debugger;
        this.$confirm({
          title: "是否添加新的标签类型？",
          async onOk() {
            let res = await Service.addLabelType(values);
            if (res.code == 0) {
              self.$message.success(res.msg);
              self.getLabelTree();
              self.form.resetFields();
              self.visible = false;
            } else {
              self.$message.error(res.msg);
            }
          },
        });
      });
    },
    handleCancel() {
      this.visible = false;
      this.form.resetFields();
    },
    modifyLabel() {},
    deleteLabel(data) {
      let self = this;
      this.$confirm({
        title: `是否删除${data.typeName}标签类型？`,
        async onOk() {
          let res = await Service.deleteLabelType(data.id);
          if (res.code == 0) {
            self.$message.success(res.msg);
            self.getLabelTree();
          } else {
            self.$message.error(res.msg);
          }
        },
      });
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
  },
};
</script>
<style scoped>
.btn {
  display: flex;
  justify-content: space-between;
}
.label-tree {
  margin-top: 15px;
}
.main {
  width: 30%;
  /* padding: 15px 0;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  vertical-align: middle; */
  /* justify-content: space-between; */
}
</style>
