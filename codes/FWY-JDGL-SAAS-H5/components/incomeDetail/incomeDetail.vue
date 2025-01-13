<template>
<view>
    <Header :borbtm="false" @change="headerChange">
        <template #search>
            <u-subsection :list="list" mode="subsection" :current="current" @change="topChange"></u-subsection>
        </template>
        <template #section>

        </template>
    </Header>
    <div v-if="current==0">
        <div class="content-detail">
            <div class="content-detail-top">
                <div>
                    <div>
                        总营业额/元
                    </div>
                    <div>
                        {{todayTotalIncome}}元
                    </div>
                </div>
                <div>
                    <div>
                        实收
                    </div>
                    <div>
                        {{todayIncome.reduce((pre, cur) => pre+cur.amount,0 ) || 0}}元
                    </div>
                </div>
                <div>
                    <div>
                        欠款（预付）/元
                    </div>
                    <div>
                        {{roomData.todayArrearsRoomData}}
                    </div>
                </div>
            </div>
            <div class="content-detail-bottom">
                <!-- <div>
                    <div>间夜数</div>
                    <div>1234</div>
                </div> -->
                <div>
                    <div>入住率</div>
                    <div>{{((roomData.checkInRoomData/roomData.allRoomData) || 0).toFixed(2)}}%</div>
                </div>
                <div>
                    <div>平均房价/元</div>
                    <div>{{roomData.housingPrices}}</div>
                </div>
                <div>
                    <div>RevPAR</div>
                    <div v-if="roomData.checkInRoomData/roomData.allRoomData!=0 && roomData.allRoomData!=0" style="font-size: 18px;">{{((roomData.housingPrices*roomData.allRoomData)/(roomData.checkInRoomData/roomData.allRoomData).toFixed(2)).toFixed(2)}}</div>
                    <div v-else style="font-size: 18px;">{{0}}</div>
                </div>
            </div>
        </div>
        <u-subsection style="margin-top:20px;" :list="listWay" mode="subsection" :current="currentWay" @change="WayChange"></u-subsection>
        <u-collapse @change="change" @close="close" @open="open" v-if="currentWay==0 && dataListTime.length>0">
            <u-collapse-item title="" name="Docs guide" v-for="(item, index) in dataListTime" :key="index">
                <b slot="title" class="u-page__item__title__slot-title">{{index+1}} {{item.roomName}} 收款合计{{item.detailList.reduce((pre, cur)=> pre+cur.money,0 )}}元</b>
                <div class="collapse-content" v-for="(sItem, index) in item.detailList" :key="index">
                    <div>
                        {{sItem.create_time}}
                    </div>
                    <div>
                        {{sItem.money}}元/ {{sItem.subject_type_name}} / {{sItem.pay_name}}
                    </div>
                    <div>
                        收款人：
                    </div>
                </div>
            </u-collapse-item>
        </u-collapse>
        <!-- <u-empty v-else mode="data" /> -->
        <u-collapse @change="change" @close="close" @open="open" v-if="currentWay==2 && dataListIncome.length>0">
            <u-collapse-item title="" name="Docs guide" v-for="(item, index) in dataListIncome" :key="index">
                <b slot="title" class="u-page__item__title__slot-title">{{index+1}} {{item.payTypeName}} 收款合计{{item.detailList.reduce((pre, cur)=> pre+cur.money,0 )}}元</b>
                <div class="collapse-content" v-for="(sItem, index) in item.detailList" :key="index">
                    <div>
                        {{sItem.create_time}}
                    </div>
                    <div>
                        {{sItem.money}}元/ {{sItem.room_name}} / {{sItem.subject_type_name}}
                    </div>
                    <div>
                        收款人：
                    </div>
                </div>
            </u-collapse-item>
        </u-collapse>
        <!-- <u-empty v-else mode="data" /> -->
        <uni-table style="margin-top:20px;" ref="table" :loading="false" border stripe type="" emptyText="暂无更多数据" @selection-change="selectionChange" v-if="currentWay==1">
            <uni-tr>
                <uni-th width="60" align="center">序号</uni-th>
                <uni-th width="60" align="center">时间</uni-th>
                <uni-th width="60" align="center">金额</uni-th>
                <uni-th width="60" align="center">房号</uni-th>
                <uni-th width="60" align="center">费项</uni-th>
                <uni-th width="60" align="center">收款方式</uni-th>
            </uni-tr>
            <uni-tr v-for="(item, index) in tableListIncome" :key="index">
                <uni-td align="center">{{ index+1 }}</uni-td>
                <uni-td>
                    <view class="name">{{ item.create_time }}</view>
                </uni-td>
                <uni-td align="center">{{ item.money }}</uni-td>
                <uni-td align="center">{{ item.room_name }}</uni-td>
                <uni-td align="center">{{ item.subject_type_name }}</uni-td>
                <uni-td align="center">{{ item.pay_name }}</uni-td>
            </uni-tr>
        </uni-table>
    </div>

    <!-- 当日支出 -->
    <div v-if="current==1">
        <div class="content-detail">
            <div class="content-detail-top">
                <div>
                    <div>
                        今日支出/元
                    </div>
                    <div>
                        65535元
                    </div>
                </div>
            </div>
            <div class="content-detail-bottom">
                <div>
                    <div>退还押金/元</div>
                    <div>1234</div>
                </div>
                <div>
                    <div>办公支出</div>
                    <div>58</div>
                </div>
                <div>
                    <div>租金支出/元</div>
                    <div>325</div>
                </div>
                <div>
                    <div>水电支出/元</div>
                    <div>216</div>
                </div>
            </div>
        </div>
        <u-subsection style="margin-top:20px;" :list="listPay" mode="subsection" :current="currentPay" @change="PayChange"></u-subsection>
        <u-collapse @change="change" @close="close" @open="open" v-if="currentPay==0">
            <u-collapse-item title="" name="Docs guide" v-for="(item, index) in dataListPayProject" :key="index">
                <b slot="title" class="u-page__item__title__slot-title">01 退还押金 支出合计400 元</b>
                <div class="collapse-content" v-for="(sItem, index) in item.detailList || []" :key="index">
                    <div>
                        2023/04/13 12:15
                    </div>
                    <div>
                        100.00元/ 房费 / 微信
                    </div>
                    <div>
                        操作员：沙暴蟑螂
                    </div>
                </div>
            </u-collapse-item>
        </u-collapse>

        <u-collapse @change="change" @close="close" @open="open" v-if="currentPay==2">
            <u-collapse-item title="" name="Docs guide" v-for="(item, index) in dataListPay" :key="index">
                <b slot="title" class="u-page__item__title__slot-title">{{index+1}} {{item.payTypeName}} 支出合计{{item.detailList.reduce((pre, cur)=> pre+cur.money,0 ) || 0}} 元</b>
                <div class="collapse-content" v-for="(sItem, index) in item.detailList" :key="index">
                    <div>
                        {{sItem.create_time}}
                    </div>
                    <div>
                        {{sItem.money}}元/ {{sItem.room_name}} / {{sItem.subject_type_name}}
                    </div>
                    <div>
                        收款人：
                    </div>
                </div>
            </u-collapse-item>
        </u-collapse>

        <uni-table style="margin-top:20px;" ref="table" :loading="false" border stripe type="" emptyText="暂无更多数据" @selection-change="selectionChange" v-if="currentPay==1">
            <uni-tr>
                <uni-th width="60" align="center">序号</uni-th>
                <uni-th width="60" align="center">时间</uni-th>
                <uni-th width="60" align="center">金额</uni-th>
                <!-- <uni-th width="60" align="center">房号</uni-th> -->
                <uni-th width="60" align="center">事项</uni-th>
                <uni-th width="60" align="center">支出方式</uni-th>
            </uni-tr>
            <uni-tr v-for="(item, index) in tableListPay" :key="index">
                <uni-td align="center">{{ index+1 }}</uni-td>
                <uni-td>
                    <view class="name">{{ item.create_time }}</view>
                </uni-td>
                <uni-td align="center">{{ item.money }}</uni-td>
                <uni-td align="center">{{ item.subject_type_name }}</uni-td>
                <uni-td align="center">{{ item.pay_name }}</uni-td>
            </uni-tr>
        </uni-table>
    </div>

    <HistoricalIncome v-if="current==2" />

