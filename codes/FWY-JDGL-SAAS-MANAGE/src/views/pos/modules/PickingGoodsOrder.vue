<template>
  <a-card :bordered="false">
    <div style="display: flex; min-height: 500px">
      <a-card style="width: 50%">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :span="8">
                <a-form-item label="">
                  <j-input
                    placeholder="取餐号"
                    v-model="queryParam.tableNo"
                  ></j-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="">
                  <a-range-picker
                    format="YYYY-MM-DD"
                    :placeholder="['开始日期', '结束日期']"
                    @change="onChange"
                    v-model="datetime"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="4">
                <span>
                  <a-button type="primary" @click="searchQuery" icon="search"
                    >查询</a-button
                  >
                </span>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <!-- 查询区域-END -->

        <!-- table区域-begin -->
        <div>
          <a-table
            ref="table"
            size="middle"
            :scroll="{ x: true }"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :loading="loading"
            :customRow="rowClick"
            class="j-table-force-nowrap"
            @change="handleTableChange"
          >
            <template slot="avatar" slot-scope="text">
              <a-avatar v-if="text" :src="text" />
              <a-avatar v-else icon="user" />
            </template>
            <template slot="imgSlot" slot-scope="text, record">
              <span v-if="!text" style="font-size: 12px; font-style: italic"
                >无图片</span
              >
              <img
                v-else
                :src="getImgView(text)"
                :preview="record.id"
                height="25px"
                alt=""
                style="max-width: 80px; font-size: 12px; font-style: italic"
              />
            </template>
            <template slot="fileSlot" slot-scope="text">
              <span v-if="!text" style="font-size: 12px; font-style: italic"
                >无文件</span
              >
              <a-button
                v-else
                :ghost="true"
                type="primary"
                icon="download"
                size="small"
                @click="downloadFile(text)"
              >
                下载
              </a-button>
            </template>

            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>

              <a-divider type="vertical" />
              <a-dropdown>
                <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
                <a-menu slot="overlay">
                  <a-menu-item>
                    <a @click="handleDetail(record)">详情</a>
                  </a-menu-item>
                  <a-menu-item>
                    <a-popconfirm
                      title="确定删除吗?"
                      @confirm="() => handleDelete(record.id)"
                    >
                      <a>删除</a>
                    </a-popconfirm>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </span>
          </a-table>
        </div>
      </a-card>
      <a-card style="width: 50%">
        <p>订单详情</p>
        <a-table
          ref="table"
          size="middle"
          :scroll="{ x: true }"
          bordered
          rowKey="id"
          :columns="columns2"
          :dataSource="orderGoodsDetailList"
          :loading="loading"
          class="j-table-force-nowrap"
        >
          <template slot="avatar" slot-scope="text">
            <a-avatar v-if="text" :src="text" />
            <a-avatar v-else icon="user" />
          </template>
          <template slot="imgSlot" slot-scope="text, record">
            <span v-if="!text" style="font-size: 12px; font-style: italic"
              >无图片</span
            >
            <img
              v-else
              :src="getImgView(text)"
              :preview="record.id"
              height="25px"
              alt=""
              style="max-width: 80px; font-size: 12px; font-style: italic"
            />
          </template>
          <template slot="fileSlot" slot-scope="text">
            <span v-if="!text" style="font-size: 12px; font-style: italic"
              >无文件</span
            >
            <a-button
              v-else
              :ghost="true"
              type="primary"
              icon="download"
              size="small"
              @click="downloadFile(text)"
            >
              下载
            </a-button>
          </template>

          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="handleDetail(record)">详情</a>
                </a-menu-item>
                <a-menu-item>
                  <a-popconfirm
                    title="确定删除吗?"
                    @confirm="() => handleDelete(record.id)"
                  >
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </a-card>
    </div>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { httpAction,getAction } from "@/api/manage";
