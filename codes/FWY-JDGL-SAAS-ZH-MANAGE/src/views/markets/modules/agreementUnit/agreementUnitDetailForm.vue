<template>
    <div class="container">
        <div class="left">
            <a-button type="primary" style="margin-bottom: 16px" @click="toggleCollapsed">
<!--                <a-icon :type="collapsed ? 'menu-unfold' : 'menu-fold'" />-->
                {{collapsed ? '展开':'收起'}}
            </a-button>
            <a-menu
                    :default-selected-keys="['1']"
                    mode="inline"
                    :inline-collapsed="collapsed"
                    @click="handleMneuClick"
            >
                <a-menu-item key="1">
                    <a-icon type="team" />
                    <span>客户信息</span>
                </a-menu-item>
                <a-menu-item key="2">
                    <a-icon type="file-text" />
                    <span>客户协议</span>
                </a-menu-item>
                <a-menu-item key="3">
                    <a-icon type="phone" />
                    <span>联系人</span>
                </a-menu-item>
                <a-menu-item key="4">
                    <a-icon type="property-safety" />
                    <span>佣金</span>
                </a-menu-item>
                <a-menu-item key="5">
                    <a-icon type="file-done" />
                    <span>入住记录</span>
                </a-menu-item>
<!--                <a-sub-menu key="sub1">-->
<!--                    <span slot="title"><a-icon type="mail" /><span>Navigation One</span></span>-->
<!--                    <a-menu-item key="5">-->
<!--                        Option 5-->
<!--                    </a-menu-item>-->
<!--                    <a-menu-item key="6">-->
<!--                        Option 6-->
<!--                    </a-menu-item>-->
<!--                    <a-menu-item key="7">-->
<!--                        Option 7-->
<!--                    </a-menu-item>-->
<!--                    <a-menu-item key="8">-->
<!--                        Option 8-->
<!--                    </a-menu-item>-->
<!--                </a-sub-menu>-->
            </a-menu>
        </div>
<!--        <a-divider type="vertical" style="height: inherit;margin-left: 20px;" />-->
        <div class="right">
            <div class="content">
                <div v-if="activeMneu == 1">
                    <a-spin :spinning="confirmLoading">
                        <j-form-container>
                            <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
                                <a-row :gutter="[16,16]">
                                    <a-col :span="24">
                                        <a-form-model-item label="选择日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                            <div class="item-content">
                                                <div class="list-container">
                                                    <div class="list-item" v-for="(item, index) in dateList" :key="index" :class="{active: activeIndex === index}" @click="handleClick(index)">
                                                        {{ item }}
                                                    </div>
                                                </div>
                                                <div>
                                                    <a-range-picker v-show="activeIndex==3" @change="onChange" size="large"/>
                                                </div>
                                            </div>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="客户全称" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="customerName">
                                            <a-input disabled="disabled" v-model="model.customerName" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="客户简称" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="customerShortName">
                                            <a-input disabled="disabled" v-model="model.customerShortName" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="系统类型" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="systemType_dictText">
                                            <a-input disabled="disabled" v-model="model.systemType_dictText" ></a-input>
<!--                                            <a-select-->
<!--                                                    disabled="disabled"-->
<!--                                                    v-model="model.systemType"-->
<!--                                                    placeholder="请选择系统类型"-->
<!--                                                    :allowClear="true"-->
<!--                                            >-->
<!--                                                <a-select-option :value="item.id" v-for="(item,index) in systemTypeList" :key="index">{{ item.itemText }}</a-select-option>-->
<!--                                            </a-select>-->
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="客户编号" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="customerNo">
                                            <a-input disabled="disabled" v-model="model.customerNo"></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="主联系人" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="contactsName">
                                            <a-input disabled="disabled" v-model="model.contactsName"></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="客户类型" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="customerType_dictText">
                                            <a-input disabled="disabled" v-model="model.customerType_dictText" ></a-input>
<!--                                            <a-select-->
<!--                                                    v-model="model.customerType"-->
<!--                                                    placeholder="请选择客户类型"-->
<!--                                                    :allowClear="true"-->
<!--                                            >-->
<!--                                                <a-select-option :value="item.id" v-for="(item,index) in customerTypeList" :key="index">{{ item.itemText }}</a-select-option>-->
<!--                                            </a-select>-->
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="销售人员" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="sellerId_dictText">
                                            <a-input disabled="disabled" v-model="model.sellerId_dictText"></a-input>
<!--                                            <a-select-->
<!--                                                    v-model="model.sellerId"-->
<!--                                                    placeholder="请选择销售人员"-->
<!--                                                    :allowClear="true"-->
<!--                                            >-->
<!--                                                <a-select-option :value="item.id" v-for="(item,index) in saleList" :key="index">{{ item.name }}</a-select-option>-->
<!--                                            </a-select>-->
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="手机号" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="phone">
                                            <a-input disabled="disabled" v-model="model.phone" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="客户状态" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="status_dictText">
                                            <a-input disabled="disabled" v-model="model.status_dictText" ></a-input>
