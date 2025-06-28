<template>
  <div>
    <h3>{{ rolemenuname }}的菜单</h3>
    <a-tree
      ref="tree1"
      v-model="checkedKeys"
      v-if="treeData.length"
      defaultExpandAll
      checkable
      :tree-data="treeData"
      :replace-fields="replaceFields"
      @check="onCheck"
    >
    </a-tree>
  </div>
</template>
<script>
import EventService from "@p/services/eventService";
export default {
  components: {},
  data() {
    return {
      treeData: [],
      replaceFields: {
        children: "subList",
        title: "name",
        key: "id",
      },
      checkedKeys: [],
      val: [],
    };
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
      let value = this.rolemenu;
      return value;
    },
  },
  watch: {
    rolemenu() {
      this.menushow();
    },
    checkedKeys(newval) {
      this.val = newval;
    },
  },
  mounted() {
    this.menushow();
    this.treeshow();
  },
  methods: {
    treeshow() {
      EventService.menutree().then((ret) => {
        this.treeData = ret.list;
      });
    },
    menushow() {
      EventService.rolemenulist(this.rolemenudata).then((ret) => {
        this.checkedKeys = ret.list.map((item) => {
          return item.menuId;
        });
        console.log(ret.list, 1111111);
      });
    },
    getArrDifference(arr1, arr2) {
      return arr1.concat(arr2).filter(function(v, i, arr) {
        return arr.indexOf(v) === arr.lastIndexOf(v);
      });
    },
    onCheck(localval, localnew) {
      let val = this.getArrDifference(localval, this.val);
      if (localnew.checked == true) {
        for (let i = 0; i < val.length; i++) {
          EventService.rolemenuadd(this.rolemenudata, val[i]).then((ret) => {
            EventService.editCallBack(ret, ret.msg);
          });
        }
      } else {
        for (let j = 0; j < val.length; j++) {
          EventService.rolemenudelete(this.rolemenudata, val[j]).then((ret) => {
            EventService.editCallBack(ret, ret.msg);
          });
        }
      }
    },
  },
};
</script>

<style lang="less" scoped></style>
