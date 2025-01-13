<template>
    <div>
        <a-table
                ref="table"
                size="middle"
                :scroll="{x:true}"
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                class="j-table-force-nowrap"
                @change="handleTableChange">
        </a-table>
    </div>
</template>

<script>
    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'

    export default {
        name: 'commodityRecord',
        mixins:[JeecgListMixin, mixinDevice],
        props: {
            agreementOrderFeeId:{
                type: String,
                required: false,
                default: () => {
                }
            }
        },
        data(){
          return {
              // 表头
              columns: [
                  {
                      title:'科目',
                      align:"center",
                      dataIndex: 'categoryId_dictText',
                  },
                  {
                      title:'分类',
                      align:"center",
                      dataIndex: 'categoryTwoId_dictText',
                  },
                  {
                      title:'名称',
                      align:"center",
                      dataIndex: 'goodsId_dictText',
                  },
                  {
                      title:'单价',
                      align:"center",
                      dataIndex: 'shopPrice',
                      sorter: (a, b) => a.shopPrice - b.shopPrice,
                  },
                  {
                      title:'数量',
                      align:"center",
                      dataIndex: 'buyNumber',
                      sorter: (a, b) => a.buyNumber - b.buyNumber,
                  },
                  {
                      title:'购买人',
                      align:"center",
                      dataIndex: 'purchaserName',
                  },
                  {
                      title:'总价格',
                      align:"center",
                      dataIndex: 'amount',
                  },
                  {
                      title:'操作员',
                      align:"center",
                      dataIndex: 'createBy'
                  },
                  {
                      title:'备注',
                      align:"center",
                      dataIndex: 'remarks'
                  }
              ],
              url: {
                  list: "/business/busCommodityRecord/list?agreementOrderFeeId="+this.agreementOrderFeeId,
              },
          }
        },
        created() {
            console.log(this.agreementOrderFeeId)
        },
        methods:{
            show(){

            }
        }
    }
</script>

<style scoped>

</style>
