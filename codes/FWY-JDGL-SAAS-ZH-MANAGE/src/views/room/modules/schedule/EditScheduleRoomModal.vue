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
    <schedule-room-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></schedule-room-form>
  </j-modal>
</template>

<script>

  import ScheduleRoomForm from './EditScheduleRoomForm'
  export default {
    name: 'ScheduleRoomModal',
    components: {
      ScheduleRoomForm
    },
    data () {
      return {
        title:'',
        width:1300,
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
      addList(record) {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.realForm.addList(record);
      });
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
<style scoped>
/deep/.ant-modal-body {
  padding: 12px;
  max-height: calc(80vh - 150px);
  overflow-y: auto;
  &::-webkit-scrollbar {
    width: 6px;
    /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }

  &::-webkit-scrollbar-thumb {
    background: #e3e3e6;
    border-radius: 6px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
    border-radius: 5px;
  }
}
</style>