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
    <select-room-form
      ref="realForm"
      @ok="submitCallback"
      :disabled="disableSubmit"
      :chooseRooms="chooseRooms"
    ></select-room-form>
  </j-modal>
</template>

<script>
import SelectRoomForm from "./liveBatchCardForm.vue";
export default {
  name: "SelectRoomFormModal",
  components: {
    SelectRoomForm,
  },
  data() {
    return {
      title: "选择制卡房间号",
      width: 800,
      visible: false,
      disableSubmit: false,
      chooseRooms: [],
    };
  },
  methods: {
    add(row) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.realForm.add(row);
      });
    },
    edit(record) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.realForm.edit(record);
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
    },
    handleOk() {
      this.$refs.realForm.submitForm();
    },
    submitCallback(e) {
      console.log("eeeeeeeeeeeeeee", e);

      this.$emit("ok", e);
      this.visible = false;
    },
    handleCancel() {
      this.close();
    },
  },
};
</script>
