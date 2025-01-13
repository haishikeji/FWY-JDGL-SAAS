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
              label="早餐数量"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="brkfstNum"
            >
              <a-input-number
                v-model="model.brkfstNum"
                placeholder="早餐数量"
                :min="0"
              ></a-input-number
              >份
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
        cusName: [{ required: true, message: "请输入会员姓名!" }],
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
      customerSourceList: [],
      warranterList: [],
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
    getAction("/business/busSalesPerson/list", {
      hotelId: _info.id,
      pageNo: 1,
      pageSize: 100,
    }).then((res) => {
      if (res.success) {
        this.warranterList = res.result.records;
      }
    });
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
      this.model.cusPhone = find.phone;
      this.model.cusName = find.name;
      this.model.cusId = find.id;
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
          httpAction(
            "/business/busRoomBookingOrders/update-orders?type=2",
            this.model,
            "post"
          )
            .then((res) => {
              if (res.success) {
                that.$message.success("修改成功");
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