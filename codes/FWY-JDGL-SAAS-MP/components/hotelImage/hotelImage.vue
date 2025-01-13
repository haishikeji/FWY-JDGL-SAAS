<template>
<view class="image-list">
    <u-album :urls="urls" multipleSize="30vw"></u-album>
</view>
</template>

<script>
export default {
    data() {
        return {
            urls: [
                'https://cdn.uviewui.com/uview/album/1.jpg',
                'https://cdn.uviewui.com/uview/album/2.jpg',
                'https://cdn.uviewui.com/uview/album/3.jpg',
                'https://cdn.uviewui.com/uview/album/4.jpg',
                'https://cdn.uviewui.com/uview/album/5.jpg',
                'https://cdn.uviewui.com/uview/album/6.jpg',
                'https://cdn.uviewui.com/uview/album/7.jpg',
                'https://cdn.uviewui.com/uview/album/8.jpg',
                'https://cdn.uviewui.com/uview/album/9.jpg',
                'https://cdn.uviewui.com/uview/album/10.jpg',
            ]
        }
    },
    methods: {

    }
}
</script>

<style lang="scss" scoped>
.image-list{
	width: 100vw;
	padding: auto;
}
/deep/.u-album{
	justify-content: center;
	align-items: center;
}
</style>
