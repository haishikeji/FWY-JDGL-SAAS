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
              label="会员"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="memberId"
            >
              <a-select
                v-model="model.memberId"
                style="width: 100%"
                placeholder="请选择会员"
                show-search
                option-filter-prop="children"
                :filter-option="filterOption"
              >
                <a-select-option
                  v-for="(item, index) in memberList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="类型"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="type"
            >
              <a-radio-group v-model="model.type">
                <a-radio :value="1">充值</a-radio>
                <a-radio :value="2">扣费</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="model.type == 1">
            <a-form-model-item
              label="充值配置"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-select
                v-model="model.marketRechargeId"
                style="width: 100%"
                placeholder="充值配置"
                @change="marketRechargeChage"
              >
                <a-select-option
                  v-for="(item, index) in marketRechargeList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              :label="model.type == 1 ? '充值金额' : '扣费金额'"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="money"
            >
              <a-input-number
                v-model="model.money"
                placeholder="请输入充值金额"
                :min="1"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="model.type == 1">
            <a-form-model-item
              label="赠送金额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :min="0"
              prop="giveMoney"
            >
              <a-input-number
                v-model="model.giveMoney"
                placeholder="请输入赠送金额"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="备注"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="remarks"
            >
              <a-textarea
                v-model="model.remarks"
                rows="4"
                placeholder="请输入备注"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="model.type == 1">
            <a-form-model-item
              label="推荐员工"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="staffId"
            >
              <a-select
                v-model="model.staffId"
                style="width: 100%"
                placeholder="推荐员工"
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
          <a-col :span="24">
            <a-form-model-item
              label="支付方式"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="paymentMethod"
            >
              <a-select
                v-model="model.paymentMethod"
                style="width: 100%"
                placeholder="支付方式"
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
          <a-col :span="24">
            <a-form-model-item
              label="支付金额"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="payMoney"
            >
              <a-input-number
                v-model="model.payMoney"
                placeholder="请输入支付金额"
                style="width: 100%"
                :min="0"
              />
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
  name: "BusMemberBalanceLogForm",
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
      model: { type: 1 },
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
        memberId: [{ required: true, message: "请选择会员!" }],
        type: [{ required: true, message: "请输入类型!" }],
        money: [{ required: true, message: "请输入充值金额!" }],
        balance: [{ required: true, message: "请输入金额余额!" }],
        giveMoney: [{ required: true, message: "请输入赠送金额!" }],
        paymentMethod: [{ required: true, message: "请输入支付方式!" }],
        payMoney: [{ required: true, message: "请输入支付金额!" }],
        code: [{ required: true, message: "请输入流水号!" }],
      },
      url: {
        add: "/business/busMemberBalanceLog/add",
        edit: "/business/busMemberBalanceLog/edit",
        queryById: "/business/busMemberBalanceLog/queryById",
      },
      memberList: [],
      marketRechargeList: [],
      staffList: [],
      paymentMethodList: [],
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    httpAction(
      "/business/busMemberCard/list",
      { pageNo: 1, pageSize: 1000 },
      "get"
    ).then((res) => {
      if (res.success) {
        this.memberList = res.result.records;
      }
    });
    httpAction(
      "/business/busMarketRecharge/list",
      { pageNo: 1, pageSize: 1000 },
      "get"
    ).then((res) => {
      if (res.success) {
        this.marketRechargeList = res.result.records;
      }
    });
    httpAction(
      "/business/busRoomPayType/queryList",
      { pageNo: 1, pageSize: 100 },
      "get"
    ).then((res) => {
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
    marketRechargeChage(e) {
      var item = this.marketRechargeList.find((t) => t.id === e);
      this.model.money = item.money;
      this.model.giveMoney = item.returnRoney;
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
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