<template>
    <j-modal
            :title="title"
            :width="width"
            :visible="visible"
            switchFullscreen
            @ok="handleOk"
            @cancel="handleCancel">
        <goods-consume-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :agreementModel="agreementModel"></goods-consume-form>
    </j-modal>
</template>

<script>

    import GoodsConsumeForm from './goodsConsumeForm'
    export default {
        name: 'goodsConsumeModal',
        components: {
            GoodsConsumeForm
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
                width:1000,
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

