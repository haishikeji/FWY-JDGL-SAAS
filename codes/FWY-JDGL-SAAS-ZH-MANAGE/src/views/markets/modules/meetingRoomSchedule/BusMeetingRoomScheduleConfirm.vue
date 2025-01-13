<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="24">
            <a-divider orientation="left">档期</a-divider></a-col
          >
          <a-col :span="6">
            <a-form-item label="">
              <a-select
                v-model="model.detail.meetingRoomId"
                style="width: 100%"
                placeholder="会议室"
                disabled
              >
                <a-select-option
                  v-for="(item, index) in meetingRoomList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="">
              <j-date
                disabled
                placeholder="请选择开始时间"
                v-model="model.detail.meetingDate"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="">
              <a-select
                disabled
                v-model="model.detail.timeSpan"
                style="width: 100%"
                placeholder="会议室"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in timeSpanList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <!--先不实现增加预定功能-->
              <!-- <a-button v-if="model.roomSchedule.billStatus==0"
                @click="handleAdd"
                type="danger"
                style="margin-left: 8px"
                >增加预定</a-button
              > -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <a-divider orientation="left">档期</a-divider>
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        class="j-table-force-nowrap"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text, record">
          <span v-if="!text" style="font-size: 12px; font-style: italic"
            >无图片</span
          >
          <img
            v-else
            :src="getImgView(text)"
            :preview="record.id"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic"
            >无文件</span
          >
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)"
          >
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">全部档期</a>
          <a-divider type="vertical" />
          <template v-if="record.status != 0 && record.billStatus == 0">
            <a @click="handleSettle(record)">结账</a>
            <a-divider type="vertical"
          /></template>
          <template v-if="record.status == 0">
            <a @click="handleEdit(record)">修改</a>
            <a-divider type="vertical"
          /></template>
          <template v-if="record.status == 0">
            <a @click="batchConfirm(record)">确认</a>
            <a-divider type="vertical" />
          </template>
          <a v-if="record.status == 0" @click="handleEdit(record)">删除</a>
        </span>
      </a-table>
    </div>
    <bus-meeting-room-schedule-edit-modal
      ref="modalForm"
      @ok="modalFormOk2($event)"
    ></bus-meeting-room-schedule-edit-modal>
    <meeting-room-schedule-detail-modal
      ref="modalMeetingRoomScheduleDetail"
      :meetingRoomScheduleId="meetingRoomScheduleId"
      @ok="modalFormOk"
    ></meeting-room-schedule-detail-modal>
    <bus-meeting-room-schedule-edit-modal-2
      ref="modalForm2"
      @ok="modalFormOk3"
    ></bus-meeting-room-schedule-edit-modal-2>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import BusMeetingRoomScheduleEditModal from "./BusMeetingRoomScheduleEditModal";
import meetingRoomScheduleDetailModal from "./meetingRoomScheduleDetail/meetingRoomScheduleDetailModal";
import BusMeetingRoomScheduleEditModal2 from "@/views/markets/modules/meetingRoomScheduleOrder/BusMeetingRoomScheduleEditModal";
import { httpAction, getAction, postAction } from "@/api/manage";

export default {
  name: "BusMarketCouponsUsedList",
  // mixins: [ mixinDevice],
  components: {
    BusMeetingRoomScheduleEditModal,
    meetingRoomScheduleDetailModal,
    BusMeetingRoomScheduleEditModal2,
  },
  props: {
    couponsId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      meetingRoomScheduleId: "",
      description: "",
      model: {
        detail: { meetingRoomId: "", meetingDate: "", meetingDatev: "" },
      },
      dataSource: [],
      // 表头
      columns: [
        {
          title: "会议主题",
          align: "center",
          dataIndex: "theme",
        },
        {
          title: "公司名称",
          align: "center",
          dataIndex: "name",
        },
        {
          title: "预定人",
          align: "center",
          dataIndex: "destinedName",
        },

        {
          title: "联系电话",
          align: "center",
          dataIndex: "mobile",
        },
        {
          title: "账单状态",
          align: "center",
          dataIndex: "billStatus",
          customRender: function (text) {
            var msg = "未结账";
            if (text == 1) {
              msg = "已挂账";
            } else if (text == 2) {
              msg = "已结账";
            }
            return msg;
          },
        },
        {
          title: "备注",
          align: "center",
          dataIndex: "gainTime",
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 80,
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        confirmBatch:
          "/business/busMeetingRoomScheduleDetail/confirmBatchByMeetingRoomScheduleId",
      },
      dictOptions: {},
      superFieldList: [],
      meetingRoomList: [],
      timeSpanList: [
        { id: 1, name: "上午" },
        { id: 2, name: "下午" },
        { id: 3, name: "晚上" },
      ],
    };
  },
  created() {
    getAction("/business/busMeetingRoom/list", {
      pageNo: 1,
      pageSize: 100,
      stauts: 1,
    }).then((res) => {
      if (res.success) {
        this.meetingRoomList = res.result.records;
      }
    });
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    handleSettle(record) {
      this.$refs.modalForm2.edit(record);
      this.$refs.modalForm2.title = "结账";
      this.$refs.modalForm2.disableSubmit = false;
    },
    handleAdd() {},
    batchConfirm(record) {
      if (!this.url.confirmBatch) {
        this.$message.error("请设置url.confirmBatch!");
        return;
      }

      var that = this;
      that.loading = true;
      postAction(that.url.confirmBatch, {
        id: record.id,
      })
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.dataSource = [
              Object.assign({}, that.model.roomSchedule, {
                status: 1,
              }),
            ];
            that.$emit("ok");
          } else {
            that.$message.warning(res.message);
          }
        })
        .finally(() => {
          that.loading = false;
        });
    },
    modalFormOk2(record) {
      console.log("record", record);
      this.dataSource = [Object.assign({}, record)];
      this.$emit("ok");
    },
    modalFormOk3() {
      this.dataSource = this.dataSource[0].billStatus = 1;
    },
    modalFormOk() {},
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      delete this.model.roomSchedule.createTime;
      this.dataSource = [
        Object.assign({}, this.model.roomSchedule, {
          status: this.model.detail.status,
        }),
      ];
      this.visible = true;
    },
    handleEdit(record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleDetail(record) {
      console.log("record", record);
      this.meetingRoomScheduleId = record.id;
      this.$refs.modalMeetingRoomScheduleDetail.add(record);
      this.$refs.modalMeetingRoomScheduleDetail.title = "档期列表预定确认";
      this.$refs.modalMeetingRoomScheduleDetail.disableSubmit = true;
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({
        type: "string",
        value: "tenantId",
        text: "关联租户",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "hotelId",
        text: "关联酒店",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "couponsId",
        text: "免房券id",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "code",
        text: "券号",
        dictCode: "",
      });
      fieldList.push({
        type: "int",
        value: "status",
        text: "状态 0未领取 1已领取 2已使用 3已作废",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "userid",
        text: "会员id",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "userName",
        text: "姓名",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "roomNumber",
        text: "房间号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "checkedInNo",
        text: "入住单号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "mobile",
        text: "手机号",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "gainTime", text: "领取时间" });
      fieldList.push({ type: "date", value: "usedTime", text: "使用时间" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>