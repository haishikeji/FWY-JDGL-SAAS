<template>
	<view>
		<div class="images">
			<u--image shape="circle" :showLoading="true" :src="'/static/niu.jpg'" width="150px" height="150px" @click="click"></u--image>
		</div>

		<div style="padding:15px;">
			酒店会，提供在线预定酒店，办理酒店会员，充值，消费。百分百酒店直营，花最少的价钱，住更好的酒店！
		</div>

		<u-cell title="营业执照" :isLink="true" arrow-direction="right"></u-cell>
		<u-cell title="经营许可证" :isLink="true" arrow-direction="right"></u-cell>
		<u-cell title="用户协议" :isLink="true" arrow-direction="right"></u-cell>
		<u-cell title="隐私政策" :isLink="true" arrow-direction="right"></u-cell>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss" scoped>
.images{
	display: flex;
	justify-content: center;
	align-items: center;
	height: 300px;
}
</style>
