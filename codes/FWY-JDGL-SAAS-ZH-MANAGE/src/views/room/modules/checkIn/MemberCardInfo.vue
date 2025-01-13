<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model
        ref="form"
        :model="model"
        :rules="validatorRules"
        slot="detail"
      >
        <a-row>
          <a-col :span="12">
            <a-form-model-item
              label="姓名"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="certType"
            >
              <a-input
                v-model="model.name"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="证件号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="cardInnerNo"
            >
              <a-input
                v-model="model.cardInnerNo"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="卡号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="cardNo"
            >
              <a-input
                v-model="model.cardNo"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="手机号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="mobile"
            >
              <a-input
                v-model="model.mobile"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="会员级别"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="gradeName"
            >
              <a-input
                v-model="model.gradeName"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="余额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="balance"
            >
              <a-input
                v-model="model.balance"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              label="积分"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="integral"
            >
              <a-input
                v-model="model.integral"
              ></a-input>
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
      model: { payType: 1, livingOrderId: "", certType: 1, gender: 1 },
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
      },
      url: {
        add: "/business/busRoomBookingOrders/booking-to-live",
        edit: "/business/busMemberCard/edit",
        queryById: "/business/busMemberCard/queryById",
      },
      gradeList: [],
      paymentMethodList: [],
      staffList: [],
      customerList: [],
      oldcustomerList: [],
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    var _info = JSON.parse(localStorage.getItem("storeInfo"));
    if (_info) {
      this.model.hotelId = _info.id;
    }
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
    this.getbusCustomer();
  },
  methods: {
    handleSearch(value) {
      let result;
      if (!value) {
        result = this.oldcustomerList;
      } else {
        result = this.oldcustomerList.filter((t) => t.name.includes(value));
      }
      this.customerList = result;
    },
    handleSelectMember(e) {
      var find = this.customerList.find((t) => t.id === e);
      this.model.phone = find.phone;
      this.model.customerName = find.name;
      this.model.customerId = find.id;
    },
    getbusCustomer() {
      getAction("/bus/busCustomer/list", {}).then((res) => {
        if (res.success) {
          this.customerList = res.result.records;
          this.oldcustomerList = JSON.parse(JSON.stringify(this.customerList));
        }
      });
    },
    add(livingOrderId, roomId) {
      this.modelDefault.livingOrderId = livingOrderId;
      this.modelDefault.roomId = roomId;
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
          // var customers = [];
          // customers.push({
          //   certNo: this.model.certNo,
          //   certType: this.model.certType,
          //   customerId: this.model.customerId,
          //   customerName: this.model.customerName,
          //   gender: this.model.gender,
          //   phone: this.model.phone,
          //   roomId: this.model.roomId,
          // });
          httpAction("/business/busLivingCustomer/add", this.model, "post")
            .then((res) => {
              if (res.success) {
                that.$message.success("入住成功");
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