export default {
  name: "BusMemberCardList",
  mixins: [JeecgListMixin, mixinDevice],
  props: {
    couponsId: {
      type: String,
      default: "",
    },
    couponsType: {
      type: Number,
      default: 1,
    },
  },
  data() {
    const hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
    return {
      datetime: [],
      hotelId: "",
      num: 1,
      description: "PickingGoodsOrder",
      // 表头
      columns: [
        {
          title: "单据号",
          align: "center",
          dataIndex: "code",
        },
        {
          title: "取餐号",
          align: "center",
          dataIndex: "tableNo",
        },
        {
          title: "消费金额",
          align: "center",
          dataIndex: "consumeMoney",
        },
        {
          title: "优惠金额",
          align: "center",
          dataIndex: "couponMoney",
        },
        {
          title: "实付金额",
          align: "center",
          dataIndex: "payMoney",
        },
      ],
      columns2: [
        {
          title: "商品名称",
          align: "center",
          dataIndex: "goodsName",
        },
        {
          title: "数量",
          align: "center",
          dataIndex: "num",
        },
        {
          title: "原价",
          align: "center",
          dataIndex: "money",
          customRender(text, record) {
            return text.toFixed(2);
          },
        },
        {
          title: "优惠价",
          align: "center",
          dataIndex: "couponMoney",
          customRender(text, record) {
            return text.toFixed(2);
          },
        },
        {
          title: "优惠金额",
          align: "center",
          dataIndex: "id",
          customRender(text, record) {
            return record.money - record.couponMoney;
          },
        },
        {
          title: "小计",
          align: "center",
          dataIndex: "payMoney",
          customRender(text, record) {
            return (text * record.num).toFixed(2);
          },
        },
      ],
      url: {
        list: "/pos/posOrderGoods/list?status=0",
        delete: "/business/busMemberCard/delete",
        deleteBatch: "/business/busMemberCard/deleteBatch",
        exportXlsUrl: "/business/busMemberCard/exportXls",
        importExcelUrl: "business/busMemberCard/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
      orderGoodsDetailList: [],
      selectOrderInfo: {},
    };
  },
  created() {},
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    submitForm() {
      if (this.orderGoodsDetailList.length <= 0) {
        this.$message.warning("请先选择订单");
        return;
      }
      this.$emit("ok", {
        orderInfo: this.selectOrderInfo,
        orderDetailList: this.orderGoodsDetailList,
      });
    },
    rowClick(row) {
      return {
        on: {
          click: (e) => {
            this.selectOrderInfo = row;
            getAction("/pos/posOrderGoodsDetail/getDetailByOrderId", {
              orderId: row.code,
            }).then((res) => {
              if (res.success) {
                this.orderGoodsDetailList = res.result;
              }
            });
          },
        },
      };
    },
    onChange(e, dateString) {
      this.queryParam.startTime = dateString[0];
      this.queryParam.endTime = dateString[1];
    },
    handleAdd() {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning("请选择一条记录！");
        return;
      }
      if (this.num <= 0) {
        this.$message.warning("发放数量不能小于0！");
        return;
      }
      var that = this;
      that.confirmLoading = true;
      var url = "/business/busMarketCouponsUsed/gainCouponsUsedToMember";
      if (this.couponsType === 2) {
        url = "/business/busMarketCouponsCashUsed/gainCouponsCashUsedToMember";
      }
      httpAction(
        url,
        {
          couponsId: this.couponsId,
          num: this.num,
          memberIds: this.selectedRowKeys,
        },
        "post"
      )
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.$emit("ok");
          } else {
            that.$message.warning(res.message);
          }
        })
        .finally(() => {
          that.confirmLoading = false;
        });
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
/deep/ .ant-input-search-button {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
}
/deep/ .ant-input-search-button[disabled]:hover {
  opacity: 0.7;
  background-color: #ff4d4f;
  border-color: #ff4d4f;
}
/deep/ .ant-input-search-button[disabled] {
  opacity: 0.7;
  color: #ffffff;
}
</style>
