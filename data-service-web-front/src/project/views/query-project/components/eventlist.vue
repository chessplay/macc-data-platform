<template>
  <div class="eventbox">
    <!-- 过滤条件展示盒子 -->
    <div class="filterbox">
      <div class="timefilter">
        <a-form layout="inline" style="float:left">
          <a-form-item label="用户ID" class="labelclass">
            <a-input v-model="uid" style="width:150px" />
          </a-form-item>
          <a-form-item label="分类">
            <div style="width:340px;padding:4px 11px" class="labelclass">
              <a-select
                style="padding:0 15px 0 0;width:54%"
                @change="clickType1"
                v-model="type1"
              >
                <a-select-option value="">
                  全部
                </a-select-option>
                <a-select-option :value="k" v-for="(v, k, i) in type" :key="i">
                  {{ k }}
                </a-select-option>
              </a-select>
              <a-select v-model="type2" style="width:48%">
                <a-select-option value="">
                  全部
                </a-select-option>
                <a-select-option
                  :value="v"
                  v-for="(v, i) in type[type1]"
                  :key="i"
                >
                  {{ v }}
                </a-select-option>
              </a-select>
            </div>
          </a-form-item>
          <a-form-item>
            <label style="float:left"
              >时间：
              <a-radio-group v-model="modelday">
                <a-radio-button :value="7" @click="changetime(7)"
                  >近7天</a-radio-button
                >
                <a-radio-button :value="14" @click="changetime(14)"
                  >近14天</a-radio-button
                >
                <a-radio-button :value="30" @click="changetime(30)"
                  >近30天</a-radio-button
                >
                <a-radio-button :value="1000" @click="changetime(1000)"
                  >全部</a-radio-button
                >
                <a-range-picker @change="changeDate" class="inputtime">
                  <span>
                    {{
                      timelist.time2
                        ? timelist.time1 + "~" + timelist.time2
                        : "自定义时间"
                    }}
                  </span>
                </a-range-picker>
              </a-radio-group>
            </label>
          </a-form-item>
          <a-form-item>
            <a-button
              style="margin: 0 0 0 20px"
              type="primary"
              @click="submait"
            >
              搜索
            </a-button>
          </a-form-item>
        </a-form>
      </div>
      <div class="timestep">
        <a-button
          style=" margin-top: -15px;"
          type="primary"
          shape="circle"
          icon="left"
          @click="backpage"
          :disabled="daylist.length == 1 || steppage == daylist.length - 1"
        />
        <a-steps
          progress-dot
          :current="current"
          @change="changeDay"
          size="small"
          style="width:90%"
          class="timestepchange"
        >
          <a-step
            v-for="(v, i) in daylist[steppage]"
            :key="i"
            :title="v"
            :status="i == current ? 'error' : 'process'"
          />
        </a-steps>
        <a-button
          type="primary"
          shape="circle"
          icon="right"
          style="margin-left:30px; margin-top: -15px;"
          :disabled="daylist.length == 1 || steppage == 0"
          @click="uppage"
        />
      </div>
    </div>
    <!-- 事件列表展示盒子 -->
    <div class="listbox">
      <div>
        <a-list
          v-if="listData.length > 0"
          item-layout="horizontal"
          :data-source="listData"
          class="eventlist"
          @scroll="scrollGet($event)"
        >
          <a-list-item
            slot="renderItem"
            :key="item.uuid"
            slot-scope="item"
            :class="changekey == item.uuid ? 'changeitem' : 'item'"
            @click="changeEventId(item)"
            >{{ moment(item.begin_date).format("YYYY-MM-DD HH:mm:ss") + "---"
            }}{{ item.event_name }}
          </a-list-item>
        </a-list>
        <a-empty v-else class="nullData" />
      </div>
    </div>
    <!-- 事件详情展示盒子 -->
    <div class="databox">
      <a-descriptions
        :column="4"
        bordered
        v-if="Object.keys(eventData).length > 0"
      >
        <a-descriptions-item
          v-for="(v, k, i) in eventitemlist"
          :key="i"
          :label="v.name"
          :span="v.span"
        >
          <div v-if="v.type === 'string'">
            {{ eventData[k] }}
          </div>
          <div v-else-if="v.type === 'cac'">
            <div v-if="eventData[v.value[1]] == eventData[v.value[2]]">
              {{ eventData[v.value[0]] + " " + eventData[v.value[1]] }}
            </div>
            <div v-else>
              {{
                eventData[v.value[0]] +
                  " " +
                  eventData[v.value[1]] +
                  " " +
                  eventData[v.value[2]]
              }}
            </div>
          </div>
          <!-- json数据展示
          <pre v-html="jsonData"
            class="showJsonBox"
          ></pre>-->
          <a-textarea
            v-else-if="v.type === 'json'"
            v-model="jsonData"
            read-only
            style="margin: 15px 0 0 0"
            :auto-size="{ minRows: 5, maxRows: 9 }"
          />
        </a-descriptions-item>
      </a-descriptions>
      <a-empty v-else />
    </div>
  </div>
