<template>
  <a-modal
    title="关联房间"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    cancelText="关闭"
    style="top: 20px"
  >
    <a-spin :spinning="confirmLoading">
      <div class="relation-wrapper">
        <a-row>
          <a-col :span="12" style="height: 500px; overflow-y: auto">
            <div class="wrapper-floor" v-for="item in roomTree" :key="item.id">
              <div>{{ item.name }}</div>
              <div class="wrapper-room">
                <div
                  @click="onRoomClick(room)"
                  class="wrapper-room-item"
                  :class="[
                    selectedRooms.findIndex((s) => s.id == room.id) > -1
                      ? 'selected-class'
                      : '',
                  ]"
                  v-for="room in item.rooms"
                  :key="room.id"
                >
                  {{ room.name }}
                </div>
              </div>
            </div>
          </a-col>

          <a-col :span="12" style="height: 500px; overflow-y: auto">
            <div style="margin: 10px">已关联房间</div>
            <div style="display: flex; flex-wrap: wrap; height: max-content">
              <div
                @click="delSelectedRoom(room)"
                class="wrapper-room-item-1"
                v-for="room in selectedRooms"
                :key="room.id"
              >
                {{ room.name }}
              </div>
            </div>
          </a-col>
        </a-row>
      </div>
    </a-spin>
  </a-modal>
</template>
  
<script>
import { putAction, getAction, postAction } from "@/api/manage";
import { min } from "lodash";
export default {
  name: "UserRoomRelation",
  data() {
    return {
      confirmLoading: false,
      visible: false,
      roomTree: [],
      selectedRooms: [],
      userId: null,
      yezhuId: "",
    };
  },
  created() {},

  methods: {
    delSelectedRoom(r) {
      let findIndex = this.selectedRooms.findIndex((s) => s.id == r.id);
      if (findIndex > -1) {
        this.selectedRooms.splice(findIndex, 1);
      }
    },
    onRoomClick(r) {
      let findIndex = this.selectedRooms.findIndex((s) => s.id == r.id);
      if (findIndex > -1) {
        this.selectedRooms.splice(findIndex, 1);
      } else {
        this.selectedRooms.push(r);
      }
    },
    show(record) {
      this.selectedRooms = JSON.parse(JSON.stringify(record.inRoomList));
      this.selectedRooms = this.selectedRooms.map((t) => {
        return {
          id: t.roomId,
          name: t.roomName,
        };
      });
      this.yezhuId = record.id;
      getAction("/business/busRoomBookingOrders/rili-fangtai", {
        start: "2023-05-19",
        end: "2023-06-18",
      }).then((res) => {
        if (res.success) {
          this.roomTree = res.result.floorRoomVos;
        }
      });
      this.visible = true;
    },
    close() {
      this.$emit("close");
      this.visible = false;
    },
    handleSubmit() {
      // 触发表单验证
      let bodyData = this.selectedRooms.map((s) => {
        return {
          roomId: s.id,
          busYezhuId: this.yezhuId,
        };
      });
      postAction(
        "/business/busYezhuInRoom/add?userId=" + this.userId,
        bodyData
      ).then((res) => {
        if (res.success) {
          this.$message.success("保存成功！");
          this.$emit("ok");
          this.visible = false;
        }
      });
    },
    handleCancel() {
      this.close();
    },
  },
};
</script>
<style>
.wrapper-room {
  display: flex;
  flex-wrap: wrap;
}

.wrapper-room-item:hover {
  border: 1px solid #f00;
  color: #fff;
  background-color: #ff000088;
}

.selected-class,
.wrapper-room-item-1 {
  border: 1px solid #f00 !important;
  color: #fff !important;
  background-color: #ff000088 !important;
}

.wrapper-room-item,
.wrapper-room-item-1 {
  width: fit-content;
  padding: 5px 8px;
  border: 1px solid #1890ff;
  background-color: #1890ff88;
  margin-left: 10px;
  margin-top: 5px;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}
</style>