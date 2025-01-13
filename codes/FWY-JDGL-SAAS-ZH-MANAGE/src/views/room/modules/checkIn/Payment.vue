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
          <a-col :span="24">
            <a-form-model-item
              label="应收金额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="billAmount"
            >
              {{ model.billAmount.toFixed(2) }}元
              <!--部分结账这里 优惠功能暂时不要了-->
              <!-- <a-switch v-model="model.coupon"  />优惠-->
            </a-form-model-item>
          </a-col>
          <template v-if="model.coupon">
            <a-col :span="24">
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
            </a-col>
            <a-col :span="24" v-if="model.preferentialType == 2">
              <a-form-model-item
                label="优惠金额"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="couponFirstAmount"
              >
                <a-input-number
                  style="width: 100px"
                  :min="0"
                  v-model="model.couponFirstAmount"
                  placeholder="请输入优惠金额"
                ></a-input-number
                >元
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item
                label="惠后金额"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                {{ couponAmount.toFixed(2) }}元
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item
                label="优惠券"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="refund"
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
            </a-col>
          </template>
          <a-col :span="24">
            <a-form-model-item
              label="实收金额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              {{ realityAmount.toFixed(2) }}元
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="收款金额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <div v-for="(item, index) in payList" :key="index">
                <a-row type="flex" :key="index">
                  <a-col :span="6">
                    <a-input-number
                      style="width: 100px"
                      v-model="item.money"
                      :min="0"
                    ></a-input-number>
                  </a-col>
                  <a-col :span="6">
                    <a-select
                      v-model="item.payType"
                      placeholder="收款方式"
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
                      @click="plus()" />
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
          <a-col :span="24">
            <a-form-model-item
              label="实收合计"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              {{ sumAmount.toFixed(2) }}元
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
import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'Payment',
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
      default: true
    }
  },
  data() {
    return {
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
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
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
      copyRealityAmount: ''
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
    sumAmount() {
      var sum = this.payList.reduce(function (total, item) {
        return total + item.money
      }, 0)
      return sum
    },
    couponAmount() {
      var sum = 0
      if (this.model.coupon) {
        if (this.model.preferentialType == 1) {
          sum = Math.floor(this.model.billAmount)
        } else if (this.model.preferentialType == 2) {
          sum = this.model.billAmount - (this.model.couponFirstAmount || 0)
        } else if (this.model.preferentialType == 3) {
          sum = parseFloat(
            ((this.model.billAmount * this.model.discount) / 10).toFixed(2)
          )
        }
      } else {
        sum = this.model.billAmount
      }
      return sum
    },
    realityAmount() {
      let result = this.couponAmount
      if (this.model.couponCard) {
        var find = this.memeberCouponList.find(
          (t) => t.id == this.model.couponId
        )
        if (find) {
          result = this.couponAmount - find.cost
        }
      }
      if (this.model.coupon) {
        var find = this.memeberCouponList.find(
            (t) => t.id == this.model.couponId
        )
        if (find) {
          result = this.couponAmount - find.cost
        }
        if (this.payList && this.payList.length > 0 && this.copyRealityAmount != result) {
          this.payList = [this.payList[0]]
          this.payList[0].money = result
        }
        this.copyRealityAmount = result
      }
      this.$forceUpdate()
      return result
    }
  },
  created() {
    var _info = JSON.parse(localStorage.getItem('storeInfo'))
    if (_info) {
      this.model.hotelId = _info.id
    }
    // 备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
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
            this.$set(this.model, 'payType', this.payTypeList[0].id)

            this.payList = [
              {
                money: this.realityAmount,
                payType: this.payTypeList[0].id,
                isVipMemmber: false,
                isDanwei: false
              }
            ]
          }
        }
      })
    },
    plus() {
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
      let data = Object.assign({}, record)
      this.model = JSON.parse(JSON.stringify(data))
      if (record.preferentialMoney !== 0 && record.preferentialMoney !== null) {
        this.model.preferentialType = 2
        this.model.couponFirstAmount = record.preferentialMoney
      }
      this.getbusRoomPayType()
      if (record.bookingYj != null) {
        return
      }
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
      // this.visible = true;
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.model.couponFirstAmount > this.model.consumption) {
            this.model.couponFirstAmount = 0
            this.$message.warning('优惠金额不能大于消费')
            return
          }
          if (this.realityAmount !== this.sumAmount) {
            that.$message.warning('实收金额和实收合计必须相等')
            return
          }
          var obj = {}
          var fees = []
          console.log(this.model)
          console.log(this.model.subjectType)

          if (this.model.coupon) {
            fees.push({
              feeType: this.model.feeType,
              preferentialType: this.model.preferentialType,
              money: this.couponAmount.toFixed(2) - this.model.billAmount.toFixed(2),
              custorerOrderRemark: '优惠金额',
              isPreferential: true
            })
          }
          if (this.model.couponCard && this.model.couponId) {
            var find = this.memeberCouponList.find(
              (t) => t.id == this.model.couponId
            )
            if (find) {
              fees.push({
                feeType: this.model.feeType,
                money: find.cost.toFixed(2),
                custorerOrderRemark: '优惠券',
                isPreferential: true
              })
            }
          }
          let remark = this.model.bookingYj ? '预约押金' : '结账退房'
          this.payList.forEach((item) => {
            fees.push({
              feeType: this.model.feeType,
              bookingYj: this.model.bookingYj,
              subjectType: this.model.subjectType,
              money: item.money.toFixed(2),
              payType: item.payType,
              remark: remark,
              isPreferential: this.model.isPreferential,
              preferentialMoney: this.model.preferentialMoney
            })
          })

          that.confirmLoading = true
          var url = '/business/busRoomBookingOrders/settle-checkout?bookingOrderId=' + this.model.bookingOrderId
          if (this.model.livingOrderId) {
            url = '/business/busRoomBookingOrders/living-settle-checkout?livingOrderId=' + this.model.livingOrderId
          }
          if (this.model.selectedFeeIds && this.model.selectedFeeIds.length > 0) {
            url = '/business/busRoomBookingOrders/partial-settle'
            obj = {
              fees: fees,
              livingOrderId: this.model.livingOrderId,
              selectedFeeIds: this.model.selectedFeeIds
            }
          }
          if (this.model.bookingYj) {
            url = '/business/busOrderFee/saveBookingDeposit'
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
                that.$emit('ok', res.result)
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
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
</style>
