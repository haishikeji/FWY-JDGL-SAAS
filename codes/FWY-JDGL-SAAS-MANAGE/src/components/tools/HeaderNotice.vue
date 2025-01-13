<template>
  <a-popover
    trigger="click"
    placement="bottomRight"
    :autoAdjustOverflow="true"
    :arrowPointAtCenter="true"
    overlayClassName="header-notice-wrapper"
    @visibleChange="handleHoverChange"
    :overlayStyle="{ width: '600px', top: '50px' }"
  >
    <template slot="content">
      <a-spin :spinning="loadding">
        <a-tabs>
          <a-tab-pane :tab="msg1Title" key="1">
            <!--<a-list>
              <a-list-item>
                <a-list-item-meta title="你收到了 14 份新周报" description="一年前">
                  <a-avatar style="background-color: white" slot="avatar" src="https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png"/>
                </a-list-item-meta>
              </a-list-item>
              <a-list-item>
                <a-list-item-meta title="你推荐的 IT大牛 已通过第三轮面试" description="一年前">
                  <a-avatar style="background-color: white" slot="avatar" src="https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png"/>
                </a-list-item-meta>
              </a-list-item>
              <a-list-item>
                <a-list-item-meta title="这种模板可以区分多种通知类型" description="一年前">
                  <a-avatar style="background-color: white" slot="avatar" src="https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png"/>
                </a-list-item-meta>
              </a-list-item>
            </a-list>-->
            <a-tabs size="small" class="header_notice" @change="tabChange">
              <a-tab-pane :tab='"房间消息(" + roomAnnouncements.length + ")"' key="1">
                <a-list>
                  <a-list-item
                      :key="index"
                      v-for="(record, index) in roomAnnouncements"
                      :selected="selectedItems">
                    <div style="font-size: 18px;color: rgb(0, 0, 0);">
                      <input v-model="record.checked" type="checkbox" @change="toggleSelected(record)">
                    </div>
                    <div style="margin-left: 5%; width: 80%">
                      <p>
                        <a @click="showAnnouncement(record)">{{ record.titile }}</a>
                      </p>
                      <p style="color: rgba(0, 0, 0, 0.45); margin-bottom: 0px">
                        {{ record.createTime }} 发布
                      </p>
                    </div>
                    <div style="text-align: right">
                      <a-tag
                          @click="showAnnouncement(record)"
                          v-if="record.priority === 'L'"
                          color="blue"
                      >一般消息</a-tag
                      >
                      <a-tag
                          @click="showAnnouncement(record)"
                          v-if="record.priority === 'M'"
                          color="orange"
                      >重要消息</a-tag
                      >
                      <a-tag
                          @click="showAnnouncement(record)"
                          v-if="record.priority === 'H'"
                          color="red"
                      >紧急消息</a-tag
                      >
                    </div>
                  </a-list-item>
                  <div style="margin-top: 5px; text-align: center">
                    <a-row>
                      <a-col :span="8">
                        <a-button @click="toMyAnnouncement()" type="dashed" block style="width: 80%">查看更多</a-button>
                      </a-col>
                      <a-col :span="8">
                        <a-button @click="allSelect(roomAnnouncements)" type="dashed" block style="width: 80%">全部勾选</a-button>
                      </a-col>
                      <a-col :span="8">
                        <a-button @click="readMess()" type="dashed" block style="width: 80%">已读</a-button>
                      </a-col>
                    </a-row>
                  </div>
                </a-list>
              </a-tab-pane>
              <a-tab-pane tab="门店消息" key="2">

              </a-tab-pane>
              <a-tab-pane tab="资产消息" key="3">

              </a-tab-pane>
              <a-tab-pane :tab='"订单消息(" + orderAnnouncements.length + ")"' key="4">
                <a-list>
                  <a-list-item
                      :key="record.id"
                      v-for="(record, index) in orderAnnouncements"
                      :selected="selectedItems">
                    <div style="font-size: 18px;color: rgb(0, 0, 0);">
                      <input v-model="record.checked" type="checkbox" @change="toggleSelected(record)">
                    </div>
                    <div style="margin-left: 5%; width: 80%">
                      <p>
                        <a @click="showAnnouncement(record)">{{ record.titile }}</a>
                      </p>
                      <p style="color: rgba(0, 0, 0, 0.45); margin-bottom: 0px">
                        {{ record.createTime }} 发布
                      </p>
                    </div>
                    <div style="text-align: right">
                      <a-tag
                          @click="showAnnouncement(record)"
                          v-if="record.priority === 'L'"
                          color="blue"
                      >一般消息</a-tag
                      >
                      <a-tag
                          @click="showAnnouncement(record)"
                          v-if="record.priority === 'M'"
                          color="orange"
                      >重要消息</a-tag
                      >
                      <a-tag
                          @click="showAnnouncement(record)"
                          v-if="record.priority === 'H'"
                          color="red"
                      >紧急消息</a-tag
                      >
                    </div>
                  </a-list-item>
                  <div style="margin-top: 5px; text-align: center">
                    <a-row>
                      <a-col :span="12">
                        <a-button @click="toMyAnnouncement()" type="dashed" block style="width: 80%">查看更多</a-button>
                      </a-col>
                      <a-col :span="8">
                        <a-button @click="allSelect(orderAnnouncements)" type="dashed" block style="width: 80%">全部勾选</a-button>
                      </a-col>
                      <a-col :span="12">
                        <a-button @click="readMess()" type="dashed" block style="width: 80%">已读</a-button>
                      </a-col>
                    </a-row>
                  </div>
                </a-list>

              </a-tab-pane>
              <a-tab-pane tab="商城消息" key="5">

              </a-tab-pane>
              <a-tab-pane tab="集团消息" key="6">

              </a-tab-pane>
              <a-tab-pane tab="系统公告" key="7">

              </a-tab-pane>
              </a-tabs>

          </a-tab-pane>
          <a-tab-pane :tab="msg2Title" key="2">
          </a-tab-pane>
        </a-tabs>
      </a-spin>
    </template>
    <span @click="fetchNotice" class="header-notice">
      <a-badge :count="msgTotal">
        <a-icon style="font-size: 16px; padding: 4px" type="bell" />
      </a-badge>
    </span>
    <show-announcement
      ref="ShowAnnouncement"
      @ok="modalFormOk"
    ></show-announcement>
    <dynamic-notice
      ref="showDynamNotice"
      :path="openPath"
      :formData="formData"
    />
  </a-popover>
