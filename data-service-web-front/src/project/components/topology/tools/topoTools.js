import * as d3 from "d3";
const topoTools = {
  getImgMap() {
    const files = require.context(
      "@/assets/images/device/",
      true,
      /\.(svg|png)$/
    );
    const lib = {};
    //key是相对路径名
    files.keys().forEach((key) => {
      const name = key.match(/\.\/(.*).(svg|png)$/)[1];
      lib[name] = files(key);
    });
    return lib;
  },
  DEVICETYPE: {
    AP: "AP",
    EAP: "AP",
    EWR: "WR",
    EHR: "WR",
    EGW: "GATEWAY",
    GATEWAY: "GATEWAY",
    MSW: "SWITCH",
    SWITCH: "SWITCH",
    ESW: "SWITCH",
    AC: "AC",
    EAC: "AC",
    UNKNOW: "UNKNOW",
    ipc: "ipc",
    nvr: "nvr",
    OUT: "OUT",
  },
  /**
   * 打包设备成组
   * @param {*} topoData
   * @param {*} commonType
   */
  pickDevice(topoData, commonType) {
    const self = this;
    if (self.checkGroupDevice(topoData.data.children || [], commonType)) {
      self.groupDevice(topoData, commonType);
    }
  },
  /**
   * 打包设备成组
   * @param {*} topoData
   * @param {*} commonType
   */
  rootPickDevice(topoData, commonType) {
    const self = this;
    if (self.checkGroupDevice(topoData.children || [], commonType)) {
      self.rootGroupDevice(topoData, commonType);
    }
  },
  /**
   * 检测子分组指定设备是否大于1台
   * @param {*} deviceList
   * @param {*} commonType
   * @returns
   */
  checkGroupDevice(deviceList, commonType) {
    const self = this;
    let list = [];
    for (let i = 0; i < deviceList.length; i++) {
      if (self.DEVICETYPE[deviceList[i].deviceType] == commonType) {
        list.push(deviceList[i]);
      }
    }
    return list.length > 1;
  },
  /**
   * 将设备打包
   * @param {*} topoData
   * @param {*} commonType
   */
  groupDevice(topoData, commonType) {
    const self = this;
    let deviceList = [],
      parentPortList = [],
      uplinkPortList = [];
    let groupStatus = "ON";
    topoData.data.children = topoData.data.children.filter(function(item) {
      if (self.DEVICETYPE[item.deviceType] == commonType) {
        parentPortList.push(item.parentPort);
        uplinkPortList.push(item.uplinkPort);
        deviceList.push(item);
        if (item.onlineStatus != "ON") {
          groupStatus = "OFF";
        }
        return false;
      } else {
        return true;
      }
    });
    topoData.children = topoData.children.filter(
      (item) => !self.DEVICETYPE[item.data.deviceType] == commonType
    );

    topoData.data[`${commonType.toLocaleUpperCase()}_ChildList`] = deviceList;
    let timer = new Date().getTime();
    let name = commonType.toLocaleUpperCase() == "IPC" ? "摄像头" : "AP";
    let groupDevice = {
      name: `${name}组`,
      deviceType: `${commonType.toLocaleUpperCase()}_GROUP`,
      id: timer,
      deviceSn: `${commonType.toLocaleUpperCase()}_GROUP_${timer}_${Math.floor(
        Math.random() * 100
      )}`,
      parentPort: self.formatterAlias(parentPortList),
      uplinkPort: Array.from(new Set(uplinkPortList)).join(),
      onlineStatus: groupStatus,
      _children: deviceList,
      children: [],
    };
    let newNode = d3.hierarchy(groupDevice);
    newNode.depth = topoData.depth + 1;
    newNode.height = topoData.height - 1;
    newNode.parent = topoData;
    //groupDevice[`group_${commonType.toLocaleUpperCase()}`] = deviceList
    topoData.children.push(newNode);
    topoData.data.children.push(newNode.data);
  },
  /**
   * 将设备打包
   * @param {*} topoData
   * @param {*} commonType
   */
  rootGroupDevice(topoData, commonType) {
    const self = this;
    let deviceList = [],
      parentPortList = [],
      uplinkPortList = [];
    let groupStatus = "ON";
    topoData.children = topoData.children.filter(function(item) {
      if (self.DEVICETYPE[item.deviceType] == commonType) {
        parentPortList.push(item.parentPort);
        uplinkPortList.push(item.uplinkPort);
        deviceList.push(item);
        if (item.onlineStatus != "ON") {
          groupStatus = "OFF";
        }
        return false;
      } else {
        return true;
      }
    });
    topoData[`${commonType.toLocaleUpperCase()}_ChildList`] = deviceList;
    let timer = new Date().getTime();
    let name = commonType.toLocaleUpperCase() == "IPC" ? "摄像头" : "AP";
    let groupDevice = {
      name: `${name}组`,
      deviceType: `${commonType.toLocaleUpperCase()}_GROUP`,
      id: timer,
      deviceSn: `${commonType.toLocaleUpperCase()}_GROUP_${timer}_${Math.floor(
        Math.random() * 100
      )}`,
      parentPort: self.formatterAlias(parentPortList),
      uplinkPort: Array.from(new Set(uplinkPortList)).join(),
      onlineStatus: groupStatus,
      _children: deviceList,
      children: [],
    };
    //groupDevice[`group_${commonType.toLocaleUpperCase()}`] = deviceList
    topoData.children.push(groupDevice);
  },
  /**
   * 格式化端口名称
   * @param {*} alias
   */
  formatterAlias(alias) {
    const self = this;
    let aliasArray = alias.concat([]);

    //别名排序
    aliasArray.sort(function(a, b) {
      let as = a.match(/\d+$/g);
      let bs = b.match(/\d+$/g);
      return as[0] - bs[0];
    });
    //定义前缀数组
    let aliasPrefix = {};
    for (let i = 0; i < aliasArray.length; i++) {
      let aliasItem = aliasArray[i];
      let num = aliasItem.match(/\d+$/g)[0];
      let prefix = aliasItem.replace(/\d+$/, "");
      if (aliasPrefix[prefix]) {
        aliasPrefix[prefix].push(parseInt(num));
      } else {
        aliasPrefix[prefix] = [];
        aliasPrefix[prefix].push(parseInt(num));
      }
    }

    console.log("前缀排序对象", aliasPrefix);

    let str = "";
    for (let name in aliasPrefix) {
      let aliasString = self.aliasFormat(name, aliasPrefix[name]);
      str += aliasString + ",";
    }

    return str.slice(0, str.length - 1);
  },
  /**
   * 同前缀别名格式化
   * @param {*} prefix
   * @param {*} numArray
   */
  aliasFormat(prefix, numArray) {
    let newList = "";
    let max = numArray[0],
      min = numArray[0];
    for (let i = 0; i < numArray.length; i++) {
      let j = i + 1;
      if (j >= numArray.length) j = i; //注意：因为j=i+1,所以j会出现超出数组下标范围的情况
      //先比较第一个和第二个，如果连续，再比较第二个和第三个...直到出现不连续时就输出缩写1-3，再进行下面数字的比较
      if (numArray[i] == numArray[j] - 1) {
        //如果连续，就先暂时把Arr[j]赋值给-右侧的数字
        max = numArray[j];
      } else {
        if (i == numArray.length - 1) {
          //如果没有这个判断最后会输出newlist="1-3,7-7,10-15,"会多一个‘，’
          if (min == max) {
            newList += prefix + min;
          } else {
            newList += prefix + min + "-" + max;
          }
        } else {
          if (min == max) {
            newList += prefix + min + ",";
          } else {
            newList += prefix + min + "-" + max + ",";
          }
        }
        //此时将min和max都赋值为7
        if (j < numArray.length) {
          min = numArray[j];
          max = numArray[j];
        }
      }
    }

    return newList;
  },
  /**
   * 获取设备图片
   * @param {*} info 设备类型对象
   * @param {*} ports 端口数量
   * @param {*} terminalTypeMap 终端MAP
   * @param {*} onlineStatusFlag 在线状态
   */
  getDevicePanelBgImg(info, terminalTypeMap, onlineStatusFlag) {
    let bgImg = "";
    let onlineStatus = onlineStatusFlag ? onlineStatusFlag : "";
    switch (info.commonType) {
      case "SWITCH":
        // if (ports == 0) {
        //   bgImg = `SW${onlineStatus}.svg`;
        // } else if (ports <= 5) {
        //   bgImg = `SW${onlineStatus}.svg`;
        // } else if (ports <= 24) {
        //   bgImg = `SW${onlineStatus}.svg`;
        // } else {
        bgImg = `SW${onlineStatus}`;
        // }
        break;
      case "WR":
        bgImg = `GW${onlineStatus}`;
        break;
      case "GATEWAY":
        //if (ports == 0) {
        bgImg = `GW${onlineStatus}`;
        // } else if (ports <= 5) {
        //   bgImg = `GW${onlineStatus}.svg`;
        // } else {
        //   bgImg = `GW${onlineStatus}.svg`;
        // }
        break;
      case "AC":
        bgImg = `GW${onlineStatus}`;
        break;
      case "AP": {
        let rapArr = [
          "RAP220(V2)",
          "RAP220(EV2)",
          "RAP230",
          "RG-RAP220(V2)",
          "RG-RAP220(EV2)",
          "RG-RAP230",
        ];
        let RG_rap210Arr = [
          "RAP210",
          "RAP210(B)",
          "RAP210(V2)",
          "RAP210(EV2)",
          "RG-RAP210",
          "RG-RAP210(B)",
          "RG-RAP210(V2)",
          "RG-RAP210(EV2)",
        ];
        let eapArr = ["EAP201", "EAP202"];
        let RG_rapArr = [
          "RAP120(V2)",
          "RAP100",
          "RAP110",
          "RAP102",
          "RG-RAP120(V2)",
          "RG-RAP100",
          "RG-RAP110",
          "RG-RAP102",
        ];
        let RG_eap = ["EAP101", "EAP102", "RG-EAP101", "RG-EAP102"];
        if (rapArr.indexOf(info.productClass) != -1) {
          bgImg = "RAP";
        } else if (RG_rap210Arr.indexOf(info.productClass) != -1) {
          bgImg = "RG-RAP210";
        } else if (eapArr.indexOf(info.productClass) != -1) {
          bgImg = "EAP";
        } else if (RG_rapArr.indexOf(info.productClass) != -1) {
          bgImg = "RG-RAP";
        } else if (RG_eap.indexOf(info.productClass) != -1) {
          bgImg = "RG-EAP";
        } else if (
          info.productClass == "RG-RAP602" ||
          info.productClass == "RAP602"
        ) {
          bgImg = "RG-RAP210";
        } else {
          bgImg = "APcommon";
        }
        break;
        // case "ipc":
        //     bgImg = 'ipc.png';
        //     break;
      }
      case "nvr":
        bgImg = `GW${onlineStatus}`;
        break;
      default: {
        let commontype = info.commonType == "ipc" ? "camera" : info.commonType;
        commontype = commontype || info.deviceType;
        if (commontype == "camera") {
          bgImg = `IPC${onlineStatus}`;
        } else if (
          terminalTypeMap[commontype] &&
          terminalTypeMap[commontype].iconName
        ) {
          bgImg = terminalTypeMap[commontype].iconName;
        } else {
          bgImg = `unkown${onlineStatus}`;
        }

        break;
      }
    }
    return bgImg;
  },
  /****
   * 递归统计拓扑中不在store中存在的面板布局的设备型号
   * @param {*} renderData  拓扑数据
   * @param {*} deviceLayouts  store中存在的面板布局
   * @param {*} networkPanelClass  需要重新获取端口面板布局的设备型号列表
   * @param {*} sns  需要重新获取端口面板布局的设备型号列表
   */
  getNetworkDeviceClass(renderData, deviceLayouts, networkPanelClass, sns) {
    let deviceType = renderData.deviceType;
    let productClass = renderData.productClass || "";
    console.log("端口布局", renderData);
    if (["SWITCH", "GATEWAY", "WR"].indexOf(this.DEVICETYPE[deviceType]) > -1) {
      if (
        networkPanelClass.indexOf(productClass) == -1 &&
        deviceLayouts[productClass] == undefined
      ) {
        networkPanelClass.push(productClass);
      }
      sns.push(renderData.deviceSn);
    }

    if (renderData.children || renderData._children) {
      let children = renderData.children || renderData._children;
      for (let i = 0; i < children.length; i++) {
        this.getNetworkDeviceClass(
          children[i],
          deviceLayouts,
          networkPanelClass,
          sns
        );
      }
    }
  },
};

export { topoTools };
