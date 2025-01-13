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
              label="税率"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="taxRate"
            >
              <a-input-number
                v-model="model.taxRate"
                placeholder="请输入税率"
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
        taxRate: [{ required: true, message: "请输入税率!" }],
      },
      url: {
        add: "/business/busOtherRoomCard/add",
        edit: "/business/busOtherRoomCard/edit",
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
    getAction("/business/busOtherRoomCard/queryByHotelId", {}).then((res) => {
      if (res.success) {
        this.model = res.result;
      }
      //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    });
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
            var _info = JSON.parse(localStorage.getItem("storeInfo"));
            if (_info) {
              this.model.hotelId = _info.id;
            }
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