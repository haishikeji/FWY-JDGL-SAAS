<template>
  <a-card :bordered="false">
    <p>预定维护</p>
    <a-divider />
    <div class="space-align-container">
      <div class="height-100" @click="toPage('/tenant/orderinfo/roompriceinfo')">
        <img
          src="http://oss.qlan99.com/20200529/d575850add3d460d9574df90aee9f132.png"
        />
        <p>房价管理</p>
      </div>

      <!-- <div class="height-100" @click="toPage()">
        <img
          src="http://oss.qlan99.com/20200529/9e08a25e47344f84aed0f5ce58ceadb1.png"
          width="50"
        />
        <p>放量管理</p>
      </div> -->
      <div class="height-100" @click="toPage('/tenant/orderinfo/appraiseinfo')">
        <img
          src="http://oss.qlan99.com/20200529/0a3e72c04b224ca19f74419090db0472.png"
          width="50"
        />
        <p>评价管理</p>
      </div>
      <div class="height-100" @click="toPage('/tenant/orderinfo/messageinfo')">
        <img
          src="http://oss.qlan99.com/20200529/0a3e72c04b224ca19f74419090db0472.png"
          width="50"
        />
        <p>留言管理</p>
      </div>
    </div>
    <p style="margin-top: 25px">订单管理</p>
    <a-divider />
    <div class="space-align-container">
      <!-- <div class="height-100" @click="toPage()">
        <img
          src="http://oss.qlan99.com/20200529/1e5f75e885454e8e83c563afec910123.png"
        />
        <p>酒店订单</p>
      </div>
      <div class="height-100" @click="toPage()">
        <img
          src="http://oss.qlan99.com/20200529/6739df10d521497ba85099650c99ac0f.png"
        />
        <p>商品订单</p>
      </div>
      <div class="height-100" @click="toPage()">
        <img
          src="http://oss.qlan99.com/20200529/1e5f75e885454e8e83c563afec910123.png"
        />
        <p>点餐订单</p>
      </div> -->
      <div class="height-100" @click="toPage('/tenant/orderinfo/hotelorderinfo')">
        <img
          src="http://oss.qlan99.com/20200529/1e5f75e885454e8e83c563afec910123.png"
        />
        <p>酒店订单</p>
      </div>
      <div class="height-100" @click="toPage('/tenant/orderinfo/servicerepairorderinfo')">
        <img
          src="http://oss.qlan99.com/20200529/1e5f75e885454e8e83c563afec910123.png"
        />
        <p>服务维修订单</p>
      </div>
      <div class="height-100" @click="toPage('/tenant/orderinfo/mealorderinfo')">
        <img
          src="http://oss.qlan99.com/20200529/1e5f75e885454e8e83c563afec910123.png"
        />
        <p>点餐订单</p>
      </div>
    </div>
  </a-card>
</template>

<script>
export default {
  data() {
    return {};
  },
  methods: {
    toPage(url) {
      if (!url) {
        this.$message.warning("开发中,请先体验其他");
        return;
      }
      this.$router.push(url);
    },
  },
};
</script>

<style scoped>
.height-100 {
  height: 100px;
  line-height: 100px;
  background: #f5f5f5;
  width: 15vw;
  align-items: center;
  display: flex;
  cursor: pointer;
  padding: 4px;
  margin: 8px 4px;
}
.height-100 img {
  margin: 15px;
  width: 50px;
  height: 50px;
}
.height-100 p {
  font-weight: 600;
  font-size: 14px;
  color: #000;
}

.space-align-container {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
}
.space-align-block {
  margin: 8px 4px;
  border: 1px solid #40a9ff;
  padding: 4px;
  flex: none;
}
.space-align-block .mock-block {
  display: inline-block;
  padding: 32px 8px 16px;
  background: rgba(150, 150, 150, 0.2);
}
</style>
