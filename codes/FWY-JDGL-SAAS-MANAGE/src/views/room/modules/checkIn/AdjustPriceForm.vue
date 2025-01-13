<template>
  <j-modal
    :title="title"
    :width="400"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <div>
      <a-row>
        <a-col :span="24">
          <a-input-number min="0" @change="roomBatchPrice" placeholder="批量改价"></a-input-number>
        </a-col>
      </a-row>
      <a-tabs
        :active-key="activePage"
        @change="changePage"
      >

        <a-tab-pane v-for="roomInfo in roomInfoList" :key="roomInfo.id" :tab="roomInfo.roomName">
          <a-card style="width: 300px; height: 400px; overflow-y: scroll" >
            <div v-for="dayPrice in roomInfo.dayPrices" :key="dayPrice.id">
              {{ dayPrice.dayTime }}
              <a-input-number :min="0" v-model="dayPrice.price"></a-input-number>
            </div>
          </a-card>
        </a-tab-pane>
      </a-tabs>
    </div>

  </j-modal>
</template>

<script>
import { postAction } from '@api/manage'

export default {
  components: {
  },
  data() {
    return {
      title: '',
      visible: false,
      disableSubmit: false,
      showYinshou: true,
      activePage: '',
      roomInfoList: []
    }
  },
  methods: {
    edit(roomInfos) {
      this.visible = true
      this.roomInfoList = []
      roomInfos.forEach(e => {
        // console.log(new Date().format('yyyy-MM-dd'))
        // console.log(e.livingDayPrices, 'e.livingDayPrices')
        let dayPrices = e.livingDayPrices.filter(dayPrice => dayPrice.dayTime >= new Date().format('yyyy-MM-dd'))
        const roomInfo = {
          id: e.id,
          livingOrderId: e.livingOrder.id,
          roomName: e.roomName,
          dayPrices
        }
        this.roomInfoList.push(roomInfo)
      })
      this.activePage = this.roomInfoList[0].id
      console.log(this.roomInfoList)
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      let params = this.roomInfoList.filter(e => e.dayPrices.length > 0)
      postAction('/business/busRoomBookingOrders/batch-update-order-price', params)
          .then(resp => {
            if (resp.result === true) {
              this.$message.success('调价成功')
              this.$emit('ok')
              this.visible = false
            } else {
              this.$message.warning('调价失败')
            }
          }
      )
    },
    handleCancel() {
      this.close()
    },
    changePage(key) {
      this.activePage = key
    },
    roomBatchPrice(newPrice) {
      this.roomInfoList.forEach(e => {
        e.dayPrices.forEach(ele => {
          ele.price = newPrice
        })
      })
    }
  }
}
</script>
