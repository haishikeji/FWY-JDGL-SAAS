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
          <a-col :span="12" type="flex" justify="start">
            <a-form-model-item
              label="分类"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="goodType"
            >
              <a-cascader
                :defaultValue="arr"
                :options="treeData ? treeData : []"
                :field-names="{
                  label: 'name',
                  value: 'id',
                  children: 'children',
                }"
                placeholder="请选择"
                @change="onChange"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" type="flex" justify="start">
            <a-form-model-item
              style="width: 100%"
              label="单位"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="goodUnit"
            >
              <a-select
                v-model="model.goodUnit"
                placeholder="请选择商品单位"
                @change="handleChange"
              >
                <a-select-option
                  v-for="item in unitData"
                  :unitName="item.name"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item
              label="名称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="name"
            >
              <a-input v-model="model.name" placeholder="请填写名称" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item
              label="售价"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="sellingPrice"
            >
              <a-input-number v-model="model.sellingPrice" :min="0" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { getRoomPlans, getSelectList } from "@/api/api";
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import { getUnit } from "@/api/good";

export default {
  name: "BusMarketMemberForm",
  inject: ["treeData"],
  props: {
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      unitData: [],
      options: [
        {
          label: "张三",
          value: "1",
        },
      ],
      model: {
        id: "",
        hotelId: 0,
        goodType: "",
        goodUnit: undefined,
        barCode: "",
        bid: null,
        name: "",
        sellingPrice: 0,
        purchases: null,
        salesVolume: null,
        inventory: null,
        appState: null,
      },
      labelCol: {
        xs: {
          span: 24,
        },
        sm: {
          span: 5,
        },
      },
      wrapperCol: {
        xs: {
          span: 24,
        },
        sm: {
          span: 16,
        },
      },
      confirmLoading: false,
      validatorRules: {
        goodType: [
          {
            required: true,
            message: "请选择分类!",
          },
        ],
        goodUnit: [
          {
            required: true,
            message: "请选择单位!",
          },
        ],
        name: [
          {
            required: true,
            message: "请输入名称!",
          },
        ],
        sellingPrice: [
          {
            required: true,
            message: "请输入价格!",
          },
        ],
      },
      url: {
        // add: "/rooms/cesRoomLayout/save",
        add: "/rooms/cesGoods/addTemp",
        // edit: "/rooms/cesRoomLayout/modify",
        edit: "/rooms/cesGoods/modify",
        delete: "/rooms/cesGoods/delete",
        // queryById: "/rooms/cesRoomLayout/queryById",
      },
      iconChooseVisible: false,
      roomPlans: [],
      members: [],
      arr: [],
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
      this.initData();
      this.getData();
    }
    console.log(111111111111, this.treeData);
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    onChange(e) {
      console.log(e);
      this.model.goodType = e[e.length - 1];
      
      console.log(this.model.goodType);
    },
    handleChange(e, options) {
      console.log(e);
      this.model.goodUnit = e;
    },
    initData() {
      getRoomPlans(this.model.hotelId, null).then((res) => {
        if (res.success) {
          this.roomPlans = res.result;
        }
      });
    },
    selectIcons() {
      this.iconChooseVisible = true;
    },
    handleIconCancel() {
      this.iconChooseVisible = false;
    },
    handleIconChoose(value) {
      console.log(value);
      this.model.icon = value;
      this.iconChooseVisible = false;
    },
    add() {
      this.edit(this.modelDefault);
    },
    //筛选分类数组
    filterType(array, id) {
      let arr = [];
      array.forEach((item, index) => {
        if (item.id == id) {
          this.arr[0] = item.parentId;
          this.arr[1] = item.id;
          return;
        } else if (Array.isArray(item.children) && item.children) {
          this.filterType(item.children, id);
        }
      });
    },
    edit(record) {
      console.log(22222222, record);
      // this.model = Object.assign({}, record);
      this.model = JSON.parse(JSON.stringify(record));

      console.log(this.filterType(this.treeData, record.goodType));
      console.log(this.arr);
      this.visible = true;
      getSelectList({
        id: this.model.id,
      }).then((res) => {
        if (res.success) {
          this.members = res.result;
        }
      });
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
          if (this.model.payFlag == 0) {
            this.model.payAmount = 0;
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                // that.$message.success(res.message);
                that.$emit("ok", res.result);
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
    getData() {
      getUnit({ pageNo: 1, pageSize: 1000 }).then((res) => {
        console.log(res);
        if (res.code && res.code == 200) {
          this.unitData = res.result.records;
        }
      });
    },
    switchState(e) {
      console.log("这是滑动按钮触发", e);
      this.model.appState = e;
    },
  },
};
</script>

<style scoped>
.avatar-uploader > .ant-upload {
  width: 104px;
  height: 104px;
}
</style>
