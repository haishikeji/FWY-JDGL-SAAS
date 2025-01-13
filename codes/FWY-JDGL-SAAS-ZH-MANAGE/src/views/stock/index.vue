<template>
  <a-card style="width: 100%;">
    <a-tabs v-model="activeKey">
      <a-tab-pane key="1" >
        <span slot="tab"> 仓库管理 </span>
        <depository></depository>
      </a-tab-pane>
      <a-tab-pane key="2">
        <span slot="tab"> 商品管理 </span>
<!--        <goods v-if="activeKey == '2'"></goods>-->
        <good-list v-if="activeKey == '2'" ></good-list>
      </a-tab-pane>
      <a-tab-pane key="3">
        <span slot="tab"> 供应商管理 </span>
        <supplier v-if="activeKey == '3'"></supplier>
      </a-tab-pane>
      <a-tab-pane key="4">
        <span slot="tab"> 出入库管理 </span>
        <stock v-if="activeKey == '4'"></stock>
      </a-tab-pane>
      <a-tab-pane key="5">
        <span slot="tab"> 商品库存统计 </span>
        <depositoryingoods></depositoryingoods>
      </a-tab-pane>
      <a-tab-pane key="6">
        <span slot="tab"> 库存盘点 </span>
        <stockChecks />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
import depository from "./depository.vue";
import goods from "./goods.vue";
import supplier from "./supplier.vue";
import stock from "./stock.vue";
import depositoryingoods from './depositoryingoods'
import stockChecks from './stockChecks/stockChecks.vue'
import GoodList from '@views/settings/components/roomModules/goodList'
export default {
  components: {
    GoodList,
    depository,
    goods,
    supplier,
    stock,
    depositoryingoods,
    stockChecks
  },
  data() {
    return { activeKey: "1" };
  },
};
</script>

<style scoped>
.main {
  height: 70% !important;
}
</style>
