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
              label="名称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="name"
            >
              <a-input v-model="model.name" placeholder="请输入名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="餐券类型"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="type"
            >
            <a-select
                v-model="model.type"
                style="width: 100%"
                placeholder="请选择餐券类型"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in mealCouponTypeList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="金额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="cost"
            >
              <a-input-number
                v-model="model.cost"
                placeholder="请输入金额"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="说明"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="introduce"
            >
              <a-input
                v-model="model.introduce"
                placeholder="请输入说明"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="消费项目"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="consumeProjectId"
            >
              <a-select
                v-model="model.consumeProjectId"
                style="width: 100%"
                placeholder="请选择消费项目"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in stockTypeList"
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
  name: "BusMarketMealCouponsForm",
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
        name: [{ required: true, message: "请输入名称!" }],
        type: [{ required: true, message: "请输入餐券类型!" }],
        cost: [{ required: true, message: "请输入金额!" }],
        introduce: [{ required: true, message: "请输入说明!" }],
        consumeProjectId: [{ required: true, message: "请输入消费项目!" }],
      },
      url: {
        add: "/business/busMarketMealCoupons/add",
        edit: "/business/busMarketMealCoupons/edit",
        queryById: "/business/busMarketMealCoupons/queryById",
      },
      stockTypeList: [],
      mealCouponTypeList:[]
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    const hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
    if (hotelInfo) {
      this.model.hotelId = hotelInfo.id;
    }
    httpAction(
      "/rooms/cesStockType/getTopTypes",
      { hotelId: hotelInfo.id },
      "get"
    ).then((res) => {
      if (res.success) {
        this.stockTypeList = res.result;
      }
    });
    // httpAction(
    //   "/rooms/cesMealCoupon/fetch",
    //   { hotelId: hotelInfo.id },
    //   "get"
    // ).then((res) => {
    //   if (res.success) {
    //     this.mealCouponTypeList = res.result;
    //   }
    // });
    httpAction(
            "/business/busMealCoupon/queryList",
            { hotelId: hotelInfo.id },
            "get"
    ).then((res) => {
      if (res.success) {
        this.mealCouponTypeList = res.result;
      }
    });
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
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
