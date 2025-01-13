<template>
<a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
                <a-col :span="6">
                    <a-form-item label="">
                        <a-range-picker @change="timeOk">
                            <template slot="renderExtraFooter">
                                <!-- extra footer -->
                            </template>
                        </a-range-picker>
                    </a-form-item>
                </a-col>
                <a-col :span="3">
                    <a-form-item label="">
                        <a-select v-model="queryParam.departmentId" style="width: 100%" :allowClear="true">
                            <a-select-option :value="'0'">全部</a-select-option>
                            <a-select-option :value="'1'">pos</a-select-option>
                            <a-select-option :value="'2'">住客</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                    <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                        <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                        <!-- <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button> -->
                    </span>
                </a-col>
            </a-row>
        </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
    </div> -->

    <!-- table区域-begin -->
    <div>
        <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div> -->

        <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columnsCopy" :dataSource="dataSource" :pagination="ipagination" :loading="loading" class="j-table-force-nowrap" @change="handleTableChange">

            <template slot="action" slot-scope="text, record">
                {{record | sum}}
            </template>

            <template slot="footer" slot-scope="currentPageData">
                <div class="tableData">

                </div>
                <a-table size="middle" bordered :showHeader="false" :pagination="false" :columns="columnsCopy" :dataSource="dataList">
                    <template slot="action" slot-scope="text, record">
                        {{record | sum}}
                    </template>

                </a-table>
            </template>

        </a-table>
    </div>

    <!-- <depository-modal ref="modalForm" @ok="modalFormOk"></depository-modal> -->
</a-card>
</template>

<script>
import {
    JeecgListMixin
} from "@/mixins/JeecgListMixin";
import {
    filterObj
} from "@/utils/util";
import {
    getAction,
    postAction
} from "@/api/manage";
// import DepositoryModal from "./outBoundModal/outBoundModal.vue";
export default {
    name: "memberList",
    mixins: [JeecgListMixin],
    components: {
        // DepositoryModal,
    },
    computed: {
        dataList() {
            if (this.dataSource.length == 0) {
                return []
            }
            let arr = Object.keys(this.dataSource[0])
            let brr = []
            arr = arr.filter(item => item != 'department' && item != 'hotel_name' && item != 'flag')
            let sum = 0
            arr.forEach(item => {
                sum += this.dataSource[this.dataSource.length - 1][item]
            })
            arr.forEach(item => {
                console.log(item);
                // this.columnsCopy.push({
                //     title: item,
                //     align: 'center',
                //     dataIndex: item
                // })
                let a = {}
                a[item] = this.dataSource.reduce((a, b) => a + b[item], 0)
                brr.push(a)
            })
            let obj1 = {}
            obj1=Object.assign({}, ...brr, {hotel_name: '合计'},{flag:true});
            console.log(obj1);
            return [obj1]
        }
    },
    filters: {
        sum(data) {
            let arr = Object.keys(data)
            arr = arr.filter(item => item != 'department' && item != 'hotel_name' && item != 'flag')
            let sum = 0
            arr.forEach(item => {
                sum += data[item]
            })
            return sum.toFixed(2)
        }
    },
    watch: {
        dataSource(newVal) {
            if (newVal[newVal.length - 1].flag) {
                return
            }
            let obj = Object.keys(newVal[0])
            this.columnsCopy = [{
                    title: "商家",
                    align: "center",
                    dataIndex: "hotel_name",
                    width:'330px'
                },
                {
                    title: "部门",
                    align: "center",
                    dataIndex: "department",
                    width:'120px'
                }
            ]
            //去除数组中某个字符串
            obj = obj.filter(item => item != 'department' && item != 'hotel_name')
            obj.forEach(item => {
                console.log(item);
                this.columnsCopy.push({
                    title: item,
                    align: 'center',
                    dataIndex: item,
                    width:'120px'
                })
                // let a = {}
                // a[item] = this.dataSource.reduce((a, b) => a + b[item], 0)
                // this.arr.push(a)
                // arr.push({
                //     item : this.dataSource.reduce((a, b) => a + b[item], 0)
                // })
            })
            this.columnsCopy.push({
                title: '合计',
                align: 'center',
                dataIndex: 'action',
                scopedSlots: {
                    customRender: 'action'
                }
            })
            // let obj1 = {}
            // obj1=Object.assign({}, ...this.arr, {hotel_name: '合计'},{flag:true});
            // this.dataSource.push(obj1)

        }
    },
    mounted() {

    },
    data() {
        return {
            activeKey: "1",
            queryParam: {
                departmentId: '0',
            },
            arr: [],
            // dataList:[],
            examineVisible: false,
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
                    title: "商家",
                    align: "center",
                    dataIndex: "hotel_name",
                    width:'200px'
                },
                {
                    title: "部门",
                    align: "center",
                    dataIndex: "department",
                    width:'200px'
                }
            ],
            columnsCopy: [

            ],
            url: {
                list: "/finance/summary/financeSummaryPage",
                delete: "/kc/kcStock/delete",
                deleteBatch: "/kc/kcStock/deleteBatch",
                exportXlsUrl: "/kc/kcStock/exportXls",
                importExcelUrl: "/kc/kcStock/importExcel",
            },
            dictOptions: {},
            superFieldList: [],
            selectedRowKeys: [],
            isorter: {
                column: "createTime",
                order: "desc",
            },
            depositoryList: [],
            supplierList: [],
            storageTypeList: [],
            examineData: {}
        };
    },
    created() {

    },
    methods: {
        tabStockTypeChange(e) {
            this.queryParam.type = e;
            this.loadData();
        },
        examine(record) {
            console.log(record);
            this.examineData = JSON.parse(JSON.stringify(record));
            this.examineVisible = true;
        },
        handleOk() {
            postAction("/kc/kcStock/audit", this.examineData).then((res) => {
                if (res.success) {
                    this.loadData();
                    this.$message.success("审核成功");
                    this.examineVisible = false;
                }
            });
        },
        handleCancel() {
            this.examineVisible = false;
        },
        onChange(e) {
            console.log(e.target.value);
            this.examineData.status = e.target.value;
        },
        timeOk(data, dataTime) {
            // console.log(e);
            this.queryParam.startTime = dataTime[0];
            this.queryParam.endTime = dataTime[1];
            this.loadData();
        },
    },
};
</script>

<style lang="less" scoped>
@import "~@assets/less/common.less";

/deep/ .ant-table-content .ant-table-body {
    position: relative;
    z-index: 2;
    background: #FFFFFF;
}

/deep/ .ant-table-footer .ant-table-body {
    overflow: hidden !important;
    position: relative !important;
    z-index: 1 !important;
}

/deep/ .ant-table-content>.ant-table-footer {
    padding: 0 !important;
    top: -47px;
    position: relative;
    z-index: 0;
}
/deep/.ant-table-footer{
    padding: 0 !important;
    top: -8px;
    position: relative;
    z-index: 2;
}
</style>
