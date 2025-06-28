/*
 * @Author: huangxuzhong
 * @Date: 2021-09-27 11:12:55
 * @LastEditTime: 2021-09-27 11:25:16
 * @LastEditors: Please set LastEditors
 * @Description:日期工具函数
 * @FilePath: \macc.admin.vue\src\utils\dateTools.js
 */
//日期处理添加方法

window.Date.prototype.DateAdd = function(strInterval, Number) {
  var dtTmp = this;
  switch (strInterval) {
    case "s":
      return new Date(Date.parse(dtTmp) + 1000 * Number);
    case "n":
      return new Date(Date.parse(dtTmp) + 60000 * Number);
    case "h":
      return new Date(Date.parse(dtTmp) + 3600000 * Number);
    case "d":
      return new Date(Date.parse(dtTmp) + 86400000 * Number);
    case "w":
      return new Date(Date.parse(dtTmp) + 86400000 * 7 * Number);
    case "q":
      return new Date(
        dtTmp.getFullYear(),
        dtTmp.getMonth() + Number * 3,
        dtTmp.getDate(),
        dtTmp.getHours(),
        dtTmp.getMinutes(),
        dtTmp.getSeconds()
      );
    case "m":
      return new Date(
        dtTmp.getFullYear(),
        dtTmp.getMonth() + Number,
        dtTmp.getDate(),
        dtTmp.getHours(),
        dtTmp.getMinutes(),
        dtTmp.getSeconds()
      );
    case "y":
      return new Date(
        dtTmp.getFullYear() + Number,
        dtTmp.getMonth(),
        dtTmp.getDate(),
        dtTmp.getHours(),
        dtTmp.getMinutes(),
        dtTmp.getSeconds()
      );
  }
};

window.Date.prototype.Format = function(formatStr) {
  var str = formatStr;
  //日/一/二/三/四/五/六
  var Week = [
    "星期日",
    "星期一",
    "星期二",
    "星期三",
    "星期四",
    "星期五",
    "星期六",
  ];

  //S:@ M:# W:` D:~ H:<
  //因为星期数的英文简写会被下面的正则替换掉，比如WED里的D会被/d|D/g匹配
  //所以先用特殊符号代替，最后再还原
  var WeekEn = ["@UN", "#ON", "TUE", "`E~", "T<U", "FRI", "@AT"];
  str = str.replace(/yyyy|YYYY/, this.getFullYear());
  str = str.replace(
    /yy|YY/,
    this.getYear() % 100 > 9
      ? (this.getYear() % 100).toString()
      : "0" + (this.getYear() % 100)
  );
  str = str.replace(
    /MM/,
    this.getMonth() >= 9
      ? (this.getMonth() + 1).toString()
      : "0" + (this.getMonth() + 1).toString()
  );
  str = str.replace(/M/g, this.getMonth() + 1);
  str = str.replace(/w|W/g, Week[this.getDay()]);
  str = str.replace(
    /dd|DD/,
    this.getDate() > 9 ? this.getDate().toString() : "0" + this.getDate()
  );
  str = str.replace(/d|D/g, this.getDate());
  str = str.replace(
    /hh|HH/,
    this.getHours() > 9 ? this.getHours().toString() : "0" + this.getHours()
  );
  str = str.replace(/h|H/g, this.getHours());
  str = str.replace(
    /mm/,
    this.getMinutes() > 9
      ? this.getMinutes().toString()
      : "0" + this.getMinutes()
  );
  str = str.replace(/m/g, this.getMinutes());
  str = str.replace(
    /ss|SS/,
    this.getSeconds() > 9
      ? this.getSeconds().toString()
      : "0" + this.getSeconds()
  );
  str = str.replace(/s|S/g, this.getSeconds());
  str = str.replace(/@/, "S");
  str = str.replace(/#/, "M");
  str = str.replace(/`/, "W");
  str = str.replace(/~/, "D");
  str = str.replace(/</, "H");
  return str;
};
