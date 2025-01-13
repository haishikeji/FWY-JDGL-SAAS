<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <ces-order-lease-goods-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></ces-order-lease-goods-form>
  </j-modal>
</template>

<script>

  import CesOrderLeaseGoodsForm from './CesOrderLeaseGoodsForm'
  export default {
    name: 'CesOrderLeaseGoodsModal',
    components: {
      CesOrderLeaseGoodsForm
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      add (livingOrderId) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(livingOrderId);
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>