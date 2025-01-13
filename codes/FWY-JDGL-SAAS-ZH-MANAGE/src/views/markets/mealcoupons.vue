<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="4">
            <a-form-item label="">
              <a-input
                placeholder="餐券名称"
                v-model="queryParam.name"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search"
                >查询</a-button
              >
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="danger" icon="plus">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('bus_market_coupons_info')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div> -->

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
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
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
          <a @click="handleConfig(record)">详情</a>
          <a-divider type="vertical" />
          <!-- <a @click="handleProvide(record)">发放</a>
          <a-divider type="vertical" /> -->
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a @click="handleDelete(record.id)">删除</a>
        </span>
      </a-table>
    </div>

    <bus-market-meal-coupons-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></bus-market-meal-coupons-modal>
    <bus-market-meal-coupons-used-list-modal
      ref="modalMealCouponsUsedListModal"
      :couponsId="couponsId"
    ></bus-market-meal-coupons-used-list-modal>

    <provide-index-modal
      ref="modalProvideIndexModal"
      :couponsId="couponsId"
      :couponsType="2"
      @ok="modalFormOk"
    ></provide-index-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import BusMarketMealCouponsModal from "./modules/mealCoupons/BusMarketMealCouponsModal";
import BusMarketMealCouponsUsedListModal from "./modules/mealCoupons/BusMarketMealCouponsUsedListModal";
import ProvideIndexModal from "./modules/couponsProvide/index";
import { deleteAction } from "@/api/manage";
export default {
  name: "coupons",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BusMarketMealCouponsModal,
    BusMarketMealCouponsUsedListModal,
    ProvideIndexModal,
  },
  data() {
    return {
      couponsId: "",
      datetime: [],
      queryParam: {},
      description: "bus_market_coupons_info管理页面",
      // 表头
      columns: [
        // {
        //   title: "#",
        //   dataIndex: "",
        //   key: "rowIndex",
        //   width: 60,
        //   align: "center",
        //   customRender: function (t, r, index) {
        //     return parseInt(index) + 1;
        //   },
        // },
        // {
        //   title: "关联租户",
        //   align: "center",
        //   dataIndex: "tenantId",
        // },
        {
          title: "商家",
          align: "center",
          dataIndex: "hotelName",
        },
        {
          title: "名称",
          align: "center",
          dataIndex: "name",
        },
        {
          title: "餐券类型",
          align: "center",
          dataIndex: "typeName",
        },
        {
          title: "金额",
          align: "center",
          dataIndex: "cost",
        },
        {
          title: "已生成",
          align: "center",
          dataIndex: "count",
        },
        {
          title: "待发放",
          align: "center",
          dataIndex: "notClaimed",
        },
        {
          title: "未核销",
          align: "center",
          dataIndex: "received",
        },
        {
          title: "已核销",
          align: "center",
          dataIndex: "used",
        },
        {
          title: "已作废",
          align: "center",
          dataIndex: "voided",
        },
        {
          title: "说明",
          align: "center",
          dataIndex: "introduce",
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
        list: "/business/busMarketMealCoupons/list",
        delete: "/business/busMarketMealCoupons/delete",
        deleteBatch: "/business/busMarketMealCoupons/deleteBatch",
        exportXlsUrl: "/business/busMarketMealCoupons/exportXls",
        importExcelUrl: "business/busMarketMealCoupons/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    // this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    handleDelete: function (id) {
      if (!this.url.delete) {
        this.$message.error("请设置url.delete属性!");
        return;
      }
      var that = this;
      this.$confirm({
        title: "确认删除",
        content: "是否删除?",
        onOk: function () {
          deleteAction(that.url.delete, { id: id }).then((res) => {
            if (res.success) {
              //重新计算分页问题
              that.reCalculatePage(1);
              that.$message.success(res.message);
              that.loadData();
            } else {
              that.$message.warning(res.message);
            }
          });
        },
        onCancel: function () {},
      });
    },
    handleProvide(item) {
      this.couponsId = item.id;
      this.$refs.modalProvideIndexModal.visible = true;
      this.$refs.modalProvideIndexModal.title = "发放";
      this.$refs.modalProvideIndexModal.disableSubmit = true;
    },
    handleConfig(item) {
      this.couponsId = item.id;
      this.$refs.modalMealCouponsUsedListModal.visible = true;
      this.$refs.modalMealCouponsUsedListModal.title = "详情";
      this.$refs.modalMealCouponsUsedListModal.disableSubmit = true;
      this.$refs.modalMealCouponsUsedListModal.add(item.id);
    },
    searchReset() {
      this.datetime = [];
      this.queryParam = {};
      this.loadData(1);
    },
    onChange(e, dateString) {
      // console.log("Selected Time: ", e);
      // console.log("Formatted Selected Time: ", dateString);
      this.queryParam.startTime = dateString[0];
      this.queryParam.endTime = dateString[1];
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "tenantId", text: "关联租户" });
      fieldList.push({ type: "string", value: "hotelId", text: "关联酒店" });
      fieldList.push({ type: "string", value: "name", text: "名称" });
      fieldList.push({ type: "date", value: "startTime", text: "开始时间" });
      fieldList.push({ type: "date", value: "endTime", text: "结束时间" });
      fieldList.push({ type: "string", value: "tenantId", text: "适用房型" });
      fieldList.push({ type: "int", value: "klqzs", text: "可领取张数" });
      fieldList.push({ type: "string", value: "type", text: "类型" });
      fieldList.push({ type: "string", value: "introduce", text: "说明" });
      fieldList.push({ type: "int", value: "lqNum", text: "领取数量" });
      fieldList.push({ type: "string", value: "ruleDec", text: "使用说明" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>