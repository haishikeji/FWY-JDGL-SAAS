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
    <market-agreement-customer-info-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :agreementModel="agreementModel"></market-agreement-customer-info-form>
  </j-modal>
</template>

<script>

  import marketAgreementCustomerInfoForm from './marketAgreementCustomerInfoForm'
  export default {
    name: 'marketAgreementCustomerInfoModal',
    components: {
      marketAgreementCustomerInfoForm
    },
    props: {
      agreementModel:{
        type: Object,
        required: false,
        default: () => {
        }
      }
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
