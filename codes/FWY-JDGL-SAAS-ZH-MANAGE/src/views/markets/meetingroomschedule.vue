<template>
  <a-card :bordered="false">
    <div class="course-week">
      <div class="week-top">
        <div class="week-btn-wrap">
          <span @click="getLastWeek">上周</span>
          <span @click="getCurWeek">本周</span>
          <span @click="getNextWeek">下周</span>
        </div>
        <span class="w-today-date"> {{ todayDate }}</span>
        <div class="w-choose-status">
          <div v-for="sta in cardStatus">
            <span class="square" :style="{ background: sta.color }"></span>
            <span class="title">{{ sta.title }}</span>
          </div>
        </div>
      </div>
      <div class="week-table">
        <div class="table-header">
          <div class="table-week">
            <template v-for="(item, index) of weeks">
              <span class="w-first" v-if="index === 0" :key="index">{{
                item
              }}</span>
              <span v-else :key="index">{{ item }}</span>
            </template>
          </div>
          <div class="w-table-date">
            <template v-for="(item, index) of months">
              <span class="w-first" v-if="index === 0" :key="index"> </span>
              <template v-else>
                <span
                  :key="index"
                  class="w-day-item"
                  :class="{ 'w-isCurDate': item && item.isCurDate }"
                >
                  {{
                    `${
                      item && item.isCurDate
                        ? (item && item.showDate + "(今天)") || ""
                        : (item && item.showDate) || ""
                    }`
                  }}
                </span>
              </template>
            </template>
          </div>
        </div>
        <div class="w-time-period-list">
          <ul
            class="w-time-period-row"
            v-for="(i, i_index) in planList2"
            :key="i_index"
          >
            <!--循环时段，看时段有多少个-->
            <template v-if="i.child && i.child.length > 0">
              <li
                class="w-time-period-col"
                v-for="(period, p_index) in i.child"
                :key="`period${p_index}`"
              >
                <!--第一列显示时段-->
                <div class="w-time-period">
                  <span v-if="p_index == 1" class="meeting-room-center">{{
                    i.meetingRoomName
                  }}</span
                  >{{ period.timePeriod }}
                </div>
                <!-- 后面显示周一到周日的计划-->
                <div class="w-row-day">
                  <!-- 循环显示每周的日期-->
                  <template v-for="(month, m_index) of months">
                    <!-- v-if="month" 去除数据处理的时候移除数组第一个为empty的问题-->
                    <div
                      v-if="month"
                      :key="`month${m_index}`"
                      class="w-things"
                      @click="handleCardDetail(month, period)"
                    >
                      <!-- 循环每个时间段的计划-->
                      <template v-for="(card, t_index) of period.schedule">
                        <template
                          v-if="card.key == month.date"
                          v-for="(single, sIndex) in card.value"
                        >
                          <div style="width: 80%">
                            <a-popover placement="rightTop">
                              <template slot="content">
                                <p>会议室：{{ i.meetingRoomName }}</p>
                                <p>会议主题：{{ single.roomSchedule.theme }}</p>
                                <p>公司名称：{{ single.roomSchedule.name }}</p>
                                <p>
                                  预定人：{{ single.roomSchedule.destinedName }}
                                </p>
                                <p>
                                  联系电话：{{ single.roomSchedule.mobile }}
                                </p>
                                <p>是否结账：未结账</p>
                                <p>备注：{{ single.roomSchedule.remark }}</p>
                              </template>
                              <template slot="title">
                                <span>详情</span>
                              </template>

                              <div
                                :key="`thing${sIndex}`"
                                class="w-thing-item"
                                @click.stop="handleDetail(single)"
                                :style="{
                                  background:
                                    cardStatus[single.detail.status].color,
                                }"
                              >
                                <span>{{ single.theme }}</span>
                              </div>
                            </a-popover>
                          </div>
                        </template>
                      </template>
                    </div>
                  </template>
                </div>
              </li>
            </template>
            <!-- <div class="w-noMore" v-else><span>暂无数据</span></div> -->
          </ul>
        </div>
      </div>
    </div>
    <bus-meeting-room-schedule-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></bus-meeting-room-schedule-modal>
    <bus-meeting-room-schedule-confirm-modal
      ref="modalBusMeetingRoomScheduleConfirm"
      @ok="modalFormOk"
    ></bus-meeting-room-schedule-confirm-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
