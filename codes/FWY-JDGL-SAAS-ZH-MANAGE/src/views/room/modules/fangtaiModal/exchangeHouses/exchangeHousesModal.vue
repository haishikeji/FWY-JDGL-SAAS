<template>
  <div>
    <j-modal
        :title="title"
        :width="width"
        :visible="visible"
        switchFullscreen
        @ok="handleOk"
        :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
        @cancel="handleCancel"
        cancelText="关闭">
      <exchange-houses-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></exchange-houses-form>
    </j-modal>
    <batchCardModalCopy ref="batchCardModalCopy" />
  </div>
</template>

<script>

  import ExchangeHousesForm from './exchangeHousesForm.vue'
  import batchCardModalCopy from './../../../modules/checkIn/batchCardModalCopy'
  export default {
    name: 'PosTableModal',
    components: {
      ExchangeHousesForm,
      batchCardModalCopy
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
          this.$refs.realForm.edit(record)
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
        console.log(e)
        this.$refs.batchCardModalCopy.edit(e)
        this.$emit('ok', e);
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>
