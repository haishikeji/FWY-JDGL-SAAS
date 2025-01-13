<template>
<a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
        <a-form-model ref="form" :model="model" layout="inline" :rules="validatorRules" slot="detail">
            <!-- <a-row :gutter="2" align="top" type="flex"> -->
            <!-- <a-col :span="5" flex> -->
            <a-form-item label="盘点仓库" prop="depositoryList">
                <a-select :disabled="flag" v-model="model.depositoryId" style="width: 200px" placeholder="盘点仓库" :allowClear="true">
                    <a-select-option v-for="(item, index) in depositoryList" :key="index" :value="item.id">{{ item.name }}</a-select-option>
                </a-select>
            </a-form-item>
            <!-- </a-col> -->
            <!-- <a-col :span="5" flex> -->
            <a-form-item label="盘点单号" prop="depositoryList">
                <a-input disabled v-model="model.code"></a-input>
            </a-form-item>
            <a-form-item label="" prop="depositoryList">
                <a-button :disabled="!model.depositoryId" @click="handleGoods" type="primary" icon="plus">添加商品</a-button>
            </a-form-item>
            <!-- </a-col> -->
            <!-- <a-col :span="5">
                    <a-form-item label="" prop="supplierList">
                        <a-select v-model="model.supplierId" style="width: 100%" placeholder="供应商" :allowClear="true">
                            <a-select-option v-for="(item, index) in supplierList" :key="index" :value="item.id">{{ item.name }}</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col> -->
            <!-- <a-col :span="5">
                    <a-form-item label="" prop="storageTypeList">
                        <a-select v-model="model.stockType" style="width: 100%" placeholder="入库类型" :allowClear="true">
                            <a-select-option v-for="(item, index) in storageTypeList" :key="index" :value="item.value">{{ item.title }}</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col> -->
            <!-- <a-col :span="6"> -->

            <!-- </a-col> -->
            <!-- </a-row> -->
        </a-form-model>
    </j-form-container>

    <!-- 查询区域-END -->
    <div>
        <a-table :scroll="{ y: 540 }" ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="false" :loading="loading" class="j-table-force-nowrap" @change="handleTableChange">
            <span slot="state" slot-scope="record">
                {{ record ? "启用" : "停用" }}
                <!-- {{record}} -->
            </span>
            <!-- <template slot="price" slot-scope="record">
                <a-input-number :min="0" :placeholder="record.costPrice" v-model="record.costPrice"></a-input-number>
            </template> -->
            <template slot="num" slot-scope="record">
                <a-input-number :disabled="flag" :min="0" :max="record.stockNum" v-model="record.num"></a-input-number>
            </template>
            <span slot="action" slot-scope="text, record">
                <!-- <a @click="handleEdit(record)">修改</a>
                <a-divider type="vertical" />
                <a @click="moreSet(record)">更多设置</a>
                <a-divider type="vertical" /> -->
                <a-popconfirm title="确定删除吗?" @confirm="() => deleteGoods(record.id)">
                    <a :disabled="flag">删除</a>
                </a-popconfirm>
            </span>
        </a-table>
    </div>

    <a-row>
        <a-col :span="12">
            <a-form-item label="备注" :label-col="labelCol">
                <a-textarea :disabled="flag" v-model="model.remarks" :rows="4" />
            </a-form-item>
        </a-col>
    </a-row>

    <supplier-in-goods-modal ref="modalSelectGoodsForm" @ok="selectOk"></supplier-in-goods-modal>
</a-spin>
</template>

