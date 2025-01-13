<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row style="display:flex;justify-content:center;">
        </a-row>

        <a-row v-if="model.subjectType===6">
            <a-col :span="10">
              <a-row style="height: 30px">
                <a-form-model ref="form" :model="model" :rules="validatorRules">
                  <a-col :span="9">
                    <a-form-model-item>
                      <a-input v-model="queryParams.name" style="width: 80%" @pressEnter="loadData" placeholder="名称/条码/简拼" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="3">
                    <a-form-model-item>
                      <a-button @click="loadData" type="primary">搜索</a-button>
                    </a-form-model-item>
                  </a-col>
                </a-form-model>
              </a-row>
              <a-row>
                <a-col :span="8">
                  <a-tree
                      showIcon
                      :replaceFields="{ children: 'children', title: 'name', key: 'id' }"
                      :treeData="barTree"
                  >
                    <div slot="title" slot-scope="item"
                         style="display: flex;align-items: center;height: 100%;overflow: hidden;">
                      <a-button style="font-size: 12px; padding-left: 0px" type="link" @click="chooseBar(item)">{{ item.name }}</a-button>
                    </div>
                  </a-tree>
                </a-col>
                <a-col :span="16">
                  <a-table :columns="columns" :customRow="handleRowClick" :dataSource="dataSource" :pagination="false" :scroll="{ y: 500 }" style="margin-top:10px; overflow-y: scroll">
<!--                    <template slot="sellingPrice" slot-scope="text, record">-->
<!--                      <a-input-number v-model="record.sellingPrice" :min="0"></a-input-number>-->
<!--                    </template>-->
<!--                    <template slot="number" slot-scope="text, record">-->
<!--                      <a-input-number :max="record.inventory" v-model="record.number" @change="changeNumber" :min="0"></a-input-number>-->
<!--                    </template>-->
                    <template slot="inventorySlot" slot-scope="text,record">
                      {{ record.inventory - record.number }}
                    </template>
                  </a-table>
                </a-col>
              </a-row>
            </a-col>
            <a-col :span="14">
              <a-row>
                <a-table :columns="chooseColumns" :dataSource="chooseGoodsList" :pagination="false" :scroll="{ y: 500 }" style="margin-top:10px; overflow-y: scroll">
                  <template #footer>
                    <span style="font-size: 15px;margin-left: 350px; margin-right: 10px;">{{ '合计：' + moneyAmount.toFixed(2) + ' 元' }}</span>&nbsp; &nbsp;
                  </template>
                  <template slot="sellingPrice" slot-scope="text, record">
                    <a-input-number v-model="record.sellingPrice" :min="0"></a-input-number>
                  </template>
                  <template slot="number" slot-scope="text, record">
                    <a-input-number :max="record.inventory" v-model="record.number" @change="changeNumber" :min="0" :disabled="record.id == null"></a-input-number>
                  </template>
                  <template slot="inventorySlot" slot-scope="text,record">
                    {{ record.inventory - record.number }}
                  </template>
                  <span slot="action" slot-scope="text, record" >
                    <a @click="deleteFee(record)">删除</a>
                </span>
                </a-table>
                <a-col :span="24" style="text-align: end">
                  <a-checkbox default-checked>打印消费账单</a-checkbox>
                </a-col>
              </a-row>
            </a-col>
          </a-row>

        <a-row v-if="model.subjectType===11">
