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
    <pay-or-refund
        ref="realForm"
        @ok="submitCallback"
        :disabled="disableSubmit"
        :showYinshou="showYinshou"
    ></pay-or-refund>
  </j-modal>
</template>

<script>
import PayOrRefund from '@views/room/modules/checkIn/PayOrRefund'
export default {
  name: 'PayOrRefundModal',
  components: {
    PayOrRefund,
  },
  data() {
    return {
      title: "",
      width: 1000,
      visible: false,
      disableSubmit: false,
      showYinshou: true,
      //是退款
      isRefund:false,
    };
  },
  methods: {
    add(record) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.realForm.add(record);
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
