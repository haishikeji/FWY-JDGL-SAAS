<template>
<a-card :bordered="false">
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
        <j-input allowClear placeholder="商品名称" v-model="queryParam.goodName" style="width: 200px;margin-right:8px;"></j-input>
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
        <!--  style="margin-right:8px;" @change="onChangeTime" /> -->
        <!-- <a-popconfirm title="确定删除吗?" @confirm="delBatch"> -->
            <a-button type="danger" @click="batchDel" :disabled="delLoading" :loading="delLoading" icon="stop" style="">
                批量删除</a-button>
        <!-- </a-popconfirm> -->
    </div>

    <!-- table区域-begin -->
    <div class="grid-clear">
        <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }" class="j-table-force-nowrap" @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
                <a @click="handleEdit(record)">编辑</a>
                <a-divider type="vertical" />
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                </a-popconfirm>
            </span>
        </a-table>
    </div>

    <CounsumableModal ref="modalForm" @ok="modalFormOk" />

</a-card>
</template>

<script>
import {
    JeecgListMixin
} from "@/mixins/JeecgListMixin";
import CounsumableModal from '../modalList/ConsumableModal/ConsumableModal.vue'
import {
    getAllLayouts
} from "@/api/roomLayout";
import {
    delBatch,
    delAll
} from '@/api/roomBuildingApi'
let hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
export default {
    mixins: [JeecgListMixin],
    components: {
        CounsumableModal
    },
    data() {
        return {
            list: ['今天', '昨日', '本周', '本月', '更多'],
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
            columns: [{
                    title: "商品名称",
                    align: "center",
                    dataIndex: "goodName",
                },
                {
                    title: "类型",
                    align: "center",
                    dataIndex: "type",
                    customRender: function (text) {
                        var msg = "--";
                        switch (text) {
                            case 1:
                                msg = '客房易耗品'
                                break;
                            case 2:
                                msg = '布草'
                                break;
                            default:
                                '--'
                                break;
                        }
                        return msg;
                    },
                },
                {
                    title: "商品规格",
                    align: "center",
                    dataIndex: "goodSpe",
                },
                {
                    title: "单位",
                    align: "center",
                    dataIndex: "unit"
                },
                {
                    title: "操作",
                    align: "center",
                    dataIndex: "name",
                    scopedSlots: {
                        customRender: "action"
                    },
                }
            ],
            url: {
                list: "/fw/fwRoomConsumable/list",
                delete: "/fw/fwRoomConsumable/delete",
                deleteBatch: "/fw/fwRoomConsumable/deleteBatch",
                exportXlsUrl: "/rooms/cesRooms/exportXls",
                importExcelUrl: "rooms/cesRooms/importExcel",
            },
            buildingFloorSearchTag: {
                name: null
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
            this.loadData()
        },
        onFliterClose() {
            this.filters['buildId'] = null
            this.filters['floorId'] = null
            this.ipagination.current = 1
            this.buildingFloorSearchTag.name = null
            this.loadData()
        },
        // 搜索
        searchParam(id, name, isBuilding) {
            this.$set(this.buildingFloorSearchTag, 'name', name)
            if (isBuilding) {
                this.filters['buildId'] = id
                if (this.filters['floorId']) {
                    delete this.filters.floorId
                }
            } else {
                this.filters['floorId'] = id
                if (this.filters['buildId']) {
                    delete this.filters.buildId
                }
            }
            this.loadData()
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
            let hotelInfo = JSON.parse(localStorage.getItem('storeInfo'))
            let keys = this.selectedRowKeys
            this.delLoading = true
            delBatch({
                hotelId: hotelInfo.id,
                idStr: keys.toString()
            }).then(res => {
                if (res.code == 200) {
                    this.selectedRowKeys = []
                    this.$message.success('删除成功');
                    this.loadData()
                }
            }).finally(_ => {
                this.delLoading = false
            })
        },
        // 删除所有
        delAll() {
            let hotelInfo = JSON.parse(localStorage.getItem('storeInfo'))
            this.delLoading = true
            delAll({
                hotelId: hotelInfo.id,
            }).then(res => {
                if (res.code == 200) {
                    this.selectedRowKeys = []
                    this.$message.success('全部删除成功');
                    this.loadData()
                }
            }).finally(_ => {
                this.delLoading = false
            })
        },
        // 批量添加按钮
        onAddBatch() {
            this.$router.push('/tenant/gen/rooms');
        },
        // 全部删除
        onDelAll() {},
        // 显示图片弹窗
        handleImage(row) {
            this.$refs.roomimgmodal.setModel(row)
        },
        onSaveOk() {
            this.loadData();
        },
        handleClick(index) {
            this.activeIndex = index
            this.searchQuery()
        },
        onChange(checkedValues) {
            console.log('checked = ', checkedValues)
        },
        //选择日期
        onChangeTime(date, dateString) {
            console.log(date, dateString)
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
