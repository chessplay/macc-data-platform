<template>
  <div class="mgt">
    <div class="tree">
      <a-button icon="plus" type="primary" @click="postslotModify(0)"
        >添加同级菜单</a-button
      >
      <a-tree
        ref="tree1"
        v-model="checkedKeys"
        v-if="treeData.length"
        defaultExpandAll
        :tree-data="treeData"
        :replace-fields="replaceFields"
        showLine
        blockNode
      >
        <template slot="subList" slot-scope="treeData">
          <span>{{ treeData.name }}</span>
          <a-button-group style="float:right">
            <a-button
              size="small"
              @click="postslotModify(treeData)"
              icon="plus-circle"
              title="添加菜单"
            ></a-button>
            <a-button
              size="small"
              @click="slotModify(treeData)"
              icon="form"
              title="修改"
            ></a-button>
            <a-button
              size="small"
              @click="slotDelete(treeData)"
              icon="close-circle"
              title="删除"
            ></a-button>
          </a-button-group>
        </template>
      </a-tree>
    </div>
    <Menumodule class="menumodule"></Menumodule>
    <Menutreeadd
      ref="modal"
      :visible="visible"
      @success="cancel('success')"
      @cancel="cancel"
      :dataRef="dataRef"
      :rowData="rowData"
    ></Menutreeadd>
  </div>
</template>
<script>
import EventService from '@p/services/eventService'
import Menutreeadd from '@p/views/powerMgt/roleMgt/modal/menumodule/menutreeadd.vue'
import Menumodule from '@p/views/powerMgt/menuMgt/Mgtmodule/menumodule.vue'
export default {
  components: {
    Menutreeadd,
    Menumodule,
  },
  data() {
    return {
      treeData: [],
      replaceFields: {
        children: 'subList',
        title: 'name',
        key: 'id',
      },
      checkedKeys: [],
      val: [],
      visible: false,
      dataRef: {},
      rowData: '',
      menuid: 0,
    }
  },
  props: {
    rolemenu: {
      type: String,
    },
    rolemenuname: {
      type: String,
    },
  },
  computed: {
    rolemenudata: function() {
      let value = this.rolemenu
      return value
    },
  },
  watch: {
    checkedKeys(newval) {
      this.val = newval
    },
  },
  mounted() {
    this.treeshow()
  },
  methods: {
    treeshow() {
      const that = this
      EventService.menutree().then((ret) => {
        that.treeData = that.deepClone(ret.list)
      })
    },
    postslotModify(record) {
      this.dataRef = {}
      if (record == 0) {
        this.dataRef.id = 0
      } else {
        this.dataRef.id = record.id
      }
      this.rowData = String(this.dataRef.name)
      this.visible = true
    },
    slotModify(record) {
      this.dataRef = {}
      this.dataRef = record.dataRef
      this.rowData = String(record.dataRef.name)
      this.visible = true
    },
    slotDelete(record) {
      const that = this
      this.$confirm({
        title: '删除菜单?',
        content: (h) => (
          <div style="color:red;">确定要删除{record.dataRef.name}菜单吗？</div>
        ),
        onOk() {
          EventService.menudelete(record.dataRef.id).then((ret) => {
            EventService.editCallBack(ret, ret.msg)
            that.treeshow()
          })
        },
        onCancel() {},
      })
    },
    cancel(value) {
      const that = this
      if (value) {
        const form = this.$refs.modal.form
        form.validateFields((err, values) => {
          if (err) {
            return
          }
          if (values.menuid == '') {
            that.menuid = 0
          } else {
            that.menuid = values.menuid
          }
          if (that.rowData == 'undefined') {
            EventService.menuadd(
              that.menuid,
              values.menuname,
              values.menuurl,
              values.menuicon,
              values.menuserial,
              values.menuparameter
            ).then((ret) => {
              EventService.editCallBack(ret, ret.msg)
              that.treeshow()
              that.visible = false
            })
          } else {
            EventService.menuupdata(
              that.menuid,
              values.menuname,
              values.menuurl,
              values.menuicon,
              values.menuserial,
              values.menuparameter
            ).then((ret) => {
              EventService.editCallBack(ret, ret.msg)
              that.treeshow()
              that.visible = false
            })
          }
        })
      } else {
        that.visible = false
      }
    },
    deepClone(obj) {
      //可传入对象 或 数组
      //  判断是否为 null 或 undefined 直接返回该值即可,
      if (obj === null || !obj) return obj
      // 判断 是要深拷贝 对象 还是 数组
      if (Object.prototype.toString.call(obj) === '[object Object]') {
        //对象字符串化的值会为 "[object Object]"
        let target = {} //生成新的一个对象
        target.scopedSlots = { title: 'subList' }
        const keys = Object.keys(obj) //取出对象所有的key属性 返回数组 keys = [ ]
        //遍历复制值, 可用 for 循环代替性能较好
        keys.forEach((key) => {
          if (obj[key] && typeof obj[key] === 'object')
            //如果遇到的值又是 引用类型的 [ ] {} ,得继续深拷贝
            target[key] = this.deepClone(obj[key])
          //递归
          else target[key] = obj[key]
        })
        return target //返回新的对象
      } else if (Array.isArray(obj)) {
        // 数组同理
        let arr = []
        obj.forEach((item, index) => {
          if (item && typeof item === 'object')
            arr[index] = this.deepClone(item)
          else arr[index] = item
        })
        return arr
      }
    },
  },
}
</script>

<style lang="less" scoped>
.mgt {
  display: flex;
  justify-content: space-between;
  .tree {
    width: 30%;
    margin-left: 20px;
    margin-top: 15px;
  }
  .menumodule {
    width: 65%;
  }
}
</style>