<!--          水表:上期读数:(自动带入上期读数，初始须提前设置，可修改):(可填)本期用量:(=本期-上期) 单价:(提前设置好的) 本期应缴:(根据用量和单价自动计算)-->
          <a-col :span="2">
            <a-form-model-item>
              <span style="font-size: large; font-weight: bold;"> 水表:</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="上期读数">
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item
                label="本期读数"
                :labelCol="{ xs: { span: 24 }, sm: { span: 10 }}"
                :wrapperCol="{xs: { span: 24 },sm: { span: 12 }}">
              <a-input-number style="width: 100%"></a-input-number>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="本期用量">
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="单价">
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="本期应缴">
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-if="model.subjectType===11">
          <a-col :span="2">
            <a-form-model-item>
              <span style="font-size: large; font-weight: bold;"> 电表:</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="上期读数">
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item
                label="本期读数"
                :labelCol="{ xs: { span: 24 }, sm: { span: 10 }}"
                :wrapperCol="{xs: { span: 24 },sm: { span: 12 }}">
              <a-input-number style="width: 100%"></a-input-number>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="本期用量">
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="单价">
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="本期应缴">
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-if="model.subjectType===11">
          <a-col :span="2">
            <a-form-model-item>
              <span style="font-size: large; font-weight: bold;">燃气表:</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="上期读数">
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item
                label="本期读数"
                :labelCol="{ xs: { span: 24 }, sm: { span: 10 }}"
                :wrapperCol="{xs: { span: 24 },sm: { span: 12 }}">
              <a-input-number style="width: 100%"></a-input-number>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="本期用量">
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="单价">
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="本期应缴">
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>

    </j-form-container>
    <j-modal
       :width="300"
       :visible="addFeeVisible"
       @ok="addFee"
       @cancel="addFeeVisible = !addFeeVisible"
    >
      <a-form-model>
        <a-col>
          <a-form-model-item
              :labelCol="{ xs: { span: 24 }, sm: { span: 8 }}"
              :wrapperCol="{xs: { span: 24 },sm: { span: 14 }}"
              label="费项"
              style="padding-top: 10px; padding-bottom: 0px">
            <a-input v-model="addFeeModel.remark" :disabled="this.barId != 14"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col>
          <a-form-model-item :labelCol="{ xs: { span: 24 }, sm: { span: 8 }}" :wrapperCol="{xs: { span: 24 },sm: { span: 14 }}" label="金额">
            <a-input-number v-model="addFeeModel.feeMoney" style="width: 100%"></a-input-number>
          </a-form-model-item>
        </a-col>
      </a-form-model>
    </j-modal>
  </a-spin>
</template>

