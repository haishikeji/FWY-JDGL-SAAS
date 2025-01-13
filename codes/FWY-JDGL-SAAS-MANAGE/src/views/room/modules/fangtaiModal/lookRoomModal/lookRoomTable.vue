<template>
<a-spin :spinning="confirmLoading">
    <j-form-container>
        <a-table :dataSource="dataSource" :columns="columns" :pagination="false">
            <template slot="action" slot-scope="text, record">
                <a v-if="record.state==0 || record.state==4" @click="handleLookRoom(record)">申请查房</a>
                <a v-if="record.state==1" @click="cancelRoom(record)">取消查房</a>
            </template>
            <template slot="livingStatus">
                正常入住
            </template>
        </a-table>
    </j-form-container>

    <a-modal :title="'申请查房'" :visible="visibleLook" @cancel="visibleLook = false" @ok="submitOK">
        <!-- <j-form-container :disabled="false"> -->
            <a-form-model-item label="请输入备注">
                <a-input v-model="model.remarks" placeholder="请输入备注"></a-input>
            </a-form-model-item>
        <!-- </j-form-container> -->
    </a-modal>
</a-spin>
</template>

<script>
import {
    httpAction,
    getAction
} from "@/api/manage";
import {
    validateDuplicateValue
} from "@/utils/util";
// import {
//     JeecgListMixin
// } from '@/mixins/JeecgListMixin'

export default {
    // mixins: [JeecgListMixin],
    name: "PosTableForm",
    components: {},
    props: {
        //表单禁用
        disabled: {
            type: Boolean,
            default: true,
            required: false,
        },
    },
    data() {
        return {
            visibleLook: false,
            queryParam: {
                hotelId: null,
                livingOrderId: null,
            },
            dataSource: [],
            columns: [{
                title: '房号',
                align: "center",
                dataIndex: 'roomName'
            }, {
                title: '客人姓名',
                align: "center",
                dataIndex: 'contactName'
            }, {
                title: '房间状态',
                align: "center",
                dataIndex: 'roomStatus',
            }, {
                title: '入住状态',
                align: "center",
                dataIndex: '',
                scopedSlots: {
                    customRender: 'livingStatus'
                },
            }, {
                title: '查房状态',
                align: "center",
                dataIndex: 'state',
                customRender: function (text) {
                    let txt = "";
                    switch (text) {
                        case 0:
                            txt = "待查房";
                            break;
                        case 1:
                            txt = "等待查房";
                            break;
                        case 2:
                            txt = "正在查房";
                            break;
                        case 3:
                            txt = "查房完毕";
                            break;
                        case 4:
                            txt = "取消查房";
                            break;
                        default:
                            break;
                    }
                    return txt;
                },
                //   sorter: true
            }, {
                title: '操作',
                key: 'operation',
                align: "center",
                width: 130,
                scopedSlots: {
                    customRender: 'action'
                },
            }],
            model: {
                hotelId: null,
                remarks:'',
            },
            labelCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 5
                },
            },
            wrapperCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 16
                },
            },
            confirmLoading: false,
            validatorRules: {
                tenantId: [{
                    required: true,
                    message: "请输入关联租户!"
                }],
                qz: [{
                    required: true,
                    message: "请输入餐桌名前缀!"
                }],
                hotelId: [{
                    required: true,
                    message: "请输入关联酒店!"
                }],
                name: [{
                    required: true,
                    message: "请输入餐桌名称!"
                }],
                posTypeId: [{
                    required: true,
                    message: "请输入pos类型id!"
                }],
                posTableTypeId: [{
                    required: true,
                    message: "请输入pos桌型id!"
                }],
                posRegionId: [{
                    required: true,
                    message: "请输入pos餐桌区域id!"
                }],
                num: [{
                    required: true,
                    message: "请输入座位数!"
                }],
                tableNo: [{
                    required: true,
                    message: "请输入餐桌序号!"
                }],
                state: [{
                    required: true,
                    message: "请输入状态!"
                }],
            },
            url: {
                list: "",
                edit: "/pos/posTable/edit",
                queryById: "/pos/posTable/queryById",
            },
            posTypeList: [],
            tableTypeList: [],
            regionList: [],
            selectData:null,
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
        }
        //备份model原始值
        this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
        add() {
            this.edit(this.modelDefault);
        },
        loadData() {
            getAction(`/fw/fwRoomExamine/list?livingOrderId=${this.model.roomInfo.livingOrderId}`).then(res => {
                console.log(res);
                if (res.success) {
                    this.dataSource = res.result.records;
                } else {
                    this.$message.warning(res.message);
                }
            })
        },
        edit(record) {
            this.model = Object.assign({}, record);
            this.queryParam.livingOrderId = record.roomInfo.livingOrderId;
            this.loadData()

            this.visible = true;
        },
        submitOK(){
            this.confirmLoading = true;
            console.log(this.model);
            httpAction("/fw/fwRoomExamine/checkRoom", {
                id: this.selectData.id,
                livingOrderId: this.selectData.livingOrderId,
                remark: this.model.remarks,
                state: 1,
            },'post').then(res => {
                this.confirmLoading = false;
                if (res.success) {
                    this.$message.success("申请成功");
                    this.loadData()
                    this.visibleLook = false;
                } else {
                    this.$message.warning(res.message);
                }
            })
        },
        handleLookRoom(record) {
            console.log(record);
            this.selectData = record
            this.visibleLook = true;
        },
        cancelRoom(record){
            this.confirmLoading = true;
            httpAction("/fw/fwRoomExamine/checkRoom", {
                id: record.id,
                livingOrderId: record.livingOrderId,
                // remark: record.remarks,
                state: 4,
            },'post').then(res => {
                this.confirmLoading = false;
                if (res.success) {
                    this.$message.success("取消成功");
                    this.loadData()
                } else {
                    this.$message.warning(res.message);
                }
            })
        }
    },
};
</script>
