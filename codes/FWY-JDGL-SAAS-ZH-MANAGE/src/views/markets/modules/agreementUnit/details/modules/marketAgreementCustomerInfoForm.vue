<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="账号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="accountNo">
              <a-input disabled="disabled" v-model="agreementModel.accountNo" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="客户简称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customerShortName">
              <a-input disabled="disabled" v-model="agreementModel.customerShortName" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="协议主题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入协议主题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="协议类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
<!--              <a-input v-model="model.type" placeholder="请输入协议类型"  ></a-input>-->
              <a-select
                      v-model="model.type"
                      placeholder="请选择协议类型"
                      :allowClear="true"
              >
                <a-select-option :value="item.id" v-for="(item,index) in typeList" :key="index">{{ item.itemText }}</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="协议单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="number">
              <a-input v-model="model.number" placeholder="请输入协议单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签约时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signTime">
              <j-date placeholder="请选择签约时间" v-model="model.signTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="有效期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="effective">
              <j-date placeholder="请选择有效期" v-model="model.effective"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="佣金信息保密" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="secrecy">
<!--              <a-input-number v-model="model.secrecy" placeholder="请输入佣金信息保密" style="width: 100%" />-->
              <a-radio-group name="radioGroup" v-model="model.secrecy">
                <a-radio :value="1">
                  是
                </a-radio>
                <a-radio :value="0">
                  否
                </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="客户签约人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customerSignatory">
              <a-input v-model="model.customerSignatory" placeholder="请输入客户签约人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="本司签约人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companySignatory">
              <a-input v-model="model.companySignatory" placeholder="请输入本司签约人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-textarea v-model="model.content" rows="4" placeholder="请输入内容" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  const hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));

  export default {
    name: 'marketAgreementCustomerInfoForm',
    components: {},
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      },
      agreementModel:{
        type: Object,
        required: false,
        default: () => {
        }
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
          name: [
            { required: true, message: '请输入协议主题!' },
          ],
          type: [
            { required: true, message: '请选择协议类型!' },
          ],
          number: [
            { required: true, message: '请输入协议单号!' },
          ],
          signTime: [
            { required: true, message: '请选择签约时间!' },
          ],
          effective: [
            { required: true, message: '请选择有效期!' },
          ],
          secrecy: [
            { required: true, message: '请选择佣金信息是否保密!' },
          ],
          customerSignatory: [
            { required: true, message: '请输入客户签约人!' },
          ],
          companySignatory: [
            { required: true, message: '请输入本司签约人!' },
          ],
        },
        url: {
          add: "/business/busMarketAgreementCustomer/add",
          edit: "/business/busMarketAgreementCustomer/edit",
          queryById: "/business/busMarketAgreementCustomer/queryById"
        },
        typeList:[]
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
      console.log(this.agreementModel)
      this.getDictConfig();
    },
    methods: {
      getDictConfig(){
        var that = this;
        var param = {
          dictName:'协议类型设置'
        }
        that.confirmLoading = true;
        getAction('/business/busDictItem/queryList',param).then((res)=>{
          console.log(res)
          if(res.success){
            if (res.code == 200 && res.result) {
              this.typeList = res.result;
            }
          }
        }).finally(() => {
          that.confirmLoading = false;
        })
      },
      add() {
        this.edit(this.modelDefault);
      },
      edit(record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm() {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
              this.model.hotelId = hotelInfo.id;
              this.model.agreementId = this.agreementModel.id;
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            httpAction(httpurl, this.model, method).then((res) => {
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
    }
  }
</script>
