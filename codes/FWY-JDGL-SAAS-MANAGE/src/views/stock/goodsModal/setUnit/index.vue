<template>
  <div>
    <a-modal destroyOnClose title="单位设置" :footer='null' closable :visible="unitVisible" @cancel="unitCancel" width="70%" >
        <a-space direction="vertical" style="width:100%">
            <a-space>
                <a-input v-model="queryParam.name" placeholder="单位名称" /><a-button @click="searchQuery" type="primary">查询</a-button>
                <a-button type="primary" @click="addUnit">新增</a-button>
                <a-button type="primary" @click="batchDelUnit">批量删除</a-button>
            </a-space>
            <a-table rowKey="id" :columns="columns" :data-source="dataSource" :row-selection="rowSelection"
             :pagination="ipagination" @change="handleTableChange" :loading="loading">
                <span slot="state" slot-scope="record">
                    {{record?'启用':'停用'}}
                    <!-- {{record}} -->
                </span>
                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">修改</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </a-space>
    </a-modal>
    <!-- 新增和修改弹窗 -->
    <a-modal destroyOnClose :title="titleUnit" :confirm-loading="confirmLoading" closable :visible="addVisible" width="70%" @ok="handleOk" @cancel="handleCancel">
        <a-form-model ref="ruleForm" :model="ruleForm" :rules="rules" v-bind="layout">
            <a-form-model-item has-feedback label="名称" prop="name">
                <a-input v-model="ruleForm.name" autocomplete="off" />
            </a-form-model-item>
            <a-form-model-item has-feedback label="是否启用" prop="">
                <a-switch :defaultChecked="ruleForm.state" @change="switchState" />
            </a-form-model-item>
        </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getUnit, addUnit, editUnit, delUnit, batchDelUnit } from '@/api/good'
const columns = [
  {
    title: '商家',
    dataIndex: 'name',
    key: 'name',
    align:'center'
  },
  {
    title: '单位名称',
    dataIndex: 'name',
    key: 'age',
    align:'center'
  },
  {
    title: '最近更新',
    dataIndex: 'updateAt',
    key: 'updateAt',
    align:'center'
  },
  {
    title: '状态',
    dataIndex: 'state',
    // key: 'address',
    scopedSlots: { customRender: "state" },
    align:'center'
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: "action" },
    align:'center'
  },
];
const rowSelection = {
  onChange: (selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
  },
  onSelect: (record, selected, selectedRows) => {
    console.log(record, selected, selectedRows);
    console.log(this.delDataArr);
  },
  onSelectAll: (selected, selectedRows, changeRows) => {
    console.log(selected, selectedRows, changeRows);
  },
};
const validatePass = (rule, value, callback) => {
    //   if (value === '') {
    //     callback(new Error('Please input the password'));
    //   } else {
    //     if (this.ruleForm.checkPass !== '') {
    //       this.$refs.ruleForm.validateField('checkPass');
    //     }
        // callback();
    //   }
    };
export default {
    props:{
        unitVisible:{
            type:Boolean
        }
    },
    mixins:[JeecgListMixin],
    data(){
        return{
            queryParam:{},
            columns,
            dataSource:[],
            titleUnit:'添加',
            // rowSelection,
            page:1,
            pageSize:10,
            addVisible:false,
            ruleForm:{
                name:'',
                checked:true
            },
            rules:{
                name: [{ required: true, message: "名称不能为空!" }],
                state: [{ validator:validatePass}],
            },
            layout: {
                labelCol: { span: 4 },
                wrapperCol: { span: 14 },
            },
            confirmLoading:false,
            hotelId:null,
            editUnitData:{},
            delDataArr:[],
            //分页参数
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
            url:{
                list:'/rooms/cesGoodsUnit/list'
            }
        }
    },
    created(){

    },
    mounted(){
        this.getData()
        this.hotelId = JSON.parse(localStorage.getItem('storeInfo')).id
    },
    computed:{
        rowSelection(){
            return {
                onChange: (selectedRowKeys, selectedRows) => {
                    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
                    if (selectedRows.length>0) {
                        //获取选中数据的id
                        this.delDataArr = selectedRows.map(i=> i.id).toString()
                        console.log(this.delDataArr);
                    }
                },
                onSelect: (record, selected, selectedRows) => {
                    console.log(record, selected, selectedRows);
                },
                onSelectAll: (selected, selectedRows, changeRows) => {
                    console.log(selected, selectedRows, changeRows);
                },
            }
        }
    },
    methods:{
        getData(){
            getUnit({pageNo:this.page,pageSize:this.pageSize}).then(res=>{
                console.log(res);
                if (res.code && res.code==200) {
                    this.dataSource = res.result.records
                }
            })
        },
        addUnit(){
            this.ruleForm = {name:'', state:false}
            this.addVisible = true
            this.titleUnit = '添加'
        },
        unitCancel(){
            this.$emit('center')
            // this.unitVisible=false
        },
        handleEdit(e){
            this.titleUnit = '修改'
            this.ruleForm.name = e.name
            this.ruleForm.state = e.state
            this.editUnitData = e
            this.addVisible = true
            console.log(e);
        },
        handleDelete(e){
            console.log(e);
            delUnit({id:e}).then(res=>{
                if (res.code && res.code==200) {
                    this.$message.success(res.message)
                    this.getData()
                }else{
                    this.$message.error(res.message)
                }
            })
        },
        handleOk(e) {
            this.confirmLoading = true
            if (this.titleUnit=='添加') {
                addUnit({hotelId:this.hotelId, name:this.ruleForm.name, state:this.ruleForm.state}).then(res=>{
                if (res.code && res.code==200) {
                    this.$message.success(res.message)
                    this.confirmLoading = false
                    this.addVisible=false
                    this.getData()
                }else{
                    this.$message.error(res.message)
                }
            })
            }else{
                this.editUnitData.name = this.ruleForm.name
                this.editUnitData.state = this.ruleForm.state
                editUnit(this.editUnitData).then(res=>{
                    if (res.code && res.code==200) {
                        this.$message.success(res.message)
                        this.confirmLoading = false
                        this.addVisible=false
                    }else{
                        this.$message.error(res.message)
                    }
                })
            }
        },
        handleCancel(e) {
            console.log('点击了取消');
            this.addVisible = false;
        },
        switchState(e){
            console.log(e);
            this.ruleForm.state = e
            console.log(this.ruleForm.state);
        },
        batchDelUnit(){
            this.$confirm({
                title: '提示',
                content: '确认要删除吗',
                okText: '确认',
                cancelText: '取消',
                onOk:(e)=>{
                    console.log(e);
                    return batchDelUnit({idsStr:this.delDataArr}).then(res=>{
                        if (res.code && res.code==200) {
                            this.$message.success(res.message)
                            this.getData()
                        }else{
                            this.$message.error(res.message)
                        }
                    })
                }
            });
        },
    }
}
</script>

<style>
</style>