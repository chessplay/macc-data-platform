<!--
旧的拓扑视图
-->
<template>
  <baseTopo class="topo" :id="id" @click-dev="clickDev" ref="topoObj" style=" height: 100%;width: 100%;">
  </baseTopo>
</template>
<script>
import baseTopo from '@p/components/topo/index.vue'
import TopoService from '@p/services/topoService.js'
// import { request, METHOD } from '@/utils/request'

//deviceType 设备类型分类
const DEVTYPE = {
  EG: ['EGW', 'EWR', 'GATEWAY'],
  SW: ['SWITCH', 'ESW', 'MSW'],
  AP: ['AP', 'EAP'],
  AC: ['AC', 'EAC'],
  NVR: ['NVR'],
  IPC: ['IPC'],
  EHR: ['EHR'],
}

export default {
  name: 'businessMap',
  props: {
    gid: {
      typeof: String,
    },
    deviceModel: {
      typeof: String,
    },
  },
  components: {
    baseTopo,
  },
  data () {
    return {
      query: this.$route.query,
      id: 'd3_tree',
      portInfoMap: {},
      deviceListMap: {},
      dhcpInfoMap: {},
      copydata: undefined,
    }
  },
  watch: {
    gid (v) {
      if (v) {
        this.portInfoMap = {}
        this.copydata = undefined
        this.initView()
      }
    },
    deviceModel (v) {
      if (v == 'topo') {
        this.$refs['topoObj'].randerTopo(this.copydata)
      }
    },
  },
  created () { },
  activated () { },
  mounted () {
    // 定义窗口大小变更通知事件
    window.onresize = () => {
      if (this.gid !== '' && this.deviceModel == 'topo') {
        this.$refs['topoObj'].randerTopo(this.copydata)
      }
    }
  },
  methods: {
    async initView () {
      await this.getGroupPortInfo()
      // await this.getBizmapDetail();
      await this.getTopoInfo()
    },
    clickDev (d) {
      // let data = {
      //   command: 'clickVlanDevice',
      //   payload: {
      //     title: '业务地图点击设备',
      //     params: {
      //       sn: d.data.deviceSn,
      //     },
      //   },
      // };
      // console.log('clickVlanDevice', data);
    },
    getGroupPortInfo () {
      return new Promise((resolve) => {
        // let params = {
        //   params: {
        //     group_id: this.query.groupId,
        //   },
        //   data: {
        //     accessKey: this.query.accessKey,
        //   },
        // }

        // TopoService.getGroupPortInfo().then((res) => {
        //   //console.log('getGroupPortInfo', res)
        //   this.portInfoMap = res
        //   resolve(res)
        // })
        let dataRow = {
          url: '/service/api/device/network/port_info',
          httpMethod: 'GET',
          queryParams: {
            group_id: this.gid,
          },
        }
        TopoService.getGroupPortInfo(this.gid, dataRow).then((res) => {
          this.portInfoMap = res
          resolve(res)
        })
      })
    },
    // getBizmapDetail() {
    //   return new Promise((resolve) => {
    //     let params = {
    //       params: {
    //         vlan: this.query.vlan,
    //         group_id: this.query.groupId,
    //       },
    //       data: {
    //         accessKey: this.query.accessKey,
    //       },
    //     };
    //     getBizmapDetail(params).then((res) => {
    //       console.log('getBizmapDetail', res);
    //       this.abnormalPortMap = {};
    //       if (res.deviceList) {
    //         this.deviceListMap = {};
    //         res.deviceList.forEach((item) => {
    //           let abnormalPort = {};
    //           this.deviceListMap[item.sn] = item;
    //           (item.portList || []).forEach((item) => {
    //             if (item.status && item.status.toLocaleLowerCase() == 'abnormal') {
    //               abnormalPort[item.alias] = item;
    //             }
    //           });
    //           this.abnormalPortMap[item.sn] = abnormalPort;
    //         });
    //       }
    //       console.log('abnormalPortMap', this.abnormalPortMap);
    //       if (res.dhcpInfo) {
    //         this.dhcpInfoMap = {};
    //         res.dhcpInfo.forEach((item) => {
    //           this.dhcpInfoMap[item.sn] = item;
    //         });
    //       }
    //       resolve(res);
    //     });
    //   });
    // },
    getTopoInfo () {
      return new Promise((resolve, reject) => {
        // TopoService.getTopoInfo().then((res) => {
        //   let data = this.formatTopoInfo(res.data || {})
        //   //console.log('getTopoInfo', data)
        //   this.copydata = data
        //   this.$refs.topoObj.randerTopo(data)
        // })

        // request('/service/agent/group/' + this.gid, METHOD.POST, {
        //   url: '/service/api/topology/info/' + this.gid,
        //   httpMethod: 'GET',
        //   queryParams: {},
        // })

        let dataRow = {
          url: '/service/api/topology/info/' + this.gid,
          httpMethod: 'GET',
          queryParams: {},
        }
        TopoService.getTopoInfo(this.gid, dataRow).then((res) => {
          //console.log(res)
          if (res.data) {
            let data = this.formatTopoInfo(res.data || {})
            this.copydata = data
            if (this.deviceModel == 'topo') {
              this.$refs.topoObj.randerTopo(data)
            }
          } else {
            this.copydata = undefined
            if (this.deviceModel == 'topo') {
              this.$refs.topoObj.randerTopo(this.copydata)
            }
          }
        })
      })
    },
    formatTopoInfo (topoData) {
      const formatChildren = (data) => {
        let res = []
          ; (data.children || []).forEach((item) => {
            //如果是IPC或者AP设备且不在业务网内过滤掉
            if (
              (DEVTYPE.AP.indexOf(item.deviceType) > -1 ||
                DEVTYPE.IPC.indexOf(item.deviceType) > -1) &&
              !this.deviceListMap[item.deviceSn]
            ) {
              return
            }
            data.deviceSn && (item.parentDeviceSn = data.deviceSn)
            item.deviceType &&
              (item.deviceType = item.deviceType.toLocaleUpperCase())
            data.deviceType && (item.parentDeviceType = data.deviceType)
            item.children = formatChildren(item)
            item.ports = this.formatPortInfo(item)
            item.isDhcpServer = this.dhcpInfoMap[item.deviceSn]
            item.showStatus = 'default'
            if (this.deviceListMap[item.deviceSn]) {
              item.status = this.deviceListMap[item.deviceSn].status
              item.showStatus =
                (this.deviceListMap[item.deviceSn].status.toLocaleUpperCase() ==
                  'ABNORMAL' &&
                  'danger') ||
                'info'
            }
            // item.showLineStatus =
            //   (this.abnormalPortMap[item.deviceSn] &&
            //     this.abnormalPortMap[item.deviceSn][item.uplinkPort]) ||
            //   (this.abnormalPortMap[item.parentDeviceSn] &&
            //     this.abnormalPortMap[item.parentDeviceSn][item.parentPort])
            //     ? 'danger'
            //     : 'primary'
            // //在业务网内设备，且端口连线正常的显示动画小球
            // this.deviceListMap[item.deviceSn] &&
            //   item.showLineStatus != 'danger' &&
            //   (item.showLineAnimate = true)
            res.push(item)
          })
        return res
      }
      let result = topoData
      result.children = formatChildren(result)
      return result
    },
    //格式化面板数据, 异常端口
    formatPortInfo (dev) {
      const Status = {
        up: 'primary',
        abnormal: 'danger',
        down: 'black',
        inVlan: 'black',
      }
      let port = [],
        res = [],
        devPortMap = {},
        isEG = DEVTYPE.EG.indexOf(dev.deviceType) > -1,
        isSW = DEVTYPE.SW.indexOf(dev.deviceType) > -1,
        maxOrder = 0
      if (!isEG && !isSW) return false
      if (dev.productClass) {
        port = this.portInfoMap[dev.productClass] || false
      }
      if (!port) return false
      if (this.deviceListMap[dev.deviceSn]) {
        ; (this.deviceListMap[dev.deviceSn].portList || []).forEach((item) => {
          isEG && (devPortMap[item.port] = item)
          isSW && (devPortMap[item.order] = item)
        })
      }
      port.forEach((line) => {
        let linePorts = []
        line.forEach((item) => {
          if (item) {
            item = isEG ? { ...item } : isSW ? { port: item } : {}
            item._order = item.port.replace(/\D+/g, '')
            item.isFiber = /\d+F$/.test(item.port)
            item.iconType = (item.isFiber && 'isFiber') || 'default'
            if (devPortMap[item._order]) {
              item.status = devPortMap[item._order].status
              item.showStatus =
                Status[
                (item.status && item.status.toLocaleLowerCase()) || 'inVlan'
                ] || 'default'
            }
            maxOrder = Math.max(maxOrder, item._order)
          }
          linePorts.push(item)
        })
        res.push(linePorts)
      })
      //获取设备口数
      dev.maxOrder = maxOrder
      return res
    },
  },
}
</script>
<style lang="less">
#d3_tree {
  // position: absolute;
  width: 100%;
  height: 100%;
  // left: 0;
  // right: 0;
  overflow: hidden;
}
</style>
