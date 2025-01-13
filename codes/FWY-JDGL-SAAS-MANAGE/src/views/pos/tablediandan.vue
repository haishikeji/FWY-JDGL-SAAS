<template>
  <a-card :bordered="false" class="card-pd" v-if="isShow">
    <div style="display: flex; height: calc(100vh - 350px)">
      <a-card style="width: 25%">
        <a-row :gutter="[16, 16]">
          <a-col :span="6">
            <div>台号：{{ tableOrderInfo.name }}</div>
          </a-col>
          <a-col :span="6">
            <div>人数：{{ tableOrderInfo.userNum }}</div>
          </a-col>
          <a-col :span="12">
            <div>开台时间：{{ tableOrderInfo.orderTime }}</div>
          </a-col>
        </a-row>
        <p v-if="selectRoomOrder && selectRoomOrder.roomName">
          房间：{{ selectRoomOrder.roomName }},姓名:{{ selectRoomOrder.customerName }}
        </p>
        <a-table
          bordered
          :columns="columns"
          :data-source="selectGoodsList"
          :pagination="false"
          rowKey="id"
          :scroll="{ y: 560 }"
        >
          <template slot="num" slot-scope="text, record, index">
            <div>
              <a-input-number
                v-model="record.num"
                :max="record.inventory"
                @change="(event) => numChange(event, index)"
              />
            </div>
          </template>
          <template slot="required" slot-scope="text, record, index">
            <div>
              <a-switch v-model="record.required" />
            </div>
          </template>

          <span slot="action" slot-scope="text, record, index">
            <a @click="handleDelete(index)">删除</a>
          </span>
        </a-table>
        <p>数量：{{ sum }}</p>
        <div style="display: flex; gap: 5px; flex-flow: wrap">
          <a-button @click="handleClear" v-if="selectGoodsList.length == 0"
            >清台</a-button
          >

          <a-button @click="handleClear" :disabled="btnDisabled">清空</a-button>

          <!-- <a-button @click="handleAdd" :disabled="btnDisabled">备注</a-button> -->
          <!-- <a-button :disabled="btnDisabled">退货</a-button> -->
          <!-- <a-button v-if="!btnDisabled" @click="handleAdd">挂单</a-button> -->
          <!-- <a-button v-else @click="handlePickingGoodsOrder">取单</a-button> -->
          <a-button
            v-if="selectGoodsList.length > 0"
            type="danger"
            @click="handleAdd"
            >下单</a-button
          >
          <a-button
            v-if="selectRoomOrder && selectRoomOrder.roomName && !btnDisabled"
            @click="handleToRoomFeePayment"
            >确定挂房帐</a-button
          >
          <a-button v-else @click="handleSelectCheckInRoomOrder"
            >挂房帐</a-button
          >
          <a-button :disabled="btnDisabled" type="danger" @click="handlePayment"
            >结账￥{{ amount.toFixed(2) }}</a-button
          >
        </div>
      </a-card>
      <a-card style="width: 75%">
        <a-tabs v-model="tabgoodsTypeId" @change="tabGoodsTypeChange">
          <a-tab-pane key="1" tab="全部"></a-tab-pane>
          <a-tab-pane
            :key="item.id"
            :tab="item.name"
            v-for="item in goodsTypeList"
          ></a-tab-pane>
        </a-tabs>
        <a-row :gutter="[5, 5]">
          <a-col
            v-for="item in dataSource"
            :key="item.id"
            :span="3"
            @click.stop="itemClick(item)"
          >
            <div class="room-item check">
              <div class="select-cell"></div>
              <template v-if="item.id != '0'">
                <div>{{ item.name }}</div>
                <div style="margin-top: 10px; color: red">
                  ￥{{ item.sellingPrice.toFixed(2) }}
                </div>
                <div style="margin-top: 10px">库{{ item.inventory }}</div>
                <div
                  v-if="item.isSellClear"
                  style="display: flex; justify-content: right"
                >
                  <a-tag color="#f50"> 沽清 </a-tag>
                </div></template
              >
              <template v-else>
                <div style="font-weight: 800; text-align: center">＋临时菜</div>
              </template>
            </div>
          </a-col>
        </a-row>
      </a-card>
    </div>
    <payment-modal ref="modalPaymentForm" @ok="modalFormOk"></payment-modal>
    <goods-modal ref="modalGoodsForm" @ok="modalFormOk2"></goods-modal>
    <picking-goods-order-modal
      ref="modalPickingGoodsOrderForm"
      @ok="modalPickingGoodsOrderFormOk"
    ></picking-goods-order-modal>
    <select-check-in-room-order-modal
      ref="modalSelectCheckInRoomOrderModal"
      @ok="modalSelectCheckInRoomOrderFormOk"
    ></select-check-in-room-order-modal>
  </a-card>
</template>

  <script>
