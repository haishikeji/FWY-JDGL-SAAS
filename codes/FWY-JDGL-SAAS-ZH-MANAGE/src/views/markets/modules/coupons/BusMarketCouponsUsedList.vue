<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="">
              <j-input placeholder="券号" v-model="queryParam.code"></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="">
              <j-input
                placeholder="姓名"
                v-model="queryParam.user_name"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="">
              <j-input
                placeholder="手机号"
                v-model="queryParam.mobile"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="">
              <a-select
                v-model="queryParam.status"
                style="width: 120px"
                placeholder="请选择状态"
                :allowClear="true"
              >
                <a-select-option value="0">未领取</a-select-option>
                <a-select-option value="1">已领取</a-select-option>
                <a-select-option value="2">已使用</a-select-option>
                <a-select-option value="3">已作废</a-select-option>
              </a-select>
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
              <a-button
                @click="handleAdd"
                type="danger"
                style="margin-left: 8px"
                >生成免房券</a-button
              >
              <a-button
                type="primary"
                icon="delete"
                style="margin-left: 8px"
                @click="batchDel"
                >批量删除</a-button
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
          <a v-if="record.statusName!=='已作废'" @click="handleEdit(record)">作废</a>
        </span>
      </a-table>
    </div>

    <bus-market-coupons-used-generate-form-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></bus-market-coupons-used-generate-form-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import BusMarketCouponsUsedGenerateFormModal from "./BusMarketCouponsUsedGenerateFormModal";
import { httpAction, getAction } from "@/api/manage";

export default {
  name: "BusMarketCouponsUsedList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BusMarketCouponsUsedGenerateFormModal,
  },
  props: {
    couponsId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      description: "免房券领取使用表管理页面",
      // 表头
      columns: [
        {
          title: "券号",
          align: "center",
          dataIndex: "code",
        },
        {
          title: "免房券名称",
          align: "center",
          dataIndex: "conponsName",
        },
        // {不知道这个金额是干啥用,数据库表中没加金额字段
        //   title: "金额",
        //   align: "center",
        //   dataIndex: "",
        // },
        {
          title: "姓名",
          align: "center",
          dataIndex: "userName",
        },

        {
          title: "房间号",
          align: "center",
          dataIndex: "roomNumber",
        },
        {
          title: "入住单号",
          align: "center",
          dataIndex: "checkedInNo",
        },
        {
          title: "手机号",
          align: "center",
          dataIndex: "mobile",
        },
        {
          title: "领取时间",
          align: "center",
          dataIndex: "gainTime",
          // customRender: function (text) {
          //   return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
          // },
        },
        {
          title: "使用时间",
          align: "center",
          dataIndex: "usedTime",
          // customRender: function (text) {
          //   return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
          // },
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "statusName",
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 80,
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list: "/business/busMarketCouponsUsed/list?couponsId=" + this.couponsId,
        delete: "/business/busMarketCouponsUsed/delete",
        deleteBatch: "/business/busMarketCouponsUsed/deleteBatch",
        exportXlsUrl: "/business/busMarketCouponsUsed/exportXls",
        importExcelUrl: "business/busMarketCouponsUsed/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    handleEdit(item) {
      var that = this;
      this.$confirm({
        title: "确认作废",
        content: "是否作废?",
        onOk: function () {
          httpAction(
            "/business/busMarketCouponsUsed/voided",
            { id: item.id },
            "put"
          )
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                item.statusName = "已作废";
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {});
        },
        onCancel: function () {},
      });
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({
        type: "string",
        value: "tenantId",
        text: "关联租户",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "hotelId",
        text: "关联酒店",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "couponsId",
        text: "免房券id",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "code",
        text: "券号",
        dictCode: "",
      });
      fieldList.push({
        type: "int",
        value: "status",
        text: "状态 0未领取 1已领取 2已使用 3已作废",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "userid",
        text: "会员id",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "userName",
        text: "姓名",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "roomNumber",
        text: "房间号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "checkedInNo",
        text: "入住单号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "mobile",
        text: "手机号",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "gainTime", text: "领取时间" });
      fieldList.push({ type: "date", value: "usedTime", text: "使用时间" });
      this.superFieldList = fieldList;
    },
    handleAdd() {
      this.$refs.modalForm.add(this.ipagination.total, this.couponsId);
      this.$refs.modalForm.title = "免房券生成";
      this.$refs.modalForm.disableSubmit = false;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>