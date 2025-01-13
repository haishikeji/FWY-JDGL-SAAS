<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                  type="false"
                  placeholder="入住单号/房间号/姓名"
                v-model="queryParam.keyw"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
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
          <a @click="handleSelect(record)">选择</a>
        </span>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { httpAction } from "@/api/manage";
import { getAction } from "../../../api/manage";
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
          title: "房间",
          align: "center",
          dataIndex: "roomName",
        },
        {
          title: "名字",
          align: "center",
          dataIndex: "customerName",
        },
        {
          title: "单号",
          align: "center",
          dataIndex: "livingOrderNo",
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "customerId",
          customRender(text) {
            return "正常入住";
          },
        },
        // {
        //   title: "金额",
        //   align: "center",
        //   dataIndex: "payMoney",
        // },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list: "/business/busRoomBookingOrders/living-orders?status=-1",
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
    handleSelect(row) {
      this.$emit("ok", row);
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
