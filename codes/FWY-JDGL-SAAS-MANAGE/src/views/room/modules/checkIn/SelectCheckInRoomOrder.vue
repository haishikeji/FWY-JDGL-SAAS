<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
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
        rowKey="livingOrderId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :expandable="true"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }">
        <template #expandedRowRender="record">
          <a-table
              size="small"
              :columns="innerColumns"
              :dataSource="record.children"
          >
          </a-table>
        </template>

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
import hotelInfo from '@views/settings/hotelInfo'
import { formatDate } from '@/utils/util'
const columns = [
  {
    title: "房间",
    align: "center",
    dataIndex: "roomName",
  },
  {
    title: "入住类型",
    align: "center",
    dataIndex: 'groupTag',
    customRender: (groupTag) => {
      return groupTag === null? "散" : groupTag
    },
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
]
const innerColumns = [
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
  }
]
export default {
  name: "BusMemberCardList",
  mixins: [JeecgListMixin, mixinDevice],
  props: {
    livingOrderId: {
      type: Array,
      default: null,
    },
    filterIds:{
      type: Array,
      default: null,
    },
    livingRoomId: {
      type: String,
      default: null,
    },
    hotelId: {
      type: String,
      default: null,
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
      columns: columns,
      innerColumns: innerColumns,
      url: {
        list: "/business/busRoomBookingOrders/union-living-orders?status=-1",
        delete: "/business/busMemberCard/delete",
        deleteBatch: "/business/busMemberCard/deleteBatch",
        exportXlsUrl: "/business/busMemberCard/exportXls",
        importExcelUrl: "business/busMemberCard/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
      orderGoodsDetailList: [],
      selectOrderInfo: {},
      end: false,
    };
  },
  watch: {
    dataSource: function (val) {
      if (this.end) return;
      console.log("val", val);
      if (val && val.length > 0) {
        this.dataSource = val.filter(
          (t) => !this.filterIds.includes(t.livingOrderId)
        );
      }
      this.end = true;
      setTimeout(() => {
        this.end = false;
      }, 100);
    },
  },
  created() {},
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {

    // expandIcon(e){
    //   console.log(e)
    //   if(e.record.children && e.record.children > 0){
    //     if (e.expanded) {
    //       return <a style="margin-right:0px" onClick={ele=> {
    //         e.onExpand(e.record, ele);
    //       }}><a-icon type="caret-down"/> </a>
    //     } else{
    //       return <a style="margin-right:0px" onClick={ele => {
    //         e.onExpand(e.record, ele);
    //       }}><a-icon type="caret-right"/></a>
    //     }
    //   }else{
    //     return <span style="margin-right:0px"><a-icon type="smile" /></span>
    //   }
    // },
    searchQuery() {
      this.loadData(1);
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    handleSelect(row) {
      this.$emit("ok", row);
    },
    submitForm() {
      const that = this;
      console.log("this.selectionRows", this.selectionRows);
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning("请先选择订单");
        return;
      }
      // if(this.selectionRows.some(e => {
      //   if (e.relType !== null) {
      //     this.$message.warning( e.roomName + '房间已关联:' + e.groupTag)
      //     return true;
      //   }
      // }))
      that.confirmLoading = true;
      let httpurl =
        '/business/busRoomBookingOrders/merge-order?livingRoomId=' +
        this.livingOrderId + '&hotelId=' + this.hotelId
      let method = 'post';
      var livingOrderIds = [];
      this.selectionRows.forEach((t) => {
        livingOrderIds.push(t.livingOrderId);
      });
      httpAction(httpurl, livingOrderIds, method).then((res) => {
        if (res.success) {
          that.$message.success(res.message);
          that.$emit("ok");
        } else {
          that.$message.warning(res.message);
        }
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
