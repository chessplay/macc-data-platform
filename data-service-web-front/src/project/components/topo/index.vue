<template>
  <div :id="id">
    <svg :visibility="loaded ? 'visilble' : 'hidden'" class="d3-tree" id="topo_tree" v-show="isData">
      <g class="container" id="container" />
    </svg>
    <div class="topo-action-btn-container" v-if="loaded">
      <div class="topo-action-btn-group">
        <div @click="rotate" class="topo-action-btn">
          <i class="iconfont icon-ico_fanzhuan"></i>
          <span>翻转</span>
        </div>
        <div @click="returned" class="topo-action-btn">
          <i class="iconfont icon-ico_huanyuan"></i>
          <span>还原</span>
        </div>
      </div>
    </div>
    <a-empty v-show="!isData" />
  </div>

</template>
<script>
import * as d3 from 'd3'
import APIMGCOFING from '@/assets/topo/apImgConfig.js'
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
//特殊设备图片
const SPECIALIMG = {
  EG105: ['EG105GW'],
}
const ICONNAME = {
  isFiber: ['guangkou', 'guangkou'],
  loopState: ['jichu-zhuan_zs-fanghuanlu', 'ZS-fanghuanlu'],
  isBlock: ['jichu-zhuan_zs-zusaikou', 'ZS-zusaikou'],
  uplink: ['jichu-zhuan_zs-shangliankou', 'ZS-shangliankou'],
  poeOn: ['jichu-zhuan_zs-PoEgongdianzhengchang', 'ZS-PoEgongdianzhengchang'],
  poeErroneous: ['jichu-zhuan_zs-PoEgongdianyichang', 'ZS-PoEgongdianyichang'],
  default: ['duankou_xia', 'duankou_shang'],
}
const NODATA = {
  name: 'OUT',
  deviceType: 'OUT',
  deviceSn: 'OUT',
  isDirect: false,
  children: [
    {
      deviceSn: 'NODATA',
      deviceType: 'NODATA',
    },
  ],
}
export default {
  name: 'baseTopo',
  props: {
    id: {
      type: String,
      default: 'd3_tree',
    },
  },
  data() {
    return {
      query: this.$route.query || {},
      loaded: false,
      devImg: {
        OUT: require('@/assets/topo/OUT.png'),
        unkown: require('@/assets/topo/unkown.png'),
        ES: require('@/assets/topo/ES.png'),
        EG105: require('@/assets/topo/GW105.png'),
        EG: require('@/assets/topo/EG.png'),
        AP: require('@/assets/topo/AP.png'),
        AC: require('@/assets/topo/AC.png'),
        NVR: require('@/assets/topo/NVR.png'),
        IPC: require('@/assets/topo/IPC.png'),
        EHR: require('@/assets/topo/EHR.png'),
      },
      DEVTYPE,
      data: NODATA,
      isLevel: false, //是否水平画图
      zoom: null,
      index: 0,
      duration: 0,
      root: null,
      nodes: [],
      links: [],
      ports: [],
      size: { width: 210, height: 100 },
      smallScale: 0.3,
      scale: 1,
      oldScale: 1,
      canSelectDevType: [],
      isData: false,
    }
  },
  created() {},
  activated() {},
  mounted() {},
  methods: {
    randerTopo(data) {
      //创建svg画布
      if (data) {
        this.isData = true
        this.width = document.getElementById(this.id).clientWidth
        this.height = document.getElementById(this.id).clientHeight
        const svg = d3
          .select('svg.d3-tree')
          .attr('width', this.width)
          .attr('height', this.height)
        // init zoom behavior
        this.zoom = d3.zoom().scaleExtent([0.1, 8]).on('zoom', this.zoomed)
        svg.call(this.zoom)
        this.root = this.getRoot(data || NODATA)
        this.update(this.root)
        setTimeout(() => {
          this.loaded = true
          this.fixPositon()
        }, this.duration)
      } else {
        this.isData = false
      }
    },
    zoomed() {
      this.scale = d3.event.transform.k
      let x = d3.event.transform.x
      let y = d3.event.transform.y
      d3.event.transform.x = Math.max(
        -(this.gPosition.x + this.gPosition.width - 20) * this.scale,
        Math.min(x, this.width - (this.gPosition.x + 20) * this.scale)
      )
      d3.event.transform.y = Math.max(
        -(this.gPosition.y + this.gPosition.height - 20) * this.scale,
        Math.min(y, this.height - (this.gPosition.y + 20) * this.scale)
      )
      d3.select('g.container').attr('transform', d3.event.transform)
      if (
        (this.oldScale >= this.smallScale && this.scale < this.smallScale) ||
        (this.oldScale <= this.smallScale && this.scale > this.smallScale)
      ) {
        this.update(this.root)
      }
      this.oldScale = this.scale
    },
    getRoot(data) {
      this.data = data
      let root = d3.hierarchy(data, (d) => {
        return d.children
      })
      root.x0 = this.height / 2
      root.y0 = 0
      return root
    },
    //获取节点数据
    getNodesAndLinks() {
      this.dTreeData = this.treemap(this.root)
      this.nodes = this.dTreeData.descendants()
      this.links = this.dTreeData.descendants().slice(1)
      this.ports = this.dTreeData.descendants().slice(1)
    },
    //校验是否可选设备 _type: enable|unable 默认enable。
    checkSelectable(item, _type) {
      let res, baseEnable, virtualEnable, type
      type = _type || 'enable'
      baseEnable =
        ((!item.isVirtualDev &&
          this.canSelectDevType.indexOf(item.deviceType) > -1) ||
          (item.isVirtualDev &&
            this.canSelectDevType.indexOf(item.swType) > -1)) &&
        item.onlineStatus == 'ON'
      // 虚拟设备业务上选择限制;
      // 1、虚拟设备父设备是NBR
      // 2、虚拟设备有子设备
      virtualEnable =
        item.isVirtualDev &&
        (item.parentDeviceType == 'GATEWAY' ||
          (item.children && item.children.length))
      switch (type) {
        case 'enable':
          res = baseEnable && !virtualEnable
          break
        case 'unable':
          res = !(baseEnable && !virtualEnable)
          break
      }
      return res
    },
    diagonal(s, d) {
      return this.isLevel
        ? //垂直连线
          `M ${s.y - this.size.width / 2} ${s.x}
                C ${(s.y + d.y) / 2} ${s.x},
                ${(s.y + d.y) / 2} ${d.x},
                ${d.y + this.size.width / 2} ${d.x}`
        : //水平连线
          `M ${s.x} ${s.y - this.size.height / 2}
                C ${s.x} ${(s.y + d.y) / 2},
                ${d.x} ${(s.y + d.y) / 2},
                ${d.x} ${d.y + this.size.height / 2}`
    },
    clickSwitchChildren(d) {
      if (!d._children && !d.children) return
      if (d.children) {
        this.$set(d, '_children', d.children)
        d.children = null
      } else {
        this.$set(d, 'children', d._children)
        d._children = null
      }
      this.$nextTick(() => {
        this.update(d)
      })
    },
    getDevImg(d) {
      let devImg, type
      switch (true) {
        //需要画端口选择图片
        case !!d.data.ports:
          if (SPECIALIMG.EG105.indexOf(d.data.productClass) > -1) {
            devImg = this.devImg.EG105
          } else if (d.data.maxOrder <= 5) {
            !this.devImg.ES5 &&
              (this.devImg.ES5 = require('@/assets/topo/ES5.png'))
            devImg = this.devImg.ES5
          } else if (d.data.maxOrder > 5 && d.data.maxOrder <= 24) {
            !this.devImg.ES24 &&
              (this.devImg.ES24 = require('@/assets/topo/ES24.png'))
            devImg = this.devImg.ES24
          } else if (d.data.maxOrder > 24) {
            !this.devImg.ES48 &&
              (this.devImg.ES48 = require('@/assets/topo/ES48.png'))
            devImg = this.devImg.ES48
          }
          break
        case this.DEVTYPE.EG.indexOf(d.data.deviceType) > -1:
          devImg =
            SPECIALIMG.EG105.indexOf(d.data.productClass) > -1
              ? this.devImg.EG105
              : this.devImg.EG
          type = 'EG'
          break
        case this.DEVTYPE.SW.indexOf(d.data.deviceType) > -1:
          devImg = this.devImg.ES
          type = 'SW'
          break
        case this.DEVTYPE.AP.indexOf(d.data.deviceType) > -1:
          let imgName = APIMGCOFING[d.data.productClass] || 'AP'
          !this.devImg[imgName] &&
            (this.devImg[imgName] = require(`@/assets/topo/${imgName}.png`))
          devImg = this.devImg[imgName]
          type = 'AP'
          break
        case this.DEVTYPE.AC.indexOf(d.data.deviceType) > -1:
          devImg = this.devImg.AC
          type = 'AC'
          break
        case this.DEVTYPE.NVR.indexOf(
          d.data.deviceType ? d.data.deviceType.toUpperCase() : 'unkown'
        ) > -1:
          devImg = this.devImg.NVR
          type = 'NVR'
          break
        case this.DEVTYPE.IPC.indexOf(
          d.data.deviceType ? d.data.deviceType.toUpperCase() : 'unkown'
        ) > -1:
          devImg = this.devImg.IPC
          type = 'IPC'
          break
        case this.DEVTYPE.EHR.indexOf(
          d.data.deviceType ? d.data.deviceType.toUpperCase() : 'unkown'
        ) > -1:
          devImg = this.devImg.EHR
          type = 'EHR'
          break
        default:
          devImg = this.devImg.unkown
          type = 'unkown'
          break
      }
      return { devImg, type }
    },
    getPortHtml(devports) {
      if (!devports) return ''
      let html = [],
        maxLength = 0,
        misCount,
        lineIdx
      devports.forEach((item) => {
        maxLength = Math.max(item.length, maxLength)
      })
      html.push('<div class="div-ports-container">')
      html.push('<div class="dev-ports">')
      devports.forEach((linePorts, idx) => {
        misCount = maxLength - linePorts.length
        lineIdx = (idx + 1) % 2
        html.push(
          `<div class="dev-ports-line dev-ports-line-${
            lineIdx ? 'top' : 'bottom'
          }">`
        )
        linePorts.forEach((port) => {
          html.push(
            `<div class="port ${
              port ? `port-${port.showStatus || 'default'}` : ''
            }">`
          )
          if (port) {
            if (lineIdx) {
              html.push(`<span class="port-name">${port._order}</span>`)
            }
            html.push(
              `<i class="iconfont icon-${
                ICONNAME[port.iconType || 'default'][lineIdx]
              }"></i>`
            )
            if (!lineIdx) {
              html.push(`<span class="port-name">${port._order}</span>`)
            }
          }
          html.push('</div>')
        })
        for (let i = 0; i < misCount; i++) {
          html.push('<div class="port color-gray"></div>')
        }
        html.push('</div>')
      })
      html.push('</div>')
      html.push('</div>')
      return html.join('\r\n')
    },
    showDevInfo(d) {
      this.$emit('click-dev', d)
    },
    update(source) {
      this.getNodesAndLinks()
      this.nodes.forEach((d) => {
        d.y = d.depth * (this.isLevel ? 400 : 240)
      })

      const svg = d3.select('svg.d3-tree')
      const container = svg.select('g.container')
      let node = container.selectAll('g.node').data(this.nodes, (d) => {
        let _id = d._id || d.data.deviceSn || ++this.index
        d._id = _id
        return _id
      })
      // 画节点
      let nodeEnter = node
        .enter()
        .append('g')
        .attr('class', (d) => {
          return `node node-${this.scale > this.smallScale ? 'large' : 'small'}`
        })
        .attr('transform', (d) => {
          return this.isLevel
            ? `translate(${source.y0}, ${source.x0})`
            : `translate(${source.x0}, ${source.y0})`
        })
      //设备信息
      nodeEnter
        .append('foreignObject')
        .attr('class', (d) => {
          return `dev-item ${
            (d.data.showStatus && `dev-item-${d.data.showStatus}`) ||
            'dev-item-default'
          }`
        })
        .attr('height', (d) => {
          return d.data.deviceType == 'OUT' ? 120 : this.size.height
        })
        .attr('width', (d) => {
          return d.data.deviceType == 'OUT' ? 120 : this.size.width
        })
        .attr('x', (d) => {
          return d.data.deviceType == 'OUT' ? -60 : -this.size.width / 2
        })
        .attr('y', (d) => {
          return d.data.deviceType == 'OUT' ? -60 : -this.size.height / 2
        })
        .on('click', this.showDevInfo)
        .html((d) => {
          let html = '',
            type
          switch (d.data.deviceType) {
            case 'NODATA':
              html = `<div class='dev-item-info dev-nodata'><div>无拓扑数据</div></div>`
              break
            case 'OUT':
              html = `<div class='out-item-info'><img class='img-OUT' src='${this.devImg.OUT}'><i class="iconfont icon-hulianwang"></i></div>`
              break
            default:
              let devImgInfo = this.getDevImg(d)
              let devImg = devImgInfo.devImg
              type = devImgInfo.type
              html = `<div class='dev-item-info dev-${type || 'default'}'>
                        <div class='dev-img'>
                          <img class='devImg' src='${devImg}'>
                        </div>
                        <div class="dev-porductClass text-inline font-10">
                        ${
                          d.data.isWirelessLink
                            ? "<i class='dev-link-type iconfont icon-wifi mr-4'></i>"
                            : "<i class='dev-link-type iconfont icon-fontchicunjixian font-10 mr-4'></i>"
                        }
                        ${
                          d.data.productClass == 'UNKNOWN'
                            ? d.data.createOrigin == 'MANUAL' &&
                              d.data.swType == 'FOOL'
                              ? '非网管交换机'
                              : '虚拟设备'
                            : d.data.productClass
                        }</div>
                        <div class="dev-name text-inline">${
                          d.data.name || '待命名'
                        }${
                d.data.createOrigin == 'MANUAL'
                  ? "<span class='color-blue'>[手动添加]</span>"
                  : ''
              }</div>
                    </div>`
              break
          }
          return html
        })
      //面板
      nodeEnter
        .append('foreignObject')
        .attr('class', (d) => {
          for (let key in this.DEVTYPE) {
            if (this.DEVTYPE[key].indexOf(d.data.deviceType) > -1) {
              return `dev-item-ports dev-ports-${key}`
            }
          }
        })
        .attr('height', (d) => {
          return d.data.deviceType == 'OUT' ? 0 : 100
        })
        .attr('width', (d) => {
          return d.data.deviceType == 'OUT' ? 0 : 820
        })
        .attr('x', (d) => {
          return d.data.deviceType == 'OUT' ? 0 : -410
        })
        .attr('y', (d) => {
          return d.data.deviceType == 'OUT' ? 0 : -95
        })
        .attr('transform', (d) => {
          return `scale(0.2)`
        })
        .on('click', this.showDevInfo)
        .html((d) => {
          return this.getPortHtml(d.data.ports)
        })
      //是否有子设备开关
      nodeEnter
        .append('foreignObject')
        .attr('class', (d) => {
          return d.data.deviceType == 'OUT' ||
            (!(d.data.children && d.data.children.length) &&
              !(d.data._children && d.data._children.length))
            ? 'hide'
            : 'switch-children'
        })
        .attr('height', 20)
        .attr('width', 80)
        .attr('rx', 20)
        .attr('ry', 20)
        .on('click', this.clickSwitchChildren)
        .html((d) => {
          return `<div class="switch-box"><span class="switch switch-${
            d.data.showStatus || 'default'
          }">
            <span>${(d.data.children && d.data.children.length) || '0'}</span>
            <i class="iconfont ${
              this.isLevel ? 'icon-youjiantou02' : 'icon-xiajiantou'
            }"></i>
            </span></div>`
        })
      //是否是DHCP服务器
      nodeEnter
        .append('foreignObject')
        .attr('class', 'dhcp-server-box')
        .attr('width', (d) => {
          return d.data.isDhcpServer ? 90 : 0
        })
        .attr('height', (d) => {
          return d.data.isDhcpServer ? 20 : 0
        })
        .style('opacity', (d) => {
          return d.data.isDhcpServer ? 1 : 0
        })
        .attr('x', (d) => {
          return this.size.width / 2 - 45
        })
        .attr('y', (d) => {
          return d.data.isDhcpServer
            ? -this.size.height / 2 - 10
            : -this.size.height / 2
        })
        .html((d) => {
          return d.data.isDhcpServer
            ? `<div class="dhcp-server flex-row">DHCP服务器</div>`
            : ''
        })
      let nodeUpdate = nodeEnter
        .merge(node)
        .transition()
        .duration(this.duration)
        .attr('transform', (d) => {
          return this.isLevel
            ? `translate(${d.y},${d.x})`
            : `translate(${d.x},${d.y})`
        })
        .attr('class', (d) => {
          return `node node-${
            this.scale > this.smallScale ? 'large' : 'small'
          } ${this.checkSelectable(d.data) ? 'node-enableSelect' : ''}`
        })
      //更新收缩按钮位置
      nodeUpdate
        .select('foreignObject.switch-children')
        .attr('x', (d) => {
          return this.isLevel ? this.size.width / 2 - 40 : -40
        })
        .attr('y', (d) => {
          return this.isLevel ? -10 : this.size.height / 2 - 10
        })
      //更新收缩按钮位置
      nodeUpdate.select('foreignObject.dhcp-server-box')
      //更新收缩图标
      nodeUpdate
        .select('foreignObject.switch-children')
        .select('i.iconfont')
        .attr('class', (d) => {
          return `iconfont ${
            this.isLevel
              ? d.children
                ? 'icon-youjiantou02'
                : 'icon-zuojiantou02'
              : d.children
              ? 'icon-xiajiantou'
              : 'icon-shangjiantou'
          }`
        })

      //重画节点效果.
      node
        .exit()
        .transition()
        .duration(this.duration)
        .attr('transform', (d) => {
          return this.isLevel
            ? `translate(${source.y}, ${source.x})`
            : `translate(${source.x}, ${source.y})`
        })
        .remove()

      // 画连接线动画
      setTimeout(
        () => {
          let animate = container
            .selectAll('circle.link-animate')
            .data(this.links, (d) => {
              return `animate_${d._id}`
            })
          let animateE = animate
            .enter()
            .insert('circle', 'g')
            .attr('opacity', (d) => {
              return d.data.showLineAnimate === true ? 1 : 0
            })
            .attr('class', (d) => {
              return `link-animate link-animate-${
                d.data.showLineStatus || d.data.showStatus || 'default'
              }`
            })
            .attr('r', '4')
            .append('animateMotion')
            .attr('dur', '4s')
            .attr('repeatCount', 'indefinite')
            .attr('rotate', 'auto')
            .attr('calcMode', '4s')
            .attr('keyPoints', '1;0')
            .attr('keyTimes', '0;1')
            .attr('path', (d) => {
              return this.diagonal(d, d.parent)
            })
          let animateUpdate = animateE.merge(animate)
          animateUpdate
            .select('animateMotion')
            .transition()
            .duration(this.duration)
            .attr('path', (d) => {
              return this.diagonal(d, d.parent)
            })
          animate.exit().remove()
        },
        this.loaded ? 0 : this.duration
      )

      // 画连接线
      let link = container.selectAll('path.link').data(this.links, (d) => {
        return `link_${d._id}`
      })

      // Enter any new links at the parent's previous position.
      let linkEnter = link
        .enter()
        .insert('path', 'g')
        .attr('class', (d) => {
          return `link link-${
            d.data.showLineStatus || d.data.showStatus || 'default'
          }`
        })
        .attr('d', (d) => {
          let o = { x: source.x0, y: source.y0 }
          return this.diagonal(o, o)
        })
      // 更新连线.
      let linkUpdate = linkEnter.merge(link)
      linkUpdate
        .transition()
        .duration(this.duration)
        .attr('d', (d) => {
          return this.diagonal(d, d.parent)
        })

      // 连线退出效果
      link
        .exit()
        .transition()
        .duration(this.duration)
        .attr('d', (d) => {
          let o = { x: source.x, y: source.y }
          return this.diagonal(o, o)
        })
        .remove()
      // 画连线端口
      let port = container
        .selectAll('g.dev-link-port')
        .data(this.ports, (d) => {
          return `port_${d._id}`
        })
      let portEnter = port
        .enter()
        .append('g')
        .attr('class', (d) => {
          return `dev-link-port dev-link-port-${
            this.scale > this.smallScale ? 'large' : 'small'
          }`
        })
        .attr('transform', (d) => {
          return this.isLevel
            ? `translate(${source.y0}, ${source.x0})`
            : `translate(${source.x0}, ${source.y0})`
        })
      //睿易网关设备端口从0开始计算
      portEnter
        .append('foreignObject')
        .attr('class', 'uplinkport')
        .attr('height', 22)
        .attr('width', 100)
        .attr('x', (d) => {
          return this.isLevel ? -this.size.width / 2 - 50 : -50
        })
        .attr('y', (d) => {
          return this.isLevel ? -10 : -this.size.height / 2 - 25
        })
        .html((d) => {
          if (d.data.deviceType == 'NODATA') return ''
          return `<div class='link-port link-port-${
            d.data.showLineStatus || d.data.showStatus || 'default'
          }'><div class="mport">${d.data.uplinkPort || '-'}</div></div>`
        })
      portEnter
        .append('foreignObject')
        .attr('class', 'parentport')
        .style('opacity', (d) => {
          return d.parent.data.deviceType == 'OUT' ? 0 : 1
        })
        .attr('height', 22)
        .attr('width', 100)
        .attr('x', (d) => {
          return this.isLevel
            ? -(d.y - d.parent.y) / 2 - 50
            : -(d.x - d.parent.x) / 2 - 50
        })
        .attr('y', (d) => {
          return this.isLevel
            ? -(d.x - d.parent.x) / 2 - 10
            : -(d.y - d.parent.y) / 2 - 10
        })
        .html((d) => {
          if (d.data.deviceType == 'NODATA') return ''
          return `<div class='link-port link-port-${
            d.data.showLineStatus || d.data.showStatus || 'default'
          }'><div class="pport">${d.data.parentPort || '-'}</div></div>`
        })
      let portUpdate = portEnter
        .merge(port)
        .attr('class', (d) => {
          return `dev-link-port dev-link-port-${
            this.scale > this.smallScale ? 'large' : 'small'
          }`
        })
        .style('opacity', (d) => {
          return d.data.isWirelessLink === true ? 0 : 1
        })
        .transition()
        .duration(this.duration)
        .attr('transform', (d) => {
          return this.isLevel
            ? `translate(${d.y},${d.x})`
            : `translate(${d.x},${d.y})`
        })
      portUpdate
        .select('foreignObject.uplinkport')
        .attr('x', (d) => {
          return this.isLevel ? -this.size.width / 2 - 50 : -50
        })
        .attr('y', (d) => {
          return this.isLevel ? -10 : -this.size.height / 2 - 25
        })
      portUpdate
        .select('foreignObject.parentport')
        .attr('x', (d) => {
          return this.isLevel
            ? -(d.y - d.parent.y) / 2 - 50
            : -(d.x - d.parent.x) / 2 - 50
        })
        .attr('y', (d) => {
          return this.isLevel
            ? -(d.x - d.parent.x) / 2 - 10
            : -(d.y - d.parent.y) / 2 - 10
        })
      //收缩是回归至父节点.
      port.exit().remove()
      this.nodes.forEach((d) => {
        d.x0 = d.x
        d.y0 = d.y
      })
    },
    fixPositon(params) {
      this.gPosition = document.getElementById('container').getBBox()
      let position = this.gPosition
      let _scale2 = (this.width / position.width) * 0.8
      let _scale1 = (this.height / position.height) * 0.8
      let _scale = (params && params.scale) || Math.min(_scale1, _scale2, 0.55)
      this.scale = _scale
      this.oldScale = _scale
      let translate = (params && params.translate) || {
        x: this.isLevel
          ? (this.width - position.width * _scale) / 2 +
            (this.size.width / 2) * _scale
          : this.width / 2 - ((position.x * 2 + position.width) / 2) * _scale,
        y: this.isLevel
          ? this.height / 2 - ((position.y * 2 + position.height) / 2) * _scale
          : (this.height - position.height * _scale) / 2 +
            (this.size.height / 2) * _scale,
      }
      const svg = d3.select('svg.d3-tree')
      const container = svg.select('g.container')
      const transform = d3.zoomIdentity
        .translate(translate.x, translate.y)
        .scale(_scale)
      container
        .transition()
        .duration(this.duration)
        .call(this.zoom.transform, transform)
      svg.call(this.zoom.transform, transform)
    },
    rotate() {
      this.isLevel = !this.isLevel
      this.update(this.root)
      setTimeout(() => {
        this.fixPositon()
        //规避无动画时没触发重画大小样式
        this.duration < 50 && this.update(this.root)
      }, this.duration + 50)
    },
    returned() {
      this.isLevel = false
      this.fixPositon()
      this.update(this.root)
      setTimeout(() => {
        this.fixPositon()
        //规避无动画时没触发重画大小样式
        this.duration < 50 && this.update(this.root)
      }, this.duration + 50)
    },
  },
  computed: {
    treemap() {
      return d3
        .tree()
        .size([this.height, this.width])
        .nodeSize(
          this.isLevel
            ? [this.size.height, this.size.width]
            : [this.size.width, this.size.height]
        )
        .separation(function (a, b) {
          return a.parent == b.parent ? 1.4 : 1.6
        })
    },
  },
  watch: {},
}
</script>
<style lang="stylus">
@import './topo.styl';
</style>
