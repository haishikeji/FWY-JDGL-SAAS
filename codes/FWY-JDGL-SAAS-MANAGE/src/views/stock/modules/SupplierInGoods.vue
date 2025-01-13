<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="">
              <a-input
                placeholder="商品名称"
                v-model="queryParam.goodsName"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <span>
              <a-button type="primary" @click="searchQuery" icon="search"
                >查询</a-button
              >
            </span>
          </a-col>
          <a-col :span="3">
            <a-button @click="handleAdd" type="danger" style="margin-left: 8px"
              >新增</a-button
            ></a-col
          >
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

        <template slot="price" slot-scope="text, record, index">
          <div>
            <a-input-number
              v-model="record.price"
              :min="0"
              @change="priceChange($event, record)"
            />
          </div>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleDelete(record.id)">删除</a>
        </span>
      </a-table>
    </div>
    <select-goods-modal ref="modalForm" @ok="modalFormOk"></select-goods-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { httpAction } from "@/api/manage";
import SelectGoodsModal from "./SelectGoodsModal";
export default {
  name: "BusMemberCardList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SelectGoodsModal,
  },
  props: {
    supplierId: {
      type: String,
      default: "",
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
          title: "商品名称",
          align: "center",
          dataIndex: "goodsName",
        },
        {
          title: "商品类别",
          align: "center",
          dataIndex: "goodsTypeName",
        },
        {
          title: "供应价",
          align: "center",
          dataIndex: "price",
          scopedSlots: { customRender: "price" },
        },
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
        list: "/kc/kcSupplierInGoods/list?supplierId=" + this.supplierId,
        delete: "/kc/kcSupplierInGoods/delete",
        deleteBatch: "/kc/kcSupplierInGoods/deleteBatch",
        exportXlsUrl: "/kc/kcSupplierInGoods/exportXls",
        importExcelUrl: "/kc/kcSupplierInGoods/importExcel",
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
    priceChange(e, record) {

    },
    submitForm(){
      let httpurl = "/kc/kcSupplierInGoods/edit";
      let method = "put";
      httpAction(httpurl, this.dataSource, method)
          .then((res) => {
            if (res.success) {
              this.$message.success(res.message);
              this.$emit('ok')
              

            } else {
              this.$message.warning(res.message);
            }
          })
    },
    handleAdd() {
      this.$refs.modalForm.add();
      this.$refs.modalForm.title = "选择供货商品";
      this.$refs.modalForm.disableSubmit = false;
      this.$refs.modalForm.supplierId = this.supplierId;
    },
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
