<template>
	<view class="pages">
		<div class="tabs">
			<div class="tabs-content" v-for="(item, index) in tabList" :key="index" :class="{'tabs-content-active':index==current}" @click="changeTabs(index)">
				{{item}}
			</div>
		</div>

		<div class="no-data">--还没有订单哟--</div>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				tabList:['全部订单','待付款','审核中','待收货','待发货','已拒绝'],
				current:0,
			}
		},
		methods: {
			changeTabs(index){
				this.current = index;
			}
		}
	}
</script>

<style lang="scss" scoped>
.tabs{
	display: flex;
	justify-content: space-evenly;
	align-items: center;
	height: 50px;
	background: #fff;
	.tabs-content{

	}
	.tabs-content-active{
		color: #00aaff;
		border-bottom: 2px solid #00aaff;
	}
}
.no-data{
	color: #00aaff;
	text-align: center;
	margin-top: 40px;
}
.pages{
	// height: 100vh;
	// width: 100vw;
	// background: #f5f5f5;
}
</style>
