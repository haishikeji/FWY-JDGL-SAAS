<template>
<div>
    <uni-table style="margin-top:40px;" ref="table" :loading="loading" border stripe type="selection" emptyText="暂无更多数据" @selection-change="selectionChange">
        <uni-tr>
            <uni-th width="60" align="center">房间类型</uni-th>
            <uni-th width="60" align="center">入住间数</uni-th>
            <uni-th width="60" align="center">入住率</uni-th>
            <uni-th width="60" align="center">房费收入</uni-th>
            <uni-th width="60" align="center">非房费收入</uni-th>
            <uni-th width="60" align="center">收入小计</uni-th>
            <uni-th width="60" align="center">平均房价</uni-th>
        </uni-tr>
        <uni-tr v-for="(item, index) in tableData" :key="index">
            <uni-td align="center">{{ item.name }}</uni-td>
            <uni-td align="center">
                <view class="name">{{ item.rz_count }}</view>
            </uni-td>
            <uni-td align="center">{{ ((item.rz_count/item.room_count) || 0).toFixed(2)}}</uni-td>
            <uni-td align="center">{{ item.room_money }}</uni-td>
            <uni-td align="center">{{ item.other_money }}</uni-td>
            <uni-td align="center">{{ item.sum_money }}</uni-td>
            <uni-td align="center">{{ item.room_average_price }}</uni-td>
        </uni-tr>
    </uni-table>
    <div style="margin-top:20px;">
        <ucharts :chartData="chartsData" :type="'column'"></ucharts>
    </div>
    <div style="margin-top:20px;">
        <pieCharts :chartData="chartsPieData" ></pieCharts>
    </div>
    <div style="margin-top:20px;">
        <pieCharts :chartData="chartsPieData2" ></pieCharts>
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
    props: {
        tableData: {
            type: Array,
            default () {
                return []
            }
        }
    },
    computed: {
        chartsData() {
            let categories = [];
            let series = [];
            let obj = {
                name: '房费收入',
                data: []
            };
            let obj1 = {
                name: '间数',
                data: []
            }
            if (this.tableData.length == 0) {
                return {
                    categories,
                    series
                }
            }
            this.tableData.forEach(ele => {
                categories.push(ele.name)
                obj.data.push(ele.room_money)
                obj1.data.push(ele.rz_count)
            })
            series = [obj, obj1]
            return {
                categories,
                series
            }
        },
        chartsPieData() {
            console.log('111111111', this.tableData);
            let series = []
            this.tableData.forEach(ele => {
                series.push({
                    name: ele.name,
                    value: ele.room_money
                })
            })
            console.log('222222222222', series);
            return series
        },
        chartsPieData2(){
            console.log('111111111', this.tableData);
            let series = []
            this.tableData.forEach(ele => {
                series.push({
                    name: ele.name,
                    value: ele.other_money
                })
            })
            console.log('222222222222', series);
            return series
        }
    },
    data() {
        return {
            loading: false,
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
