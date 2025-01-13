<template>
<div>
    <uni-table style="margin-top:40px;" ref="table" :loading="loading" border stripe type="" emptyText="暂无更多数据" @selection-change="selectionChange">
        <uni-tr>
            <uni-th width="60" align="center">入住类型</uni-th>
            <uni-th width="60" align="center">入住间数</uni-th>
            <uni-th width="60" align="center">房费</uni-th>
            <uni-th width="60" align="center">平均房价</uni-th>
            <uni-th width="60" align="center">非房费</uni-th>
            <uni-th width="60" align="center">收入小计</uni-th>
        </uni-tr>
        <uni-tr v-for="(item, index) in tableData" :key="index">
            <uni-td align="center">
                <view class="name">{{ item.name }}</view>
            </uni-td>
            <uni-td align="center">{{ item.rz_count }}</uni-td>
            <!-- <uni-td align="center">{{ item.address }}</uni-td> -->
            <uni-td align="center">{{ item.room_money }}</uni-td>
            <uni-td align="center">{{ item.room_average_price }}</uni-td>
            <uni-td align="center">{{ item.other_money }}</uni-td>
            <uni-td align="center">{{ item.sum_money }}</uni-td>
            <!-- <uni-td align="center">{{ item.date }}</uni-td> -->
        </uni-tr>
    </uni-table>
    <div style="margin-top:20px;">
        <ucharts :chartData="chartsData" :type="'column'"></ucharts>
    </div>
    <div style="margin-top:20px;">
        <pieCharts :chartData="chartsPieDatas"></pieCharts>
    </div>
    <div style="margin-top:20px;">
        <pieCharts :chartData="chartsPieData"></pieCharts>
    </div>
</div>
</template>

<script>
import ucharts from '../echarts/line.vue'
import pieCharts from '../echarts/pie.vue'
export default {
    components: {
        ucharts,
        pieCharts
    },
    props:{
        tableData:{
            type:Array,
            default(){
                return []
            }
        }
    },
    computed: {
        chartsData() {
            let categories = [];
            let series = [];
            let obj = {
                name: '收入',
                data: []
            }
            console.log(this.tableData);
            if (!this.tableData || this.tableData.length === 0) {
                return {
                    categories,
                    series
                }
            } else {
                this.tableData.forEach(ele => {
                    categories.push(ele.name)
                    obj.data.push(ele.sum_money)
                })
                series = [obj]
                return {
                    categories,
                    series
                }
            }
        },
        chartsPieData() {
            console.log('111111111', this.tableData);
            let series = []
            this.tableData.forEach(ele => {
                series.push({
                    name: ele.name,
                    value: ele.sum_money
                })
            })
            console.log('222222222222', series);
            return series
        },
        chartsPieDatas() {
            console.log('111111111', this.tableData);
            let series = []
            this.tableData.forEach(ele => {
                series.push({
                    name: ele.name,
                    value: ele.rz_count
                })
            })
            console.log('222222222222', series);
            return series
        },
    },
    data() {
        return {
            loading:false,
        }
    }
}
</script>

<style lang="scss">
/deep/.uni-table {
    min-width: 100vw !important;
}

/deep/.uni-table-th {
    font-size: 12px !important;
}
</style>
