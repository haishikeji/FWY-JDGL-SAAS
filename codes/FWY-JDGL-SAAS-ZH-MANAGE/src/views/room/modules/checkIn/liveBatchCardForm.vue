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
          <a-col :span="21">
            <a-form-model-item
              label="房型"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-checkbox-group @change="checkChange" v-model="checkLayoutList">
                <a-row>
                  <a-col
                    :span="6"
                    v-for="(item, index) in roomLayoutList"
                    :key="item.id"
                  >
                    <a-checkbox :value="item.id">
                      {{ item.name }}
                    </a-checkbox>
                  </a-col>
                </a-row>
              </a-checkbox-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="2" :pull="3" >
            <a-input v-model="roomNum" placeholder="房间号" style="width: 70px"></a-input>
          </a-col>
          <a-col :span="1" :pull="3">
            <a-button @click="makeCard" style="margin-left: 10px;" type="primary" ghost >制卡</a-button>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="房态"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="roomStatus"
            >
              <!-- <a-checkbox-group
                v-model="checkedRoomStatusList"
                :options="roomStatusOptions"
                @change="checkChange"
              /> -->
              <div style="display:flex;flex-wrap:wrap;">
                <div v-for="(item, index) in roomStatusOptions" :class="{'check':checkedRoomStatusList.some(ele=> ele==item.value)}" :key="index" class="nocheck room-status" :style="{borderLeft:`2px solid ${item.extend}`}" @click="roomStatusChange(item, index)">
                  {{item.label}}
                </div>
              </div>
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
              <!-- <a-tabs
                default-active-key="room-all"
                tab-position="left"
                :style="{ height: '300px' }"
              >
              <a-tab-pane
                  tab="全部"
                  key="room-all"
                > -->
                  <div>
                    <a-row :gutter="[8, 8]" style="overflow: auto;height:300px;">
                      <a-col
                        :span="24"
                        v-for="(item, index) in (roomList || [])"
                        :key="index"
                      >
                        <div>{{item.name}}</div>
                        <div style="display:flex;flex-wrap:wrap;">
                            <div v-for="(r, rIndex) in item.rooms" :style="{'border-left-color':filterColor(r.roomStatus)}" @click="checkRoomClick(r)" :key="'floorRooms' + rIndex" :class="[r.check == 1 ? 'check' : 'nocheck', r.kz == 1 ? 'kz' : '',]" class="nocheck room-status">
                            <!-- <div
                            :class="[
                              r.check == 1 ? 'check' : 'nocheck',
                              r.kz == 1 ? 'kz' : '',
                            ]"
                            style="display:flex;flex-wrap:wrap;"
                            @click="checkRoomClick(r)"
                          >
                            <div class="nocheck room-status">
                              {{ r.name }}
                            </div>
                          </div> -->
                          {{ r.name }}
                          </div>
                        </div>
                        </a-col>
                    </a-row>
                  </div>
                <!-- </a-tab-pane>
                <a-tab-pane
                  v-for="(room, index) in roomList"
                  :key="'room' + index"
                  :tab="room.name"
                >
                  <div
                    id="components-grid-demo-playground"
                    style="height: 300px; overflow: hidden auto"
                  >
                    <a-row :gutter="[8, 8]">
                      <a-col
                        :span="3"
                        v-for="(r, rIndex) in room.rooms"
                        :key="'floorRooms' + rIndex"
                      >
                        <div
                          :class="[
                            r.check == 1 ? 'check' : 'nocheck',
                            r.kz == 1 ? 'kz' : '',
                          ]"
                          :style="{'border-left-color':filterColor(r.roomStatus)}"
                          @click="checkRoomClick(r)"
                        >
                          {{ r.name }}
                        </div></a-col
                      >
                    </a-row>
                  </div>
                </a-tab-pane>
              </a-tabs> -->
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
    chooseRooms: []
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
        // { key: "1层", child: rooms },
        // { key: "2层", child: rooms2 },
      ],
      roomStatusList: [],
      checkedRoomStatusList: [],
      roomTypeOptions: [],
      roomStatusOptions: [],
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
      canUserRooms: [],
      oldcanUserRooms: [],
      checkLayoutList: [],
      roomLayoutList: [],
      oldroomList: [],
      kzfLiving: false,
      roomNum: '',
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

    getAction("/rooms/cesRoomLayout/list", { pageSize: 99999, pageNo: 1 }).then(
      (res) => {
        if (res.success) {
          this.roomLayoutList = res.result.records;
          if (this.roomLayoutList && this.roomLayoutList.length > 0) {
            this.roomLayoutList.forEach((item) => {
              this.checkLayoutList.push(item.id);
            });
          }
        }
      }
    );
    getAction("/rooms/cesRooms/room-status-color", {}).then((res) => {
            if (res.success) {
                // this.roomStatusColorList = res.result;
                var list = [];
                res.result.forEach((item) => {
                    list.push(item);
                });
                // list = list.filter(item => item.label == '空净' || item.label == '空脏')
                console.log(list);
                this.roomStatusOptions = list;
                this.checkChange('',[this.roomStatusOptions[0].value,this.roomStatusOptions[1].value])
            }
        });
    getAction("/business/busOtherEmptyDirtyRoom/queryByHotelId", {}).then(
        (res) => {
          if (res.success) {
            this.kzfLiving = res.result.status === 1;
          }
        }
      );
    this.getData()
  },
  methods: {
    filterColor(state){
      var color = '';
      let arr = this.roomStatusOptions.filter(item => item.value == state);
      color = arr[0] ? arr[0].extend : '';
      return color;
    },

    roomStatusChange(item, index){
      console.log('roomStatusChange');
      if (this.checkedRoomStatusList.some(ele=> ele==item.value)) {
        var index = this.checkedRoomStatusList.findIndex((t) => t == item.value);
        this.checkedRoomStatusList.splice(index, 1);
      } else {
        this.checkedRoomStatusList.push(item.value);
      }
      this.checkChange()
    },

    checkChange(e,stateList) {
            console.log("e", e);
            var list = this.oldroomList;
            console.log(list);
            console.log(this.checkedRoomStatusList);
            if (this.checkLayoutList.length > 0) {
                list = this.oldroomList.reduce((acc, curr) => {
                    const rooms = curr.rooms.filter((room) =>
                        this.checkLayoutList.includes(room.layoutId)
                    );
                    acc.push({
                        ...curr,
                        rooms
                    });
                    return acc;
                }, []);
            }
            if (this.checkedRoomStatusList.length > 0) {
                list = list.reduce((acc, curr) => {
                    const rooms = curr.rooms.filter((room) =>
                        this.checkedRoomStatusList.includes(
                            room.roomStatus.toString()
                        )
                    );
                    acc.push({
                        ...curr,
                        rooms
                    });
                    return acc;
                }, []);
            }
            if (this.checkedRoomStatusList.length == 0) {
              let arr = []
              this.roomStatusOptions.forEach(ele=>{
                arr.push(ele.value)
              })
              list = list.reduce((acc, curr) => {
                  const rooms = curr.rooms.filter((room) =>
                      arr.includes(
                          room.roomStatus.toString()
                      )
                  );
                  acc.push({
                      ...curr,
                      rooms
                  });
                  return acc;
              }, []);
            }
            if (stateList && stateList.length > 0) {
                list = list.reduce((acc, curr) => {
                    const rooms = curr.rooms.filter((room) =>
                        stateList.includes(room.roomStatus.toString())
                    );
                    acc.push({
                        ...curr,
                        rooms
                    });
                    return acc;
                }, []);
            }
            console.log("list2", list);
            this.roomList = list;
        },

    checkRoomClick(row) {
      console.log(row);
      if (!this.kzfLiving && row.roomStatus === 2) {
            this.$message.warning("空脏房不允许入住");
            return;
          }
      if (row.check === 1) {
        row.check = 0;
        var index = this.model.rooms.findIndex((t) => t.id == row.id);
        this.model.rooms.splice(index, 1);
      } else {
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
    onChange(e) {
      console.log(e);
      var list = [];
      this.oldroomList.forEach((item) => {
        var floorRooms = [];
        item.floorRooms.forEach((f) => {
          if (e.includes(f.layoutId)) {
            floorRooms.push(f);
          }
        });
        var itemd = JSON.parse(JSON.stringify(item));
        itemd.floorRooms = floorRooms;
        list.push(itemd);
      });
      this.roomList = list;
      // result = this.oldcustomerList.filter((t) => t.name.includes(value));
      console.log("this.oldroomList", this.oldroomList);
      console.log("this.roomList", this.roomList);
      this.checkChange()
    },
    add(row) {
      console.log("canUserRooms", row);
      // this.canUserRooms = row;
      this.oldroomList = JSON.parse(JSON.stringify(row[0].buildingRooms));

      // this.layoutName = row.layout.name;
      // this.presetNum = row.layout.presetNum;
      this.roomList = row[0].buildingRooms;
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      console.log(this.model)
      console.log(this.roomList, 'roomList')
      this.roomList.forEach((t) => {
        t.floorRooms.forEach((f) => {
          if (f.check && f.check === 1) {
            this.model.rooms.push(f)
          }
        });
      });
      this.visible = true;
    },
    getData(){
      getAction("/business/busRoomBookingOrders/rili-fangtai").then((res) => {
        if (res.success) {
          this.roomList = res.result.floorRoomVos;
          this.oldroomList = JSON.parse(JSON.stringify(res.result.floorRoomVos));
          this.chooseRooms.forEach(c =>{
            this.roomList.forEach(floor =>{
              floor.rooms.forEach(room =>{
                if (c.id === room.id){
                  this.$set(room,"check",1);
                }
              })
            })
          })
          console.log(this.roomList)
          this.model.rooms = this.chooseRooms;
        }
      });
    },
    submitForm() {
      const that = this;
      if (that.model.rooms.length === 0) {
        that.$message.warning("请先选择房间");
        return;
      }
      that.$emit("ok", that.model.rooms);
    },
    makeCard() {
      const that = this
      let findRoom = []
      this.oldroomList.forEach(e => {
        e.rooms.forEach(room => {
          if (room.name === this.roomNum) {
            findRoom.push(room)
          }
        })
      })
      console.log(findRoom)

      if (findRoom.length === 0) {
        this.$message.warning('未找到房间:' + this.roomNum)
      } else {
        that.$emit('ok', findRoom);
      }
    }
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
#components-grid-demo-playground .nocheck {
  border: 1px solid #cccccc;
  border-left-width: 2px;
}
.check {
  border: 1px solid #00a0e9 !important;
}
.nocheck {
  border: 1px solid #cccccc;
}
.room-status{
  /* height: 100%; */
  /* line-height: 100%; */
  font-size: 13px;
  text-align: center;
  cursor: pointer;
  border-radius: 5px;
  margin-right: 10px;
  border: 1px solid #cccccc;
  padding: 0px 16px;
  margin-top: 5px;
  border-left-width: 2px;
  border-left-style: solid;
}
</style>
