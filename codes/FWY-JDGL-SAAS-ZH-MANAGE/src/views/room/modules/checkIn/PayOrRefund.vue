<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model
        ref="form"
        :model="model"
        :rules="validatorRules"
        slot="detail"
      >
        <a-row>
          <!-- <a-col :span="12">

          </a-col> -->
          <div class="crad_style">
            <a-col :span="8" class="refund_style" v-show="showYinshou">
              <span>{{ '应' + (model.money < 0 ? '退' : '收') }}: {{ realityAmount.toFixed(2) }}元</span>
              <!-- </a-form-model-item> -->
            </a-col>
            <a-col :span="16">
              <a-form-model-item
                label="收款金额"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                {{ model.collection != null ?model.collection.toFixed(2) : 0}}元
              </a-form-model-item>
              <a-form-model-item
                label="消费金额"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                {{ model.consumption != null ? model.consumption.toFixed(2) : 0}}元
                <a-switch v-model="model.coupon" />优惠
                <a-radio-group v-if="model.coupon" v-model="model.preferentialType">
                  <a-radio :value="1"> 抹零 </a-radio>
                  <a-radio :value="2"> 减现 </a-radio>
                  <a-radio :value="3"> 打折 </a-radio>
                </a-radio-group>
                <template
                  v-if="model.preferentialType === 3"
                >打<a-input-number
                  style="width: 60px"
                  v-model="model.discount"
                  :min="1"
                  :max="99"
                ></a-input-number
                >折
                </template>
                <template v-if="model.preferentialType == 2">
                  优惠金额: <a-input-number
                    style="width: 80px"
                    :min="0"
                    v-model="model.couponFirstAmount"
                    placeholder="请输入优惠金额"
                  ></a-input-number
                  >元
                <!-- <a-form-model-item
                  label="优惠金额"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="couponFirstAmount"
                >

                </a-form-model-item> -->
                </template>
              </a-form-model-item>
              <!-- <a-col :span="24" v-if="model.preferentialType == 2"> -->

              <a-form-model-item
                label="优惠券"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="refund"
                v-if="model.coupon"
              >
                <a-select
                  v-if="model.couponCard"
                  v-model="model.couponId"
                  style="width: 200px"
                  placeholder="优惠券"
                  :allowClear="true"
                >
                  <a-select-option
                    :value="couponItem.id"
                    v-for="couponItem in memeberCouponList"
                    :key="couponItem.id"
                  >{{ couponItem.couponsName }}</a-select-option
                  >
                </a-select>
                <a-switch v-model="model.couponCard" />使用优惠券
              </a-form-model-item>
              <a-form-model-item
                v-if="model.coupon"
                label="惠后金额"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                {{ couponAmount.toFixed(2) }}元
              </a-form-model-item>
              <!-- </a-col> -->

              <template v-if="model.coupon">
              <!-- <a-col :span="24">
                <a-form-model-item
                  label="优惠方式"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  prop="refund"
                >
                  <a-radio-group v-model="model.preferentialType">
                    <a-radio :value="1"> 抹零 </a-radio>
                    <a-radio :value="2"> 减现 </a-radio>
                    <a-radio :value="3"> 打折 </a-radio>
                  </a-radio-group>
                  <template
                    v-if="model.preferentialType === 3"
                  >打<a-input-number
                    style="width: 50px"
                    v-model="model.discount"
                    :min="1"
                    :max="9"
                  ></a-input-number
                  >折
                  </template>
                </a-form-model-item>
              </a-col> -->

              <!-- <a-col :span="24">

                <a-col :span="24">

              </a-col>
              </a-col> -->
              </template>
            <!-- <a-form-model-item
                :label="&quot;实&quot; + (this.isRefund ? &quot;退&quot; : &quot;收&quot;) + &quot;金额&quot;"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                {{ realityAmount.toFixed(2) }}元
              </a-form-model-item>
            </a-col>
            <a-col :span="24">

            </a-col> -->

            <!-- <a-col :span="24">-->

            </a-col>
          </div>
          <div class="presentation_style" v-if="!isRefund">应收金额=消费合计/惠后金额-已收合计</div>
          <a-col :span="14" class="receipt_style" v-if="!isRefund">
            <a-form-model-item
              label="收款金额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <div v-for="(item, index) in payList" :key="index">
                <a-row type="flex" :key="index">
                  <a-col :span="9">
                    <a-input-number
                      style="width: 100px"
                      v-model="item.money"
                      :min="0"
                    ></a-input-number>
                  </a-col>
                  <a-col :span="6">
                    <a-select
                      v-model="item.payType"
                      @change="(event) => onChange(event, item)"
                    >
                      <a-select-option
                        :value="item2.id"
                        v-for="item2 in payTypeList"
                        :key="item2.id"
                        :disabled="item2.delFlag == 99"
                      >
                        {{ item2.name }}
                      </a-select-option>
                    </a-select></a-col
                  >
                  <a-col :span="6">
                    <a-icon
                      v-if="index == payList.length - 1"
                      type="plus-circle"
                      class="dynamic-delete-button"
                      @click="puls()" />
                    <a-icon
                      v-if="payList.length > 1"
                      type="minus-circle"
                      style="color: #f56c6c"
                      class="dynamic-delete-button"
                      @click="() => remove(index)"
                    /></a-col>
                </a-row>

                <a-row type="flex" v-if="item.isVipMemmber">
                  <a-col :span="12"> 姓名：{{ memberCard.name }} </a-col>
                  <a-col :span="12"> 卡号：{{ memberCard.cardNo }} </a-col>
                  <a-col :span="12">
                    会员级别： {{ memberCard.gradeName }}</a-col
                  >
                  <a-col :span="12"> 余额：{{ memberCard.balance }} </a-col>
                  <a-col :span="12"> 积分：{{ memberCard.integral }} </a-col>
                  <!-- <a-col :span="12"> 本次扣后剩余： </a-col>
                <a-col :span="12"> 本次新增积分： </a-col> -->
                </a-row>
                <a-row type="flex" v-if="item.isDanwei">
                  <a-col :span="12">
                    协议单位：{{ agreementUnitData.customerName }}
                  </a-col>
                  <a-col :span="12">
                    签约开始日期：{{ danwei.signTime }}
                  </a-col>
                  <a-col :span="12">
                    签约结束日期： {{ danwei.effective }}</a-col
                  >
                  <a-col :span="12">
                    可用额度：{{ agreementUnitData.balance || 0 }}
                  </a-col>
                </a-row>
              </div>
            </a-form-model-item>
          </a-col>
          <div class="presentation_style_two" v-if="isRefund">退款:
            <a-button danger type="link" @click="addCustomRefund">新增自定义退款</a-button>
          </div>
          <!--退款表格-->
          <a-col :span="24" v-if="isRefund">
            <a-table :columns="columns" :data-source="combineFeeList" bordered :pagination="false" :scroll="{ y: 250 }" >
              <template #footer>
                <span style="font-size: 15px;margin-left: 410px; margin-right: 10px">{{ '退款合计：' + Math.abs(sumAmount) + ' 元' }}</span>&nbsp; &nbsp;
                <span style="font-size: 15px"> {{ '还应退：' + (realityAmount - Math.abs(sumAmount)).toFixed(2) + ' 元' }}</span>
              </template>
              <template slot="payType" slot-scope="text, record, index">
                {{ getPayTypeText(text) }}
              </template>
              <template slot="subjectType" slot-scope="text, record, index">
                {{ getSubjectTypeText(text,record) }}
              </template>
              <template slot="roomId" slot-scope="text, record, index">
                {{ getRoomName(text) }}
              </template>
              <template slot="payType" slot-scope="text, record, index">
                <a-select style="width: 100px" v-model="record.payType" :disabled="record.createTime !== '自定义退款'">
                  <a-select-option v-for="(item, key) in payTypeList" :key="key" :value="item.id" :disabled="item.disable">{{ item.name }}</a-select-option>
                </a-select>
              </template>
              <template slot="prerefund" slot-scope="text, record, index">
                <a-input-number :min="0" :max="record.money - record.returnMoney" v-model="record.prerefund" style="width: 80%"></a-input-number>
              </template>
              <template slot="operation" slot-scope="text, record, index">
                <!-- <a-popconfirm
                  title="Sure to delete?"
                  :disable="record.createTime == '自定义退款'"
                >
                  <a>删除</a>
                </a-popconfirm> -->
                <a-button danger v-if="record.createTime === '自定义退款'" type="link" @click="reduceCustomization(record)">删除</a-button>
                <!-- <a-button
                v-if="record.returnMoney"
                  @click="reduceCustomization"
                ><a-icon type="exception" /></a-button
                >
                <span v-else>{{ record.returnMoney }}</span> -->
              </template>
            </a-table>
          </a-col>
          <a-col :span="20" class="settle_style" v-if="!isRefund">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <span style="font-size: 18px;margin-left: 20px; margin-right: 20px">{{ '实收合计：' + Math.abs(sumAmount) + ' 元' }}</span>
              <span style="font-size: 18px"> {{ '还差' + (realityAmount - Math.abs(sumAmount)).toFixed(2) + ' 元' }}</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" style="text-align: end">
            <a-checkbox default-checked>打印结账单</a-checkbox>
            <a-checkbox default-checked>打印发票二维码</a-checkbox>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction, postAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'