</template>
<script>
import ApiService from "@/services/baseService";
import moment from "moment";

export default {
  name: "eventbox",
  props: {
    gid: {
      typeof: String,
    }, //由父组件传入的项目id
    type: {
      typeof: Object,
    }, //由父组件传入的type1,tpye2
  },
  data() {
    return {
      type1: "", //类别1
      type2: "", //类别2
      uid: "", //用户ID
      listData: [], //事件列表
      changekey: "", //选中的事件id
      eventListDataRow: {
        modelId: "zg-network-event",
        filters: [
          {
            alias: "groupId",
            op: "EQ",
            value: [],
          },
          {
            alias: "begin_date",
            op: "BETWEEN",
            value: [
              "1971-01-01 00:00:00",
              moment(new Date().setDate(new Date().getDate() + 1)).format(
                "YYYY-MM-DD"
              ) + " 00:00:00",
            ],
          },
        ],
        orderby: [
          {
            alias: "begin_date",
            sortType: "desc",
          },
        ],
        skip: 0,
        limit: 1000,
      }, //获取事件列表的data-row
      eventData: {}, //事件详情
      eventDataRow: {
        modelId: "zg-single-event-detail",
        filters: [
          {
            alias: "uuid",
            op: "EQ",
            value: [],
          },
          {
            alias: "begin_date",
            op: "EQ",
            value: [],
          },
          {
            alias: "event_name",
            op: "EQ",
            value: [],
          },
        ],
        limit: 1,
      }, //获取事件信息的data-row
      eventitemlist: {
        bs: { name: "浏览器品牌：", type: "string", span: 2 },
        bv: { name: "浏览器版本：", type: "string", span: 2 },
        device_id: { name: "设备ID：", type: "string", span: 2 },
        macc_user_id: { name: "macc用户ID：", type: "string", span: 2 },
        sourcename: { name: "来源域名：", type: "string", span: 2 },
        platform: { name: "平台：", type: "string", span: 2 },
        sourceURL: { name: "来源URL：", type: "string", span: 2 },
        currentURL: { name: "当前URL：", type: "string", span: 2 },
        cac: {
          name: "地区：",
          type: "cac",
          span: 2,
          value: ["country", "area", "city"],
        },
        os: { name: "操作系统：", type: "string", span: 2 },
        initialsourceURL: { name: "初始来源URL：", type: "string", span: 2 },
        ip_str: { name: "IP：", type: "string", span: 2 },
        width: { name: "屏幕宽度：", type: "string", span: 2 },
        height: { name: "屏幕高度：", type: "string", span: 2 },
        params: { name: "拓展信息：", type: "json", span: 4 },
      }, //事件详情项对应名称
      jsonData: "", //事件详情的json属性
      timelist: {
        time1: undefined, //自定义起始时间
        time2: undefined, //自定义结束时间
      },
      current: -1, //选中的时间节点
      steppage: 0, //时间轴分页
      daylist: [], //时间轴数据
      modelday: 7,
      pageIndex: 1,
      totalCount: 0,
    };
  },
  watch: {
    /**
     * 监听由父组件传入的项目id,根据id获取事件列表;
     * @param {String} gid 项目id
     */
    gid(gid) {
      this.timelist.time2 = undefined;
      this.eventData = {};
      this.current = -1;
      if (gid == "") {
        this.listData = [];
      } else {
        this.listData = [];
        this.eventListDataRow.filters[0].value = [gid];
        let time = new Date().getTime();
        // this.eventListDataRow.filters[1].value[0] = "1971-01-01 00:00:00";
        // (this.eventListDataRow.filters[1].value[1] =
        //   moment(new Date().setDate(new Date().getDate() + 1)).format(
        //     "YYYY-MM-DD"
        //   ) + " 00:00:00"),
        this.eventListDataRow.filters[1].value[0] = moment(
          time - 24 * 60 * 60 * 1000 * 6
        ).format("YYYY-MM-DD 00:00:00");
        this.eventListDataRow.filters[1].value[1] = moment(time).format(
          "YYYY-MM-DD HH:mm:ss"
        );
        this.eventListDataRow.skip = 0;
        this.pageIndex = 1;
        this.querylist(this.eventListDataRow, 1);
      }
    },
  },
  methods: {
    moment,
    /**
     * 类别1事件:选择一个类别作为过滤条件去获取类别2
     * @param {String} v 类别1
     */
    clickType1(v) {
      this.type1 = v;
      this.type2 = "";
    },
    /**
     * 表单提交事件
     */
    submait() {
      this.eventData = {};
      this.listData = [];
      if (this.eventListDataRow.filters[0].value.length == 0) {
        return;
      } else {
        let dataRow = this.setDataRowfilter(this.eventListDataRow);
        this.pageIndex = 1;
        this.querylist(dataRow, 1);
      }
    },
    /**
     * 设置dataRow
     * @param {Object} oldDataRow 原始dataRow
     * @return {Object} dataRow 新dataRow
     */
    setDataRowfilter(oldDataRow) {
      let dataRow = JSON.parse(JSON.stringify(oldDataRow));
      if (this.uid !== "") {
        dataRow.filters.push({
          //alias: 'user_id',
          //alias: "macc_user_id",
          alias: "maccUserId",
          op: "EQ",
          value: [this.uid],
        });
      }
      if (this.type1 !== "") {
        dataRow.filters.push({
          alias: "type1",
          op: "EQ",
          value: [this.type1],
        });
      }
      if (this.type2 !== "") {
        dataRow.filters.push({
          alias: "type2",
          op: "EQ",
          value: [this.type2],
        });
      }
      return dataRow;
    },
    /**
     * 时间轴节点点击事件
     * @param {Number} value 时间轴的下标
     */
    changeDay(value) {
      this.current = value;
      this.listData = [];
      if (this.eventListDataRow.filters[0].value.length == 0) {
        return;
      } else {
        let day = this.daylist[this.steppage][value];
        let day2 =
          moment(new Date(day).setDate(new Date(day).getDate() + 1)).format(
            "YYYY-MM-DD"
          ) + " 00:00:00";
        this.eventListDataRow.filters[0].value = [this.gid];
        this.eventListDataRow.filters[1].value[0] = day + " 00:00:00";
        this.eventListDataRow.filters[1].value[1] = day2;
        this.eventListDataRow.skip = 0;
        let dataRow = this.setDataRowfilter(this.eventListDataRow);
        this.pageIndex = 1;
        this.querylist(dataRow, 1);
      }
    },
    /**
     * 时间轴的上一页点击事件
     */
    backpage() {
      if (this.steppage < this.daylist.length - 1) {
        this.steppage = this.steppage + 1;
        this.current = -1;
      }
    },
    /**
     * 时间轴的下一页点击事件
     */
    uppage() {
      if (this.steppage > 0) {
        this.steppage = this.steppage - 1;
        this.current = -1;
      }
    },
    /**
     * 选择一个时间段作为过滤条件去获取事件列表
     * @param {Number} day 近几天的数
     */
    changetime(day) {
      this.current = -1;
      this.listData = [];
      this.modelday = day;
      if (this.eventListDataRow.filters[0].value.length == 0) {
        return;
      } else {
        //this.activeKey = []
        let date = new Date();
        this.infoDate(date, day);
        date.setDate(date.getDate() - day + 1);
        let d = moment(date).format("YYYY-MM-DD");
        this.eventListDataRow.filters[1].value[0] = d + " 00:00:00";
        this.eventListDataRow.filters[1].value[1] = moment(new Date()).format(
          "YYYY-MM-DD HH:mm:ss"
        );
        this.eventListDataRow.skip = 0;
        let dataRow = this.setDataRowfilter(this.eventListDataRow);
        this.pageIndex = 1;
        this.querylist(dataRow, 1);
        this.timelist.time2 = undefined;
      }
    },
    /**
     * 选择一个时间段作为过滤条件去获取事件列表
     * @param {Object} data 数组里存放着起始时间和结束时间
     */
    changeDate(date) {
      this.modelday = 0;
      this.current = -1;
      this.listData = [];
      if (this.eventListDataRow.filters[0].value.length == 0) {
        return;
      } else {
        let d1 = moment(date[0]).format("YYYY-MM-DD");
        let d2 = moment(date[1]).format("YYYY-MM-DD");
        let count =
          parseInt(Date.parse(new Date(d2)) - Date.parse(new Date(d1))) /
          (1000 * 60 * 60 * 24);
        this.infoDate(d2, count + 1);
        let ndate = [d1, d2];
        this.timelist.time1 = ndate[0];
        this.timelist.time2 = ndate[1];
        this.eventListDataRow.filters[1].value = ndate;
        this.eventListDataRow.skip = 0;
        let dataRow = this.setDataRowfilter(this.eventListDataRow);
        this.pageIndex = 1;
        this.querylist(dataRow, 1);
      }
    },
    /**
     * 生成时间轴数据
     * @param {String} startTime 时间
     * @param {Number} dayCount 天数
     */
    infoDate(startTime, dayCount) {
      this.steppage = 0;
      this.daylist = [];
      let day = [];
      for (let i = 0; i < dayCount; i++) {
        let time = new Date(startTime);
        let presentTime = moment(time.setDate(time.getDate() - i)).format(
          "YYYY-MM-DD"
        );
        day.push(presentTime);
      }
      let num = dayCount;
      if (dayCount % 7 == 0) {
        num = day.length - 1;
      }
      for (let j = 0; j <= parseInt(num / 7); j++) {
        this.daylist.push(day.slice(j * 7, j * 7 + 7).sort());
      }
    },
    /**
     *滚动获取事件
     *@param {Object} e 页面dom对象
     */
    scrollGet(e) {
      if (
        e.path[0].scrollTop + e.path[0].clientHeight >=
        e.path[0].scrollHeight
      ) {
        if (this.listData.length >= this.totalCount) {
          return;
        }
        this.eventListDataRow.skip = this.listData.length;
        let dataRow = this.setDataRowfilter(this.eventListDataRow);
        this.pageIndex++;
        this.querylist(dataRow, this.pageIndex);
      }
    },
    /**
     * 获取事件列表
     * @param {Objrct} dataRow 相应的匹配规则
     */
    querylist(dataRow, pageIndex) {
      console.log(dataRow);
      let params = {};
      dataRow.filters.forEach((item) => {
        if (item.alias == "groupId" || item.alias == "maccUserId") {
          params[item.alias] = Number(item.value[0]);
        } else if (item.alias == "begin_date") {
          params.startTime = new Date(item.value[0]).getTime();
          params.endTime = new Date(item.value[1]).getTime();
        } else {
          params[item.alias] = item.value[0];
        }
      });
      ApiService.queryDynamic(dataRow.modelId, params, pageIndex, 25).then(
        (ret) => {
          this.listData = this.listData.concat(ret.dataList);
          this.totalCount = ret.totalCount;
        }
      );
    },
    /**
     * 获取事件详情
     * @param {Objrct} event 选中的事件信息
     */
    changeEventId(event) {
      this.changekey = event.uuid;
      this.eventDataRow.filters[0].value = [event.uuid];
      this.eventDataRow.filters[1].value = [event.begin_date];
      this.eventDataRow.filters[2].value = [event.event_name];
      let params = {
        uuid: event.uuid,
        event_name: event.event_name,
        groupId: Number(this.eventListDataRow.filters[0].value[0]),
      };
      ApiService.queryDynamic("zg-single-event-detail", params).then((ret) => {
        if (ret.hasOwnProperty("data")) {
          this.eventData = ret.data;
          this.jsonData = this.jsonFormat(
            JSON.parse('"' + this.eventData.params + '"')
          );
        }
      });
    },
    /**
     * 格式化json
     * @param {String} format json字符串
     */
    jsonFormat(format) {
      let msg = "",
        pos = 0,
        prevChar = "",
        outOfQuotes = true;
      for (let i = 0; i < format.length; i++) {
        let char = format.substring(i, i + 1);
        if (char == '"' && prevChar != "\\") {
          outOfQuotes = !outOfQuotes;
        } else if ((char == "}" || char == "]") && outOfQuotes) {
          msg += "\n";
          pos--;
          for (let j = 0; j < pos; j++) msg += "    ";
        }
        msg += char;
        if ((char == "," || char == "{" || char == "[") && outOfQuotes) {
          msg += "\n";
          if (char == "{" || char == "[") pos++;
          for (let k = 0; k < pos; k++) msg += "    ";
        }
        prevChar = char;
      }
      return msg;
    },
  },
  created() {
    this.infoDate(moment(new Date()).format("YYYY-MM-DD"), 7);
  },
};
</script>
<style lang="less" scoped>
@import "../style/eventList.less";
</style>
