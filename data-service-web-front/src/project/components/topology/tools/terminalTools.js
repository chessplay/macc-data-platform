// const maccApi = {},
//   requestHttp = {};
import TopoService from "../services/index";
const terminalTools = {
  async getTerminalTypeList(params) {
    let res = await TopoService.getDevicePanelpoerts(params);
    if (res && res.code == 0) {
      return res.typeList || [];
    } else {
      this.$message.error(res.msg);
    }
    // return new Promise((resolve, reject) => {
    //   let apiUrl = maccApi.GET_TERMINAL_CUSTOM;
    //   if (params && params.get_class) {
    //     apiUrl += `&get_class=${params.get_class}`;
    //   }
    //   if (params && params.device_class) {
    //     apiUrl += `&device_class=${params.device_class}`;
    //   }
    //   let data = {
    //     api: apiUrl,
    //     method: "GET"
    //   };
    //   requestHttp(data)
    //     .then(res => {
    //       if (res && res.code == 0) {
    //         resolve(res.typeList || []);
    //       } else {
    //         reject(res);
    //       }
    //     })
    //     .catch(error => {
    //       reject(error);
    //     });
    // });
  },
  getTerminalTypeIconMap(typelist) {
    const iconMap = {};
    for (let i = 0; i < typelist.length; i++) {
      iconMap[typelist[i].deviceType.toLocaleLowerCase()] = {
        iconName: (typelist[i].iconName || "").toLocaleLowerCase(),
        desc: typelist[i].remark || ""
      };
    }
    return iconMap;
  }
};
export { terminalTools };
