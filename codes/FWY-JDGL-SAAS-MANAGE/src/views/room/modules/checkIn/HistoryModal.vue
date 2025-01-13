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
    <history-model-table
      :phone="phone"
      ref="realForm"
      @ok="submitCallback"
      :disabled="disableSubmit"
    ></history-model-table>
  </j-modal>
</template>

<script>

import HistoryModelTable from '@views/room/modules/checkIn/HistoryModalTable'
export default {
  name: 'HistoryModel',
  components: {
    HistoryModelTable
  ,
  },
  data() {
    return {
      title: '历史入住信息',
      width: 800,
      visible: false,
      disableSubmit: false,
      phone: '',
    };
  },
  methods: {
    add(phone) {
      this.phone = phone;
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
