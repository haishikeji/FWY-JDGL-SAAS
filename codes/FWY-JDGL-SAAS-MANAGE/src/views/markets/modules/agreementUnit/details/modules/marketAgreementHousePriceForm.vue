<template>
    <div>
        <div>
            <a-spin :spinning="confirmLoading">
                <j-form-container :disabled="formDisabled">
                    <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
                        <a-row>
                            <a-col :span="24">
                                <a-form-model-item label="记佣类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roomType">
                                    <j-dict-select-tag type="radio" v-model="model.roomType" placeholder="请选择记佣类型" dictCode="commission_type"
                                    @change="e=>typeChange(e)" />
                                </a-form-model-item>
                            </a-col>
                            <a-col :span="24" v-if="model.roomType==0">
                                <a-form-model-item label="佣金单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="commissionUnit">
<!--                                    <j-dict-select-tag v-model="model.commissionUnit" placeholder="请选择佣金单位" dictCode="commission_unit" style="width: 50%" />-->
                                    <a-select v-model="model.commissionUnit.toString()" style="width: 100%">
                                        <a-select-option v-for="(item,index) in commissionUnitList" style="width: 100%"
                                                         :key="index" :value="item.value">{{ item.text || item.label }}</a-select-option>
                                    </a-select>
                                </a-form-model-item>
                            </a-col>
                            <a-col :span="24" v-if="model.roomType==0">
                                <a-form-model-item label="每日佣金" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dailyCommission">
                                    <a-input-number v-model="model.dailyCommission" placeholder="请输入每日佣金" style="width: 50%" />
                                </a-form-model-item>
                            </a-col>
                            <a-col :span="24">
                                <a-form-model-item label="每日记佣" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="commission">
                                    <j-dict-select-tag type="radio" v-model="model.commission" placeholder="请选择每日计佣" dictCode="agreement_common"/>
                                </a-form-model-item>
                            </a-col>
                            <a-col :span="24">
                                <a-form-model-item label="使用固定房价折扣" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fixedDiscount">
                                    <j-dict-select-tag type="radio" v-model="model.fixedDiscount" placeholder="请选择每日计佣" dictCode="agreement_common"
                                                       @change="e=>fixedChange(e)" />
                                </a-form-model-item>
                            </a-col>
                            <a-col :span="24" v-if="model.fixedDiscount==1">
                                <a-form-model-item label="房价固定折扣" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="discount">
                                    <a-input-number v-model="model.discount" placeholder="请输入房价固定折扣" style="width: 50%"
                                                    @blur="e=>fixedDiscountChange(e)"/><span> %</span>
                                </a-form-model-item>
                            </a-col>
                        </a-row>
                    </a-form-model>
                </j-form-container>
            </a-spin>
        </div>
        <div>
            <a-card :bordered="false">
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
                        <template slot="htmlSlot" slot-scope="text">
                            <div v-html="text"></div>
                        </template>

                        <template slot="agreementPriceSlot" slot-scope="text,record,index">
                            <a-input-number :disabled="model.fixedDiscount==1" v-model="record.agreementPrice" placeholder="协议房价" style="width: 100%"
                                            @blur="e=>agreementPriceChange(e, record, index)" />
                        </template>
                        <template slot="discountSlot" slot-scope="text,record, index">
                            <a-input-group compact>
                                <a-input-number v-model="record.discount" placeholder="房价折扣"
                                                @blur="e=>discountChange(e, record, index)"
                                                style="width: 75%" :min="0" :step="1" :precision="2"/>
                                <a-input disabled value="%" style="width: 25%;padding: 0 2px"/>
                            </a-input-group>
                        </template>
                        <template slot="monthPriceSlot" slot-scope="text,record">
                            <a-input-number v-model="record.monthPrice" placeholder="月长包房价" style="width: 100%" />
                        </template>
                        <template slot="breakfastNumberSlot" slot-scope="text,record">
                            <a-input-number v-model="record.breakfastNumber" placeholder="早餐份数" style="width: 100%" />
                        </template>
                        <template slot="unitSlot" slot-scope="text,record">
