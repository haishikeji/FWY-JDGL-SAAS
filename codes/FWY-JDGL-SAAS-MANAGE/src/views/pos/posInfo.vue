<template>
  <a-card style="width: 100%; height: 100%">
    <a-tabs v-model="activeKey" @change="tabChange">
      <a-tab-pane key="1">
        <span slot="tab"> 点单 </span>
        <diandan ref="diandan" :tableId="diandanId" :isUnionStation="isUnionStation" :orderId="orderId" :tableState="diandanState" :tableName="diandanName" @reTable="reTable"></diandan>
      </a-tab-pane>
      <a-tab-pane key="2">
        <span slot="tab"> 桌台点单 </span>
        <tablediandangallery v-if="activeKey == '2'" ref="tablediandangallery" @retId="retId"></tablediandangallery>
      </a-tab-pane>
      <a-tab-pane key="3">
        <span slot="tab"> 订单列表 </span>
        <goods-order v-if="activeKey == '3'"></goods-order>
      </a-tab-pane>
      <a-tab-pane key="4">
        <span slot="tab"> pos类型管理 </span>
        <pos-type></pos-type>
      </a-tab-pane>
      <a-tab-pane key="5">
        <span slot="tab"> 扫码点餐管理 </span>
        <pos-thali></pos-thali>
      </a-tab-pane>
      <a-tab-pane key="6">
        <span slot="tab"> 预定 </span>
        <reserve></reserve>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
import diandan from './diandan.vue'
import posType from './posType.vue'
import goodsOrder from './goodsOrder.vue'
import tablediandangallery from './tablediandangallery.vue'
import posThali from './posThali.vue'
import Reserve from '@views/pos/reserve'

export default {
  components: {
    Reserve,
    diandan,
    posType,
    goodsOrder,
    tablediandangallery,
    posThali
  },
  data() {
    return {
      activeKey: '1',
      diandanId: '',
      diandanState: -1,
      diandanName: '',
      orderId: '',
      // 判断是否可以取消联台
      isUnionStation: null
    }
  },
  methods: {
    retId(id, state, name,orderId,isUnionStation) {
      this.activeKey = '1'
      this.diandanId = ''
      this.orderId = orderId
      this.isUnionStation = isUnionStation

      setTimeout(()=>{
        this.diandanId = id
      }, 200)
      this.diandanState = state
      this.diandanName = name
    },
    reTable() {
      this.activeKey = '2'
      this.$refs.diandan.modalFormOk()
    },
    tabChange(e) {
      if (e !== '1') {
        this.$refs.diandan.clean()
        this.$refs.diandan.posTableId = ''
        this.$refs.diandan.posTableState = -1
      }
    }
  }
}
</script>

<style scoped>
.main {
  height: 70% !important;
}
</style>
