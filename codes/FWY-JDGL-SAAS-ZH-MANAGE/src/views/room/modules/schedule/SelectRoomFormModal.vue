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
    ></select-room-form>
  </j-modal>
</template>

<script>
import SelectRoomForm from "./SelectRoomForm";
export default {
  name: "SelectRoomFormModal",
  components: {
    SelectRoomForm,
  },
  data() {
    return {
      title: "",
      width: 800,
      visible: false,
      disableSubmit: false,
    };
  },
  methods: {
    add(row,chooseRooms) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.realForm.add(row,chooseRooms);
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
    submitCallback(e, chooseRooms) {
      console.log("e", e, chooseRooms);
      this.$emit("ok", e, chooseRooms);
      this.visible = false;
    },
    handleCancel() {
      this.close();
    },
  },
};
</script>
