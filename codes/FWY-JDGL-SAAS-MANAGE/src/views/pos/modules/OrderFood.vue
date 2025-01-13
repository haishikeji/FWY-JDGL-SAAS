<template>
        <a-drawer width="100%" title="预定菜品" placement="right" :visible="foodOpen" @close="onClose">
            <a-tabs v-model="tabPosTypeId">
                <a-tab-pane
                    :key="item.id"
                    :tab="item.name"
                    v-for="item in posTypeList"
                ></a-tab-pane>
            </a-tabs>
            <a-row :gutter="24">
            <a-col :span="8">
                <!-- <a-table :columns="columns" :data-source="merchandiseData" >
                </a-table> -->
                <a-table
                    ref="table"
                    size="middle"
                    :scroll="{ x: true }"
                    bordered
                    rowKey="id"
                    :columns="columns"
                    :dataSource="merchandiseData"
                    :row-selection="rowSelection"
                    class="j-table-force-nowrap"
                >
                    <template slot="num" slot-scope="text, record, index">
                        <a-input-number
                            v-if="record.id == null"
                            v-model="record.num"
                            style="width: auto; "
                            :max="record.inventory"
                            @change="(event) => numChange(event, index)"/>
                        <span v-else>{{ record.num }}</span>
                    </template>
                    <template slot="money" slot-scope="text, record, index">
                        ￥{{ (record.num * record.money).toFixed(2) }}
                    </template>
                </a-table>
              <a-button @click="cleanTable">清空</a-button>
              <a-button @click="chooseOk">确定</a-button>
            </a-col>
            <a-col :span="16">
                <a-tabs v-model="tabgoodsTypeId">
                    <a-tab-pane key="1" tab="全部"></a-tab-pane>
                    <a-tab-pane
                        :key="item.id"
                        :tab="item.name"
                        v-for="item in goodsTypeList"
                    ></a-tab-pane>
                    <!-- <a-tab-pane key="1" tab="4"> -->

                    <!-- </a-tab-pane> -->
                </a-tabs>
                <a-row :gutter="[5, 5]">
                    <a-col :span="3"
                    v-for="cust in dataSource"
                    :key="cust.id"
                    @click="addMerchandise(cust)">
                        <div class="dish_style" v-if="cust.id != '0'">
                            <div class="price_name_style">{{ cust.name }}</div>
                            <div class="price_style">￥{{ cust.sellingPrice.toFixed(2) }}</div>
                            <div class="stock_style">库存{{ cust.inventory }}</div>
                            <div
                                v-if="cust.inventory <= 0"
                                style="display: flex; justify-content: right">
                            <a-tag color="#f50"> 沽清 </a-tag>
                            </div>
                        </div>
                    </a-col>
                </a-row>
            </a-col>
          </a-row>
        </a-drawer>
        <!-- <a-modal
            v-model:open="foodOpen"
            title="Basic Modal"
            width="100%"
            wrap-class-name="full-modal"
            @ok="handleOk"
            >
            <p>Some contents...</p>
            <p>Some contents...</p>
            <p>Some contents...</p>
        </a-modal> -->
</template>