import moment from 'moment/moment'
import { isNumber } from 'xe-utils/methods'
export default {
  name: 'PayOrRefund',
  components: {},
  props: {
    // 表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    showYinshou: {
      type: Boolean,
      default: true,
      required: false
    }
  },
  data() {
    return {
      feeList: [],
      originalFeeList: [],
      combineFeeList: [],
      isRefund: false,
      model: {
        coupon: false,
        discount: 1,
        couponCard: false,
        couponType: 1,
        couponFirstAmount: 0,
        billAmount: 0,
        selectedFeeIds: []
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 4 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 20 }
      },
      confirmLoading: false,
      validatorRules: {
        mobile: [
          {
            required: true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入手机号!'
          }
        ],
        cardNo: [{ required: true, message: '请输入会员卡号!' }],
        gradeId: [{ required: true, message: '请输入等级类型!' }],
        payType: [{ required: true, message: '请输入付款类型!' }],
        paymentMethod: [{ required: true, message: '请输入付款方式!' }],
        customerName: [{ required: true, message: '请输入会员姓名!' }],
        sex: [{ required: true, message: '请输入性别!' }],
        certificateType: [{ required: true, message: '请输入证件类型!' }],
        validity: [{ required: true, message: '请输入有效期!' }]
      },
      url: {
        add: '/business/busRoomBookingOrders/booking-to-live',
        edit: '/business/busMemberCard/edit',
        queryById: '/business/busMemberCard/queryById'
      },
      gradeList: [],
      paymentMethodList: [],
      staffList: [],
      customerList: [],
      oldcustomerList: [],
      payTypeList: [],
      memberCard: {},
      danwei: {},
      isVipMemmber: false,
      isDanwei: false,
      payList: [],
      memeberCouponList: [],
      agreementUnitData: {},
      copyRealityAmount: '', // 原本值
      allFeeMoney: 0,
      allReturnFee: 0,
      columns: [

        {
          // title: '序号',
          key: 'key',
          align: 'center',
          customRender: (text, record, index) => `${index + 1}`// 此处为重点
        },
        {
          title: '可退金额（元）',
          dataIndex: 'id',
          align: 'center',
          width: '15%',
          customRender: function (text, record) {
            return record.money
          }
        },
        {
          title: '退款方式',
          dataIndex: 'payType',
          align: 'center',
          // width: '40%',
          scopedSlots: { customRender: 'payType' }
        },
        {
          title: '退款金额（元）',
          dataIndex: 'prerefund',
          align: 'center',
          scopedSlots: { customRender: 'prerefund' }
        },
        {
          title: '已退金额（元）',
          align: 'center',
          customRender: function (text, record) {
            return record.prerefund
          }
        },
        {
          title: '操作',
          // width: '150px',
          align: 'center',
          scopedSlots: { customRender: 'operation' }
          // dataIndex: 'operation',
        }
      ]
//       [
//   {
//     title: '房间号',
//     dataIndex: 'roomName',
//     width: 80,
//     align: 'center',
//     // scopedSlots: { customRender: 'roomId' }
//   },
//   {
//     title: '支付类型',
//     dataIndex: 'payType',
//     align: 'center',
//     width: 80,
//     scopedSlots: { customRender: 'payType' }
//   },
//   {
//     title: '费项',
//     dataIndex: 'subjectType',
//     align: 'center',
//     width: 100,
//     scopedSlots: { customRender: 'subjectType' }
//   },
//   {
//     title: '入账日期',
//     dataIndex: 'dayTime',
//     align: 'center',
//     width: 110,
//     customRender: function (text, record) {
//       return record.createTime.substring(0, 10)
//     }
//   },
//   {
//     title: '支付金额',
//     dataIndex: 'id',
//     align: 'center',
//     width: 90,
//     customRender: function (text, record) {
//       return record.money
//     }
//   },
//   {
//     title: '退款方式',
//     // dataIndex: 'id',
//     align: 'center',
//     width: 130,
//     scopedSlots: { customRender: 'refundType' }
//   },
//   {
//   title: '本次退款',
//   dataIndex: 'prerefund',
//     align: 'center',
//     scopedSlots: { customRender: 'prerefund' }
//   },
//   {
//     title: '已退款',
//     align: 'center',
//     dataIndex: 'returnMoney',
//   }
// ]
    }
  },
  computed: {
    discount() {
      return 1
    },
    formDisabled() {
      return this.disabled
    },
    sumAmount() {
      console.log(this.isRefund)
      if (this.isRefund) {
        let sum = this.combineFeeList.reduce(function (total, item) {
          console.log(item)
          if (item && item.prerefund != null) {
            return total + item.prerefund
          } else {
            return total
          }
        }, 0)
        console.log(sum,'sum1')
        return sum == null ? 0 : sum
      } else {
        let sum = this.payList.reduce(function (total, item) {
          return total + Number(item.money)
        }, 0)
        console.log(sum,'sum2')
        return sum == null ? 0 : sum
      }
    },
    // sumAmount() {
    //   var sum = this.payList.reduce(function (total, item) {
    //     return total + item.money
    //   }, 0)
    //   return sum
    // },
    couponAmount() {
      var sum = 0
      if (this.model.coupon) {
        if (this.model.preferentialType == 1) {
          sum = Math.floor(this.model.consumption)
        } else if (this.model.preferentialType == 2) {
          sum = this.model.consumption - (this.model.couponFirstAmount || 0)
        } else if (this.model.preferentialType == 3) {
          if (this.model.discount > 10) {
            // console.log(this.model.consumption);
            // console.log(this.model.discount);
            sum = parseFloat(
              ((this.model.consumption * this.model.discount) / 100).toFixed(2)
            )
          } else if (this.model.discount == 10) {
            sum = parseFloat(
              ((this.model.consumption * this.model.discount) / 10).toFixed(2)
            )
          } else if (this.model.discount < 10) {
            sum = parseFloat(
              ((this.model.consumption * this.model.discount) / 10).toFixed(2)
            )
          }
        }
      } else {
        sum = this.model.consumption == null ? 0 : this.model.consumption
      }
      return sum
    },
    realityAmount() {
      console.log(this.model)
      // 实际要收的钱,为负表示要退的钱
      let relMoney = this.couponAmount - this.model.collection
      this.isRefund = relMoney < 0
      let result = 0
      if (this.model.couponCard) {
        var find = this.memeberCouponList.find(
            (t) => t.id == this.model.couponId
        )
        if (find) {
          this.isRefund = relMoney - find.cost < 0
          result = Math.abs(relMoney - find.cost)
        }
      }
      result = Math.abs(relMoney)
      if (this.payList && this.payList.length > 0 && this.copyRealityAmount != result) {
        this.payList = [this.payList[0]]
        this.payList[0].money = result
      }
      this.copyRealityAmount = result
      return result == null ? 0 : result
    }
  },
  created() {
    var _info = JSON.parse(localStorage.getItem('storeInfo'))
    if (_info) {
      this.model.hotelId = _info.id
    }
    // 备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    // this.refundRequest()
    // getAction("/business/busRoomPayType/list", {
    //   pageSize: 99999,
    //   pageNo: 1,
    // }).then((res) => {
    //   if (res.success) {
    //     this.payTypeList = res.result.records;
    //   }
    // });
  },
  methods: {
    getbusRoomPayType() {
      getAction('/business/busRoomPayType/list', {
        pageSize: 99999,
        pageNo: 1
      }).then((res) => {
        console.log(res)
        if (res.success) {
          this.payTypeList = res.result.records
          if (this.payTypeList && this.payTypeList.length > 0) {
            if (
                !this.model.vipCustomerId ||
                this.model.vipCustomerId.length == 0
            ) {
              var index = this.payTypeList.findIndex((t) =>
                  t.name.includes('会员')
              )
              if (index >= 0) {
                this.payTypeList[index].delFlag = 99
              }
            }
            if (
                !this.model.contractTeamId ||
                this.model.contractTeamId.length == 0 ||
                !this.model.contractTeamProtocolId ||
                this.model.contractTeamProtocolId.length == 0
            ) {
              var index = this.payTypeList.findIndex((t) =>
                  t.name.includes('单位')
              )
              if (index >= 0) {
                this.payTypeList[index].delFlag = 99
              }
            }
            this.payList = [
              {
                money: this.realityAmount,
                payType: this.payTypeList[0].id,
                isVipMemmber: false,
                isDanwei: false
              }
            ]
            console.log(this.payList,'payliet')
          }
        }
      })
    },
    puls() {
      var sum = this.payList.reduce(function (total, item) {
        return total + item.money
      }, 0)
      var money = this.realityAmount - sum
      if (money < 0) {
        money = 0
      }
      this.payList.push({
        money: money,
        payType: this.payTypeList[0].id,
        isVipMemmber: false,
        isDanwei: false
      })
    },
    remove(index) {
      this.payList.splice(index, 1)
    },
    onChange(e, value) {
      console.log('value', value)
      console.log('e', e)
      var find = this.payTypeList.find((t) => t.id == value.payType)
      value.isVipMemmber = find && find.name.includes('会员')
      value.isDanwei = find && find.name.includes('单位')
    },
    handleSearch(value) {
      let result
      if (!value) {
        result = this.oldcustomerList
      } else {
        result = this.oldcustomerList.filter((t) => t.name.includes(value))
      }
      this.customerList = result
    },
    handleSelectMember(e) {
      var find = this.customerList.find((t) => t.id === e)
      this.model.phone = find.phone
      this.model.customerName = find.name
      this.model.customerId = find.id
    },
    getMemeberCouponList() {
      getAction('/business/busMarketCouponsCashUsed/memeberCouponList', {
        pageNo: 1,
        pageSize: 99,
        conditions: 900,
        mobile: this.model.vipCustomerId
      }).then((res) => {
        if (res.success) {
          this.memeberCouponList = res.result.records
        }
      })
    },
    add(livingOrderId, roomId) {
      this.modelDefault.livingOrderId = livingOrderId
      this.modelDefault.roomId = roomId
      this.edit(this.modelDefault)
    },
    edit(record) {
      // console.log(record);
      this.model = Object.assign({}, record)
      // console.log(this.model);
      this.isRefund = this.model.isRefund
      // if (record.preferentialMoney !== 0 && record.preferentialMoney !== null) {
      //   console.log(record.preferentialMoney)
      //   debugger
      //   this.model.coupon = true
      //   this.model.preferentialType = 2
      //   this.model.couponFirstAmount = record.preferentialMoney
      // }
      this.getbusRoomPayType()
      this.refundRequest()
      if (this.model.vipCustomerId) {
        this.getMemeberCouponList()
      }
      if (this.model.vipCustomerId) {
        getAction('/business/busMemberCard/list', {
          id: this.model.vipCustomerId
        }).then((res) => {
          if (res.success) {
            if (res.result.records && res.result.records.length > 0) {
              this.memberCard = res.result.records[0]
            }
          }
        })
      }
      if (this.model.contractTeamId && this.model.contractTeamProtocolId) {
        getAction('/business/busMarketAgreementUnit/list', {
          id: this.model.contractTeamId
        }).then((res) => {
          if (res.success) {
            var list = res.result.records
            if (list.length > 0) {
              this.agreementUnitData = list[0]
              getAction('/business/busMarketAgreementCustomer/list', {
                agreementId: this.model.contractTeamId
              }).then((res2) => {
                if (res2.success) {
                  var list2 = res2.result.records
                  if (list2 && list2.length > 0) {
                    this.danwei = list2.find(
                        (t) => t.id == this.model.contractTeamProtocolId
                    )
                  }
                }
              })
            }
          }
        })
      }
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.model.couponFirstAmount > this.model.consumption.toFixed(2)) {
            this.model.couponFirstAmount = 0
            this.$message.warning('优惠金额不能大于消费')
            return
          }
          if (this.realityAmount != this.sumAmount) {
            console.log(this.realityAmount)
            console.log(this.sumAmount)
            let mes = that.isRefund ? '应退金额和实收合计必须相等' : '应收金额和实收合计必须相等'
            that.$message.warning(mes)
            return
          }
          // return
          var obj = {}
          var fees = []
          if (this.model.coupon) {
            fees.push({
              feeType: this.model.feeType,
              preferentialType: this.model.preferentialType,
              money: this.couponAmount.toFixed(2) - this.model.consumption.toFixed(2),
              custorerOrderRemark: '优惠金额',
              isPreferential: true
            })
          }
          console.log(fees, 'fees')
          if (this.model.couponCard && this.model.couponId) {
            var find = this.memeberCouponList.find(
                (t) => t.id === this.model.couponId
            )
            if (find) {
              fees.push({
                feeType: this.model.feeType,
                money: -find.cost.toFixed(2),
                custorerOrderRemark: '优惠券',
                isPreferential: true,
                couponscashId: find.id
              })
            }
          }
          // this.payList.forEach((item) => {
          //   let remark = '结账收款'
          //   let money = item.money.toFixed(2)
          //   if (this.isRefund) {
          //     remark = '结账退款'
          //     money = -money
          //   }
          //   fees.push({
          //     feeType: this.model.feeType,
          //     money,
          //     payType: item.payType,
          //     custorerOrderRemark: remark,
          //     isPreferential: this.model.isPreferential,
          //     preferentialMoney: this.model.preferentialMoney
          //   })
          // })

          let tempbreak = false
          this.combineFeeList.forEach(index => {
            if (index.payType == null && this.isRefund) {
              this.$message.warning('请选择自定义退款退款方式')
              tempbreak = true
            }
          })
          if (tempbreak) {
            return
          }
          // 退款
          if (this.isRefund) {
            let originalFeeList = JSON.parse(JSON.stringify(this.originalFeeList))
            let isCustomFees = []
            this.combineFeeList.forEach(e => {
              if (e.isCustom) {
                isCustomFees.push(e)
                return
              }
              // 将组合退款上的退款金额，按支付方式摊到各个收款单
              let prerefund = e.prerefund
              originalFeeList.forEach(ele => {
                if (ele.payType !== e.payType) {
                  return
                }
                if (prerefund > ele.money) {
                  ele.prerefund = ele.money
                  prerefund -= ele.money
                } else {
                  ele.prerefund = prerefund
                  prerefund = 0
                }
              })
            })
            // 添加自定义收款
            originalFeeList.push(...isCustomFees)
            originalFeeList.forEach((item) => {
              fees.push({
                feeType: this.model.feeType,
                money: -item.prerefund,
                payType: item.payType,
                custorerOrderRemark: '结账退款',
                isPreferential: this.model.isPreferential,
                preferentialMoney: this.model.preferentialMoney,
                returnFeeId: item.id,
                returnItem: true
              })
            })
          } else {
            this.payList.forEach((item) => {
            let remark = '结账收款'
            let money = item.money.toFixed(2)
            fees.push({
              feeType: this.model.feeType,
              money,
              payType: item.payType,
              custorerOrderRemark: remark,
              isPreferential: this.model.isPreferential,
              preferentialMoney: this.model.preferentialMoney
            })
          })
          }
          fees = fees.filter(e => !isNaN(e.money) && e.money != null && e.money !== 0)
          that.confirmLoading = true
          var url =
              '/business/busRoomBookingOrders/settle-checkout?bookingOrderId=' +
              this.model.bookingOrderId
          if (this.model.livingOrderId) {
            url =
                '/business/busRoomBookingOrders/living-settle-checkout?livingOrderId=' +
                this.model.livingOrderId
          }
          if (
              this.model.selectedFeeIds &&
              this.model.selectedFeeIds.length > 0
          ) {
            url = '/business/busRoomBookingOrders/partial-settle'
            // 部分
            obj = {
              fees: fees,
              livingOrderId: this.model.livingOrderId,
              selectedFeeIds: this.model.selectedFeeIds
            }
          }
          httpAction(
              url,
              this.model.selectedFeeIds && this.model.selectedFeeIds.length > 0
                  ? obj
                  : fees,
              'post'
          )
              .then((res) => {
                if (res.success) {
                  that.$message.success('结账成功')
                  that.$emit('ok')
                } else {
                  that.$message.warning(res.message)
                }
              })
              .finally(() => {
                that.confirmLoading = false
              })
        }
      })
    },
    // 退款列表请求数据
    refundRequest() {
      let returnFeeList = []
      // let feeList = []
      console.log(this.model)
      let livingOrderIds = this.model.livingOrderIds.split(',')
      if (this.model.money < 0) {
        postAction('/business/busOrderFee/refundList', livingOrderIds).then((res) => {
          // 过滤掉退款单
          this.originalFeeList = res.result.filter(e => !e.returnItem)
          this.originalFeeList.forEach(e => {
            let returnMoney = res.result.filter(ele => e.id === ele.returnFeeId && ele.returnItem).reduce((accumulator, ele) => accumulator + ele.money, 0)
            e.money = e.money + returnMoney
          })
          console.log(res.result.filter(e => e.returnItem))
          console.log(this.originalFeeList, 'originalFeeList')
          let originalFeeListCopy = JSON.parse(JSON.stringify(this.originalFeeList))
          originalFeeListCopy.forEach(e => {
            if (this.combineFeeList.length === 0) {
              this.combineFeeList.push(e)
              return
            }
            let find = this.combineFeeList.find(ele => ele.payType != null && e.payType === ele.payType)
            console.log(find)
            debugger
            if (find != null) {
              find.money += e.money
            } else {
              this.combineFeeList.push(e)
            }
          })
          console.log(this.combineFeeList, '9999999999999')

          // templist.forEach(e => {
          //   if (e.returnItem == true) {
          //     e.money = -e.money;
          //     returnFeeList.push(e)
          //   } else {
          //     // 如果是收款
          //     // e.returnMoney = 0
          //     // e.prerefund = 0
          //     // var room = this.model.filter(room => room.roomId === e.roomId)
          //     // e.roomName = room[0].roomName
          //     // this.$set(e, 'refundType', e.payType)
          //     // let payTypeList = JSON.parse(JSON.stringify(this.payTypeList));
          //     // console.log(room[0].livingOrder.vipCustomerId)
          //     // console.log(room[0].livingOrder.contractTeamId)
          //     // console.log(vipIndex)
          //     // console.log(danweiIndex)
          //     // this.$set(payTypeList[vipIndex], 'disable', room[0].livingOrder.vipCustomerId == null || room[0].livingOrder.vipCustomerId === '')
          //     // this.$set(payTypeList[danweiIndex], 'disable', room[0].livingOrder.contractTeamId == null)
          //     // this.$set(e, 'payTypeList', payTypeList)
          //     // feeList.push(e)
          //   }
          // })
          // this.feeList = feeList
          // this.allFeeMoney = 0
          // this.allReturnFee = 0
          // this.feeList.forEach(e => {
          //   // 当前收款对象下所有的退款记录
          //   let returnMoney = returnFeeList.filter(ele => e.id === ele.returnFeeId).reduce((accumulator, ele) => accumulator + ele.money, 0)
          //   e.returnMoney = returnMoney;
          //   this.allFeeMoney += e.money;
          //   this.allReturnFee += returnMoney;
          //   // console.log(returnMoney);
          // })
          // console.log(this.feeList);
          // console.log(this.templist);
          // this.feeList.forEach(index => {
          //   templist.forEach(item => {
          //     if (index.id == item.returnFeeId) {
          //       let tempindex = this.feeList.findIndex(item)
          //       this.feeList.slice(tempindex,1)
          //     }
          //   })
          // })

          // this.feeList.forEach(item => {

          // })
        })
      }
    },
    // 新增自定义退款
    addCustomRefund() {
      this.combineFeeList.push({
        createTime: '自定义退款',
        payType: null,
        prerefund: 0,
        // 是自定义的
        isCustom: true
      })
    },
    // 减少自定义退款
    reduceCustomization(val) {
      console.log(val)
      let tempindex = this.combineFeeList.findIndex(index => index == val)
      this.combineFeeList.splice(tempindex, 1)
    }
  }
}
</script>
<style scoped>
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  /* top: 4px; */
  margin-left: 5px;
  font-size: 18px;
  color: #1890ff;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
.refund_style {
  font-size: 32px;
  color: red;
  text-align: center;
  padding-top: 3%;
  /* margin: 0 auto; */
  /* display: inline-block; */
  /* margin-top: 60px; */

}
.crad_style {
  display: flex;
  background-color: #fcfcfc;
  width: 100%;
  border-radius: 5%;
}
.ant-col-16 {
  border-left: 2px solid #fc9424;
}
.ant-form-item {
  margin-bottom: 16px;
}
.presentation_style {
  /* font-size: ; */
  color: #fc9424;
  padding-left: 25px;
  padding-bottom: 5px;
  border-bottom: 1px solid #fc9424;
  padding-top: 10px;
}
.presentation_style_two {
  /* padding-left: 25px; */
  /* padding-bottom: 5px; */
  border-top: 1px solid #fc9424;
  margin-top: 10px;
  padding: 20px 0px 5px 25px;
}
.receipt_style {
  margin-top: 30px;
}
.settle_style {
  color: red !important;
}
</style>
