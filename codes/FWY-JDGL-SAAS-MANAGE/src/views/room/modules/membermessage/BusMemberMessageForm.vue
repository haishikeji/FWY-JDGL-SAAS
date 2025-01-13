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
              label="房号"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="livingOrderId"
            >
              <a-select v-model="model.livingOrderId" placeholder="请选择房号">
                <a-select-option
                  :value="item.livingOrderId"
                  v-for="(item, index) in roomsList"
                  :key="item.id"
                >
                  {{ item.roomName }}/{{ item.customerName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="留言人"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="userName"
            >
              <a-input
                v-model="model.userName"
                placeholder="请输入留言人"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="自动提醒时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="remindDate"
            >
              <j-date
                placeholder="请选择自动提醒时间"
                v-model="model.remindDate"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="有效时间"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="validDate"
            >
              <j-date
                placeholder="请选择有效时间"
                v-model="model.validDate"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="是否传达"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="relay"
            >
              <a-select placeholder="请选择" v-model="model.relay">
                <a-select-option :value="1"> 是 </a-select-option>
                <a-select-option :value="2"> 否 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="留言内容"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="contentBody"
            >
              <a-textarea
                v-model="model.contentBody"
                rows="4"
                placeholder="请输入留言内容"
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
  name: "BusMemberMessageForm",
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
        tenantId: [{ required: true, message: "请输入关联租户!" }],
        hotelId: [{ required: true, message: "请输入关联酒店!" }],
        remindDate: [{ required: true, message: "请输入自动提醒时间!" }],
        validDate: [{ required: true, message: "请输入有效时间!" }],
        relay: [{ required: true, message: "请输入是否传达!" }],
        createDate: [{ required: true, message: "请输入创建时间!" }],
      },
      url: {
        add: "/business/busMemberMessage/add",
        edit: "/business/busMemberMessage/edit",
        queryById: "/business/busMemberMessage/queryById",
      },
      roomsList: [],
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
    getAction("/business/busRoomBookingOrders/living-orders?status=-1", {
      pageNo: 1,
      pageSize: 99999,
    }).then((res) => {
      if (res.success) {
        this.roomsList = res.result.records;
      }
    });
  },
  methods: {
    add(livingOrderId) {
      this.modelDefault.livingOrderId = livingOrderId;
      if (this.modelDefault.livingOrderId == null) {
        this.modelDefault.livingOrderId = undefined;
      }
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      console.log("this.model", this.model);
      this.visible = true;
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          var find = this.roomsList.find(
            (t) => t.livingOrderId === this.model.livingOrderId
          );
          this.model.roomId = find.roomId;
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