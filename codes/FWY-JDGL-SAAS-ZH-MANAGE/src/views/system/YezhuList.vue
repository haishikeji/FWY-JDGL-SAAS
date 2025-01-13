<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="员工姓名/账号"
                v-model="queryParam.name"
              ></j-input>
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
              <a-button
                @click="handleAdd"
                style="margin-left: 8px"
                type="primary"
                icon="plus"
                >新增业主</a-button
              >
              <a-button
                @click="handleshuilv"
                style="margin-left: 8px"
                type="primary"
                >配置税率</a-button
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
        <template slot="payFlagslot" slot-scope="text, record, index">
          {{ record.payFlag == 1 ? "是" : "否" }}
        </template>
        <template slot="iconslot" slot-scope="text, record, index">
          <a-icon :type="record.icon" theme="filled" />
        </template>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="inRoomList" slot-scope="text, record">
          <span v-if="!text || text.length == 0">--</span>
          <a-tag color="cyan" v-for="(item, index) in text" :key="index">
            {{ item.roomName }}
          </a-tag>
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
          <a @click="handleInRooms(record)">关联房间</a>

          <a-divider type="vertical" />
          <a @click="handleEdit(record)">配置分成</a>

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
    <sys-user-modal ref="modalForm" @ok="modalFormOk"></sys-user-modal>
    <bus-yezhu-modal
      ref="modalBusYezhuForm"
      @ok="modalFormOk"
    ></bus-yezhu-modal>
    <user-room-relation-modal ref="UserRoomRelationModal" @ok="modalFormOk"></user-room-relation-modal>
    <shuilv-modal ref="modalshuilvForm"></shuilv-modal>
  </a-card>
</template>
  
  <script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import SysUserModal from "./modules/yezhu/SysUserModal";
import BusYezhuModal from "./modules/yezhu/BusYezhuModal";
import shuilvModal from "./modules/yezhu/shuilvModal";
import UserRoomRelationModal from "./modules/yezhu/UserRoomRelationModal";
import { filterObj } from "@/utils/util";
import { getAction } from "@/api/manage";
export default {
  name: "memberList",
  mixins: [JeecgListMixin],
  components: {
    SysUserModal,
    BusYezhuModal,
    UserRoomRelationModal,
    shuilvModal
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
          title: "姓名",
          align: "center",
          dataIndex: "userName",
        },
        {
          title: "用户名",
          align: "center",
          dataIndex: "name",
        },
        {
          title: "关联房间",
          align: "center",
          dataIndex: "inRoomList",
          scopedSlots: { customRender: "inRoomList" },
        },
        {
          title: "分成",
          align: "center",
          dataIndex: "commission",
          customRender: function (text) {
            return text > 0 ? "按照百分比：" + text + "%" : "--";
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
        list: "/business/busYezhu/list",
        delete: "/business/busYezhu/delete",
        deleteBatch: "/business/busYezhu/deleteBatch",
        exportXlsUrl: "/business/busYezhu/exportXls",
        importExcelUrl: "business/busYezhu/importExcel",
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
  created() {},
  methods: {
    handleshuilv(){
      this.$refs.modalshuilvForm.add();
      this.$refs.modalshuilvForm.title = "税率";
      this.$refs.modalshuilvForm.disableSubmit = false;
    },
    handleEdit(record) {
      record.type = 1;
      this.$refs.modalBusYezhuForm.edit(record);
      this.$refs.modalBusYezhuForm.title = "编辑";
      this.$refs.modalBusYezhuForm.disableSubmit = false;
    },
    handleInRooms(record) {
      this.$refs.UserRoomRelationModal.show(record);
      this.$refs.UserRoomRelationModal.title = "选择关联房间号";
      this.$refs.UserRoomRelationModal.disableSubmit = false;
    },
    modalFormOk() {
      this.loadData();
    },
  },
};
</script>
  <style scoped>
@import "~@assets/less/common.less";
</style>