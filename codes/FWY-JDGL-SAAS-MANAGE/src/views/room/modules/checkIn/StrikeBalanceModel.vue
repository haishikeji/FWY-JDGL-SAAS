<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :confirm-loading="confirmLoading"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-radio-group v-model="tebdata" style="margin-bottom: 16px">
      <a-radio-button value="small">金额冲账</a-radio-button>
      <a-radio-button value="default">商品冲账</a-radio-button>
    </a-radio-group>
    <a-table
    v-if="tebdata == 'small'"
      :columns="columns"
      :data-source="feesList"
      :pagination="false"
      :scroll="{ y: 400 }"
      style="height: 430px"
      rowKey="id">
      <template slot="subjectType" slot-scope="text, record">
        {{ getSubjectTypeText(text, record) }}
      </template>
      <template slot="money" slot-scope="text, record">
        <a-input-number v-model='record.money' @blur="handleBlur()" :max="record.oldPrice" :min="0"></a-input-number>
      </template>
      <template slot="custorerOrderRemark" slot-scope="text, record">
        <a-textarea v-model=record.custorerOrderRemark></a-textarea>
      </template>
    </a-table>
    <a-table
    v-if="tebdata == 'default'"
      :columns="columnstwo"
      :data-source="productList"
      :pagination="false"
      :scroll="{ y: 400 }"
      style="height: 430px"
      rowKey="id">
      <template slot="subjectType" slot-scope="text, record">
        {{ getSubjectTypeText(text, record) }}
      </template>
      <template slot="money" slot-scope="text, record">
        <!-- <a-input-number v-model=record.money @blur="handleBlur()" :min="0"></a-input-number> -->
        {{ record.money }}
      </template>
      <template slot="returnNum" slot-scope="text, record">
        <a-input-number v-model="record.returnNum" :min="0" :max="record.feeGoodVo.num" @change="changeValue(record)"></a-input-number>
      </template>
      <template slot="money" slot-scope="text, record">
        <a-input-number v-model=record.money @blur="handleBlur()" :max="record.oldPrice" :min="0"></a-input-number>
      </template>
      <template slot="custorerOrderRemark" slot-scope="text, record">
        <a-textarea v-model=record.custorerOrderRemark></a-textarea>
      </template>
    </a-table>
  </a-modal>
</template>

<script>
import { postAction } from '@api/manage'

const columns = [
  {
    title: '房间',
    dataIndex: 'roomName',
    width: 30,
    align: 'center'
  },
  {
    title: '费项',
    dataIndex: 'subjectType',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'subjectType' }
  },
  {
    title: '日期',
    dataIndex: 'dayTime',
    width: 100,
    align: 'center'
  },
  {
    title: '金额',
    dataIndex: 'oldPrice',
    width: 40,
    align: 'center'
  },
  {
    title: '冲账金额',
    dataIndex: 'money',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'money'}
  },
  {
    title: '备注',
    dataIndex: 'custorerOrderRemark',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'custorerOrderRemark'}
  },
  ]
const columnstwo = [
  {
    title: '房间',
    dataIndex: 'roomName',
    width: 30,
    align: 'center'
  },
  {
    title: '费项',
    dataIndex: 'subjectType',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'subjectType' }
  },
  {
    title: '日期',
    dataIndex: 'dayTime',
    width: 100,
    align: 'center'
  },
  {
    title: '金额',
    dataIndex: 'oldPrice',
    width: 60,
    align: 'center'
  },
  {
    title: '数量',
    // dataIndex: 'oldPrice',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'subjectType' },
    customRender: function (text, record) {
        return record.feeGoodVo.num
    }
  },
  {
    title: '冲账数量',
    // dataIndex: 'returnNum',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'returnNum'}
    // customRender: function (text, record) {
    //     return record.num = record.feeGoodVo.num
    // }
  },
  {
    title: '冲账金额',
    dataIndex: 'money',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'money'}
  },
  {
    title: '备注',
    dataIndex: 'custorerOrderRemark',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'custorerOrderRemark'}
  },
  ]