<script>
  import { httpAction, getAction, postAction } from '@/api/manage'

  export default {
    name: 'OrderFood',
    components: {},
    props: {
    },
    data() {
      return {
        // 商品列表
        merchandiseData: [],
        // 控制预定点餐弹框
        foodOpen: false,
        tabPosTypeId: '',
        tabgoodsTypeId: '1',
        columns: [
            {
                title: '商品名称',
                dataIndex: 'name',
            },
            {
                title: '数量',
                dataIndex: 'num',
                scopedSlots: { customRender: 'num'}
            },
            {
                title: '售价',
                dataIndex: 'money',
                scopedSlots: { customRender: 'money'}
            },
        ],
        dataSource: [],
        // 大tab栏切换数据
        posTypeList: [],
        // 中tab栏切换数据
        goodsTypeList: [],
        dataSource2: [],
      }
    },
    computed: {
        // 计算每一样商品的金额
        amount() {
            return this.merchandiseData.reduce(function (total, item) {
                return total + item.sellingPrice * item.num
            }, 0)
        },
        sum() {
            return this.merchandiseData.reduce(function (total, item) {
                return total + item.num
            }, 0)
        },
        rowSelection() {
            return {
                onChange: (selectedRowKeys, selectedRows) => {
                console.log(
                    `selectedRowKeys: ${selectedRowKeys}`,
                    'selectedRows: ',
                    selectedRows
                )
                this.selectedRowKeys = selectedRowKeys
                this.selectedRows = selectedRows
                },
                getCheckboxProps: (record) => ({
                props: {
                    disabled: record.name === 'Disabled User', // Column configuration not to be checked
                    name: record.name
                }
                })
            }
        },
    },
    created() {
        this.dataSource = [
            {
                id: '0'
            }
        ]
        getAction('/pos/posType/list', {
            pageNo: 1,
            pageSize: 99
            }).then((res) => {
                console.log(res);
            if (res.success) {
                // console.log();
                this.posTypeList = res.result.records
                if (this.posTypeList && this.posTypeList.length > 0) {
                this.tabPosTypeId = this.posTypeList[0].id
                this.loadGoodsType()
                }
            }
        })
    },
    methods: {
      // 商品数量变化
      numChange(val, index) {
        if (val <= 0) {
            this.merchandiseData.splice(index, 1)
        }
      },
      rowSelection() {

      },
      cleanTable() {
        this.merchandiseData = []
        this.$emit('ok', [])
      },
      chooseOk() {
        this.$emit('ok', this.merchandiseData)
        this.foodOpen = false
      },
      // 点击添加商品
      addMerchandise(val) {
        if (val.inventory <= 0) {
            this.$message.warning('此商品库存不足')
            return
        }
        let index = this.merchandiseData.findIndex(e => e.goodsId === val.id)
        if (index === -1) {
            let obj = {
              goodsId: val.id,
              name: val.name,
              money: val.sellingPrice,
              num: 1
            }
            this.merchandiseData.push(obj)
        } else {
          this.merchandiseData[index].num++
        }
      },
      // 请求商品列表数据
      loadGoods() {
        var ids = []
        if (this.tabgoodsTypeId === '1') {
            this.goodsTypeList.forEach((t) => {
            ids.push(t.id)
            })
        } else {
            ids.push(this.tabgoodsTypeId)
        }
        this.dataSource = [

        ]
        getAction('/pos/posSellClearGoods/list', {
            pageNo: 1,
            pageSize: 99999,
            goodTypes: ids
        }).then((res) => {
            console.log(res);
            if (res.success) {
            this.dataSource = [...this.dataSource, ...res.result.records]
            }
        })
        getAction('/pos/posSellClearGoods/thali-list', {
            pageNo: 1,
            pageSize: 99999,
            goodTypes: ids
        }).then((res) => {
            console.log(res);
            if (res.success) {
            this.dataSource2 = res.result.records
            }
        })
      },
      loadGoodsType() {
        getAction('/rooms/cesStockType/getTopTypesByPosType', {
            posType: this.tabPosTypeId
        }).then((res2) => {
            console.log(res2);
            if (res2.success) {
            this.goodsTypeList = res2.result
            console.log(this.goodsTypeList);
            if (this.goodsTypeList && this.goodsTypeList.length > 0) {
                this.loadGoods()
            }
            }
        })
        },
      handleOk() {

      },
      // 关闭预定点餐
      onClose() {
        this.foodOpen = false
      },
    // 预定点餐
      edit(orderGoodsDetailList) {
        this.foodOpen = true
        if (orderGoodsDetailList == null) {
          return
        }
        this.$nextTick(() => {
          this.merchandiseData = []
          orderGoodsDetailList.forEach(e => {
            let obj = {
              goodsId: e.id,
              name: e.goodsName,
              money: e.money,
              num: e.num
            }
            this.merchandiseData.push(obj)
          })
        })
      },
    }
  }
</script>
<style scoped lang="less">
.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }
  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh) !important;
  }
  .ant-modal-body {
    flex: 1;
  }
}
.dish_style {
    border: 3px solid #000;
    border-radius: 5%;
    height: 131px;
}
.price_style {
    margin-top: 10px;
    margin-bottom: 10px;
    color: red;
    margin-left: 5px;
}
.stock_style {
    margin-top: 10px;
    margin-bottom: 10px;
    color: #2c2f3b;
    margin-left: 5px;
}
.price_name_style {
    margin-top: 10px;
    margin-left: 5px;
}
::v-deep .ant-table-bordered.ant-table-empty .ant-table-placeholder {
    height: 65vh !important;
}
</style>
