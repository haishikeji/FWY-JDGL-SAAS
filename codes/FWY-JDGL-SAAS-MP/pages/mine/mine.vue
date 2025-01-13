<template>
<view class="pages">
    <u-navbar title="我的" :bgColor="'#00AAFF'" :titleStyle="{color:'#fff'}" placeholder>
        <view class="u-nav-slot" slot="left">
        </view>
    </u-navbar>
    <div class="avatar">
        <u-avatar @click="handleToEditUserInfo" size="80" src="https://cdn.uviewui.com/uview/album/1.jpg"></u-avatar>
        <div style="color:#fff;margin-left:10px;">
            面包人
        </div>
    </div>
    <div class="order">
        <div style="display:flex;align-items:center;border-bottom:1px solid #00000011;height:40px">
            <u-icon name="order" size="26"></u-icon><span>我的订单</span>
        </div>
        <div class="order-list">
            <navigator url="/components/order/BookingOrder/BookingOrder" hover-class="navigator-hover">
                <div class="order-list-content">
                    <div>
                        <u--image :showLoading="true" src="/static/mineIcon/weibiaoti-.svg" width="28px" height="28px"></u--image>
                    </div>
                    <div>
                        预定订单
                    </div>
                </div>
            </navigator>

            <navigator url="/components/order/inOrder/inOrder" hover-class="navigator-hover">
                <div class="order-list-content">
                    <div>
                        <u--image :showLoading="true" src="/static/mineIcon/u.svg" width="28px" height="28px"></u--image>
                    </div>
                    <div>
                        入住订单
                    </div>
                </div>
            </navigator>

            <navigator url="/components/order/posOrder/posOrder" hover-class="navigator-hover">
                <div class="order-list-content">
                    <div>
                        <u--image :showLoading="true" src="/static/mineIcon/POSdingdankaipiao.svg" width="28px" height="28px"></u--image>
                    </div>
                    <div>
                        pos订单
                    </div>
                </div>
            </navigator>

            <navigator url="/components/order/foodOrder/foodOrder" hover-class="navigator-hover">
                <div class="order-list-content">
                    <div>
                        <u--image :showLoading="true" src="/static/mineIcon/diancan.svg" width="28px" height="28px"></u--image>
                    </div>
                    <div>
                        点餐订单
                    </div>
                </div>
            </navigator>

            <navigator url="/components/order/serviceOrder/serviceOrder" hover-class="navigator-hover">
                <div class="order-list-content">
                    <div>
                        <u--image :showLoading="true" src="/static/mineIcon/fuwu_o.svg" width="28px" height="28px"></u--image>
                    </div>
                    <div>
                        服务订单
                    </div>
                </div>
            </navigator>

            <navigator url="/components/order/settingsOrder/settingsOrder" hover-class="navigator-hover">
                <div class="order-list-content">
                    <div>
                        <u--image :showLoading="true" src="/static/mineIcon/weixiu.svg" width="28px" height="28px"></u--image>
                    </div>
                    <div>
                        维修订单
                    </div>
                </div>
            </navigator>
        </div>
    </div>

    <div style="background:#fff;margin-top:5px;margin-bottom:20px;">
        <u-cell icon="/static/mineIcon/shoucang.svg" title="收藏酒店" url="/components/collectHotel/collectHotel" :isLink="true" arrow-direction="right"></u-cell>
    </div>

    <div style="background:#fff;">
        <u-cell icon="/static/mineIcon/shoucang.svg" title="常住联系人" url="/components/contacts/contacts" :isLink="true" arrow-direction="right"></u-cell>
        <u-cell icon="/static/mineIcon/wodefapiao.svg" title="我的发票" url="/components/bill/bill" :isLink="true" arrow-direction="right"></u-cell>
        <u-cell icon="/static/mineIcon/dianhua.svg" title="客服电话" :value="'400-0000-0000'" :isLink="true" arrow-direction="right"></u-cell>
        <u-cell icon="/static/mineIcon/guanyu.svg" title="关于我们" url="/components/aboutUs/aboutUs" :isLink="true" arrow-direction="right"></u-cell>
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
		handleToEditUserInfo(){
			uni.navigateTo({
				url:'/components/editUserInfo/editUserInfo'
			})
		}
    }
}
</script>

<style lang="scss" scoped>
.pages {
    background: #f5f5f5;
    width: 100vw;
    height: 100vh;
}

.avatar {
    width: 100vw;
    height: 60px;
    display: flex;
    align-items: center;
    background: #00aaff;
    padding: 20px;
}

.order {
    width: 98vw;
    margin: auto;
    // height: 100px;
    background: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px #00000020;
    margin-top: 3px;
    padding:0 10px 20px 10px;
    box-sizing: border-box;
}

.order-list {
    // display: flex;
    overflow-x: auto;
    // flex-wrap: nowrap;
    margin-top: 20px;
    display: grid;
    grid-template-columns: repeat(6, 20vw);

    .order-list-content {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        // width: 25vw;
        // margin-right: 20px;
    }
}
</style>
