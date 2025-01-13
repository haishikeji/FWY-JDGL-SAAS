<template>
<div>
    <template>
        <div class="card-day">
            <div>
                <div style="font-size: 12px;color:#409EFF; display:flex;justify-content:space-between;align-items:center;">
                    <div style="font-size: 16px;font-weight: bold;margin-right:10px">预定概览</div>
                    <!-- 共 <span>10</span> 间 -->
                    <div @click="show = true" class="calendar">{{`${startTime} ~ ${endTime}`}}</div>
                </div>
                <div class="card-day-top">
                    <div style="font-size: 12px;color:#409EFF;">
                        <!-- <span style="font-size: 16px;font-weight: bold;margin-right:10px">今日预抵概览</span> -->
                        共 <span>{{dataList.length}}</span> 间
                    </div>
                    <div style="color:#409EFF;font-size:14px;width:40%;">
                        <u-subsection :list="list1" mode="subsection" :current="current1" @change="sectionChangeDay"></u-subsection>
                    </div>
                </div>
            </div>
            <div class="card-day-btm" v-show="current1==0">
                <div v-for="(item, index) in cesRoomLayoutList" :key="index">
                    <div>
                        {{item.name}} {{ filterRoomType(item.id)}}间
                    </div>
                    <!-- <div>
                        高级商务标间 {{ 10 }}间
                    </div> -->
                </div>
                <!-- <div>
                    <div>
                        麻将房 {{25}}间
                    </div>
                    <div>
                        零压高级大床房 {{25}} 间
                    </div>
                </div>
                <div>
                    <div>
                        商务单人房 {{25}}间
                    </div>
                    <div>
                        特惠大床房 {{25}} 间
                    </div>
                </div> -->
            </div>
            <div class="card-day-btm" v-show="current1==1">
                <div v-for="(item, index) in customerSourceList" :key="index">
                    <div>
                        {{item.itemText}} {{ filterCustomerSource(item.id)}}间
                    </div>
                </div>
                <!-- <div>
                    <div>
                        携程 {{25}}间
                    </div>
                    <div>
                        去哪儿 {{25}} 间
                    </div>
                </div>
                <div>
                    <div>
                        艺龙 {{25}}间
                    </div>
                    <div>
                        微信 {{25}} 间
                    </div>
                </div> -->
            </div>
        </div>

        <div class="day-detail">
            <div class="day-detail-title">
                <div style="color: #409EFF;">
                    预定详情
                </div>
                <uni-data-select :placeholder="current1==0? '请选择房型' : '请选择订单来源'" :clear="false" style="width:40%;flex:none;" :localdata="current1==0? range : range2" @change="change"></uni-data-select>
            </div>
            <div>
                <!-- <div style="color:rgb(255, 141, 26);text-align:right;border-bottom:1px solid #00000030;line-height:30px">A栋6层：12间</div> -->
                <div v-for="(sItem, index) in dataList" :key="sItem.id" :class="index==curIdx?'list-active':''" :style="{height:'90px',borderBottom:index!=5?'1px solid #00000030':'',padding:'10px 0 0 0'}" @click="changeList(index)">
                    <div>
                        <span v-if="sItem.bookingData &&sItem.bookingData.bookingOrder && sItem.bookingData.bookingCustomer" style="font-size: 16px; font-weight: bold;">{{index+1}} {{sItem.bookingData.bookingCustomer.name + ' / '+  (sItem.livingData.livingCustomers? sItem.livingData.livingCustomers.gender == 1 ? "男 / ": "女 / " : "") + sItem.bookingData.bookingCustomer.phone }}</span>
                        <b v-else>{{index+1}}</b>
                    </div>
                    <div class="day-detail-list">
                        <div>房型：{{sItem.layout.name}}/1间</div>
                        <!-- <div>一天</div> -->
                        <div>预住时长：{{sItem.bookingData.bookingOrder.dayCount}}天</div>
                    </div>
                    <div class="day-detail-list">
                        <div>预抵：{{sItem.bookingData.bookingOrder.arrivalTime}}</div>
                        <div>保留：</div>
                        <div>已付：0元</div>
                    </div>
                </div>
            </div>
        </div>
    </template>
    <u-calendar closeOnClickOverlay :show="show" mode="range" @close="close" @confirm="confirm"></u-calendar>

