<template>
    <div class="back">
        <div class="" style="">
            <a-card :bordered="false">
                <!-- 查询区域 -->
                <!-- <div class="table-page-search-wrapper">
                    <a-form layout="inline" @keyup.enter.native="searchQuery">
                        <a-row :gutter="24">
                            <a-col :span="4">
                                <a-form-item label="">
                                    <j-input placeholder="商品名称" v-model="queryParam.name" style="width: 200px"></j-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="13" :sm="8" style="display: flex; justify-content: space-around;">
                                <span style="float:left; overflow: hidden" class="table-page-search-submitButtons">
                                    <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                </span>
                            </a-col>
                        </a-row>
                    </a-form>
                </div> -->
                <!-- 查询区域-END -->
                <div>
                    <a-table ref="table" size="middle" :scroll="{ x: '100%' }" bordered rowKey="id" :columns="columns"
                        :dataSource="dataSource" :pagination="ipagination" :loading="loading" :rowSelection="{
                            selectedRowKeys: selectedRowKeys,
                            onChange: onSelectChange,
                        }" class="j-table-force-nowrap" @change="handleTableChange">
                        <span slot="state" slot-scope="record">
                            {{record?'启用':'停用'}}
                            <!-- {{record}} -->
                        </span>
                        <span slot="action" slot-scope="text, record">
                            <!-- <a @click="handleEdit(record)">修改</a> -->
                            <!-- <a-divider type="vertical" /> -->
                            <a @click="ok(record)">选择</a>
                            <!-- <a-divider type="vertical" />
                            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                <a>删除</a>
                            </a-popconfirm> -->
                        </span>
                    </a-table>
                </div>
            </a-card>
        </div>
        <!-- 单位设置区域 -->
        <!-- <SetUnit :unitVisible="unitVisible" @center="handleCancel" /> -->
    </div>

</template>
<script>
import { tree, delTree, goodBatchDel, goodSet } from '@/api/good'
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// import roomLayoutForm from './goodStock/goods'
// import index from '/src/index.vue'
// import stockTypeModel from './stockTypeModel.vue'
// import Commodity from './moreSet/commodity.vue'
// import GoodImg from './moreSet/goodImg.vue';
// import moreSet from './moreSet/moreModal.vue'
import { computed } from 'vue';
// import SetUnit from './setUnit/index.vue'


const tabListNoTitle=[
        {
          key: 'commodity',
          tab: '商品小程序设置',
        },
        {
          key: 'goodImg',
          tab: '商品图片',
        },
        {
          key: 'project',
          tab: '餐饮设置',
        },
        {
          key: 'game',
          tab: '娱乐设置',
        },
      ]