</template>

<script>
import { getAction, putAction } from "@/api/manage";
import ShowAnnouncement from "./ShowAnnouncement";
import store from "@/store/";
import DynamicNotice from "./DynamicNotice";
import Vue from "vue";
import { ACCESS_TOKEN } from "@/store/mutation-types";

export default {
  name: "HeaderNotice",
  components: {
    DynamicNotice,
    ShowAnnouncement,
  },
  data() {
    return {
      loadding: false,
      url: {
        listCementByUser: "/sys/annountCement/listByUser",
        editCementSend: "/sys/sysAnnouncementSend/editByAnntIdAndUserId",
        queryById: "/sys/annountCement/queryById",
      },
      hovered: false,
      announcement1: [],
      announcement2: [],
      roomAnnouncements: [],
      orderAnnouncements: [],
      selectedItems: [],
      msg1Count: "0",
      msg2Count: "0",
      msg1Title: "通知(0)",
      msg2Title: "",
      stopTimer: false,
      websock: null,
      lockReconnect: false,
      //websocket错误连接次数
      wsConnectErrorTime: 1,
      heartCheck: null,
      formData: {},
      openPath: "",
    };
  },
  computed: {
    msgTotal() {
      return parseInt(this.msg1Count) + parseInt(this.msg2Count);
    },
  },
  mounted() {
    this.loadData();
    //this.timerFun();
    this.initWebSocket();
    // this.heartCheckFun();
  },
  destroyed: function () {
    // 离开页面生命周期函数
    this.websocketOnclose();
  },
  methods: {
    timerFun() {
      this.stopTimer = false;
      let myTimer = setInterval(() => {
        // 停止定时器
        if (this.stopTimer == true) {
          clearInterval(myTimer);
          return;
        }
        this.loadData();
      }, 6000);
    },
    tabChange() {
      this.selectedItems = []
    },
    loadData() {
      try {
        // 获取系统消息
        getAction(this.url.listCementByUser)
          .then((res) => {
            if (res.success) {
              this.announcement1 = res.result.anntMsgList;
              this.roomAnnouncements = res.result.anntMsgList.filter(e => e.msgCategoryDetail === '1');
              this.orderAnnouncements = res.result.anntMsgList.filter(e => e.msgCategoryDetail === '4');
              this.msg1Count = res.result.anntMsgTotal;
              this.msg1Title = "通知(" + res.result.anntMsgTotal + ")";
              this.announcement2 = res.result.sysMsgList;
              this.msg2Count = res.result.sysMsgTotal;
              this.msg2Title = "系统消息(" + res.result.sysMsgTotal + ")";
              console.log(this.orderAnnouncements,'this.orderAnnouncements')
            }
          })
          .catch((error) => {
            console.log("系统消息通知异常", error); //这行打印permissionName is undefined
            this.stopTimer = true;
            console.log("清理timer");
          });
      } catch (err) {
        this.stopTimer = true;
        console.log("通知异常", err);
      }
    },
    fetchNotice() {
      if (this.loadding) {
        this.loadding = false;
        return;
      }
      this.loadding = true;
      setTimeout(() => {
        this.loadding = false;
      }, 200);
    },
    toggleSelected(record) {
      const idx = this.selectedItems.indexOf(record)
      if (idx === -1) {
        this.selectedItems.push(record)
      } else {
        this.selectedItems.splice(idx, 1)
      }
      console.log(this.selectedItems,'selectedItems')
    },
    showAnnouncement(record) {
      putAction(this.url.editCementSend, { anntId: record.id }).then((res) => {
        if (res.success) {
          this.loadData();
        }
      });
      this.hovered = false;
      if (record.openType === "component") {
        this.openPath = record.openPage;
        this.formData = { id: record.busId };
        this.$refs.showDynamNotice.detail(record.openPage);
      } else {
        this.$refs.ShowAnnouncement.detail(record);
      }
    },
    toMyAnnouncement() {
      this.$router.push({
        path: "/isps/userAnnouncement",
      });
    },
    allSelect(announcements) {
      console.log(announcements, "orderAnnouncements")
      announcements.forEach(item => {
        item.checked = true
      })
      this.selectedItems = JSON.parse(JSON.stringify(announcements))
      this.$forceUpdate()
    },
    readMess() {
      let ids = this.selectedItems.map(e => e.id)
      let idStr = ids.join()
      console.log(idStr);
      putAction(this.url.editCementSend, { anntId: idStr }).then((res) => {
        if (res.success) {
          this.loadData();
          this.selectedItems = []
        }
      });
    },
    modalFormOk() {},
    handleHoverChange(visible) {
      this.hovered = visible;
    },

    initWebSocket: function () {
      // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
      var userId = store.getters.userInfo.id;
      // var url =
      //   window._CONFIG["domianURL"]
      //     .replace("https://", "wss://")
      //     .replace("http://", "ws://") +
      //   "/websocket/" +
      //   userId;
      // console.log("domianURL", window._CONFIG["domianURL"]);
      // console.log("url", url);
      //update-begin-author:taoyan date:2022-4-22 for:  v2.4.6 的 websocket 服务端，存在性能和安全问题。 #3278
      let token = Vue.ls.get(ACCESS_TOKEN);
      // this.websock = new WebSocket(url, [token]);
      this.websock = new WebSocket(
        "ws://14.215.164.140:8081/jeecg-boot/websocket/" + userId,
        [token]
      );
      console.log(this.websock,'this.websock')
      //update-end-author:taoyan date:2022-4-22 for:  v2.4.6 的 websocket 服务端，存在性能和安全问题。 #3278
      this.websock.onopen = this.websocketOnopen;
      this.websock.onerror = this.websocketOnerror;
      this.websock.onmessage = this.websocketOnmessage;
      this.websock.onclose = this.websocketOnclose;
    },
    websocketOnopen: function () {
      console.log("WebSocket连接成功");
      //心跳检测重置
      //this.heartCheck.reset().start();
    },
    websocketOnerror: function (e) {
      console.log("WebSocket连接发生错误，第%s次", this.wsConnectErrorTime);
      console.log(e);
      this.wsConnectErrorTime = this.wsConnectErrorTime + 1;
      if (this.wsConnectErrorTime > 5) {
        console.log("WebSocket连接错误超过5次，就不再重新连了！");
        this.lockReconnect = true;
        return;
      }

      this.reconnect();
    },
    websocketOnmessage: function (e) {
      console.log("进来了")
      console.log("-----接收消息-------", e.data);
      var data = eval("(" + e.data + ")"); //解析对象
      this.voiceBroadcast(data.msgTxt);
      if (data.cmd == "topic") {
        //系统通知
        this.loadData();
      } else if (data.cmd == "user") {
        //用户消息
        this.loadData();
      }
      //心跳检测重置
      //this.heartCheck.reset().start();
      this.$notification.open({
        message: "消息提醒!",
        description: data.msgTxt,
        duration: 120,
        onClick: () => {},
      });
    },
    websocketOnclose: function (e) {
      console.log("connection closed (" + e + ")");
      if (e) {
        console.log("connection closed (" + e.code + ")");
      }
      this.reconnect();
    },
    websocketSend(text) {
      // 数据发送
      try {
        this.websock.send(text);
      } catch (err) {
        console.log("send failed (" + err.code + ")");
      }
    },
    //语音播报系统通知
    voiceBroadcast(text) {
      console.log("play");
      // var url =
      //   "http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&text=" +
      //   encodeURI(text); // baidu文字转语音
      var url =
        "http://img-qn.51miz.com/preview/sound/00/27/78/51miz-S277826-5CF721D2.mp3";
      var voiceContent = new Audio();
      voiceContent.src = url;
      voiceContent.play();
    },
    openNotification(data) {
      var text = data.msgTxt;
      const key = `open${Date.now()}`;
      this.$notification.open({
        message: "消息提醒",
        placement: "bottomRight",
        description: text,
        key,
        btn: (h) => {
          return h(
            "a-button",
            {
              props: {
                type: "primary",
                size: "small",
              },
              on: {
                click: () => this.showDetail(key, data),
              },
            },
            "查看详情"
          );
        },
      });
    },

    reconnect() {
      var that = this;
      if (that.lockReconnect) return;
      that.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      setTimeout(function () {
        console.info("尝试重连...");
        that.initWebSocket();
        that.lockReconnect = false;
      }, 20000);
    },
    heartCheckFun() {
      var that = this;
      //心跳检测,每20s心跳一次
      that.heartCheck = {
        timeout: 20000,
        timeoutObj: null,
        serverTimeoutObj: null,
        reset: function () {
          clearTimeout(this.timeoutObj);
          //clearTimeout(this.serverTimeoutObj);
          return this;
        },
        start: function () {
          var self = this;
          this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            that.websocketSend("HeartBeat");
            console.info("客户端发送心跳");
            //self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
            //  that.websock.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            //}, self.timeout)
          }, this.timeout);
        },
      };
    },

    showDetail(key, data) {
      this.$notification.close(key);
      var id = data.msgId;
      getAction(this.url.queryById, { id: id }).then((res) => {
        if (res.success) {
          var record = res.result;
          this.showAnnouncement(record);
        }
      });
    },
  },
};
</script>

<style lang="css">
.header-notice-wrapper {
  top: 50px !important;
}

.header_notice {
  .ant-tabs-tab{
    margin-right: 0px !important;
    padding-right: 10px !important;
    padding-left: 10px !important
  }
}
</style>
<style lang="less" scoped>
.header-notice {
  display: inline-block;
  transition: all 0.3s;

  span {
    vertical-align: initial;
  }
}
</style>
