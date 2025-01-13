<template>
  <div>
    <a-card style="width: 100%;">
      <a-form>
        <a-row>
          <a-col :span="5">
            <a-form-item
                label="标识号/商品名称"
                :labelCol="{ xs: { span: 24 }, sm: { span: 8 }}"
                :wrapperCol="{xs: { span: 24 },sm: { span: 13 }}">
              <a-input v-model="queryParam.roomSign" placeholder="标识号/商品名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="9">
            <a-form-item label="">
              <a-radio-group v-model="queryParam.timeFrame">
                <a-radio-button value="1">今日</a-radio-button>
                <a-radio-button value="2">昨日</a-radio-button>
                <a-radio-button value="3">本周</a-radio-button>
                <a-radio-button value="4">其他</a-radio-button>
              </a-radio-group>
              <a-range-picker v-if="queryParam.timeFrame === '4'" v-model="chooseTime" />
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-button
                type="primary"
                @click="loadDate"
                icon="search">查询
            </a-button>
          </a-col>
        </a-row>
      </a-form>
      <a-table
          ref="table"
          size="middle"
          :scroll="{ x: true }"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="goodDate"
          :pagination="ipagination"
          class="storage_collect"
          @change="pageChange">
      </a-table>
    </a-card>
  </div>
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
    align: 'center',
    dataIndex: 'placeName',
    width: 80,
  }
]

export default {
  name: 'SellDetail',

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
        timeFrame: '1',
        startTime: '',
        endTime: '',
        roomSign: '',
      },
      chooseTime: [],

    }
  },
  created() {
    this.loadDate()
    getAction('/business/busHousePriceSchemeLayout/layoutPricePage', ).then(resp => {

    })
  },
  methods: {
    loadDate() {
      if (this.queryParam.timeFrame === '4' && this.chooseTime.length === 0) {
        this.$message.warning('请选择时间')
        return
      }
      this.getTimeFrame()
      let params = {
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
        startTime: this.queryParam.startTime,
        endTime: this.queryParam.endTime,
        roomSign: this.queryParam.roomSign
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
    // 获取时间范围
    getTimeFrame() {
      if (this.queryParam.timeFrame === '1') {
        // 今天
        this.queryParam.startTime = moment().format('YYYY-MM-DD')
        this.queryParam.endTime = moment().add(1, 'days').format('YYYY-MM-DD')
      } else if (this.queryParam.timeFrame === '2') {
        // 昨天
        this.queryParam.endTime = moment().format('YYYY-MM-DD')
        this.queryParam.startTime = moment().subtract(1, 'days').format('YYYY-MM-DD')
      } else if (this.queryParam.timeFrame === '3') {
        // 本周
        this.queryParam.startTime = moment().weekday(0).format('YYYY-MM-DD')
        this.queryParam.endTime = moment().weekday(6).format('YYYY-MM-DD')
      } else if (this.queryParam.timeFrame === '4') {
        // 自定义时间
        this.queryParam.startTime = this.chooseTime[0].format('YYYY-MM-DD')
        this.queryParam.endTime = this.chooseTime[1].format('YYYY-MM-DD')
      }
    },

  }
}
</script>

<style scoped>
.main {
  height: 70% !important;
}
</style>