// import { mixinDevice } from "@/utils/mixin";
// import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { httpAction, getAction } from "@/api/manage";
import BusMeetingRoomScheduleModal from "./modules/meetingRoomSchedule/BusMeetingRoomScheduleModal.vue";
import BusMeetingRoomScheduleConfirmModal from "./modules/meetingRoomSchedule/BusMeetingRoomScheduleConfirmModal.vue";

export default {
  name: "BusMeetingRoomList",
  // mixins: [JeecgListMixin, mixinDevice],
  components: {
    BusMeetingRoomScheduleModal,
    BusMeetingRoomScheduleConfirmModal,
  },
  data() {
    const planList = [
      {
        timePeriod: "上午",
        schedule: [
          {
            key: "2023-03-15",
            value: [
              {
                theme: "大会议室",
                status: 1,
                meeting_room_schedule: {}, //预定信息实体
              },
            ],
          },
          {
            key: "2023-03-14",
            value: [
              {
                theme: "大会议室",
                status: 0,
                meeting_room_schedule: {}, //预定信息实体
              },
            ],
          },
        ],
      },
      {
        timePeriod: "下午",
        schedule: [
          {
            key: "2023-03-15",
            value: [
              {
                theme: "大会议室1111111111111111111111111",
                status: 0,
                meeting_room_schedule: {}, //预定信息实体
              },
            ],
          },
          {
            key: "2023-03-14",
            value: [
              {
                theme: "大会议室",
                status: 0,
                meeting_room_schedule: {}, //预定信息实体
              },
            ],
          },
        ],
      },
      {
        timePeriod: "晚上",
        schedule: [],
      },
    ];
    return {
      isFirstDayOfMondayOrSunday: 1,
      cardStatus: {
        0: {
          title: "预定未确认",
          color: "#3291F8",
        },
        1: {
          title: "预定已确认",
          color: "#FF6200",
        },
      },
      weeks: ["时段", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
      todayDate: "",
      months: [],
      curDate: "",
      nowDate: new Date(),
      planList2: [
        // { meetingRoomName: "大会议", child: planList },
        // { meetingRoomName: "大会议2", child: planList },
      ],
      startDate: "",
      endDate: "",
    };
  },
  watch: {
    isFirstDayOfMondayOrSunday: {
      handler(val) {
        if (val > 1) {
          let arr = ["周一", "周二", "周三", "周四", "周五", "周六", "周日"];
          const arr1 = arr.slice(val - 1);
          const arr2 = arr.slice(0, val - 1);
          this.weeks = ["时段", ...arr1, ...arr2];
        }
      },
      immediate: true,
    },
  },
  mounted() {
    this.getCurWeek();
  },
  created() {},
  methods: {
    modalFormOk() {
      this.loadData();
    },
    loadData() {
      var _info = JSON.parse(localStorage.getItem("storeInfo"));
      getAction("/business/busMeetingRoomSchedule/fetch", {
        startDate: this.startDate,
        endDate: this.endDate,
        hotelId: _info.id,
      }).then((res) => {
        if (res.success) {
          this.planList2 = res.result;
        }
      });
    },
    /**
     * 获取 时间
     * @param time
     */
    getWeek(time) {
      this.curDate = new Date(time);
      //当前是周几
      const whichDay = time.getDay();
      let num = 0;
      if (this.isFirstDayOfMondayOrSunday <= whichDay) {
        num = this.isFirstDayOfMondayOrSunday;
      } else {
        num = this.isFirstDayOfMondayOrSunday - 7;
      }
      const weekDay = time.getDay() - num;
      time = this.addDate(time, weekDay * -1);
      for (let i = 0; i < 7; i++) {
        const { year, month, day } = this.formatDate(
          i === 0 ? time : this.addDate(time, 1)
        );
        this.months.push({
          date: `${year}-${month}-${day}`,
          showDate: `${month}-${day}`,
          timestamp: new Date(`${year}-${month}-${day}`).getTime(),
        });
      }
      this.months.sort((a, b) => a.timestamp - b.timestamp);
      delete this.months[0];
      this.todayDate = `${this.months[1].date} ~ ${
        this.months[this.months.length - 1].date
      }`;

      this.startDate = this.months[1].date + " 00:00:00";
      this.endDate = this.months[this.months.length - 1].date + " 00:00:00";
      this.loadData();
    },
    /**
     * 处理日期
     * @param date
     * @param n
     * @returns {*}
     */
    addDate(date, n) {
      date.setDate(date.getDate() + n);
      return date;
    },
    /**
     * 上周
     */
    getLastWeek() {
      const date = this.addDate(this.curDate, -7),
        { year, month, day } = this.formatDate(date),
        dateObj = {
          date: `${year}-${month}-${day}`,
          timestamp: new Date(`${year}-${month}-${day}`).getTime(),
        };
      this.dealDate(date);
      this.$emit("changeWeek", dateObj);
    },
    /**
     * 本周
     */
    getCurWeek() {
      const { year, month, day } = this.formatDate(new Date()),
        dateObj = {
          date: `${year}-${month}-${day}`,
          timestamp: new Date(`${year}-${month}-${day}`).getTime(),
        };
      this.dealDate(new Date());
      this.$emit("changeWeek", dateObj);
    },
    //日期格式处理
    formatDate(date) {
      var year = date.getFullYear();
      var months = date.getMonth() + 1;
      var month = (months < 10 ? "0" + months : months).toString();
      var day = (
        date.getDate() < 10 ? "0" + date.getDate() : date.getDate()
      ).toString();
      return {
        year: year.toString(),
        month,
        day,
      };
    },
    /**
     * 获取当天时间
     * @returns {string}
     */
    getCurDay(num = 0) {
      var datetime = new Date();
      var year = datetime.getFullYear();
      var month =
        datetime.getMonth() + 1 < 10
          ? "0" + (datetime.getMonth() + 1)
          : datetime.getMonth() + 1;
      let day = datetime.getDate();
      if (day + num > 0) {
        day =
          day + num < 10
            ? "0" + (datetime.getDate() + num)
            : datetime.getDate() + num;
      } else {
        day =
          day - num < 10
            ? "0" + (datetime.getDate() - num)
            : datetime.getDate() - num;
      }
      return `${year}-${month}-${day}`;
    },
    /**
     * 下周
     */
    getNextWeek() {
      const date = this.addDate(this.curDate, 7),
        { year, month, day } = this.formatDate(date),
        dateObj = {
          date: `${year}-${month}-${day}`,
          timestamp: new Date(`${year}-${month}-${day}`).getTime(),
        };
      this.dealDate(date);
      this.$emit("changeWeek", dateObj);
    },
    /**
     * 显示当天日期状态
     * @param date
     */
    dealDate(date) {
      this.months = [""];
      this.getWeek(date);
      const curDate = this.getCurDay();
      this.months.forEach((item) => {
        item.isCurDate = item.date === curDate;
      });
    },
    /**
     * 预定确认
     * @param row
     */
    handleDetail(row) {
      console.log("row", row);
      this.$refs.modalBusMeetingRoomScheduleConfirm.edit(row);
      this.$refs.modalBusMeetingRoomScheduleConfirm.title = "预定确认";
      this.$refs.modalBusMeetingRoomScheduleConfirm.disableSubmit = false;
      this.$refs.modalBusMeetingRoomScheduleConfirm.disableSubmit = true;
    },
    /**
     * 预定登记
     * @param month
     * @param period
     */
    handleCardDetail(month, period) {
      this.$refs.modalForm.add();
      this.$refs.modalForm.title = "预定登记";
      this.$refs.modalForm.disableSubmit = false;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
<style scoped>
ul {
  list-style: none;
}

ul,
li {
  margin: 0;
  padding: 0;
}

.course-week {
  width: 100%;
  border: 1px solid #ddd;
  padding: 1%;
  box-sizing: border-box;
}

.week-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 40px;
  padding: 0 1%;
  box-sizing: border-box;
}

.week-top .week-btn-wrap {
  width: 200px;
  display: flex;
  justify-content: space-around;
  color: #409eff;
}

.week-top .week-btn-wrap span {
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
}

.w-today-date {
  font-weight: bold;
  font-size: 16px;
}

.w-choose-status {
  display: flex;
  justify-content: flex-end;
  width: 200px;
}

.w-choose-status > div {
  width: 100%;
  flex: 1;
  display: flex;
  padding: 0 2%;
  white-space: nowrap;
  line-height: 20px;
  box-sizing: border-box;
}

.w-choose-status > div .square {
  display: flex;
  width: 16px;
  height: 16px;
  border-radius: 4px;
  box-sizing: border-box;
}

.w-choose-status > div .title {
  display: flex;
  align-items: center;
  line-height: 16px;
  padding-left: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.week-table {
  display: flex;
  flex-direction: column;
}

.week-table .table-header {
  width: 100%;
  height: 80px;
  background: #eaedf2;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-bottom: 1px solid #eaedf2;
  box-sizing: border-box;
}

.table-header .w-table-date,
.table-week {
  width: 100%;
  height: 40px;
  text-align: left;
  display: flex;
  justify-content: center;
  align-items: center;
}

.table-header .w-table-date > span,
.table-week > span {
  flex: 1;
  color: #000;
  height: 100%;
  font-size: 14px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
}

.w-table-date .w-day-item,
.table-week .w-day-item {
  color: #000;
  font-size: 14px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.week-table .w-time-period-list {
  width: 100%;
}

.w-time-period-list .w-time-period-row {
  width: 100%;
  min-height: 60px;
}

.w-time-period-col {
  width: 100%;
  min-height: 60px;
  display: flex;
}

.w-time-period-col .w-time-period {
  width: 12.5%;
  display: flex;
  justify-content: center;
  align-items: center;
  border-left: 1px solid #eaedf2;
  /* border-bottom: 1px solid #eaedf2; */
  box-sizing: border-box;
}
.w-time-period-col:last-child {
  border-bottom: 1px solid #eaedf2;
}
.meeting-room-center {
  transform: translate(-200%, 0%);
  width: 14px;
  font-size: 14px;
  word-wrap: break-word;
  position: absolute;
}

.w-time-period-col .w-row-day {
  width: 87.5%;
  display: flex;
  justify-content: center;
}

.w-row-day .w-things {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-left: 1px solid #eaedf2;
  border-bottom: 1px solid #eaedf2;
  box-sizing: border-box;
  cursor: pointer;
}

.w-row-day .w-things:last-child {
  border-right: 1px solid #eaedf2;
}

.w-things .w-thing-item {
  display: flex;
  width: 80%;
  font-size: 14px;
  flex-direction: column;
  justify-content: space-around;
  min-height: 50px;
  border-radius: 10px;
  margin: 2% 1%;
  padding: 1% 2%;
  cursor: pointer;
  color: #fff;
  background: #ff6200;
  box-sizing: border-box;
  transition: all 1s linear 0.5s;
}

.w-isCurDate {
  color: #ff2525 !important;
}

.w-noMore {
  min-height: 200px;
  padding: 2%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid rgba(156, 173, 173, 0.3);
  color: #9cadadb7;
  box-sizing: border-box;
}

.w_expand,
.w_shrink {
  color: #0a98d5;
  cursor: pointer;
  width: 100%;
  padding: 2% 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>