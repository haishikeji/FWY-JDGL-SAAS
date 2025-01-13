<template>
<a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
                <a-col :span="3">
                    <a-form-item label="">
                        <a-select v-model="queryParam.depositoryId" style="width: 100%" placeholder="盘点仓库" :allowClear="true">
                            <a-select-option v-for="(item, index) in depositoryList" :key="index" :value="item.id">{{ item.name }}</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <!-- <a-col :span="3">
                    <a-form-item label="">
                        <a-select v-model="queryParam.supplierId" style="width: 100%" placeholder="供应商" :allowClear="true">
                            <a-select-option v-for="(item, index) in supplierList" :key="index" :value="item.id">{{ item.name }}</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col> -->
                <!-- <a-col :span="3">
                    <a-form-item label="">
                        <a-select v-model="queryParam.stockType" style="width: 100%" placeholder="入库类型" :allowClear="true">
                            <a-select-option v-for="(item, index) in storageTypeList" :key="index" :value="item.value">{{ item.title }}</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col> -->
                <a-col :span="3">
                    <a-form-item label="">
                        <a-select v-model="queryParam.status" style="width: 100%" placeholder="审核状态" :allowClear="true">
                            <a-select-option :value="0">待审核</a-select-option>
                            <a-select-option :value="1">审核通过</a-select-option>
                            <a-select-option :value="2">审核未通过</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <!-- <a-col :span="3">
                    <a-form-item label="">
                        <j-input placeholder="入库内容" v-model="queryParam.stockInfo"></j-input>
                    </a-form-item>
                </a-col> -->
                <a-col :md="6" :sm="8">
                    <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                        <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                        <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                    </span>
                </a-col>
            </a-row>
        </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
        <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div> -->

        <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }" class="j-table-force-nowrap" @change="handleTableChange">
            <template slot="payFlagslot" slot-scope="text, record, index">
                {{ record.payFlag == 1 ? "是" : "否" }}
            </template>
            <template slot="iconslot" slot-scope="text, record, index">
                <a-icon :type="record.icon" theme="filled" />
            </template>

            <!-- 盘点状态 -->
            <template slot="status" slot-scope="record">
                {{record.status==0?'有差异':record.status==1?'无差异':'有差异'}}
            </template>

            <template slot="htmlSlot" slot-scope="text">
                <div v-html="text"></div>
            </template>
            <template slot="imgSlot" slot-scope="text, record">
                <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
                <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width: 80px; font-size: 12px; font-style: italic" />
            </template>
            <template slot="fileSlot" slot-scope="text">
                <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
                <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
                    下载
                </a-button>
            </template>

            <span slot="action" slot-scope="text, record">
                <a @click="handleEdit(record)"  v-if="record.status!=1">编辑</a>

                <a-divider type="vertical"  v-if="record.status!=1" />
                <a @click="examine(record)"  v-if="record.status==0" >审核</a>
                <a-divider type="vertical"  v-if="record.status==0" />
                <a @click="handleEdit(record,true)">查看</a>
                <a-divider type="vertical"  v-if="record.status!=1" />
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)"  v-if="record.status!=1" >
                    <a>删除</a>
                </a-popconfirm>
            </span>
        </a-table>
    </div>

    <!-- 审核弹窗 -->
    <a-modal title="入库审核" :visible="examineVisible" @ok="handleOk" @cancel="handleCancel">
        <a-form>
            <a-form-item label="审核状态">
                <a-radio-group name="radioGroup" v-model="examineData.status" @change="onChange">
                    <a-radio :value="1">
                        通过
                    </a-radio>
                    <a-radio :value="2">
                        未通过
                    </a-radio>
                </a-radio-group>
            </a-form-item>
            <a-form-item label="审核意见">
                <a-textarea v-model="examineData.verifyRemarks" rows="4" />
            </a-form-item>
        </a-form>
    </a-modal>

    <!-- 明细弹窗 -->
    <a-modal title="出库单明细" :visible="detailedVisible" @ok="detailedOk" @cancel="detailedCancel" :width="1200">
        <a-row :gutter="[2, 20]">
            <a-col :span="5">
                入库单号:{{ detailedData.code }}
            </a-col>
            <a-col :span="4">
                入库仓库:{{ detailedData.depositoryName }}
            </a-col>
            <a-col :span="4">
                入库类型:{{ detailedData.stockType }}
            </a-col>
            <a-col :span="4">
                申请日期:{{ detailedData.createTime }}
            </a-col>
            <!-- <a-col :span="4">
                制单人:{{}}
            </a-col> -->
        </a-row>
        <a-row :gutter="[2, 20]">
            <a-col :span="4">
                审核状态:{{ detailedData.status==1 ? "通过" : "未通过" }}
            </a-col>
            <a-col :span="4">
                审核时间:{{ detailedData.verifyTime }}
            </a-col>
            <a-col :span="4">
                审核人:{{ detailedData.verifyAt }}
            </a-col>
            <a-col :span="4">
                经办人:{{ detailedData.createAt }}
            </a-col>
            <a-col :span="4">
                供应商:{{ detailedData.supplierName }}
            </a-col>
        </a-row>
        <a-row :gutter="[2, 20]">
            <a-col :span="3">
                入库备注:{{ detailedData.remarks }}
            </a-col>
            <a-col :span="3">
                审核意见:{{ detailedData.verifyRemarks }}
            </a-col>
        </a-row>
        <a-row>
            <a-table :columns="detailedColumns" :data-source="detailedData.list" :pagination="false" :rowKey="record => record.id">
                <template slot="action" slot-scope="text, record">
                    <a @click="detailedDelete(record.id)">删除</a>
                </template>
            </a-table>
        </a-row>
    </a-modal>

    <depository-modal ref="modalForm" @ok="modalFormOk"></depository-modal>
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
import DepositoryModal from "./stockChecksModal.vue";
export default {
    name: "memberList",
    mixins: [JeecgListMixin],
    components: {
        DepositoryModal,
    },
    data() {
        return {
            activeKey: "1",
            queryParam: {},
            loading: false,
            //明细弹窗
            detailedVisible: false,
            // 明细数据
            detailedData:{},
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
            // 明细表头
            detailedColumns: [
                {
                    title: "商品名称",
                    align: "center",
                    dataIndex: "name",
                },
                {
                    title: "商品规格",
                    align: "center",
                    dataIndex: "spec",
                },
                {
                    title: "单位",
                    align: "center",
                    dataIndex: "goodUnit",
                },
                {
                    title: "成本价",
                    align: "center",
                    dataIndex: "price",
                },
                {
                    title: "出库数量",
                    align: "center",
                    dataIndex: "num",
                    scopedSlots: {
                        customRender: "num"
                    },
                },
            ],
            // 表头
            columns: [{
                    title: "盘点单号",
                    align: "center",
                    dataIndex: "code",
                },
                // {
                //     title: "申请日期",
                //     align: "center",
                //     dataIndex: "createTime",
                // },
                {
                    title: "仓库",
                    align: "center",
                    dataIndex: "depositoryName",
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "status",
                    customRender(text) {
                        var msg = "待审核";
                        if (text == 1) {
                            msg = "审核通过";
                        } else if (text == 2) {
                            msg = "审核未通过";
                        }
                        return msg;
                    },
                },
                {
                    title: "盘点状态",
                    align: "center",
                    scopedSlots: {
                        customRender: "status"
                    },
                    // dataIndex: "status",
                    
                    // customRender(text) {
                    //     var msg = "有差异";
                    //     if (text == 1) {
                    //         msg = "无差异";
                    //     } else if (text == 2) {
                    //         msg = "有差异";
                    //     }
                    //     return msg;
                    // },
                },
                {
                    title: "审批人",
                    align: "center",
                    dataIndex: "verifyAt",
                },
                {
                    title: "审批时间",
                    align: "center",
                    dataIndex: "verifyTime",
                },
                {
                    title: "备注",
                    align: "center",
                    dataIndex: "remarks",
                },
                // {
                //     title: "入库类型",
                //     align: "center",
                //     dataIndex: "stockTypeName",
                // },
                // {
                //     title: "供应商",
                //     align: "center",
                //     dataIndex: "supplierName",
                // },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    // fixed: "right",
                    // width: 147,
                    scopedSlots: {
                        customRender: "action"
                    },
                },
            ],
            url: {
                list: "/kc/kcStock/list?type=3",
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
        getAction("/kc/kcDepository/list", {
            state: 1,
        }).then((res) => {
            if (res.success) {
                this.depositoryList = res.result.records;
            }
        });
        getAction("/kc/kcSupplier/list", {
            state: 1,
        }).then((res) => {
            if (res.success) {
                this.supplierList = res.result.records;
            }
        });
        getAction("/kc/kcStock/storageTypeList", {}).then((res) => {
            if (res.success) {
                this.storageTypeList = res.result;
            }
        });
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
        detailedOk() {
            this.detailedVisible = false;
        },
        detailedCancel() {
            this.detailedVisible = false;
        },
        detailed(record) {
            console.log(record);
            getAction(`/kc/kcStockDetail/list?stockId=${record.code}`).then(res=>{
              console.log(res);
              if ( res.code==200 ) {
                this.detailedData = record;
                this.detailedData.list = res.result.records
              }
            })
            
            this.detailedVisible = true;
            // this.$refs.modalForm.show(record);
        },
    },
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
