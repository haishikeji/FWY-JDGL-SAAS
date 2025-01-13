<template>
  <a-spin :spinning="confirmLoading">
    <div style="gap: 10px; display: flex; margin: 10px">
      <a-button v-if="disabled" @click="disabled = false">修改预定</a-button>
      <a-button v-if="!disabled" @click="editOrder">确认修改</a-button>
      <a-button v-if="!disabled" @click="cancelOrder">取消修改</a-button>
      <a-button
        v-if="disabled && model.orderInfo.bookingStatus == 1"
        @click="setOrderStatus(2)"
        >取消预定</a-button
      >
      <a-button
        v-if="model.orderInfo.bookingStatus == 2"
        @click="setOrderStatus(1)"
        >恢复预定</a-button
      >
      <a-button v-if="model.orderInfo.bookingStatus == 2" @click="deleteOrder"
        >删除预定</a-button
      >
    </div>
    <j-form-container>
      <a-form-model
        ref="form"
        :model="model"
        :rules="validatorRules"
        slot="detail"
      >
        <a-row>
          <div style="display: flex; gap: 15px">
            <div style="width: 39%">
              <h4 style="color: rgba(255, 141, 26, 1); font-weight: 600">
                订价信息
              </h4>
              <a-divider />
              <a-col :span="24" v-if="disabled">
                <a-form-model-item
                  label="预定单号"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                >
                  {{ model.orderInfo.bookingOrdersNo }}
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="入住类型"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.bookingType"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="入住类型"
                    v-model="model.orderInfo.bookingType"
                    @change="bookingTypeChange"
                  >
                    <a-select-option :value="1"> 全天 </a-select-option>
                    <a-select-option :value="2"> 钟点 </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>

              <a-col :span="24">
                <a-form-model-item
                  label="预抵时间"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.arrivalTime2"
                >
                  <j-date
                    :disabled="disabled"
                    placeholder="预抵时间"
                    v-model="model.orderInfo.arrivalTime2"
                    style="width: 180px"
                    :allowClear="false"
                    :disabled-date="disabledDate"
                    @change="arrivalTimeChange"
                  />
                  <a-time-picker
                    :disabled="disabled"
                    style="width: 80px; margin-left: 2px"
                    v-model="model.orderInfo.arrivalTimeSpan"
                    :default-value="moment('12:00', 'HH:mm')"
                    format="HH:mm"
                    :allowClear="false"
                    @change="arrivalTimeSpanChange"
                  />
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="预离时间"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.dueOutTime2"
                >
                  <j-date
                    :disabled="disabled || model.orderInfo.bookingType === 2"
                    placeholder="预离时间"
                    v-model="model.orderInfo.dueOutTime2"
                    style="width: 180px"
                    :allowClear="false"
                    :disabled-date="disabledDate"
                    @change="arrivalTimeChange2"
                  />
                  <a-time-picker
                    :disabled="disabled || model.orderInfo.bookingType === 2"
                    style="width: 80px; margin-left: 2px"
                    v-model="model.orderInfo.dueOutTimeSpan"
                    format="HH:mm"
                    :allowClear="false"
                  />
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="客人来源"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.customerSource"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="客人来源"
                    v-model="model.orderInfo.customerSource"
                  >
                    <a-select-option
                      :value="item.id"
                      v-for="(item, index) in customerSourceList"
                      :key="item.id"
                    >
                      {{ item.itemText }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="24" v-if="model.orderInfo.bookingType == 2">
                <a-form-model-item
                  label="时长"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.hourRoomId"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="时长"
                    v-model="model.orderInfo.hourRoomId"
                    @change="hourRoomIdChange"
                  >
                    <a-select-option
                      :value="item.id"
                      v-for="(item, index) in hourRoomRuleList"
                      :key="item.id"
                    >
                      {{ item.hourRoomName }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="24" v-else>
                <a-form-model-item
                  label="天数"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.dayCount"
                >
                  <a-input-number
                    :disabled="disabled"
                    v-model="model.orderInfo.dayCount"
                    placeholder="天数"
                    :min="1"
                    @change="dayCountChange"
                  ></a-input-number>
                </a-form-model-item>
              </a-col>

              <a-col :span="24">
                <a-form-model-item
                  label="早餐"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.breakfastNum"
                >
                  <a-input-number
                    :disabled="disabled"
                    v-model="model.orderInfo.breakfastNum"
                    placeholder="早餐"
                    :min="0"
                  ></a-input-number>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="预定方式"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.bookingDicWay"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="预定方式"
                    v-model="model.orderInfo.bookingDicWay"
                  >
                    <a-select-option
                      :value="item.id"
                      v-for="(item, index) in bookingdicWayList"
                      :key="item.id"
                    >
                      {{ item.itemText }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="客人类型"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.customerType"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="客人类型"
                    v-model="model.orderInfo.customerType"
                  >
                    <a-select-option :value="1"> 散客 </a-select-option>
                    <a-select-option :value="2"> 会员 </a-select-option>
                    <a-select-option :value="3"> 协议单位 </a-select-option>
                    <a-select-option :value="4"> 中介 </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="24" v-if="model.orderInfo.bookingType === 1">
                <a-form-model-item
                  label="房价方案"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.roomPriceSlnId"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="房价方案"
                    v-model="model.orderInfo.roomPriceSlnId"
                  >
                    <a-select-option value="会员价"> 会员价 </a-select-option>
                    <a-select-option value="平日价"> 平日价 </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <h4 style="color: rgba(255, 141, 26, 1); font-weight: 600">
                其他信息
              </h4>
              <a-divider />
              <a-col :span="24">
                <a-form-model-item
                  label="联系人"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="contactName"
                >
                  <a-auto-complete
                    v-model="model.contactName"
                    placeholder="联系人"
                    @search="handleSearch"
                    @select="(e) => handleSelectMember(e)"
                  >
                    <template slot="dataSource">
                      <a-select-option
                        v-for="item in customerList"
                        :key="item.id"
                        >{{ item.name }}-{{ item.phone }}</a-select-option
                      >
                    </template>
                  </a-auto-complete>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="电话"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="phone"
                >
                  <a-input
                    :disabled="disabled"
                    v-model="model.phone"
                    placeholder="电话"
                  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="担保方式"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.warrantType"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="担保方式"
                    v-model="model.orderInfo.warrantType"
                  >
                    <a-select-option :value="1"> 无担保 </a-select-option>
                    <a-select-option :value="2"> 有担保 </a-select-option>
                    <a-select-option :value="3"> OTA担保 </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="销售员"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.warranter"
                >
                  <a-select
                    :disabled="disabled"
                    placeholder="销售员"
                    v-model="model.orderInfo.warranter"
                  >
                    <a-select-option
                      :value="item.id"
                      v-for="item in warranterList"
                      :key="item.id"
                    >
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="外部单号"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.outerOrdersNo"
                >
                  <a-input
                    :disabled="disabled"
                    v-model="model.orderInfo.outerOrdersNo"
                    placeholder="外部单号"
                  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item
                  label="备注"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="orderInfo.remark"
                >
                  <a-textarea
                    :disabled="disabled"
                    v-model="model.orderInfo.remark"
                    rows="4"
                    placeholder="备注"
                  />
                </a-form-model-item>
              </a-col>
            </div>
            <div style="width: 61%" v-if="disabled">
              <h4 style="color: rgba(255, 141, 26, 1); font-weight: 600">
                选择房间
              </h4>
              <a-divider />
              <div style="display: flex; justify-content: space-between">
                <div>
                  <span>房间数：{{ model.orderInfo.dayCount }}晚</span>
                  <span style="padding-left: 10px"
                    >占房天数：{{ model.orderInfo.dayCount }}晚</span
                  >
                  <span style="padding-left: 10px">总价：{{ amount2 }}</span>
                </div>
                <div style="display: flex; gap: 10px">
                  <a-button>制房卡</a-button>
                  <a-button>批量排房</a-button>
                  <a-button>批量入住</a-button>
                </div>
              </div>
              <a-table
                :columns="columns"
                :data-source="model.roomIds"
                :pagination="false"
                rowKey="id"
              >
                <template slot="favPrice" slot-scope="text, record, index">
                  {{ record.layout.favPrice }}
                </template>
                <template slot="presetNum" slot-scope="text, record, index">
                  <div>
                    <a-input-number
                      v-model="record.layout.presetNum"
                      :min="(record.rooms || []).length"
                      :max="record.layout.oldTags"
                      @change="presetNumChange($event, record)"
                    />
                  </div>
                </template>

                <span slot="selectRoom" slot-scope="text, record, index">
                  <a-button type="link" @click="pulsRoom2(record, index)">{{
                    text ? text : "排房"
                  }}</a-button>
                </span>
                <span slot="action" slot-scope="text, record, index">
                  <div style="gap: 10px; display: flex">
                    <a-button
                      v-if="record.roomStatus != 3 && record.roomStatus != 4"
                      size="small"
                      >入住</a-button
                    >
                    <a-button
                      size="small"
                      v-if="!record.isMain"
                      @click="setMain(record.id)"
                      >设主房</a-button
                    >
                    <a-button v-if="record.roomStatus == 3" size="small"
                      >查看详单</a-button
                    >
                  </div>
                </span>
                <template slot="zhu" slot-scope="text, record, index">
                  <span
                    v-if="text == true"
                    class="booking_circle_span orange_color"
                    >主</span
                  >
                </template>
              </a-table>
            </div>
            <div style="width: 61%" v-else>
              <h4 style="color: rgba(255, 141, 26, 1); font-weight: 600">
                选择房间
              </h4>
              <a-divider />
              <div style="display: flex; justify-content: space-between">
                <div>
                  <span style="padding-left: 10px"
                    >占房天数：{{ model.orderInfo.dayCount }}晚</span
                  >
                  <span style="padding-left: 10px">总价：{{ amount }}</span>
                </div>
              </div>
              <a-table
                :columns="columns2"
                :data-source="canUserRooms"
                :pagination="false"
                :rowKey="rowKey"
              >
                <div
                  slot="expandedRowRender"
                  slot-scope="record, index, indent, expanded"
                  style="margin: 0"
                >
                  <p>
                    已排房：
                    <template
                      v-for="(building, bIndex) in record.buildingRooms"
                    >
                      <a-tag
                        color="blue"
                        closable
                        :visible="visible"
                        @close.stop="tagClose2(rindex, bIndex, index)"
                        v-for="(item, rindex) in building.floorRooms"
                        :key="rindex"
                        v-if="item.check && item.check == 1"
                        >{{ item.name }}</a-tag
                      >
                      <!-- <a-tag
                        color="blue"
                        closable
                        :visible="visible"
                        @close.stop="tagClose2(rindex, index)"
                        v-for="(item, rindex) in record.rooms"
                        :key="rindex"
                        >{{ item.name }}</a-tag
                      > -->
                    </template>
                  </p>
                  <p>
                    未排房：{{
                      record.layout.presetNum - (record.rooms || []).length
                    }}间
                  </p>
                </div>
                <template slot="favPrice" slot-scope="text, record, index">
                  <!-- <editable-cell
                    :text="record.layout.favPrice"
                    @change="onCellChange('favPrice', index, $event)"
                  /> -->
                  {{ record.layout.favPrice }}
                </template>
                <template slot="presetNum" slot-scope="text, record, index">
                  <div>
                    <a-input-number
                      v-model="record.layout.presetNum"
                      :min="(record.rooms || []).length"
                      :max="record.layout.oldTags"
                      @change="presetNumChange($event, record)"
                    />
                  </div>
                </template>
                <span slot="action" slot-scope="text, record, index">
                  <a
                    :disabled="record.layout.presetNum <= 0"
                    @click="pulsRoom(index)"
                    >排房</a
                  >
                </span>
              </a-table>
            </div>
          </div>
        </a-row>
      </a-form-model>
    </j-form-container>
    <select-room-form-modal
      ref="modalSelectRoomForm"
      @ok="modalFormOk"
    ></select-room-form-modal>
    <select-room-form-modal
      ref="modalSelectRoomForm2"
      @ok="modalFormOk2"
    ></select-room-form-modal>
  </a-spin>
</template>

<script>
import { httpAction, getAction, postAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import moment from "moment";
import EditableCell from "@views/room/modules/checkIn/EditableCell.vue";
import SelectRoomFormModal from "./SelectRoomFormModal.vue";
const date = new Date();
const endDate = new Date(date.setDate(date.getDate() + 1));
const columns = [
  {
    title: "",
    dataIndex: "isMain",
    width: 50,
    scopedSlots: { customRender: "zhu" },
  },
  {
    title: "排房",
    dataIndex: "roomName",
    scopedSlots: { customRender: "selectRoom" },
  },
  {
    title: "房型",
    dataIndex: "layoutName",
  },
  {
    title: "房价",
    dataIndex: "marketPrice",
    customRender: function (text, record) {
      var price = 0;
      if (record.layoutDayPrices && record.layoutDayPrices.length > 0) {
        price = record.layoutDayPrices[0].price;
      }
      return price;
    },
  },
  {
    title: "操作",
    dataIndex: "action",
    align: "center",
    fixed: "right",
    width: 70,
    scopedSlots: { customRender: "action" },
  },
];
const data = [];
for (let i = 0; i < 2; i++) {
  data.push({
    id: i,
    key1: `双人床` + i,
    key2: 298,
    key3: 298,
    key4: 8,
    key5: 0,
    key6: 0,
  });
}
export default {
  name: "ScheduleRoomForm",
  components: { EditableCell, SelectRoomFormModal },
  props: {
    //表单禁用
    disabled2: {
      type: Boolean,
      default: false,
      required: false,
    },
    id: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      disabled: false,
      visible: true,
      columns,
      columns2: [
        // {
        //     title: "",
        //     dataIndex: "key",
        //     width: 20,
        //   },
        {
          title: "房型",
          dataIndex: "name",
          width: 150,
          customRender: function (text, record) {
            return record.layout.name;
          },
        },
        {
          title: "门市价",
          dataIndex: "marketPrice",
          width: 100,
          customRender: function (text, record) {
            return record.layout.marketPrice;
          },
        },
        {
          title: "优惠价",
          dataIndex: "favPrice",
          width: 120,
          scopedSlots: { customRender: "favPrice" },
        },
        {
          title: "可订数/可超数",
          dataIndex: "canUseCount",
          width: 170,
          customRender: function (text, record) {
            return record.layout.canUseCount + "/0";
          },
        },
        {
          title: "预定间数",
          dataIndex: "presetNum",
          width: 100,
          scopedSlots: { customRender: "presetNum" },
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 70,
          scopedSlots: { customRender: "action" },
        },
      ],
      model: {
        // data: data,
        orderInfo: {
          bookingOrdersType: 1,
          arrivalTime2: moment(new Date()).format("YYYY-MM-DD"),
          dueOutTime2: moment(endDate).format("YYYY-MM-DD"),
          arrivalTimeSpan: moment("18:00", "HH:mm"),
          dueOutTimeSpan: moment("12:00", "HH:mm"),
          bookingType: 1,
          dayCount: 1,
          warrantType: 1,
          hourRoomId: "",
          breakfastNum: 0,
        },
        roomIds: [],
        layoutDayPrices: [],
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {
        "orderInfo.bookingType": [
          { required: true, message: "请选择入住类型!" },
        ],
        "orderInfo.arrivalTime2": [
          { required: true, message: "请选择预抵时间!" },
        ],
        "orderInfo.dueOutTime2": [
          { required: true, message: "请选择预离时间!" },
        ],
        "orderInfo.customerSource": [
          { required: true, message: "请选择客人来源!" },
        ],
        "orderInfo.bookingDicWay": [
          { required: true, message: "请选择预定方式!" },
        ],
        "orderInfo.customerType": [
          { required: true, message: "请选择客人类型!" },
        ],
        contactName: [{ required: true, message: "请输入联系人!" }],
        phone: [{ required: true, message: "请输入电话!" }],
      },
      url: {
        add: "/business/busRoomBookingOrders/add",
        edit: "/business/busRoomBookingOrders/add",
        getBookingOrderInfo:
          "/business/busRoomBookingOrders/getBookingOrderInfo",
      },
      result: [],
      selectIndex: 0,
      customerSourceList: [],
      bookingdicWayList: [],
      warranterList: [],
      hourRoomRuleList: [],
      canUserRooms: [],
      customerList: [],
      oldcustomerList: [],
      canUserRooms2: [],
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
    amount() {
      var sum = 0;
      this.canUserRooms.forEach((t) => {
        sum += t.layout.favPrice * t.layout.presetNum;
      });
      return sum.toFixed(2);
    },
    amount2() {
      var sum = 0;
      this.model.roomIds.forEach((t) => {
        t.layoutDayPrices.forEach((p) => {
          sum += p.price;
        });
      });
      return sum.toFixed(2);
    },
  },
  created() {
    this.load();
  },
  methods: {
    deleteOrder() {
      deleteAction("/business/busRoomBookingOrders/delete", {
        id: this.model.orderInfo.id,
      }).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
          this.$router.go(-1);
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    setOrderStatus(status) {
      postAction(
        "/business/busRoomBookingOrders/set-batch-order-status?id=" +
          this.model.orderInfo.id +
          "&bookingStatus=" +
          status,
        {
          id: this.model.orderInfo.id,
          bookingStatus: status,
        }
      ).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
          this.getBookingOrderInfo();
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    load() {
      this.disabled = this.disabled2;
      var _info = JSON.parse(localStorage.getItem("storeInfo"));
      if (_info) {
        this.model.hotelId = _info.id;
      }
      //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));

      getAction('/business/busDictItem/queryList', {
        hotelId: _info.id,
        dictName: '客人来源设置'
      }).then((res) => {
        if (res.success) {
          this.customerSourceList = res.result;
        }
      });
      getAction('/business/busDictItem/queryList', {
        hotelId: _info.id,
        dictName: '预定方式'
      }).then((res) => {
        if (res.success) {
          this.bookingdicWayList = res.result;
        }
      });
      getAction("/business/busSalesPerson/list", {
        hotelId: _info.id,
        pageNo: 1,
        pageSize: 100,
      }).then((res) => {
        if (res.success) {
          this.warranterList = res.result.records;
        }
      });

      getAction("/rooms/cesHourRoomRule/list", {
        pageNo: 1,
        pageSize: 100,
      }).then((res) => {
        if (res.success) {
          this.hourRoomRuleList = res.result.records;
        }
      });
      postAction("/rooms/cesAllDayPriceRule/fetch", { hotelId: _info.id }).then(
        (res) => {
          if (res.success) {
            if (
              res.result &&
              res.result.cesAllDayPriceRule &&
              res.result.cesAllDayPriceRule.leaveTime
            ) {
              this.model.orderInfo.dueOutTimeSpan = moment(
                res.result.cesAllDayPriceRule.leaveTime,
                "HH:mm"
              );
            }
          }

          // this.loadRooms();
        }
      );

      // getAction(this.url.getBookingOrderInfo, {
      //   bookingNo: this.id,
      // }).then((res) => {
      //   if (res.success) {
      //     this.model = res.result;
      //     this.model.orderInfo.arrivalTime2 = moment(
      //       this.model.orderInfo.arrivalTime
      //     ).format("YYYY-MM-DD");
      //     this.model.orderInfo.dueOutTime2 = moment(
      //       this.model.orderInfo.dueOutTime
      //     ).format("YYYY-MM-DD");
      //     this.model.orderInfo.arrivalTimeSpan = moment(
      //       this.model.orderInfo.arrivalTime.substr(11, 15),
      //       "HH:mm"
      //     );
      //     this.model.orderInfo.dueOutTimeSpan = moment(
      //       this.model.orderInfo.dueOutTime.substr(11, 15),
      //       "HH:mm"
      //     );
      //     this.getbusCustomer();
      //   }
      // });
      this.getBookingOrderInfo();
    },
    setMain(id) {
      postAction("/business/busBookingRooms/set-main", { id: id }).then(
        (res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.getBookingOrderInfo();
          } else {
            this.$message.warning(res.message);
          }
        }
      );
    },
    pulsRoom2(record, index) {
      this.selectIndex = index;
      getAction("/rooms/cesRooms/can-user-rooms", {
        startOf:
          this.model.orderInfo.arrivalTime2 +
          " " +
          moment(this.model.orderInfo.arrivalTimeSpan).format("HH:mm"),
        endOf:
          this.model.orderInfo.dueOutTime2 +
          " " +
          moment(this.model.orderInfo.dueOutTimeSpan).format("HH:mm"),
        bookingType: this.model.orderInfo.bookingType,
        hourRoomRuleId: this.model.orderInfo.hourRoomId,
        layoutId: record.roomLayoutId,
        // orderId: this.model.orderInfo.id,
      }).then((res) => {
        if (res.success) {
          res.result.forEach((t) => {
            t.layout = Object.assign({}, t.layout, {
              presetNum: 0,
              oldTags: t.layout.canUseCount,
            });
            t.buildingRooms.forEach((b) => {
              b.floorRooms.forEach((f) => {
                this.$set(f, "check", 0);
              });
            });
          });
          this.canUserRooms2 = res.result;
          this.canUserRooms2[0].layout.presetNum = 1;
          this.$refs.modalSelectRoomForm2.add(this.canUserRooms2[0]);
          this.$refs.modalSelectRoomForm2.title = "排房";
          this.$refs.modalSelectRoomForm2.disableSubmit = false;
        }
      });
    },
    getBookingOrderInfo() {
      getAction("/business/busRoomBookingOrders/batch-order-detail", {
        batchId: this.id,
      }).then((res) => {
        if (res.success) {
          this.model = res.result;
          this.model.orderInfo.arrivalTime2 = moment(
            this.model.orderInfo.arrivalTime
          ).format("YYYY-MM-DD");
          this.model.orderInfo.dueOutTime2 = moment(
            this.model.orderInfo.dueOutTime
          ).format("YYYY-MM-DD");
          this.model.orderInfo.arrivalTimeSpan = moment(
            this.model.orderInfo.arrivalTime.substr(11, 15),
            "HH:mm"
          );
          this.model.orderInfo.dueOutTimeSpan = moment(
            this.model.orderInfo.dueOutTime.substr(11, 15),
            "HH:mm"
          );
          this.$set(this.model, "phone", "");
          this.$set(this.model, "contactName", "");
          this.getbusCustomer();
          this.loadRooms();
        }
      });
    },
    getbusCustomer() {
      getAction("/bus/busCustomer/list", {}).then((res) => {
        if (res.success) {
          this.customerList = res.result.records;
          this.oldcustomerList = JSON.parse(JSON.stringify(this.customerList));
          var find = this.oldcustomerList.find(
            (t) => t.id == this.model.orderInfo.contactId
          );
          if (find) {
            this.model.contactName = find.name;
            this.model.phone = find.phone;
          }
        }
      });
    },
    presetNumChange(e, record) {
      console.log(e);
      record.layout.canUseCount = record.layout.oldTags - e;
    },
    onCellChange(key, dataIndex, value) {
      const dataSource = [...this.canUserRooms];
      const target = dataSource[dataIndex];
      console.log("target", target);
      if (target && target.layout) {
        target.layout[key] = value;
        this.canUserRooms = dataSource;
      }
      console.log("this.canUserRooms", this.canUserRooms);
    },
    rowKey(record) {
      return record.layout.id;
    },
    loadRooms() {
      getAction("/rooms/cesRooms/can-user-rooms", {
        startOf:
          this.model.orderInfo.arrivalTime2 +
          " " +
          moment(this.model.orderInfo.arrivalTimeSpan).format("HH:mm"),
        endOf:
          this.model.orderInfo.dueOutTime2 +
          " " +
          moment(this.model.orderInfo.dueOutTimeSpan).format("HH:mm"),
        bookingType: this.model.orderInfo.bookingType,
        hourRoomRuleId: this.model.orderInfo.hourRoomId,
        orderId: this.model.orderInfo.bookingOrdersId,
      }).then((res) => {
        if (res.success) {
          res.result.forEach((t) => {
            var rooms = [];
            var layouts = this.model.roomIds.filter(
              (r) => r.roomLayoutId === t.layout.id
            );
            t.layout = Object.assign({}, t.layout, {
              presetNum: layouts.length,
              oldTags: t.layout.canUseCount + layouts.length,
            });
            t.buildingRooms.forEach((b) => {
              b.floorRooms.forEach((f) => {
                var exRoom = this.model.roomIds.some((r) => r.roomId === f.id);
                if (exRoom) {
                  rooms.push(f);
                }
                this.$set(f, "check", exRoom === true ? 1 : 0);
              });
            });

            this.$set(t, "rooms", rooms);
          });
          this.canUserRooms = res.result;
          console.log("this.canUserRooms", this.canUserRooms);
        }
      });
    },
    bookingTypeChange(e) {
      if (this.model.orderInfo.bookingType == 1) {
        this.model.orderInfo.hourRoomId = "";
      } else {
        var hourRoomRule = this.hourRoomRuleList[0];
        if (hourRoomRule) {
          this.model.orderInfo.hourRoomId = hourRoomRule.id;
        }
      }
      if (this.model.orderInfo.bookingType === 2) {
        var find = this.hourRoomRuleList.find(
          (t) => t.id === this.model.orderInfo.hourRoomId
        );
        if (find) {
          var mDate = moment(
            this.model.orderInfo.arrivalTime2 +
              " " +
              moment(this.model.orderInfo.arrivalTimeSpan).format("HH:mm")
          ).add(find.afterOpenRoom, "minutes");
          this.model.orderInfo.dueOutTime2 = mDate.format("YYYY-MM-DD");

          this.model.orderInfo.dueOutTimeSpan = moment(
            mDate.format("HH:mm"),
            "HH:mm"
          );
        }
      } else if (this.model.orderInfo.bookingType === 1) {
        var mDate = moment(this.model.orderInfo.arrivalTime2).add(1, "days");
        this.model.orderInfo.dueOutTime2 = mDate.format("YYYY-MM-DD");
      }
      this.loadRooms();
    },
    dayCountChange(e) {
      this.model.orderInfo.dueOutTime2 = moment(
        this.model.orderInfo.arrivalTime2
      )
        .add(e, "days")
        .format("YYYY-MM-DD");
      this.loadRooms();
    },
    disabledDate(current) {
      return current && current < moment().add(-1, "days").endOf("day");
    },
    arrivalTimeChange(e) {
      var v = parseInt(
        (new Date(this.model.orderInfo.dueOutTime2).getTime() -
          new Date(this.model.orderInfo.arrivalTime2).getTime()) /
          (1000 * 60 * 60 * 24)
      );
      if (v <= 0) {
        this.model.orderInfo.dueOutTime2 = moment(
          this.model.orderInfo.arrivalTime2
        )
          .add(1, "days")
          .format("YYYY-MM-DD");
      }
      v = parseInt(
        (new Date(this.model.orderInfo.dueOutTime2).getTime() -
          new Date(this.model.orderInfo.arrivalTime2).getTime()) /
          (1000 * 60 * 60 * 24)
      );
      this.model.orderInfo.dayCount = Math.abs(v);
      this.loadRooms();
    },
    arrivalTimeChange2(e) {
      var v = parseInt(
        (new Date(this.model.orderInfo.dueOutTime2).getTime() -
          new Date(this.model.orderInfo.arrivalTime2).getTime()) /
          (1000 * 60 * 60 * 24)
      );
      if (v <= 0) {
        this.model.orderInfo.arrivalTime2 = moment(
          this.model.orderInfo.dueOutTime2
        )
          .add(-1, "days")
          .format("YYYY-MM-DD");
      }
      v = parseInt(
        (new Date(this.model.orderInfo.dueOutTime2).getTime() -
          new Date(this.model.orderInfo.arrivalTime2).getTime()) /
          (1000 * 60 * 60 * 24)
      );
      this.model.orderInfo.dayCount = Math.abs(v);
      this.loadRooms();
    },
    arrivalTimeSpanChange(m, time) {
      console.log(time);
      console.log(moment(m).format("HH:mm"));
      if (this.model.orderInfo.bookingType === 2) {
        var find = this.hourRoomRuleList.find(
          (t) => t.id === this.model.orderInfo.hourRoomId
        );
        if (find) {
          var mDate = moment(
            this.model.orderInfo.arrivalTime2 +
              " " +
              moment(this.model.orderInfo.arrivalTimeSpan).format("HH:mm")
          ).add(find.afterOpenRoom, "minutes");
          this.model.orderInfo.dueOutTime2 = mDate.format("YYYY-MM-DD");

          this.model.orderInfo.dueOutTimeSpan = moment(
            mDate.format("HH:mm"),
            "HH:mm"
          );
        }
      }
      this.loadRooms();
    },
    hourRoomIdChange() {
      if (this.model.orderInfo.bookingType === 2) {
        var find = this.hourRoomRuleList.find(
          (t) => t.id === this.model.orderInfo.hourRoomId
        );
        if (find) {
          var mDate = moment(
            this.model.orderInfo.arrivalTime2 +
              " " +
              moment(this.model.orderInfo.arrivalTimeSpan).format("HH:mm")
          ).add(find.afterOpenRoom, "minutes");
          this.model.orderInfo.dueOutTime2 = mDate.format("YYYY-MM-DD");

          this.model.orderInfo.dueOutTimeSpan = moment(
            mDate.format("HH:mm"),
            "HH:mm"
          );
        }
      }
      this.loadRooms();
    },
    tagClose2(rindex, bindex, index) {
      // console.log(rindex, index);
      // console.log(this.canUserRooms[index]);
      this.canUserRooms[index].rooms.splice(rindex, 1);

      this.$set(
        this.canUserRooms[index].buildingRooms[bindex].floorRooms[rindex],
        "check",
        0
      );
      console.log(this.canUserRooms);
    },
    modalFormOk(e) {
      // this.modelDefault = Object.assign({}, this.modelDefault, {
      //   rooms: e,
      // });
      // this.edit(this.modelDefault);
      this.$set(this.canUserRooms[this.selectIndex], "rooms", e);
      console.log(this.canUserRooms[this.selectIndex]);
    },
    modalFormOk2(e) {
      this.$set(this.canUserRooms2[0], "rooms", e);
      console.log("modalFormOk2", this.canUserRooms2[0]);
      this.model.roomIds[this.selectIndex].roomId = e[0].id;
      this.model.roomIds[this.selectIndex].roomName = e[0].name;
      postAction(
        "/business/busBookingRooms/edit",
        this.model.roomIds[this.selectIndex]
      ).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    pulsRoom(index) {
      this.selectIndex = index;
      this.$refs.modalSelectRoomForm.add(this.canUserRooms[this.selectIndex]);
      this.$refs.modalSelectRoomForm.title = "排房";
      this.$refs.modalSelectRoomForm.disableSubmit = false;
    },
    handleSearch(value) {
      let result;
      if (!value) {
        result = this.oldcustomerList;
      } else {
        result = this.oldcustomerList.filter((t) => t.name.includes(value));
      }
      this.customerList = result;
    },
    handleSelectMember(e) {
      var find = this.customerList.find((t) => t.id === e);
      this.model.phone = find.phone;
      this.model.contactName = find.name;
      this.model.orderInfo.contactId = find.id;
    },
    moment,
    onChange(date, dateString) {
      console.log(date, dateString);
    },
    add() {
      this.edit(this.modelDefault);
    },
    addList(roomLiveList) {
      this.modelDefault = Object.assign({}, this.modelDefault, {
        rooms: roomLiveList,
      });
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
    },
    cancelOrder() {
      this.disabled = true;
      this.getBookingOrderInfo();
    },
    editOrder() {
      this.submitForm();
    },
    submitForm() {
      const that = this;
      that.model.roomIds = [];
      that.model.layoutDayPrices = [];
      that.canUserRooms.forEach((t) => {
        console.log("foreach");
        var lastlayoutId = t.layout.id;
        t.buildingRooms.forEach((b) => {
          b.floorRooms.forEach((f) => {
            if (f.check && f.check === 1) {
              that.model.roomIds.push({
                layoutId: t.layout.id,
                roomId: f.id,
                roomLayoutId: t.layout.id,
              });
            }
          });
        });
        const roomIds = that.model.roomIds.filter(
          (it) => it.layoutId === lastlayoutId
        );
        var len = roomIds.length;
        for (var i = len; i < t.layout.presetNum; i++) {
          that.model.roomIds.push({
            layoutId: t.layout.id,
            roomId: null,
            roomLayoutId: t.layout.id,
          });
        }
        for (var b = 0; b < that.model.orderInfo.dayCount; b++) {
          var dayTime = moment(this.model.orderInfo.arrivalTime2)
            .add(b, "days")
            .format("YYYY-MM-DD");
          that.model.layoutDayPrices.push({
            bookingType: 1,
            dayTime: dayTime,
            price: t.layout.favPrice,
            roomLayoutId: t.layout.id,
          });
        }
      });
      console.log("this.model", this.model);
      if (that.model.roomIds.length <= 0) {
        that.$message.warning("请先添加房间");
        return;
      }
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.model.orderInfo.arrivalTime =
            this.model.orderInfo.arrivalTime2 +
            " " +
            moment(this.model.orderInfo.arrivalTimeSpan).format("HH:mm");
          this.model.orderInfo.dueOutTime =
            this.model.orderInfo.dueOutTime2 +
            " " +
            moment(this.model.orderInfo.dueOutTimeSpan).format("HH:mm");
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.orderInfo.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl = "/business/busRoomBookingOrders/add-batch";
            method = "post";
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                if (that.model.orderInfo.id) {
                  that.getBookingOrderInfo();
                  that.disabled = true;
                } else {
                  that.$emit("ok");
                }
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
            });
        }
      });
    },
  },
};
</script>
<style scoped>
/deep/.ant-divider-horizontal {
  margin: 12px 0 !important;
}
/deep/ .ant-form-item {
  margin-bottom: 5px !important;
}
.orange_color {
  border-color: #ff9e35 !important;
  color: #ff9e35 !important;
  vertical-align: initial;
}
.booking_circle_span {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  color: #f24e4c;
  border: 1px solid #f24e4c;
  font-size: 12px;
  text-align: center;
  line-height: 16px;
  display: inline-block;
}
</style>
