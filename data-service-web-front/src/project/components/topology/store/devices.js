//临时定义变量 需要修改成百川接口对接方式
// const maccApi = {},
//   requestHttp = {};
import TopoService from "../services/index";

const state = {
  deviceLayout: {},
  group_id: "111",
  multiPanelClass: [],
  deviceTotal: {
    onlineDevCount: 0,
    totalDevCount: 0
  }
};
const mutations = {
  SET_GROUP_ID(state, group_id) {
    state.group_id = group_id;
  },
  SET_DEVICELAYOUT(state, payload) {
    state.deviceLayout = { ...state.deviceLayout, ...payload };
  },
  UPDATA_DEVICELAYOUT(state, payload) {
    state.deviceLayout = payload;
  },
  SET_MULTICLASS(state, multiPanelClass) {
    state.multiPanelClass = multiPanelClass;
  },
  SET_DEVICETOTAL(state, deviceTotal) {
    state.deviceTotal = { ...deviceTotal };
  }
};
const actions = {
  setDeviceLayout({ commit }, prodectClass) {
    commit("SET_DEVICELAYOUT", prodectClass);
  },
  async updataMultiPanelClass({ commit }) {
    let res = await TopoService.getDeviceLinecardswitch();
    if (res && res.code == 0) {
      commit("SET_MULTICLASS", res.productClass);
    } else {
      this.$message.error(res.msg);
    }
    // return new Promise((resolve, reject) => {
    //   let data = {
    //     api: maccApi.GET_DEVICE_LINECARDSWITCH,
    //     method: "GET"
    //   };
    //   requestHttp(data)
    //     .then(res => {
    //       if (res && res.code == 0) {
    //         commit("SET_MULTICLASS", res.productClass);
    //         resolve();
    //       } else {
    //         reject(res);
    //       }
    //     })
    //     .catch(error => {
    //       reject(error);
    //     });
    // });
  },
  async updataDeviceLayout({ commit, state }, list) {
    let res = await TopoService.getDevicePortInfo(list);
    if (res && res.code == 0) {
      let deviceLayout = Object.assign({}, state.deviceLayout);
      for (let name in res) {
        if (name != "code") {
          deviceLayout[name] = res[name];
        }
      }
      commit("UPDATA_DEVICELAYOUT", deviceLayout);
    } else {
      this.$message.error(res.msg);
    }
    // return new Promise((resolve, reject) => {
    //   let data = {
    //     api: maccApi.POST_DEVICE_PORT_INFO,
    //     method: "POST",
    //     params: { list: list }
    //   };
    //   requestHttp(data)
    //     .then(res => {
    //       if (res && res.code == 0) {
    //         let deviceLayout = Object.assign({}, state.deviceLayout);
    //         for (let name in res) {
    //           if (name != "code") {
    //             deviceLayout[name] = res[name];
    //           }
    //         }
    //         commit("UPDATA_DEVICELAYOUT", deviceLayout);
    //         resolve();
    //       } else {
    //         reject(res);
    //       }
    //     })
    //     .catch(error => {
    //       reject(error);
    //     });
    // });
  }
};

const getters = {
  getDevicePortsTotalMap(state) {
    const map = {};
    for (let name in state.deviceLayout) {
      let ports = 0;
      const device = state.deviceLayout[name];
      for (let i = 0; i < device.length; i++) {
        for (let j = 0; j < device[i].length; j++) {
          if (device[i][j]) {
            ports++;
          }
        }
      }
      map[name] = ports;
    }
    return map;
  }
};

export default {
  namespaced: true, //开启命名空间，更好的区分使用stage,防止不同stage模块中的方法重名
  state,
  getters,
  mutations,
  actions
};
