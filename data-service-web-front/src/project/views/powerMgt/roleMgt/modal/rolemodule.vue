<template>
<div>
  <a-table
        tableLayout="fixed"
        size="middle"
        class="dc-tb-stripe"
        :columns="columns"
        :data-source="data"
        :pagination="pagination"
      >
      <span slot="action" slot-scope="text, record">
      <a-checkbox-group @change="onchange" v-model="antionvalue[record.key]">
        <a-row>
            <a-checkbox :value="record.key+'read'" @click="valuechange(record)"></a-checkbox>
            <a-checkbox :value="record.key+'write'" @click="valuechange(record)"></a-checkbox>
        </a-row>
      </a-checkbox-group>
      </span>
  </a-table>
</div>
  
</template>

<script>
import EventService from "@p/services/eventService";
const columns = [
  {
    title: '模块ID',
    dataIndex: 'id',
  },
  {
    title: '模块名称',
    dataIndex: 'name',
  },
  {
    title: '读 写',
    dataIndex: 'action',
    scopedSlots: { customRender: 'action' },
  }
]
const pagination = {
  defaultPageSize: 50,
  hideOnSinglePage:true
}
export default {
  components:{
  },
  data(){
    return{
      data:[],
      columns,
      pagination,
      checkedList:'',
      antionvalue: [],
      rowData: "",
      next: {},
      checkedListkey:0,
    }
  },
  props:{
    rolemenu:{
      type:String
    },
  },
  computed:{
    rolemenudata: function () {
      let value = this.rolemenu
      return value
    },
  },
  watch: {
    rolemenu() {
      this.roleshow()
    },
  },
  mounted(){
    this.roleshow()
    this.show()
  },
  methods:{
    show(){
      this.data = []
      EventService.modulelist().then((ret) => {
        for(let i=0;i<ret.list.length;i++){
          let rolemenu = {}
            rolemenu.key=i,
            rolemenu.id=ret.list[i].moduleId,
            rolemenu.name=ret.list[i].moduleName
            rolemenu.description=ret.list[i].description
          this.data.push(rolemenu)
        }
      })
    },
    roleshow(){
      this.antionvalue = []
      const that = this
      EventService.rolemodulelist(this.rolemenudata).then((ret) => {
        if(ret.ok == false)return
          for(let j=0;j<that.data.length;j++){
            let newvalue = []
            for(let i=0;i<ret.list.length;i++){
              if(that.data[j].id == ret.list[i].moduleId){
                if(ret.list[i].privilege == 'READ'){
                  newvalue.push(`${j}read`);break
                }else{newvalue.push(`${j}write`);break}
              }
              if(i+1 == ret.list.length){
                newvalue.push('')
              }
          }
          that.antionvalue.push(newvalue)
        }
      })
    },
    valuechange(record){
      this.checkedListkey = record.key
      this.checkedList = record.id
    },
    onchange(record) {
      let newchoice = []
      const that = this
      if(record.length == 2){
         newchoice =  [record[1]]
      }else{
        newchoice = record
      }
      if(newchoice[0] == undefined){
        EventService.moduleinfo(this.rolemenu,this.checkedList,"NONE").then((ret) => {
          that.roleshow()
          EventService.editCallBack(ret, ret.msg);         
        })
      }else{
        if(newchoice[0].replace(/[^a-zA-Z]/g,'')=="read"){
          EventService.moduleinfo(this.rolemenu,this.checkedList,"READ").then((ret) => {
            that.roleshow()
            EventService.editCallBack(ret, ret.msg);
          })
        }else{
          EventService.moduleinfo(this.rolemenu,this.checkedList,"WRITE").then((ret) => {
            that.roleshow()
            EventService.editCallBack(ret, ret.msg);
          })
        }
      }
    },
    vsubSet(arr1, arr2) {
      let set1 = new Set(arr1);
      let set2 = new Set(arr2); 
      let subset = [];
      for (let item of set1) {
          if (!set2.has(item)) {
             subset.push(item);
         }
     }
     return subset
    }
  }
}
</script>

<style lang="less" scoped>

</style>