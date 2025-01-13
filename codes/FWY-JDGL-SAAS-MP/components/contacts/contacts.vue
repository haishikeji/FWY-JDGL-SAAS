<template>
	<view class="pages">
		<div class="card" v-for="(item, index) in contactsList" :key="item.id" @click="()=>backSet(item, index)">
			<div>
				{{item.name}}
			</div>
			<div>
				{{item.phone}}
			</div>
		</div>
		<div class="add" @click="handleToAdd">
			<u-icon name="plus" color="#000" size="20"></u-icon>
			<div>立即添加</div>
		</div>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				contactsList:[
					{
						name:'小明',
						phone:'17212359128'
					},
					{
						name:'小红',
						phone:'17212359128'
					},
					{
						name:'小花',
						phone:'17212359128'
					},
					{
						name:'小西',
						phone:'17212359128'
					}
				]
			}
		},
		methods: {
			handleToAdd(){
				console.log("别点了",uni);
				uni.navigateTo({
					url:'/components/addContacts/addContacts',
					success:(res)=>{
						console.log('成功',res);
					},
					fail:(err)=>{
						console.log('失败',err);
					}
				})
			},
			backSet(data, idx){
				console.log(data, idx);
				let pages = getCurrentPages();
				let prevPage = pages[pages.length - 2];
				prevPage.$vm.form.name = data.name;
				prevPage.$vm.form.phone = data.phone;
				uni.navigateBack({
					delta: 1
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
.pages{
	width: 100%;
	height: 100vh;
	background: #f5f5f5;
	overflow: auto;
}
.card{
	width: 96vw;
	height: 100px;
	background: #fff;
	padding: 20px;
	box-sizing: border-box;
	margin: 10px auto;
	box-shadow: 0 0 10px #00000029;
	display: flex;
	align-items: flex-start;
	flex-direction: column;
	justify-content: space-around;
	border-radius: 5px;
}
.add{
	width: 96vw;
	height: 100px;
	background: #fff;
	padding: 20px;
	box-sizing: border-box;
	margin: 10px auto;
	box-shadow: 0 0 10px #00000029;
	display: flex;
	align-items: center;
	border-radius: 5px;
}
</style>