<!--                                            <a-select-->
<!--                                                    v-model="model.status"-->
<!--                                                    placeholder="请选择客户状态"-->
<!--                                                    :allowClear="true"-->
<!--                                            >-->
<!--                                                <a-select-option :value="item.id" v-for="(item,index) in customerStatusList" :key="index">{{ item.itemText }}</a-select-option>-->
<!--                                            </a-select>-->
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="地区" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="region">
                                            <a-input disabled="disabled" v-model="model.region" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="邮编" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="postalCode">
                                            <a-input disabled="disabled" v-model="model.postalCode" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="地址" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="address">
                                            <a-input disabled="disabled" v-model="model.address" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="公司电话" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="companyPhone">
                                            <a-input disabled="disabled" v-model="model.companyPhone" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="传真" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="fax">
                                            <a-input disabled="disabled" v-model="model.fax" placeholder="请输入传真"  ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="客户来源" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="customerSource_dictText">
                                            <a-input disabled="disabled" v-model="model.customerSource_dictText"></a-input>
<!--                                            <a-select-->
<!--                                                    v-model="model.customerSource"-->
<!--                                                    placeholder="请选择客户来源"-->
<!--                                                    :allowClear="true"-->
<!--                                            >-->
<!--                                                <a-select-option :value="item.id" v-for="(item,index) in customerSourceList" :key="index">{{ item.itemText }}</a-select-option>-->
<!--                                            </a-select>-->
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="主页" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="homepage">
                                            <a-input disabled="disabled" v-model="model.homepage" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="Email" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="email">
                                            <a-input disabled="disabled" v-model="model.email" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="客户行业" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="customerIndustry_dictText">
                                            <a-input disabled="disabled" v-model="model.customerIndustry_dictText" ></a-input>
