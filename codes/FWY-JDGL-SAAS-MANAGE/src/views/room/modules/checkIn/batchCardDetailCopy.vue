<template>
<div>
    <a-card :title="roomList.length > 1 ? '批量制卡' : '制卡'" style="width: 100%">
        <div slot="extra" style="display:flex;align-items:center;">
            <a-switch v-model="isChecked"></a-switch>制卡完毕自动跳转下一张
        </div>
        <p>
            <a-row type="flex" justify="center">
                <a-col :span="12">
                    <div style="display:flex;flex-direction:column;justify-content:center;align-items:center;">
                      <h3 style="color:rgba(42, 130, 228, 1)">
                        当前房间号
                      </h3>
                      <div style="color:#fff;background: rgba(42, 130, 228, 1);line-height:100px;width:40%;text-align:center;border-radius:12px;">
                        {{roomList[roomIndex] && roomList[roomIndex].name}}
                      </div>
                    </div>
                </a-col>
                <a-col :span="12">
                    <div style="display:flex;flex-direction:column;justify-content:center;height:100%;">
                        <div style="display:flex;align-items:center;justify-content:space-between">
                            <div>
                                入住时长：{{ roomList[roomIndex] && roomList[roomIndex].dayCount }}天
                            </div>
                            <div>
                                已制卡：{{'--'}}张
                            </div>
                        </div>
                        <div>
                            <!-- 入住时间:{{roomList[roomIndex] && roomList[roomIndex].Info.arrivalTime2}} -->
                            入住时间：{{ roomList[roomIndex] && roomList[roomIndex].arriveTime }}
                        </div>
                        <div>
                            <!-- 预离时间:{{roomList[roomIndex] && roomList[roomIndex].Info.dueOutTime2}} -->
                            预离时间：{{ roomList[roomIndex] && roomList[roomIndex].dueOutTime }}
                        </div>
                    </div>
                </a-col>
            </a-row>
        </p>
        <p>
            <a-row :gutter="[6,6]">
                <a-col :span="5">
                    <a-button @click="changeErr">复制卡</a-button>
                </a-col>
                <a-col :span="4">
                    <a-button @click="changeErr">读卡</a-button>
                </a-col>
                <a-col :span="4">
                    <a-button @click="changeErr">注销</a-button>
                </a-col>
              <a-col :span="5">
                <a-button @click="makeCard">制卡</a-button>
              </a-col>
                <a-col :span="5" v-if="roomIndex>0">
                    <a-button @click="handleCutRoom">上一张</a-button>
                </a-col>
                <a-col :span="5" v-if="roomIndex<roomList.length-1">
                    <a-button @click="handleAddRoom">下一张</a-button>
                </a-col>
            </a-row>
        </p>
        <p>
            <a-table
                  ref="table"
                  size="middle"
                  :row-selection="{
                     type: 'radio',
                     selectedRowKeys: selectedRowKeys,
                     onChange: onSelectChange,
                     }"
                  :scroll="{ x: '100%',y:250 }"
                  style="height: 250px"
                  bordered
                  rowKey="id"
                  :columns="columns"
                  :dataSource="roomList"
                  :pagination="ipagination"
                  :loading="loading"
                  class="j-table-force-nowrap">
                <span slot="state" slot-scope="text">
                    {{text?'已制卡':''}}
                    <!-- {{record}} -->
                </span>
                <!-- <template slot="num" slot-scope="text, record, index">
                    {{ (ipagination.current-1)*ipagination.pageSize+index+1}}
                </template> -->
                <span slot="action" slot-scope="text, record">
                    <a @click="ok(record)">选择</a>
                </span>
            </a-table>
        </p>
    </a-card>
</div>
</template>

<script>
export default {
    data() {
        return {
            // 表头
            columns: [
                // {
                //     title: "序号",
                //     align: "center",
                //     loading:false,
                //     width: 60,
                //     scopedSlots: {
                //         customRender: 'num'
                //     }
                // },
                {
                    title: "房间号",
                    align: "center",
                    dataIndex: "name",
                },
                {
                    title: "房型",
                    align: "center",
                    dataIndex: "layoutName",
                },
                {
                    title: "住客姓名",
                    align: "center",
                    dataIndex: "customerName",
                },
                {
                    title: "手机号码",
                    align: "center",
                    dataIndex: "phone",
                },
                {
                    title: "",
                    align: "center",
                    dataIndex: "iscard",
                    scopedSlots: { customRender: 'state' }
                }
            ],
            ipagination:{},
            dataSource:[],
            loading:false,
            isChecked: true,
            roomList:[],
            roomIndex:0,
            selectedRowKeys: [],
            selectedRows: []
        }
    },
    methods: {

        onSelectChange(selectedRowKeys, selectionRows) {
          this.selectedRowKeys = selectedRowKeys
          this.selectedRows = selectionRows
        },
        makeCard(){
          if (this.selectedRowKeys.length > 0) {
            let index = this.roomList.findIndex(e => e.id === this.selectedRowKeys[0])
            console.log(this.roomList)
            console.log(index)
            if (index !== -1) {
              this.roomList[index].iscard = true
              //
              if (this.isChecked) {
                if (index < this.roomList.length - 1) {
                  this.selectedRowKeys = [this.roomList[index + 1].id]
                } else {
                  this.selectedRowKeys = [this.roomList[0].id]
                }
              }
              this.$forceUpdate()

            }
          }
        },
        changeErr(){
            this.$message.error('接口程序未打开，请打开接口程序')
        },
        handleAddRoom(){
            this.roomIndex++;
            this.selectedRowKeys = [this.roomList[this.roomIndex].id]
        },
        handleCutRoom(){
            this.roomIndex--;
            this.selectedRowKeys = [this.roomList[this.roomIndex].id]

        },
        edit(record) {
            console.log(record, 'record')
          if (record.length > 0) {
              this.selectedRowKeys = [record[0].id]
            }
          this.roomList = record;
          this.roomList.forEach(e => {
            if (!e.arriveTime || !e.dueOutTime) {
              return
            }
            let dayCount = this.daysBetween(e.arriveTime, e.dueOutTime)
            e.dayCount = dayCount
          })
        },
        submitForm() {
            this.$emit('ok')
        },
        daysBetween(date1, date2) {
          console.log(date1)
          console.log(date2)
          const oneDay = 24 * 60 * 60 * 1000 // hours*minutes*seconds*milliseconds
          const firstDate = new Date(date1)
          const secondDate = new Date(date2)
          if (secondDate < firstDate) {
            this.$message.warning('离开时间不能小于预离时间')
            return
          }
          const diffDays = Math.round(Math.abs((firstDate - secondDate) / oneDay))
          if (diffDays == 0) {
            return 1
          }
          return diffDays
      },
    }
}
</script>

<style scoped>
/deep/.ant-table-thead > tr > th {
  background: rgba(42, 130, 228, 1);
  color: #ffffff;
}
/deep/.ant-divider-horizontal {
  margin: 12px 0 !important;
}
/deep/ .ant-table-tbody .ant-table-row td {
  padding-top: 5px;
  padding-bottom: 5px;
}
/deep/.ant-table-thead > tr > th,
.ant-table-tbody > tr > td {
  padding: 5px 5px !important;
  overflow-wrap: break-word;
}
</style>
