<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleCancel"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <pos-table-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></pos-table-form>
  </j-modal>
</template>

<script>

  import PosTableForm from '../pos/diandan.vue'
  export default {
    name: 'PosTableModal',
    components: {
      PosTableForm
    },
    data () {
      return {
        title:'',
        width:1700,
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
      submitCallback(e){
        this.$emit('ok', e);
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>