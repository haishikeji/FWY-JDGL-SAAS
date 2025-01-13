<template>
  <a-card :bordered="false">
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-tag color="pink" v-if="buildingFloorSearchTag.name">
        {{ buildingFloorSearchTag.name }}
        <a-icon type="close" @click="onFliterClose" />
      </a-tag>
      <div style="">
        <div class="list-container-1">
          <div
            class="list-item"
            v-for="(item, index) in layouts"
            :key="index"
            :class="{ active: activeIndex === index }"
            @click="handleClick(item, index)"
          >
            {{ item.name }}
          </div>
        </div>
      </div>
    </div>

    <!-- table区域-begin -->
    <div class="grid-clear">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="[layouts[activeIndex]]"
        :pagination="false"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="houseType" v-if="layouts.length > 0">
          <div>
            {{ layouts[activeIndex].name }}
          </div>
        </template>
        <template slot="continueConsume" slot-scope="text, record">
          <div
            class="flex"
            style="margin-bottom: 10px"
            v-for="(item, index) in xfData.filter((item) => item.type == 1)"
            :key="index"
          >
            <div>{{ item.goodName }}</div>
            <a-input-number v-model="item.num" :min="0" :max="1000" :step="1" />
          </div>
          <!-- <div class="flex">
                    <div>一次性香皂(个)</div>
                    <a-input-number :min="0" :max="1000" :step="1" />
                </div> -->
        </template>
        <template slot="Linen" slot-scope="text, record">
          <div
            class="flex"
            style="margin-bottom: 10px"
            v-for="(item, index) in xfData.filter((item) => item.type == 2)"
            :key="index"
          >
            <div>{{ item.goodName }}</div>
            <a-input-number v-model="item.num" :min="0" :max="1000" :step="1" />
          </div>
        </template>
        <template slot="outConsume">
          <div
            class="flex"
            style="margin-bottom: 10px"
            v-for="(item, index) in tfData.filter((item) => item.type == 1)"
            :key="index"
          >
            <div>{{ item.goodName }}</div>
            <a-input-number v-model="item.num" :min="0" :max="1000" :step="1" />
          </div>
        </template>
        <template slot="outLinen">
          <div
            class="flex"
            style="margin-bottom: 10px"
            v-for="(item, index) in tfData.filter((item) => item.type == 2)"
            :key="index"
          >
            <div>{{ item.goodName }}</div>
            <a-input-number v-model="item.num" :min="0" :max="1000" :step="1" />
          </div>
        </template>
        <!-- <span slot="action" slot-scope="text, record">
                <a @click="handleEdit(record)">编辑</a>

                <a-divider type="vertical" />
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                </a-popconfirm>
                <a-divider type="vertical" />
                <a @click="handleImage(record)">图片</a>
            </span> -->
      </a-table>
    </div>
    <!-- <room-num-modal ref="modalForm" @ok="modalFormOk"></room-num-modal> -->
    <!-- <room-imgs @saveOk="onImgSave" ref="roomimgmodal"></room-imgs> -->
  </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// import roomNumModal from "./roomNumModal.vue"; //  roomLayoutForm 需要替换成房型的表单弹窗
// import roomImgs from './roomImagesForm.vue'
import { getAllLayouts } from "@/api/roomLayout";
import { delBatch, delAll } from "@/api/roomBuildingApi";

import {
  deleteAction,
  getAction,
  downFile,
  getFileAccessHttpUrl,
} from "@/api/manage";
import { postAction } from "../../../api/manage";

let hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
export default {
  mixins: [JeecgListMixin],
  components: {
    // roomNumModal,
    // roomImgs
  },
  data() {
    return {
      list: [],
      activeIndex: 0,
      delLoading: false,
      layouts: [{ children: [] }],
      queryParam: {
        roomLayoutId: "",
      },
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
          title: "房型",
          align: "center",
          width: 150,
          scopedSlots: {
            customRender: "houseType",
          },
          // dataIndex: "hotelName",
        },
        {
          title: "续房",
          // align: "center",
          // dataIndex: "floorName",
          children: [
            {
              title: "客房易耗品",
              // dataIndex: 'continueConsume',
              // key: 'companyAddress',
              width: 200,
              scopedSlots: {
                customRender: "continueConsume",
              },
            },
            {
              title: "布草",
              // dataIndex: 'Linen',
              // key: 'companyAddress',
              width: 200,
              scopedSlots: {
                customRender: "Linen",
              },
            },
          ],
        },
        {
          title: "退房",
          // align: "center",
          // dataIndex: "buildName",
          children: [
            {
              title: "客房易耗品",
              // dataIndex: 'outConsume',
              // key: 'companyAddress',
              width: 200,
              scopedSlots: {
                customRender: "outConsume",
              },
            },
            {
              title: "布草",
              // dataIndex: 'buildName',
              // key: 'companyAddress',
              width: 200,
              scopedSlots: {
                customRender: "outLinen",
              },
            },
          ],
        },
      ],
      url: {
        // list: 'org.jeecg.modules.business/busMarketMember/list',
        list: "/fw/fwRoomLayoutInConsumable/layoutInConsumableList",
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
      xfData: [],
      tfData: [],
    };
  },
  created() {
    // this.loadData()
    getAllLayouts().then((res) => {
      if (res.code == 200) {
        this.layouts = res.result.records;
        // this.layouts.forEach(ele => {
        //     ele.children = []
        // })
        this.queryParam.roomLayoutId = this.layouts[0].id;
        this.loadData(0, 1);
        this.loadData(0, 2);
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
    handleClick(item, index) {
      this.activeIndex = index;
      this.queryParam.roomLayoutId = item.id;
      this.loadData(0, 1);
      setTimeout(() => {
        this.loadData(0, 2);
      }, 1000);
      // this.searchQuery()
    },
    onChange(checkedValues) {
      console.log("checked = ", checkedValues);
    },
    //选择日期
    onChangeTime(date, dateString) {
      console.log(date, dateString);
    },
    loadData(arg, type) {
      if (this.url.list == 2) {
        return;
      }
      if (this.layouts.length == 0) {
        return;
      }
      if (!this.url.list) {
        this.$message.error("请设置url.list属性!");
        return;
      }
      if (!type) {
        return;
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams(); //查询条件
      if (type) {
        params.type = type;
      }
      this.loading = true;
      getAction(this.url.list, params)
        .then((res) => {
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records || res.result;
            if (type == 1) {
              this.xfData = res.result.records || res.result;
            } else if (type == 2) {
              this.tfData = res.result.records || res.result;
            }
            if (res.result.total) {
              this.ipagination.total = res.result.total;
            } else {
              this.ipagination.total = 0;
            }
            //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          } else {
            this.$message.warning(res.message);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    submitForm() {
      if (this.xfData.length == 0 && this.tfData.length == 0) {
        this.$message.error("未获取到数据!");
        return;
      }

      let xf = JSON.parse(JSON.stringify(this.xfData));
      let tf = JSON.parse(JSON.stringify(this.tfData));

      xf.forEach((ele) => {
        ele.type = 1;
      });
      tf.forEach((ele) => {
        ele.type = 2;
      });

      let arr = xf.concat(tf);

      arr.forEach((ele) => {
        ele.roomLayoutId = this.layouts[this.activeIndex].id;
      });
      postAction("/fw/fwRoomLayoutInConsumable/layoutInConsumableSave", arr)
        .then((res) => {
          if (res.success) {
            this.$message.success("保存成功!");
            this.$emit("ok");
          } else {
            this.$message.warning(res.message);
          }
        })
        .finally(() => {});

      // if (this.xfData.length > 0) {

      //     this.xfData.forEach(ele => {
      //         ele.roomLayoutId = this.layouts[this.activeIndex].id
      //     })

      //     postAction('/fw/fwRoomLayoutInConsumable/layoutInConsumableSave', this.xfData).then((res) => {
      //         if (res.success) {
      //             // this.$message.success("保存成功!")
      //             this.$emit('ok')
      //         } else {
      //             this.$message.warning(res.message)
      //         }
      //     }).finally(() => {

      //     })
      // }
      // if (this.tfData.length > 0) {
      //     this.tfData.forEach(ele => {
      //         ele.roomLayoutId = this.layouts[this.activeIndex].id
      //     })
      //     postAction('/fw/fwRoomLayoutInConsumable/layoutInConsumableSave', this.tfData).then((res) => {
      //         if (res.success) {
      //             this.$message.success("保存成功!")
      //             this.$emit('ok')
      //         } else {
      //             this.$message.warning(res.message)
      //         }
      //     }).finally(() => {

      //     })
      // }
    },
  },
};
</script>

<style scoped>
@import "~@assets/less/common.less";

.list-container-1 {
  display: flex;
  /* margin-right: 10px; */
  width: 100%;
}

.flex {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.list-item {
  padding: 10px;
  border: solid 1px #d9d9d9;
  cursor: pointer;
  text-align: center;
  border-radius: 5px;
  margin-right: 20px;
  /* width:60px */
  width: 100px;
}

.list-item.active {
  background-color: #1890ff;
  color: #fff;
}

.grid-clear {
  display: grid;
  grid-template-columns: 2fr 1fr;
  grid-gap: 10px;
}
</style>
