/**
 * 自定义工具包
 */
import axios from 'axios';
import SysConfig from './config';
const BASE_URL = "/datacenter"
// ajax请求相关工具包
let ReqUtil = {
    /**
     *
     * @param {
     * fn: 服务方法名称
     * p:  需要下发的参数
     * vm: vue对象，用于显示异常提醒等
     * } opts
     */
    request (opts, vm) {
        let pars = {
            timeout: 8000,
            method: 'post',
            url: '',
            data: {},
            params:{},
            baseURL : BASE_URL,
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        };
        Object.assign(pars, opts);
        if (pars.data) {
            pars.data = JSON.stringify(pars.data);
        }       
        // get请求增加随机数，防止缓存
        if (pars.method.toLowerCase === 'get') {
            pars.params.rt = (new Date()).getTime() + Math.random;
        }
        let insance = axios.create(pars);
        insance.interceptors.request.use(null, function (error) {
            // 请求失败异常处理
            vm && vm.$Message.warning({
                content: SysConfig.ConnectErr,
                duration: 3
            });
            return Promise.reject(error);
        });
        insance.interceptors.response.use(function (response) {
            let ret = response.data;
            ret.ok = ret.code === SysConfig.SuccessCode;// 返回操作是否成功的判断
            return ret;
        }, function (error) {
            let msg = '';
            let defErrTip = function () {
                // 服务端请求异常
                vm && vm.$Message.warning({
                    content: SysConfig.ServerErr,
                    duration: 3
                });
            };
            if (!error.response) { // 请求无响应
                defErrTip();
                return Promise.reject(error);
            }
            let status = error.response.status;
            if (typeof error.response.data === 'object' && error.response.data.msg) {
                msg = error.response.data.msg;
            }
            let errorPage = '';
            if (typeof error.response.data === 'object' && error.response.data && error.response.data.code === SysConfig.NoLoginCode) {
                // 跳转到登录页
                if (vm) {
                    vm.$store.commit('account/logout');
                    vm.$router.push('/login');
                }
                error.goToLogin = true;
                return Promise.reject(error);
            }  else if (status === 403) {
                errorPage = '/exception/403';
            } else if (status === 404) {
                errorPage = '/exception/404';
            } else if (status === 500) {
                errorPage = '/exception/500';
            }
            if (errorPage) {
                //vm && vm.$router.replace(errorPage);
                vm && vm.$router.push({path:errorPage});
                return Promise.reject(error);
            }
            defErrTip();
        });
        return insance.request(pars);
    },

    // get请求
    get (opts, vm) {
        let pars = {
            method: 'get'
        };
        Object.assign(pars, opts);
        return ReqUtil.request(pars, vm);
    },

    // post请求
    post (opts, vm) {
        let pars = {
            method: 'post'
        };
        Object.assign(pars, opts);
        return ReqUtil.request(pars, vm);
    },

    // delete请求
    delete(opts,vm){
        let pars = {
            method: 'delete'
        };
        Object.assign(pars, opts);
        return ReqUtil.request(pars, vm);
    }

};

/**
 * 对象辅助工具
 */
let ObjectUtil = {
    /**
     * 深度对象拷贝
     */
    deepCopy (sourceObj) {
        return JSON.parse(JSON.stringify(sourceObj));
    }
};
/**
 * 字符串工具类
 */
let StringUtil = {
    trim (str) { // 去前后空格
        var re = /^\s+|\s+$/g;
        return str.replace(re, '');
    },
    // 取html字符串对应的节点内容
    getHtmlBodyContent (html) {
        var reg = /<body[^>]*>([\s\S]*)<\/body>/;
        var result = reg.exec(html);
        if (result && result.length === 2) {
            return result[1];
        }
        return false;
    },

    // IP地址转换成整型
    ipToLong (ipstr) {
        var iplong;
        var ipArr = [];
        ipArr = ipstr.split('.');
        iplong = parseInt(ipArr[0], 10) * 256 * 256 * 256 + parseInt(ipArr[1], 10) * 256 * 256 + parseInt(ipArr[2], 10) *
			256 + parseInt(ipArr[3], 10);
        return iplong;
    },
    isIP (value) {
        var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g;
        if (StringUtil.trim(value) && re.test(value) &&
            RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256 &&
            RegExp.$1.length < 4 && RegExp.$2.length < 4 && RegExp.$3.length < 4 && RegExp.$4.length < 4) {
            return true;
        }
        return false;
    },
    isMask (value) {
        // 首先判断是否合法的IP
        if (!this.isIP(value)) {
            return false;
        }
        // 二进制码要相邻，即形如111...11000...0的形式
        // 将掩码转化成32无符号整型，取反为000...00111...1，然后再加1为00...01000...0，此时为2^n，如果满足就为合法掩码
        var num = this.ipToLong(value);
        num = ~num + 1;
        // 判断是否为2^n
        return (num & (num - 1)) == 0;
    },
    // 是否包含中文
    hasZh: function (str) {
        return /[\u4E00-\u9FA5]/.test(str);
    }

};
/**
 * 常用自定义验证方法
 */
