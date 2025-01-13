<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->

        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('已返佣记录')">导出</a-button>
        </div>

        <!-- table区域-begin -->
        <div>
            <a-table
                    ref="table"
                    size="middle"
                    :scroll="{x:true}"
                    bordered
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                    class="j-table-force-nowrap"
                    @change="handleTableChange">

                <template slot="htmlSlot" slot-scope="text,record">
                    <div v-html="text"></div>
                </template>
                <template slot="paySlot" slot-scope="text,record">
                    <a-button type="primary" @click="e=>handlePayDetail(e,record)">
                        支付详情
                    </a-button>
                </template>
                <template slot="commissionSlot" slot-scope="text,record">
                    <a-button type="primary" @click="e=>handleCommissionDetail(e,record)">
                        返佣详情
                    </a-button>
                </template>
            </a-table>
        </div>
    </a-card>
</template>

<script>

    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'

    export default {
        name: 'refundedCommissionList',
        mixins:[JeecgListMixin, mixinDevice],
        components: {
        },
        props: {
            agreementModel:{
                type: Object,
                required: false,
                default: () => {
                }
            }
        },
        data () {
            return {
                description: '已返佣记录管理页面',
                // 表头
                columns: [
                    {
                        title: "返佣时间",
                        align: "center",
                        dataIndex: "createTime",
                        customRender: function (text) {
                            return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                        },
                    },
                    {
                        title:'总金额',
                        align:"center",
                        dataIndex: 'sumMoney'
                    },
                    {
                        title:'已返佣金',
                        align:"center",
                        dataIndex: 'returnedCommission'
                    },
                    {
                        title:'支付详情',
                        align:"center",
                        dataIndex: 'id',
                        scopedSlots: { customRender: "priceSlot" },
                    },
                    {
                        title:'返佣详情',
                        align:"center",
                        dataIndex: 'agreementUnreturnedId',
                        scopedSlots: { customRender: "priceSlot" },
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align:"center",
                        fixed:"right",
                        width:147,
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                url: {
                    list: "/business/busMarketAgreementReturnedCommission/list",
                    delete: "/business/busMarketAgreementReturnedCommission/delete",
                    deleteBatch: "/business/busMarketAgreementReturnedCommission/deleteBatch",
                    exportXlsUrl: "/business/busMarketAgreementReturnedCommission/exportXls",
                    importExcelUrl: "business/busMarketAgreementReturnedCommission/importExcel",

                },
                dictOptions:{},
                superFieldList:[],
            }
        },
        created() {
            this.getSuperFieldList();
        },
        computed: {
            importExcelUrl: function(){
                return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
            },
        },
        methods: {
            initDictConfig(){
            },
            getSuperFieldList(){
                let fieldList=[];
                fieldList.push({type:'string',value:'tenantId',text:'关联租户'})
                fieldList.push({type:'string',value:'hotelId',text:'关联酒店'})
                fieldList.push({type:'string',value:'agreementId',text:'关联协议'})
                fieldList.push({type:'string',value:'agreementUnreturnedId',text:'关联返佣记录'})
                fieldList.push({type:'number',value:'sumMoney',text:'总金额'})
                fieldList.push({type:'number',value:'returnedCommission',text:'已返佣金'})
                fieldList.push({type:'int',value:'delFlag',text:'删除状态(0-正常,1-已删除)'})
                this.superFieldList = fieldList
            },
            handlePayDetail(e,record){
                console.log(e,record)

                // this.$refs.modalHousePriceForm.edit(record);
                // this.$refs.modalHousePriceForm.title = "协议房价列表";
                // this.$refs.modalHousePriceForm.disableSubmit = false;
            },
            handleCommissionDetail(e,record){
                console.log(e,record)

                // this.$refs.modalHousePriceForm.edit(record);
                // this.$refs.modalHousePriceForm.title = "协议房价列表";
                // this.$refs.modalHousePriceForm.disableSubmit = false;
            }
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less';
</style>
