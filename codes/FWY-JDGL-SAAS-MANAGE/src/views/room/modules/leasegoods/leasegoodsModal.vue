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
    <upkeep-room-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :livingOrderId="livingOrderId" :payTypeList = 'payTypeList'></upkeep-room-form>
  </j-modal>
</template>

<script>

  import UpkeepRoomForm from '@/views/room/leasegoods'
  export default {
    name: 'UpkeepRoomFormModal',
    components: {
      UpkeepRoomForm
    },
    data () {
      return {
        title:'',
        width:1000,
        visible: false,
        disableSubmit: false,
        livingOrderId:'',
        payTypeList: []
      }
    },
    methods: {
      add (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.payTypeList = this.payTypeList
          this.$refs.realForm.add(record);
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      close () {
        this.$emit('ok');
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
<style scoped>
/deep/.ant-modal-header {
  padding: 16px 24px !important;
  background-color: #fff !important;
}
/deep/.ant-modal-close {
  color: rgba(0, 0, 0, 0.45) !important;
}
</style>
