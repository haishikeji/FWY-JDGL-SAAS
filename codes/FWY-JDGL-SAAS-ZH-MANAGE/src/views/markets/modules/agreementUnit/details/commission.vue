<template>
    <a-card :bordered="false">
        <a-tabs v-model="activeKey" type="card" @change="tabsClick">
            <a-tab-pane key="1" tab="未返"> </a-tab-pane>
            <a-tab-pane key="2" tab="已返"> </a-tab-pane>
        </a-tabs>

        <div v-if="activeKey === '1'">
            <unrefunded-commission-list  :agreementModel="agreementModel"></unrefunded-commission-list>
        </div>
        <div v-if="activeKey === '2'">
            <refunded-commission-list  :agreementModel="agreementModel"></refunded-commission-list>
        </div>
    </a-card>
</template>

<script>
    import UnrefundedCommissionList from './unRefundedCommissionList'
    import RefundedCommissionList from './refundedCommissionList'
    export default {
        name: 'commission',
        components: { RefundedCommissionList, UnrefundedCommissionList },
        props: {
            agreementModel:{
                type: Object,
                required: false,
                default: () => {
                }
            }
        },
        data(){
            return {
                activeKey: "1",
                refundedList: [],//已返佣金记录
                unRefundedList: [],//未返佣金记录
            }
        },
        methods:{
            tabsClick(e) {
                if (e == "1") {
                    this.loadData();
                }
            },
            loadData() {
                this.checkInBtnDis = false;
                this.scheduleBtnDis = false;
                getAction("/rooms/cesRooms/realtime-rooms", {}).then((res) => {
                    if (res.success) {
                        res.result.forEach((row) => {
                            this.$set(row, "collapse", 1);
                        });
                        this.roomList = res.result;
                        this.oldRoomList = JSON.parse(JSON.stringify(res.result));
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>
