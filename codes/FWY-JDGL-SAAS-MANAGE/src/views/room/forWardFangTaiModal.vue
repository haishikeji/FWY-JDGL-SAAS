<template>
<!-- 弹窗 -->
<a-modal :title="modalTitle" :visible.sync="modalVisible" :width="1200" :footer="null" @cancel="() => (modalVisible = false)">
    <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
                <a-col :span="6">
                    <a-form-item label="">
                        <a-input placeholder="房号" v-model="search"></a-input>
                    </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                    <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                        <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                    </span>
                </a-col>
            </a-row>
        </a-form>
    </div>
    <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="tableData" class="j-table-force-nowrap">
        <template slot="orderNo" slot-scope="text, record">
          <span v-if="record.isRepair || record.isLock">--</span>
          <a v-else href="#" @click="orderDetail(text)">{{text.orderNo}}</a>
        </template>
        <template slot="status" slot-scope="text, record">
          <span v-if="record.isRepair">维修中</span>
          <span v-else-if="record.isLock">锁房中</span>
          <span v-else>{{record.isLiving==0?'预定房':'在住房'}}</span>
            <!-- {{text}} -->
        </template>
    </a-table>
    <BillRoomInfoModal ref="ModalBillRoomInfo" />
</a-modal>
</template>

<script>
import BillRoomInfoModal from '../room/modules/checkIn/BillRoomInfoModal.vue'
export default {
    name: 'forWardFangTaiModal',
    components: {
        BillRoomInfoModal
    },
    props: {
        // modalTitle: {
        //     type: String,
        //     default: '今日预离'
        // },
        // modalVisible: {
        //     type: Boolean,
        //     default: false
        // },
        dataSource: {
            type: Array,
            default: () => []
        }
    },
    data() {
        return {
            tableData: [],
            tableDataCopy: [],
            modalVisible: false,
            modalTitle: '今日预离',
            search: '',
            columns: [{
                    title: "房号",
                    align: "center",
                    dataIndex: "roomName",
                },
                {
                    title: "单号",
                    align: "center",
                    // dataIndex: "orderNo",
                    scopedSlots: {
                        customRender: "orderNo"
                    }
                },
                {
                    title: "客人姓名",
                    align: "center",
                    dataIndex: "customerName",
                    customRender: function (text, record) {
                      if (record.customerName == null) {
                        return '--'
                      }
                      return text
                    }
                },
                {
                    title: "房间类型",
                    align: "center",
                    dataIndex: "layoutName",
                },
                {
                    title: "状态",
                    align: "center",
                    // dataIndex: "isLiving",
                    scopedSlots: {
                        customRender: "status"
                    }
                },
                {
                    title: "入住时间",
                    align: "center",
                    dataIndex: "arrivalTime",
                    customRender: function (text, record) {
                      if (record.arrivalTime == null) {
                        return '--'
                      }
                      return text
                    }
                },
                {
                    title: "预离时间",
                    align: "center",
                    dataIndex: "dueOutTime",
                    customRender: function (text, record) {
                      if (record.dueOutTime == null) {
                        return '--'
                      }
                      return text
                    }
                },
            ]
        }
    },
    methods: {
        searchQuery() {
            // this.tableDataCopy = JSON.parse(JSON.stringify(this.tableData))
            this.tableData = this.tableDataCopy.filter(item => item.roomName.includes(this.search))
        },
        orderDetail(text) {
            console.log(text);
            // this.$router.push({
            //     path: '/order/orderDetail/'+text.id,
            // })
            if (text.isLiving == 0) {
                this.modalVisible = false
                this.$router.push({
                    name: "room-scheduledetail",
                    params: {
                        id: text.orderNo
                    },
                });
            }else{
                this.handleBillInfo(text)
            }
        },
        handleBillInfo(roomLive) {
            console.log("handleBillInfo", roomLive);
            // if (this.timeId) {
            //     clearTimeout(this.timeId);
            // }
            // if (!roomLive.livingData || !roomLive.livingData.livingOrder) {
            //     if (!roomLive.bookingData || !roomLive.bookingData.bookingOrder) {
            //         this.handleCheckInAdd(roomLive);
            //     } else {
            //         this.$router.push({
            //             name: "room-scheduledetail",
            //             params: {
            //                 id: roomLive.bookingData.bookingOrder.bookingOrdersNo
            //             },
            //         });
            //     }
            // } else {
            this.$refs.ModalBillRoomInfo.add(
                roomLive.bookingOrderId,
                1
            );
            this.$refs.ModalBillRoomInfo.title = "账单";
            this.$refs.ModalBillRoomInfo.disableSubmit = true;
            // }
            // roomLive.roomInfo.state = 0;
        },
    },
    mounted() {

    }
}
</script>

<style>

</style>