// import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// import PosRegionModal from "./modules/PosRegionModal";
import { filterObj } from "@/utils/util";
import { getAction, postAction, deleteAction } from "@/api/manage";
import PaymentModal from "./modules/PaymentModal.vue";
import goodsModal from "./modules/goodStock/goodsModal.vue";
import PickingGoodsOrderModal from "./modules/PickingGoodsOrderModal.vue";
import SelectCheckInRoomOrderModal from "./modules/SelectCheckInRoomOrderModal.vue";
import { computed } from "vue";
import { tree } from "@/api/good";
const columns = [
  {
    title: "商品名称",
    dataIndex: "name",
  },
  {
    title: "数量",
    dataIndex: "num",
    scopedSlots: { customRender: "num" },
  },
  {
    title: "售价",
    dataIndex: "sellingPrice",
  },
];
export default {
  name: "memberList",
  components: {
    PaymentModal,
    goodsModal,
    PickingGoodsOrderModal,
    SelectCheckInRoomOrderModal,
  },
  data() {
    return {
      columns,
      sellClear: 0,
      queryParam: {},
      // 分页参数
      ipagination: {
        current: 1,
        pageSize: 99999,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      url: {
        list: "/pos/posSellClearGoods/list",
        delete: "/pos/posSellClearGoods/delete",
        deleteBatch: "/pos/posSellClearGoods/deleteBatch",
        exportXlsUrl: "/pos/posSellClearGoods/exportXls",
        importExcelUrl: "pos/posSellClearGoods/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
      selectedRowKeys: [],
      isorter: {
        column: "createTime",
        order: "desc",
      },
      treeData: [],
      selectGoods: {},
      posTypeList: [],
      tabPosTypeId: "",
      selectGoodsList: [],
      goodsTypeList: [],
      tabgoodsTypeId: "1",
      dataSource: [],
      oldSelectGoodsList: [],
      selectOrderInfo: {},
      selectRoomOrder: {},
      tableOrderInfo: {},
      isShow: true,
    };
  },
  provide() {
    return {
      treeData: computed(() => this.treeData),
    };
  },
  computed: {
    sum() {
      return this.selectGoodsList.reduce(function (total, item) {
        return total + item.num;
      }, 0);
    },
    amount() {
      return this.selectGoodsList.reduce(function (total, item) {
        return total + item.sellingPrice * item.num;
      }, 0);
    },
    btnDisabled() {
      var res = this.selectGoodsList && this.selectGoodsList.length > 0;
      return !res;
    },
  },
  created() {},
  activated() {
    //重新进入界面可能参数id不一样，需要重新加载数据，使用钩子方式
    this.tableId = this.$route.params.tableId;
    this.isShow = true;
    this.dataSource = [{ id: "0" }];
    this.selectGoodsList = [];
    this.oldSelectGoodsList = [];
    this.getSelectGoodsList();
    this.loadTree();
    this.loadGoodsType();
  },
  // deactivated() {
  //   this.isShow = false;
  // },
  methods: {
    getSelectGoodsList() {
      getAction("/pos/posTable/queryById", { id: this.tableId }).then((res) => {
        if (res.success) {
          this.tableOrderInfo = res.result;
          if (
            this.tableOrderInfo.state == 2 ||
            this.tableOrderInfo.state == 3
          ) {
            this.selectOrderInfo = this.tableOrderInfo.posOrderGoods;
            getAction("/pos/posOrderGoodsDetail/getDetailByOrderId", {
              orderId: this.tableOrderInfo.posOrderGoods.code,
            }).then((res2) => {
              if (res2.success) {
                res2.result.forEach((t) => {
                  t.sellingPrice = t.payMoney;
                  t.name = t.goodsName;
                  t.id = t.goodsId;
                });
                this.selectGoodsList = res2.result;
              }
            });
          }
        }
      });
    },
    loadTree() {
      var that = this;
      tree().then((res) => {
        if (res.success) {
          this.treeData = res.result;
        }
      });
    },
    modalFormOk() {
      this.$router.go(-1);
    },
    modalFormOk2(e) {
      console.log(e);
      var good = JSON.parse(JSON.stringify(e));
      this.$set(good, "num", 1);
      this.selectGoodsList.push(good);
    },
    modalSelectCheckInRoomOrderFormOk(e) {
      console.log(e);
      this.selectRoomOrder = e;
    },
    modalPickingGoodsOrderFormOk(e) {
      console.log(e);
      if (e && e.orderDetailList && e.orderDetailList.length > 0) {
        this.selectOrderInfo = e.orderInfo;
        e.orderDetailList.forEach((t) => {
          t.sellingPrice = t.payMoney;
          t.name = t.goodsName;
          t.id = t.goodsId;
        });
        this.selectGoodsList = e.orderDetailList;
      }
    },
    numChange(e, index) {
      console.log(e);
      if (e <= 0) {
        this.selectGoodsList.splice(index, 1);
      }
    },
    handleClear() {
      this.selectGoodsList = [];
      // this.selectOrderInfo = {};
    },
    tabPosTypeChange(e) {
      this.loadGoodsType();
    },
    tabGoodsTypeChange(e) {
      this.loadGoods();
    },
    loadGoodsType() {
      getAction("/rooms/cesStockType/getTopTypesByPosType", {
        posType: this.tabPosTypeId,
      }).then((res2) => {
        if (res2.success) {
          this.goodsTypeList = res2.result;
          if (this.goodsTypeList && this.goodsTypeList.length > 0) {
            this.loadGoods();
          }
        }
      });
    },
    loadGoods() {
      var ids = [];
      if (this.tabgoodsTypeId === "1") {
        this.goodsTypeList.forEach((t) => {
          ids.push(t.id);
        });
      } else {
        ids.push(this.tabgoodsTypeId);
      }
      this.dataSource = [{ id: "0" }];
      getAction("/pos/posSellClearGoods/list", {
        pageNo: 1,
        pageSize: 99999,
        goodTypes: ids,
      }).then((res) => {
        if (res.success) {
          this.dataSource = [...this.dataSource, ...res.result.records];
        }
      });
    },
    handleDelete() {
      deleteAction("/pos/posSellClearGoods/delete", {
        goodsId: this.selectGoods.id,
      })
        .then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
            this.sellClear = 0;
          } else {
            this.$message.warning(res.message);
          }
        })
        .finally(() => {});
    },
    handleSelectCheckInRoomOrder() {
      this.$refs.modalSelectCheckInRoomOrderModal.add();
      this.$refs.modalSelectCheckInRoomOrderModal.title = "选择转帐人";
      this.$refs.modalSelectCheckInRoomOrderModal.disableSubmit = false;
    },
    handlePickingGoodsOrder() {
      this.$refs.modalPickingGoodsOrderForm.add();
      this.$refs.modalPickingGoodsOrderForm.title = "取单";
      this.$refs.modalPickingGoodsOrderForm.disableSubmit = false;
    },
    handleAddTempGoods() {
      this.$refs.modalGoodsForm.add();
      this.$refs.modalGoodsForm.title = "新增菜品";
      this.$refs.modalGoodsForm.disableSubmit = false;
    },
    handleToRoomFeePayment() {
      this.handleAdd();
    },
    handlePayment() {
      this.handleAdd((e) => {
        var amount = this.oldSelectGoodsList.reduce(function (total, item) {
          return total + item.sellingPrice * item.num;
        }, 0);
        this.$refs.modalPaymentForm.edit({
          billAmount: amount,
          deposit: 0,
          roomFee: amount,
          subjectType: 5,
          feeType: 2,
          preferentialType: 1,
          couponFirstAmount: 0,
          discount: 9,
          orderCode: e,
        });
        this.$refs.modalPaymentForm.title = "POS结账";
        this.$refs.modalPaymentForm.disableSubmit = false;
      });
    },
    handleAdd(callback) {
      if (!this.selectGoodsList || this.selectGoodsList.length <= 0) {
        this.$message.warning("请先选择商品");
        return;
      }
      var _info = JSON.parse(localStorage.getItem("storeInfo"));
      var model = { goodsId: this.selectGoods.id };
      if (_info) {
        model.hotelId = _info.id;
      }
      model.id = this.selectOrderInfo.id;
      model.toRoomFeeOrderId = this.selectRoomOrder.livingOrderId;
      model.posType = this.tableOrderInfo.posTypeId;
      model.posTableId = this.tableOrderInfo.id;
      model.tableNo = this.tableOrderInfo.name;
      var posOrderGoodsDetailList = [];
      this.selectGoodsList.forEach((t) => {
        posOrderGoodsDetailList.push({
          goodsId: t.id,
          num: t.num,
          money: t.sellingPrice,
        });
      });
      model.posOrderGoodsDetailList = posOrderGoodsDetailList;
      postAction("/pos/posOrderGoods/add", model)
        .then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.oldSelectGoodsList = JSON.parse(
              JSON.stringify(this.selectGoodsList)
            );
            // this.selectGoodsList = [];
            // this.selectOrderInfo = {};
            // this.selectRoomOrder = {};
            this.tableId = res.result.posTableId;
            this.getSelectGoodsList();
            this.loadGoods();
            if (callback) {
              callback(res.result.code);
            }
          } else {
            this.$message.warning(res.message);
          }
        })
        .finally(() => {});
    },
    itemClick(row) {
      console.log(row);
      if (row.id == "0") {
        this.handleAddTempGoods();
        return;
      }
      if (row.isSellClear) {
        this.$message.warning("选择的商品已沽清");
        return;
      }
      var good = JSON.parse(JSON.stringify(row));
      this.$set(good, "num", 1);
      var find = this.selectGoodsList.find((t) => t.id === row.id);
      console.log("find", find);
      if (find) {
        var m = this.dataSource.find((t) => t.id === row.id);
        if (!find.inventory) {
          find.inventory = m.inventory;
        }
        if (find.inventory > find.num) {
          find.num++;
        }
      } else {
        this.selectGoodsList.push(good);
      }
    },
  },
};
</script>
  <style scoped>
@import "~@assets/less/common.less";
.room-item {
  height: 110px;
  /* line-height: 200px; */
  font-size: 13px;
  padding: 0px 5px;
  border-radius: 5px;
  cursor: pointer;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.check {
  border: #000 solid 3px;
}
.ant-table-wrapper {
  height: calc(100vh - 500px);
}
/deep/ .card-pd .ant-card-body {
  padding: 0 !important;
}
.select-cell {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  z-index: 10;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>