export default {
    name: "goodList",
    mixins: [JeecgListMixin],
    components: {
        // stockTypeModel,
        // roomLayoutForm,
        // SetUnit,
        // moreSet,
        // index
        // Commodity,
        // GoodImg
      },
    props:{
        agreementId:{},
    },
    data() {
        return {
            key: 'tab1',
            noTitleKey: 'commodity',

            checkedKeys: [''],
            tabListNoTitle,
            selectedKeys: [],
            queryParam: {},
            //单位对话框
            unitVisible:false,
            // 分页参数
            ipagination: {
                current: 1,
                pageSize: 10,
                pageSizeOptions: ["10", "20", "30"],
                showTotal: (total, range) => {
                    return range[0] + "-" + range[1] + " 共" + total + "条";
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0,
            },
            // 表头
            columns: [
                {
                    title: "协议类型",
                    align: "center",
                    dataIndex: "type_dictText",
                    customCell:() => {
                        return {
                        style: {
                            wordWrap:'break-word',
                            wordBreak:'break-all',
                            whiteSpace:'normal',
                            minHeight:'50px',
                            width: '50px',
                        }
                        }
                    }
                },
                {
                    title: "协议主题",
                    align: "center",
                    dataIndex: "name",
                    // customCell:() => {
                    //     return {
                    //     style: {
                    //         wordWrap:'break-word',
                    //         wordBreak:'break-all',
                    //         whiteSpace:'normal',
                    //         minHeight:'50px',
                    //         width: '50px',
                    //     }
                    //     }
                    // }
                },
                {
                    title: "协议单号",
                    align: "center",
                    dataIndex: "number",
                    // customCell:() => {
                    //     return {
                    //     style: {
                    //         wordWrap:'break-word',
                    //         wordBreak:'break-all',
                    //         whiteSpace:'normal',
                    //         minHeight:'50px',
                    //         width: '50px',
                    //     }
                    //     }
                    // }
                },
                {
                    title: "签约时间",
                    align: "center",
                    dataIndex: "signTime",
                },
                {
                    title: "折扣值",
                    align: "center",
                    dataIndex: "discount",
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    key: 'action',
                    scopedSlots: { customRender: "action" },
                    align:'center',
                    // fixed: "right",
                    // width: 200,
                }
            ],
            url: {
                // list: 'org.jeecg.modules.business/busMarketMember/list',
                list:'/business/busMarketAgreementCustomer/list?agreementId=' + this.agreementId,
                // list: "/rooms/cesRoomLayout/list?hotelId",
                // delete: "/rooms/cesRoomLayout/remove",
                delete:'/rooms/cesGoods/delete',
                // deleteBatch: "/rooms/cesRoomLayout/deleteBatch",
                deleteBatch:'/rooms/cesGoods/delBatch',
                exportXlsUrl: "/rooms/cesRoomLayout/exportXls",
                importExcelUrl:"rooms/cesRoomLayout/importExcel",
            },
            dictOptions: {},
            superFieldList: [],
            selectedRowKeys: [],
            isorter: {
                column: "createTime",
                order: "desc",
            },
            stockTypeVisible:false,
            // 选中的数据
            beforeTree:null,
            goodsSetVisible:false,//进退货弹窗
            moreSetVisible:false,//更多设置弹窗
            goodsSetData:{
                number:0,
                type:1
            }
        };
    },
    computed: {

    },
    watch: {

    },
    created() {
        this.loadTree();
    },
    provide(){
        return {
            beforeTree:computed(() => this.beforeTree?this.beforeTree:{id:'0'}),
            treeData:computed(()=> this.treeData),
            dataSource:computed(()=> this.dataSource)
        }
    },
    methods: {
        // handleStockTypeModelManager(item,type) {
        //     this.$refs.indexTable.visible = true
        //     return
        //     console.log(item, type)
        //     this.beforeTree = null
        //     if (item!='-1') {
        //         this.beforeTree = item
        //         this.beforeTree.editData = type ===2?true:false
        //     }
        //     console.log(this.beforeTree);
        //     this.$refs.stockTypeModel.title = type === 1 ?"新增":"修改"
        //     this.$refs.stockTypeModel.visible = true
        // },
        loadTree() {
            var that = this
            tree().then((res) => {
                if (res.success) {
                    this.treeData = res.result;
                }
            })
        },
        ok(record){
            this.$emit('ok', record)
        },
        onExpand(expandedKeys) {
            console.log('onExpand', expandedKeys);
            // if not set autoExpandParent to false, if children expanded, parent can not collapse.
            // or, you can remove all expanded children keys.
        },
        onSelect(selectedKeys, info) {
            console.log('onSelect', info);
            this.selectedKeys = selectedKeys;
        },
        /**
         * 保存成功
         */
        onSave(){
            this.loadTree()
        },
        /**
         * 删除
         */
        confirmDel(id){
            delTree({id:id}).then(res=>{
                if (res.code && res.code==200) {
                    this.$message.success('删除成功')
                    this.loadTree()
                }
            })
        },
        //批量删除
        goodBatchDel(){
            console.log(this.selectedRowKeys);
            if (this.selectedRowKeys.length==0) {
                this.$message.info('请先选择要删除的数据')
                return
            }
            this.$confirm({
                title: '提示',
                content: '确认要删除吗',
                okText: '确认',
                cancelText: '取消',
                onOk:(e)=>{
                    return goodBatchDel({idStr:this.selectedRowKeys.toString()}).then(res=>{
                        if (res.code && res.code==200) {
                            this.$message.success(res.message)
                            this.loadData();
                        }else{
                            this.$message.error(res.message)
                        }
                    })
                }
            });
        },
        //单位设置区域
        handleOk(e) {
            // this.confirmLoading = true;
            // setTimeout(() => {
            //     this.visible = false;
            //     this.confirmLoading = false;
            // }, 2000);
            this.unitVisible=false
        },
        handleCancel(e) {
            console.log('Clicked cancel button',e);
            this.unitVisible = false;
        },
        /**
         * 进退货
         */
        goodsSet(){
            this.goodsSetData = this.dataSource.filter(item=> item.id==this.selectedRowKeys.toString() )[0]
            console.log(this.goodsSetData);
            this.goodsSetVisible = true
        },
        goodChange(e){
            console.log(e);
            this.goodsSetData.type = e.target.value
        },
        //更多设置
        moreSet(record){
            console.log(this.$refs.modalSet);
            this.$refs.modalSet.edit(record)
            this.$refs.modalForm.disableSubmit = false;
            // this.moreSetVisible = true
        },
        //更多设置确认
        handleMoreSetOk(){

        },
        //更多设置切换卡片
        onTabChange(key, type) {
            console.log(key, type);
            this[type] = key;
        },

        //进退货确认事件
        handleGoodOk(){
            console.log(this.goodsSetData);
            if (!this.goodsSetData.type) {
                this.goodsSetData.type = 1
            }
            let obj = {
                hotelId:this.goodsSetData.hotelId,
                goodId:this.goodsSetData.id,
                type:this.goodsSetData.type,
                goodNum:this.goodsSetData.goodNum,
                remark:this.goodsSetData.remark?this.goodsSetData.remark:''
            }
            // this.goodsSetData.goodId = this.goodsSetData.id
            goodSet(obj).then(res=>{
                if (res.code && res.code==200) {
                            this.$message.success(res.message)
                            this.goodsSetVisible = false
                            // this.goodsSetData = {}
                            this.loadData();
                        }else{
                            this.$message.error(res.message)
                        }
            })

        },
        //进退货取消
        handleGoodCancel(){
            this.goodsSetVisible = false
        }
    },
};
</script>
<style scoped>
.back {
    display: flex;
    justify-content: space-between;
}

.back-left {
    /* flex: 1 */
}

.back-right {
    flex: 1;
}

.ant-tree li .ant-tree-node-content-wrapper {
    height: auto !important;
}

.ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
    background-color: #bae7ff33;
}</style>