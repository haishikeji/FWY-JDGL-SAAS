<template>
    <a-spin :spinning="confirmLoading">
        <j-form-container :disabled="formDisabled">
            <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
                <a-form-model-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
                    <a-input v-model="model.name" placeholder="请输入名称"></a-input>
                </a-form-model-item>
                <a-form-model-item label="上级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="">
                    <a-select  style="width: 120px" :default-value="beforeTree.editData?beforeTree.parentId:beforeTree.id" @change="onChangeSelect" >
                        <!-- :defaultValue='beforeTree.id' -->
                        <a-select-option value="0">{{"顶级目录"}}</a-select-option>
                        <a-select-option v-for="d in selectData" :key="d.value" :value="d.id">
                            {{ d.name }}
                        </a-select-option>
                    </a-select>
                </a-form-model-item>
                
                <!-- <a-form-model-item label="快捷" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="canLivePersonNum">
                    <a-switch default-checked @change="onChange"  v-model="model.isQuick" />
                </a-form-model-item> -->

                <a-form-model-item label="是否启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="breakfastNum">
                    <a-switch default-checked @change="onChange"  v-model="model.state" />
                </a-form-model-item>

                <!-- <a-form-model-item label="应用范围" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lunchNum">
                    <a-checkbox-group v-model="model.applyScope" :options="plainOptions" @change="onChange" />
                </a-form-model-item>
                <a-form-model-item v-show="model.applyScope.includes('2')" label="POS类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="posType">
                    <a-checkbox :indeterminate="indeterminatePos" :checked="posCheckAll" @change="onCheckAllChangePos">
                        全选
                    </a-checkbox>
                    <a-checkbox-group :disabled='beforeTree.parentId==0?false:true' v-model="model.posType" :options="posOptions" @change="onChangePos" />
                </a-form-model-item>
                <a-form-model-item v-show="model.applyScope.includes('3')" label="娱乐类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="funType">
                    <a-checkbox :indeterminate="indeterminateFun" :checked="funCheckAll" @change="onCheckAllChangeFun">
                        全选
                    </a-checkbox>
                    <a-checkbox-group :disabled='beforeTree.parentId==0?false:true' v-model="model.funType" :options="funOptions" @change="onChangeFun" />
                </a-form-model-item>
                <a-form-model-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dinnerNum">
                    <a-input-number style="width:50%;" v-model="model.sort" :min="0"  placeholder="请输入排序"/>
                </a-form-model-item> -->
            </a-form-model>

        </j-form-container>
    </a-spin>

</template>
<script>
import { getRoomPlans, getSelectList } from "@/api/api";
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import { getStockTypeList, getPosTypeList } from '@/api/good'

