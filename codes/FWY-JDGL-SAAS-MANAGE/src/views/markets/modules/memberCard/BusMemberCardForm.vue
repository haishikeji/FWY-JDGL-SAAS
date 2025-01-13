<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <div class="title_style">基本信息</div>
          <a-col :span="24" class="string_style">
            <a-form-model-item label="会员姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input style="width: 180px;" v-model="model.name" placeholder="请输入会员姓名"  ></a-input>
              <a-button @click="readCardNo" style="margin-left: 10px;">读取身份证</a-button>
              <a-radio-group v-model="model.sex">
                <a-radio :value="1">男</a-radio>
                <a-radio :value="2">女</a-radio>
              </a-radio-group>
            </a-form-model-item>
            
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="avatar">
              <a-input v-model="model.avatar" placeholder="请输入头像"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24" class="string_style">
            <a-form-model-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mobile">
              <a-input v-model="model.mobile" placeholder="请输入手机号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="证件类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="certificateType">
              <a-select
                v-model="model.certificateType"
                style="width: 100%"
                placeholder="证件类型"
                :allowClear="true"
              >
                <a-select-option :value="1" >身份证</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="证件号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="certificateNo">
              <a-input v-model="model.certificateNo" placeholder="请输入证件号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" class="string_style">
            <a-form-model-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="address">
              <a-input v-model="model.address" placeholder="请输入地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <div class="title_style">会员详情</div>
          <a-col :span="12">
            <a-form-model-item label="会员卡号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cardNo">
              <a-input v-model="model.cardNo" placeholder="请输入会员卡号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="卡内码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cardInnerNo">
              <a-input v-model="model.cardInnerNo" placeholder="请输入卡内码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="等级类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gradeId">
              <a-select
                v-model:value="model.gradeId"
                style="width: 100%"
                placeholder="会员等级"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in gradeList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="会员费" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="memberFee">
              <a-input-number v-model="model.memberFee" placeholder="请输入会员费" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="付款类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payType">
              <a-radio-group v-model="model.payType">
                <a-radio :value="1">现付</a-radio>
                <a-radio :value="2" disabled>转房帐</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-if="model.payType==1">
            <a-form-model-item label="付款方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="paymentMethod">
              <a-select
                v-model="model.paymentMethod"
                style="width: 100%"
                placeholder="付款方式"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in paymentMethodList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          
          
          <!-- <a-col :span="12">
            <a-form-model-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sex">
              
            </a-form-model-item>
          </a-col> -->
          
          
          <a-col :span="12">
            <a-form-model-item label="消费密码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="consumePassword">
              <a-input-password  v-model="model.consumePassword" placeholder="请输入消费密码"></a-input-password>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="有效期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="validity">
              <a-radio-group v-model="model.validity">
                <a-radio :value="1">无限制</a-radio>
                <a-radio :value="2">有限制</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-if="model.validity==2">
            <a-form-model-item label="有效期截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="validityEndTime" :rules="[
                                        {
                                            required: true,
                                            message: '请输入有效期截止日期',
                                            trigger: 'change',
                                        },
                                    ]">
              <j-date placeholder="请选择有效期截止日期" v-model="model.validityEndTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" rows="4" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="推荐员工" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="staffId">
              <a-select
                v-model="model.staffId"
                style="width: 100%"
                placeholder="推荐员工"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in staffList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";

export default {
  name: "BusMemberCardForm",
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      model: { payType: 1 },
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
        mobile: [
          {
            required: true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入手机号!",
          },
        ],
        cardNo: [{ required: true, message: "请输入会员卡号!" }],
        gradeId: [{ required: true, message: "请输入等级类型!" }],
        payType: [{ required: true, message: "请输入付款类型!" }],
        paymentMethod: [{ required: true, message: "请输入付款方式!" }],
        name: [{ required: true, message: "请输入会员姓名!" }],
        sex: [{ required: true, message: "请输入性别!" }],
        certificateType: [{ required: true, message: "请输入证件类型!" }],
        validity: [{ required: true, message: "请输入有效期!" }],
      },
      url: {
        add: "/business/busMemberCard/add",
        edit: "/business/busMemberCard/edit",
        queryById: "/business/busMemberCard/queryById",
      },
      gradeList: [],
      paymentMethodList: [],
      staffList: [],
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    getAction("/org.jeecg.modules.business/busMarketMember/list", {
      pageNo: 1,
      pageSize: 100,
    }).then((res) => {
      if (res.success) {
        this.gradeList = res.result.records;
      }
    });
    getAction("/business/busRoomPayType/queryList", {
      pageNo: 1,
      pageSize: 100,
    }).then((res) => {
      if (res.success) {
        this.paymentMethodList = res.result;
      }
    });
    var _info = JSON.parse(localStorage.getItem("storeInfo"));
    if (_info) {
      this.model.hotelId = _info.id;
    }
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    readCardNo() {
      this.$message.error("接口程序未打开，请打开接口程序");
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
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
            });
        }
      });
    },
  },
};
</script>
<style scoped lang="less">
@media (min-width: 576px){
  ::v-deep .string_style {
    .ant-col-sm-5 {
      width: 10.5% !important;
    }
    .ant-col-sm-16 {
      width: 83.2%;
    }
  }
}
.title_style {
  color: #ff8d1a;
  border-left: 2px solid #ff8d1a;
  display: flex;
  padding-left: 8px;
  margin-bottom: 12px;
}
.ant-radio-group {
  margin-left: 10px;
}
</style>