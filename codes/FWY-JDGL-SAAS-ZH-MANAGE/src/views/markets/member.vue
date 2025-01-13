<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="3">
            <a-form-item label="">
              <a-select
                mode="multiple"
                style="width: 180px"
                placeholder="商家名称"
                :maxTagCount="1"
                :maxTagTextLength="50"
                v-model="queryParam.hotelIds"
              >
                <a-select-option
                  v-for="(item, index) in hotelList"
                  :key="index"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="会员名称"
                v-model="queryParam.name"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="会员手机号"
                v-model="queryParam.mobile"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <j-input
                placeholder="会员卡号"
                v-model="queryParam.cardNo"
              ></j-input>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-select
                v-model:value="queryParam.gradeId"
                style="width: 100%"
                placeholder="会员等级"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in gradeList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-select
                v-model:value="queryParam.groupId"
                style="width: 100%"
                placeholder="会员分组"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in groupList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="">
              <a-select
                v-model:value="queryParam.labelId"
                style="width: 100%"
                placeholder="会员标签"
                :allowClear="true"
              >
                <a-select-option
                  v-for="(item, index) in labelList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="">
              <a-range-picker
                format="YYYY-MM-DD"
                :placeholder="['注册开始日期', '注册结束日期']"
                @change="onChange"
                v-model="datetime"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search"
                >查询</a-button
              >
              <a-button
                type="primary"
                @click="searchReset"
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
    <div
      class="table-operator"
      style="display: flex; justify-content: space-between"
    >
      <div>
        <a-button @click="handleAdd" type="primary" icon="plus">新增会员</a-button>
        <a-button @click="batchDel" type="danger" icon="minus"
          >批量删除</a-button
        >
        <a-button
          type="primary"
          icon="download"
          @click="handleExportXls('会员列表')"
          >导出</a-button
        >
        <!-- <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload> -->
        <a-button type="primary" icon="import" @click="importExcelModal">导入</a-button>

      </div>
      <div>
        <a-switch @change="onSwitchChange" />
      </div>
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>-->
    </div>
    <div v-show="showCard">
      <a-row :gutter="24">
        <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
          <div class="ant-card ant-card-h">
            <div class="ant-card-body" style="padding: 20px 24px 8px">
              <div class="chart-card-header">
                <div class="meta">
                  <span class="chart-card-title">今日过生日的会员</span>
                </div>
                <div class="total" style="color: #ff5400">
                  <span>{{ stat.birthdayCount || 0 }}</span
                  ><span style="font-size: 15px">位</span>
                </div>
              </div>
              <div class="chart-card-content">
                <div class="content-fix">
                  <div>
                    <div style="margin-right: 16px">
                      本月还有<span style="color: #ff5400">{{
                        stat.monthBirthdayCount || 0
                      }}</span
                      >位会员生日
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a-col>

        <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
          <div class="ant-card ant-card-h">
            <div class="ant-card-body" style="padding: 20px 24px 8px">
              <div class="chart-card-header">
                <div class="meta">
                  <span class="chart-card-title">会员总数</span>
                </div>
                <div class="total" style="color: #ff5400">
                  <span>{{ stat.count || 0 }}</span
                  ><span style="font-size: 15px">位</span>
                </div>
              </div>
              <div class="chart-card-content">
                <div class="content-fix">
                  <div>
                    <div style="margin-right: 16px">
                      今日新增<span style="color: #ff5400">{{
                        stat.dayCount || 0
                      }}</span
                      >位会员
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a-col>

        <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
          <div class="ant-card ant-card-h">
            <div class="ant-card-body" style="padding: 20px 24px 8px">
              <div class="chart-card-header">
                <div class="meta">
                  <span class="chart-card-title">累计消费</span>
                </div>
                <div class="total" style="color: #ff5400">
                  <span>0.00</span><span style="font-size: 15px">元</span>
                </div>
              </div>
              <div class="chart-card-content">
                <div class="content-fix">
                  <div>
                    <div style="margin-right: 16px">
                      今日已有<span style="color: #ff5400">0</span
                      >位会员消费<span style="color: #ff5400">0.00</span>元
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a-col>

        <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
          <div class="ant-card ant-card-h">
            <div class="ant-card-body" style="padding: 20px 24px 8px">
              <div class="chart-card-header">
                <div class="meta">
                  <span class="chart-card-title">消费会员(TOP3)</span>
                </div>
              </div>
              <div class="chart-card-content">
                <div class="content-fix">
                  <div>
                    <div style="margin-right: 16px">暂无</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="avatar" slot-scope="text">
          <a-avatar v-if="text" :src="text" />
          <a-avatar v-else icon="user" />
        </template>
        <template slot="imgSlot" slot-scope="text, record">
          <span v-if="!text" style="font-size: 12px; font-style: italic"
            >无图片</span
          >
          <img
            v-else
            :src="getImgView(text)"
            :preview="record.id"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic"
            >无文件</span
          >
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)"
          >
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handlePointEdit(record)">积分调整</a>
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleTopUp(record)">充值</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm
                  title="确定删除吗?"
                  @confirm="() => handleDelete(record.id)"
                >
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
      <!-- <a-select
        style="width: 200px"
        placeholder="请选择会员分组"
        :disabled="selectedRowKeys.length <= 0"
        v-model="groupId"
        @change="groupChange"
      >
        <a-select-option
          v-for="(item, index) in groupList"
          :key="index"
          :value="item.id"
          >{{ item.name }}</a-select-option
        >
      </a-select>
      <a-select
        style="width: 200px; margin-left: 10px"
        placeholder="请选择会员标签"
        :disabled="selectedRowKeys.length <= 0"
        v-model="labelId"
        @change="labelChange"
      >
        <a-select-option
          v-for="(item, index) in labelList"
          :key="index"
          :value="item.id"
          >{{ item.name }}</a-select-option
        >
      </a-select> -->
    </div>
    <bus-member-card-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></bus-member-card-modal>
    <bus-member-top-up-modal ref="modalMemberTopUp" @ok="modalFormOk"> </bus-member-top-up-modal>
    <bus-member-point-log-modal
      ref="modalMemberPointLogForm"
      @ok="modalFormOk"
    ></bus-member-point-log-modal>
    <bus-member-card-import-excel-modal
      ref="modalBusMemberCardImportExcelModal"
      @ok="modalFormOk"
    ></bus-member-card-import-excel-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { httpAction } from "@/api/manage";
