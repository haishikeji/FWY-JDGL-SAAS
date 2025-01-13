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
          <a-col :span="24">
            <a-form-model-item
              label="证件类型"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="certType"
            >
              <a-select
                v-model="model.certType"
                style="width: 100%"
                placeholder="证件类型"
                :allowClear="true"
              >
                <a-select-option :value="1">身份证</a-select-option>
                <a-select-option :value="2">护照</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="姓名"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="customerName"
            >
              <a-auto-complete
                v-model="model.customerName"
                placeholder="联系人"
                @search="handleSearch"
                @select="(e) => handleSelectMember(e)"
              >
                <template slot="dataSource">
                  <a-select-option v-for="item in customerList" :key="item.id"
                    >{{ item.name }}-{{ item.phone }}</a-select-option
                  >
                </template>
              </a-auto-complete>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="性别"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="gender"
            >
              <a-radio-group v-model="model.gender">
                <a-radio :value="1">男</a-radio>
                <a-radio :value="2">女</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="手机号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="phone"
            >
              <a-input
                v-model="model.phone"
                placeholder="请输入手机号"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="证件号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="certNo"
            >
              <a-input
                v-model="model.certNo"
                placeholder="请输入证件号"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="address">
              <a-input v-model="model.address" placeholder="请输入地址"  ></a-input>
            </a-form-model-item>
          </a-col> -->
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
        customerName: [{ required: true, message: "请输入会员姓名!" }],
        sex: [{ required: true, message: "请输入性别!" }],
        certificateType: [{ required: true, message: "请输入证件类型!" }],
        validity: [{ required: true, message: "请输入有效期!" }],
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
          var customers = [];
          customers.push({
            certNo: this.model.certNo,
            certType: this.model.certType,
            customerId: this.model.customerId,
            customerName: this.model.customerName,
            gender: this.model.gender,
            phone: this.model.phone,
            roomId: this.model.roomId,
          });
          httpAction(
            "/business/busRoomBookingOrders/booking-to-live?bookingRoomId=" +
              this.model.bookingRoomId,
            customers,
            "post"
          )
            .then((res) => {
              if (res.success) {
                that.$message.success('入住成功');
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