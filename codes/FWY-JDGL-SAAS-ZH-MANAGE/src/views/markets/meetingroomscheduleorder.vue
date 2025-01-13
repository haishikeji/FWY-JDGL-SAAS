<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="账单号"
                v-model="queryParam.orderNo"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="会议主题"
                v-model="queryParam.theme"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="公司名称"
                v-model="queryParam.name"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="预定人"
                v-model="queryParam.destinedName"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="联系电话"
                v-model="queryParam.mobile"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search"
                >查询</a-button
              >
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

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
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="orderDetailList" slot-scope="text">
          <div
            v-if="text && text.length > 0"
            v-for="(item, index) in text"
            :key="index"
          >
            {{ item.payWayName }}:{{ item.amount }}
            <a-divider type="vertical" />
          </div>
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

        <!-- <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="setStatus(record, 0)">确认</a>
          <a-divider type="vertical" />
          <a @click="setStatus(record, 0)">结账</a>
          <a-divider type="vertical" />
          <a @click="setStatus(record, 0)">删除</a>
        </span> -->
        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">全部档期</a>
          <a-divider type="vertical" />
          <template v-if="record.confirmStatus != 0 && record.billStatus == 0">
            <a @click="handleSettle(record)">结账</a>
            <a-divider type="vertical"
          /></template>
          <template v-if="record.confirmStatus == 0">
            <a @click="handleEdit(record)">修改</a>
            <a-divider type="vertical"
          /></template>
          <template v-if="record.confirmStatus == 0">
            <a @click="batchConfirm(record)">确认</a>
            <a-divider type="vertical" />
          </template>
          <a v-if="record.status == 0" @click="handleEdit(record)">删除</a>
        </span>
      </a-table>
    </div>

    <bus-meeting-room-schedule-edit-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></bus-meeting-room-schedule-edit-modal>
    <meeting-room-schedule-detail-modal
      ref="modalMeetingRoomScheduleDetail"
      :meetingRoomScheduleId="meetingRoomScheduleId"
      @ok="modalFormOk"
    ></meeting-room-schedule-detail-modal>
    <bus-meeting-room-schedule-edit-modal-2
      ref="modalForm2"
      @ok="modalFormOk"
    ></bus-meeting-room-schedule-edit-modal-2>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import BusMeetingRoomScheduleEditModal from "./modules/meetingRoomSchedule/BusMeetingRoomScheduleEditModal";
import meetingRoomScheduleDetailModal from "./modules/meetingRoomSchedule/meetingRoomScheduleDetail/meetingRoomScheduleDetailModal";
import BusMeetingRoomScheduleEditModal2 from "./modules/meetingRoomScheduleOrder/BusMeetingRoomScheduleEditModal";
import { httpAction, postAction } from "@/api/manage";
export default {
  name: "BusMeetingRoomList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BusMeetingRoomScheduleEditModal,
    meetingRoomScheduleDetailModal,
    BusMeetingRoomScheduleEditModal2,
  },
  data() {
    return {
      description: "bus_meeting_room管理页面",
      // 表头
      columns: [
        {
          title: "账单号",
          align: "center",
          dataIndex: "orderNo",
        },
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
            return text == 1 ? "已结账" : "未结账";
          },
        },
        {
          title: "挂账人",
          align: "center",
          dataIndex: "contactName",
          customRender: function (text) {
            //后期结合房间订单关联
            return text?text:"无";
          },
        },
        {
          title: "消费金额",
          align: "center",
          dataIndex: "amount",
        },
        {
          title: "付款",
          align: "center",
          dataIndex: "orderDetailList",
          scopedSlots: { customRender: "orderDetailList" },
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "confirmStatus",
          customRender: function (text) {
            return text == 1 ? "已确认" : "未确认";
          },
        },
        {
          title: "创建时间",
          align: "center",
          dataIndex: "createTime",
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list: "/business/busMeetingRoomSchedule/list",
        delete: "/business/busMeetingRoomSchedule/delete",
        deleteBatch: "/business/busMeetingRoomSchedule/deleteBatch",
        exportXlsUrl: "/business/busMeetingRoomSchedule/exportXls",
        importExcelUrl: "business/busMeetingRoomSchedule/importExcel",
        confirmBatch:
          "/business/busMeetingRoomScheduleDetail/confirmBatchByMeetingRoomScheduleId",
      },
      dictOptions: {},
      superFieldList: [],
      meetingRoomScheduleId: "",
    };
  },
  created() {
    this.getSuperFieldList();
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
    handleDetail(record) {
      console.log("record", record);
      this.meetingRoomScheduleId = record.id;
      this.$refs.modalMeetingRoomScheduleDetail.add(record);
      this.$refs.modalMeetingRoomScheduleDetail.title = "档期列表预定确认";
      this.$refs.modalMeetingRoomScheduleDetail.disableSubmit = true;
    },
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
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        })
        .finally(() => {
          that.loading = false;
        });
    },
    setStatus(record, status) {
      record.status = status;
      postAction("/business/busMeetingRoomSchedule/edit", record).then(
        (res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message);
          }
        }
      );
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "tenantId", text: "关联租户" });
      fieldList.push({ type: "string", value: "hotelId", text: "关联酒店" });
      fieldList.push({ type: "string", value: "name", text: "名称" });
      fieldList.push({ type: "string", value: "spec", text: "规格" });
      fieldList.push({ type: "number", value: "area", text: "金额" });
      fieldList.push({ type: "int", value: "number", text: "人数" });
      fieldList.push({
        type: "string",
        value: "adminName",
        text: "管理员姓名",
      });
      fieldList.push({ type: "string", value: "address", text: "地址" });
      fieldList.push({ type: "string", value: "facility", text: "设备" });
      fieldList.push({ type: "int", value: "sort", text: "排序" });
      fieldList.push({ type: "string", value: "remark", text: "描述" });
      fieldList.push({ type: "int", value: "status", text: "状态" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>