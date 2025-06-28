<!--
description: 测试
auth: zcj at 2021.2.19
-->
<template>
  <div class="dc-page-card">
    <a-button-group>
      <a-button @click="query">query</a-button>
      <a-button @click="addOrUpdate">addOrUpdate</a-button>
      <a-button @click="remove">delete</a-button>
      <a-button @click="test">test</a-button>
    </a-button-group>
  </div>
</template>
<script>
import EventService from '@p/services/eventService'
import menuData from './menuConfig'
export default {
  name: 'test',
  components: {},
  data() {
    return {
      menuText: ''
    }
  },
  computed: {},
  methods: {
    query() {
      EventService.query().then(ret => {
        console.log(ret)
      })
    },
    addOrUpdate() {
      EventService.addOrUpdate({
        name: 'stest',
        type1: 'sceneMacc',
        type2: 'sc-businessConf',
        extColumns: 'groupId,workId,funcId',
        remark: 'test'
      })
    },
    remove() {
      EventService.delete({
        name_list: ['stest']
      })
    },
    getMenuData(parentPath, list) {
      list.forEach((item, index) => {
        let fullPath = parentPath + '/' + item.path
        if (item.children && item.children.length > 0) {
          this.getMenuData(fullPath, item.children)
        } else {
          this.menuText += fullPath + '\t' + item.label + '\n'
        }
      })
    },
    fun1() {
      var promise = new Promise(function(resolve, reject) {
        window.setTimeout(function() {
          resolve('hello')
        })
      })
      return promise
    },
    async test() {
      let h = await this.fun1()
      alert(h)
    }
  },
  created() {
    // this.getMenuData("",menuData);
    //console.log(this.menuText);
  },

  mounted() {}
}
</script>

<style lang="less" scoped>
.bg {
  background: #fff;
  min-height: 660px;
  padding: 20px 20px;
}
</style>
