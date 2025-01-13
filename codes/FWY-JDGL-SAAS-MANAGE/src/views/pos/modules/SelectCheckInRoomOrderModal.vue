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
    <picking-goods-order ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></picking-goods-order>
  </j-modal>
</template>

<script>

  import PickingGoodsOrder from './SelectCheckInRoomOrder'
  export default {
    name: 'PickingGoodsOrderModal',
    components: {
      PickingGoodsOrder
    },
    data () {
      return {
        title:'',
        width:1200,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      add () {
        this.visible=true
        // this.$nextTick(()=>{
        //   this.$refs.realForm.add();
        // })
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
      submitCallback(e){
        this.$emit('ok',e);
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>