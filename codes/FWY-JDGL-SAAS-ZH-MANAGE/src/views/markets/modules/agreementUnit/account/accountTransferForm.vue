<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :span="4">
                        <a-form-item>
                            <a-input placeholder="客户名称" v-model="queryParam.keyWord" allowClear></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                            <a-button type="primary" @click="searchReset" icon="reload"
                                      style="margin-left: 8px">重置</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->

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
                    class="j-table-force-nowrap"
                    @change="handleTableChange">

                <span slot="action" slot-scope="text, record">
                  <a @click="handleTransfer(record)">确认转账</a>
                </span>

            </a-table>
        </div>

    </a-card>
</template>

<script>
    import { httpAction, getAction } from "@/api/manage";
    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'

    export default {
        name: 'accountTransferForm',
        mixins:[JeecgListMixin, mixinDevice],
        props: {
            //表单禁用
            disabled: {
                type: Boolean,
                default: false,
                required: false
            },
            orderIds: {
                type: String,
                default: 200,
                required: false
            }
        },
        data () {
            return {
                confirmLoading: false,
                model: {},
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 3 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 18 },
                },
                // 表头
                columns: [
                    {
                        title:'账号',
                        align:"center",
                        dataIndex: 'accountNo'
                    },
                    {
                        title:'客户名称',
                        align:"center",
                        dataIndex: 'customerName'
                    },
                    {
                        title:'剩余额度',
                        align:"center",
                        dataIndex: 'balance'
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
                    list: "/business/busMarketAgreementUnit/list",
                    transferBatch: "/business/busAgreementOrderFee/transferBatch",
                },
            }
        },
        created() {
            httpAction(
                "/business/busRoomPayType/queryList",
                { pageNo: 1, pageSize: 100 },
                "get"
            ).then((res) => {
                if (res.success) {
                    this.paymentMethodList = res.result;
                }
            });
        },
        methods:{
            edit() {
                // console.log(record)
                // this.model = Object.assign({}, record);
                // this.changeColumns(this.model.roomType, this.model.fixedDiscount)
                // this.visible = true;
            },
            handleTransfer(record){
                console.log(record);
                console.log(this.orderIds);
                const that = this;
                var data = {
                    agreementUnitId:record.id,
                    orderIds:that.orderIds
                }
                console.log(data);
                that.confirmLoading = true;
                let httpurl = this.url.transferBatch;
                let method = 'post';
                httpAction(httpurl,data,method).then((res)=>{
                    if(res.success){
                        that.$message.success(res.message);
                        that.$emit('ok');
                    }else{
                        that.$message.warning(res.message);
                    }
                }).finally(() => {
                    that.confirmLoading = false;
                })
            }
        }
    }
</script>

<style scoped>

</style>
