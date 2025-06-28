const wioMenu = {
  label: "一键网优",
  t: "menu.wio",
  path: "wio",
  checkName: 'wifiWio',
  //compPath: () => import("@/views/egw/wio/Wio"),
}
const mswMenuConfig = [
  {
    label: "首页",
    t: "menu.home",
    icon: "rjucd-project",
    path: "home_overview",
    //compPath: () => import("@/views/msw/home/Overview")
  },
  {
    label: "VLAN划分",
    t: "menu.vlan_base",
    icon: "rjucd-vlan",
    path: "vlan_base",
    //compPath: () => import("@/views/msw/vlan/Base")
  },
  {
    label: "监控信息",
    t: "menu.monitor",
    icon: "rjucd-monitor",
    path: "monitor",
    showChilds: true,
    children: [
      {
        label: "端口流量",
        t: "menu.port_flow",
        path: "monitor_portflow",
        //compPath: () => import("@/views/msw/monitor/Portflow")
      },
      {
        label: "终端管理",
        t: "menu.pdmanage",
        path: "monitor_pdmanage",
        //compPath: () => import("@/views/msw/monitor/Pdmanage")
      }
    ]
  },
  {
    label: "端口管理",
    t: "menu.port_manage",
    icon: "rjucd-basicsettings",
    path: "port",
    showChilds: true,
    children: [
      {
        label: "端口设置",
        t: "menu.port_cfg",
        path: "port_setting",
        //compPath: () => import("@/views/msw/port/Setting")
      },
      {
        label: "聚合端口",
        t: "menu.port_ap",
        path: "port_aggregation",
        //compPath: () => import("@/views/msw/port/Aggregation")
      },
      {
        label: "端口镜像",
        t: "menu.port_mirror",
        path: "port_mirror",
        //compPath: () => import("@/views/msw/port/Mirror")
      },
      {
        label: "端口限速",
        t: "menu.port_rate",
        path: "port_rate",
        //compPath: () => import("@/views/msw/port/Rate")
      },
      {
        label: "PoE",
        path: "port_poe",
        checkName: 'mswPoe',
        //compPath: () => import("@/views/msw/home/poe/Port")
      },
      {
        label: "管理IP",
        t: "menu.manage_ip",
        path: "home_wan",
        //compPath: () => import("@/views/msw/home/Wan")
      },
      {
        label: "机箱管理IP",
        t: "menu.chassis_ip",
        path: "chassis",
        checkName: 'mswChassis',
        //compPath: () => import("@/views/msw/home/Chassis"),
      }
    ]
  },
  {
    label: "二层组播",
    t: "menu.igmp",
    icon: "rjucd-cloud",
    path: "igmp",
    checkName: 'mswIgmp',
    //compPath: () => import("@/views/msw/igmp/Igmp"),
  },
  {
    label: "三层管理",
    t: "menu.l3interface",
    icon: "rjucd-network",
    path: "l3_interface",
    checkName: 'mswL3interface',
    //compPath: () => import("@/views/msw/port/L3switch"),
  },
  {
    label: "安全管理",
    t: "menu.sec_manage",
    icon: "rjucd-safe",
    path: "sec",
    showChilds: true,
    children: [
      {
        label: "DHCP Snooping",
        path: "sec_snooping",
        //compPath: () => import("@/views/msw/sec/Snooping")
      },
      {
        label: "风暴控制",
        t: "menu.storm",
        path: "sec_storm",
        //compPath: () => import("@/views/msw/sec/Storm")
      },
      {
        label: "ACL",
        path: "advanced_acl",
        //compPath: () => import("@/views/msw/advanced/Acl")
      },
      {
        label: "端口保护",
        t: "menu.port_protect",
        path: "sec_protect",
        //compPath: () => import("@/views/msw/port/setting/Protect")
      },
      {
        label: "IP+MAC端口绑定",
        t: "menu.ipMacBind",
        path: "ip_mac_bind",
        checkName: 'ipMacBind',
        //compPath: () => import("@/views/msw/sec/IpMacBind")
      },
      {
        label: "防网关ARP欺骗",
        t: "menu.assPrevent",
        path: "aas",
        checkName: 'aas',
        //compPath: () => import("@/views/msw/sec/Aas")
      }
    ]
  },
  {
    label: "高级设置",
    t: "menu.advanced_cfg",
    icon: "rjucd-toolbox",
    path: "advanced",
    showChilds: true,
    children: [
      {
        label: "STP",
        path: "advanced_stp",
        //compPath: () => import("@/views/msw/advanced/Stp")
      },
      {
        label: "LLDP",
        path: "advanced_lldp",
        //compPath: () => import("@/views/msw/advanced/Lldp")
      },
      {
        label: "RLDP",
        path: "advanced_rldp",
        //compPath: () => import("@/views/msw/advanced/Rldp")
      },
      {
        label: "本机DNS",
        t: "menu.dns",
        path: "advanced_dns",
        //compPath: () => import("@/views/common/advanced/Dns")
      }
    ]
  },
  {
    label: "故障诊断",
    t: "menu.diagnose",
    icon: "rjucd-diagnosis",
    path: "diagnose",
    showChilds: true,
    children: [
      {
        label: "网络工具",
        t: "menu.net_tool",
        path: "diagnose_tool",
        //compPath: () => import("@/views/common/diagnose/Tool")
      },
      {
        label: "故障收集",
        t: "menu.collect_err",
        path: "diagnose_tech",
        //compPath: () => import("@/views/common/diagnose/Tech")
      },
      {
        label: "线缆检测",
        t: "menu.cable_test",
        path: "monitor_cable",
        //compPath: () => import("@/views/msw/monitor/Cable")
      }
    ]
  },
  {
    label: "系统设置",
    t: "menu.sys_cfg",
    icon: "rjucd-systemset",
    path: "systool",
    showChilds: true,
    children: [
      {
        label: "系统时间",
        t: "menu.sys_date",
        path: "systool_date",
        checkName: 'mswDate',
        //compPath: () => import("@/views/common/systool/Date")
      },
      {
        label: "登录管理",
        t: "menu.login_management",
        path: "systool_pass_session",
        //compPath: () => import("@/views/common/systool/PassSession")
      },
      {
        label: "配置管理",
        t: "menu.cfg_manage",
        path: "systool_recover",
        //compPath: () => import("@/views/common/systool/Recover")
      },
      {
        label: "系统升级",
        t: "menu.sys_upgrade",
        path: "systool_upgrade",
        //compPath: () => import("@/views/common/systool/Upgrade")
      },
      {
        label: "定时重启",
        t: "menu.time_reboot",
        path: "time_reload",
        checkName: 'mswReload',
        //compPath: () => import("@/views/common/systool/reload/TimeReloadTab"),
      },
      {
        label: "设备重启",
        t: "menu.reboot",
        path: "systool_reload",
        checkName: 'mswReboot',
        //compPath: () => import("@/views/common/systool/reload/ReloadTab"),
      }
    ]
  }
]
// devAllow的值有 egw ehr eap msw四种，对应四个序列的web包
let menuConfig = [
  {
    label: "菜单列表",
    path: "admin",
    //compPath: () => import("@/views/common/AdminMain"),
    showChilds: true,
    children: [
      {
        label: "整网概览",
        t: "menu.whole_view",
        icon: "rjucd-project",
        path: "home_overview",
        checkName: 'isMaster',
        devAllow: ['egw', 'eap', 'msw'],
        //compPath: () => import("@/views/common/overview/Overview"),
      },
      {
        label: "在线用户",
        t: "menu.online_user",
        icon: "rjucd-user",
        path: "home_online",
        checkName: 'homeOnline',
        devAllow: ['egw'],
        //compPath: () => import("@/views/egw/OnlineUser"),
      },
      {
        label: "设备配置(交换机)",
        t: "menu.dev_cfg",
        icon: "rjucd-switch",
        path: "alone",
        devAllow: ['msw'],
        //compPath: () => import("@/views/msw/home/Setting"),
        showChilds: false,
        hidden: true,
        children: mswMenuConfig
      },
      {
        label: "网关管理(NBR)",
        t: "menu.nbr_manage",
        icon: "rjucd-gateway",
        path: "nbr",
        checkName: 'nbrManage',
        devAllow: ["egw", "eap", "msw"],
        //compPath: () => import("@/views/common/Nbr"),
        showChilds: false,
      },
      {
        label: "路由管理（EG从角色)",
        t: "menu.gateway_manage",
        icon: "rjucd-gateway",
        path: "slave_eg",
        checkName: 'egwManage',
        devAllow: ['egw'],
        //compPath: () => import("@/views/common/SlaveEg"),
        showChilds: false
      },
      {
        label: "路由管理(EG)/本机管理",
        t: "menu.gateway_manage",
        icon: "rjucd-gateway",
        path: "alone",
        checkName: "aloneManage",
        devAllow: ["egw", "eap", "ehr"],
        //compPath: () => import("@/views/common/AloneMain"),
        showChilds: false,
        children: [
          {
            label: "设备概览",
            t: "menu.dev_view",
            icon: "rjucd-project",
            path: "homepage",
            checkName: 'egwHome',
            //compPath: () => import("@/views/common/overview/OverviewDev"),
          },
          {
            label: "在线用户",
            t: "menu.online_user",
            icon: "rjucd-user",
            path: "home_online",
            checkName: 'egwOnline',
            devAllow: ["egw"],
            //compPath: () => import("@/views/egw/OnlineUser"),
          },
          { // ehr的在线用户列表和egw有区别
            label: "在线用户",
            t: "menu.online_user",
            icon: "rjucd-user",
            path: "home_online",
            checkName: 'isSlave',
            devAllow: ["ehr"],
            //compPath: () => import("@/views/ehr/OnlineUser"),
          },
          {
            label: "基本管理",
            t: "menu.base_manage",
            icon: "rjucd-network",
            path: "network",
            showChilds: true,
            children: [
              {
                label: "WAN设置",
                t: "menu.wan_cfg",
                path: "network_wan",
                checkName: 'networkWan',
                //compPath: () => import("@/views/common/network/Wan"),
              },
              {
                label: "LAN设置",
                t: "menu.lan_cfg",
                path: "network_lan",
                checkName: 'networkLan',
                //compPath: () => import("@/views/common/network/Lan"),
              },
              {
                label: "IPv6设置",
                t: "menu.ipv6_cfg",
                path: "network_ipv6",
                checkName: 'networkIpv6',
                //compPath: () => import("@/views/common/network/IPv6"),
              },
              {
                label: "POE供电",
                t: "menu.poe_power",
                path: "eg_poe",
                checkName: 'networkPoe',
                //compPath: () => import("@/views/common/Poe"),
              },
              {
                label: "中继设置",
                t: "menu.dev_mode",
                path: "dev_mode",
                devAllow: ["ehr"],
                //compPath: () => import("@/views/ehr/DevMode"),
              },
              {
                label: "端口VLAN",
                t: "menu.port_vlan",
                path: "vlan_port",
                checkName: 'portVlan',
                devAllow: ["egw"],
                //compPath: () => import("@/views/common/network/lan/VlanPortTab"),
              }
            ]
          },
          {
            label: "无线管理",
            t: "menu.wifi_manage",
            icon: "rjucd-wifi",
            path: "wifi",
            showChilds: true,
            children: [
              {
                label: "无线设置",
                t: "menu.wifi_cfg",
                path: "wifi_setting",
                checkName: 'wifiSetting',
                //compPath: () => import("@/views/common/wireless/wifiSetting"),
              },
              {
                label: "无线用户",
                t: "menu.wifi_user",
                path: "wifi_sta",
                checkName: 'wifiSta',
                devAllow: ["egw", "eap"],
                //compPath: () => import("@/views/common/wireless/components/WirelessUser"),
              },
              {
                label: "黑白名单",
                t: "menu.wifiBwlist",
                path: "wifi_bwlist",
                checkName: 'wifiBwlist',
                devAllow: ["egw", "eap", "ehr"],
                //compPath: () => import("@/views/common/wireless/MacFilter"),
              },
              {
                label: "射频设置",
                t: "menu.wifi_advanced",
                path: "wifi_advanced",
                checkName: 'wifiSsidRadio',
                //compPath: () => import("@/views/common/wireless/components/SsidRadioTab"),
              },
              {
                label: "有线口设置",
                t: "menu.wirelan_cfg",
                path: "wifi_wirelan_alone",
                checkName: 'wifiWirelan',
                devAllow: ["egw", "eap"],
                //compPath: () => import("@/views/common/wireless/WireLan"),
              },
              {
                // "一键网优"
                ...wioMenu,
                devAllow: ["ehr"]//EHR页面放开
              }
            ]
          },
          {
            label: "子路由管理",
            t: "menu.repeater_list",
            icon: "rjucd-router",
            path: "wifi_repeater",
            checkName: 'wifiRepeater',
            devAllow: ["ehr"],
            //compPath: () => import("@/views/common/aplist/List"),
          },
          {
            label: "安全管理",
            t: "menu.sec_manage",
            icon: "rjucd-safe",
            path: "security",
            showChilds: true,
            children: [
              {
                label: "ARP列表",
                t: "menu.arp_list",
                path: "security_arp",
                checkName: 'securityArp',
                //compPath: () => import("@/views/common/security/Arp")
              },
              {
                label: "MAC过滤",
                t: "menu.mac_filter",
                path: "security_macfilter",
                checkName: 'securityMacfilter',
                devAllow: ["egw", "eap"],
                //compPath: () => import("@/views/common/security/MacFilter"),
              },
              {
                label: "防攻击",
                t: "menu.attack_filter",
                path: "security_attack",
                checkName: 'securityAttack',
                devAllow: ["egw", "ehr"],
                //compPath: () => import("@/views/egw/advanced/Attack"),
              }
            ]
          },
          {
            label: "行为管理",
            t: "menu.behavior_manage",
            icon: "rjucd-factoranalysis",
            path: "behavior",
            checkName: 'behavior',
            devAllow: ["egw"],
            showChilds: true,
            children: [
              {
                label: "应用控制",
                t: "menu.app_ctrl",
                path: "behavior_app",
                checkName: 'behaviorApp',
                //compPath: () => import("@/views/egw/behavior/ApplicationControl"),
              },
              {
                label: "网站管理",
                t: "menu.site_manage",
                path: "behavior_site",
                checkName: 'behaviorSite',
                //compPath: () => import("@/views/egw/behavior/SiteManage"),
              },
              {
                label: "QQ管理",
                t: "menu.qq_manage",
                path: "behavior_qq",
                checkName: 'behaviorQQ',
                //compPath: () => import("@/views/egw/behavior/QqBwList"),
              },
              {
                label: "访问控制",
                t: "menu.access_ctrl",
                path: "behavior_acl",
                checkName: 'behaviorAcl',
                //compPath: () => import("@/views/egw/behavior/AccessCtrl"),
              },
              {
                label: "地址管理",
                t: "menu.addr_manage",
                path: "behavior_addr",
                checkName: 'behaviorAddr',
                //compPath: () => import("@/views/egw/behavior/AddrManage"),
              },
              {
                label: "时间管理",
                t: "menu.date_manage",
                path: "behavior_date",
                checkName: 'behaviorDate',
                //compPath: () => import("@/views/egw/behavior/DateManage"),
              }
            ]
          },
          {
            label: "VPN管理",
            t: "menu.vpn_manage",
            icon: "rjucd-vpn",
            path: "vpn",
            devAllow: ["egw"],
            showChilds: true,
            children: [
              {
                label: "IPSec设置",
                t: "menu.ipsec_cfg",
                path: "vpn_ipsec",
                checkName: 'vpnIpsec',
                //compPath: () => import("@/views/egw/vpn/IPSec"),
              },
              {
                label: "L2TP",
                path: "vpn_l2tp",
                checkName: 'vpnL2tp',
                //compPath: () => import("@/views/egw/vpn/L2tp"),
              },
              {
                label: "PPTP",
                path: "vpn_pptp",
                checkName: 'vpnPPtp',
                //compPath: () => import("@/views/egw/vpn/Pptp"),
              },
              {
                label: "用户管理",
                t: "menu.user_manage",
                path: "vpn_user",
                checkName: 'vpnUser',
                //compPath: () => import("@/views/egw/vpn/Manager"),
              }
            ]
          },
          {
            label: "高级管理",
            t: "menu.advance_manage",
            icon: "rjucd-toolbox",
            path: "advanced",
            showChilds: true,
            children: [
              {
                label: "路由设置",
                t: "menu.route_cfg",
                path: "advanced_route",
                checkName: 'advancedRoute',
                devAllow: ["egw"],
                //compPath: () => import("@/views/egw/advanced/Route"),
              },
              {
                label: "流控设置",
                t: "menu.flow_cfg",
                path: "advanced_flowctrl",
                checkName: 'advancedFlowctrl',
                devAllow: ["egw", "ehr"],
                //compPath: () => import("@/views/egw/advanced/Flowctrl"),
              },
              {
                label: "PPPoE服务器",
                t: "menu.pppoe_server",
                path: "advanced_pppoe",
                checkName: 'advancedPppoe',
                devAllow: ["egw"],
                //compPath: () => import("@/views/egw/advanced/Pppoe"),
              },
              {
                label: "认证设置",
                t: "menu.auth_cfg",
                path: "advanced_authentication",
                checkName: 'advancedAuthentication',
                devAllow: ["egw"],
                //compPath: () => import("@/views/egw/advanced/Auth"),
              },
              {
                label: "连接数限制",
                t: "menu.connect_cnt",
                path: "advanced_ipconn",
                checkName: 'advancedIpconn',
                devAllow: ["egw"],
                //compPath: () => import("@/views/egw/advanced/Ipconn"),
              },
              {
                label: "端口映射",
                t: "menu.port_map",
                path: "advanced_nat",
                checkName: 'advancedNat',
                devAllow: ["egw", "ehr"],
                //compPath: () => import("@/views/egw/advanced/Nat"),
              },
              {
                label: "动态域名",
                t: "menu.dynamic_domain",
                path: "advanced_ddns",
                checkName: 'advancedDdns',
                devAllow: ["egw", "ehr"],
                //compPath: () => import("@/views/egw/advanced/Ddns"),
              },
              {
                label: "UPnP设置",
                t: "menu.advanced_upnp",
                path: "advanced_upnp",
                checkName: 'advancedUpnp',
                devAllow: ["ehr"],
                //compPath: () => import("@/views/ehr/advanced/Upnp"),
              },
              {
                label: "本机DNS",
                t: "menu.dns",
                path: "advanced_dns",
                //compPath: () => import("@/views/common/advanced/Dns")
              },
              {
                label: "易联开关",
                t: "menu.elinksync",
                path: "elink_sync",
                checkName: 'elinkSync',
                devAllow: ["ehr"],
                //compPath: () => import("@/views/ehr/advanced/Elinksync"),
              },
              {
                label: "硬件加速",
                t: "menu.hardSpeed",
                path: "hardware_speed",
                checkName: 'hardwareSpeed',
                devAllow: ["ehr"],
                //compPath: () => import("@/views/ehr/advanced/HardwareSpeed"),
              },
              {
                label: "POE设置",
                t: "menu.poeConf",
                path: "poe_power",
                checkName: 'poePower',
                devAllow: ["eap"],
                //compPath: () => import("@/views/common/advanced/PoePower"),
              },
              {
                label: "其它设置",
                t: "menu.certify",
                path: "advanced_certify",
                checkName: 'advancedCertify',
                //compPath: () => import("@/views/common/advanced/Certify"),
              }
            ]
          },
          {
            label: "故障诊断",
            t: "menu.diagnose",
            icon: "rjucd-diagnosis",
            path: "diagnose",
            showChilds: true,
            children: [
              {
                label: "网络自检",
                t: "menu.net_check",
                path: "diagnose_network",
                //compPath: () => import("@/views/common/diagnose/NetworkCheck"),
              },
              {
                label: "故障告警",
                t: "menu.warn_alarm",
                path: "diagnose_alarm",
                checkName: 'diagnoseAlarm',
                //compPath: () => import("@/views/common/diagnose/Alarm"),
              },
              {
                label: "网络工具",
                t: "menu.net_tool",
                path: "diagnose_tool",
                //compPath: () => import("@/views/common/diagnose/Tool")
              },
              {
                label: "抓包诊断",
                t: "menu.pack_check",
                path: "diagnose_package",
                checkName: 'diagnosePackage',
                //compPath: () => import("@/views/common/diagnose/Package"),
              },
              {
                label: "故障收集",
                t: "menu.collect_err",
                path: "diagnose_tech",
                devAllow: ["egw", "eap"],
                //compPath: () => import("@/views/common/diagnose/Tech"),
              },
              {
                label: "系统日志",
                t: "menu.system_log",
                path: "syslog",
                checkName: 'systemLog',
                //compPath: () => import("@/views/common/diagnose/Log"),
              }
            ]
          },
          {
            label: "系统管理",
            t: "menu.sys_manage",
            icon: "rjucd-systemset",
            path: "systool",
            showChilds: true,
            children: [
              {
                label: "系统时间",
                t: "menu.sys_date",
                path: "systool_date",
                checkName: 'systoolDate',
                //compPath: () => import("@/views/common/systool/Date"),
              },
              {
                label: "登录管理",
                t: "menu.login_management",
                path: "systool_pass_session",
                //compPath: () => import("@/views/common/systool/PassSession")
              },
              {
                label: "配置管理",
                t: "menu.cfg_manage",
                path: "systool_recover",
                //compPath: () => import("@/views/common/systool/Recover")
              },
              {
                label: "系统升级",
                t: "menu.sys_upgrade",
                path: "systool_upgrade",
                //compPath: () => import("@/views/common/systool/Upgrade")
              },
              // {
              //   label: "账户管理",
              //   t: "menu.account",
              //   path: "egw_account",
              //   checkName: 'egwAccount',
              //   devAllow: ["egw"],
              //   //compPath: () => import("@/views/egw/Account"),
              // },
              {
                label: "LED灯设置",
                t: "menu.led_cfg",
                path: "cfg_led",
                devAllow: ["ehr"],
                //compPath: () => import("@/views/common/wireless/Led")
              },
              {
                label: "设备重启",
                t: "menu.reboot",
                path: "time_reload",
                //compPath: () => import("@/views/common/systool/Reload")
              }
            ]
          }
        ]
      },
      {
        label: "无线管理",
        t: "menu.wifi_manage",
        icon: "rjucd-wifi",
        path: "wifi",
        checkName: 'wireless',
        devAllow: ["egw", "eap"],
        showChilds: true,
        children: [
          {
            label: "AP列表",
            t: "menu.ap_list",
            path: "wifi_ap",
            //compPath: () => import("@/views/common/aplist/List")
          },
          {
            label: "无线设置",
            t: "menu.wifi_cfg",
            path: "wifi_setting",
            //compPath: () => import("@/views/common/wireless/wifiSetting")
          },
          {
            label: "无线用户",
            t: "menu.wifi_user",
            path: "wifi_sta",
            //compPath: () => import("@/views/common/wireless/components/WirelessUser")
          },
          {
            label: "黑白名单",
            t: "menu.wifiBwlist",
            path: "wifi_bwlist",
            //compPath: () => import("@/views/common/wireless/MacFilter")
          },
          {
            label: "射频设置",
            t: "menu.wifi_advanced",
            path: "wifi_advanced",
            //compPath: () => import("@/views/common/wireless/components/SsidRadioTab")
          },
          {
            label: "AP有线口",
            t: "menu.wifiless_ap",
            path: "wifi_wirelan",
            //compPath: () => import("@/views/common/wireless/WireLan")
          },
          {
            label: "LED灯设置",
            t: "menu.led_cfg",
            path: "wifi_led",
            //compPath: () => import("@/views/common/wireless/Led")
          },
          {
            // "一键网优" EGW和EAP做为隐藏页面但打包
            ...wioMenu,
            hidden: true,
            devAllow: ["egw", "eap"]
          },
          {
            label: "DHCP安全",
            t: "menu.ac_dhcp",
            path: "wifi_dhcp",
            checkName: 'wifiDhcp',
            //compPath: () => import("@/views/common/wireless/Dhcplist"),
          },
          {
            label: "网络安全",
            t: "menu.net_sec",
            path: "wifi_net",
            checkName: 'wifiNet',
            //compPath: () => import("@/views/common/wireless/Net"),
          }
        ]
      },
      {
        label: "交换管理",
        t: "menu.switch_manage",
        icon: "rjucd-switch",
        path: "switch_list",
        checkName: 'switchList',
        devAllow: ["egw", "eap", "msw"],
        //compPath: () => import("@/views/common/switch/List"),
      },
      {
        label: "整网管理",
        t: "menu.whole_manage",
        icon: "rjucd-systemset",
        path: "manage",
        checkName: 'webManage',
        devAllow: ["egw", "eap", "msw"],
        showChilds: true,
        children: [
          {
            label: "整网时间",
            t: "menu.whole_date",
            path: "systool_date",
            //compPath: () => import("@/views/common/systool/Date")
          },
          {
            label: "整网密码",
            t: "menu.whole_pass",
            path: "systool_password",
            //compPath: () => import("@/views/common/systool/passsession/Password")
          },
          {
            label: "整网定时重启",
            t: "menu.whole_reboot_time",
            path: "time_reload",
            //compPath: () => import("@/views/common/systool/reload/TimeReloadTab")
          },
          {
            label: "整网重启/恢复",
            t: "menu.whole_reboot_recover",
            path: "manager",
            //compPath: () => import("@/views/common/systool/Manager")
          }
        ]
      },
      {
        label: "隐藏菜单",
        hidden: true,
        children: [
          {
            label: "网络邻居列表",
            path: "network_neighbor",
            checkName: 'networNeighbor',
            //compPath: () => import("@/views/common/menuout/Neighbor"),
          },
          {
            label: "流表信息页面",
            path: "menuout_audit",
            checkName: 'menuoutAudit',
            devAllow: ["egw"],
            //compPath: () => import("@/views/egw/menuout/Audit"),
          },
          {
            label: "流表报文数页面",
            path: "menuout_conntrace",
            checkName: 'menuoutConntrace',
            devAllow: ["egw"],
            //compPath: () => import("@/views/egw/menuout/Conntrace"),
          },
          {
            label: "测试页面（简洁版）",
            path: "menuout_speedtest",
            checkName: 'menuoutSpeedtest',
            // devAllow: ["egw", "eap"],
            devAllow: ["egw"],
            //compPath: () => import("@/views/egw/menuout/SpeedTest-eap"),
          }
        ]
      }
    ]
  },
  {
    label: "全网配置",
    t: "menu.quickmacc",
    path: "quickmacc",
    checkName: 'quickmacc',
    devAllow: ["egw", "eap", "msw"],
    //compPath: () => import("@/views/common/Quickmacc"),
  },
  {
    label: "EHR家用PC",
    path: "ehr",
    checkName: 'ehr',
    devAllow: ["ehr"],
    //compPath: () => import("@/views/common/AdminMain"),
    hidden: true,
    children: [
      {
        label: "首页",
        icon: "rjucd-home",
        path: "home_overview",
        ////compPath : "views/common/overview/OverviewDev"
        //compPath: () => import("@/views/ehr/MainIndex")
      },
      {
        label: "终端管理",
        icon: "rjucd-user",
        path: "home_user",
        //compPath: () => import("@/views/ehr/OnlineUser")
      },
      {
        label: "上网设置",
        icon: "rjucd-network",
        path: "home_network",
        //compPath: () => import("@/views/ehr/mobile/quick/MNetwork")
      },
      {
        label: "无线设置",
        icon: "rjucd-wifi",
        path: "home_wifi",
        //compPath: () => import("@/views/ehr/mobile/quick/MWifi")
      }
    ]
  },
  {
    //label : "手机相关界面", //手机端页面只做声明路由，菜单前端控制
    path: "m",
    hidden: true,
    devAllow: ["ehr"],
    children: [
      {
        path: "meshset",
        checkName: 'ehrQuick',
        //compPath: () => import("@/views/ehr/mobile/quick/mesh/Mmesh"),
        //-----------------新版家用开局-------------
        children: [
          // {
          //   //label : "路由器未插网线",
          //   path: "checWan",
          //   //compPath: () => import("@/views/ehr/mobile/quick/CheckWan"),
          // },
          {
            //label : "一键易联",
            path: "elink",
            //compPath: () => import(/* webpackChunkName: 'meshset' */ "@/views/ehr/mobile/quick/mesh/MElink"),
          },
          {
            //label : "一键易联结果",
            path: "elinkRes",
            //compPath: () => import(/* webpackChunkName: 'meshset' */ "@/views/ehr/mobile/quick/mesh/MElinkRes"),
          },
          {
            //label : "无线中继",
            path: "wifiRepeat",
            //compPath: () => import(/* webpackChunkName: 'meshset' */"@/views/ehr/mobile/quick/mesh/MWifiRepeat"),
          },
          {
            //label : "无线中继配置",
            path: "wifiRepeatSet",
            //compPath: () => import(/* webpackChunkName: 'meshset' */"@/views/ehr/mobile/quick/mesh/MWifiRepeatSet"),
          },
          {
            //label : "无线中继结果",
            path: "wifiRepeatRes",
            //compPath: () => import(/* webpackChunkName: 'meshset' */"@/views/ehr/mobile/quick/mesh/MWifiRepeatRes"),
          },
          {
            //label : "主路由易联从设备",
            path: "repeatElink",
            //compPath: () => import(/* webpackChunkName: 'meshset' */"@/views/ehr/mobile/quick/mesh/MRepeatElink"),
          },
          {
            //label : "主路由易联从设备结果",
            path: "repeatElinkR",
            //compPath: () => import(/* webpackChunkName: 'meshset' */"@/views/ehr/mobile/quick/mesh/MRepeatElinkRes"),
          }
        ]
      },
      {
        path: "quick",
        checkName: 'ehrQuick',
        //compPath: () => import("@/views/ehr/mobile/Mquick")
      },
      {
        path: "main",
        //compPath: () => import("@/views/ehr/mobile/Mapp"),
        children: [
          {
            path: "homepage",
            hidden: true,
            children: [
              {
                path: "",
                //compPath: () => import("@/views/ehr/mobile/main/MHomepage")
              },
              {
                path: "overview",
                //compPath: () => import("@/views/common/overview/components/SysInfo")
              },
              {
                //label : "系统状态",
                path: "qrcode",
                //compPath: () => import("@/views/common/header/Qrcode")
              },
              {
                //label : "端口信息",
                path: "port",
                //compPath: () => import("@/views/common/overview/components/PortStatus")
              }
            ]
          },
          {
            //label : "终端管理",
            path: "userlist",
            children: [
              {
                //终端列表
                path: "",
                //compPath: () => import("@/views/ehr/mobile/main/user/MUserList")
              },
              {
                // "终端详情",
                path: "userDetail",
                //compPath: () => import("@/views/ehr/mobile/main/user/MUserDetail")
              },
              {
                // "上网守护",
                path: "userControl",
                checkName: 'userControl',
                //compPath: () => import("@/views/ehr/mobile/main/user/MUserControl"),
              },
              {
                // "上网守护详情",
                path: "userControlDetail",
                checkName: 'userControlDetail',
                //compPath: () => import("@/views/ehr/mobile/main/user/MUserControlDetail"),
              },
              // ------------新版儿童上网守护--------
              {
                // "开启上网守护",
                path: "addUserControl",
                //compPath: () => import("@/views/ehr/mobile/main/user/MAddUserControl"),
              },
              {
                // "上网守护主页",
                path: "userControlMain",
                //compPath: () => import("@/views/ehr/mobile/main/user/MUserControlMain"),
              },
              {
                // "上网守护应用列表",
                path: "appList",
                //compPath: () => import("@/views/ehr/mobile/main/user/MAppList"),
              },
              {
                // "上网守护禁用网址列表",
                path: "websiteList",
                //compPath: () => import("@/views/ehr/mobile/main/user/MWebsiteList"),
              },
              {
                // "上网守护允许时间详情",
                path: "allowTimeDetail",
                //compPath: () => import("@/views/ehr/mobile/main/user/MUserAllowTimeDetail"),
              }
            ]
          },
          {
            //label : "网络",
            path: "network",
            checkName: 'ehrNetwork',
            //compPath: () => import("@/views/ehr/mobile/quick/MNetwork"),
          },
          {
            //label : "Wi-Fi",
            path: "wifi",
            checkName: 'ehrWifi',
            children: [
              {
                path: "",
                ////compPath : "views/ehr/mobile/quick/MWifi"
                //compPath: () => import("@/views/ehr/mobile/main/wifi/WifiList")
              },
              {
                path: "mwifi",
                //compPath: () => import("@/views/ehr/mobile/main/wifi/MWifi")
              }
            ]
          },
          {
            label: "更多设置",
            path: "more",
            children: [
              {
                path: "",
                checkName: 'moreMenu',
                //compPath: () => import("@/views/ehr/mobile/main/MMoreMenu")
              },
              {
                //label : "中继设置",
                path: "devmode",
                //compPath: () => import("@/views/ehr/DevMode")
              },
              {
                //label : "网络自检",
                path: "diagnosis",
                //compPath: () => import("@/views/common/diagnose/NetworkCheck")
              },
              // {
              //   label: "一键无线优化",
              //   path: "wifiOptimize",
              //   checkName: 'wifiOptimize',
              //   //compPath: () => import("@/views/ehr/mobile/main/wifi/OptimizeTab"),
              // },
              {
                //label : "漫游设置",
                path: "roam",
                //compPath: () => import("@/views/common/wireless/components/RadioTab")
              },
              {
                //label : "主设备-国家码&频宽设置",
                path: "country",
                checkName: 'ehrCountry',
                //compPath: () => import("@/views/common/wireless/components/SsidRadioTab"),
              },
              {
                //label : "信道功率",
                path: "radio",
                //compPath: () => import("@/views/common/wireless/components/SsidRadioTab")
              },
              {
                //label : "游戏加速",
                path: "xpress",
                checkName: 'ehrXpress',
                //compPath: () => import("@/views/ehr/mobile/main/wifi/XpressTab"),
              },
              {
                //label : "健康模式",
                path: "mhealthy",
                checkName: 'ehrMhealthy',
                children: [
                  {
                    path: "",
                    //compPath: () => import("@/views/ehr/mobile/main/MHealthyMenu")
                  },
                  {
                    //label : "健康模式",
                    path: "healthy",
                    //compPath: () => import("@/views/common/wireless/components/HealthyTab")
                  }
                ]
              }
            ]
          },
          {
            label: "子路由管理",
            path: "aplist",
            checkName: 'wifiRepeater',
            children: [
              {
                path: "",
                //compPath: () => import("@/views/ehr/mobile/main/ap/MApList")
              },
              {
                //label : "信道功率",
                path: "radio",
                //compPath: () => import("@/views/common/wireless/components/RadioTab")
              },
              {
                //label : "设备详情",
                path: "detail",
                //compPath: () => import("@/views/ehr/mobile/main/components/MDetailList")
              }
            ]
          },
          {
            //label : "系统管理",
            path: "system",
            children: [
              {
                path: "",
                //compPath: () => import("@/views/ehr/mobile/main/MSystemMenu")
              },
              {
                //label : "管理密码",
                path: "password",
                checkName: 'ehrPassword',
                //compPath: () => import("@/views/common/systool/passsession/Password"),
              },
              {
                label: "网络工具",
                path: "diagnose_tool",
                //compPath: () => import("@/views/common/diagnose/Tool")
              },
              {
                //label : "系统时间",
                t: "menu.sys_date",
                path: "systool_date",
                checkName: 'ehrSystoolDate',
                //compPath: () => import("@/views/common/systool/Date"),
              },
              {
                //label : "定时重启",
                path: "clock",
                checkName: 'ehrClock',
                //compPath: () => import("@/views/common/systool/reload/TimeReloadTab"),
              },
              {
                //label : "重启",
                path: "reload",
                //compPath: () => import("@/views/common/systool/reload/ReloadTab")
              },
              {
                //label : "恢复出厂设置",
                path: "recover",
                //compPath: () => import("@/views/common/systool/recover/RecoverTab")
              },
              {
                //label : "在线升级",
                path: "upgrade",
                //compPath: () => import("@/views/common/systool/upgrade/Online")
                ////compPath : "views/common/systool/Upgrade"
              }
            ]
          }
        ]
      }
    ]
  }
]

export default menuConfig;