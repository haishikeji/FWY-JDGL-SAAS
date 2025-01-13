<template>
  <a-card :bordered="false">
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!-- <a-button ghost @click="searchButton('')">全部</a-button>
        <a-button ghost @click="searchButton(0)">待审核</a-button>
        <a-button ghost @click="searchButton(1)">已审核</a-button>
        <a-button ghost @click="searchButton(2)">审核不通过</a-button> -->
      <a-button @click="searchButton('')">全部</a-button>
      <a-button @click="searchButton(0)">待审核</a-button>
      <a-button @click="searchButton(1)">已审核</a-button>
      <a-button @click="searchButton(2)">审核不通过</a-button>
    </div>

    <!-- table区域-begin -->
    <div class="grid-clear">
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
        <template slot="layoutId" slot-scope="text, record">
          {{ getLayoutName(record) }}
        </template>
        <template slot="prefix_name" slot-scope="text, record">
          {{ (record.prefix || "") + record.name }}
        </template>
        <template slot="pictureSlot" slot-scope="text, record">
          <img :src="record.cover" style="width:40px;height40px;" />
        </template>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>

        <span slot="action" slot-scope="text, record">
          <template v-if="record.verifyState == 0">
            <a @click="handleExamine(record)">审核</a>

            <a-divider type="vertical" />
            <a @click="handleEdit(record)">修改</a>
          </template>

          <!-- <a-divider type="vertical" />
                <a @click="handleImage(record)">图片</a> -->
        </span>
      </a-table>
    </div>
    <!-- <room-num-modal ref="modalForm" @ok="modalFormOk"></room-num-modal> -->
    <!-- <room-imgs @saveOk="onImgSave" ref="roomimgmodal"></room-imgs> -->
    <verify-modal ref="verifyModalForm" @ok="modalFormOk"></verify-modal>
    <editVerifyModalVue ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// import roomNumModal from "./roomNumModal.vue"; //  roomLayoutForm 需要替换成房型的表单弹窗
// import roomImgs from './roomImagesForm.vue'
import verifyModal from "../modalList/verifyModal/verifyModal.vue";
import editVerifyModalVue from "../modalList/editVerifyModal/editVerifyModal.vue";

import { getAllLayouts } from "@/api/roomLayout";
import { delBatch, delAll } from "@/api/roomBuildingApi";

let hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
export default {
  mixins: [JeecgListMixin],
  components: {
    // roomNumModal,
    // roomImgs,
    verifyModal,
    editVerifyModalVue,
  },
  data() {
    return {
      list: ["今天", "昨日", "本周", "本月", "更多"],
      activeIndex: 0,
      delLoading: false,
      layouts: [],
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
          title: "房扫姓名",
          align: "center",
          dataIndex: "waiterName",
        },
        {
          title: "房型",
          align: "center",
          dataIndex: "layoutName",
        },
        {
          title: "时间",
          align: "center",
          dataIndex: "createTime",
        },
        {
          title: "房号",
          align: "center",
          dataIndex: "roomNo",
          // scopedSlots: {
          //     customRender: "layoutId"
          // },
        },
        // {
        //     title: "脏房类型",
        //     // align: "center",
        //     dataIndex: "floorName",
        // },
        {
          title: "备注",
          align: "center",
          dataIndex: "remark",
        },
        {
          title: "审核状态",
          align: "center",
          dataIndex: "verifyState",
          customRender: function (text) {
            var msg = "--";
            switch (text) {
              case 0:
                msg = "待审核";
                break;
              case 1:
                msg = "已审核";
                break;
              case 2:
                msg = "审核不通过";
                break;
              default:
                "--";
                break;
            }
            return msg;
          },
        },
        {
          title: "操作",
          align: "center",
          dataIndex: "name",
          scopedSlots: {
            customRender: "action",
          },
        },
      ],
      url: {
        // list: 'org.jeecg.modules.business/busMarketMember/list',
        list: "/fw/fwRoomClean/list",
        delete: "/rooms/cesRooms/remove",
        deleteBatch: "/rooms/cesRooms/deleteBatch",
        exportXlsUrl: "/rooms/cesRooms/exportXls",
        importExcelUrl: "rooms/cesRooms/importExcel",
      },
      buildingFloorSearchTag: {
        name: null,
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
    getAllLayouts().then((res) => {
      if (res.code == 200) {
        this.layouts = res.result.records;
        this.loadData();
      }
    });
  },
  methods: {
    onImgSave() {
      this.loadData();
    },
    onFliterClose() {
      this.filters["buildId"] = null;
      this.filters["floorId"] = null;
      this.ipagination.current = 1;
      this.buildingFloorSearchTag.name = null;
      this.loadData();
    },
    // handleEdit() {},

    searchButton(type) {
      this.queryParam.verifyState = type;
      this.searchQuery();
    },
    modalVerifyFormOk() {},

    handleExamine(record) {
      this.$refs.verifyModalForm.edit(record);
      this.$refs.verifyModalForm.title = "审核";
      this.$refs.verifyModalForm.disableSubmit = false;
    },

    // 搜索
    searchParam(id, name, isBuilding) {
      this.$set(this.buildingFloorSearchTag, "name", name);
      if (isBuilding) {
        this.filters["buildId"] = id;
        if (this.filters["floorId"]) {
          delete this.filters.floorId;
        }
      } else {
        this.filters["floorId"] = id;
        if (this.filters["buildId"]) {
          delete this.filters.buildId;
        }
      }
      this.loadData();
    },
    getLayoutName(row) {
      let i = this.layouts.findIndex((s) => s.id == row.layoutId);
      if (i > -1) {
        return this.layouts[i].name;
      }
      return "";
    },
    // 批量删除
    delBatch() {
      let hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
      let keys = this.selectedRowKeys;
      this.delLoading = true;
      delBatch({
        hotelId: hotelInfo.id,
        idStr: keys.toString(),
      })
        .then((res) => {
          if (res.code == 200) {
            this.selectedRowKeys = [];
            this.$message.success("删除成功");
            this.loadData();
          }
        })
        .finally((_) => {
          this.delLoading = false;
        });
    },
    // 删除所有
    delAll() {
      let hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
      this.delLoading = true;
      delAll({
        hotelId: hotelInfo.id,
      })
        .then((res) => {
          if (res.code == 200) {
            this.selectedRowKeys = [];
            this.$message.success("全部删除成功");
            this.loadData();
          }
        })
        .finally((_) => {
          this.delLoading = false;
        });
    },
    // 批量添加按钮
    onAddBatch() {
      this.$router.push("/tenant/gen/rooms");
    },
    // 全部删除
    onDelAll() {},
    // 显示图片弹窗
    handleImage(row) {
      this.$refs.roomimgmodal.setModel(row);
    },
    onSaveOk() {
      this.loadData();
    },
    handleClick(index) {
      this.activeIndex = index;
      this.searchQuery();
    },
    onChange(checkedValues) {
      console.log("checked = ", checkedValues);
    },
    //选择日期
    onChangeTime(date, dateString) {
      console.log(date, dateString);
    },
  },
};
</script>

<style scoped>
@import "~@assets/less/common.less";

.list-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  border: solid 1px #d9d9d9;
  width: 300px;
  border-radius: 5px;
  /* margin-bottom: 20px; */
  margin-right: 10px;
}

.list-item {
  padding: 10px;
  border-right: solid 1px #d9d9d9;
  cursor: pointer;
  text-align: center;
}

.list-item.active {
  background-color: #1890ff;
  color: #fff;
}

.grid-clear {
  /* display: grid; */
  /* grid-template-columns: 2fr 1fr; */
  /* grid-gap: 10px; */
  width: 100%;
}
</style>
