<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭1">
    <bus-market-coupons-cash-used-list :couponsId="couponsId" ref="modalCouponsCashUsedListModal"></bus-market-coupons-cash-used-list>
  </j-modal>
</template>

<script>
  import BusMarketCouponsCashUsedList from "./BusMarketCouponsCashUsedList";
  export default {
    name: 'BusMarketCouponsCashUsedListModal',
    components: {
      BusMarketCouponsCashUsedList
    },
    props: {
      couponsId: {
        type: String,
        default: ''
      }
    },
    data () {
      return {
        title:'',
        width:1200,
        visible: false,
        disableSubmit: false,
        // eventId:'',
      }
    },
    methods: {
      add () {
        this.visible=true

      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.modalCouponsCashUsedListModal.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.modalCouponsCashUsedListModal.submitForm();
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