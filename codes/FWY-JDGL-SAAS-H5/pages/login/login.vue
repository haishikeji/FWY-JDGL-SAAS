<template>
<div>
    <form>
        <label for="username">账号:</label>
        <input type="text" id="username" v-model="username">
        <label for="password">密码:</label>
        <input type="password" id="password" v-model="password">
        <button type="submit" @click.prevent="login">登录</button>
    </form>
</div>
</template>

<script>
import {
    login
} from '@/utils/api.js'
export default {
    data() {
        return {
            username: '',
            password: ''
        }
    },
	onShow() {
		if(uni.getStorageSync('token')) {
			uni.switchTab({
			    url: '/pages/index/index'
			})
		}
        if (uni.getStorageSync('userInfo')) {
            let userInfo = JSON.parse(uni.getStorageSync('userInfo'))
            this.username = userInfo.username
            this.password = userInfo.password            
        }
	},
    methods: {
        login() {
            // perform login logic here
			// uni.switchTab({
			//     url: '/pages/index/index'
			// })
			// return
            login({
                username: this.username,
                password: this.password
            }).then(res => {
                console.log(res)
                if (res.code === 200) {
                    this.$store.commit('setToken', res.result.token)
                    uni.setStorageSync("token", res.result.token)
                    let userInfo = {
                        username: this.username,
                        password: this.password
                    }
                    uni.setStorageSync("userInfo", JSON.stringify(userInfo))
                    uni.showToast({
                        icon: 'none',   
                        duration: 3000,
                        title: `成功`
                    });
                    uni.switchTab({
                        url: '/pages/index/index'
                    })
                }
            })
        }
    }
}
</script>

<style>
/* add any necessary styles here */
form {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 50px;
}

label {
    margin-bottom: 10px;
}

input {
    padding: 5px;
    margin-bottom: 20px;
    border-radius: 5px;
    border: 1px solid #ccc;
}

button {
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

button:hover {
    background-color: #3e8e41;
}
</style>
