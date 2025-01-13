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
              label="每人发放张数"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="num"
            >
              <a-input-number
                v-model="model.num"
                placeholder="请输入每人发放张数"
                style="width: 50%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-for="(item, index) in model.mobiles" :key="index">
            <a-form-model-item
              :label="'手机号' + (index + 1)"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :prop="`mobiles[${index}]`"
              :rules="[
                {
                  required: true,
                  message: '请输入手机号!',
                  trigger: 'change',
                },
              ]"
            >
              <a-input
                v-model="item.value"
                placeholder="请输入手机号"
                style="width: 50%"
              ></a-input>
              <a-icon
                v-if="model.mobiles.length - 1 == index"
                type="plus-circle"
                class="dynamic-delete-button"
                @click="puls()"
              />
              <a-icon
                type="minus-circle"
                style="color: #f56c6c"
                class="dynamic-delete-button"
                v-if="model.mobiles.length > 1"
                @click="() => remove(index)"
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
  name: "MobileForm",
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
    couponsId: {
      type: String,
      default: "",
      required: true,
    },
    couponsType: {
      type: Number,
      default: 1,
      required: true,
    },
  },
  data() {
    return {
      model: { num: 1, mobiles: [{ value: "" }] },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {
        num: [{ required: true, message: "请输入每人发放张数!" }],
      },
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    this.model.couponsId = this.couponsId;
  },
  methods: {
    puls() {
      this.model.mobiles.push({ value: "" });
    },
    remove(index) {
      this.model.mobiles.splice(index, 1);
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true;
          var url = "/business/busMarketCouponsUsed/gainCouponsUsedToNoMember";
          if (this.couponsType === 2) {
            url = "/business/busMarketCouponsCashUsed/gainCouponsCashUsedToNoMember";
          }
          httpAction(url, this.model, "post")
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
<style scoped>
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  margin-left: 5px;
  font-size: 18px;
  color: #1890ff;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>