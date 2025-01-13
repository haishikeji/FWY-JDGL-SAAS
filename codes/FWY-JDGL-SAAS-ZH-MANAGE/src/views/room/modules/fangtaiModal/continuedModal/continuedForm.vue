<template>
  <a-spin :spinning="confirmLoading">
    <a-tabs default-active-key="1" @change="callback">
      <!-- <a-tab-pane key="2" tab="续住记录"></a-tab-pane> -->
      <a-tab-pane key="1" tab="操作">
        <a-row>
          <a-form-model style="margin-bottom: 10px ;">
            <a-col :span="24">
              <a-form-model-item style="margin-bottom: 10px" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                <a-radio-group v-model="model.batchWay" @change="batchWayChange">
                  <a-radio :value="1">按天数</a-radio>
                  <a-input-number v-model="model.batchDay" style="margin-right: 30px" @change="batchChangeDay"></a-input-number>
                  <a-radio :value="2">按预离时间</a-radio>
                  <a-date-picker
                      v-model="model.batchTime"
                      :disabled-date="disabledDate"
                      :disabled-time="disabledTime"
                      show-time
                      format="YYYY-MM-DD HH:mm:ss"
                      @change="batchChangeTime" />
                </a-radio-group>
              </a-form-model-item>
            </a-col>
            <!--            <a-col :span="12">-->
            <!--              <a-button @click="batchChangeDay" type="primary" style="float: right; margin-right: 20px">批量续住</a-button>-->
            <!--            </a-col>-->
          </a-form-model>

          <a-col :span="24">
            <a-table
                ref="table"
                size="middle"
                height="450px"
                overflow-y="scroll"
                bordered
                rowKey="id"
                :columns="columns"
                :dataSource="tableData"
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
              <template slot="newDueOutTime" slot-scope="text, record, index">
                <a-date-picker
                    v-model="record.newDueOutTime"
                    :disabled-date="disabledDate"
                    :disabled-time="disabledTime"
                    show-time
                    format="YYYY-MM-DD HH:mm:ss"
                    @change="timeChange(record)"/>
              </template>
              <!-- 续住天数-->
              <template slot="dayNum" slot-scope="text, record, index">
                <a-input-number  v-model="record.dayNum" placeholder="请输入续住天数" style="width: 90%" @change="dayChange(record)" />
              </template>
              <!-- 支付方式-->
              <template slot="payType" slot-scope="text, record, index">
                <a-select style="width: 100px" v-model="record.payType" :disabled="record.dayNum < 0">
                  <a-select-option v-for="(item, key) in record.payTypeList" :key="key" :value="item.id" :disabled="item.disable">{{ item.name }}</a-select-option>
                </a-select>
              </template>
              <!-- 支付金额-->
              <template slot="payMoney" slot-scope="text, record, index">
                <a-input-number  :min="0" v-model="record.payMoney" style="width: 80%" :disabled="record.dayNum <= 0"/>
              </template>
              <template slot="breakfastNum" slot-scope="text, record, index">
                <a-input-number  :min="0" v-model="record.breakfastNum" style="width: 80%"/>
              </template>
              <!-- 总费用-->
              <template slot="allMoney" slot-scope="text, record, index">
                <a>{{record.layoutDayPriceList.reduce((pre,cur)=> pre+cur.price*1,0 ) || 0}}</a>
                <a-popover placement="right" v-if="record.dayNum>0">
                  <template slot="content">
                    <div style="max-height:400px;overflow:auto;width:100%;">
                      <div v-for="(item, index) in record.layoutDayPriceList" :key="index">
                        {{item.dayTime}}
                        <a-input-number :min="0" v-model="item.price"></a-input-number>
                      </div>
                    </div>
                  </template>
                  <template slot="title">
                    <a-input-number :min="0" @change="batchPrice($event,record)" placeholder="批量改价"></a-input-number>
                  </template>
                  <a-icon type="edit" />
                </a-popover>
              </template>
            </a-table>
          </a-col>
          <a-col :span="24" style="text-align: end">
            <a-checkbox style="margin-top: 20px; margin-right: 10px" default-checked>打印续住单</a-checkbox>
          </a-col>
        </a-row>
      </a-tab-pane>
    </a-tabs>

  </a-spin>