export default {
    name: "BusMarketMemberForm",
    inject:{
        beforeTree:{
            default:{id:0}
        }
    },
    props: {
        disabled: {

            
            type: Boolean,
            default: false,
            required: false,
        },
    },
    data() {
        return {
            indeterminatePos: true,
            indeterminateFun:true,
            posCheckAll: false,
            funCheckAll:false,
            plainOptions:[
                { label: '客房', value: '1' },
                { label: 'POS', value: '2' },
                { label: '娱乐', value: '3' },
            ],
            posOptions:[
               { label: '茶室', value: '1' },
                { label: '餐厅', value: '2' },
                { label: '超市', value: '3' },
            ],
            funOptions:[
               { label: '茶室', value: '1' },
                { label: '餐厅', value: '2' },
                { label: '超市', value: '3' },
            ],
            selectData:[],
            model: {
                // id: "",
                name: null, //名字
                hotelId: 0, //酒店id
                parentId: 0, //上级id
                tenantId:"",
                // marketPrice: null,
                // canLivePersonNum: true,
                // breakfastNum: 0,
                // isQuick:0, // 是否快捷
                state:1, // 是否启用
                // lunchNum: 0,
                // checkedList: ["1"],
                // applyScope:['1'],//应用范围
                // posType:[], //应用范围 pos
                // funType:[], //应用范围 娱乐
                // sort:0
            },
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            confirmLoading: false,
            validatorRules: {
                name: [{ required: true, message: "请输入名称!" }],
                marketPrice: [{ required: false, message: "请填写门市价!" }],
                canLivePersonNum: [{ required: false, message: "请填写可住人数!" }],
                breakfastNum: [{ required: false, message: "请填写早餐数量!" }],
                lunchNum: [{ required: false, message: "请填写中餐数量!" }],
                isQuick: [{required: false, message: ""}],
                sort: [{ required: false, message: "请填写晚餐数量!" }],
            },
            url: {
                add: "/kc/kcGoodsType/create",
                edit: "/kc/kcGoodsType/modify",
                queryById: "/rooms/cesRoomLayout/queryById",
            },
            iconChooseVisible: false,
            roomPlans: [],
            members: [],
        };
    },
    computed: {
        formDisabled() {
            return this.disabled;
        },
    },
    created() {
        var _info = JSON.parse(localStorage.getItem("storeInfo"));
        if (_info) {
            this.model.hotelId = _info.id;
            this.initData();
        }
        console.log(this.beforeTree);
        this.model.parentId = this.beforeTree.id
        if (this.beforeTree && this.beforeTree.editData) {
            this.model.name = this.beforeTree.name
            // this.model.hotelId = this.beforeTree.hotelId
            this.model.parentId = this.beforeTree.parentId
            this.model.id = this.beforeTree.id
            // this.model.hotelId = this.beforeTree
            // this.model.hotelId = this.beforeTree
            // this.model.hotelId = this.beforeTree
            // this.model.hotelId = this.beforeTree
        }
        this.modelDefault = JSON.parse(JSON.stringify(this.model));
        this.getAlldata()
    },
    mounted(){
        // console.log(this.beforeTree);
    },
    methods: {
        /**
         * 获取初始数据
         */
        getAlldata(){
            getStockTypeList().then(res=>{
                if (res.success) {
                    this.selectData = res.result;
                }
            })
            getPosTypeList().then(res=>{
                if (res.success) {
                    this.posOptions = res.result.records.map(item=>{
                        return {
                            label:item.name,
                            value:item.id
                        }
                    })
                }
            })
        },
        /**
         * 选择器选中时
         */
        onChangeSelect(val, options){
            console.log(val);
            console.log(options);
            this.model.parentId = val
        },
        onChangePos(checkedList){
            this.indeterminatePos = !!checkedList.length && checkedList.length < this.posOptions.length;
            this.posCheckAll = checkedList.length === this.posOptions.length;
        },
        onChangeFun(checkedList){
            this.indeterminateFun = !!checkedList.length && checkedList.length < this.funOptions.length;
            this.funCheckAll = checkedList.length === this.funOptions.length;
        },
        onChange(checkedList) {
           if(checkedList.length == 0){
              this.$message.error("应用范围必须选择一项!");
              this.model.applyScope.push("1")
           }
         
        },
        onCheckAllChangePos(e) {
            this.model.posType = e.target.checked ? this.posOptions.map(v => v.value) : []
            this.indeterminatePos = false
            this.posCheckAll = e.target.checked
            // Object.assign(this, {
            //     model:{
            //         posType: e.target.checked ? this.posOptions.map(v => v.value) : []
            //     },
            //     indeterminatePos: false,
            //     ,
            // });
        },
        onCheckAllChangeFun(e) {   
            this.model.funType = e.target.checked ? this.funOptions.map(v => v.value) : []
            this.indeterminateFun = false
            this.funCheckAll = e.target.checked
            // Object.assign(this, {
            //     model:{
            //         funType: e.target.checked ? this.funOptions.map(v => v.value) : []
            //     },
            //     indeterminateFun: false,
            //     funCheckAll: e.target.checked,
            // });
        },
        initData() {
            getRoomPlans(this.model.hotelId, null).then((res) => {
                if (res.success) {
                    this.roomPlans = res.result;
                }
            });

        },
        selectIcons() {
            this.iconChooseVisible = true;
        },
        handleIconCancel() {
            this.iconChooseVisible = false;
        },
        handleIconChoose(value) {
            console.log(value);
            this.model.icon = value;
            this.iconChooseVisible = false;
        },
        add() {
            this.edit(this.modelDefault);
        },
        edit(record) {
            this.model = Object.assign({}, record);
            this.visible = true;
            getSelectList({ id: this.model.id }).then((res) => {
                if (res.success) {
                    this.members = res.result;
                }
            });
        },
        submitForm() {
            const that = this;
            // 触发表单验证
            
            this.$refs.form.validate((valid) => {
                if (valid) {
                    that.confirmLoading = true;
                    let httpurl = "";
                    let method = "";
                    if (!this.model.id) {
                        httpurl += this.url.add;
                        method = "post";
                    } else {
                        httpurl += this.url.edit;
                        method = "put";
                    }
                    if (this.model.payFlag == 0) {
                        this.model.payAmount = 0;
                    }
                    // that.model.applyScope = that.model.applyScope.toString()
                    // that.model.posType = that.model.posType.toString()
                    // that.model.funType = that.model.funType.toString()
                    // that.model.isQuick = that.model.isQuick?true:false
                    that.model.state = that.model.state?true:false
                    httpAction(httpurl, this.model, method)
                        .then((res) => {
                            if (res.success) {
                                that.$message.success(res.message);
                                that.$emit("ok");
                            } else {
                                that.$message.warning(res.message);
                            }
                        })
                        .finally(() => {
                            that.confirmLoading = false;
                        });
                }
            });
        },
    },
};
</script>
<style scoped>
.avatar-uploader>.ant-upload {
    width: 104px;
    height: 104px;
}
</style>