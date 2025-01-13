<template>
  <div style="background: #FFFFFF">
    <a-row>
      <a-col :span="4" v-for="item in demo">
        <div class="resport" @click="getResport(item)">{{ item.name }}</div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { getAction } from '@api/manage'

export default {
  components: {
  },
  data() {
    return {
      demo: [{
        name: '货品库存报表',
        path: '/report/stock/storageAllCollect'
        },
        {
          name: '出库汇总表',
          path: '/report/stock/storageOutCollect'
        },
        {
          name: '入库汇总表',
          path: '/report/stock/storageInCollect'
        },
        {
          name: '入库明细表',
          path: '/report/stock/storageInDetail'
        },
        {
          name: '出库明细表',
          path: '/report/stock/storageOutDetail'
        },
        {
          name: '商品销售明细报表',
          path: '/report/room/sellDetail'
        },
        {
          name: '商品销售汇总报表',
          path: '/report/room/sellCollect'
        },
      ]
    };
  },
  methods: {
    getResport(item) {
      console.log(item.path)
      this.$router.push(item.path)
      this.$router.push()
    }
  },
  created() {

    // getAction('/order/cesOrderLeaseGoods/getLeaseRoom').then(resp => {
    //   console.log(resp.records,"借用")
    // });
    // getAction('/fw/fwLivingJx/getJxRooms').then(resp => {
    //   console.log(resp.records,"叫醒")
    // })
  }
};
</script>

<style scoped>
.main {
  height: 70% !important;
}

.resport {
  background: rgb(245, 245, 245);
  padding: 20px 0px;
  text-align: center;
  display: block;
  margin: 20px;
}
</style>
