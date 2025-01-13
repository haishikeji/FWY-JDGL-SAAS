<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk($event)"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <bus-meeting-room-schedule-edit-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></bus-meeting-room-schedule-edit-form>
  </j-modal>
</template>

<script>

  import BusMeetingRoomScheduleEditForm from './BusMeetingRoomScheduleEditForm'
  export default {
    name: 'BusMeetingRoomModal',
    components: {
      BusMeetingRoomScheduleEditForm
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
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
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
      submitCallback($event){
        this.$emit('ok',$event);
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>