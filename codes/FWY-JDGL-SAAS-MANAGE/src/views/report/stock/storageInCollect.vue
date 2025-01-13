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
      list: '/kc/kcDepositoryInGoods/storagePage',
      columns: [
        {
          title: '仓库名称',
          align: 'center',
          dataIndex: 'depositoryName',
          width: 80
        },
        {
          title: '商品名称',
          align: 'center',
          dataIndex: 'goodName',
          width: 80
        },
        {
          title: '商品分类',
          align: 'center',
          dataIndex: 'goodTypeName',
          width: 80

        },
        {
          title: '库存量',
          align: 'center',
          dataIndex: 'num',
          width: 80
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'goodUnitName',
          width: 80
        },
        // {
        //   title: '预警状态',
        //   align: 'center',
        //   dataIndex: 'id',
        //   customRender(text, record) {
        //     return record.stockWarning >= record.num ? '预警' : '正常'
        //   }
        // }
      ],
    }

  },
  created() {

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
