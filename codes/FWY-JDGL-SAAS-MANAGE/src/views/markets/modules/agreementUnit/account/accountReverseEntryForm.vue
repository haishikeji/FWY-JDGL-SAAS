<template>
    <a-spin :spinning="confirmLoading">
        <j-form-container >
            <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
                <a-row>
                    <a-col :span="24">
                        <a-form-model-item label="可冲账金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="price">
                            <a-input-number disabled
                                    v-model="maxMoney"
                                    style="width: 30%"
                            />
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-model-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="price">
                            <a-input-number
                                    v-model="model.price"
                                    placeholder="请输入金额"
                                    :min="0"
                                    :max="maxMoney"
                                    style="width: 30%"
                            />
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
                            <a-textarea
                                    v-model="model.remarks"
                                    rows="4"
                                    placeholder="请输入备注"
                            />
                        </a-form-model-item>
                    </a-col>

                </a-row>
            </a-form-model>
        </j-form-container>
<!--        <a-row>-->
<!--            <a-col :span="8" :offset="3">-->
<!--                <a-button @click="handleSubmit" type="primary" >确认</a-button>-->
<!--            </a-col>-->
<!--        </a-row>-->

    </a-spin>
</template>

<script>
    import { httpAction, getAction } from "@/api/manage";

    export default {
        name: 'accountReverseEntryForm',
        props: {
            //表单禁用
            disabled: {
                type: Boolean,
                default: false,
                required: false
            },
            maxMoney: {
                type: Number,
                default: 200,
                required: false
            },
            agreementModel:{
                type: Object,
                required: false,
                default: () => {
                }
            },
        },
        data () {
            return {
                confirmLoading: false,
                model: {
                    price:0,
                },
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 3 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 18 },
                },
                paymentMethodList: [],
                url: {
                    reverseEntry: "/business/busAgreementOrderFee/reverseEntry",
                },
                validatorRules: {
                    // price: [
                    //     { required: true, message: '请输入金额!'},
                    // ],
                },
            }
        },
        created() {
        },
        methods:{
            edit() {
                // console.log(record)
                // this.model = Object.assign({}, record);
                // this.changeColumns(this.model.roomType, this.model.fixedDiscount)
                // this.visible = true;
            },
            submitForm(){
                const that = this;
                if (that.model.price <=0){
                    that.$message.warning("金额需大于0");
                    return;
                }
                // 触发表单验证
                this.$refs.form.validate(valid => {
                    if (valid) {
                        that.confirmLoading = true;
                        let httpurl = this.url.reverseEntry;
                        let method = 'post';
                        var data = {
                            agreementUnitId:that.agreementModel.id,
                            projectName:"冲账",
                            type:6,
                            payPrice:that.model.price*(-1),
                            remarks:that.model.remarks,
                        }
                        console.log(data);
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
                })
            },
        }
    }
</script>

<style scoped>

</style>
