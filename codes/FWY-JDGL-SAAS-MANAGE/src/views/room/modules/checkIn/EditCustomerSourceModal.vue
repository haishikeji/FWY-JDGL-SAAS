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
    <bus-member-card-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></bus-member-card-form>
  </j-modal>
</template>

<script>

  import BusMemberCardForm from './EditCustomerSourceForm'
  export default {
    name: 'BusMemberCardModal',
    components: {
      BusMemberCardForm
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
      add (livingOrderId,roomId) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(livingOrderId,roomId);
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