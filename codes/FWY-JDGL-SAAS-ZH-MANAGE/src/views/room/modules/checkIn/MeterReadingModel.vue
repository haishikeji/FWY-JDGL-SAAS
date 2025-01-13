<template>
  <j-modal
      :title="title"
      :width="width"
      :visible="visible"
      switchFullscreen
      @ok="handleOk"
      :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
      @cancel="handleCancel"
      cancelText="关闭">

    <!-- 水表:上期读数:(自动带入上期读数，初始须提前设置，可修改):(可填)本期用量:(=本期-上期) 单价:(提前设置好的) 本期应缴:(根据用量和单价自动计算)-->
    <a-row v-if="waterCharge.id != null">
      <a-col :span="2">
        <a-form-model-item>
          <span style="font-size: large; font-weight: bold;"> 水表:</span>
        </a-form-model-item>
      </a-col>
      <a-col :span="3">
        <a-form-model-item
            label="上期读数"
           :labelCol="labelCol"
           :wrapperCol="wrapperCol">
          {{ room.waterMeter }}
        </a-form-model-item>
      </a-col>
      <a-col :span="5">
        <a-form-model-item
            label="本期读数"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          <a-input-number :precision="2" v-model="waterCharge.meterRead" @change="dushuChange($event, room.waterMeter, waterCharge)" style="width: 100%"></a-input-number>
        </a-form-model-item>
      </a-col>
      <a-col :span="5">
        <a-form-model-item
            label="本期用量"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ waterCharge.useNum }}
        </a-form-model-item>
      </a-col>
      <a-col :span="4">
        <a-form-model-item
            label="单价"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ waterCharge.money + '元/吨' }}
        </a-form-model-item>
      </a-col>
      <a-col :span="4">
        <a-form-model-item
            label="本期应缴"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ waterCharge.feeMoney }}
        </a-form-model-item>
      </a-col>
    </a-row>
    <a-row v-if="energyCharge.id != null">
      <a-col :span="2">
        <a-form-model-item>
          <span style="font-size: large; font-weight: bold;"> 电表:</span>
        </a-form-model-item>
      </a-col>
      <a-col :span="3">
        <a-form-model-item
            label="上期读数"
           :labelCol="labelCol"
           :wrapperCol="wrapperCol">
          {{ room.energyMeter }}
        </a-form-model-item>
      </a-col>
      <a-col :span="5">
        <a-form-model-item
            label="本期读数"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          <a-input-number v-model="energyCharge.meterRead" @change="dushuChange($event, room.energyMeter, energyCharge)" style="width: 100%"></a-input-number>
        </a-form-model-item>
      </a-col>
      <a-col :span="5">
        <a-form-model-item
            label="本期用量"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ energyCharge.useNum }}
        </a-form-model-item>
      </a-col>
      <a-col :span="4">
        <a-form-model-item
            label="单价"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ energyCharge.money + '元/度' }}
        </a-form-model-item>
      </a-col>
      <a-col :span="4">
        <a-form-model-item
            label="本期应缴"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ energyCharge.feeMoney }}
        </a-form-model-item>
      </a-col>
    </a-row>
    <a-row v-if="gasCharge.id != null">
      <a-col :span="2">
        <a-form-model-item>
          <span style="font-size: large; font-weight: bold;">燃气表:</span>
        </a-form-model-item>
      </a-col>
      <a-col :span="3">
        <a-form-model-item
            label="上期读数："
           :labelCol="labelCol"
           :wrapperCol="wrapperCol">
          {{ room.gasMeter }}
        </a-form-model-item>
      </a-col>
      <a-col :span="5">
        <a-form-model-item
            label="本期读数："
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          <a-input-number v-model="gasCharge.meterRead"  @change="dushuChange($event, room.gasMeter, gasCharge)" style="width: 100%"></a-input-number>
        </a-form-model-item>
      </a-col>
      <a-col :span="5">
        <a-form-model-item
            label="本期用量"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ gasCharge.useNum }}
        </a-form-model-item>
      </a-col>
      <a-col :span="4">
        <a-form-model-item
            label="单价"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ gasCharge.money + '元/立方' }}
        </a-form-model-item>
      </a-col>
      <a-col :span="4">
        <a-form-model-item
            label="本期应缴"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol">
          {{ gasCharge.feeMoney }}
        </a-form-model-item>
      </a-col>
    </a-row>
  </j-modal>
</template>

<script>

import { getAction, postAction } from '@api/manage'

export default {
  name: 'MeterReadingModal',
  components: {
  },
  data () {
    return {
      title:'',
      width:1100,
      visible: false,
      disableSubmit: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
      livingOrderId: '',
      model: {},
      room: {},
      chargeList: [],
      waterCharge: {},
      energyCharge: {},
      gasCharge: {},

    }
  },
  methods: {
    add (obj) {
      this.visible = true
      this.$nextTick(() => {
      })
    },
    edit (roomId, livingOrderId) {
      this.visible = true
      this.$nextTick(async () => {
        await this.getRoom(roomId)
        await this.getMeterCharge(livingOrderId)
      })
    },
    getRoom(roomId) {
      getAction('/rooms/cesRooms/getRoomById', { roomId: roomId }).then(resp => {
        if (resp.success) {
          this.room = resp.result
        } else {
          this.$message.warning(resp.message)
        }
      })
    },
    getMeterCharge(livingOrderId) {
      getAction('/rooms/houseLongRentCharges/getMeterCharge', { livingOrderId: livingOrderId }).then(resp => {
        if (resp.success) {
          let chargeList = []
          resp.result.forEach(e => {
            if (e.chargeType === 3) {
              this.waterCharge = e
              this.waterCharge.meterRead = this.room.waterMeter
              this.waterCharge.useNum = 0
              this.waterCharge.feeMoney = 0
              chargeList.push(this.waterCharge)
            } else if (e.chargeType === 4) {
              this.energyCharge = e
              this.energyCharge.meterRead = this.room.energyMeter
              this.energyCharge.useNum = 0
              this.energyCharge.feeMoney = 0
              chargeList.push(this.energyCharge)
            } else if (e.chargeType === 5) {
              this.gasCharge = e
              this.gasCharge.meterRead = this.room.gasMeter
              this.gasCharge.useNum = 0
              this.gasCharge.feeMoney = 0
              chargeList.push(this.gasCharge)
            }
          })
          this.chargeList = chargeList
        } else {
          this.$message.warning(resp.message)
        }
      })
    },
    dushuChange(e, lastNum, charge) {
      console.log(e, lastNum, charge)
      if (charge.chargeType === 5) {
        charge.useNum = parseFloat(lastNum - e).toFixed(2)
      } else {
        charge.useNum = parseFloat(e - lastNum).toFixed(2)
      }
      charge.feeMoney = parseFloat(charge.useNum * charge.money).toFixed(2)
      this.$forceUpdate()
    },
    close () {
      this.$emit('close');
      this.visible = false;
    },
    handleOk () {
      if (this.chargeList.some(e => e.useNum < 0)) {
        this.$message.warning('本期读数不能小于上期读数')
      }
      postAction('/business/busOrderFee/addMeterFee', this.chargeList).then(resp => {
        if (resp.success) {
          this.$message.success(resp.message)
          this.$emit('ok')
          this.visible = false
        } else {
          this.$message.warning(resp.message)
        }
      })
      console.log(this.chargeList, 'chargeList')
    },
    submitCallback() {
      this.$emit('ok');
      this.visible = false;
    },
    handleCancel () {
      this.close()
    }
  }
}
</script>