<script>
import {
    httpAction,
    getAction,
    postAction
} from "@/api/manage";
import {
    validateDuplicateValue
} from "@/utils/util";
import {
    JeecgListMixin
} from "@/mixins/JeecgListMixin";
import SupplierInGoodsModal from "./selectGoodsModal/SelectGoodsModal.vue";
export default {
    name: "PosTypeForm",
    mixins: [JeecgListMixin],
    components: {
        SupplierInGoodsModal,
    },
    props: {
        //表单禁用
        disabled: {
            type: Boolean,
            default: false,
            required: false,
        },
    },
    watch: {
        "model.supplierId": function (newVal, oldVal) {
            if (newVal != oldVal) {
                this.dataSource = [];
            }
        }
    },
    data() {
        return {
            model: {
                code: "",
                createAt: "",
                createTime: "",
                // depositoryId: "",
                hotelId: "",
                id: "",
                remarks: "",
                status: 0,
                num: 0,
                // stockInfo: "",
                stockType: 0,
                // supplierId: "",
                tenantId: "",
                type: 3,
                verifyAt: "",
                verifyRemarks: "",
                verifyTime: "",
            },
            flag: false,
            labelCol: {
                xs: {
                    span: 24,
                },
                sm: {
                    span: 5,
                },
            },
            wrapperCol: {
                xs: {
                    span: 24,
                },
                sm: {
                    span: 16,
                },
            },
            confirmLoading: false,
            validatorRules: {
                depositoryList: [{
                    required: true,
                    message: "请选择入库仓库!",
                }, ],
                supplierList: [{
                    required: true,
                    message: "请选择供应商!",
                }, ],
                storageTypeList: [{
                    required: true,
                    message: "请选择入库类型!",
                }, ],
            },
            // 表头
            columns: [
                // {
                //     title: "商品条码",
                //     align: "center",
                //     dataIndex: "code",
                // },
                {
                    title: "商品名称",
                    align: "center",
                    dataIndex: "name",
                },
                {
                    title: "单位",
                    align: "center",
                    dataIndex: "goodUnit",
                },
                {
                    title: "商品规格",
                    align: "center",
                    dataIndex: "spec",
                },
                {
                    title: "库存数量",
                    align: "center",
                    dataIndex: "stockNum",
                    scopedSlots: {
                        customRender: "stockNum"
                    },
                },
                {
                    title: "实际库存",
                    align: "center",
                    // dataIndex: "num",
                    scopedSlots: {
                        customRender: "num"
                    },
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    key: 'action',
                    scopedSlots: {
                        customRender: "action"
                    },
                    align: 'center',
                }
            ],
            url: {
                list: 2,
                add: "/kc/kcStock/add",
                edit: "/kc/kcStock/edit",
                queryById: "/kc/kcDepository/queryById",
            },
            depositoryList: [],
            supplierList: [],
            storageTypeList: [],
            dataSource: []
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
        getAction("/kc/kcDepository/list", {
            state: 1,
        }).then((res) => {
            if (res.success) {
                this.depositoryList = res.result.records;
            }
        });
        getAction("/kc/kcSupplier/list", {
            state: 1,
        }).then((res) => {
            if (res.success) {
                this.supplierList = res.result.records;
            }
        });
        getAction("/kc/kcStock/storageTypeList", {}).then((res) => {
            if (res.success) {
                this.storageTypeList = res.result;
            }
        });
    },
    methods: {
        handleGoods(row) {
            this.$refs.modalSelectGoodsForm.add();
            this.$refs.modalSelectGoodsForm.title = "供货商品";
            this.$refs.modalSelectGoodsForm.disableSubmit = false;
            this.$refs.modalSelectGoodsForm.supplierId = this.model.supplierId;
        },
        /**
         * 删除商品
         */
        deleteGoods(id) {
            this.dataSource = this.dataSource.filter(item => item.id !== id)
        },
        selectOk(e) {
            console.log(e);
            this.dataSource = JSON.parse(JSON.stringify(e))
            console.log(this.dataSource);
        },
        add() {
            this.edit(this.modelDefault, 1);
        },
        edit(record, type) {
            console.log(type);
            if (typeof type == 'boolean') {
                this.flag = type
            }
            if (type===1) {
                return
            }
            this.model = Object.assign({}, record);
            this.model.stockType = this.model.stockType.toString()
            console.log(this.model);
            getAction(`/kc/kcStockDetail/list?stockId=${this.model.code}`).then(res => {
                console.log(res);
                if (res.code == 200) {
                    this.dataSource = res.result.records
                    this.dataSource.forEach(ele => {
                        ele.costPrice = ele.price
                    })
                }
            })
            this.visible = true;
        },
        submitForm(flag) {
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
                    this.model.stockDetailList = this.dataSource
                    this.model.stockDetailList.forEach(element => {
                        if (!element.supplierInGoodsId) {
                            element.supplierInGoodsId = element.id;
                        }
                        // element.supplierInGoodsId = element.supplierInGoodsId;
                        element.price = 0;
                        element.num = Number(element.num);
                    });
                    httpAction(httpurl, this.model, method)
                        .then((res) => {
                            if (res.success) {
                                that.$emit("ok");
                                that.$message.success(res.message);
                                if (flag) {
                                    // this.model.status = 1
                                    // postAction("/kc/kcStock/audit", this.model).then((res) => {
                                    //     if (res.success) {
                                    //         // this.loadData();
                                    //         that.$emit("ok");
                                    //         this.$message.success("审核成功");
                                    //         // this.examineVisible = false;
                                    //     }
                                    // });
                                }
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
