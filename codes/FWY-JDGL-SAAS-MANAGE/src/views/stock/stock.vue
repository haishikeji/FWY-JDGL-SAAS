<template>
  <a-card :bordered="false">
    <a-tabs type="card" v-model="activeKey">
      <a-tab-pane key="1">
        <span slot="tab"> 商品入库 </span>
        <storage></storage>
      </a-tab-pane>
      <a-tab-pane key="2">
        <span slot="tab"> 商品出库 </span>
        <outbound />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
  
  <script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { filterObj } from "@/utils/util";
import { getAction } from "@/api/manage";
import DepositoryModal from "./modules/DepositoryModal.vue";
import storage from "./storage.vue";
import outbound from './outbound.vue'
export default {
  name: "memberList",
  mixins: [JeecgListMixin],
  components: {
    storage,
    outbound,
  },
  data() {
    return {
      activeKey: "1",
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
    // this.loadData()
  },
  methods: {
  },
};
</script>
  <style scoped>
@import "~@assets/less/common.less";
</style>