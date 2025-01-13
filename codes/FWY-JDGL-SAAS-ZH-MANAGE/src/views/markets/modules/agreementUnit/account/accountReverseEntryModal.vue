<template>
    <j-modal
            :title="title"
            :width="width"
            :visible="visible"
            switchFullscreen
            @ok="handleOk"
            @cancel="handleCancel"
            cancelText="关闭">
        <account-reverse-entry-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :maxMoney="maxMoney" :agreementModel="agreementModel"></account-reverse-entry-form>
    </j-modal>
</template>

<script>

    import AccountReverseEntryForm from './accountReverseEntryForm'
    export default {
        name: 'accountReverseEntryModal',
        components: {
            AccountReverseEntryForm
        },
        props: {
            agreementModel:{
                type: Object,
                required: false,
                default: () => {
                }
            },
            maxMoney: {
                type: Number,
                default: 200,
                required: false
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