</div>
</template>

<script>
import {
    getHotelRoomList,
    getRoomStatus,
    getCustomerSource,
    getRoomType
} from '../../utils/customerOrder'
import {
    mapState
} from 'vuex'
export default {
    props: {
        keyWord: {
            type: String,
            default: ''
        }
    },
    watch: {
        keyWord: {
            handler(val) {
                this.filter()
            }
        },
    },
    data() {
        return {
            list1: ['按房间类型', '按订单来源'],
            current1: 0,
            filterId: null,
            value: 0,
            range: [],
            range2: [],
            show: false,
            startTime: (new Date().toLocaleDateString()).replace(/\//g, '-'),
            endTime: (new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toLocaleDateString()).replace(/\//g, '-'),
            curIdx: -1,
            dataList: [],
            dataListCopy: [],
            /**
             * 用户来源列表
             */
            customerSourceList: [],
            /**
             * 方块背景颜色列表
             */
            roomStatusColorList: [],
            /**
             * 房型列表
             */
            cesRoomLayoutList: [],
        }
    },
    computed: {
        ...mapState(['hotelId'])
    },
    mounted() {
        this.getData()
    },
    filters: {

    },
    methods: {
        sectionChangeDay(e) {
            console.log(e)
            this.current1 = e
        },
        change(e) {
            console.log(e)
            this.filterId = e
            this.filter()
        },
        confirm(e) {
            console.log(e)
            this.startTime = e[0]
            this.endTime = e[e.length - 1]
            this.show = false
        },
        close() {
            this.show = false
        },
        changeList(index) {
            this.curIdx = index
            this.$emit('changeList', index)
        },
        headerChange(e) {
            this.getData(e)
        },
        getData(id = this.hotelId) {
            getHotelRoomList({
                hotelId: id
            }).then(res => {
                console.log(res);
                if (res.code == 200 && res.result.length > 0) {
                    this.dataListCopy = res.result
                    // this.dataList = res.result
                    this.filter()
                } else {
                    this.dataListCopy = []
                    this.dataList = []
                }
            })
            getRoomStatus().then(res => {
                console.log(res);
                if (res.code == 200 && res.result.length > 0) {
                    let arr = []
                    res.result.forEach(ele => {
                        arr.push(ele.text)
                    })
                    this.roomStatusColorList = res.result
                    this.list3 = arr
                    this.list3.unshift('全部')
                } else {
                    this.roomStatusColorList = []
                    this.list3 = []
                }
            })
            getCustomerSource().then(res => {
                console.log(res);
                if (res.code === 200 && res.result.records.length > 0) {
                    this.customerSourceList = res.result.records
                    console.log(this.current2);
                    this.range2 = res.result.records.map(item => {
                        return {
                            value: item.id,
                            text: item.itemText
                        }
                    })
                    this.range2.unshift({
                        value: null,
                        text: '全部'
                    })
                } else {
                    this.customerSourceList = []
                    this.range2 = []
                }
            })
            getRoomType().then(res => {
                console.log(res);
                if (res.code === 200 && res.result.records.length > 0) {
                    this.cesRoomLayoutList = res.result.records
                    this.current2 = res.result.records[0].id
                    this.range = res.result.records.map(item => {
                        return {
                            value: item.id,
                            text: item.name
                        }
                    })
                    this.range.unshift({
                        value: null,
                        text: '全部'
                    })
                } else {
                    this.cesRoomLayoutList = []
                    this.range = []
                }
            })
        },
        /**
         * @description: 筛选
         * @param
         * @return
         */
        filter() {
            console.log('this.current1', this.current1);
            console.log('this.current3', this.current3);
            let arr = JSON.parse(JSON.stringify(this.dataListCopy))
            console.log(arr);
            arr.forEach(item => {
                item.rooms = item.rooms.filter(ele => {
                    return ele.bookingData && ele.bookingData.bookingOrder
                })
            })
            if (this.current1 == 0 && this.filterId) {
                arr.forEach(item => {
                    console.log(item);
                    item.rooms = item.rooms.filter(ele => {
                        return ele.layout.id == this.filterId
                    })
                })
            }
            if (this.current1 == 1 && this.filterId) {
                arr.forEach(item => {
                    console.log(item);
                    item.rooms = item.rooms.filter(ele => {
                        return ele.livingData && ele.livingData.livingOrder ? ele.livingData.livingOrder.customerSource == this.filterId : ele.bookingData && ele.bookingData.bookingOrder ? ele.bookingData.bookingOrder.customerSource == this.filterId : false
                    })
                })
            }
            if (this.keyWord) {
                arr.forEach(item => {
                    item.rooms = item.rooms.filter(ele => {
                        return ele.roomInfo.name == this.keyWord ||
                            (ele.livingData && ele.livingData.livingCustomers ?
                                ele.livingData.livingCustomers.customerName == this.keyWord :
                                ele.bookingData && ele.bookingData.bookingCustomer ?
                                ele.bookingData.bookingCustomer.name == this.keyWord :
                                false) ||
                            (ele.livingData && ele.livingData.livingCustomers ?
                                ele.livingData.livingCustomers.phone == this.keyWord :
                                ele.bookingData && ele.bookingData.bookingCustomer ?
                                ele.bookingData.bookingCustomer.phone == this.keyWord :
                                false)
                    })
                })
            }
            let brr = []
            arr.forEach(item => {
                if (item.rooms.length > 0) {
                    brr.push(...item.rooms)
                }
            })
            console.log('arrarrarrarr', arr);
            this.dataList = JSON.parse(JSON.stringify(brr))
            // this.dataListCopy = JSON.parse(JSON.stringify(brr))
            console.log(brr);
            // return arr
        },
        /**
         * 根据id计算每种房型的数量
         */
        filterRoomType(id) {
            let arr = JSON.parse(JSON.stringify(this.dataList))
            arr = arr.filter(item => {
                return item.layout.id == id
            })
            return arr.length
        },
        /**
         * 根据id计算每种来源的数量
         */
        filterCustomerSource(id) {
            let arr = JSON.parse(JSON.stringify(this.dataList))
            arr = arr.filter(item => {
                return item.livingData && item.livingData.livingOrder ? item.livingData.livingOrder.customerSource == id : item.bookingData && item.bookingData.bookingOrder ? item.bookingData.bookingOrder.customerSource == id : false
            })
            return arr.length
        },
        getCustomerSourceList(customerSource, first) {
            console.log(customerSource);
            var find = this.customerSourceList.find((t) => t.id == customerSource);
            console.log(find);
            console.log(this.customerSourceList);
            if (find) {
                // return first ? find.label.substr(0, 1) : find.label;
                return find.itemText.substr(0, 1);
            }
            return "";
        },
        /**
         * 计算方块的背景颜色
         */
        getRoomStatusColor(roomStatus) {
            var find = this.roomStatusColorList.find((t) => t.value == roomStatus);
            return find ? find.extend : "";
        },
        /**
         * 计算房态出现的数量
         */
        roomStateFilter(roomStatus) {
            let count = 0
            let arr = JSON.parse(JSON.stringify(this.dataListCopy))
            arr.forEach(ele => {
                ele.rooms.forEach(item => {
                    if (item.roomInfo.roomStatus == roomStatus) {
                        count++
                    }
                })
            })
            return count
        },
    }
}
</script>

<style lang="scss" scoped>
.list-active {
    background: rgb(228, 240, 253);
}

.card-day {
    padding: 10px;
    background: #fff;
    margin-top: 8px;
    border-bottom: #00000030 1px solid;
}

.card-day-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: #00000030 1px solid;
    height: 35px;
}

.card-day-btm {
    margin-top: 10px;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    justify-content: space-between;
    gap: 10px;

    // div为偶数时右对齐
    div:nth-child(even) {
        text-align: right;
    }
}

.day-detail {
    background: #fff;
    padding: 10px;
    margin-top: 8px;

    .day-detail-title {
        font-weight: bold;
        font-size: 16px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
}

.day-detail-list {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 10px 15px;
    // height: 30px;
    font-size: 12px;
}

.calendar {
    border: 1px solid;
    padding: 5px 10px;
    border-radius: 5px;
}
</style>
