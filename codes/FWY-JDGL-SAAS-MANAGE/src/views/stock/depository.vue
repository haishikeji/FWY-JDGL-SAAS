<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="3">
            <a-form-item label="">
              <j-input placeholder="名称" v-model="queryParam.name"></j-input>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
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
        <template slot="payFlagslot" slot-scope="text, record, index">
          {{ record.payFlag == 1 ? "是" : "否" }}
        </template>
        <template slot="iconslot" slot-scope="text, record, index">
          <a-icon :type="record.icon" theme="filled" />
        </template>
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
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-popconfirm
            title="确定删除吗?"
            @confirm="() => handleDelete(record.id)"
          >
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>

    <depository-modal ref="modalForm" @ok="modalFormOk"></depository-modal>
  </a-card>
</template>
  
  <script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { filterObj } from "@/utils/util";
import { getAction } from "@/api/manage";
import DepositoryModal from "./modules/DepositoryModal.vue";
export default {
  name: "memberList",
  mixins: [JeecgListMixin],
  components: {
    DepositoryModal,
  },
  data() {
    return {
      queryParam: {},
      // 分页参数
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      // 表头
      columns: [
        {
          title: "仓库名称",
          align: "center",
          dataIndex: "name",
        },
        {
          title: "仓库地址",
          align: "center",
          dataIndex: "address",
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "state",
          customRender(text) {
            return text === 1 ? "启用" : "停用";
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
        list: "/kc/kcDepository/list",
        delete: "/kc/kcDepository/delete",
        deleteBatch: "/kc/kcDepository/deleteBatch",
        exportXlsUrl: "/kc/kcDepository/exportXls",
        importExcelUrl: "pos/posType/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
      selectedRowKeys: [],
      isorter: {
        column: "createTime",
        order: "desc",
      },
    };
  },
  created() {
    getAction("/finance/summary/financeSummaryPage", {}).then((res) => {
      if (res.success) {
      }
    });
  },
  methods: {},
};
</script>
  <style scoped>
@import "~@assets/less/common.less";
</style>