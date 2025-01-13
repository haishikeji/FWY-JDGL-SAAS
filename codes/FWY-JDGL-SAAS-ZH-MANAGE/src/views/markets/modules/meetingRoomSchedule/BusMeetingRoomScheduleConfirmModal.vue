<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <bus-meeting-room-schedule-confirm
      ref="modalBusMeetingRoomScheduleConfirmModal"
      @ok="submitCallback"
    ></bus-meeting-room-schedule-confirm>
  </j-modal>
</template>

<script>
import BusMeetingRoomScheduleConfirm from "./BusMeetingRoomScheduleConfirm";
export default {
  name: "BusMarketCouponsCashUsedListModal",
  components: {
    BusMeetingRoomScheduleConfirm,
  },
  props: {
    couponsId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      title: "",
      width: 1200,
      visible: false,
      disableSubmit: false,
      // eventId:'',
    };
  },
  methods: {
    add() {
      this.visible = true;
    },
    edit(record) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.modalBusMeetingRoomScheduleConfirmModal.edit(record);
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
    },
    handleOk() {
      this.$refs.modalBusMeetingRoomScheduleConfirmModal.submitForm();
    },
    submitCallback() {
      this.$emit("ok");
    },
    handleCancel() {
      this.close();
    },
  },
};
</script>