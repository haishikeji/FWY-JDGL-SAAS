<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="">
        <a-row :gutter="24">
          <a-col :span="3">
            <a-form-item label="">
              <a-select
                v-model="queryParam.depositoryId"
                style="width: 100%"
                placeholder="仓库"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in depositoryList"
                  :key="index"
                  :value="item.id"
                >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-input
                placeholder="商品名称"
                v-model="queryParam.goodsName"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button
                type="primary"
                @click="loadDate"
                icon="search"
              >查询</a-button
              >
              <a-button
                type="primary"
                icon="reload"
                style="margin-left: 8px"
              >重置</a-button
              >
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
<!--    <div class="table-operator">-->
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--    </div>-->

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div> -->

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="goodDate"
        :pagination="ipagination"
        @change="pageChange"
        @expand="loadInnerData"
        :expandedRowKeys="expandedRowKeys"
      >
        <a-table
          size="small"
          slot="expandedRowRender"
          slot-scope="record"
          :columns="innerColumns"
          :dataSource="record.kcDepositoryInGoods"
          :pagination="record.ipagination"
          rowKey="id"
          @change="innerPageChange($event, record)"
        >
        </a-table>
      </a-table>
    </div>
<!--    <depository-modal ref="modalForm" @ok="modalFormOk"></depository-modal>-->
  </a-card>
</template>

  <script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { filterObj } from '@/utils/util'
import { getAction } from '@/api/manage'
import DepositoryModal from './modules/DepositoryModal.vue'
export default {
  name: 'MemberList',
  // mixins: [JeecgListMixin],
  components: {
    DepositoryModal
  },
  data() {
    return {
      goodDate: [],
      queryParam: {},
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
      innerPagination: {
        current: 1,
        pageSize: 2,
        pageSizeOptions: ['1', '2', '3'],
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      // 表头
      columns: [
        {
          title: '商品名称',
          align: 'center',
          dataIndex: 'name'
        },
        // {
        //   title: "仓库名称",
        //   align: "center",
        //   dataIndex: "depositoryName",
        // },
        {
          title: '商品规格',
          align: 'center',
          dataIndex: 'spec'
        },
        {
          title: '库存预警数',
          align: 'center',
          dataIndex: 'stockWarning'
        },
        {
          title: '库存量',
          align: 'center',
          dataIndex: 'inventory'
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'unitName'
        },
        {
          title: '预警状态',
          align: 'center',
          dataIndex: 'id',
          customRender(text, record) {
            return record.stockWarning >= record.num ? '预警' : '正常'
          }
        }
      ],
      innerColumns: [
        {
          title: '单号',
          align: 'center',
          dataIndex: 'stockCode'
        },
        {
          title: '仓库名称',
          align: 'center',
          dataIndex: 'depositoryName'
        },
        {
          title: '进货量',
          align: 'center',
          dataIndex: 'num'
        },
        {
          title: '价格',
          align: 'center',
          dataIndex: 'price'
        }
      ],

      url: {
        list: '/rooms/cesGoods/list',
        delete: '/kc/kcDepositoryInGoods/delete',
        deleteBatch: '/kc/kcDepositoryInGoods/deleteBatch',
        exportXlsUrl: '/kc/kcDepositoryInGoods/exportXls',
        importExcelUrl: '/kc/kcDepositoryInGoods/importExcel'
      },
      dictOptions: {},
      superFieldList: [],
      selectedRowKeys: [],
      isorter: {
        column: 'createTime',
        order: 'desc'
      },
      expandedRowKeys: [],
      depositoryList: []
    }
  },
  created() {
    this.loadDate()
  },
  // mounted() {
  //   setTimeout(() => {
  //     this.goodDate.forEach(item => {
  //       this.$set(item, 'ipagination', this.innerPagination)
  //     })
  //     console.log(this.dataSource,'this,dateSource')
  //   }, 500)
  //
  // },
  methods: {
    loadDate() {
      let params = {
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
        name: this.queryParam.goodsName,
      }
      getAction(this.url.list, params).then((res) => {
        if (!res.success) {
          this.$message.warning('查询失败')
          return
        }
        res.result.records.forEach(item => {
          this.$set(item, 'ipagination', this.innerPagination)
        })
        this.goodDate = res.result.records
        this.ipagination.pageNo = res.result.current
        this.ipagination.total = res.result.total
        this.expandedRowKeys = []
      }).finally(() => {
        // this.loading = false
      })
    },
    loadInnerData(expanded, record) {
      if (!expanded) {
        this.expandedRowKeys = this.expandedRowKeys.filter(item => item !== record.id)
        return
      }
      this.expandedRowKeys.push(record.id)
      console.log(record)
      let parmas = {
        pageNo: record.ipagination.current,
        pageSize: record.ipagination.pageSize,
        goodsId: record.id,
        depositoryId: this.queryParam.depositoryId
      }
      getAction('/kc/kcDepositoryInGoods/list', parmas).then(resp => {
        if (!resp.success) {
          this.$message.warning('查询失败')
          return
        }
        let index = this.goodDate.findIndex(e => e.id === record.id)
        if (index === -1) {
          return
        }
        this.goodDate[index].kcDepositoryInGoods = resp.result.records
        this.$set(this.goodDate[index], 'kcDepositoryInGoods', resp.result.records)
        this.goodDate[index].ipagination.current = resp.result.current
        this.goodDate[index].ipagination.total = resp.result.total
        this.goodDate[index].ipagination.pageSize = resp.result.size
      })
    },
    innerPageChange(page, item) {
      console.log(page)
      item.ipagination = page
      this.loadInnerData(true, item)
    },
    pageChange(page) {
      console.log(page)
      this.ipagination = page
      this.loadDate()
    }
  }
}
</script>
  <style scoped>
@import "~@assets/less/common.less";
</style>
