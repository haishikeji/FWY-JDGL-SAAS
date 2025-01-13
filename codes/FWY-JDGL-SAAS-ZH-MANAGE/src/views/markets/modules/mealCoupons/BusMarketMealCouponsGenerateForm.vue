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
          <a-col :span="24" v-show="false">
            <a-form-model-item
              label="免房券id"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="couponsId"
            >
            <a-input-number
                v-model="model.couponsId"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="已生成数量"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <p>{{ total }}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="生成数量"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="num"
            >
              <a-input-number
                v-model="model.num"
                placeholder="请输入张数"
                style="width: 100%"
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
  name: "BusMarketMealCouponsGenerateForm",
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
      total: 0,
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
        //  couponsId: [
        //     { required: true, message: '请输入免房券id!'},
        //  ],
        num: [{ required: true, message: "请输入生成数量!" }],
      },
      url: {
        add: "/business/busMarketMealCouponsUsed/generate",
        edit: "/business/busMarketMealCouponsUsed/edit",
        queryById: "/business/busMarketMealCouponsUsed/queryById",
      },
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add(total, couponsId) {
      this.total = total;
      this.modelDefault.couponsId = couponsId;
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