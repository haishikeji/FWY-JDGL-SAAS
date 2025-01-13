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
              label="房型"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-checkbox default-checked disabled>{{ layoutName }}</a-checkbox>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item
              label="房态"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="roomStatus"
            >
              <a-checkbox-group
                v-model="model.roomStatus"
                :options="roomStatusOptions"
              />
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item
              label="已排房"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              {{ model.rooms.length || 0 }}/{{ presetNum }}间
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="已选"
              :labelCol="labelCol"
              :wrapperCol="{
                xs: { span: 24 },
                sm: { span: 20 },
              }"
              prop="roomStatus"
            >
              <a-tag
                color="blue"
                closable
                :visible="visible"
                @close.stop="tagClose2(index, item.name)"
                v-for="(item, index) in model.rooms"
                :key="index"
                >{{ item.name }}</a-tag
              >
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="房间"
              :labelCol="labelCol"
              :wrapperCol="{
                xs: { span: 24 },
                sm: { span: 20 },
              }"
            >
              <a-tabs
                default-active-key="room0"
                tab-position="left"
                :style="{ height: '300px' }"
              >
                <a-tab-pane
                  v-for="(room, index) in roomList"
                  :key="'room' + index"
                  :tab="room.floorName"
                >
                  <div
                    id="components-grid-demo-playground"
                    style="height: 300px; overflow: hidden auto"
                  >
                    <a-row :gutter="[8, 8]">
                      <a-col
                        :span="3"
                        v-for="(r, rIndex) in room.floorRooms"
                        :key="'floorRooms' + rIndex"
                      >
                        <div
                          :class="[
                            r.check == 1 ? 'check' : '',
                            r.kz == 1 ? 'kz' : '',
                          ]"
                          @click="checkRoomClick(r)"
                        >
                          {{ r.name }}
                        </div></a-col
                      >
                    </a-row>
                  </div>
                </a-tab-pane>
              </a-tabs>
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
  name: "SelectRoomForm",
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
    const rooms = [];
    for (let i = 0; i < 100; i++) {
      rooms.push({
        id: "100" + i,
        roomNo: 1000 + i,
        check: 0,
        kz: i == 1 ? 1 : 0,
      });
    }
    const rooms2 = [];
    for (let i = 0; i < 50; i++) {
      rooms2.push({
        id: "200" + i,
        roomNo: 2000 + i,
        check: 0,
        kz: i == 5 ? 1 : 0,
      });
    }
    return {
      visible: true,
      roomList: [
        { key: "1层", child: rooms },
        { key: "2层", child: rooms2 },
      ],
      roomTypeOptions: [],
      roomStatusOptions: ["空净", "空脏"],
      model: { roomType: "", rooms: [] },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 2 },
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
        add: "/business/busMeetingRoom/add",
        edit: "/business/busMeetingRoom/edit",
        queryById: "/business/busMeetingRoom/queryById",
      },
      layoutName: "",
      presetNum: 0,
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
    checkRoomClick(row) {
      if (row.check === 1) {
        row.check = 0;
        var index = this.model.rooms.findIndex((t) => t.key1 == row.key1);
        this.model.rooms.splice(index, 1);
      } else {
        if (this.model.rooms.length >= this.presetNum) {
          this.$message.warning("超过排房数量");
          return;
        }
        row.check = 1;
        this.model.rooms.push(row);
      }
    },
    tagClose2(index, key1) {
      this.model.rooms.splice(index, 1);
      this.roomList.some((t) => {
        var r = t.floorRooms.some((c) => {
          if (c.name === key1) {
            c.check = 0;
            return true;
          }
          return false;
        });
        if (r === true) {
          return true;
        }
      });
    },
    onChange(date, dateString) {
      console.log(date, dateString);
    },
    add(row) {
      console.log("buildingRooms", row.buildingRooms);
      this.layoutName = row.layout.name;
      this.presetNum = row.layout.presetNum;
      this.roomList = row.buildingRooms;
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.roomList.forEach((t) => {
        t.floorRooms.forEach((f) => {
          if (f.check && f.check == 1) {
            this.model.rooms.push(f);
          }
        });
      });
      this.visible = true;
    },
    submitForm() {
      const that = this;
      if (that.model.rooms.length === 0) {
        that.$message.warning("请先选择房间");
        return;
      }
      that.$emit("ok", that.model.rooms);
    },
  },
};
</script>
<style scoped>
#components-grid-demo-playground [class~="ant-col"] {
  background: transparent;
  border: 0;
}
#components-grid-demo-playground [class~="ant-col"] > div {
  /* background: #00a0e9; */
  height: 28px;
  line-height: 28px;
  font-size: 13px;
  text-align: center;
  cursor: pointer;
  border-radius: 5px;
}
#components-grid-demo-playground .check {
  border: 1px solid #00a0e9;
}
#components-grid-demo-playground .kz {
  background: rgba(166, 166, 166, 1);
}
#components-grid-demo-playground pre {
  background: #f9f9f9;
  border-radius: 6px;
  font-size: 13px;
  padding: 8px 16px;
}
</style>