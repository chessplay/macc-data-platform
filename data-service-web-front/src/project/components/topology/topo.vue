<template>
  <div class="topo-container">
    <div :id="elemId" class="topo" v-show="checkRenderData()"></div>
    <div class="tools-container" v-show="checkRenderData()">
      <div class="tool-group">
        <div
          class="tool-btn"
          @click="saveTopoImg"
          v-show="configOptions.saveImg"
        >
          <a-icon type="picture" />
        </div>
      </div>
      <div class="tool-group">
        <div class="tool-btn" @click="zoomOut"><a-icon type="plus" /></div>
        <div class="tool-btn" @click="zoomIn"><a-icon type="minus" /></div>
      </div>
      <div class="tool-group">
        <div class="tool-btn" @click="rotate">
          <a-icon type="reload" />
        </div>
      </div>
    </div>
    <div v-show="!checkRenderData()">暂无拓扑数据</div>
    <div class="topo" style="display: none" ref="savePngDom"></div>
  </div>
</template>
<script>
import * as d3 from "d3";
import topoConf from "./topo-mixin";
import { topoTools } from "./tools/index";
import saveToPng from "save-svg-as-png";
export default {
  name: "topo",
  props: ["elemId", "topoData", "width", "height", "option"],
  mixins: [topoConf],
  data() {
    return {
      panelLayoutMap: {},
      terminalTypeMap: {},
      hasTopoData: false,
    };
  },
  computed: {
    renderData() {
      let renderData = Object.assign({}, this.topoData);
      return renderData;
    },

    configOptions() {
      let configOptions = Object.assign({ saveImg: true }, this.option);
      console.log("================更新拓扑配置项目", configOptions);
      //this.updataOptions(configOptions);
      return configOptions;
    },
  },
  watch: {
    renderData() {
      this.$nextTick(() => {
        this.initTopo();
      });
    },
  },
  methods: {
    /****
     * 更新配置项
     */
    // updataOptions(options) {
    //   if (options.modal) {
    //     this.modal = options.modal;
    //   }

    //   if (options.terminalTypeMap) {
    //     this.terminalTypeMap = options.terminalTypeMap;
    //   }
    // },
    /****
     * 保存图片方法
     */
    saveTopoImg() {
      const canvas = this.getCanvas();
      const groupMap = this.$store.state.Group.groupMap;
      const groupId = this.$store.state.Group.groupId;
      const pngName = (groupMap[groupId] && groupMap[groupId].name) || "拓扑图";
      saveToPng.saveSvgAsPng(canvas, pngName + ".png", {
        backgroundColor: "#efefef",
        excludeUnusedCss: true,
      });
      // saveToPng.svgAsPngUri(canvas, { excludeUnusedCss: true }).then((uri) => {
      //   this.$refs.img.src = uri;
      // });
    },
    /****
     * 获取拓扑生成图片URI
     */
    getPngUri(options = {}, cb = "") {
      const canvas = this.getCanvas();
      return saveToPng.svgAsPngUri(
        canvas,
        Object.assign({ excludeUnusedCss: true }, options),
        cb
      );
    },
    /****
     * 获取图片生成图片用svg
     */
    getCanvas() {
      const ghtml = this.BASE.svg.node();
      const gbbox = this.BASE.g.node().getBBox();
      const nWidth = gbbox.width + 100;
      const nHeight = gbbox.height + 200;
      this.$refs.savePngDom.innerHTML = `<svg width="${nWidth}" height="${nHeight}" class="topo">${ghtml.innerHTML}</svg`;

      const canvas = this.$refs.savePngDom.querySelector("svg");

      //遍历获取最小节点x偏移
      let nodeTransformYList = this.$refs.savePngDom.querySelectorAll(".node");
      let tyArry = [];
      nodeTransformYList.forEach((item) => {
        console.log(item);
        let transform = item.getAttribute("transform");
        let reg = transform.match(/translate\((.*?)\)/);
        let res = reg && reg[1].split(",");
        if (this.BASE.horizontal) {
          tyArry.push(res[1]);
        } else {
          tyArry.push(res[0]);
        }
      });

      let min = Math.min.apply({}, tyArry);
      let outelemHeight = 0;
      //判断是否只有一行，OUT节点高度比普通节点高，进行偏移兼容
      if (parseFloat(min) == -15) {
        outelemHeight = 50;
      }

      let ntransform;
      //将最小Y节点置于图片顶部
      if (this.BASE.horizontal) {
        let translateY = outelemHeight - min + 50;
        ntransform = "translate(0," + translateY + ") scale(1)";
      } else {
        let translateX = outelemHeight - min + 50;
        ntransform = "translate(" + translateX + ",0) scale(1)";
      }

      canvas.lastChild.setAttribute("transform", ntransform);
      return canvas;
    },
    /****
     * 检测是否有拓扑数据
     */
    checkRenderData() {
      return Object.keys(this.renderData).length > 0;
    },
    /****
     * 获取store中不存的面板布局并更新到store
     * @param {*} renderData  拓扑数据
     */
    updataLayoutPanel(renderData) {
      const self = this;
      let networkPanelClass = [],
        sns = [];
      this.getNetworkDeviceClass(renderData, networkPanelClass, sns);
      this.$emit("topologyPanelDeviceSns", sns);
      if (networkPanelClass.length > 0) {
        this.$store
          .dispatch("devices/updataDeviceLayout", networkPanelClass)
          .then(() => {
            self.initTopoData(self.renderData);
          });
      } else {
        self.initTopoData(self.renderData);
      }
    },
    /****
     * 递归统计拓扑中不在store中存在的面板布局的设备型号
     * @param {*} renderData  拓扑数据
     * @param {*} networkPanelClass  需要重新获取端口面板布局的设备型号列表
     */
    getNetworkDeviceClass(renderData, networkPanelClass, sns) {
      let deviceType = renderData.deviceType;
      let productClass = renderData.productClass || "";
      console.log(
        "端口布局",
        this.$store.state.devices.deviceLayout[productClass]
      );
      if (
        ["SWITCH", "GATEWAY", "WR"].indexOf(topoTools.DEVICETYPE[deviceType]) >
        -1
      ) {
        if (
          networkPanelClass.indexOf(productClass) == -1 &&
          this.$store.state.devices.deviceLayout[productClass] == undefined
        ) {
          networkPanelClass.push(productClass);
        }
        sns.push(renderData.deviceSn);
      }

      if (renderData.children || renderData._children) {
        let children = renderData.children || renderData._children;
        for (let i = 0; i < children.length; i++) {
          this.getNetworkDeviceClass(children[i], networkPanelClass, sns);
        }
      }
    },
    /****
     * 生成line path
     * @param {*} d  NODE数据
     */
    elbow(d) {
      if (this.BASE.horizontal) {
        //return "M" + d.source.y + "," + d.source.x + "H" +(d.source.y+30)+ "V" + d.target.x + "H" + d.target.y;
        let radiusStart =
          d.target.x > d.source.x
            ? d.target.x - this.config.elbowRadius
            : d.target.x < d.source.x
            ? d.target.x + this.config.elbowRadius
            : d.target.x;
        return `M${d.source.y},${d.source.x}H${d.source.y +
          this.config.elbowfar}V${radiusStart}Q${d.source.y +
          this.config.elbowfar +
          " " +
          d.target.x},${d.source.y +
          this.config.elbowfar +
          this.config.elbowRadius +
          " " +
          d.target.x}H${d.target.y}`;
      } else {
        //return "M" + d.source.x + "," + d.source.y + "V" + (d.source.y+30) + "H" + d.target.x + "V" + d.target.y;
        //return `M${d.source.x},${d.source.y}V${d.source.y+30}H${d.target.x-elbowRadius}Q${(d.target.x-elbowRadius)+" "+d.source.y+30},${d.target.x+" "+(d.source.y+30+elbowRadius)}V${d.target.y}`
        let radiusStart =
          d.target.x > d.source.x
            ? d.target.x - this.config.elbowRadius
            : d.target.x < d.source.x
            ? d.target.x + this.config.elbowRadius
            : d.target.x;
        return `M${d.source.x},${d.source.y}V${d.source.y +
          this.config.elbowfar}H${radiusStart}Q${d.target.x +
          " " +
          (d.source.y + this.config.elbowfar)},${d.target.x +
          " " +
          (d.source.y + this.config.elbowfar + this.config.elbowRadius)}V${
          d.target.y
        }`;
      }
    },
    /****
     * 获取设备背板数据
     * @param {*} d  NODE数据
     */
    getImage(d) {
      let self = this;
      let onlineStatusFlag = d.data.onlineStatus != "ON" ? "_OFF" : "";
      let imgMap = topoTools.getImgMap();
      if (d.data.deviceType == "OUT") {
        return imgMap[`network`];
      } else if (d.data.deviceType == "AP_GROUP") {
        return imgMap[`APGROUP${onlineStatusFlag}`];
      } else if (d.data.deviceType == "IPC_GROUP") {
        return imgMap[`IPCGROUP${onlineStatusFlag}`];
      } else {
        let commonType = topoTools.DEVICETYPE[d.data.deviceType];
        if (
          commonType == "SWITCH" &&
          self.$store.state.devices.multiPanelClass.indexOf(
            d.data.productClass
          ) > -1
        ) {
          return imgMap[`mutilswitch${onlineStatusFlag}`];
        }
        let nodeInfo = {
          commonType: commonType,
          deviceType: d.data.deviceType,
          productClass: d.data.productClass,
        };
        let deviceImg = topoTools.getDevicePanelBgImg(
          nodeInfo,
          self.configOptions.terminalTypeMap,
          onlineStatusFlag
        );
        //deviceImg = `${self.config.IMGBASEPATH}${deviceImg}`;
        // console.log(deviceImg)
        return imgMap[deviceImg];
      }
    },
    /****
     * 创建端口面板
     * @param {*} portsLayout  端口布局数据
     * @param {*} commonType  设备类型
     * @param {*} d  NODE数据
     */
    creatPortPanel(portsLayout, commonType, d) {
      const self = this;
      let portString = "",
        portColor = "";
      let ty = 0;
      const portsDataMap = d.data.portsData;
      for (let i = 0; i < portsLayout.length; i++) {
        let tx = 0;
        for (let j = 0; j < portsLayout[i].length; j++) {
          let portItemName = portsLayout[i][j];
          if (portItemName) {
            if (commonType == "GATEWAY" || commonType == "WR") {
              portItemName = portsLayout[i][j].port;
            }
            let order = portItemName.match(/\d+/g)[0];

            if (/F$/g.test(portItemName)) {
              if (portsDataMap[order]) {
                if (portsDataMap[order].mediumType == "Fiber") {
                  portColor = portsDataMap[order].portClass.join(" ");
                } else {
                  portColor = portsDataMap[order].exceptionColor;
                }
              }
              //判断光电口
              portString += `<path d='${
                self.config.port.f
              }' transform="translate(${tx},${ty})${
                i / 2 != 0 ? "rotate(180,10,10)" : ""
              }" class="port ${portColor}" data-order="${order}" data-mediumType="Fiber"></path>`;
            } else {
              if (portsDataMap[order]) {
                if (portsDataMap[order].mediumType == "Copper") {
                  portColor = portsDataMap[order].portClass.join(" ");
                } else {
                  portColor = portsDataMap[order].exceptionColor;
                }
              }
              portString += `<path d='${
                self.config.port.d
              }' transform="translate(${tx},${ty})${
                i / 2 != 0 ? "rotate(180,10,10)" : ""
              }" class="port ${portColor}" data-order="${order}" data-mediumType="Copper"></path>`;
            }
            tx += self.config.port.width + self.config.port.depath;
          } else {
            tx += self.config.port.width / 2 + self.config.port.depath;
          }
        }
        ty += self.config.port.height;
      }
      return portString;
    },
    /****
     * 获取端口文件
     * @param {*} d  node对象
     * @param {*} port  端口
     */
    getPortText(d, port) {
      var borderColor = "";
      var bgColor = "#fff";
      //if (d.source.name == 'OUT' && d.target.onlineStatus == 'ON') {
      borderColor = this.getLineStroke(d);
      // } else if (d.source.onlineStatus == 'ON' && d.target.onlineStatus == 'ON') {
      //     borderColor = TopoConf.deviceStatus["ON"];
      // } else {
      //     borderColor = TopoConf.deviceStatus["OFF"];
      // }
      if (!port) {
        bgColor = "transparent";
        borderColor = "transparent";
      }
      return `<span style="background-color:${bgColor};border-color:${borderColor};color:#000;max-width: 70px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" title="${port ||
        ""}">${port || ""}</span>`;
    },
    /****
     * 初始化树数据
     * @param {fun} data  拓扑数据
     */
    initTree(data) {
      var self = this;
      d3
        .tree()
        .separation(function(a, b) {
          //console.log("间隔访问",a,b)
          return a.parent == self.root && b.parent == self.root ? 2 : 1;
          //return a.parent == b.parent ? 1 : 2
        })
        .nodeSize(
          (function() {
            if (self.BASE.horizontal) {
              return [self.config.rectH + 10, self.config.rectW + 10];
            } else {
              return [self.config.rectW + 10, self.config.rectH + 10];
            }
          })()
        )(data);
    },
    /****
     * 初始化拓扑数据
     * @param {*} renderData  拓扑数据
     */
    initTopoData(renderData) {
      let self = this;
      self.root = d3.hierarchy(renderData);
      //self.root = json.data.data;
      console.log("拓扑数据", self.root);
      if (self.BASE.horizontal) {
        self.root.x0 = self.height / 2;
      } else {
        self.root.x0 = self.width / 2;
      }
      self.root.y0 = 0;
      self.updata(self.root);
    },
    /****
     * 更新拓扑
     * @param {*} source  Node数据
     * @param {*} noAutoZoom  是否自动缩放标记
     * @param {*} selectDevice  已选中设备
     */
    updata(source, noAutoZoom, selectDevice) {
      let self = this;

      if (!noAutoZoom) {
        self.renderPortsDevice = [];
      }

      let nodes = self.root.descendants().reverse();
      let links = self.root.links();

      self.initTree(self.root);
      // Compute the new tree layout.

      //console.log("links",links)
      // Normalize for fixed-depth.
      nodes.forEach(function(d) {
        if (self.BASE.horizontal) {
          d.y = d.depth * (self.nodeDepth * 1.7 + self.config.rectW);
        } else {
          d.y = d.depth * (self.nodeDepth + self.config.rectH);
        }

        let children = d.children || d._children;
        if (children) {
          d.childrenTotal = children.length;
        } else {
          d.childrenTotal = 0;
        }

        //d.childrenTotal = totalChlidDevices(d)-1;
        //console.log("nodeCount",d)
      });

      // Update the nodes¡­
      let node = self.BASE.g.selectAll("a.node").data(nodes, function(d) {
        d.nodeId = d.nodeId != undefined ? d.nodeId : d.id;
        return (d.id = ++self.BASE.nodeIndex);
      });

      // Enter any new nodes at the parent's previous position.
      let nodeEnter = node
        .enter()
        .append("svg:a")
        .attr("class", "node")
        .classed("apgroup", (d) => d.data.deviceType == "AP_GROUP")
        .classed("ipcgroup", (d) => d.data.deviceType == "IPC_GROUP")
        .classed("selectednode", function(d) {
          if (selectDevice) {
            return selectDevice.indexOf(d.data.deviceSn) > -1;
          } else {
            return false;
          }
        })
        .attr("id", (d) => `node_${d.data.deviceSn.replace(/\./g, "")}`)
        .attr("xlink:title", function(d) {
          // if(self.BASE.showNodeName){
          //     return "点击查看设备详情"
          // }else{
          let devicesName = d.data.name ? d.data.name : d.data.deviceSn;
          let productClass = d.data.productClass
            ? "型号：" +
              (d.data.productClass == "UNKNOWN"
                ? "未知"
                : d.data.productClass) +
              "\n"
            : "";
          return "设备：" + devicesName + "\n" + productClass;
          // }
        })
        .attr("transform", function() {
          if (self.BASE.horizontal) {
            return "translate(" + source.y0 + "," + source.x0 + ")";
          } else {
            return "translate(" + source.x0 + "," + source.y0 + ")";
          }
        });

      //添加外框
      nodeEnter
        .append("rect")
        .attr("width", function(d) {
          if (
            d.data.deviceType == "IPC_GROUP" ||
            d.data.deviceType == "AP_GROUP"
          ) {
            d3.select(this).attr(
              "transform",
              `translate(${(self.config.rectW - self.config.rectH) / 2},0)`
            );
            return self.config.rectH;
          }
          return self.config.rectW;
        })
        .attr("height", self.config.rectH)
        .attr("rx", 4)
        .attr("ry", 4)
        .classed("devices-root", (d) => d.data.deviceType == "OUT")
        .classed("devices-box", (d) => d.data.deviceType != "OUT")
        .classed("on", (d) => d.data.onlineStatus == "ON")
        .classed(
          "isvirtualdev",
          (d) =>
            self.configOptions.modal == "markUp" &&
            d.data.isVirtualDev &&
            (!d.data.deviceType || d.data.deviceType == "UNKNOWN")
        )
        .on("click", function(e, d) {
          console.log("点击参数", arguments, d);

          //非补全模式虚拟节点不许点击
          // if(self.option.modal != "markUp" && d.isVirtualDev == true){
          //     return ;
          // }
          debugger;

          if (
            self.configOptions.modal == "devices" &&
            typeof self.configOptions.filterClickCallback == "function" &&
            self.configOptions.filterClickCallback(
              d3.select(this.parentNode),
              d
            )
          ) {
            return;
          }

          //过滤不允许点击设备
          if (
            self.configOptions.filterClass &&
            self.configOptions.filterClass.indexOf(d.data.deviceType) > -1
          ) {
            return;
          }

          //点击摄像头或者AP分组时处理
          if (
            d.data.deviceType == "IPC_GROUP" ||
            d.data.deviceType == "AP_GROUP"
          ) {
            let sns = self.getTreeSelectNodeSns();
            self.groupExpansion(d, sns);
            return;
          }

          if (d.data.deviceType == "OUT") {
            return;
          }

          // if(d3.select(this.parentNode).classed("selectednode")){
          //     d3.select(this.parentNode).classed("selectednode",false);
          // }else{
          if (self.configOptions.multipleSelect) {
            d3.select(this.parentNode).classed(
              "selectednode",
              !d3.select(this.parentNode).classed("selectednode")
            );
          } else {
            self.BASE.g.selectAll(".node").classed("selectednode", false);
            d3.select(this.parentNode).classed("selectednode", true);
          }

          //}
          //   self.option &&
          //     typeof self.option.panelCallback == 'function' &&
          //     self.option.panelCallback(d, d3.select(this.parentNode));
        })
        .on("mouseover", function() {
          console.log("over");
          //   self.option &&
          //     typeof self.option.overCallback == 'function' &&
          //     self.option.overCallback(d, d3.select(this.parentNode));
        })
        .on("mouseout", function() {
          console.log("out");
          //   self.option &&
          //     typeof self.option.outCallback == 'function' &&
          //     self.option.outCallback(d, d3.select(this.parentNode));
        });

      //添加选中标志
      nodeEnter
        .append("path")
        .classed("nodeSelectFlag", true)
        .attr("d", function(d) {
          if (d.data.deviceType == "OUT") {
            return "";
          } else {
            return "m2.83933,30.08963l27.32138,-27.33728l0,27.33728l-27.32138,0zm23.7519,-12.52012c-0.46845,-0.48069 -1.23453,-0.48069 -1.703,0l-5.39941,5.52258l-2.55446,-2.61551c-0.47092,-0.48305 -1.23702,-0.48305 -1.70547,0c-0.47089,0.47828 -0.47089,1.25902 0,1.74208l3.40847,3.48892c0.47089,0.48316 1.23453,0.48316 1.70544,0l6.24843,-6.39229c0.47094,-0.48316 0.47094,-1.2639 0,-1.74577l0,0l0,0l0,0z";
          }
        })
        .attr("transform", function(d) {
          if (["AP_GROUP", "IPC_GROUP"].indexOf(d.data.deviceType) > -1) {
            return (
              "translate(" +
              (self.config.rectW -
                (self.config.rectW - self.config.rectH) / 2 -
                30 -
                1) +
              "," +
              (self.config.rectH - 30 - 1) +
              ")"
            );
          } else {
            return (
              "translate(" +
              (self.config.rectW - 30 - 1) +
              "," +
              (self.config.rectH - 30 - 1) +
              ")"
            );
          }
        });

      //添加背板图片
      nodeEnter
        .append("svg:image")
        //.attr("width", rectW)
        .attr("height", self.config.rectH * 0.35)
        .classed("panelimg", true)
        .style("pointer-events", "none")
        .attr("transform", function(d) {
          let left = self.config.rectW * 0.05;
          let top = self.config.rectH * 0.12;
          if (
            topoTools.DEVICETYPE[d.data.deviceType] == "AC" ||
            d.data.deviceType == "nvr"
          ) {
            return "scale(1)translate(" + left + "," + top + ")";
          } else if (d.data.deviceType == "OUT") {
            if (self.BASE.horizontal) {
              return (
                "scale(2)translate(" +
                self.config.rectW * 0.3 +
                "," +
                self.config.rectH * 0.1 +
                ")"
              );
            } else {
              return (
                "scale(2)translate(" +
                self.config.rectW * 0.15 +
                "," +
                self.config.rectH * 0.3 +
                ")"
              );
            }
          } else if (
            ["ipc", "IPC_GROUP", "AP_GROUP"].indexOf(d.data.deviceType) > -1
          ) {
            return (
              "scale(1.3)translate(" +
              self.config.rectW * 0.25 +
              "," +
              self.config.rectH * 0.1 +
              ")"
            );
          } else if (topoTools.DEVICETYPE[d.data.deviceType] == "AP") {
            return (
              "scale(1.3)translate(" +
              self.config.rectW * 0.3 +
              "," +
              self.config.rectH * 0.1 +
              ")"
            );
            // }else if(d.deviceType == "EAP"){
            //     return "translate("+left+","+top+")"
          } else if (self.configOptions.terminalTypeMap[d.data.deviceType]) {
            return (
              "scale(1.2)translate(" +
              self.config.rectW * 0.3 +
              "," +
              self.config.rectH * 0.1 +
              ")"
            );
          } else if (
            topoTools.DEVICETYPE[d.data.deviceType] == "SWITCH" &&
            self.$store.state.devices.multiPanelClass &&
            self.$store.state.devices.multiPanelClass.indexOf(
              d.data.productClass
            ) > -1
          ) {
            return (
              "scale(1.5)translate(" +
              (self.config.rectW / 5) * 0.65 +
              "," +
              3 +
              ")"
            );
          } else if (
            ["SWITCH", "GATEWAY", "WR"].indexOf(
              topoTools.DEVICETYPE[d.data.deviceType]
            ) > -1
          ) {
            return "scale(1)translate(" + left + "," + top + ")";
          } else {
            return "scale(1)translate(" + left + "," + top + ")";
          }
        })
        .attr("xlink:href", function(d) {
          let img = self.getImage(d);
          return img;
        });

      nodeEnter
        .append("svg:g")
        .attr("width", self.config.rectW)
        .attr("height", self.config.rectH)
        .classed("panelport", true)
        .style("pointer-events", "none")
        .html(function(d) {
          let commonType = topoTools.DEVICETYPE[d.data.deviceType];
          let onlineStatusFlag = d.data.onlineStatus != "ON" ? true : false;
          if (["GATEWAY", "SWITCH", "WR"].indexOf(commonType) > -1) {
            let left = 0,
              top = self.config.rectH * 0.5 + 50;
            if (
              // (ports == 0 && d.data.productClass != 'NBS7003') ||
              // (true && onlineStatusFlag && d.data.productClass != 'NBS7003')
              onlineStatusFlag ||
              (!self.$store.state.devices.deviceLayout[d.data.productClass] &&
                self.$store.state.devices.multiPanelClass.indexOf(
                  d.data.productClass
                ) == -1)
            ) {
              let commTypeObj = {
                GATEWAY: "网关",
                SWITCH: "交换机",
                WR: "无线路由器",
              };
              d3.select(this).attr(
                "transform",
                "translate(" + self.config.rectW / 2 + ",40)"
              );
              return `<text style="text-anchor:middle;${
                onlineStatusFlag
                  ? "fill:" + self.config.deviceStatus["OFF"]
                  : ""
              }">${onlineStatusFlag ? "离线-" : ""}${
                commTypeObj[commonType]
              }</text>`;
            }

            d3.select(this).attr(
              "transform",
              "scale(1)translate(" + left + "," + top + ")"
            );
            d3.select(this).classed("ports", true);

            // if (
            //   !noAutoZoom &&
            //   window.NETWORKDEVICEPANELLAYOUT[d.data.productClass]
            // ) {
            //   self.renderPortsDevice.push(d.data.deviceSn);
            // }

            return self.creatPortPanel(
              self.$store.state.devices.deviceLayout[d.data.productClass] || [],
              commonType,
              d
            );
          } else if (
            ["AC"].indexOf(commonType) > -1 ||
            d.data.deviceType == "nvr"
          ) {
            d3.select(this).attr(
              "transform",
              "translate(" + self.config.rectW / 2 + ",40)"
            );
            if (d.data.deviceType == "nvr") {
              return `<text style="text-anchor:middle;${
                !onlineStatusFlag
                  ? ""
                  : "fill:" + self.config.deviceStatus["OFF"]
              }">${!onlineStatusFlag ? "" : "离线-"}NVR</text>`;
            } else {
              return `<text style="text-anchor:middle;${
                !onlineStatusFlag
                  ? ""
                  : "fill:" + self.config.deviceStatus["OFF"]
              }">${!onlineStatusFlag ? "" : "离线-"}AC</text>`;
            }
          } else {
            return "";
          }
        });

      //添加设备名称
      nodeEnter
        .append("svg:foreignObject")
        .attr("width", self.config.rectW)
        .attr("height", self.config.rectH * 0.25)
        .classed("device-name", true)
        .attr("transform", function() {
          let top = self.config.rectH * 0.58;
          return "translate(0," + top + ")";
        })
        .html(function(d) {
          if (d.data.deviceType != "OUT") {
            if (
              d.data.deviceType == "AP_GROUP" ||
              d.data.deviceType == "IPC_GROUP"
            ) {
              d3.select(this).attr(
                "transform",
                `translate(0,${self.config.rectH * 0.62})`
              );
            }

            if (d.data.isVirtualDev) {
              return `<div style="text-align: center;font-size: 12px;color: #333;width: 120px;margin: 0px auto;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;font-weight:500"><span>${
                d.data.name
                  ? d.data.name == "虚拟设备"
                    ? "待命名"
                    : d.data.name
                  : "待命名"
              }</span></div>`;
            } else {
              return `<div style="text-align: center;font-size: 12px;color: #333;width: 120px;margin: 0px auto;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;font-weight:500"><span>${
                d.data.name &&
                d.data.name != "Ruijie" &&
                d.data.name != "ruijie"
                  ? d.data.name
                  : d.data.productClass
                  ? d.data.productClass
                  : d.data.deviceSn
              }</span></div>`;
            }
          }
        });

      nodeEnter
        .append("svg:foreignObject")
        .attr("width", self.config.rectW)
        .attr("height", self.config.rectH * 0.25)
        .classed("device-name", true)
        .attr("transform", function() {
          let top = self.config.rectH * 0.75;
          return "translate(0," + top + ")";
        })
        .html(function(d) {
          if (
            ["AP_GROUP", "IPC_GROUP", "ipc", "OUT"].indexOf(d.data.deviceType) >
            -1
          ) {
            return ``;
          } else {
            return `<div style="text-align: center;font-size: 12px;color: #8c8c8c;width: 150px;margin: 0px auto;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;"><span>${
              d.data.isOldVirtualDev
                ? ""
                : !d.data.isVirtualDev
                ? "SN:" + d.data.deviceSn
                : d.data.swType == "FOOL"
                ? d.data.productClass == "UNKNOWN"
                  ? "非网管交换机"
                  : d.data.productClass
                : d.data.productClass == "UNKNOWN"
                ? ""
                : d.data.productClass
            }</span><span style="color:#2b6afd">${
              d.data.isVirtualDev && d.data.createOrigin == "MANUAL"
                ? "[手动添加]"
                : ""
            }</span></div>`;
          }
        });

      //添加IPC或者AP分组其下设备数量
      nodeEnter
        .append("svg:foreignObject")
        .attr("width", self.config.groupTotalbox.with)
        .attr("height", self.config.groupTotalbox.height)
        .classed("nodeChildrenTotal", true)
        .attr("transform", function() {
          //return `translate(${rectW-groupTotalbox.with/2*groupTotalbox.scale},-${groupTotalbox.height/2*groupTotalbox.scale})scale(${groupTotalbox.scale})`
          return `translate(${self.config.rectW -
            (self.config.groupTotalbox.with / 2) *
              self.config.groupTotalbox.scale -
            (self.config.rectW - self.config.rectH) /
              2},-${(self.config.groupTotalbox.height / 2) * self.config.groupTotalbox.scale})scale(${self.config.groupTotalbox.scale})`;
        })
        .html(function(d) {
          if (d.data.deviceType != "OUT") {
            if (
              d.data.deviceType == "AP_GROUP" ||
              d.data.deviceType == "IPC_GROUP"
            ) {
              if (d.data._children && d.data._children.length) {
                let offlineDeviceTotal = self.getOffLineDeviceTotal(
                  d.data._children
                );
                return `<div style="width:${
                  self.config.groupTotalbox.with
                }px;height:${
                  self.config.groupTotalbox.height
                }px;display:flex;justify-content: center;align-items: center; background-color: #fff;border-radius:15px;border:1px solid ${
                  self.config.deviceStatus["ON"]
                };color:#333;font-size:17px">${d.data._children.length -
                  offlineDeviceTotal}/${d.data._children.length}</div>`;
              } else {
                return ``;
              }
            } else {
              return ``;
            }
          }
        });

      let nodeUpdate = node.merge(nodeEnter).attr("transform", function(d) {
        if (self.BASE.horizontal) {
          return "translate(" + d.y + "," + d.x + ")";
        } else {
          return "translate(" + d.x + "," + d.y + ")";
        }
      });

      //更新设备端口位置居中
      nodeUpdate.selectAll(`.panelport.ports`).attr("transform", function() {
        let transform = d3.select(this).attr("transform");
        if (transform.indexOf("scale") > -1) {
          let scale = 0.3;
          let portsbbox = d3
            .select(this)
            .node()
            .getBBox();
          return `scale(${scale})translate(${self.config.rectW / 2 / scale -
            portsbbox.width / 2},${35 / scale - portsbbox.height / 2})`;
        } else {
          return transform;
        }
      });

      //过滤出需要打包得节点
      let nodeHasGroup = nodeUpdate.filter(function(d) {
        if (d.data.children) {
          return (
            topoTools.checkGroupDevice(d.data.children, "AP") ||
            topoTools.checkGroupDevice(d.data.children, "ipc")
          );
        } else {
          return false;
        }
      });

      //将之前得节点得开关删除
      node.selectAll(".closeChildren").remove();
      node.selectAll(".treeExp").remove();

      //添加终端打包开关
      nodeHasGroup
        .append("rect")
        .classed("closeChildren", true)
        .style("fill", self.config.deviceStatus["ON"])
        .attr("width", self.BASE.collapseboxW)
        .attr("height", self.BASE.collapseboxH)
        .attr("rx", 5)
        .attr("ry", 5)
        .attr("transform", () => {
          if (self.BASE.horizontal) {
            return `translate(${self.config.rectW -
              self.BASE.collapseboxW / 2},${self.config.rectH / 2 -
              self.BASE.collapseboxH / 2})`;
          } else {
            return `translate(${self.config.rectW / 2 -
              self.BASE.collapseboxW / 2},${self.config.rectH -
              self.BASE.collapseboxH / 2})`;
          }
        })
        .on("click", function(e, d) {
          topoTools.pickDevice(d, "AP");
          topoTools.pickDevice(d, "ipc");
          let sns = self.getTreeSelectNodeSns();
          self.updata(d, true, sns);
        });

      nodeHasGroup
        .append("path")
        .attr("class", "treeExp")
        .attr("d", self.config.collapseArr)
        .attr("fill", "#fff")
        .style("pointer-events", "none")
        .attr("transform", () => {
          if (self.BASE.horizontal) {
            let rotate = "rotate(360,5,5)";
            return (
              "translate(" +
              (self.config.rectW - 3 * 2) +
              "," +
              (self.config.rectH / 2 - self.BASE.collapseboxH / 2) +
              ")" +
              rotate +
              "scale(1.5)"
            );
          } else {
            let rotate = "rotate(90,5,5)";
            return (
              "translate(" +
              (self.config.rectW / 2 - 3) +
              "," +
              (self.config.rectH - self.BASE.collapseboxH / 2) +
              ")" +
              rotate +
              "scale(1.5)"
            );
          }
        });

      // Transition exiting nodes to the parent's new position.
      let nodeExit = node
        .exit()
        .attr("transform", () => {
          if (self.BASE.horizontal) {
            return "translate(" + source.y + "," + source.x + ")";
          } else {
            return "translate(" + source.x + "," + source.y + ")";
          }
        })
        .remove();

      nodeExit
        .select("rect")
        .attr("width", 0)
        .attr("height", 0);
      nodeExit.select("text").style("fill-opacity", 0);
      nodeExit.select("path").style("fill-opacity", 0);

      // Update the links

      let link = self.BASE.g.selectAll("g.link").data(links, function(d) {
        return d.target.id;
      });

      // Enter any new links at the parent's previous position.

      let linkEnter = link
        .enter()
        .insert("g", ":first-child")
        .attr("class", function(d) {
          return (
            "link ls_" +
            d.source.data.deviceSn.replace(/\./g, "") +
            " ls_" +
            d.target.data.deviceSn.replace(/\./g, "")
          );
        });

      linkEnter
        .append("path")
        .attr("fill", "none")
        .attr("stroke", (d) => self.getLineStroke(d))
        .attr("stroke-width", "2")
        .attr("stroke-dasharray", "")
        //.attr("d",self.elbow);
        .attr("d", () => {
          let o = {
            x: source.x0,
            y: source.y0,
          };
          //console.log(source);
          return self.elbow({
            source: o,
            target: o,
          });
        });

      linkEnter
        .append("svg:foreignObject")
        .attr("class", function(d) {
          return "parentPort SN_" + d.source.data.deviceSn.replace(/\./g, "");
        })
        .attr("width", function() {
          return self.config.portBoxW;
        })
        .attr("height", self.config.portBoxH)
        .html(function(d) {
          return self.getPortText(d, d.target.data.parentPort);
        });

      linkEnter
        .append("svg:foreignObject")
        .attr("class", function(d) {
          return "suplinkPort SN_" + d.target.data.deviceSn.replace(/\./g, "");
        })
        .attr("width", function() {
          return self.config.portBoxW;
        })
        .attr("height", self.config.portBoxH)
        .html(function(d) {
          return self.getPortText(d, d.target.data.uplinkPort);
        });

      // Transition links to their new position.
      let linkUpdata = link.merge(linkEnter);

      linkUpdata.select("path").attr("d", function(d) {
        if (self.BASE.horizontal) {
          //判断在横向得情况下目标为AP组或者IPC组要长一截
          if (
            ["AP_GROUP", "IPC_GROUP"].indexOf(d.target.data.deviceType) > -1
          ) {
            return self.elbow({
              source: {
                x: d.source.x + self.config.rectH / 2,
                y: d.source.y + self.config.rectW,
              },
              target: {
                x: d.target.x + self.config.rectH / 2,
                y: d.target.y + (self.config.rectW - self.config.rectH) / 2,
              },
            });
          } else {
            return self.elbow({
              source: {
                x: d.source.x + self.config.rectH / 2,
                y: d.source.y + self.config.rectW,
              },
              target: {
                x: d.target.x + self.config.rectH / 2,
                y: d.target.y,
              },
            });
          }
        } else {
          return self.elbow({
            source: {
              x: d.source.x + self.config.rectW / 2,
              y: d.source.y + self.config.rectH,
            },
            target: {
              x: d.target.x + self.config.rectW / 2,
              y: d.target.y,
            },
          });
        }
      });

      linkUpdata
        .select(".parentPort")
        .attr("transform", function(d) {
          if (self.BASE.horizontal) {
            return `translate(${d.target.y - self.config.rectW},${d.target.x +
              self.config.portBoxW / 2 -
              2})`;
          } else {
            return `translate(${d.target.x + self.config.portBoxW / 2},${d
              .source.y +
              self.config.rectH +
              self.config.elbowRadius +
              self.config.elbowfar})`;
          }
        })
        .style("pointer-events", "auto");

      linkUpdata
        .select(".suplinkPort")
        .attr("transform", function(d) {
          //return self.getProTxtTranslate(d3.select(this.parentNode),0.8);
          if (self.BASE.horizontal) {
            return `translate(${d.target.y - self.config.portBoxW},${d.target
              .x +
              self.config.portBoxW / 2 -
              2})`;
          } else {
            return `translate(${d.target.x + self.config.portBoxW / 2},${d
              .target.y - 30})`;
          }
        })
        .style("pointer-events", "auto");

      // Transition exiting nodes to the parent's new position.
      let linkExit = link.exit().remove();

      linkExit
        .select("path")
        .attr("d", () => {
          let o = {
            x: source.x,
            y: source.y,
          };
          return self.elbow({
            source: o,
            target: o,
          });
        })
        .remove();

      linkExit.select("text").remove();

      // self.BASE.svg.selectAll(".nodeTips").remove();

      nodes.forEach(function(d) {
        d.x0 = d.x;
        d.y0 = d.y;
      });

      self.$emit("renderSuccess");

      if (!noAutoZoom) {
        self.autoZoom();
      }
    },
    /**
     * 初始化自动缩放
     */
    autoZoom() {
      const self = this;
      const gElem = self.BASE.g.node();
      console.log("gElem", gElem);
      if (gElem) {
        const bbox = gElem.getBBox();
        if (!self.BASE.horizontal) {
          if (self.height < bbox.height) {
            let changeScale =
              Math.floor(
                (self.height / (bbox.height + self.config.MARGIN)) * 10
              ) / 10;
            if (changeScale < 0.1) {
              changeScale = 0.1;
            }
            let transform = d3.zoomTransform(gElem);
            let x = transform.x + self.config.MARGIN / changeScale;
            // transform = transform.scale(changeScale);
            self.BASE.svg.call(
              self.BASE.zm.transform,
              d3.zoomIdentity.translate(x, transform.y).scale(changeScale)
            );
          }
        } else {
          let changeScale =
            Math.floor((self.width / (bbox.width + self.config.MARGIN)) * 10) /
            10;
          if (changeScale < 0.1) {
            changeScale = 0.1;
          }

          if (changeScale > 5) {
            changeScale = 5;
          }

          let transform = d3.zoomTransform(gElem);
          self.BASE.svg.call(
            self.BASE.zm.transform,
            d3.zoomIdentity
              .translate(transform.x, transform.y)
              .scale(changeScale)
          );
        }
      }
    },
    /**
     * 拓扑放大
     */
    zoomOut() {
      const self = this;
      const transform = d3.zoomTransform(self.BASE.g.node());
      self.BASE.svg.call(
        self.BASE.zm.transform,
        d3.zoomIdentity
          .translate(transform.x, transform.y)
          .scale(transform.k + 0.2)
      );
    },
    /**
     * 拓扑缩小
     */
    zoomIn() {
      const self = this;
      const transform = d3.zoomTransform(self.BASE.g.node());
      self.BASE.svg.call(
        self.BASE.zm.transform,
        d3.zoomIdentity
          .translate(transform.x, transform.y)
          .scale(transform.k - 0.2)
      );
    },
    rotate() {
      const self = this;
      const transform = d3.zoomTransform(self.BASE.g.node());
      self.BASE.horizontal = !self.BASE.horizontal;
      //self.BASE.zm.scale(scale);
      if (self.BASE.horizontal) {
        self.root.x0 = self.height / 2;
        self.BASE.svg.call(
          self.BASE.zm.transform,
          d3.zoomIdentity
            .translate(20, self.height / 2 - self.config.rectH / 2)
            .scale(transform.k)
        );
      } else {
        self.root.x0 = self.width / 2;
        self.BASE.svg.call(
          self.BASE.zm.transform,
          d3.zoomIdentity
            .translate(self.width / 2 - self.config.rectW / 2, 20)
            .scale(transform.k)
        );
      }
      //重置画布大小
      self.BASE.svg.each(function() {
        d3.select(this.parentNode)
          .attr("height", self.height)
          .attr("width", self.width);
      });

      self.updata(self.root);
    },
    /**
     * 统计分组中离线或者从未上线的设备
     * @param {*} deviceList
     * @returns
     */
    getOffLineDeviceTotal(deviceList) {
      let total = 0;
      for (let i = 0; i < deviceList.length; i++) {
        if (
          deviceList[i].onlineStatus == "OFF" ||
          deviceList[i].onlineStatus == "NEVER_ONLINE"
        ) {
          total++;
        }
      }
      return total;
    },
    /**
     * 获取拓扑树中已选中得节点sn
     * @returns sns
     */
    getTreeSelectNodeSns() {
      let self = this;
      let sns = [];
      self.BASE.g.selectAll(".node.selectednode").each(function(d) {
        sns.push(d.deviceSn);
      });
      return sns;
    },
    /**
     * 分组展开
     * @param {*} d
     * @param {*} selectDevice
     */
    groupExpansion(d, selectDevice) {
      let self = this;
      let groupType = d.data.deviceType.replace("_GROUP", "");
      let deviceList = d.parent.data[`${groupType}_ChildList`] || [];
      d.parent.data.children = d.parent.data.children.filter(
        (item) => item.deviceType != d.data.deviceType
      );
      d.parent.children = d.parent.children.filter(
        (item) => item.data.deviceType != d.data.deviceType
      );
      for (let i = 0; i < deviceList.length; i++) {
        let device = deviceList[i];
        let newNode = d3.hierarchy(device);
        newNode.depth = d.parent.depth + 1;
        newNode.height = d.parent.height - 1;
        newNode.parent = d.parent;
        d.parent.children.push(newNode);
        d.parent.data.children.push(newNode.data);
      }
      self.updata(d.parent, true, selectDevice);
    },
    /**
     * 初始化拓扑
     */
    initTopo() {
      let self = this;
      d3.select("#" + this.elemId)
        .selectAll("svg")
        .remove();
      self.BASE.svg = d3
        .select("#" + this.elemId)
        .append("svg:svg")
        .attr("class", "nodeView")
        .attr("width", self.width)
        .attr("height", self.height);

      self.BASE.g = self.BASE.svg
        .html(
          `<defs>
            <filter id="f1" x="0" y="0" width="200%" height="200%">
                <feOffset result="offOut" in="SourceAlpha" dx="4" dy="4" />
                <feGaussianBlur  stdDeviation="2" />
                <feColorMatrix type="matrix" values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.2 0"/>
                <feBlend in="SourceGraphic" in2="blurOut" mode="normal" />
            </filter>
        </defs>`
        )
        .append("svg:g");

      self.BASE.zm = d3
        .zoom()
        .scaleExtent([0.1, 5])
        .on("zoom", (e) => {
          //console.log(e.transform);
          self.BASE.g.attr("transform", e.transform);
        });

      self.BASE.svg
        .call(self.BASE.zm)
        .call(
          self.BASE.zm.transform,
          d3.zoomIdentity.translate(
            self.width / 2 - self.config.rectW / 2,
            self.config.MARGIN
          )
        )
        .on("dblclick.zoom", null);

      if (self.checkRenderData()) {
        //self.updataLayoutPanel(self.renderData);
        self.initTopoData(self.renderData);
      }

      //return self.BASE.svg;
    },
    /**
     * 获取拓扑连线颜色
     * @param {*} d
     * @param {*} selectDevice
     */
    getLineStroke(d) {
      const self = this;
      let skroke = "";
      switch (self.configOptions.modal) {
        case "vlan":
          skroke = self.config.deviceStatus["ON"];
          break;
        default:
          if (
            d.source.data.name == "OUT" &&
            d.target.data.onlineStatus == "ON"
          ) {
            skroke = self.config.deviceStatus["ON"];
          } else if (
            d.source.data.onlineStatus == "ON" &&
            d.target.data.onlineStatus == "ON"
          ) {
            skroke = self.config.deviceStatus["ON"];
          } else if (
            ["AP_GROUP", "IPC_GROUP"].indexOf(d.source.data.deviceType) > -1 &&
            d.target.data.onlineStatus == "ON"
          ) {
            skroke = self.config.deviceStatus["ON"];
          } else {
            skroke = self.config.deviceStatus["OFF"];
          }
          break;
      }
      return skroke;
    },
  },
  mounted() {
    this.initTopo();
  },
};
</script>
<style lang="less">
.topo-container {
  position: relative;
  .tools-container {
    position: absolute;
    bottom: 10px;
    right: 10px;
    .tool-group {
      margin: 10px 0;
      .tool-btn {
        width: 24px;
        height: 24px;
        background-color: #fff;
        text-align: center;
        line-height: 24px;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3);
        color: #333;
        font-size: 12px;
      }
    }
  }
}
.topo {
  .devices-box {
    fill: #fff;
  }
  .devices-root {
    fill: transparent;
  }
  .selectednode {
    .nodeSelectFlag {
      display: block;
    }
  }
  .nodeSelectFlag {
    fill: #2b6afd;
    display: none;
  }
  .link {
    text-align: center;
    span {
      display: inline-block;
      padding: 0 5px;
      background-color: #fff;
      height: 22px;
      line-height: 19px;
      border-radius: 22px;
      border: 1px solid transparent;
    }
  }
  .device-name {
    pointer-events: none;
  }
  .panelport {
    .port {
      &.grey {
        fill: #d9d9d9;
      }
      &.green {
        fill: #13bb72;
      }
      &.yellow {
        fill: #f17a00;
      }
      &.empty {
        fill: #fff;
        stroke: #8c8c8c;
        stroke-width: 1px;
        stroke-dasharray: 1, 1;
      }
      &.abnormal {
        fill: #de321f;
      }
      &.warn {
        fill: #f17a00;
      }
    }
  }
}
</style>
