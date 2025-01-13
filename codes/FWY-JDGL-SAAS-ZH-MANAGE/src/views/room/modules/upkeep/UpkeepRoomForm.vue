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
              label="维修时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="dateRange"
            >
              <a-range-picker v-model="model.dateRange" @change="onChange" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="维修原因"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="remark"
            >
              <a-textarea
                v-model="model.remark"
                rows="4"
                placeholder="请输入维修原因"
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
import moment from "moment";
const date = new Date();
const endDate = new Date(date.setDate(date.getDate() + 1));
const dateRange = [
  moment(new Date(), "YYYY-MM-DD"),
  moment(endDate, "YYYY-MM-DD"),
];
export default {
  name: "BusMeetingRoomForm",
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
      model: {
        dateRange: dateRange,
        startDate: moment(dateRange[0]).format("YYYY-MM-DD"),
        endDate: moment(dateRange[1]).format("YYYY-MM-DD"),
      },
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
        dateRange: [{ required: true, message: "请选择维修时间!" }],
        remark: [{ required: true, message: "请输入维修原因!" }],
      },
      url: {
        add: "/fw/fwRoomRepair/add",
        edit: "/fw/fwRoomRepair/edit",
        queryById: "/fw/fwRoomRepair/queryById",
      },
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
  },
  methods: {
    moment,
    onChange(e, dateString) {
      this.model.startDate = dateString[0];
      this.model.endDate = dateString[1];
    },
    add(record) {
      this.modelDefault = Object.assign(this.modelDefault, record);
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