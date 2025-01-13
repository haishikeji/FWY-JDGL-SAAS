<template>
  <storage-collect-info :is-in="true" :columns="columns" :list="list"></storage-collect-info>
</template>

<script>
import storageCollectInfo from './storageCollectInfo'
import { getAction } from '@api/manage'
export default {
  name: 'storageInCollect',
  components: {
    storageCollectInfo
  },
  data() {
    return {
      list: '/kc/kcDepositoryInGoods/storageDetailPage',
      columns: [
        {
          title: '仓库',
          align: 'center',
          dataIndex: 'depositoryName',
          width: 80
        },
        {
          title: '入库类型',
          align: 'center',
          dataIndex: 'stockType',
          width: 80,
          customRender(text, record) {
            console.log(text)
            if (text == null && record.feeId != null) {
              return '销售退货'
            }
            return text
          }
        },
        {
          title: '商品名称',
          align: 'center',
          dataIndex: 'goodName',
          width: 80
        },
        {
          title: '商品规格',
          align: 'center',
          dataIndex: 'spec',
          width: 80
        },
        {
          title: '商品分类',
          align: 'center',
          dataIndex: 'goodTypeName',
          width: 80
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'goodUnitName',
          width: 80
        },
        {
          title: '入库时间',
          align: 'center',
          dataIndex: 'createTime',
          width: 80
        },
        {
          title: '单价',
          align: 'center',
          dataIndex: 'price',
          width: 80,
          customRender(text, record) {
            if(record.depositoryName !== '合计') {
              return text
            }
          }
        },
        {
          title: '入库数量',
          align: 'center',
          dataIndex: 'num',
          width: 80
        },
        {
          title: '入库金额',
          align: 'center',
          width: 80,
          customRender(text, record) {
            if(record.price !== undefined) {
              return record.price * record.num
            }
          }
        },
        {
          title: '申请人',
          align: 'center',
          dataIndex: 'createAt',
          width: 80
        },
        {
          title: '审批人',
          align: 'center',
          dataIndex: 'verifyAt',
          width: 80
        }
        // {
        //   title: '预警状态',
        //   align: 'center',
        //   dataIndex: 'id',
        //   customRender(text, record) {
        //     return record.stockWarning >= record.num ? '预警' : '正常'
        //   }
        // }
      ]
    }

  },
  created() {
    getAction('/kc/kcStock/storageTypeList', {}).then((res) => {
      if (res.success) {
        this.storageTypeList = res.result;
        let map = new Map()
        res.result.forEach(e => {
          map.set(e.value, e.text)
        })
        this.storageTypeMap = map
      }
    });
  },
  methods: {
  }
};
</script>

<style scoped>
.main {
  height: 70% !important;
}
</style>