import BusMemberCardModal from "./modules/memberCard/BusMemberCardModal";
import BusMemberPointLogModal from "./modules/memberPoint/BusMemberPointLogModal";
import BusMemberCardImportExcelModal from "./modules/memberCard/BusMemberCardImportExcelModal";
import BusMemberTopUpModal from '@views/markets/modules/memberCard/BusMemberTopUp'
const hotelInfo = JSON.parse(localStorage.getItem("storeInfo"));
export default {
  name: "BusMemberList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BusMemberTopUpModal,
    BusMemberCardModal,
    BusMemberPointLogModal,
    BusMemberCardImportExcelModal,
  },
  data() {
    return {
      showCard: false,
      groupId: undefined,
      labelId: undefined,
      description: "会员页面",
      datetime: [],
      // 表头
      columns: [
        {
          title: "头像",
          align: "center",
          dataIndex: "avatar",
          scopedSlots: { customRender: "avatar" },
        },
        // {
        //   title: "商家",
        //   align: "center",
        //   dataIndex: "hotelName",
        // },
        {
          title: "会员姓名",
          align: "center",
          dataIndex: "name",
        },
        {
          title: "性别",
          align: "center",
          dataIndex: "sex",
          customRender: function (text) {
            return text === 1 ? "男" : "女";
          },
        },
        {
          title: "手机号",
          align: "center",
          dataIndex: "mobile",
        },
        {
          title: "证件号",
          align: "center",
          dataIndex: "certificateNo",
        },
        {
          title: "会员卡号",
          align: "center",
          dataIndex: "cardNo",
        },
        {
          title: "等级类型",
          align: "center",
          dataIndex: "gradeName",
        },
        {
          title: "分组",
          align: "center",
          dataIndex: "groupName",
        },
        {
          title: "创建日期",
          align: "center",
          dataIndex: "createTime",
        },
        {
          title: "可用积分",
          align: "center",
          dataIndex: "integral",
          customRender: function (text) {
            return text || 0;
          },
        },
        {
          title: "可用余额",
          align: "center",
          dataIndex: "balance",
          customRender: function (text) {
            return text || 0;
          },
        },
        {
          title: "推荐员工",
          align: "center",
          dataIndex: "staffId",
        },
        {
          title: "状态",
          align: "center",
          // dataIndex: "labelName",
        },
        {
          title: "标签？",
          align: "center",
          dataIndex: "labelName",
        },

        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list:
          hotelInfo && hotelInfo.id
            ? "/business/busMemberCard/list?hotelId=" + hotelInfo.id
            : "/business/busMemberCard/list",
        delete: "/business/busMemberCard/delete",
        deleteBatch: "/business/busMemberCard/deleteBatch",
        exportXlsUrl: "/business/busMemberCard/exportXls",
        importExcelUrl:
          "business/busMemberCard/importExcel?hotelId=" + hotelInfo.id,
      },
      dictOptions: {},
      superFieldList: [],
      hotelList: [],
      gradeList: [],
      groupList: [],
      labelList: [],
      stat: {},
    };
  },
  created() {
    // this.getSuperFieldList();
    httpAction(
      "/business/busHotel/list",
      { pageNo: 1, pageSize: 100 },
      "get"
    ).then((res) => {
      if (res.success) {
        this.hotelList = res.result.records;
      }
    });
    httpAction(
      "/org.jeecg.modules.business/busMarketMember/list",
      { pageNo: 1, pageSize: 100 },
      "get"
    ).then((res) => {
      if (res.success) {
        this.gradeList = res.result.records;
      }
    });
    httpAction(
      "/business/busMarketMemberGroup/list",
      { pageNo: 1, pageSize: 100 },
      "get"
    ).then((res) => {
      if (res.success) {
        this.groupList = res.result.records;
      }
    });
    httpAction(
      "/business/busMarketMemberLable/list",
      { pageNo: 1, pageSize: 100 },
      "get"
    ).then((res) => {
      if (res.success) {
        this.labelList = res.result.records;
      }
    });
    httpAction("/business/busMemberCard/stat", {}, "get").then((res) => {
      if (res.success) {
        this.stat = res.result;
      }
    });
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    importExcelModal() {
      this.$refs.modalBusMemberCardImportExcelModal.add();
      this.$refs.modalBusMemberCardImportExcelModal.title = "导入会员";
      this.$refs.modalBusMemberCardImportExcelModal.disableSubmit = true;
    },
    searchQuery() {
      if (this.queryParam.hotelIds && this.queryParam.hotelIds.length > 0) {
        this.url.list = "/business/busMemberCard/list";
      } else {
        this.url.list = "/business/busMemberCard/list?hotelId=" + hotelInfo.id;
      }
      this.loadData(1);
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    handlePointEdit(record) {
      this.$refs.modalMemberPointLogForm.add(record);
      this.$refs.modalMemberPointLogForm.title = "积分调整";
      this.$refs.modalMemberPointLogForm.disableSubmit = false;
    },
    handleTopUp(record) {
      this.$refs.modalMemberTopUp.add(record)
    },
    groupChange(e) {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning("请选择一条记录！");
        return;
      }
      var that = this;
      httpAction(
        "/business/busMemberCard/groupBatch",
        {
          groupId: e,
          ids: this.selectedRowKeys,
        },
        "post"
      )
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.groupId = undefined;
            that.modalFormOk();
          } else {
            that.$message.warning(res.message);
          }
        })
        .finally(() => {});
    },
    labelChange(e) {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning("请选择一条记录！");
        return;
      }
      var that = this;
      httpAction(
        "/business/busMemberCard/lableBatch",
        {
          labelId: e,
          ids: this.selectedRowKeys,
        },
        "post"
      )
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.labelId = undefined;
            that.modalFormOk();
          } else {
            that.$message.warning(res.message);
          }
        })
        .finally(() => {});
    },
    searchReset() {
      this.datetime = [];
      this.queryParam = {};
      this.loadData(1);
    },
    onChange(e, dateString) {
      // console.log("Selected Time: ", e);
      // console.log("Formatted Selected Time: ", dateString);
      this.queryParam.startTime = dateString[0];
      this.queryParam.endTime = dateString[1];
    },
    onSwitchChange(e) {
      console.log(e);
      this.showCard = e;
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "tenantId", text: "关联租户" });
      fieldList.push({ type: "string", value: "hotelId", text: "关联酒店" });
      fieldList.push({ type: "string", value: "mobile", text: "手机号" });
      fieldList.push({ type: "string", value: "cardNo", text: "会员卡号" });
      fieldList.push({ type: "string", value: "cardInnerNo", text: "卡内码" });
      fieldList.push({ type: "string", value: "gradeId", text: "等级类型" });
      fieldList.push({ type: "number", value: "memberFee", text: "会员费" });
      fieldList.push({ type: "int", value: "payType", text: "付款类型" });
      fieldList.push({ type: "int", value: "paymentMethod", text: "付款方式" });
      fieldList.push({ type: "string", value: "name", text: "会员姓名" });
      fieldList.push({ type: "string", value: "avatar", text: "头像" });
      fieldList.push({ type: "int", value: "sex", text: "性别" });
      fieldList.push({
        type: "int",
        value: "certificateType",
        text: "证件类型",
      });
      fieldList.push({
        type: "string",
        value: "certificateNo",
        text: "证件号",
      });
      fieldList.push({ type: "string", value: "address", text: "地址" });
      fieldList.push({
        type: "string",
        value: "consumePassword",
        text: "消费密码",
      });
      fieldList.push({ type: "int", value: "validity", text: "有效期" });
      fieldList.push({
        type: "date",
        value: "validityEndTime",
        text: "有效期截止日期",
      });
      fieldList.push({ type: "string", value: "remark", text: "备注" });
      fieldList.push({ type: "string", value: "staffId", text: "推荐员工" });
      fieldList.push({ type: "int", value: "integral", text: "可用积分" });
      fieldList.push({ type: "number", value: "balance", text: "可用余额" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
/deep/ .ant-input-search-button {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
}
/deep/ .ant-input-search-button[disabled]:hover {
  opacity: 0.7;
  background-color: #ff4d4f;
  border-color: #ff4d4f;
}
/deep/ .ant-input-search-button[disabled] {
  opacity: 0.7;
  color: #ffffff;
}
.ant-card-h {
  border: 1px solid #e8e8e8;
  height: 150px;
}
.total {
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
  color: #000;
  margin-top: 4px;
  margin-bottom: 0;
  font-size: 30px;
  line-height: 38px;
  height: 38px;
}
.chart-card-content {
  margin-bottom: 12px;
  position: relative;
  height: 46px;
  width: 100%;
}
.chart-card-content .content-fix {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
}
</style>

