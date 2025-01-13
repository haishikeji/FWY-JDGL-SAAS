<template>
<div style="background-color:#fff;overflow: hidden;" :style="{'border-bottom': borbtm?'1px solid':'none'}">
    <u-navbar title="" @rightClick="rightClick" :autoBack="!isSwitch" placeholder @leftClick="leftClick" :leftIcon="isSwitch? '' : 'arrow-left' ">
        <div slot="center" style="width:80%">
            <uni-data-select :clear="false" style="flex:1" v-model="hotelIds" :localdata="range" @change="change"></uni-data-select>
        </div>
    </u-navbar>
    <!-- <u--input placeholder="房间号/姓名/手机号/身份证号" border="surround" shape="circle" prefixIcon="search" prefixIconStyle="font-size: 22px;color: #909399" fontSize="12px" :customStyle="{border:'1px solid #e2e2e2',padding:'0 10px',margin:'0 10px',borderRadius:'20px'}">
    </u--input> -->
    <slot name="search"></slot>
    <div style="margin:10px 0 6px 0;">
        <!-- <u-subsection :list="list" mode="subsection" :current="current" @change="sectionChange"></u-subsection> -->
        <slot name="section"></slot>
    </div>
</div>
</template>

<script>
import {
    mapState
} from 'vuex'
import {
    getHotelList
} from '../utils/api'
export default {
    props: {
        borbtm: {
            type: Boolean,
            default: true
        },
        isSwitch: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            hotelIds: uni.getStorageSync(('hotelId') || ''),
            value: 0,
            range: [{
                    value: 0,
                    text: "全部"
                }
            ],
        }
    },
    computed: {
        // ...mapState(['hotelIdList','hotelId'])
        ...mapState({
            hotelIdList: state => state.hotelIdList,
            hotelId: state => state.hotelId
        })
    },
    onShow(){
        // this.hotelIds = uni.getStorageSync('hotelId')
    },
    watch: {
        hotelIdList(newVal) {
            console.log('header', newVal);
            this.range = newVal.map(item => {
                return {
                    value: item.id,
                    text: item.name
                }
            })
            // this.hotelIds = newVal[0].id
        },
        hotelId(newVal, oldVal) {
            console.log('header', newVal, oldVal);
            if(newVal != oldVal){
                this.hotelIds = newVal
            }
            this.$emit('change', this.hotelIds)
        }
    },
    async mounted() {
        let data = await getHotelList()
        if (data.code == 200) {
            this.$nextTick(() => {
                this.$store.commit('setHotelIdList', data.result.records)
                // this.hotelIds = data.result.records[0].id
                if (uni.getStorageSync('hotelId')) {
                    this.hotelIds = uni.getStorageSync('hotelId')
                    this.$store.commit('setHotelId', uni.getStorageSync('hotelId'))
                }else{
                    this.hotelIds = data.result.records[0].id
					uni.setStorageSync("hotelId",data.result.records[0].id)
                    this.$store.commit('setHotelId', data.result.records[0].id)
                }
                this.$emit('change', this.hotelIds)
            })
        }
    },
    methods: {
        change(e) {
            console.log(e)
            this.$store.commit('setHotelId', e)
            uni.setStorageSync('hotelId', e)
            console.log(this.hotelId);
            this.$emit('change', e)
        },
        leftClick() {
            if (this.isSwitch) {
                return
            }
            console.log('leftClick');
        },
        rightClick(){}
    }
}
</script>

<style lang="scss">
/deep/.uni-select {
    border: none !important;
    text-align: center;
}
</style>
