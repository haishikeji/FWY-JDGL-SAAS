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
              label="公司名称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="name"
            >
              <a-input
                v-model="model.name"
                placeholder="请输入公司名称"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="会议主题"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="theme"
            >
              <a-input
                v-model="model.theme"
                placeholder="请输入会议主题"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="预定人姓名"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="destinedName"
            >
              <a-input
                v-model="model.destinedName"
                placeholder="请输入预定人姓名"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="联系电话"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="mobile"
            >
              <a-input
                v-model="model.mobile"
                placeholder="请输入联系电话"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="备注"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="remark"
            >
              <a-textarea
                v-model="model.remark"
                rows="4"
                placeholder="请输入备注"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-divider orientation="left">档期</a-divider>
          </a-col>
          <template v-for="(item, index) in model.detailList">
            <a-col :span="7">
              <a-form-model-item
                label="会议室名称"
                :labelCol="labelCol2"
                :wrapperCol="wrapperCol2"
                :prop="`detailList[${index}].meetingRoomId`"
                :rules="[
                  {
                    required: true,
                    message: '请选择会议室!',
                    trigger: 'change',
                  },
                ]"
              >
                <a-select
                  v-model="item.meetingRoomId"
                  style="width: 100%"
                  placeholder="会议室"
                  :allowClear="true"
                >
                  <a-select-option
                    v-for="(item, index) in meetingRoomList"
                    :key="index"
                    :value="item.id"
                    >{{ item.name }}</a-select-option
                  >
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="7">
              <a-form-model-item
                label="开会日期"
                :labelCol="labelCol2"
                :wrapperCol="wrapperCol2"
                :prop="`detailList[${index}].meetingDate`"
                :rules="[
                  {
                    required: true,
                    message: '请选择开会日期!',
                    trigger: 'change',
                  },
                ]"
              >
                <j-date
                  placeholder="请选择开始时间"
                  v-model="item.meetingDate"
                  style="width: 100%"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="7">
              <a-form-model-item
                label="开会时段"
                :labelCol="labelCol2"
                :wrapperCol="wrapperCol2"
                :prop="`detailList[${index}].timeSpan`"
                :rules="[
                  {
                    required: true,
                    message: '请选择开会时段!',
                    trigger: 'change',
                  },
                ]"
              >
                <a-select
                  v-model="item.timeSpan"
                  style="width: 100%"
                  placeholder="会议室"
                  :allowClear="true"
                >
                  <a-select-option
                    v-for="(item, index) in timeSpanList"
                    :key="index"
                    :value="item.id"
                    >{{ item.name }}</a-select-option
                  >
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="2">
              <a-icon
                v-if="model.detailList.length - 1 == index"
                type="plus-circle"
                class="dynamic-delete-button"
                @click="puls()"
              />
              <a-icon
                type="minus-circle"
                style="color: #f56c6c"
                class="dynamic-delete-button"
                v-if="model.detailList.length > 1"
                @click="() => remove(index)"
              />
            </a-col>
          </template>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";

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
      model: { detailList: [{}] },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol2: {
        xs: { span: 7 },
        sm: { span: 10 },
      },
      wrapperCol2: {
        xs: { span: 7 },
        sm: { span: 11 },
      },
      confirmLoading: false,
      validatorRules: {
        name: [{ required: true, message: "请输入名称!" }],
        theme: [{ required: true, message: "请输入会议主题!" }],
        destinedName: [{ required: true, message: "请输入预定人姓名!" }],
        mobile: [{ required: true, message: "请输入联系电话!" }],
      },
      url: {
        add: "/business/busMeetingRoomSchedule/create",
        edit: "/business/busMeetingRoomSchedule/edit",
        queryById: "/business/busMeetingRoomSchedule/queryById",
      },
      meetingRoomList: [],
      timeSpanList: [
        { id: 1, name: "上午" },
        { id: 2, name: "下午" },
        { id: 3, name: "晚上" },
      ],
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    getAction("/business/busMeetingRoom/list", {
      pageNo: 1,
      pageSize: 100,
      stauts: 1,
    }).then((res) => {
      if (res.success) {
        this.meetingRoomList = res.result.records;
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
    puls() {
      this.model.detailList.push({ });
    },
    remove(index) {
      this.model.detailList.splice(index, 1);
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
<style scoped>
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 10px;
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