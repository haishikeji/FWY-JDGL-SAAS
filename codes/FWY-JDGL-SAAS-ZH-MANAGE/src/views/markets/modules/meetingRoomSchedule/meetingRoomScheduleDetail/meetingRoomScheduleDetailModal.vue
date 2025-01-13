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
    <meeting-room-schedule-detail-list
      :meetingRoomScheduleId="meetingRoomScheduleId"
      ref="modalMeetingRoomScheduleDetailListModal"
      @ok="submitCallback"
    ></meeting-room-schedule-detail-list>
  </j-modal>
</template>

<script>
import meetingRoomScheduleDetailList from "./meetingRoomScheduleDetailList";
export default {
  name: "meetingRoomScheduleDetailListModal",
  components: {
    meetingRoomScheduleDetailList,
  },
  props: {
    meetingRoomScheduleId: {
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
    };
  },
  methods: {
    add() {
      this.visible = true;
    },
    edit(record) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.modalMeetingRoomScheduleDetailListModal.edit(record);
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
    },
    handleOk() {
      this.$refs.modalMeetingRoomScheduleDetailListModal.submitForm();
    },
    submitCallback() {
      this.$emit("ok");
      this.visible = false;
    },
    handleCancel() {
      this.close();
    },
  },
};
</script>