</template>

<script>
import {
  httpAction,
  getAction, postAction
} from '@/api/manage'
import {
  formatDate,
  validateDuplicateValue
} from '@/utils/util'
import moment from 'moment/moment'

const columns = [
  {
    title: '房间号',
    dataIndex: 'roomName',
    width: 80,
    align: 'center',
    // scopedSlots: { customRender: 'roomId' }
  },
  {
    title: '姓名',
    dataIndex: 'customerName',
    width: 80,
    align: 'center',
    // scopedSlots: { customRender: 'roomId' }
  },
  {
    title: '原预离时间',
    dataIndex: 'dueOutTime',
    width: 120,
    align: 'center',
  },
  {
    title: '续住天数',
    // dataIndex: 'dayNum',
    width: 80,
    align: 'center',
    scopedSlots: { customRender: 'dayNum' }
  },
  {
    title: '收款方式',
    // dataIndex: 'dayNum',
    width: 80,
    align: 'center',
    scopedSlots: { customRender: 'payType' }
  },
  {
    title: '支付金额',
    // dataIndex: 'dayNum',
    width: 90,
    align: 'center',
    scopedSlots: { customRender: 'payMoney' }
  },
  {
    title: '早餐券',
    width: 80,
    align: 'center',
    scopedSlots: { customRender: 'breakfastNum' }
  },
  {
    title: '时间',
    width: 80,
    align: 'center',
    scopedSlots: { customRender: 'newDueOutTime' }
  },
  {
    title: '合计房价',
    width: 80,
    align: 'center',
    scopedSlots: { customRender: 'allMoney' }
  },
]
const date = new Date()
const endDate = new Date(date.setDate(date.getDate() + 1))
export default {
  name: "ContinuedForm",
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      model: {
        day: 0,
        batchDay: 1,
        batchTime: moment(endDate).format('yyyy-MM-DD') + ' 13:00',
        batchWay: 1
      },
      labelCol: {
        xs: {
          span: 24
        },
        sm: {
          span: 5
        },
      },
      wrapperCol: {
        xs: {
          span: 24
        },
        sm: {
          span: 16
        },
      },
      confirmLoading: false,

      payTypeList: [],
      tableTypeList: [],
      regionList: [],
      PriceData: [],
      isAddDate: true,
      payList: [],
      columns: columns,
      tableData: [],
      selectedRowKeys: [],
      selectionRows: [],
    };
  },

  computed: {
    // dayDataArr() {
    //   let arr = [];
    //   console.log(this.dayNum);
    //   if (this.dayNum && this.dayNum > 0) {
    //     arr.length = this.dayNum;
    //     for (let i = 0; i < arr.length; i++) {
    //       arr[i] = {
    //         day: this.addDate(this.model.livingData.livingOrder.dueOutTime, i),
    //         price: this.model.layout.marketPrice,
    //       };
    //     }
    //     this.PriceData = arr;
    //   } else {
    //     return [];
    //   }
    //
    //   return arr;
    // }
  },
  created() {
    var _info = JSON.parse(localStorage.getItem("storeInfo"));
    if (_info) {
      this.model.hotelId = _info.id;
    }
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
    getAction('/business/busRoomPayType/list', {
      pageSize: 99999,
      pageNo: 1,
    }).then((res) => {
      if (res.success) {
        this.payTypeList = res.result.records;
        this.loadDate()
      }
    });
  },
  methods: {
    loadDate() {
      getAction('/business/busRoomBookingOrders/get-continue-order', { bookingOrderId: this.model.bookingOrderId }).then(resp => {
        if (resp.success) {
          var vipIndex = this.payTypeList.findIndex((t) => t.name.includes('会员'))
          var danweiIndex = this.payTypeList.findIndex((t) => t.name.includes('单位'))
          this.tableData = resp.result
          this.tableData.forEach(e => {
            e.newDueOutTime = e.dueOutTime
            e.dayNum = 0
            e.layoutDayPriceList = []
            e.payMoney = 0
            let payTypeList = JSON.parse(JSON.stringify(this.payTypeList))
            this.$set(payTypeList[vipIndex], 'disable', e.vipCustomerId == null || e.vipCustomerId === '')
            this.$set(payTypeList[danweiIndex], 'disable', e.contractTeamId == null || e.contractTeamId === '')
            this.$set(e, 'payTypeList', payTypeList)
            if (e.id === this.model.livingOrderId) {
              this.selectedRowKeys.push(e.id)
              this.selectionRows.push(e)
              e.dayNum = 1
              this.dayChange(e)
            }
          })
        }
      })
    },
    disabledDate(current) {
      return current && current < moment().add(-1, 'days').endOf('day')
    },
    disabledTime() {
      return {
        disabledHours: () => this.range(0, 24),
        disabledMinutes: () => this.range(0, 60),
        disabledSeconds: () => this.range(0, 60)
      };
    },
    range (start, end) {
      const result = []
      for (let i = start; i < end; i++) {
        result.push(i)
      }
      return result
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      console.log(selectedRowKeys, selectionRows)
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectionRows
    },
    changePriceData(record){
      let arr = [];
      console.log(record.dayNum);
      if (record.dayNum && record.dayNum > 0) {
        arr.length = record.dayNum;
        for (let i = 0; i < arr.length; i++) {
          arr[i] = {
            dayTime: this.addDate(record.dueOutTime, i),
            price: record.roomLayout.marketPrice,
            roomLayoutId: record.roomLayout.id,
            roomId: record.roomId,
            livingOrderId: record.id
          };
        }
        record.layoutDayPriceList = arr;
      } else {
        record.layoutDayPriceList = [];
      }
    },
    // 批量修改的方式改变
    batchWayChange() {
      this.batchChangeDay()
      this.batchChangeTime()
    },
    // 批量修改勾选的预离天数
    batchChangeDay() {
      if (this.model.batchWay === 1) {
        this.selectionRows.forEach(e => {
          e.dayNum = this.model.batchDay
          this.dayChange(e)
        })
        this.$forceUpdate()
      }
    },
    // 批量修改勾选的预离时间
    batchChangeTime() {
      if (this.model.batchWay === 2) {
        this.selectionRows.forEach(e => {
          e.newDueOutTime = this.model.batchTime
          this.timeChange(e)
        })
        this.$forceUpdate()
      }
    },
    // 预离日期改变
    timeChange(record) {
      let nowDate = new Date()
      if (record.newDueOutTime.batchTime < nowDate) {
        this.$message.warning('退租时间不能小于当前时间')
        return
      }
      record.dayNum = this.daysBetween(record.dueOutTime, record.newDueOutTime)
      this.changePriceData(record)
      // this.model.startValue = e._i;
      this.$forceUpdate()
    },
    // 预离天数改变
    dayChange(record) {
      // if (!this.isAddDate) {
      //   record.dayNum = -record.dayNum
      // }
      let newTime = this.addDate(record.dueOutTime, record.dayNum)
      if (record.dayNum < 0 && moment(newTime) < moment(endDate.format('yyyy-MM-dd'))) {
        record.dayNum = 1
        this.dayChange(record)
        this.$message.warning('预离时间不能晚于当前时间')
        return
      }
      record.newDueOutTime = newTime
      console.log(record)
      this.changePriceData(record)
      this.$forceUpdate()
    },

    /**
     * 根据传入的数字将日期加上对应的天数
     * @param {String} date 日期
     * @param {Number} days 天数
     * @return {String} 返回日期
     */
    addDate(date, days) {
      console.log(date, days);
      let time = new Date(date);
      time.setDate(time.getDate() + days);
      let m = time.getMonth() + 1;
      let d = time.getDate();
      if (m < 10) {
        m = "0" + m;
      }
      if (d < 10) {
        d = "0" + d;
      }
      return time.getFullYear() + '-' + m + '-' + d + ' ' + time.getHours() + ':' + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ':' + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds())
    },

    //计算两个日期之间相隔多少天
    daysBetween(date1, date2) {
      const oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
      const firstDate = new Date(date1);
      const secondDate = new Date(date2);
      const diffDays = (secondDate - firstDate) / oneDay;
      this.isAddDate = diffDays >= 0;
      if (Math.round(diffDays) <= 0) {

        this.PriceData = []
      }
      return Math.round(diffDays);
    },
    batchPrice(value, record) {
      console.log(value)
      console.log(record.layoutDayPriceList,'record.layoutDayPriceList')
      record.layoutDayPriceList.forEach(item => {
        item.price = value
      })
      this.$forceUpdate()
    },
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      console.log(record, 'record=============')
      this.model = Object.assign(this.model, record)
      // this.model.startValue = record.livingData.livingOrder.dueOutTime
      this.visible = true
      this.loadDate()
    },

    chooseVipPay() {
    },
    submitForm() {
      const that = this
      let params = []
      let cardInfoList = []
      if (this.selectionRows.length === 0) {
        that.$emit("ok");
        return
      }
      if (this.selectionRows.some(e => e.payMoney > 0 && e.payType == null)) {
        this.$message.warning('请选择支付方式')
        return
      }
      this.selectionRows.forEach(e => {
        if (e.dayNum === 0) {
          return
        }
        let obj = {
          id: e.id,
          payType: e.payType,
          roomId: e.roomId,
          payMoney: e.payMoney,
          breakfastNum: e.breakfastNum,
          dayNum: e.dayNum == null ? 0 : e.dayNum,
          layoutDayPriceList: e.layoutDayPriceList,
          dueOutTime: moment(e.newDueOutTime).format('yyyy-MM-DD HH:mm')
        }
        params.push(obj)
        let cardInfo = {
          customerName: e.customerName,
          phone: e.phone,
          arriveTime: moment(e.arrivalTime).format('yyyy-MM-DD HH:mm'),
          dueOutTime: moment(e.newDueOutTime).format('yyyy-MM-DD HH:mm')
        }
        cardInfoList.push(cardInfo)
      })
      httpAction(`/business/busRoomBookingOrders/isBooked`, params, 'post')
          .then(res => {
            if (!res.success) {
              return
            }
            if (res.result.length > 0) {
              let mess = '房间已被预定：'
              res.result.forEach(e => {
                mess += '\n' + e.customerName + '，电话：' + e.phone + ' 预抵时间：' + e.arrivalTime + '。'
              })
              mess += '是否续住?'
              this.$confirm({
                title: '提示',
                content: mess,
                onOk: function () {
                  that.confirmLoading = true;
                  httpAction(`/business/busRoomBookingOrders/continue-living`, params, 'post')
                      .then((res) => {
                        if (res.success) {
                          that.$message.success(res.message)
                          that.$emit('ok', cardInfoList)
                        } else {
                          that.$message.warning(res.message);
                        }
                      })
                      .finally(() => {
                        that.confirmLoading = false;
                      });
                },
              })
            } else {
              that.confirmLoading = true;
              httpAction(`/business/busRoomBookingOrders/continue-living`, params, 'post')
                  .then((res) => {
                    if (res.success) {
                      that.$message.success(res.message);
                      that.$emit("ok", cardInfoList);
                    } else {
                      that.$message.warning(res.message);
                    }
                  })
                  .finally(() => {
                    that.confirmLoading = false;
                  });
            }
          })
    },

    callback(key) {
      console.log(key)
    },
  },
};
</script>
