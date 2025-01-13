<template>
  <a-modal
      :visible="visible"
      title="明细"
      :width="1000"
      @ok="handleOk"
      @cancel="handleOk">
    <div>
      <a-form>
        <a-row>
        </a-row>
      </a-form>
      <a-table
          ref="table"
          size="middle"
          :scroll="{ x: true, y: 500 }"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="goodDate"
          :pagination="ipagination"
          class="storage_collect"
          @change="pageChange">
      </a-table>
    </div>

  </a-modal>
</template>

<script>
import { getAction } from '@api/manage'
import moment from 'moment/moment'

const columns = [
  {
    title: '发生时间',
    align: 'center',
    dataIndex: 'createTime',
    width: 80
  },
  {
    title: '商品名称',
    align: 'center',
    dataIndex: 'goodName',
    width: 80
    // customRender: function (text, record) {
    //   return text == null ? '--' : text
    // },
  },
  {
    title: '标识号',
    align: 'center',
    dataIndex: 'roomSign',
    width: 80
  },
  {
    title: '消费类目',
    align: 'center',
    dataIndex: 'goodTypeName',
    width: 80
    // customRender: function (text, record) {
    //   return text == null ? '--' : text
    // },
  },
  {
    title: '数量',
    align: 'center',
    dataIndex: 'num',
    width: 80
  },
  {
    title: '金额',
    align: 'center',
    dataIndex: 'money',
    width: 80
  },
  {
    title: '类型',
    align: 'center',
    dataIndex: '',
    width: 80,
    customRender: function (text, record) {
      if (record.feeGoodId != null) {
        return '现结'
      } else if (record.feeId != null) {
        return '挂房账'
      }
    }
  },
  {
    title: '购买位置',
    dataIndex: 'placeName',
    align: 'center',
    width: 80
  }
]

export default {
  name: 'SellDetailModel',

  data() {
    return {
      list: '/kc/kcDepositoryInGoods/storagePage',
      columns: columns,
      // 分页参数
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      goodDate: [],
      queryParam: {
        startTime: '',
        endTime: '',
        roomSign: '',
      },
      visible: false

    }
  },
  created() {
  },
  methods: {
    edit(obj) {
      this.visible = true;
      this.queryParam = obj
      this.loadDate()
    },
    loadDate() {
      let params = {
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
        startTime: this.queryParam.startTime,
        endTime: this.queryParam.endTime,
        roomSign: this.queryParam.roomSign,
        placeId: this.queryParam.placeId,
        goodId: this.queryParam.goodId
      }
      getAction('/kc/kcDepositoryInGoods/goodsSellDetailPage', params).then((res) => {
        if (!res.success) {
          this.$message.warning('查询失败')
          return
        }
        this.goodDate = res.result.records
        this.ipagination.pageNo = res.result.current
        this.ipagination.total = res.result.total
        this.expandedRowKeys = []
      }).finally(() => {
        // this.loading = false
      })
    },
    pageChange(page) {
      console.log(page)
      this.ipagination = page
      this.loadDate()
    },
    handleOk(){
      this.visible = false
    }

  }
}
</script>

<style scoped>
.main {
  height: 70% !important;
}
</style>
