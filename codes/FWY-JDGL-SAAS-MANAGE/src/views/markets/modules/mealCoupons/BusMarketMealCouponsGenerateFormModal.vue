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
    <bus-market-meal-coupons-generate-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></bus-market-meal-coupons-generate-form>
  </j-modal>
</template>

<script>

  import BusMarketMealCouponsGenerateForm from './BusMarketMealCouponsGenerateForm'
  export default {
    name: 'BusMarketMealCouponsGenerateFormModal',
    components: {
      BusMarketMealCouponsGenerateForm
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
      add (total,couponsId) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(total,couponsId);
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