<script>
import {
    httpAction,
    getAction
} from '@/api/manage'
import FeeGoodsForm from '@views/room/modules/checkIn/FeeGoodsForm'
export default {
    name: 'BusMemberCardForm',
    components: { FeeGoodsForm },
    props: {
        // 表单禁用
        disabled: {
            type: Boolean,
            default: false,
            required: false
        },
    },
    data() {
        return {
          addFeeVisible: false,
          addFeeModel: {
            subjectType: null,
            feeMoney: null,
            remark: null,
          },
          barTree: [
            {
              id: 6,
              name: '商品'
            },
            {
              id: 10,
              name: '手工房费'
            },
            {
              id: 12,
              name: '赔偿费'
            },
            {
              id: 14,
              name: '其他'
            }
          ],
          barId: 6,
          dataSource: [],
          chooseGoodsList: [],
          columns: [
            {
              title: '名称',
              align: 'center',
              dataIndex: 'name'
            },
            {
              title: '售价',
              align: 'center',
              dataIndex: 'sellingPrice',
            },
            {
              title: '库存',
              align: 'center',
              dataIndex: 'inventory',
              width: '80',
              scopedSlots: {
                customRender: 'inventorySlot'
              }
            }
          ],
          chooseColumns: [
            {
              title: '名称',
              align: 'center',
              dataIndex: 'name'
            },
            {
              title: '售价',
              align: 'center',
              dataIndex: 'sellingPrice',
              scopedSlots: {
                customRender: 'sellingPrice'
              }
            },
            {
              title: '数量',
              align: 'center',
              dataIndex: 'number',
              scopedSlots: {
                customRender: 'number'
              }
            },
            {
              title: '金额',
              align: 'center',
              customRender: function (text, record) {
                return record.sellingPrice * record.number
              }
            },
            {
              title: '操作',
              dataIndex: 'action',
              key: 'action',
              align: 'center',
              scopedSlots: { customRender: 'action' }
            }
          ],
          model: {
                payType: 1,
                livingOrderId: '',
                certType: 1,
                gender: 1,
                subjectType: 6,
                vipCustomerId: null
            },
          markerMemberCard: {},
          queryParams: {},
          labelCol: {
              xs: {
                  span: 24
              },
              sm: {
                  span: 5
              }
          },
          wrapperCol: {
              xs: {
                  span: 24
              },
              sm: {
                  span: 16
              }
          },
          confirmLoading: false,
          validatorRules: {
              subjectType: [{
                  required: true,
                  message: '请输入消费项目!'
              }],
              money: [{
                  required: true,
                  message: '请输入金额!'
              }]
          },
          url: {
              list: '/rooms/cesGoods/kf-goods-list',
              edit: '/business/busMemberCard/edit',
              queryById: '/business/busMemberCard/queryById'
          },
          gradeList: [],
          paymentMethodList: [],
          staffList: [],
          customerList: [],
          oldcustomerList: []
        }
    },
    computed: {
      formDisabled() {
          return this.disabled
      },
      moneyAmount() {
            let money = this.chooseGoodsList.reduce((pre, cur) => pre + cur.sellingPrice * (cur.number || 0), 0)
            return money == null ? 0 : money
      }
    },
    created() {
        var _info = JSON.parse(localStorage.getItem('storeInfo'))
        if (_info) {
            this.model.hotelId = _info.id
        }
        // 备份model原始值
        this.modelDefault = JSON.parse(JSON.stringify(this.model))
        this.getbusCustomer()
        this.loadStockType()

    },
    methods: {
      chooseBar(item) {
        this.barId = item.id
        if (item.id === 6) {
          this.queryParams.goodTypeId = null
          this.loadData()
          return
        } else if (item.id === 10 || item.id === 12 || item.id === 14) {
          this.addFeeModel.remark = this.getSubjectTypeText(item.id)
          this.addFeeVisible = true
          return
        }
        this.queryParams.goodTypeId = item.id
        this.loadData()
      },
      addFee() {
        let obj = {
          sellingPrice: this.addFeeModel.feeMoney,
          number: 1,
          remark: this.addFeeModel.remark,
          name: this.addFeeModel.remark,
          subjectType: this.barId
        }
        this.chooseGoodsList.push(obj)
        this.addFeeVisible = false
      },
      deleteFee(record) {
        let index = this.chooseGoodsList.findIndex(e => e === record)
        if (index !== -1) {
          this.chooseGoodsList.splice(index, 1)
          if (record.id != null) {
            record.inventory = record.inventory + record.number
          }
        }
      },
      getSubjectTypeText(text, record) {
        var msg = ''
        if (text == 1) {
          msg = '押金'
          //
          if (record.remark != null && record.remark != '') {
            msg = record.remark
          }
        } else if (text == 2) {
          msg = '预收房费'
        } else if (text == 3) {
          msg = '每日房费'
        } else if (text == 4) {
          msg = '优惠金额'
        } else if (text == 5) {
          msg = '结账收款'
        } else if (text == 6) {
          msg = '商品-' + record.feeGoodVo.name
        } else if (text == 7) {
          msg = '点餐-' + record.feeGoodVo.name
        } else if (text == 8) {
          msg = '夜审房费'
        } else if (text == 9) {
          msg = '会议室'
        } else if (text == 10) {
          msg = '手工房费'
        } else if (text == 11) {
          msg = '水电煤抄表'
        } else if (text == 12) {
          msg = '赔偿费'
        } else if (text == 13 && record != null) {
          msg = record.remark
        } else if (text == 14) {
          msg = '其他'
        }
        return msg
      },

      handleRowClick(row) {
        return {
          on: {
            dblclick: () => {
              if (row.inventory - row.number <= 0) {
                this.$message.warning('库存不足')
                return
              }
              row.number++
              if (!this.chooseGoodsList.some(e => e === row)) {
                this.chooseGoodsList.push(row)
              }
            }
          }
        }
      },

      changeNumber() {
            console.log(this.dataSource)
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
        getbusCustomer() {
            getAction('/bus/busCustomer/list', {}).then((res) => {
                if (res.success) {
                    this.customerList = res.result.records
                    this.oldcustomerList = JSON.parse(JSON.stringify(this.customerList))
                }
            })
        },
        getMarkerMemberByCarId() {
          console.log(this.model)
          if (this.model.vipCustomerId == null || this.model.vipCustomerId === '') {
            this.markerMemberCard = null;
            return
          }
          if (this.markerMemberCard != null && this.markerMemberCard.goodsDiscount != null) {
            this.dataSource.forEach(e => {
              e.sellingPrice = e.sellingPrice * this.markerMemberCard.goodsDiscount / 100
            })
            return
          }
          console.log(this.model.vipCustomerId)
          getAction('/business/busMemberCard/getMarkerMemberByCarId', { id: this.model.vipCustomerId }).then((res) => {
            if (res.success) {
              this.markerMemberCard = res.result
              this.dataSource.forEach(e => {
                e.sellingPrice = e.sellingPrice * this.markerMemberCard.goodsDiscount / 100
              })
            }
          })
        },
        /**查询商品类别  */
        loadStockType() {
          let params = {
            pageSize: 9999,
            pageNo: 1
          }
          getAction('/rooms/cesStockType/tree', params).then((res) => {
            if (!res.success) {
              this.$message.warning('查询失败')
              return
            }
            this.stockTypeList = res.result
            this.$set(this.barTree[0], 'children', this.stockTypeList)
            this.$forceUpdate()
          })
        },
        stockTypeChange(e) {
          this.queryParams.goodTypeId = e[e.length - 1]
        },
        add(obj) {
            this.modelDefault.livingOrderId = obj.livingOrderId
            this.modelDefault.roomId = obj.roomId
            this.modelDefault.vipCustomerId = obj.vipCustomerId
            this.edit(this.modelDefault)
        },
        edit(record) {
            this.model = Object.assign({}, record)
            this.visible = true
          debugger
            this.loadData()
        },
        submitForm() {
            const that = this
            // 触发表单验证
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.chooseGoodsList.every(ele => ele.number == 0) && this.model.subjectType == 6) {
                        this.$message.warning('请至少选择一件商品')
                        return
                    }
                    that.confirmLoading = true
                    var orders = []
                    let feeGood = {}

                    that.chooseGoodsList.forEach(ele => {
                      if (ele.id != null) {
                          feeGood = {
                              goodsId: ele.id,
                              num: ele.number,
                              price: ele.sellingPrice
                          }
                          orders.push({
                            money: ele.sellingPrice * (ele.number || 0),
                            subjectType: 6,
                            feeGoodVo: feeGood
                          })
                      } else {
                        orders.push({
                          money: ele.sellingPrice,
                          subjectType: ele.subjectType,
                          remark: ele.remark
                        })
                      }
                    })
                  console.log(orders)
                    this.model.orders = orders
                    httpAction('/business/busRoomBookingOrders/set-living-order-fee?livingOrderId=' + this.model.livingOrderId, orders, 'post')
                        .then((res) => {
                            if (res.success) {
                                that.$message.success(res.message)
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
      loadData(arg) {
        if (this.url.list == 2) {
          return
        }
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }
        let params = {
          pageSize: 9999,
          pageNo: 1,
          name: this.queryParams.name,
          goodType: this.queryParams.goodTypeId
        }
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            // update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            let arr = []
            arr = res.result.records || res.result
            arr.forEach(ele => {
              ele.number = 0
            })
            this.dataSource = JSON.parse(JSON.stringify(arr))
            this.getMarkerMemberByCarId()
            this.loading = false
            // update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loading = false
        })
      },

    }
}
</script>
