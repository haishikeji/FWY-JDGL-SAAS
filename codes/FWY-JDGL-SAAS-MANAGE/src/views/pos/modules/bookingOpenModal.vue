<template>
  <div >
    <a-modal
        title="是否继续开台"
        :width="800"
        :visible="visible"
        switchFullscreen
        @ok="handleOk"
        :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
        @cancel="handleCancel"
        footer
        >
      <a-row>
        <p style="color: red">{{ tableInfo.name }}桌台已存在预订，为避免客人用餐冲突，请再次确认</p>
        <a-col>
          <a-card v-for="record in bookingOrderList" style="margin: 20px" v-if="visible">
            <p>预定人：{{ record.customerName }}</p>
            <p>预定联系方式：{{ record.phone }}</p>
            <p>预定就餐时间：{{ record.arriveTime }}</p>
            <p>预定人数：{{ record.peopleNum }}</p>
            <p>预定单号：{{ record.code }}</p>
            <a-button @click="bookingOpen(record)" :loading="loading" style="width: 100%; color: #85d289">预定开台</a-button>
          </a-card>
        </a-col>
        <a-col :span="12" style="padding: 5px">
          <a-button @click="handleCancel" style="width: 100%;  color: #8ecaf6">取消开台</a-button>
        </a-col>
        <a-col :span="12" style="padding: 5px">
          <a-button @click="newOpen" style="width: 100%;  color: #f56c6c">继续开台</a-button>
        </a-col>
      </a-row>
    </a-modal>
    <!-- <order-food ref="orderFood"></order-food> -->
  </div>
</template>

<script>

import { getAction } from '@api/manage'

export default {
  name: 'BookingOpenModel',
  components: {
  },
  data() {
    return {
      visible: false,
      loading: false,
      tableInfo: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 4 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 }
      },
      bookingOrderList: []
    }
  },
  methods: {
    edit(record) {
      this.visible = true
      this.$nextTick(() => {
        this.tableInfo = record
        getAction('pos/posOrderGoods/getBookingByTableId', { tableId: record.id }).then(resp => {
          this.bookingOrderList = resp.result
        })
      })
    },
    handleOk() {

    },
    handleCancel() {
      this.visible = false
    },
    bookingOpen(record) {
      this.loading = true
      getAction('/pos/posOrderGoods/addOrderByBookingId', { bookingId: record.id }).then(resp => {
        if (resp.success) {
          this.$message.success('开台成功')
          this.$emit('ok')
          this.visible = false
        } else {
          this.$message.warning('开台失败')
        }
      }).finally(e => {
        this.loading = false
      })
    },
    newOpen() {
      this.$emit('newOpen', this.tableInfo)
      this.visible = false
    }
  },
  created() {

  }
}
</script>
<style>
</style>
