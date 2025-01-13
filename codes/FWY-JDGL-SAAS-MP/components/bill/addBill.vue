<template>
<view class="pages">
    <u-subsection :list="list" mode="subsection" :current="current" @change="sectionChange"></u-subsection>
    <div v-if="current==0" class="form-input">
        <u-input :customStyle="{height:'30px'}" v-model="ordinaryInvoice.name" placeholder="请输入公司名称" />
        <u-input :customStyle="{height:'30px'}" v-model="ordinaryInvoice.number" placeholder="请输入纳税人识别号" />
    </div>
    <div v-if="current==1" class="form-input">
        <u-input :customStyle="{height:'30px'}" placeholder="请输入公司名称" />
        <u-input :customStyle="{height:'30px'}" placeholder="请输入纳税人识别号" />
        <u-input :customStyle="{height:'30px'}" placeholder="请输入开户银行" />
        <u-input :customStyle="{height:'30px'}" placeholder="请输入银行账户" />
        <u-input :customStyle="{height:'30px'}" placeholder="请输入公司地址" />
        <u-input :customStyle="{height:'30px'}" placeholder="请输入公司电话" />
    </div>
    <div class="bottom-btn">
		<u-button type="primary" @click="save">保存</u-button>
	</div>
</view>
</template>

<script>
export default {
    data() {
        return {
            list: ['公司普票', '公司专票'],
            current: 0,
            ordinaryInvoice: {
                name: null,
                number: null
            },
            specialInvoice: {
                name: null,
                number: null,
                bank: null,
                account: null,
                address: null,
                phone: null
            }
        }
    },
    methods: {
        save() {
            if (this.current == 0) {
                if (this.ordinaryInvoice.name == null) {
                    this.$u.toast('请输入公司名称')
                    return
                }
                if (this.ordinaryInvoice.number == null) {
                    this.$u.toast('请输入纳税人识别号')
                    return
                }
                uni.navigateBack(1)
            }
            if (this.current == 1) {

                uni.navigateBack(1)
            }
        },
        sectionChange(e) {
            console.log(e);
            this.current = e
        }
    }
}
</script>

<style lang="scss" scoped>
.pages {
    width: 100%;
    height: 100vh;
    background: #f5f5f5;
}

.form-input {
    background: #fff;
    padding: 10px;
}
.bottom-btn{
	position: fixed;
	bottom: 0;
	width: 100%;
	padding: 20px;
	background: #fff;
	box-sizing: border-box;
}
</style>
