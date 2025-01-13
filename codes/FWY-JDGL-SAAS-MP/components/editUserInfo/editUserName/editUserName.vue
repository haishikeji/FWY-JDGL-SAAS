<template>
<view class="pages">
    <div class="input-back">
		<u--input :customStyle="{height: '40px'}" placeholder="请输入内容" border="surround" v-model="value" @change="change"></u--input>
	</div>
	<div class="save-btn">
		<u-button type="primary">保存</u-button>
	</div>
</view>
</template>

<script>
export default {
    data() {
        return {
			value:''
        };
    },
	methods:{
		change(){
			console.log(this.value);
		}
	}
}
</script>

<style lang="scss">
.pages{
	background: #f5f5f5;
	width: 100vw;
	height: 100vh;
}
.input-back{
	width: 100%;
	// height: 100px;
	background: #fff;
}
.save-btn{
	text-align:center;
	// margin-top:20px;
	position: fixed;
	bottom: 0;
	width: 100%;
	padding: 30px;
	box-sizing: border-box;
}
</style>
