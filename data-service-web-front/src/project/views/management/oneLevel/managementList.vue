<template>
  <div class="projectlist">
    <a-card
      title="一级渠道列表"
      :bordered="false"
      :bodyStyle="{ padding: '0 ', height: '80vh' }"
      style="position: relative"
    >
      <span class="total">共{{ totalCount }}个</span>
      <div class="search">
        <a-input
          placeholder="搜索渠道名称"
          :allow-clear="true"
          v-model="keyword"
        ></a-input>
        <a-button type="primary" icon="search" @click="search">搜索</a-button>
      </div>
      <a-spin :spinning="loading">
        <a-list
          item-layout="vertical"
          size="large"
          :data-source="listData"
          v-if="listData.length > 0"
          class="prolist"
          @scroll="scroll($event)"
        >
          <a-list-item
            slot="renderItem"
            slot-scope="item"
            :key="item.cmpny_name"
            class="listitem"
            :class="{ active: item.cmpny_id == classFlag }"
            @click="changeItem(item)"
          >
            <a-list-item-meta>
              <span slot="title"
                ><span style="font-weight: bold">[{{ item.cmpny_id }}]</span>
                {{ item.cmpny_name }}</span
              >
              <a-table
                :pagination="false"
                slot="description"
                :row-key="(item) => item.cmpny_id"
                :columns="columns"
                :showHeader="false"
                :data-source="itemToArray(item)"
                :bordered="false"
                size="small"
              >
              </a-table>
            </a-list-item-meta>
          </a-list-item>
        </a-list>
        <a-empty v-else />
      </a-spin>
    </a-card>
  </div>
</template>
<script>
const columns = [
  // {
  //   title: '公司ID',
  //   dataIndex: 'cmpny_id',
  //   key: 'cmpny_id',
  //   align: 'center',
  // },
  // {
  //   title: '公司名称',
  //   dataIndex: 'cmpny_name',
  //   key: 'cmpny_name',
  //   align: 'center',
  // },
]
// 左边列表页面
import Service from '@p/services/managementService'
export default {
  data() {
    return {
      columns,
      loading: false,
      classFlag: 0,
      listData: [], //项目列表
      changekey: '', //项目列表选择的标识
      pageIndex: 1, //存放分页的索引
      keyword: '', //查询关键字
      isQueryKeyword: '', //已搜索过得关键字
      totalCount: 0,
      company_type: '一级渠道',
    }
  },
  mounted() {
    this.getList(this.company_type, '')
  },
  methods: {
    itemToArray(item) {
      let arr = []
      arr.push(item)
      return arr
    },
    //搜索出匹配的信息
    async getList(company_type, keyword) {
      this.loading = true
      const obj = {
        params: {
          company_type,
          keyword,
        },
        pageIndex: this.pageIndex,
        pageSize: 10000,
      }
      let res = await Service.managementDynamic('um-company-page', obj)
      this.loading = false
      if (res.code == 0) {
        res.dataList.forEach((v) => {
          this.listData.push(v)
        })
        this.totalCount = res.totalCount
      } else {
        this.$message.error(res.msg)
      }
    },
    changeItem(item) {
      this.$emit('sendOneLevelData', item)
      this.classFlag = item.cmpny_id
    },
    search() {
      this.pageIndex = 1
      this.isQueryKeyword = this.keyword
      this.listData = []
      this.getList(this.company_type, this.keyword)
    },
    async scroll(e) {
      if (e.target.scrollTop + e.target.clientHeight >= e.target.scrollHeight) {
        if (this.loading || this.totalCount == this.listData.length) {
          console.log('到底了')
          return
        }
        this.pageIndex++
        this.getList(this.isQueryKeyword)
      }
    },
  },
}
</script>
<style scoped lang="less">
.total {
  position: absolute;
  right: 15px;
  top: 17px;
}
.search {
  margin-bottom: 15px;
  display: flex;
  // justify-content: space-between;
  padding: 0 15px;
  button {
    margin-left: 10px;
  }
}
.active {
  background-color: #f2f6fe;
  color: red;
}
.projectlist {
  display: inline-block;
  background-color: #fff;
  margin: 2vh 1vw 0 0;
  width: 14vw;
}
.listitem {
  padding: 5px 6px;
  border-bottom: none;
  & span {
    font-size: 14px;
    text-align: center;
  }
}
.prolist {
  overflow-y: scroll;
  height: 77vh;
}
/deep/ .projectlist {
  & .ant-card-head-title {
    padding-bottom: 12px;
    padding-top: 20px;
  }
}
.ant-list-vertical .ant-list-item-meta {
  margin-bottom: 3px;
}
.ant-list-vertical .ant-list-item-meta-title {
  margin-bottom: 3px;
}
</style>
<style>
.projectlist .ant-table-thead > tr > th {
  font-weight: normal;
}
</style>
