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
    <refund
      :agreementId="agreementId"
      ref="realForm"
      @ok="submitCallback"
      :disabled="disableSubmit"
    ></refund>
  </j-modal>
</template>

<script>
import Refund from "./AgreementUnitTable.vue";

export default {
  name: "RefundModal",
  components: {
    Refund,
  },
  data() {
    return {
      title: "协议",
      width: 800,
      visible: false,
      disableSubmit: false,
      agreementId: "",
    };
  },
  methods: {
    add(agreementId) {
      this.agreementId = agreementId;
      this.visible = true;
      // this.$nextTick(() => {
      //   this.$refs.realForm.add();
      // });
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
    submitCallback(record) {
      console.log(record);
      this.$emit("ok", record);
      this.visible = false;
    },
    handleCancel() {
      this.close();
    },
  },
};
</script>