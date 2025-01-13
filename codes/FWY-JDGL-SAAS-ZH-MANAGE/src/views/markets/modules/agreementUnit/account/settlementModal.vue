<template>
    <j-modal
            :title="title"
            :width="width"
            :visible="visible"
            switchFullscreen
            @ok="handleOk"
            @cancel="handleCancel"
            cancelText="关闭">
        <settlement-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :maxMoney="maxMoney" :orderIds="orderIds" :agreementModel="agreementModel"></settlement-form>
    </j-modal>
</template>

<script>

    import SettlementForm from './settlementForm'
    export default {
        name: 'settlementModal',
        components: {
            SettlementForm,
        },
        props: {
            agreementModel:{
                type: Object,
                required: false,
                default: () => {
                }
            },
            // maxMoney: {
            //     type: Number,
            //     default: 200,
            //     required: false
            // },
            // orderIds: {
            //     type: String,
            //     default: 200,
            //     required: false
            // }
        },
        data () {
            return {
                title:'',
                orderIds:'',
                maxMoney:0,
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

