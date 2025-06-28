import { terminalTools, topoTools } from "./tools/index";
import TopoService from "./services/index";
const topoDataMixin = {
  data() {
    return {
      topoData: {},
      portsMap: {},
      topOptions: {
        terminalTypeMap: {
          switch: {
            iconName: "switch",
            desc: "交换机",
          },
          printer: {
            iconName: "printer",
            desc: "打印机",
          },
          server: {
            iconName: "server",
            desc: "服务器",
          },
          pc: {
            iconName: "pc",
            desc: "电脑",
          },
          nvr: {
            iconName: "nvr",
            desc: "硬盘录像机(NVR)",
          },
          camera: {
            iconName: "camera",
            desc: "摄像机",
          },
          bridge: {
            iconName: "bridge",
            desc: "网桥",
          },
          ap: {
            iconName: "ap",
            desc: "无线AP",
          },
          ac: {
            iconName: "ac",
            desc: "无线AC",
          },
          exit_router: {
            iconName: "exit_router",
            desc: "出口路由器",
          },
          other: {
            iconName: "other",
            desc: "其他",
          },
        },
        modal: "devices",
        multipleSelect: false, //是否可以多选节点
        filterClass: [], //禁止点击设备类型
      },
    };
  },
  methods: {
    /**
     * 获取拓扑数据
     * @param {boolean} [withWiredTerminal=false]
     * @returns
     */
    async getTopoData(withWiredTerminal = false, groupId) {
      if (groupId) {
        let res = await TopoService.getTopoData(withWiredTerminal, groupId);
        if (res && res.code == 0) {
          return res.data;
        } else {
          this.$message.error(res.msg);
        }
      } else {
        return {};
      }
      // return new Promise((resolve, reject) => {
      //   //const groupId = this.$store.state.Group.groupId;
      //   if (groupId) {
      //     let data = {
      //       api: maccApi.GET_TOPOLOGY_TREEDATA.replace(
      //         "{group_id}",
      //         groupId
      //       ).replace("{with_wired_terminal}", withWiredTerminal),
      //       method: "GET"
      //     };
      //     requestHttp(data)
      //       .then(res => {
      //         if (res && res.code == 0) {
      //           resolve(res.data);
      //         } else {
      //           reject(res);
      //         }
      //       })
      //       .catch(error => {
      //         reject(error);
      //       });
      //   } else {
      //     resolve({});
      //   }
      // });
    },
    /**
     * 获取拓扑中设备面板端口数据
     * @param {*} sn
     * @returns
     */
    async getTopoDevicePanelPortsData(sn) {
      let res = await TopoService.getTopoDevicePanelPortsData(sn);
      if (res && res.code == 0) {
        this.portsMap = res.portMap;
        return res.portMap;
      } else {
        this.$message.error(res.msg);
      }
      // return new Promise((resolve, reject) => {
      //   let data = {
      //     api: maccApi.GET_DEVICE_PANELPOERTS,
      //     method: "POST",
      //     params: { snList: sns }
      //   };
      //   requestHttp(data)
      //     .then(res => {
      //       if (res && res.code == 0) {
      //         resolve(res.portMap);
      //       } else {
      //         reject(res);
      //       }
      //     })
      //     .catch(error => {
      //       reject(error);
      //     });
      // });
    },
    /**
     * 格式化拓扑数据
     * @param {*} topoData 拓扑数据
     * @param {*} portsMap 设备面板端口数据MAP
     */
    formatterTopoData(topoData, portsMap) {
      topoTools.rootPickDevice(topoData, "AP");
      topoTools.rootPickDevice(topoData, "ipc");
      if (topoData.deviceSn) {
        topoData.portsData = this.formatterPortData(
          portsMap[topoData.deviceSn] || []
        );
      }
      let children = topoData.children || topoData._children || [];
      if (children.length > 0) {
        for (let i = 0; i < children.length; i++) {
          this.formatterTopoData(children[i], portsMap);
        }
      }
    },
    /**
     * 格式化设备端口面板数据
     * @param {*} portsData
     * @returns
     */
    formatterPortData(portsData) {
      let portData = {};
      for (let i = 0; i < portsData.length; i++) {
        let item = portsData[i];
        let port = {
          name: item.name,
          order: item.order,
          mediumType: item.mediumType || "Copper",
          portClass: [],
          exceptionColor: "grey", //不可用端口颜色
        };

        if (item.enable == "false") {
          port.portClass.push("grey");
        }

        if (item.status == "Up") {
          if (["10M", "100M"].indexOf(item.speed) > -1) {
            port.portClass.push("yellow");
          } else {
            port.portClass.push("green");
          }
        }
        portData[item.order] = port;
      }
      return portData;
    },
    /**
     * 初始化拓扑
     * @param {*} withWiredTerminal
     */
    initTopo(withWiredTerminal = false, gid) {
      const self = this;
      this.getTopoData(withWiredTerminal, gid).then((topoData) => {
        //判断是否存在拓扑数据
        if (topoData == undefined || JSON.stringify(topoData) == "{}") {
          self.topoData = topoData;
          return;
        }
        //;
        // 获取设备端口信息接口需要传一个id
        let networkPanelClass = [],
          sns = [];
        let deviceLayouts = self.$store.state.devices.deviceLayout;
        topoTools.getNetworkDeviceClass(
          topoData || {},
          deviceLayouts,
          networkPanelClass,
          sns
        );
        let promiseList = [];
        promiseList.push(self.getTopoDevicePanelPortsData(sns));

        if (withWiredTerminal) {
          promiseList.push(terminalTools.getTerminalTypeList());
        }
        //当网络中的设备面板在store中不存在时，请求面板布局数据并存入store
        // 判断条件 >0 ===> >=0
        if (networkPanelClass.length >= 0) {
          promiseList.push(
            self.$store.dispatch(
              "devices/updataDeviceLayout",
              networkPanelClass
            )
          );
        }

        Promise.all(promiseList)
          .then((result) => {
            console.log("网络中设备面板端口信息", result[0]);
            topoData = topoData || {};
            self.formatterTopoData(topoData, result[0]);
            if (withWiredTerminal) {
              let terminalTypeMap = terminalTools.getTerminalTypeIconMap(
                result[1]
              );
              console.log("终端类型列表", terminalTypeMap);
              self.topOptions.terminalTypeMap = terminalTypeMap;
            }
            self.topoData = topoData;
          })
          .catch((error) => {
            console.log(`更新面板布局失败`, error);
          });
      });
    },
  },
};

export default topoDataMixin;
