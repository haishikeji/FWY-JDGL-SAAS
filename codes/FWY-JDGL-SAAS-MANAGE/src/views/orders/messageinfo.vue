<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="3">
            <a-form-item label="">
              <a-select
                mode="multiple"
                style="width: 180px"
                placeholder="商家名称"
                :maxTagCount="1"
                :maxTagTextLength="50"
                v-model="queryParam.hotelIds"
              >
                <a-select-option
                  v-for="(item, index) in hotelList"
                  :key="index"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-input placeholder="房号" v-model="queryParam.roomNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-input placeholder="姓名" v-model="queryParam.userName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-input placeholder="手机号" v-model="queryParam.userMobile"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-select
                v-model="queryParam.commentType"
                style="width: 100%"
                placeholder="类型"
              >
                <a-select-option value="1">投诉</a-select-option>
                <a-select-option value="2">建议</a-select-option>
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
              <!-- <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              > -->
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
          <!-- <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" /> -->
          <a @click="handleDelete(record.id)">删除</a>
        </span>
      </a-table>
    </div>

    <ces-order-message-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></ces-order-message-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import CesOrderMessageModal from "./modules/CesOrderMessageModal";
import { httpAction, postAction, getAction } from "@/api/manage";
export default {
  name: "CesOrderMessageList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    CesOrderMessageModal,
  },
  data() {
    return {
      description: "ces_order_message管理页面",
      // 表头
      columns: [
        {
          title: "商家",
          align: "center",
          dataIndex: "hotelName",
        },
        {
          title: "留言类型",
          align: "center",
          dataIndex: "messageType",
          customRender: function (text) {
            return text == 1 ? "投诉" : "建议";
          },
        },
        {
          title: "内容",
          align: "center",
          dataIndex: "contentBody",
        },
        {
          title: "姓名",
          align: "center",
          dataIndex: "userName",
        },
        {
          title: "手机号",
          align: "center",
          dataIndex: "userMobile",
        },
        {
          title: "房号",
          align: "center",
          dataIndex: "roomNo",
        },
        {
          title: "创建时间",
          align: "center",
          dataIndex: "createDate",
          customRender: function (text) {
            return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
          },
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
        list: "/order/cesOrderMessage/list",
        delete: "/order/cesOrderMessage/delete",
        deleteBatch: "/order/cesOrderMessage/deleteBatch",
        exportXlsUrl: "/order/cesOrderMessage/exportXls",
        importExcelUrl: "order/cesOrderMessage/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
      hotelList: [],
    };
  },
  created() {
    getAction("/business/busHotel/list", { pageNo: 1, pageSize: 100 }).then(
      (res) => {
        if (res.success) {
          this.hotelList = res.result.records;
        }
      }
    );
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "tenantId", text: "关联租户" });
      fieldList.push({ type: "string", value: "hotelId", text: "关联酒店" });
      fieldList.push({ type: "int", value: "userId", text: "用户ID" });
      fieldList.push({
        type: "int",
        value: "messageType",
        text: "留言类型 1 投诉 2建议",
      });
      fieldList.push({ type: "string", value: "images", text: "图片逗号分隔" });
      fieldList.push({ type: "string", value: "contentBody", text: "内容" });
      fieldList.push({ type: "string", value: "userName", text: "姓名" });
      fieldList.push({ type: "string", value: "userMobile", text: "手机号" });
      fieldList.push({ type: "string", value: "roomNo", text: "房号" });
      fieldList.push({ type: "date", value: "createDate", text: "创建时间" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>