</view>
</template>

<script>
import Header from '../header.vue'
import PayMethodsVue from '../tableCharts/payMethods.vue'
import RoomTypeVue from '../tableCharts/roomType.vue'
import Time from '../tableCharts/time.vue'
import FeeItemsVue from '../tableCharts/feeItems.vue'
import SourceVue from '../tableCharts/source.vue'
import CustomerTypeVue from '../tableCharts/customerType.vue'
import OccupancyTypeVue from '../tableCharts/occupancyType.vue'
import HistoricalIncome from './historicalIncome.vue'

import {
    getRoomIncomeList,
    getTimeIncomeList,
    getPayMethodIncomeList,
    getMatterExpenditureList,
    getTimeExpenditureList,
    getPayMethodExpenditureList,
} from '../../utils/incomeDetail'

import {
    getHotelList,
    getRevPAR,
    getTodayIncome,
    getTodayTotalIncome,
    getStaySource,
    getRoomStatus,
    getIncomeAndExpenditure
} from '../../utils/api.js';

export default {
    components: {
        Header,
        Time,
        PayMethodsVue,
        RoomTypeVue,
        FeeItemsVue,
        SourceVue,
        CustomerTypeVue,
        OccupancyTypeVue,
        HistoricalIncome
    },
    data() {
        return {
            list: ['当日收入', '当日支出', '历史收入', '历史支出'],
            listWay: ['按房间', '按时间', '按收款方式'],
            listPay: ['按支出事项', '按支出时间', '按付款方式'],
            payMethodsList: ['查看营业额', '查看实收'],
            current: 0,
            currentWay: 0,
            currentPayMethods: 0,
            currentPay: 0,
            rangeTime: [{
                    value: 0,
                    text: "按日"
                },
                {
                    value: 1,
                    text: "按月"
                },
                {
                    value: 2,
                    text: "按季度"
                },
                {
                    value: 3,
                    text: "按年"
                },
            ],
            rangeDay: [{
                    value: 0,
                    text: "按营业日"
                },
                {
                    value: 1,
                    text: "按自然日"
                }
            ],
            timeCheck: 0,
            dayCheck: 0,
            startTime: (new Date().toLocaleDateString()).replace(/\//g, '-'),
            endTime: (new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toLocaleDateString()).replace(/\//g, '-'),
            show: false,
            dataListTime:[],
            dataListIncome:[],
            dataListPay:[],
            dataListPayProject: [],
            tableListIncome: [],
            tableListPay: [],


            todayTotalIncome: 0, //今日总收款
            roomData: {
                //房间总数
                allRoomData: 0,
                //入住总数
                checkInRoomData: 0,
                //平均房价
                housingPrices: 0,
                // 今日预离
                todayLeaveRoomData: 0,
                //今日预抵
                todayArriveRoomData: 0,
                //今日欠费
                todayArrearsRoomData: 0,
                //脏房
                dirtyRoomData: 0,
            },
            todayIncome:[],
        }
    },
    mounted() {
        this.getData()
    },
    methods: {
        topChange(e) {
            this.current = e
            this.getData()
        },
        changeTime(e) {
            this.timeCheck = e
        },
        changeDay(e) {
            this.dayCheck = e
        },
        WayChange(e) {
            this.currentWay = e
            this.getData()
        },
        PayChange(e) {
            this.currentPay = e
            this.getData()
        },
        // 手风琴部分
        change(e) {
            console.log(e);
        },
        close(e) {
            console.log(e);
        },
        open(e) {
            console.log(e);
        },
        headerChange(e){
            console.log(e);
            this.getData()
        },

        getData() {
            getTodayTotalIncome({
                startTime: this.startTime,
                endTime: this.endTime
            }).then(res => {
                console.log(res);
                if (res.code == 200 && res.result.records.length > 0) {
                    let brr = []
                    let data = res.result.records
                    data.forEach(item => {
                        let arr = []
                        arr = Object.keys(item).filter(items => items != 'department' && items != 'hotel_name')
                        brr = Object.keys(item).filter(items => items == '现金' || items == '微信' || items == '支付宝')
                        console.log(arr);
                        arr.forEach(ele => {
                            this.todayTotalIncome += item[ele] * 1
                        })
                    })
                    let obj = {}
                    let copy = []
                    brr.forEach((item, i) => {
                        console.log(obj);
                        copy.push({
                            name: item,
                            amount: data.reduce((pre, cur) => {
                                return pre + cur[item]
                            }, 0)
                        })
                    })
                    // this.paymentMethod = copy
                    // console.log('2222222222', copy);
                    // console.log(this.todayTotalIncome);
                } else {
                    this.todayTotalIncome = 0
                }
            })
            // 房间数据统计
            getRevPAR({
                startTime: this.startTime,
                endTime: this.endTime
            }).then(res => {
                if (res.code == 200 && res.result.length > 0) {
                    this.roomData.allRoomData = res.result[0]
                    this.roomData.checkInRoomData = res.result[1]
                    this.roomData.housingPrices = res.result[2]
                    this.roomData.todayLeaveRoomData = res.result[3]
                    this.roomData.todayArriveRoomData = res.result[4]
                    this.roomData.todayArrearsRoomData = res.result[5]
                    this.roomData.dirtyRoomData = res.result[6]
                }
            })

            //今日收入统计
            getTodayIncome({
                startTime: this.startTime,
                endTime: this.endTime
            }).then(res => {
                if (res.code == 200 && res.result.length > 0) {
                    console.log(res.result);
                    this.todayIncome = res.result
                } else {
                    this.todayIncome = [{
                            name: '押金',
                            amount: 0
                        },
                        {
                            name: '房费',
                            amount: 0
                        },
                        {
                            name: '商品',
                            amount: 0
                        }
                    ]
                }
            })
            if (this.current == 0 && this.currentWay == 0) {
                getRoomIncomeList({startTime:this.startTime, endTime:this.endTime}).then(res => {
                    console.log(res);
                    if (res.code == 200) {
                        this.dataListTime = res.result
                    }
                })
            }
            if (this.current == 0 && this.currentWay == 1) {
                getTimeIncomeList({startTime:this.startTime, endTime:this.endTime}).then(res => {
                    console.log(res);
                    if (res.code == 200) {
                        this.tableListIncome = res.result
                    }
                })
            }
            if (this.current == 0 && this.currentWay == 2) {
                getPayMethodIncomeList({startTime:this.startTime, endTime:this.endTime}).then(res => {
                    console.log(res);
                    if (res.code == 200) {
                        this.dataListIncome = res.result
                    }
                })
            }
            if (this.current == 1 && this.currentPay == 0) {
                getMatterExpenditureList().then(res => {
                    console.log(res);
                })
            }
            if (this.current == 1 && this.currentPay == 1) {
                getTimeExpenditureList().then(res => {
                    console.log(res);
                    if (res.code == 200) {
                        this.tableListPay = res.result
                    }
                })
            }
            if (this.current == 1 && this.currentPay == 2) {
                getPayMethodExpenditureList().then(res => {
                    console.log(res);
                    if (res.code == 200) {
                        this.dataListPay = res.result
                    }
                })
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.pages {
    width: 100vw;
    height: 100vh;
    background-color: #f5f5f5;
}

.calendar {
    border: 1px solid;
    padding: 5px 10px;
    border-radius: 5px;
    color: #409EFF;
    font-size: 12px;
    // min-width: 30%;
    margin-left: 10px;
    display: flex;
    align-items: center;
}

.content-detail {
    background: #409EFF;
    padding: 10px;
    display: flex;
    flex-direction: column;
    box-shadow: 0 0 10px #000;
    color: #fff;
    height: 130px;
    justify-content: space-between;

    .content-detail-top {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;

        div {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            flex: 1;
        }
    }

    .content-detail-bottom {
        display: flex;
        justify-content: space-between;

        div {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            flex: 1;
            font-size: 16px;
        }
    }
}

.collapse-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 12px;
}
</style>
