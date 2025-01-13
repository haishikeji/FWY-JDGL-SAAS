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
    <picking-goods-order
      ref="realForm"
      @ok="submitCallback"
      :disabled="disableSubmit"
      :supplierId="supplierId"
    ></picking-goods-order>
  </j-modal>
</template>

<script>
import PickingGoodsOrder from "./SupplierInGoods";
export default {
  name: "PickingGoodsOrderModal",
  components: {
    PickingGoodsOrder,
  },
  data() {
    return {
      title: "",
      width: 800,
      visible: false,
      disableSubmit: false,
      supplierId: "",
    };
  },
  methods: {
    add() {
      this.visible = true;
      // this.$nextTick(()=>{
      //   this.$refs.realForm.add();
      // })
    },
    edit(record) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.realForm.edit(record);
      });
    },
    close() {
      this.visible = false;
    },
    handleOk() {
      this.$refs.realForm.submitForm();
    },
    submitCallback(e) {
      // this.$emit("ok", e);
      this.visible = false;
    },
    handleCancel() {
      this.close();
    },
  },
};
</script>
