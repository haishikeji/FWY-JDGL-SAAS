<template>
	<view>
		<div class="card-nodata">
			<div style="color:#00aaff;text-align:center;">
				你还没有办理酒店会员,无法享受会员价及会员权益！
			</div>
			<div style="width:100%;margin-top:10px;">
				<u-button @click="toSearchHotel" type="primary">去办卡</u-button>
			</div>
		</div>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			toSearchHotel(){
				uni.navigateTo({
					url: '/components/searchHotel/searchHotel'
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
.card-nodata{
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	height: 50vh;
	width: 80%;
	margin: auto;
}
</style>
