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
          <!-- <a-col :span="24">
            <a-form-model-item label="关联租户" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tenantId">
              <a-input v-model="model.tenantId" placeholder="请输入关联租户"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="关联酒店" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hotelId">
              <a-input v-model="model.hotelId" placeholder="请输入关联酒店"  ></a-input>
            </a-form-model-item>
          </a-col> -->
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
              label="开始时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="startTime"
            >
              <j-date
                placeholder="请选择开始时间"
                v-model="model.startTime"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="结束时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="endTime"
            >
              <j-date
                placeholder="请选择结束时间"
                v-model="model.endTime"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="优惠条件"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="conditions"
            >
              <a-radio-group v-model="model.type">
                <a-radio :value="1">不限制</a-radio>
                <a-radio :value="2">满</a-radio>
              </a-radio-group>
              <a-input-number
                v-model="model.conditions"
                :min="1"
                style="width: 30%"
                @blur="handleChange"
              />元可使用
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="每单可使用张数"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="ksyzs"
            >
              <a-input-number
                v-model="model.ksyzs"
                :min="1"
                placeholder="请输入每单可使用张数"
                style="width: 100%"
              />
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
                :min="1"
                placeholder="请输入金额"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="每人可领取张数"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              :min="1"
              prop="klqzs"
            >
              <a-input-number
                v-model="model.klqzs"
                placeholder="请输入可领取张数"
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
              label="使用说明"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="ruleDec"
            >
              <a-input
                v-model="model.ruleDec"
                placeholder="请输入使用说明"
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
  name: "BusMarketCouponsCashForm",
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
    var validateConditions = (rule, value, callback) => {
      console.log(22);
      if (!this.model.conditions && this.model.type === 2) {
        callback("请输入满多少元!");
      } else {
        callback();
      }
    };
    return {
      roomLayouts: [],
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
        name: [{ required: true, message: "请输入名称!" }],
        startTime: [{ required: true, message: "请输入开始时间!" }],
        endTime: [{ required: true, message: "请输入结束时间!" }],
        type: [{ required: true, message: "请输入优惠条件!" }],
        conditions: [
          { required: true, validator: validateConditions, trigger: "change" },
        ],
        ksyzs: [{ required: true, message: "请输入每单可使用张数!" }],
        cost: [{ required: true, message: "请输入金额!" }],
        introduce: [{ required: true, message: "请输入说明!" }],
        klqzs: [{ required: true, message: "请输入每人可领取张数!" }],
      },
      url: {
        add: "/business/busMarketCouponsCash/add",
        edit: "/business/busMarketCouponsCash/edit",
        queryById: "/business/busMarketCouponsCash/queryById",
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
    handleChange(e) {
      // this.$nextTick(() => {
      //   this.form.validateFields(["conditions"], { force: true });
      // });
      if (this.model.conditions) {
        this.$refs.form.clearValidate(["conditions"]);
      }
    },
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      console.log("record", record);
      var roomLayouts = (record.roomIds || "").split(",");
      this.model = Object.assign({}, record, { roomLayouts: roomLayouts });
      this.visible = true;
      getAction("/rooms/cesRoomLayout/list", { pageNo: 1, pageSize: 100 })
        .then((res) => {
          if (res.success) {
            this.roomLayouts = res.result.records;
            var data = [];
            res.result.records.forEach((item) => {
              data.push({ label: item.name, value: item.id });
            });
            this.roomLayouts = data;
          }
        })
        .finally(() => {});
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