export default {
  name: 'StrikeBalanceModel',
  components: {
  },
  data() {
    return {
      title: '',
      width: 1000,
      visible: false,
      confirmLoading: false,
      disableSubmit: false,
      showYinshou: true,
      columns: columns,
      columnstwo: columnstwo,
      feesList: [],
      tebdata: 'small',
      // 商品
      productList:[]
    }
  },
  methods: {
    // 冲账数量变化
    changeValue(val) {
      console.log(val);
      let tempvalue = val.oldPrice / val.feeGoodVo.num * val.returnNum
      return val.money = tempvalue
    },
    edit(record, map) {
      this.visible = true
      console.log(record)
      this.productList = []
      this.feesList = []
      record.forEach(index => {
        if (index.feeGoodVo == null) {
          this.feesList.push(index)
        } else if (index.feeGoodVo !== null) {
          this.productList.push(index)
        }
      })
      // this.feesList = record
      this.feesList.forEach(e => {
        e.roomName = map.get(e.roomId)
        e.oldPrice = e.money
        // e.remark = '冲账' + this.getSubjectTypeText(e.subjectType)
        e.money = 0
      })
      this.productList.forEach(e => {
        e.roomName = map.get(e.roomId)
        e.oldPrice = e.money
        e.money = 0
        let tempnum = JSON.parse(JSON.stringify(e.feeGoodVo))
        e.remark = '冲账' + tempnum.name
        if (e.returnNum == null) {
          e.returnNum = 0
        }
      })
      console.log(this.feesList);
      console.log(this.productList);
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      let fees = []
      if (this.productList.some(e => e.money > e.oldPrice || e.money < 0 || e.returnNum > e.feeGoodVo.num || e.returnNum < 0)) {
        this.$message.warning('冲账金额不能超过原金额,冲账数量不能超过原数量，且都不能小于0')
        return
      }
      if (this.feesList.some(e => e.money > e.oldPrice || e.money < 0)) {
        this.$message.warning('冲账金额不能超过原金额,且不能小于0')
        return
      }
      this.productList.forEach(e => {
        // 一条消费可能有多次冲账，所以传入后端的money是冲账的负数差值
        if (e.money > 0 || e.returnNum > 0) {
          let param = Object.assign({}, e)
          param.money = -param.money
          param.remark = '冲账' + this.getSubjectTypeText(e.subjectType,e)
          fees.push(param)
        }
      })
      this.feesList.forEach(e => {
        // 一条消费可能有多次冲账，所以传入后端的money是冲账的负数差值
        if (e.money > 0) {
          let param = Object.assign({}, e)
          param.money = -param.money
          param.remark = '冲账' + this.getSubjectTypeText(e.subjectType,e)
          fees.push(param)
        }
      })
      if (fees.length === 0) {
        this.$message.warning('请填写需要冲账的金额或数量')
        return
      }
      this.confirmLoading = true
      postAction('/business/busOrderFee/strike-balance', fees).then(resp => {
        if (resp.result) {
          this.$message.success('冲账成功')
          this.$emit('ok')
          this.visible = false
        } else {
          this.$message.warning('冲账失败')
        }
      }).finally(() => {
        this.confirmLoading = false
      });
    },
    handleCancel() {
      this.close()
    },
    getSubjectTypeText(text, record) {
      console.log(record, 'record')
      var msg = ''
      if (text === 1) {
        msg = '押金'
      } else if (text === 2) {
        msg = '预收房费'
      } else if (text === 3) {
        msg = '每日房费'
      } else if (text === 4) {
        msg = '优惠金额'
      } else if (text === 5) {
        msg = '结账收款'
      } else if (text === 6) {
        msg = '商品-' + record.feeGoodVo.name
      } else if (text === 7) {
        msg = '点餐'
      } else if (text === 8) {
        msg = '夜审房费'
      } else if (text === 9) {
        msg = '会议室'
      } else if (text === 10) {
        msg = '手工房费'
      } else if (text === 11 && record) {
        msg = record.remark
      } else if (text === 12) {
        msg = '赔偿费'
      } else if (text === 13) {
        msg = '退单结账'
      } else if (text === 14 && record) {
        console.log(record)
        msg = record.remark
      }
      return msg
    },
    handleBlur() {
      this.$forceUpdate()
    }

  }
}
</script>