let Validator = {
    mobile (rule, value, callback) {
        var re = /^1[0-9]{10}$/;
        if (value && !re.test(value)) {
            callback(new Error('请输入正确格式的手机号'));
        } else {
            callback();
        }
    },
    // 校验IP规则
    ip (rule, value, callback) {
        if (StringUtil.isIP(value)) {
            callback();
        } else {
            callback(new Error('IP地址格式错误'));
        }
    },

    // 校验掩码规则
    mask (rule, value, callback) {
        var re = /^(?:[1-9]|[12][0-9]|3[01])$/;
        if (value && !re.test(value)) {
            callback(new Error('掩码格式错误'));
        } else {
            callback();
        }
    },
    // 整数验证
    integer (rule, value, callback) {
        if (!Number.isInteger(value)) {
            callback(new Error('请输入数字值'));
        } else {
            callback();
        }
    },
    // 是否包含中文
    hasZh: function (rule, value, callback) {
        if (!StringUtil.hasZh(value)) {
            callback();
        } else {
            callback(new Error('包含中文字符'));
        }
    }
};

/**
 * 页面控制工具类
 */
let PageUtil = {
    NoAccessTip: SysConfig.NoAccessTip,
    // 当前用户拥有的该页面功能权限
    pageFuns (vm) {
        let menuCode = vm.$route.meta.menuCode;
        let userCurPageFun = vm.$store.getters["account/getMenuMap"];
        return userCurPageFun[menuCode] || {};
    },

    // 判断当前用户在该页面是否有某个功能权限
    getPageFun (vm, funCode) {        
        let pageFun = this.pageFuns(vm);
        return !!pageFun[funCode];
    }
    // 长字段渲染方法,加title，超出使用省略号替换 使用列属性tooltip替代
    /* tbLongFieldRender (h, params, fieldName) {
        let txt = params.row[fieldName];
        return h('span', {
            class: 'tb-long-field',
            domProps: {
                title: txt
            }
        }, txt);
    }, */
   
};

/**
 * 日期工具类
 */
let DateUtil = {
    /**
     *
     * @param {Date对象} date
     * @param {格式化字符串} fmt :如yyyy-MM-dd hh:mm:ss.S -》2006-07-02 08:09:04.423  yyyy-M-d h:m:s.S-》2006-7-2 8:9:4.18
     */
    formatDate (date, fmt) { // 格式化时间
        var o = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'h+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds(),
            'q+': Math.floor((date.getMonth() + 3) / 3),
            'S': date.getMilliseconds()
        };
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1,
                (date.getFullYear() + '').substr(4 -
                            RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp('(' + k + ')').test(fmt)) {
                fmt = fmt.replace(RegExp.$1,
                    RegExp.$1.length === 1 ? o[k] : ('00' + o[k])
                        .substr(('' + o[k]).length));
            }
        }
        return fmt;
    },

    /**
     * 获取当月第一天
     */
    getCurrentMonthFirst () {
        var date = new Date();
        date.setDate(1);
        return date;
    },

    /**
     * 获取当天
     */
    getNow () {
        return new Date();
    },

    /**
     * 判断日期是否在今天之前，含今天
     * @param {*} date
     */
    checkDateBeforeNow (date) {
        var fmt = 'yyyMMdd';
        var nowNum = parseInt(DateUtil.formatDate(new Date(), fmt));
        var dateNum = parseInt(DateUtil.formatDate(date, fmt));
        return dateNum <= nowNum;
    },

    /**
     * 获取日期的星期数，返回中文
     * @param {*} dayStr 如2018-06-15
     * return 星期一
     */
    getDayChn (dateStr) {
        return '星期' + '天一二三四五六 '.charAt(new Date(dateStr).getDay());
    }

};

export {ReqUtil, ObjectUtil, Validator, PageUtil, DateUtil, StringUtil};