<!--                            <j-dict-select-tag v-model="record.commissionUnit" dictCode="commission_unit" />-->
                            <a-select v-model="record.commissionUnit.toString()" style="width: 100%">
                                <a-select-option v-for="(item,index) in commissionUnitList" style="width: 100%"
                                                 :key="index" :value="item.value">{{ item.text || item.label }}</a-select-option>
                            </a-select>
                        </template>
                        <template slot="dailyCommissionSlot" slot-scope="text,record">
                            <a-input-number v-model="record.dailyCommission" placeholder="日佣金" style="width: 100%" />
                        </template>
                    </a-table>
                </div>
            </a-card>
        </div>
    </div>
</template>

<script>
    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import { httpAction, getAction } from '@/api/manage'
    import {ajaxGetDictItems,getDictItemsFromCache} from '@/api/api'

    export default {
        name: 'marketAgreementHousePriceForm',
        components: {},
        mixins: [JeecgListMixin, mixinDevice],
        props: {
            //表单禁用
            disabled: {
                type: Boolean,
                default: false,
                required: false
            }
        },
        data() {
            return {
                model: {},
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 },
                },
                confirmLoading: false,
                validatorRules: {
                    roomType: [
                        { required: true, message: '请选择记佣类型!' },
                    ],
                    commissionUnit: [
                        { required: true, message: '请选择佣金单位!' },
                    ],
                    dailyCommission: [
                        { required: true, message: '请输入每日佣金!' },
                    ],
                    commission: [
                        { required: true, message: '请选择是否每日记佣!' },
                    ],
                    fixedDiscount: [
                        { required: true, message: '请选择是否使用固定房价折扣!' },
                    ],
                    discount: [
                        { required: true, message: '请输入房价固定折扣!' },
                    ],
                },
                url: {
                    add: "/business/busMarketAgreementCustomer/add",
                    editHousePrice: "/business/busMarketAgreementCustomer/editHousePrice",
                    queryById: "/business/busMarketAgreementCustomer/queryById",
                    list: "/business/busMarketAgreementCustomerHousePrice/list",
                    delete: "/business/busMarketAgreementCustomerHousePrice/delete",
                    deleteBatch: "/business/busMarketAgreementCustomerHousePrice/deleteBatch",
                    exportXlsUrl: "/business/busMarketAgreementCustomerHousePrice/exportXls",
                    importExcelUrl: "business/busMarketAgreementCustomerHousePrice/importExcel",
                },
                description: '客户协议-房价管理管理页面',
                // 表头
                columns: [],
                dictOptions: {},
                superFieldList: [],
                commissionUnitList: []
            }
        },
        computed: {
            formDisabled() {
                return this.disabled
            },
        },
        created() {
            //备份model原始值
            this.modelDefault = JSON.parse(JSON.stringify(this.model));
            this.getDictConfig();
        },
        methods: {
            getDictConfig() {
                //优先从缓存中读取字典配置
                if (getDictItemsFromCache('commission_unit')) {
                    this.commissionUnitList = getDictItemsFromCache('commission_unit');
                    return
                }
                //根据字典Code, 初始化字典数组
                ajaxGetDictItems('commission_unit', null).then((res) => {
                    if (res.success) {
                        this.commissionUnitList = res.result
                    }
                })
            },
            add() {
                this.edit(this.modelDefault);
            },
            edit(record) {
                console.log(record)
                this.model = Object.assign({}, record);
                this.changeColumns(this.model.roomType, this.model.fixedDiscount)
                this.visible = true;
            },
            submitForm() {
                const that = this;
                console.log(this.dataSource)
                // 触发表单验证
                this.$refs.form.validate(valid => {
                    if (valid) {
                        that.confirmLoading = true;
                        let httpurl = this.url.editHousePrice;
                        let method = 'put';
                        var dataSource = this.dataSource;
                        var list = [];
                        if (dataSource != null && dataSource.length > 0) {
                            for (let i = 0; i < dataSource.length; i++) {
                                //协议选择固定记佣，则所有房型的佣金单位和日佣金取协议设置数据
                                var commissionUnit = dataSource[i].commissionUnit
                                var dailyCommission = dataSource[i].dailyCommission
                                if (this.model.roomType == 0){//固定记佣
                                    commissionUnit = this.model.commissionUnit
                                    dailyCommission = this.model.dailyCommission
                                }
                                var discount = dataSource[i].discount
                                var agreementPrice = dataSource[i].agreementPrice
                                if (this.model.fixedDiscount == 1){//使用固定房价折扣
                                    discount = this.model.discount
                                    agreementPrice =  parseFloat(dataSource[i].layoutPrice*(this.model.discount/100)).toFixed(2);
                                }
                                var item = {
                                    id: dataSource[i].id,
                                    agreementPrice: agreementPrice,
                                    discount: discount,
                                    monthPrice: dataSource[i].monthPrice,
                                    breakfastNumber: dataSource[i].breakfastNumber,
                                    commissionUnit: commissionUnit,
                                    dailyCommission: dailyCommission
                                }
                                list.push(item)
                            }
                        }
                        // var list = [
                        //     {
                        //         id:1,
                        //         agreementPrice:99,
                        //         discount:10,
                        //         monthPrice:500,
                        //         breakfastNumber:2,
                        //         commissionUnit:1,
                        //         dailyCommission:2
                        //     },{
                        //         id:2,
                        //         agreementPrice:55,
                        //         discount:10,
                        //         monthPrice:500,
                        //         breakfastNumber:3,
                        //         commissionUnit:0,
                        //         dailyCommission:3
                        //     }
                        // ];
                        var param = {
                            agreementCustomer: {
                                id: this.model.id,
                                roomType: this.model.roomType,
                                commissionUnit: this.model.commissionUnit,
                                dailyCommission: this.model.dailyCommission,
                                commission: this.model.commission,
                                fixedDiscount: this.model.fixedDiscount,
                                discount: this.model.discount,
                            },
                            customerHousePriceList: list

                        }
                        console.log(param)
                        httpAction(httpurl, param, method).then((res) => {
                            if (res.success) {
                                that.$message.success(res.message);
                                that.$emit('ok');
                            } else {
                                // that.$message.warning(res.message);
                            }
                        }).finally(() => {
                            that.confirmLoading = false;
                        })
                    }

                })
            },
            agreementPriceChange(e, record, index) {
                //协议房价更改，根据额定和更改后的房价计算房价折扣
                var discount =
                    parseFloat(
                        ((record.agreementPrice / parseFloat(record.layoutPrice)) * 100).toFixed(2)
                    );
                record.discount = discount
            },
            discountChange(e, record, index) {
                var agreementPrice =
                    parseFloat(
                        (parseFloat(record.layoutPrice) * (record.discount / 100)).toFixed(2)
                    );
                record.agreementPrice = agreementPrice
            },
            changeColumns(type, fixed) {
                var columns = [];
                //房类
                if (type == 1) {
                    //固定房价折扣
                    if (fixed == 1) {
                        columns = [
                            {
                                title: '房间类型',
                                align: "center",
                                dataIndex: 'layoutName',
                                width: 1
                            },
                            {
                                title: '额定房价',
                                align: "center",
                                dataIndex: 'layoutPrice',
                                width: 1
                            },
                            {
                                title: '协议房价',
                                align: "center",
                                dataIndex: 'agreementPrice',
                                scopedSlots: { customRender: "agreementPriceSlot" },
                                width: 1
                            },
                            {
                                title: '月长包房价',
                                align: "center",
                                dataIndex: 'monthPrice',
                                scopedSlots: { customRender: "monthPriceSlot" },
                                width: 1
                            },
                            {
                                title: '早餐份数',
                                align: "center",
                                dataIndex: 'breakfastNumber',
                                scopedSlots: { customRender: "breakfastNumberSlot" },
                                width: 1
                            },
                            {
                                title: '佣金单位',
                                align: "center",
                                dataIndex: 'commissionUnit',
                                scopedSlots: { customRender: "unitSlot" },
                                width: 1
                            },
                            {
                                title: '日佣金',
                                align: "center",
                                dataIndex: 'dailyCommission',
                                scopedSlots: { customRender: "dailyCommissionSlot" },
                                width: 1
                            }
                        ]
                    } else {
                        columns = [
                            {
                                title: '房间类型',
                                align: "center",
                                dataIndex: 'layoutName',
                                width: 1
                            },
                            {
                                title: '额定房价',
                                align: "center",
                                dataIndex: 'layoutPrice',
                                width: 1
                            },
                            {
                                title: '协议房价',
                                align: "center",
                                dataIndex: 'agreementPrice',
                                scopedSlots: { customRender: "agreementPriceSlot" },
                                width: 1
                            },
                            {
                                title: '房价折扣',
                                align: "center",
                                dataIndex: 'discount',
                                scopedSlots: { customRender: "discountSlot" },
                                width: 2
                            },
                            {
                                title: '月长包房价',
                                align: "center",
                                dataIndex: 'monthPrice',
                                scopedSlots: { customRender: "monthPriceSlot" },
                                width: 1
                            },
                            {
                                title: '早餐份数',
                                align: "center",
                                dataIndex: 'breakfastNumber',
                                scopedSlots: { customRender: "breakfastNumberSlot" },
                                width: 1
                            },
                            {
                                title: '佣金单位',
                                align: "center",
                                dataIndex: 'commissionUnit',
                                scopedSlots: { customRender: "unitSlot" },
                                width: 1
                            },
                            {
                                title: '日佣金',
                                align: "center",
                                dataIndex: 'dailyCommission',
                                scopedSlots: { customRender: "dailyCommissionSlot" },
                                width: 1
                            }
                        ]
                    }
                } else {
                    //固定房价折扣
                    if (fixed == 1) {
                        columns = [
                            {
                                title: '房间类型',
                                align: "center",
                                dataIndex: 'layoutName',
                                width: 1
                            },
                            {
                                title: '额定房价',
                                align: "center",
                                dataIndex: 'layoutPrice',
                                width: 1
                            },
                            {
                                title: '协议房价',
                                align: "center",
                                dataIndex: 'agreementPrice',
                                scopedSlots: { customRender: "agreementPriceSlot" },
                                width: 1
                            },
                            {
                                title: '月长包房价',
                                align: "center",
                                dataIndex: 'monthPrice',
                                scopedSlots: { customRender: "monthPriceSlot" },
                                width: 1
                            },
                            {
                                title: '早餐份数',
                                align: "center",
                                dataIndex: 'breakfastNumber',
                                scopedSlots: { customRender: "breakfastNumberSlot" },
                                width: 1
                            }
                        ]
                    } else {
                        columns = [
                            {
                                title: '房间类型',
                                align: "center",
                                dataIndex: 'layoutName',
                                width: 1
                            },
                            {
                                title: '额定房价',
                                align: "center",
                                dataIndex: 'layoutPrice',
                                width: 1
                            },
                            {
                                title: '协议房价',
                                align: "center",
                                dataIndex: 'agreementPrice',
                                scopedSlots: { customRender: "agreementPriceSlot" },
                                width: 1
                            },
                            {
                                title: '房价折扣',
                                align: "center",
                                dataIndex: 'discount',
                                scopedSlots: { customRender: "discountSlot" },
                                width: 2
                            },
                            {
                                title: '月长包房价',
                                align: "center",
                                dataIndex: 'monthPrice',
                                scopedSlots: { customRender: "monthPriceSlot" },
                                width: 1
                            },
                            {
                                title: '早餐份数',
                                align: "center",
                                dataIndex: 'breakfastNumber',
                                scopedSlots: { customRender: "breakfastNumberSlot" },
                                width: 1
                            }
                        ]
                    }
                }
                this.columns = columns;
            },
            typeChange(e) {
                console.log(e)
                this.changeColumns(this.model.roomType, this.model.fixedDiscount)
            },
            fixedChange(e) {
                this.changeColumns(this.model.roomType, this.model.fixedDiscount)
                if (this.model.fixedDiscount == 0){
                    var dataSource = this.dataSource;
                    if (dataSource != null && dataSource.length > 0) {
                        for (let i = 0; i < dataSource.length; i++) {
                            if (!dataSource[i].agreementPrice){
                                dataSource[i].agreementPrice = 0;
                                dataSource[i].discount = 0
                            }
                        }
                        this.dataSource= dataSource
                    }
                }
            },
            fixedDiscountChange(e){
                var dataSource = this.dataSource;
                if (dataSource != null && dataSource.length > 0) {
                    for (let i = 0; i < dataSource.length; i++) {
                        var price = 0
                        if (this.model.fixedDiscount == 1) {
                            price = parseFloat(dataSource[i].layoutPrice*(this.model.discount/100)).toFixed(2);
                        }
                        console.log(price)
                        dataSource[i].agreementPrice = price
                    }
                    this.dataSource= dataSource
                }
            }
        }
    }
</script>

<style scoped>

</style>