<!--                                            <a-select-->
<!--                                                    v-model="model.customerIndustry"-->
<!--                                                    placeholder="请选择客户行业"-->
<!--                                                    :allowClear="true"-->
<!--                                            >-->
<!--                                                <a-select-option :value="item.id" v-for="(item,index) in customerIndustryList" :key="index">{{ item.itemText }}</a-select-option>-->
<!--                                            </a-select>-->
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="累计入住天数" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="email">
                                            <a-input disabled="disabled" v-model="otherModel.email" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="累计NoShow次数" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="email">
                                            <a-input disabled="disabled" v-model="otherModel.email" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="累计取消预计" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="email">
                                            <a-input disabled="disabled" v-model="otherModel.email" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="同类客户排名" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="email">
                                            <a-input disabled="disabled" v-model="otherModel.email" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="未结算佣金" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="email">
                                            <a-input disabled="disabled" v-model="otherModel.email" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="已结算佣金" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="email">
                                            <a-input disabled="disabled" v-model="otherModel.email" ></a-input>
                                        </a-form-model-item>
                                    </a-col>

                                    <a-col :span="8">
                                        <a-form-model-item label="记账限额" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="bookkeeping">
                                            <a-input-number style="width: 100%;margin: auto" v-model="model.bookkeeping" :min="0" :precision="2" placeholder="请输入记账限额" />
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item label="备注" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="remark">
                                            <a-input v-model="model.remarks" placeholder="请输入备注" ></a-input>
                                        </a-form-model-item>
                                    </a-col>
                                    <a-col :span="8">
                                        <a-form-model-item >
                                            <a-checkbox @change="onReceptionChange" v-model="model.commissionValue">
                                                是否返佣
                                            </a-checkbox>
                                            <a-checkbox @change="onReceptionChange" v-model="model.receptionValue">
                                                前台是否查询
                                            </a-checkbox>
                                            <a-checkbox @change="onReceptionChange" v-model="model.bookkStatusValue">
                                                不允许该单位记账
                                            </a-checkbox>
                                        </a-form-model-item>
                                    </a-col>
                                </a-row>
                                <div style="width:100%;display: flex;">
                                    <a-button type="primary" @click="submitOtherData" style="margin: auto;">保存</a-button>
                                </div>
                            </a-form-model>
                        </j-form-container>
                    </a-spin>
                </div>
                <div v-if="activeMneu == 2">
                    <customer-list :agreementModel="model"></customer-list>
                </div>
                <div v-if="activeMneu == 3">
                    <contacts-list :agreementModel="model"></contacts-list>
                </div>
                <div v-if="activeMneu == 4">
                    <commission :agreementModel="model"></commission>
                </div>
                <div v-if="activeMneu == 5">
                    <check-in-list :agreementModel="model"></check-in-list>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import { httpAction, getAction } from '@/api/manage'
    import '@/assets/less/TableExpand.less'
    import CustomerList from './details/customerList'
    import ContactsList from './details/contactsList'
    import CheckInList from './details/checkInList'
    import Commission from './details/commission'

    export default {
        name: 'agreementUnitDetailForm',
        components: { Commission, CheckInList, ContactsList, CustomerList },
        computed: {
            formDisabled(){
                return this.disabled
            },
        },
        data() {
            return {
                collapsed: false,
                activeMneu: 1,
                dateList: ['今天', '昨日', '本周', '更多'],
                activeIndex: 0,
                model:{
                    receptionValue:false,
                    commissionValue:false,
                    bookkStatusValue:false,
                },
                otherModel:{

                },
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 2 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 20 },
                },
                labelCol1: {
                    xs: { span: 24 },
                    sm: { span: 8 },
                },
                wrapperCol1: {
                    xs: { span: 24 },
                    sm: { span: 14 },
                },
                confirmLoading: false,
                validatorRules:{
                    customerName: [
                        { required: true, message: '请输入客户全称!'},
                    ],
                    customerShortName: [
                        { required: true, message: '请输入客户简称!'},
                    ],
                    systemType_dictText: [
                        { required: true, message: '请选择系统类型!'},
                    ],
                    customerNo: [
                        { required: true, message: '请输入客户编号!'},
                    ],
                    contactsName: [
                        { required: true, message: '请输入主联系人!'},
                    ],
                    customerType_dictText: [
                        { required: true, message: '请选择客户类型!'},
                    ],
                    sellerId_dictText: [
                        { required: true, message: '请选择销售人员!'},
                    ],
                    phone: [
                        { required: true, message: '请输入手机号!'},
                    ],
                    status_dictText: [
                        { required: true, message: '请选择状态!'},
                    ],
                    customerSource_dictText: [
                        { required: true, message: '请选择客户来源!'},
                    ],
                    customerIndustry_dictText: [
                        { required: true, message: '请选择客户行业!'},
                    ],
                },
                url: {
                    editOther: "/business/busMarketAgreementUnit/editOther",
                    queryById: "/business/busMarketAgreementUnit/queryById",
                    query_saleList: "/business/busSalesPerson/queryList",
                    listCheckInRecord: "/business/busMarketAgreementUnit/listCheckInRecord",
                },
            };
        },
        created() {
            getAction(this.url.listCheckInRecord,{}).then((res)=>{
                console.log(res)
                if(res.success){
                    //res.result
                }else{

                }
            })
        },
        methods: {
            add() {
                this.edit(this.modelDefault);
            },
            edit(record) {
                console.log(record)
                this.model = Object.assign({}, record);
                this.visible = true;

                this.model.receptionValue = record.reception == 1?true:false;
                this.model.commissionValue = record.commission == 1?true:false;
                this.model.bookkStatusValue = record.bookkStatus == 1?true:false;
            },
            toggleCollapsed() {
                this.collapsed = !this.collapsed;
            },
            handleMneuClick(e) {
                console.log('click', e);
                console.log('click', e.key);
                this.activeMneu = e.key;
            },
            handleClick(index) {
                this.activeIndex = index
                // this.searchQuery()
                console.log(index)
                switch (index) {
                    case 0:
                        console.log('今天')
                        break;
                    case 1:
                        console.log('昨天')
                        break;
                    case 2:
                        console.log('本周')
                        break;
                }
            },
            onChange(date, dateString) {
                console.log(date, dateString)
            },
            onReceptionChange(e){
                // console.log(e)
                //
                // console.log(this.model)
                // var newModel = this.model
                // newModel.receptionValue = e.target.checked
                // newModel.reception = 1
                // this.$nextTick(() => {
                //     this.$set(this.model, 'receptionValue', e.target.checked)
                //     this.$set(this.model, 'reception', e.target.checked?1:0)
                // })
                this.$forceUpdate();
            },
            submitOtherData(){
                var that = this;
                that.confirmLoading = true;
                let httpurl = this.url.editOther;
                let method = 'put';
                var param = {
                    id:that.model.id,
                    bookkeeping:that.model.bookkeeping,
                    remarks:that.model.remarks,
                    reception:that.model.receptionValue?1:0,
                    commission:that.model.commissionValue?1:0,
                    bookkStatus:that.model.bookkStatusValue?1:0,
                };
                console.log(param);
                httpAction(httpurl,param,method).then((res)=>{
                    if(res.success){
                        that.$message.success(res.message);
                        that.$emit('ok');
                    }else{
                        that.$message.warning(res.message);
                    }
                }).finally(() => {
                    that.confirmLoading = false;
                })
            },
        },
    }
</script>

<style scoped>
    .container{
        display: flex;
        flex-direction: row;
    }
    .right{
        flex: 3;
    }
    .content{
        width: 100%;
        margin: auto;
        margin-top: 60px;
    }
    .item-content{
        display: flex;
        flex-direction: row;
        /*align-items: center;*/
        /*justify-items: center;*/
    }
    .list-container {
        display: grid;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        border: solid 1px #d9d9d9;
        width: 260px;
        border-radius: 5px;
        margin-right: 10px;
        overflow: hidden;
        /*height: 32px;*/
        /*line-height: 32px;*/
    }

    .list-item {
        padding: 0px;
        border-right: solid 1px #d9d9d9;
        cursor: pointer;
        text-align: center;
    }

    .list-item.active {
        background-color: #1890ff;
        color: #fff;
    }
</style>
