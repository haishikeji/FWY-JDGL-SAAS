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
            <a-button @click="handleRebate" type="primary" icon="plus">返佣</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('未返佣记录')">导出</a-button>
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

                <template slot="commissionSlot" slot-scope="text,record">
                    <a-tag v-if="text == 1" color="green">是</a-tag>
                    <a-tag v-else color="red">否</a-tag>
                </template>
                <template slot="imgSlot" slot-scope="text,record">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
                    <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
                </template>
            </a-table>
        </div>

        <market-agreement-rebate-modal ref="modalForm" @ok="modalFormOk"></market-agreement-rebate-modal>
    </a-card>
</template>

<script>

    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import MarketAgreementRebateModal from './modules/marketAgreementRebateModal'

    export default {
        name: 'unrefundedCommissionList',
        mixins:[JeecgListMixin, mixinDevice],
        components: {
            MarketAgreementRebateModal
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
                description: '未返佣记录管理页面',
                // 表头
                columns: [
                    {
                        title:'房间号',
                        align:"center",
                        dataIndex: 'roomNumber'
                    },
                    {
                        title:'记佣时间',
                        align:"center",
                        dataIndex: 'commissionDate',
                        // customRender:function (text) {
                        //     return !text?"":(text.length>10?text.substr(0,10):text)
                        // }
                    },
                    {
                        title:'需返佣金',
                        align:"center",
                        dataIndex: 'refundedCommission'
                    },
                    {
                        title:'单据号',
                        align:"center",
                        dataIndex: 'livingOrderNo'
                    },
                    {
                        title:'是否每日记佣金',
                        align:"center",
                        dataIndex: 'commission',
                        scopedSlots: { customRender: "commissionSlot" },
                    },
                    {
                        title: "创建时间",
                        align: "center",
                        dataIndex: "createTime",
                        // customRender: function (text) {
                        //     return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                        // },
                    }
                ],
                url: {
                    list: "/business/busMarketAgreementUnreturnedCommission/list?agreementId="+this.agreementModel.id,
                    delete: "/business/busMarketAgreementUnreturnedCommission/delete",
                    deleteBatch: "/business/busMarketAgreementUnreturnedCommission/deleteBatch",
                    exportXlsUrl: "/business/busMarketAgreementUnreturnedCommission/exportXls",
                    importExcelUrl: "business/busMarketAgreementUnreturnedCommission/importExcel",

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
            handleRebate: function () {
                this.$refs.modalForm.edit(this.agreementModel);
                this.$refs.modalForm.title = "返佣";
                this.$refs.modalForm.disableSubmit = false;
            },
            getSuperFieldList(){
                let fieldList=[];
                fieldList.push({type:'string',value:'tenantId',text:'关联租户'})
                fieldList.push({type:'string',value:'hotelId',text:'关联酒店'})
                fieldList.push({type:'string',value:'agreementId',text:'关联协议'})
                fieldList.push({type:'string',value:'customerId',text:'关联客户协议'})
                fieldList.push({type:'string',value:'livingOrderNo',text:'单据号'})
                fieldList.push({type:'string',value:'roomId',text:'房间号'})
                fieldList.push({type:'date',value:'businessDay',text:'入住时间'})
                fieldList.push({type:'number',value:'refundedCommission',text:'需返佣金'})
                fieldList.push({type:'int',value:'commission',text:'每日记佣(0-否；1-是)'})
                fieldList.push({type:'date',value:'commissionDate',text:'记佣时间'})
                fieldList.push({type:'int',value:'rebate',text:'是否已返佣(0-否；1-是)'})
                fieldList.push({type:'int',value:'delFlag',text:'删除状态(0-正常,1-已删除)'})
                this.superFieldList = fieldList
            }
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less';
</style>
