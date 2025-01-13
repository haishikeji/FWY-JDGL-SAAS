<template>
    <div>
        <a-table
                ref="table"
                size="middle"
                :scroll="{x:true}"
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="false"
                :loading="loading"
                class="j-table-force-nowrap"
                @change="handleTableChange">

            <template slot="returnSlot" slot-scope="text, record">
                <span>{{(record.returnMaxNumber)}}</span>
            </template>
            <template slot="returnNumberSlot" slot-scope="text, record">
                <a-input-number :class="record.id"
                                v-model="record.returnNumber"
                                placeholder="请输入数量"
                                style="width: 50%"
                                :min="0"
                                :max="record.returnMaxNumber"
                                :disabled="record.sellingPrice == 0"
                />
            </template>
            <template slot="returnMoneySlot" slot-scope="text, record">
                <span>{{record.shopPrice*record.returnNumber}}</span>
            </template>
        </a-table>

        <j-form-container style="margin-top: 10px;" >
            <a-form-model ref="form" :model="model" slot="detail">
                <a-row>
                    <a-col :span="24">
                        <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
                            <a-textarea
                                    v-model="model.remarks"
                                    rows="4"
                                    placeholder="请输入备注"
                                    style="width: 100%"
                            />
                        </a-form-model-item>
                    </a-col>
                </a-row>
            </a-form-model>
        </j-form-container>
    </div>
</template>

<script>
    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import { httpAction, getAction } from "@/api/manage";

    export default {
        name: 'commodityReturn',
        mixins:[JeecgListMixin, mixinDevice],
        props: {
            agreementOrderFeeId:{
                type: String,
                required: false,
                default: () => {
                }
            }
        },
        data(){
            return {
                model:{
                    remarks:'',
                },
                confirmLoading: false,
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 2 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 22 },
                },
                // 表头
                columns: [
                    {
                        title:'名称',
                        align:"center",
                        dataIndex: 'goodsId_dictText',
                    },
                    {
                        title:'可退数',
                        align:"center",
                        dataIndex: 'buyNumber',
                        scopedSlots: { customRender: 'returnSlot' }
                    },
                    {
                        title:'单价',
                        align:"center",
                        dataIndex: 'shopPrice',
                        customRender: function (text) {
                            return !text?"":'¥'+text
                        },
                    },
                    {
                        title:'退货数量',
                        align:"center",
                        dataIndex: 'purchaserName',
                        scopedSlots: { customRender: 'returnNumberSlot' }
                    },
                    {
                        title:'退款',
                        align:"center",
                        dataIndex: 'amount',
                        scopedSlots: { customRender: 'returnMoneySlot' }
                    },

                ],
                url: {
                    list: "/business/busCommodityRecord/list?agreementOrderFeeId="+this.agreementOrderFeeId,
                    addReturn: "/business/busAgreementOrderFee/addReturn",
                },
            }
        },
        created() {
            console.log(this.agreementOrderFeeId)
        },
        methods:{
            show(){

            },
            submitForm(){
                var that = this;
                var goodsList = [];
                var goodsDataSource = this.dataSource;
                if (goodsDataSource && goodsDataSource.length > 0){
                    var amount = 0
                    for(let i=0;i<goodsDataSource.length;i++){
                        var item = goodsDataSource[i];
                        if (parseFloat(item.returnNumber) > 0 ){
                            var _money = parseFloat(item.returnNumber) * parseFloat(item.shopPrice);
                            amount = amount + parseFloat(_money.toFixed(2));
                            var goodsItem = {
                                id:item.id,
                                returnNumber:item.returnNumber,
                            }
                            goodsList.push(goodsItem)
                        }
                    }
                    if (amount <= 0 ){
                        that.$message.warning('退货信息未填写');
                        return;
                    }

                    //组装退货信息
                    var data = {
                        id:that.agreementOrderFeeId,
                        type:4,
                        payPrice:amount*(-1),
                        remarks:that.model.remarks,
                        commodityRecords:goodsList
                    }

                    that.confirmLoading = true;
                    let httpurl = this.url.addReturn;
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
    }
</script>

<style scoped>

</style>

