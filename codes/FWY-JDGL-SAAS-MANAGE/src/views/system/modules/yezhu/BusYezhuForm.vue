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
              label="分成模式"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="type"
            >
              <a-select v-model="model.type">
                <a-select-option :value="1">按百分比 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="分成比例"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="commission"
            >
              <a-input-number
                v-model="model.commission"
                placeholder="请输入分成比例"
                style="width: 20%"
              />%
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
  name: "BusYezhuForm",
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
        hotelId: [{ required: true, message: "请输入关联酒店!" }],
        sysUserId: [{ required: true, message: "请输入平台登录账号id!" }],
        commission: [{ required: true, message: "请输入分成比例!" }],
      },
      url: {
        add: "/business/busYezhu/add",
        edit: "/business/busYezhu/edit",
        queryById: "/business/busYezhu/queryById",
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