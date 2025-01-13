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
    <template slot="footer">
        <a-button key="back" @click="handleCancel">
          关闭
        </a-button>
        <a-button key="submit" type="primary" @click="handleOk">
          确认
        </a-button>
      </template>
    <pos-type-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></pos-type-form>
  </j-modal>
</template>

<script>

  import PosTypeForm from './stockChecksForm.vue'
  export default {
    name: 'PosTypeModal',
    components: {
      PosTypeForm
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false,
        flag:true
      }
    },
    methods: {
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
        })
      },
      edit (record, flag) {
        this.visible=true
        this.flag = flag
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record, flag);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      // handleYes(){
      //   this.$refs.realForm.submitForm(true);
      // },
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
