<template>
  <j-modal
      style="padding-bottom: 0px"
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-form-model>
      <a-row>
        <a-col :span="24" style="display: flex" >
          <a-form-model-item
            label="收款账号"
            style="width:100%; flex: 1"
            :labelCol="{
              xs: { span: 24 },
              sm: { span: 13 },
            }"
            :wrapperCol="{
              xs: { span: 24 },
              sm: { span: 10 },
            }">
            <a-select v-model="roomId" @change="changeSelect">
              <a-select-option v-for="item in roomInfos" :value="item.roomId" >{{ item.roomName }}</a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item style="width:100%; flex: 2" >
            <a-button @click="addEnterAccount">添加收款</a-button>
          </a-form-model-item>

        </a-col>
      </a-row>
      <a-row v-for="(item, index) in roomInfos[errIndex].addAccountList" :key="index" style="margin-bottom:10px;">
        <a-col :span="24" style="display: flex">
          <a-form-model-item
              style="width: 100%; flex: 1; margin-bottom: 20px"
              label="收款方式"
              :labelCol="{
              xs: { span: 24 },
              sm: { span: 13 },
            }"
              :wrapperCol="{
              xs: { span: 24 },
              sm: { span: 10 },
            }">
            <a-select v-model="item.payType">
              <a-select-option v-for="type in payTypeList" :value="type.id" :disabled="type.delFlag == 99">{{ type.name }}</a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item
              style="width: 100%; flex: 2; margin-bottom: 20px"
              label="金额"
              :labelCol="{
              xs: { span: 24 },
              sm: { span: 3 },
            }"
              :wrapperCol="{
              xs: { span: 24 },
              sm: { span: 14 },
            }">
            <div style="display: flex; margin-top: 4px">
              <a-input-number v-model="item.money"></a-input-number>
              <div  style="width: 30px; height: 30px; display: flex; align-items: center; justify-content: center; margin-left: 20px">
                <span v-if="item.subjectType === 1" class="centered-span" style="">押</span>
              </div>
              <a-button @click="isDeposit(index)" style="margin-left: 20px">标记押金</a-button>
            </div>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24" style="display: flex">
          <a-form-model-item
              style="width: 100%"
              label="备注"
              :labelCol="{
              xs: { span: 24 },
              sm: { span: 4 },
            }"
              :wrapperCol="{
              xs: { span: 24 },
              sm: { span: 18 },
            }">
            <a-textarea v-model="roomInfos[errIndex].remark"></a-textarea>
          </a-form-model-item>
        </a-col>
      </a-row>

    </a-form-model>

  </j-modal>
</template>

<script>

import { getAction, postAction } from '@api/manage'

export default {

  data () {
    return {
      title: '',
      width: 800,
      visible: false,
      disableSubmit: false,
      roomId: 0,
      roomInfos: [{}],
      payTypeList: [],
      errIndex: 0, //上一条数据
    }
  },
  created() {
  },
  methods: {
    add (record) {
      let modelList = []
      console.log(record)
      record.forEach(e => {
        let model = {
          roomId: e.roomId,
          roomName: e.roomName,
          remark: '',
          contactId: e.livingCustomers[0].customerId,
          livingOrderId: e.livingOrder.id,
          feeType: 2,
          vipCardId: e.livingOrder.vipCustomerId,
          contractTeamProtocolId: e.livingOrder.contractTeamProtocolId,
          contractTeamId: e.livingOrder.contractTeamId,
          agreementUnitId: e.livingOrder.contractTeamId,
          addAccountList: [{
            payType: '',
            money: 0,
            subjectType: 2
          }]
        }
        modelList.push(model)
      })
      this.roomInfos = modelList
      this.roomId = this.roomInfos[0].roomId
      this.errIndex = 0
      this.visible = true
      this.getPayTypeList()
    },
    close () {
      this.$emit('close')
      this.visible = false
    },
    handleOk () {
      let feeInfoList = []
      this.roomInfos.forEach(e => {
        e.addAccountList.forEach(ele => {
          console.log(ele.money)
          if (ele.money === null || ele.money === 0) {
            return
          }
          let feeInfo = Object.assign(ele, e)
          delete feeInfo.addAccountList
          feeInfoList.push(feeInfo)
        })
      })
      if (feeInfoList.length === 0) {
        this.$message.warning('没有要添加的费用')
        return
      }
      postAction('/business/busOrderFee/saveBatch', feeInfoList)
          .then(resp => {
            if (resp.result) {
              this.$message.success('添加成功')
            } else {
              this.$message.warning('添加失败')
            }
          })
      this.$emit('ok')
      this.visible = false
    },
    handleCancel () {
      this.close()
    },
    addEnterAccount() {
      if (this.payTypeList.length === 0) {
        return
      }
      this.roomInfos[this.errIndex].addAccountList.push({
        payType: this.payTypeList[0].id,
        money: 0,
        subjectType: 2
      })
    },
    getPayTypeList() {
      getAction('/business/busRoomPayType/list', {
        pageSize: 99999,
        pageNo: 1
      }).then((res) => {
        if (res.success) {
          console.log(this.roomInfos, 'this.roomInfos')
          this.payTypeList = res.result.records
          if (this.payTypeList && this.payTypeList.length > 0) {
            this.roomInfos.forEach(e => e.addAccountList[0].payType = this.payTypeList[0].id)
            this.changePayTypeList(this.roomInfos[0])
          }
        }
      })
    },
    changeSelect(value) {
      console.log(value)
      this.errIndex = this.roomInfos.findIndex(item => item.roomId == value)
      if (this.payTypeList && this.payTypeList.length > 0) {
        let roomInfo = this.roomInfos.find(item => item.roomId == value)
        this.changePayTypeList(roomInfo)
      }
      this.$forceUpdate()
    },
    changePayTypeList(roomInfo) {
      if (!roomInfo.vipCardId || roomInfo.vipCardId.length === 0) {
        var index = this.payTypeList.findIndex((t) =>
            t.name === '会员卡'
        )
        if (index >= 0) {
          this.payTypeList[index].delFlag = 99
        }
      }
      console.log(roomInfo, 'roomInfo')
      if (!roomInfo.contractTeamId || roomInfo.contractTeamId.length === 0 ||
          !roomInfo.contractTeamProtocolId || roomInfo.contractTeamProtocolId.length === 0) {
        let index = this.payTypeList.findIndex((t) =>
            t.name === '单位挂账'
        )
        if (index >= 0) {
          this.payTypeList[index].delFlag = 99
        }
      }
    },

    isDeposit(index) {
      let type = this.roomInfos[this.errIndex].addAccountList[index].subjectType
      this.roomInfos[this.errIndex].addAccountList[index].subjectType = type === 2 ? 1 : 2
    }
  }
}
</script>
<style>
.centered-span {
  display: flex;
  justify-content: center;
  text-align:center;
  align-items: center;
  background-color: pink;
  width: 30px;
  height: 30px;
  border: solid 1px pink;
  border-radius: 80%;
}
</style>
