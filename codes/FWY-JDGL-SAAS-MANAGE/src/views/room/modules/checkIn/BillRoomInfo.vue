<template>
  <div>
    <div class="tab_height" style="display: flex; gap: 15px">
      <div style="width: 33%">
        <div style="display: flex; justify-content: space-between">
          <h4
            style="
              color: rgba(255, 141, 26, 1);
              font-weight: 600;
              margin-top: 15px;
            "
          >
            入住信息
          </h4>
          <div style="display: flex">
            <div
              style="display: flex; flex-direction: column"
              v-if="chooseLivingRoom.livingOrder && chooseLivingRoom.livingOrder.settleType == -1 && !chooseLivingRoom.isMain && tabSelectRoomId !== 1"
              @click="changeMainRoom">
              <a-icon
                type="clock-circle"
                style="color: rgba(255, 141, 26, 1); font-size: 18px"/>
              <a-button type="link"> 改为主房 </a-button>
            </div>
            <div style="display: flex; flex-direction: column" @click="puls()" v-if="isLiving">
              <a-icon
                type="clock-circle"
                style="color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button type="link"> 叫醒服务 </a-button>
            </div>
            <div style="display: flex; flex-direction: column" @click="splitLiving" v-if="model.livingRoomIds.length > 2">
              <a-icon
                type="compass"
                style="color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button
                type="link"
                :disabled="selectRoomId == '1'"
              >
                拆分房间
              </a-button>
            </div>
          </div>
        </div>
        <a-divider />
        <a-tabs @change="tabChange" v-model="tabSelectRoomId">
          <a-tab-pane
            :key="item.roomId"
            v-for="(item, livingIndex) in model.livingRoomIds"
          >
            <span slot="tab">
              {{ item.roomName }}
              <a-badge
                dot
                :number-style="{ backgroundColor: '#52c41a' }"
                v-if="item.livingOrder && item.livingOrder.settleType == 1"
              >
                <span style="font-size: 12px">已结退房</span>
              </a-badge>
              <a-badge
                dot
                :number-style="{ backgroundColor: '#52c41a' }"
                v-if="item.livingOrder && item.livingOrder.settleType == 2"
              >
                <span style="font-size: 12px">未结退房</span>
              </a-badge>
              <span v-if="item.isMain && item.roomId !=='1' ">(主房间)</span>
            </span>
            <a-descriptions :column="2">
              <a-descriptions-item label="主客姓名">
                {{
                  item.livingCustomers && item.livingCustomers[0]
                    ? item.livingCustomers[0].customerName
                    : "--"
                }}
                <a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editCustomer(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                /></a-descriptions-item>
              <a-descriptions-item label="性别">
                {{
                  item.livingCustomers && item.livingCustomers[0]
                    ? item.livingCustomers[0].gender === 1
                      ? "男"
                      : "女"
                    : "--"
                }}</a-descriptions-item
              >
              <a-descriptions-item label="房间数量">
                {{ livingIndex == 0 ? model.livingRoomIds.length - 1 : 1 }}间
              </a-descriptions-item>
              <a-descriptions-item label="手机号">
                {{
                  item.livingCustomers && item.livingCustomers[0]
                    ? item.livingCustomers[0].phone
                    : "--"
                }}
                <a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editCustomer(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                /></a-descriptions-item>

              <a-descriptions-item label="宾客类型">
                {{ customerTypeName( item.livingOrder && item.livingOrder.customerType) }}
                <a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editCustomerType(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                /></a-descriptions-item>
              <a-descriptions-item label="入住时间">
                {{ item.livingOrder && item.livingOrder.arrivalTime }}
              </a-descriptions-item>
              <a-descriptions-item label="房价方案"> -- </a-descriptions-item>
              <a-descriptions-item label="预离时间">
                {{ item.livingOrder && item.livingOrder.dueOutTime }}
              </a-descriptions-item>
              <a-descriptions-item label="订单来源">
                {{ customerSourceName( item.livingOrder && item.livingOrder.customerSource) }}
                <a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editCustomerSource(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                /></a-descriptions-item>
              <a-descriptions-item label="入住类型">
                {{ getlivingTypeText( item.livingOrder && item.livingOrder.livingType) }}
                <a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editBookingType(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                />
              </a-descriptions-item>
              <a-descriptions-item label="入住天数" v-if="item.livingOrder.livingType !== 5">
                {{ item.livingOrder && item.livingOrder.dayCount }}
              </a-descriptions-item>
              <a-descriptions-item label="剩余天数" v-else>
                {{ moment(item.livingOrder.dueOutTime).diff(moment(new Date()), 'day')  }}
              </a-descriptions-item>
              <a-descriptions-item label="早餐券">
                {{ item.livingOrder && item.livingOrder.breakfastNum }}
                <a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editBreakfastNum(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                />
              </a-descriptions-item>
              <a-descriptions-item label="外部单号">
                {{ item.livingOrder && model.orderInfo.outerOrdersNo }}
              </a-descriptions-item>
              <a-descriptions-item label="销售员工">
                {{ warranterName( item.livingOrder && item.livingOrder.warranter) }}
                <a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editWarranter(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                /></a-descriptions-item>
              <a-descriptions-item label="订单备注" :span="2">
                {{ item.livingOrder && item.livingOrder.remark
                }}<a-icon
                  v-if="livingIndex > 0 && isLiving"
                  @click="editRemark(item)"
                  type="edit"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
                />
              </a-descriptions-item>
              <template
                v-if="
                  wakeList.filter((item) => {
                    return tabSelectRoomId == 1
                      ? item
                      : tabSelectRoomId == item.roomId;
                  }).length == 0
                "
              >
                <a-descriptions-item label="叫醒服务" :span="2">
                  <a-icon
                      v-if="isLiving"
                      type="plus-circle"
                    class="dynamic-delete-button"
                    @click="puls()"
                  />
                </a-descriptions-item>
              </template>
              <template v-else>
                <a-descriptions-item
                  label="叫醒服务"
                  :span="2"
                  v-for="(item, index) in wakeList.filter((item) => {
                    return tabSelectRoomId == 1
                      ? item
                      : tabSelectRoomId == item.roomId;
                  })"
                  :key="index"
                >
                  {{ item.roomName }}
                  {{ item.jxDate ? item.jxDate + " /" : "" }} {{ item.jxTime }}
                  <a-icon
                    v-if="
                      wakeList.filter((item) => {
                        return tabSelectRoomId == 1
                          ? item
                          : tabSelectRoomId == item.roomId;
                      }).length -
                        1 ==
                        index
                    "
                    type="plus-circle"
                    class="dynamic-delete-button"
                    @click="puls()"
                  />
                  <a-icon
                    type="minus-circle"
                    style="color: #f56c6c"
                    class="dynamic-delete-button"
                    @click="removeWakeService(item.id)"
                  />
                </a-descriptions-item>
              </template>
              <!--               留言-->
              <template>

              </template>

              <a-descriptions-item
                label="留言"
                :span="2"
                v-for="(item, index) in messageList.filter((item) => {
                  return tabSelectRoomId == 1
                    ? item
                    : tabSelectRoomId == item.roomId;
                })"
                :key="index"
              >
                {{ item.contentBody }}
              </a-descriptions-item>
            </a-descriptions>
            <div style="display: flex; justify-content: space-between">
              <h4
                style="
                  color: rgba(255, 141, 26, 1);
                  font-weight: 600;
                  margin-top: 15px;
                "
              >
                同住人
              </h4>
              <div style="display: flex">
                <div style="display: flex; flex-direction: column">
                  <a-icon
                    type="user-add"
                    style="color: rgba(255, 141, 26, 1); font-size: 18px"
                  /><a-button
                    type="link"
                    @click="addlivingCustomer(item.livingOrder.id, item.roomId)"
                  >
                    添加
                  </a-button>
                </div>
              </div>
            </div>
            <a-row v-for="(fItem, index) in addPeopleList" :key="index" style="margin-bottom:10px;">
              <a-col :span="5">
                <a-auto-complete
                  v-model="fItem.customerName"
                  placeholder="姓名"
                  @search="handleSearch"
                  @select="(e) => handleSelectMember(e,index)"
                >
                  <template slot="dataSource">
                    <a-select-option
                      v-for="item in customerList"
                      :key="item.id"
                    >{{ item.name }}-{{ item.phone }}</a-select-option
                    >
                  </template>
                </a-auto-complete>
              </a-col>
              <a-col :span="5">
                <a-radio-group v-model="fItem.gender">
                  <a-radio :value="1" style="margin-right: 0px">男</a-radio>
                  <a-radio :value="2" style="margin-right: 0px">女</a-radio>
                </a-radio-group>
              </a-col>
              <a-col :span="6">
                <a-input
                  v-model="fItem.certNo"
                  placeholder="请输入证件号"
                ></a-input>
              </a-col>
              <a-col :span="5">
                <a-input
                  v-model="fItem.phone"
                  placeholder="请输入手机号"
                ></a-input>
              </a-col>
              <a-col :span="3">
                <a-icon @click="savePeople(fItem)" style="color:bule;" class="dynamic-delete-button" type="check-circle" />
                <a-icon @click="cancelPeople(index)" style="color: #f56c6c" class="dynamic-delete-button" type="close-circle" />
              </a-col>
              <a-col :span="21">
                <a-input v-model="fItem.address" placeholder="请输入地址" style="width: 70%;"></a-input>
                <a-button type="primary" ghost style="width: 20%; margin-left: 20px">读卡</a-button>
              </a-col>
            </a-row>
            <div id="livingCustomers-grid">
              <a-row
                v-for="(customer, index) in item.livingCustomers"
                :key="customer.id"
              >
                <template v-if="index > 0">
                  <a-col :span="6">
                    ({{ getRoomName(customer.roomId) }}){{
                      customer.customerName
                    }}
                  </a-col>
                  <a-col
                    :span="2"
                  >{{ customer.gender == 1 ? "男" : "女" }}
                  </a-col>
                  <a-col :span="8"> {{ customer.certNo || "--" }}</a-col>
                  <a-col :span="6"> {{ customer.phone }}</a-col>
                  <a-col :span="2">
                    <a-icon
                      type="minus-circle"
                      style="color: #f56c6c"
                      class="dynamic-delete-button"
                      @click="() => removeLivingCustomer(customer.id)"
                    /></a-col>
                </template>
              </a-row>
            </div>
          </a-tab-pane>
        </a-tabs>
        <template v-if="vipCustomer && vipCustomer.id">
          <h4
            style="
              color: rgba(255, 141, 26, 1);
              font-weight: 600;
              margin-top: 50px;
            "
          >
            会员信息
          </h4>
          <a-divider />
          <a-descriptions :column="2">
            <a-descriptions-item label="姓名">
              {{ vipCustomer.name }}
            </a-descriptions-item>
            <a-descriptions-item
              label="证件号"
            >{{ vipCustomer.certificateNo }}
            </a-descriptions-item>
            <a-descriptions-item label="卡号">
              {{ vipCustomer.cardNo }}
            </a-descriptions-item>
            <a-descriptions-item label="手机号">
              {{ vipCustomer.mobile }}
            </a-descriptions-item>
            <a-descriptions-item label="会员级别">
              {{ vipCustomer.gradeName }}
            </a-descriptions-item>
            <a-descriptions-item
              label="余额"
            >{{ vipCustomer.balance.toFixed(2) }} 元
            </a-descriptions-item>
            <a-descriptions-item label="积分">
              {{ vipCustomer.integral }} 分
            </a-descriptions-item>
          </a-descriptions></template
        >
      </div>
      <div style="width: 51%">
        <div style="display: flex; justify-content: space-between">
          <h4
            style="
              color: rgba(255, 141, 26, 1);
              font-weight: 600;
              margin-top: 15px;
            "
          >
            消费详情
          </h4>
          <div style="display: flex">
            <div
                @click="meterReading"
                :class="`${isLiving && selectRoomId != '1' && chooseLivingRoom.livingOrder && chooseLivingRoom.livingOrder.livingType === 5  ? '' : 'disabled'}`"
                style="display: flex; flex-direction: column">
              <a-icon
                  type="pound"
                  style="color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button
                type="link">
              抄表
              </a-button>
            </div>
            <div
              @click="strikeBalance"
              :class="`${isLiving ? '' : 'disabled'}`"
              style="display: flex; flex-direction: column">
              <a-icon
                type="pound"
                style="color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button
                type="link"
              >
                冲账
              </a-button>
            </div>
            <div
              @click="addProjectFee"
              style="display: flex; flex-direction: column"
              :class="`${selectRoomId == '1' || chooseLivingRoom.livingOrder && chooseLivingRoom.livingOrder.settleType !== -1 ? 'disabled' : ''}`">
              <a-icon
                type="tool"
                style="color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button
                type="link"
              >
                增加消费
              </a-button>
            </div>
            <div :class="`${isLiving ? '' : 'disabled'}`" style="display: flex; flex-direction: column">
              <a-icon
                type="property-safety"
                style="color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button
                type="link"
                @click="partialSettle"
              >
                部分结账
              </a-button>
            </div>
          </div>
        </div>
        <a-divider style="margin-bottom: 0px" />
        <a-tabs :v-model="active" @change="tabFeeChange" style="padding-top: 0px">
          <a-tab-pane tab="全部账单" key="0" :forceRender="true">
            <a-table
              :columns="columns0"
              :data-source="entiretyList"
              :pagination="false"
              :scroll="{ y: 160 }"
              rowKey="id"
              :rowSelection="{
                consumSelectedRowKeys: consumSelectedRowKeys,
                onChange: onSelectChange,
              }"
            >
              <template #default="{ record }">
              <a-table-column
                  title="Selection"
                  key="selection"
                  align="center"
              >
                <a-checkbox :disabled="record.preferentialStatus === 2" />
              </a-table-column>
              <!-- 其他列 -->
            </template>
              <template slot="subjectType" slot-scope="text, record, index" class="strike_style">
                <span class="strike_style" v-if="record.returnItem">{{ getSubjectTypeText(text,record) }}</span>
                <span v-else>{{ getSubjectTypeText(text,record) }}</span>
              </template>
              <template slot="roomId" slot-scope="text, record, index">
                <span class="strike_style" v-if="record.returnItem">{{ getRoomName(text) }}</span>
                <span v-else>{{ getRoomName(text) }}</span>
              </template>
              <template slot="dayTime" slot-scope="text, record, index" class="strike_style">
                <span class="strike_style" v-if="record.returnItem">{{ record.dayTime || record.createTime }}</span>
                <span v-else>{{ record.dayTime || record.createTime }}</span>
              </template>
              <!-- 单价-->
              <template slot="originalPrice" slot-scope="text, record, index" class="strike_style">
                <span class="strike_style" v-if="record.returnItem">--</span>
                <span v-else-if="record.subjectType === 6 || record.subjectType === 7">{{ record.feeGoodVo.originalPrice ? record.feeGoodVo.originalPrice : record.originalMoney }}</span>
                <span v-else>{{ record.subjectType === 11 && record.longRentMeterLog.price ? record.longRentMeterLog.price : record.originalMoney }}</span>
              </template>
              <!--优惠价-->
              <template slot="money" slot-scope="text, record, index" class="strike_style">
                <span class="strike_style" v-if="record.returnItem">--</span>
                <span v-else-if="record.subjectType === 6 || record.subjectType === 7" >{{ record.feeGoodVo.price ? record.feeGoodVo.price : record.originalMoney }}</span>
                <span v-else-if="record.subjectType === 11 && record.longRentMeterLog.price">{{ record.longRentMeterLog.price }}</span>
                <span v-else>{{ record.originalMoney }}</span>
              </template>
              <template slot="num" slot-scope="text, record, index" class="strike_style">
                <span class="strike_style" v-if="record.returnItem">{{ record.returnNum }}</span>
                <span v-else-if="record.subjectType === 6 || record.subjectType === 7">{{ record.feeGoodVo.num ? record.feeGoodVo.num : 1 }}</span>
                <span v-else>{{ record.subjectType === 11 && record.longRentMeterLog.startRead ? Math.abs(record.longRentMeterLog.startRead - record.longRentMeterLog.endRead).toFixed(2) : '1' }}</span>
              </template>
              <!-- 小计 -->
              <template slot="originalMoney" slot-scope="text, record, index" class="strike_style">
                <span class="strike_style" v-if="record.returnItem">{{ record.money }}</span>
                <span v-else>{{ record.originalMoney }}</span>
              </template>
              <!-- <template slot="roomId" slot-scope="text, record, index" class="strike_style">
                {{ getRoomName(text) }}
              </template> -->
            </a-table>
            <div
              style="
            color: rgba(255, 87, 51, 1);
            font-weight: 600;
            text-align: right;
          "
            >
              合计消费：{{ feesAmount.toFixed(2) }}
            </div>
          </a-tab-pane>
          <a-tab-pane tab="未结算" key="1" :forceRender="true">
            <a-table
              :columns="columns1"
              :data-source="feesList"
              :pagination="false"
              :scroll="{ y: 160 }"
              rowKey="id"
              :rowSelection="{
                consumSelectedRowKeys: consumSelectedRowKeys,
                onChange: onSelectChange}">
              <template slot="subjectType" slot-scope="text, record, index">
                {{ getSubjectTypeText(text,record) }}
              </template>
              <template slot="roomId" slot-scope="text, record, index">
                {{ getRoomName(text) }}
              </template>
            </a-table>
            <div
              style="
            color: rgba(255, 87, 51, 1);
            font-weight: 600;
            text-align: right;
          "
            >
              合计消费：{{ unsettledAmount.toFixed(2) }}
            </div>
          </a-tab-pane>
          <a-tab-pane tab="已结算" key="2">
            <a-table
              :columns="columns1"
              :data-source="paidList"
              :pagination="false"
              :scroll="{ y: 160 }"
              rowKey="id"
            >
              <template slot="subjectType" slot-scope="text, record, index">
                {{ getSubjectTypeText(text,record) }}
              </template>
              <template slot="roomId" slot-scope="text, record, index">
                {{ getRoomName(text) }}
              </template>
            </a-table>
            <div
              style="
            color: rgba(255, 87, 51, 1);
            font-weight: 600;
            text-align: right;
          "
            >
              合计消费：{{ paidAmount }}
            </div>
          </a-tab-pane>
          <a-tab-pane tab="冲账单" key="3">
            <a-table
              :columns="strikeColumns"
              :data-source="strikeList"
              :pagination="false"
              :scroll="{ y: 160 }"
              rowKey="id"
            >
              <template slot="subjectType" slot-scope="text, record, index">
                {{ getSubjectTypeText(text,record) }}
              </template>
              <template slot="roomId" slot-scope="text, record, index">
                {{ getRoomName(text) }}
              </template>
            </a-table>
            <div
              style="
            color: rgba(255, 87, 51, 1);
            font-weight: 600;
            text-align: right;
          "
            >
              合计冲账：{{ strikeAmount }}
            </div>
          </a-tab-pane>

        </a-tabs>
        <div
          style="
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
          "
        >
          <h4
            style="
              color: rgba(255, 141, 26, 1);
              font-weight: 600;
              margin-top: 15px;
            "
          >
            收款详情
          </h4>
          <div style="display: flex" >
            <div
              @click="enterAccount"
              :class="`${isLiving ? '' : 'disabled'}`"
              style="display: flex; flex-direction: column">
              <a-icon
                type="transaction"
                style=" color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button type="link" > 收款 </a-button>
            </div>
            <div
              @click="refund"
              :class="`${chooseLivingRoom.livingOrder && chooseLivingRoom.livingOrder.settleType !== -1 ? 'disabled' : ''}`"
              style="display: flex; flex-direction: column">
              <a-icon
                type="trademark"
                style="color: rgba(255, 141, 26, 1); font-size: 18px"
              /><a-button type="link"> 退款 </a-button>
            </div>
          </div>
        </div>
        <a-divider />
        <a-table
          :columns="columns2"
          :data-source="paymentList"
          :pagination="false"
          :scroll="{ y: 160 }"
        >
          <template slot="payType" slot-scope="text, record, index">
            {{ getPayTypeText(text) }}
          </template>
          <template slot="subjectType" slot-scope="text, record, index">
            {{ getSubjectTypeText(record.subjectType,record) }}
          </template>
        </a-table>
        <div
          style="
            color: rgba(255, 87, 51, 1);
            font-weight: 600;
            text-align: right;
          "
        >
          合计收款：{{ paymentAmount.toFixed(2) }}
        </div>
      </div>
      <div style="width: 16%">
        <h4 style="color: rgba(255, 141, 26, 1); font-weight: 600">财务汇总</h4>
        <a-divider />
        <a-descriptions :column="1">
          <a-descriptions-item label="合计收款">
            + {{ paymentAmount.toFixed(2) }}
          </a-descriptions-item>
          <a-descriptions-item
            label="合计消费"
          >-{{ feesAmount.toFixed(2) }}
          </a-descriptions-item>
          <a-descriptions-item
            label="结账应退"
            v-if="paymentAmount - feesAmount > 0"
          >
            {{ (paymentAmount - feesAmount).toFixed(2) }}
          </a-descriptions-item>
          <a-descriptions-item label="结账应收" v-else>
            {{ (feesAmount - paymentAmount).toFixed(2) }}
          </a-descriptions-item>
        </a-descriptions>
        <a-button
          v-if="paymentAmount - feesAmount > 0"
          @click="handleAdd"
          type="danger"
          style="margin-left: 100px"
          :disabled="btnSisabled"
        >结账退款</a-button
        >
        <a-button
          v-else
          @click="handleAdd"
          type="danger"
          style="margin-left: 100px"
          :disabled="btnSisabled"
        >结账收款</a-button
        >
      </div>
    </div>
    <adjust-price-form ref="adjustPriceForm" @ok="modalFormOk"></adjust-price-form>
    <customer-modal ref="modalCustomerForm" @ok="modalFormOk"></customer-modal>
    <payment-modal ref="modalPaymentForm" @ok="modalFormOk"></payment-modal>
    <enter-account-model ref="enterAccountModel" @ok="modalFormOk"></enter-account-model>
    <refund-modal ref="modalRefundForm" @ok="modalFormOk"></refund-modal>
    <pay-or-refund-modal ref="modalPayOrRefundForm" @ok="modalFormOk"></pay-or-refund-modal>
    <fee-modal ref="modalFeeForm" @ok="modalFormOk"></fee-modal>
    <meter-reading-modal ref="modalMeterReading" @ok="modalFormOk"></meter-reading-modal>
    <strike-balance-model ref="modalStrikeBalanceForm" @ok="modalFormOk"></strike-balance-model>
    <member-message-modal ref="modalMemberMessage" @ok="modalFormOk"></member-message-modal>
    <lease-goods-modal ref="modalLeaseGoods" @ok="modalFormOk"></lease-goods-modal>
    <select-check-in-room-order-modal
      ref="modalSelectCheckInRoomOrder"
      @ok="modalFormOk"
    ></select-check-in-room-order-modal>
    <edit-customer-modal
      ref="modalEditCustomer"
      @ok="modalFormOk"
    ></edit-customer-modal>
    <edit-customer-type-modal
      ref="modalEditCustomerType"
      @ok="modalFormOk"
    ></edit-customer-type-modal>
    <edit-customer-source-modal
      ref="modalEditCustomerSource"
      @ok="modalFormOk"
    ></edit-customer-source-modal>
    <edit-warranter-modal
      ref="modalEditWarranter"
      @ok="modalFormOk"
    ></edit-warranter-modal>
    <edit-remark-modal
      ref="modalEditRemarkModal"
      @ok="modalFormOk"
    ></edit-remark-modal>
    <edit-breakfast-num-modal
      ref="modalEditBreakfastNumModal"
      @ok="modalFormOk"
    ></edit-breakfast-num-modal>
    <edit-booking-type-modal
      ref="modalEditBookingTypeModal"
      @ok="modalFormOk"
    ></edit-booking-type-modal>
    <continued-model ref="continuedModalVue" @ok="modalFormOk" ></continued-model>
    <batch-card-modal-copy ref="batchCardModalCopy"></batch-card-modal-copy>
    <!-- 叫醒服务弹窗 -->
    <j-modal
      :title="'叫醒服务'"
      :visible="wakeService"
      @cancel="wakeService = false"
      @ok="wakeServiceOk"
      destroyOnClose
    >
      <a-select
        style="width: 100px"
        placeholder="房间号"
        @change="onChange"
        v-model="wakeTime.id"
      >
        <a-select-option
          :value=" item.livingOrder && item.livingOrder.id"
          v-for="(item, index) in (model.livingRoomIds || []).filter(
            (item) => item.roomName != '全部'
          )"
          :key="index"
        >
          {{ item.roomName }}
        </a-select-option>
        <!-- <a-select-option value="lucy"> 1002 </a-select-option> -->
      </a-select>
      <a-date-picker
        style="width: 120px; margin-left: 2px"
        placeholder="日期"
        :default-value="wakeTime.date"
        @change="onChangeWakeService"
      />
      <a-time-picker
        style="width: 100px; margin-left: 2px"
        :default-value="moment(wakeTime.time, 'HH:mm')"
        format="HH:mm"
        @change="onChangeWakeServiceTime"
      />
    </j-modal>
  </div>
</template>

<script>
import { httpAction, getAction, deleteAction, postAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'
import moment from 'moment'
import CustomerModal from './CustomerModal.vue'
import RefundModal from './RefundModal.vue'
import PaymentModal from './PaymentModal.vue'
import FeeModal from './FeeModal'
import MemberMessageModal from '@/views/room/modules/membergoodsmanage/membermessageModal'
import LeaseGoodsModal from '@/views/room/modules/leasegoods/leasegoodsModal'
import SelectCheckInRoomOrderModal from './SelectCheckInRoomOrderModal.vue'
import EditCustomerModal from './EditCustomerModal.vue'
import EditCustomerTypeModal from './EditCustomerTypeModal.vue'
import EditCustomerSourceModal from './EditCustomerSourceModal.vue'
import EditWarranterModal from './EditWarranterModal.vue'
import EditRemarkModal from './EditRemarkModal.vue'
import EditBreakfastNumModal from './EditBreakfastNumModal.vue'
import EditBookingTypeModal from './EditBookingTypeModal.vue'
import { match } from 'assert'
import PayOrRefundModal from '@views/room/modules/checkIn/PayOrRefundModal'
import AdjustPriceForm from '@views/room/modules/checkIn/AdjustPriceForm'
import StrikeBalance from '@views/room/modules/checkIn/StrikeBalanceModel'
import StrikeBalanceModel from '@views/room/modules/checkIn/StrikeBalanceModel'
import EnterAccountModel from '@views/room/modules/checkIn/EnterAccountModel'
import MeterReadingModal from '@views/room/modules/checkIn/MeterReadingModel'
import ContinuedModel from '@views/room/modules/fangtaiModal/continuedModal/continuedModal'
import BatchCardModalCopy from '@views/room/modules/checkIn/batchCardModalCopy'

const columns0 = [
  {
    title: '房间号',
    dataIndex: 'roomId',
    width: 80,
    align: 'center',
    scopedSlots: { customRender: 'roomId' }
  },
  {
    title: '费项',
    dataIndex: 'subjectType',
    width: 150,
    align: 'center',
    scopedSlots: { customRender: 'subjectType' }
  },
  {
    title: '入账日期',
    dataIndex: 'dayTime',
    width: 180,
    align: 'center',
    scopedSlots: { customRender: 'dayTime' }
    // customRender: function (text, record) {
    //   if (text !== null && text !== '') {
    //     return record.dayTime.substring(0, 10)
    //   }
    // }
  },
  {
    title: '单价',
    // dataIndex: 'money',
    width: 67,
    align: 'center',
    scopedSlots: { customRender: 'originalPrice' },
    // customRender: function (text, record) {
    //   if ((record.subjectType === 6 || record.subjectType === 7) && record.feeGoodVo.originalPrice) {
    //     return record.feeGoodVo.originalPrice
    //   }
    //   return record.originalMoney
    // }
  },
  {
    title: '优惠价',
    align: 'center',
    dataIndex: 'money',
    scopedSlots: { customRender: 'money' },
    width: 67,
    // customRender: function (text, record) {
    //   if ((record.subjectType === 6 || record.subjectType === 7) && record.feeGoodVo.price) {
    //     return record.feeGoodVo.price
    //   }
    //   return record.originalMoney
    // }
  },
  {
    title: '数量',
    dataIndex: 'num',
    width: 65,
    align: 'center',
    scopedSlots: { customRender: 'num' },
    // customRender: function (text, record) {
    //   if ((record.subjectType === 6 || record.subjectType === 7) && record.feeGoodVo.num) {
    //     return record.feeGoodVo.num
    //   }
    //   return 1
    // }
  },
  {
    title: '小计',
    align: 'center',
    dataIndex: 'originalMoney',
    scopedSlots: { customRender: 'originalMoney' },
    width: 67
  },

]
const columns1 = [
  {
    title: '房间号',
    dataIndex: 'roomId',
    width: 80,
    align: 'center',
    scopedSlots: { customRender: 'roomId' }
  },
  {
    title: '费项',
    dataIndex: 'subjectType',
    width: 150,
    align: 'center',
    scopedSlots: { customRender: 'subjectType' }
  },
  {
    title: '入账日期',
    dataIndex: 'dayTime',
    width: 180,
    align: 'center',
    scopedSlots: { customRender: 'dayTime' }
  },
  {
    title: '单价',
    // dataIndex: 'money',
    width: 67,
    align: 'center',
    // scopedSlots: { customRender: 'originalPrice' },
    customRender: function (text, record) {
      if ((record.subjectType === 6 || record.subjectType === 7) && record.feeGoodVo.originalPrice) {
        return record.feeGoodVo.originalPrice
      } else if (record.subjectType === 11 && record.longRentMeterLog.price) {
        return record.longRentMeterLog.price
      }
      return record.originalMoney
    }
  },
  {
    title: '优惠价',
    align: 'center',
    dataIndex: 'money',
    // scopedSlots: { customRender: 'money' },
    width: 67,
    customRender: function (text, record) {
      if ((record.subjectType === 6 || record.subjectType === 7) && record.feeGoodVo.price) {
        return record.feeGoodVo.price
      } else if (record.subjectType === 11 && record.longRentMeterLog.price) {
        return record.longRentMeterLog.price
      }
      return record.originalMoney
    }
  },
  {
    title: '数量',
    dataIndex: 'num',
    width: 65,
    align: 'center',
    // scopedSlots: { customRender: 'num' },
    customRender: function (text, record) {
      if ((record.subjectType === 6 || record.subjectType === 7) && record.feeGoodVo.num) {
        return record.feeGoodVo.num
      } else if (record.subjectType === 11 && record.longRentMeterLog.startRead) {
        return Math.abs(record.longRentMeterLog.startRead - record.longRentMeterLog.endRead).toFixed(2)
      }
      return 1
    }
  },
  {
    title: '小计',
    align: 'center',
    dataIndex: 'originalMoney',
    scopedSlots: { customRender: 'originalMoney' },
    width: 67
  },

]

const columns2 = [
  {
    title: '入账时间',
    dataIndex: 'createTime',
    align: 'center',
    // width: 110,
    customRender: function (text, record) {
      if (text !== null && text !== '') {
        return record.createTime
      }
    }
  },
  {
    title: '支付方式',
    dataIndex: 'payType',
    align: 'center',
    // width: 100,
    scopedSlots: { customRender: 'payType' }
  },
  {
    title: '备注',
    dataIndex: 'remark',
    align: 'center',
    // width: 120,
    scopedSlots: { customRender: 'subjectType' }
  },
  {
    title: '金额',
    dataIndex: 'money',
    align: 'center',
    // width: 60,
  }
]

const strikeColumns = [
  {
    title: '房间号',
    dataIndex: 'roomId',
    width: 60,
    align: 'center',
    scopedSlots: { customRender: 'roomId' }
  },
  {
    title: '费项',
    dataIndex: 'subjectType',
    width: 120,
    align: 'center',
    scopedSlots: { customRender: 'subjectType' }
  },
  {
    title: '冲账日期',
    dataIndex: 'dayTime',
    width: 110,
    align: 'center',
  },
  {
    title: '冲账数量',
    align: 'center',
    dataIndex: 'returnNum',
    width: 67,
  },
  {
    title: '冲账金额',
    align: 'center',
    dataIndex: 'money',
    width: 67,
    customRender: function (text, record) {
      return -record.money
    }
  }
]
const data = []
for (let i = 0; i < 100; i++) {
  data.push({
    key: i,
    name: `100${i}`,
    age: '房费',
    address: 280
  })
}
const data2 = []
for (let i = 0; i < 8; i++) {
  data2.push({
    key: i,
    name: `2023-02-0${i} 10:52`,
    age: '支付宝',
    remark: '押金',
    amount: 888
  })
}
const date = new Date()
const endDate = new Date(date.setDate(date.getDate() + 1))
export default {
  name: 'BusMeetingRoomForm',
  components: {
    BatchCardModalCopy,
    MeterReadingModal,
    EnterAccountModel,
    StrikeBalanceModel,
    StrikeBalance,
    AdjustPriceForm,
    PayOrRefundModal,
    CustomerModal,
    RefundModal,
    PaymentModal,
    FeeModal,
    MemberMessageModal,
    LeaseGoodsModal,
    SelectCheckInRoomOrderModal,
    EditCustomerModal,
    EditCustomerTypeModal,
    EditCustomerSourceModal,
    EditWarranterModal,
    EditRemarkModal,
    EditBreakfastNumModal,
    EditBookingTypeModal,
    ContinuedModel
  },
  props: {
    // 表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      isLiving: true,
      wakeService: false,
      wakeTime: {
        id: '',
        date: '',
        time: ''
      },
      selectedRowKeys: [],
      consumSelectedRowKeys: [],
      consumSelectedRows: [],
      data,
      columns0,
      columns1,
      data2,
      columns2,
      strikeColumns,
      wakeList: [{}],
      messageList: [{}],
      model: {
        // data: data,
        orderInfo: {},
        roomIds: [],
        layoutDayPrices: [],
        livingRoomIds: []
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,

      validatorRules: {
        dateRange: [{ required: true, message: '请选择维修时间!' }],
        remark: [{ required: true, message: '请输入维修原因!' }]
      },
      url: {
        add: '/business/busMeetingRoom/add',
        edit: '/business/busMeetingRoom/edit',
        queryById: '/business/busMeetingRoom/queryById',
        getBookingOrderInfo:
          '/business/busRoomBookingOrders/getBookingOrderInfo'
      },
      id: '',
      roomId: '',
      tabSelectRoomId: '',
      customerSourceList: [],
      warranterList: [],
      key: 0,
      // 未结算
      feesList: [],
      oldfeesList: [],
      paymentList: [],
      oldpaymentList: [],
      // 已结算
      paidList: [],
      oldpaidList: [],
      // 冲账
      strikeList: [],
      oldStrikeList: [],
      payTypeList: [],
      active: 1,
      vipCustomer: {},
      selectRoomId: '1',
      /**
       * 选中房间信息
       */
      chooseLivingRoom: {},
      /**
       * 同住人
       */
      addPeopleList: [],
      customerList: [],
      // 全部账单
      entiretyList: [],
      // 全部账单计算总消费金额
      entiretyAmount: []
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
    feesAmount() {
      return this.entiretyList.filter(e => !e.returnItem).reduce(function (total, item) {
        return total + item.money
      }, 0)
    },
    /* 未结算合计 */
    unsettledAmount() {
      return this.feesList.reduce(function (total, item) {
        return total + item.money
      }, 0)
    },
    paidAmount() {
      return this.paidList.reduce(function (total, item) {
        return total + item.money
      }, 0)
    },
    strikeAmount() {
      return this.strikeList.reduce(function (total, item) {
        return total - item.money
      }, 0)
    },
    paymentAmount() {
      return this.paymentList.reduce(function (total, item) {
        return total + item.money
      }, 0)
    },
    btnSisabled() {
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      var find = this.model.livingRoomIds[index]
      if (find) {
        return find.livingOrder.settleType == 1
      }
      return false
    },
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {
          this.consumSelectedRowKeys = selectedRowKeys
          this.consumSelectedRows = selectedRows
        },
        getCheckboxProps: (record) => ({
          props: {
            disabled: record.preferentialStatus === 2,
            id: record.id
          }
        })
      }
    }
  },
  created() {
    var _info = JSON.parse(localStorage.getItem('storeInfo'))
    if (_info) {
      this.model.hotelId = _info.id
    }
    // 备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    getAction('/business/busDictItem/queryList', {
      hotelId: _info.id,
      dictName: '客人来源设置'
    }).then((res) => {
      if (res.success) {
        this.customerSourceList = res.result
      }
    })
    getAction('/business/busSalesPerson/list', {
      hotelId: _info.id,
      pageNo: 1,
      pageSize: 100
    }).then((res) => {
      if (res.success) {
        this.warranterList = res.result.records
      }
    })
    this.getbusCustomer()
  },
  methods: {
    // table的expandIcon属性，修改默认展开关闭按钮 子表无数据时不显示展开图标
    /**
     * 添加同住人姓名
     */
    handleSelectMember(e, idx) {
      var find = this.customerList.find((t) => t.id === e)
      console.log(idx)
      console.log(this.addPeopleList)
      this.addPeopleList[idx].phone = find.phone
      this.addPeopleList[idx].customerName = find.name
      this.addPeopleList[idx].customerId = find.id
      // this.model.phone = find.phone;
      // this.model.customerName = find.name;
      // this.model.customerId = find.id;
    },
    handleSearch(value) {
      let result
      if (!value) {
        result = this.oldcustomerList
      } else {
        result = this.oldcustomerList.filter((t) => t.name.includes(value))
      }
      this.customerList = result
    },
    getbusCustomer() {
      getAction('/bus/busCustomer/list', {}).then((res) => {
        if (res.success) {
          this.customerList = res.result.records
          this.oldcustomerList = JSON.parse(JSON.stringify(this.customerList))
        }
      })
    },
    /**
     * 添加同住人保存
     */
    savePeople(item) {
      console.log(item)
      const that = this
      if (!item.customerName) {
        this.$message.warning('姓名不能为空')
        return
      }
      httpAction('/business/busLivingCustomer/add', item, 'post')
      .then((res) => {
        if (res.success) {
          that.$message.success('入住成功')
          // that.$emit("ok");
          this.modalFormOk()
          this.addPeopleList.length = 0
        } else {
          that.$message.warning(res.message)
        }
      })
      .finally(() => {
        // that.confirmLoading = false;
      })
    },
    /**
     * 添加同住人取消
     */
    cancelPeople(idx) {
      this.addPeopleList.splice(idx, 1)
    },

    // 叫醒服务
    wakeServiceOk() {
      console.log(this.wakeTime)
      if (!this.wakeTime.id) {
        this.$message.warning('请选择房间号')
        return
      }
      if (!this.wakeTime.date) {
        this.$message.warning('请选择日期')
        return
      }
      if (!this.wakeTime.time) {
        this.$message.warning('请选择时间')
        return
      }
      let obj = {
        livingOrderId: this.wakeTime.id,
        jxDate: this.wakeTime.date,
        jxTime: this.wakeTime.time
      }
      postAction('/fw/fwLivingJx/add', obj).then((res) => {
        if (res.success) {
          this.$message.success('添加成功')
          this.wakeTime = {}
          this.getWakeServiceData()
          this.wakeService = false
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    // 获取叫醒服务数据
    getWakeServiceData() {
      let ids = []
      this.model.livingRoomIds.forEach((item) => {
        if (item.roomName != '全部') {
          ids.push(item.livingOrder && item.livingOrder.id)
        }
      })
      getAction('/fw/fwLivingJx/list?livingOrderId=' + ids.toString()).then(
        (res) => {
          if (res.success) {
            this.wakeList = res.result.records
          }
        }
      )
    },
    getMessageData() {
      let ids = []
      this.model.livingRoomIds.forEach((item) => {
        if (item.roomName != '全部') {
          ids.push(item.livingOrder && item.livingOrder.id)
        }
      })
      getAction('business/busMemberMessage/list?livingOrderId=' + ids.toString()).then(
          (res) => {
            if (res.success) {
              this.messageList = res.result.records
            }
          }
      )
    },
    // 删除叫醒服务
    removeWakeService(id) {
      deleteAction('/fw/fwLivingJx/delete?id=' + id).then((res) => {
        if (res.success) {
          this.$message.success('删除成功')
          this.getWakeServiceData()
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    changeMainRoom() {
      console.log(this.chooseLivingRoom)
      const bookingRoom = {
        'id': this.chooseLivingRoom.id,
        'isMain': this.chooseLivingRoom.isMain,
        'bookingOrdersId': this.chooseLivingRoom.bookingOrderId
      }
      postAction('/business/busBookingRooms/set-main?livingOrderId=' + this.chooseLivingRoom.livingOrder.id, bookingRoom)
          .then(resp => {
            if (resp.success) {
              this.$message.success('更换主房成功')
              this.getBookingOrderInfo()
            } else {
              this.$message.success('更换主房失败')
            }
          })
    },
    onChangeWakeService(e, e1) {
      console.log(e, e1)
      this.wakeTime.date = e1
    },
    onChangeWakeServiceTime(e, e1) {
      console.log(e, e1)
      this.wakeTime.time = e1
    },
    editBookingType(item) {
      var obj = {
        orderId: item.livingOrder.id,
        liveType: item.livingOrder.livingType,
        fangAnId: item.livingOrder.hourRoomId,
        livingDayPrices: item.livingDayPrices,
        roomId: item.roomId
      }
      this.$refs.modalEditBookingTypeModal.edit(obj)
      this.$refs.modalEditBookingTypeModal.title = '修改'
      this.$refs.modalEditBookingTypeModal.disableSubmit = false
    },
    editBreakfastNum(item) {
      var obj = {
        orderId: item.livingOrder.id,
        brkfstNum: item.livingOrder.breakfastNum
      }
      this.$refs.modalEditBreakfastNumModal.edit(obj)
      this.$refs.modalEditBreakfastNumModal.title = '修改'
      this.$refs.modalEditBreakfastNumModal.disableSubmit = false
    },
    editRemark(item) {
      var obj = {
        orderId: item.livingOrder.id,
        remark: item.livingOrder.remark
      }
      this.$refs.modalEditRemarkModal.edit(obj)
      this.$refs.modalEditRemarkModal.title = '修改'
      this.$refs.modalEditRemarkModal.disableSubmit = false
    },
    editWarranter(item) {
      var obj = {
        orderId: item.livingOrder.id,
        warranter: item.livingOrder.warranter
      }
      this.$refs.modalEditWarranter.edit(obj)
      this.$refs.modalEditWarranter.title = '修改'
      this.$refs.modalEditWarranter.disableSubmit = false
    },
    getlivingTypeText(livingType) {
      var text = ''
      if (livingType === 1) {
        text = '全天'
      } else if (livingType === 2) {
        text = '钟点'
      } else if (livingType === 3) {
        text = '自用'
      } else if (livingType === 4) {
        text = '免费'
      } else if (livingType === 4) {
        text = '长租'
      }
      return text
    },
    editCustomerSource(item) {
      var obj = {
        orderId: item.livingOrder.id,
        customerSource: item.livingOrder.customerSource
      }
      this.$refs.modalEditCustomerSource.edit(obj)
      this.$refs.modalEditCustomerSource.title = '修改'
      this.$refs.modalEditCustomerSource.disableSubmit = false
    },
    editCustomerType(item) {
      console.log(item)
      var obj = {
        orderId: item.livingOrder.id,
        customerType: item.livingOrder.customerType,
        vipCustomerId: item.livingOrder.vipCustomerId,
        contractTeamId: item.livingOrder.contractTeamId,
        contractTeamProtocolId: item.livingOrder.contractTeamProtocolId
      }
      this.$refs.modalEditCustomerType.edit(obj)
      this.$refs.modalEditCustomerType.title = '修改'
      this.$refs.modalEditCustomerType.disableSubmit = false
    },
    editCustomer(item) {
      var customer = item.livingCustomers[0] || {}
      console.log(item)
      console.log(customer)
      var obj = {
        orderId: item.livingOrder.id,
        cusName: customer.customerName,
        cusPhone: customer.phone,
        cusId: customer.id,
        certType: customer.certType,
        certNo: customer.certNo,
        address: customer.address,
        gender: customer.gender,
        nation: customer.nation
      }
      console.log(obj)
      this.$refs.modalEditCustomer.edit(obj)
      this.$refs.modalEditCustomer.title = '修改'
      this.$refs.modalEditCustomer.disableSubmit = false
    },
    splitLiving() {
      var that = this
      this.$confirm({
        title: '提示',
        content: '确认要拆分房间?',
        onOk: function () {
          if (that.model.livingRoomIds.length === 2){
            that.$message.warning('不能拆分主房')
            return
          }
          var index = that.model.livingRoomIds.findLastIndex(
            (t) => t.roomId == that.selectRoomId
          )
          var livingRoom = that.model.livingRoomIds[index]
          console.log(that.model.livingRoomIds)
          if (livingRoom.isMain) {
            if (that.model.livingRoomIds.length > 2) {
              that.$message.warning('请先变更主房间再拆分')
              return
            } else {
              var index2 = that.model.livingRoomIds.findLastIndex(
                  (t) => t.roomId !== that.selectRoomId
              )
              livingRoom = that.model.livingRoomIds[index2]
            }
          }
          httpAction(
            'business/busRoomBookingOrders/split-living?livingOrderId=' +
              livingRoom.livingOrder.id,
            {},
            'post'
          )
            .then((res) => {
              if (res.success) {
                that.$message.success('拆分成功')
                that.getBookingOrderInfo()
                that.$emit('ok')
                console.log(that.model.livingRoomIds && that.model.livingRoomIds.length > 1)
                if (that.model.livingRoomIds && that.model.livingRoomIds.length > 1) {
                  that.tabSelectRoomId = that.model.livingRoomIds[1].roomId
                }
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
    addUnion() {
      if (this.selectRoomId === '1') {
        this.$message.warning('请先选择房间')
        return
      }
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      console.log(this.model.livingRoomIds)
      var find = this.model.livingRoomIds[index]
      let arr = []
      this.model.livingRoomIds.forEach((ele) => {
        if (ele.roomName != '全部') {
          arr.push(ele.livingOrder.id)
        }
      })
      this.$refs.modalSelectCheckInRoomOrder.add()
      this.$refs.modalSelectCheckInRoomOrder.title = '请选择需要关联的订单'
      this.$refs.modalSelectCheckInRoomOrder.disableSubmit = false
      this.$refs.modalSelectCheckInRoomOrder.livingOrderId =
        find.livingOrder.id
      this.$refs.modalSelectCheckInRoomOrder.filterIds = arr
      this.$refs.modalSelectCheckInRoomOrder.livingRoomId = find.roomId
    },
    addLeaseGoods() {
      // if (this.selectRoomId === "1") {
      //   this.$message.warning("请先选择房间");
      //   return;
      // }
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      var find = this.model.livingRoomIds[index]
      var livingOrderId = ''
      if (find.roomId != '1') {
        livingOrderId = find.livingOrder.id
      } else {
        this.model.livingRoomIds
          .filter((t) => t.roomId != '1')
          .forEach((t) => {
            livingOrderId += t.livingOrder.id + ','
          })
      }
      let chooseRooms = [this.chooseLivingRoom]
      if (this.selectRoomId === '1') {
        chooseRooms = this.model.livingRoomIds.filter(e => e.roomId !== '1')
      }
      this.$refs.modalLeaseGoods.add(chooseRooms)
      this.$refs.modalLeaseGoods.title = '物品借用'
      this.$refs.modalLeaseGoods.disableSubmit = false
      this.$refs.modalLeaseGoods.livingOrderId = livingOrderId // find.livingOrder.id;
      this.$refs.modalLeaseGoods.payTypeList = this.payTypeList
    },
    addMessage() {
      if (this.selectRoomId === '1') {
        this.$message.warning('请先选择房间')
        return
      }
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      var find = this.model.livingRoomIds[index]
      console.log(find)
      this.$refs.modalMemberMessage.add()
      this.$refs.modalMemberMessage.title = '客人留言'
      this.$refs.modalMemberMessage.disableSubmit = false
      this.$refs.modalMemberMessage.livingOrderId = find.livingOrder.id
    },
    addProjectFee() {
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      var livingRoom = this.model.livingRoomIds[index]
      console.log(livingRoom)
      console.log(this.model.orderInfo)
      console.log(livingRoom.livingOrder.vipCustomerId)
      let obj = {
        livingOrderId: livingRoom.livingOrder.id,
        vipCustomerId: livingRoom.livingOrder.vipCustomerId
      }
      this.$refs.modalFeeForm.add(obj)
      // this.$refs.modalFeeForm.livingOrderId = livingRoom.livingOrder.id
      this.$refs.modalFeeForm.title = '增加消费项目'
      this.$refs.modalFeeForm.disableSubmit = false
    },
    // tuidan() {
    //   postAction(
    //     '/business/busRoomBookingOrders/退你麻痹单狗东西',
    //     this.consumSelectedRowKeys
    //   ).then((res) => {
    //     if (res.success) {
    //       this.$message.success('退单成功')
    //       this.getBookingOrderInfo()
    //     } else {
    //       this.$message.warning(res.message)
    //     }
    //   })
    // },
    // 部分结账
    partialSettle() {
      if (this.selectRoomId === '1') {
        this.$message.warning('请先选择房间')
        return
      }
      if (this.consumSelectedRowKeys.length === 0) {
        this.$message.warning('请先勾选消费')
        return
      }
      var list2 = this.feesList.filter((t) =>
        this.consumSelectedRowKeys.includes(t.id)
      )
      console.log(list2)
      var amount = list2.reduce(function (total, item) {
        return total + item.money
      }, 0)
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      var livingRoom = this.model.livingRoomIds[index]
      this.$refs.modalPaymentForm.edit({
        billAmount: parseFloat(amount.toFixed(2)),
        deposit: 0,
        roomFee: parseFloat(amount.toFixed(2)),
        subjectType: 5,
        feeType: 2,
        bookingOrderId: this.model.orderInfo.id,
        livingOrderId:
          livingRoom.roomId != '1' ? livingRoom.livingOrder.id : '',
        preferentialType: 1,
        couponFirstAmount: 0,
        discount: 9,
        vipCustomerId: this.model.orderInfo.vipCustomerId,
        selectedFeeIds: this.consumSelectedRowKeys
      })
      this.$refs.modalPaymentForm.title = '部分结账收款'
      this.$refs.modalPaymentForm.disableSubmit = false
      this.$refs.modalPaymentForm.showYinshou = false
    },
    tabChange(e) {
      //
      this.selectRoomId = e
      if (e == '1') {
        this.entiretyList = JSON.parse(JSON.stringify(this.entiretyAmount))
        this.feesList = this.oldfeesList
        this.paymentList = this.oldpaymentList
        this.paidList = this.oldpaidList
        this.strikeList = this.oldStrikeList
        this.strikeList.forEach(item => {
          let tempindex = this.entiretyList.findIndex(index => index.id == item.returnFeeId)
          console.log(this.tempindex);
          this.entiretyList.splice(tempindex+1,0,item)
        })
      } else {
        this.feesList = this.oldfeesList.filter((t) => t.roomId === e)
        this.paymentList = this.oldpaymentList.filter((t) => t.roomId === e)
        this.paidList = this.oldpaidList.filter((t) => t.roomId === e)
        this.strikeList = this.oldStrikeList.filter((t) => t.roomId === e)
        this.entiretyList = this.entiretyAmount.filter((t) => t.roomId === e)
        let find = this.model.livingRoomIds.find((t) => t.roomId === this.selectRoomId)
        this.chooseLivingRoom = find
        console.log(this.model)
        console.log(find)
        // this.loadStrikeDate()
        this.strikeList.forEach(item => {
          let tempindex = this.entiretyList.findIndex(index => index.id == item.returnFeeId)
          console.log(this.tempindex);
          this.entiretyList.splice(tempindex+1,0,item)
        })
        this.isLiving = find.livingOrder.settleType === -1
        this.$emit('changeLivingStatu', find.livingOrder.settleType)
      }
    },
    getPayTypeText(text) {
      var find = this.payTypeList.find((t) => t.id == text)
      return find ? find.name : ''
    },
    getRoomName(id) {
      // console.log(this.model.livingRoomIds)
      var index = this.model.livingRoomIds.findLastIndex((t) => t.roomId == id)
      var find = this.model.livingRoomIds[index]
      return find ? find.roomName : ''
    },
    getSubjectTypeText(text, record) {
      var msg = ''
      if (text == 1) {
        msg = '押金'
        if (record.remark != null && record.remark != '') {
          msg = record.remark
        }
      } else if (text == 2) {
        msg = '预收房费'
      } else if (text == 3) {
        msg = '每日房费'
        if (record.remark != null) {
          msg = record.remark
        }
      } else if (text == 4) {
        msg = '优惠金额'
      } else if (text == 5) {
        if (record.money < 0) {
          msg = '结账退款'
        } else {
          msg = '结账收款'
        }
      } else if (text == 6) {
        msg = '商品-' + record.feeGoodVo.name
      } else if (text == 7) {
        msg = '点餐-' + record.feeGoodVo.name
      } else if (text == 8) {
        msg = '夜审房费'
      } else if (text == 9) {
        msg = '会议室'
      } else if (text == 10) {
        msg = '手工房费'
      } else if (text == 11) {
        msg = record.remark
      } else if (text == 12) {
        msg = '赔偿费'
      } else if (text == 13) {
        msg = record.remark
      } else if (text == 14) {
        msg = record.remark
      }
      return msg
    },
    async getbusRoomPayType() {
      await getAction('/business/busRoomPayType/list', {
        pageSize: 99999,
        pageNo: 1
      }).then((res) => {
        if (res.success) {
          this.payTypeList = res.result.records
        }
      })
    },
    removeLivingCustomer(id) {
      deleteAction('/business/busLivingCustomer/delete', { id: id }).then(
        (res) => {
          if (res.success) {
            this.getBookingOrderInfo()
          }
        }
      )
    },
    addlivingCustomer(id, roomId) {
      console.log(id, roomId)
      // this.$refs.modalCustomerForm.add(id, roomId);
      // this.$refs.modalCustomerForm.title = "添加同住人";
      // this.$refs.modalCustomerForm.disableSubmit = false;
      this.addPeopleList.push({
        livingOrderId: id,
        roomId: roomId,
        certType: 1,
        gender: 1,
        phone: '',
        customerName: '',
        customerId: '',
        hotelId: JSON.parse(localStorage.getItem('storeInfo')).id
      })
    },
    modalFormOk(e) {
      this.getBookingOrderInfo()
      this.$emit('ok')
    },
    customerTypeName(customerType) {
      switch (customerType) {
        case 1:
          return '散客'
        case 2:
          return '会员'
        case 3:
          return '协议单位'
        case 4:
          return '中介'
        default:
          return '散客'
      }
    },
    warranterName(warranter) {
      var find = this.warranterList.find((t) => t.id == warranter)
      return find ? find.name : '--'
    },
    customerSourceName(customerSource) {
      var find = this.customerSourceList.find((t) => t.id == customerSource)
      return find ? find.itemText : '--'
    },
    getMemberCard() {
      getAction('/business/busMemberCard/list', {
        id: this.model.orderInfo.vipCustomerId
      }).then((res) => {
        if (res.success) {
          if (res.result.records && res.result.records.length > 0) {
            this.vipCustomer = res.result.records[0]
          }
        }
      })
    },
    async getBookingOrderInfo() {
      if (this.payTypeList == 0) {
        await this.getbusRoomPayType()
      }
      var obj = {
        bookingNo: this.id
      }
      if (this.key && this.key == 1) {
        obj = {
          bookingOrderId: this.id
        }
      }
      await getAction(this.url.getBookingOrderInfo, obj).then((res) => {
        console.log(res);
        if (res.success) {
          if (res.result.livingRoomIds.length > 1) {
            let index = res.result.livingRoomIds.findIndex(e => e.isMain === true)
              if (index !== -1 && index !== 0) {
                [res.result.livingRoomIds[0], res.result.livingRoomIds[index]] = [res.result.livingRoomIds[index], res.result.livingRoomIds[0]]
              }
          }
          var livingRoomId = JSON.parse(
              JSON.stringify(res.result.livingRoomIds[0])
          )
          livingRoomId.roomId = '1'
          livingRoomId.roomName = '全部'
          var list = []
          res.result.livingRoomIds.forEach((t) => {
            if (t.livingCustomers) {
              list = [...list, ...t.livingCustomers]
            }
          })
          livingRoomId.livingCustomers = list
          res.result.livingRoomIds.unshift(livingRoomId)
            this.tabSelectRoomId = this.roomId && res.result.livingRoomIds.find(t => t.roomId === this.roomId)
                ? this.roomId
                : res.result.livingRoomIds[1].roomId
            this.selectRoomId = this.tabSelectRoomId
          this.model = res.result
          console.log(this.model);
          this.getWakeServiceData()
          this.getMessageData()

          // 查询账单
        getAction('/business/busRoomBookingOrders/living-fees', {
            bookingOrderId: this.model.orderInfo.id,
            isAllFee: true
          }).then((res) => {
            console.log(res);
            if (res.success) {
              if (res.result && res.result.length > 0) {
                // 未支付的费用账单，oldfeesList用于存放所有费用账单，feesList为当前房间账单
                this.feesList = res.result.filter((t) => t.feeType === 1 && t.preferentialStatus === 1)
                this.oldfeesList = JSON.parse(JSON.stringify(this.feesList))
                // 已支付的消费
                this.paidList = res.result.filter((t) => t.feeType === 1 && t.preferentialStatus === 2)
                this.oldpaidList = JSON.parse(JSON.stringify(this.paidList))
                // 全部账单
                this.entiretyAmount = this.feesList.concat(this.paidList)
                this.entiretyList = JSON.parse(JSON.stringify(this.entiretyAmount))
                // 收费账单
                this.paymentList = res.result.filter((t) => t.feeType === 2)
                this.oldpaymentList = JSON.parse(JSON.stringify(this.paymentList))
                // 按当前选中房间过滤出费用
                this.tabChange(this.tabSelectRoomId)
              }
            }
          })
          // 查询冲账账单
          this.loadStrikeDate()
          if (this.model.orderInfo.vipCustomerId) {
            this.getMemberCard()
          }
        }
      })
    },
    // 获取冲账单
    async loadStrikeDate() {
      await getAction('/business/busOrderFee/strike-balance-page', { bookingOrderId: this.model.orderInfo.id })
          .then(res => {
            console.log(res);
            if (res.success) {
              //
              if (res.result && res.result.records.length > 0) {
                // 未支付的费用账单，oldfeesList用于存放所有费用账单，feesList为当前房间账单
                this.strikeList = res.result.records
                this.oldStrikeList = JSON.parse(JSON.stringify(this.strikeList))
                console.log(this.strikeList, '冲帐单')
                // this.entiretyAmount = this.entiretyList
                // console.log(this.entiretyAmount, '冲帐单')

                // let templist = this.strikeList.find()

                // this.strikeList.forEach(item => {
                //   this.entiretyList.forEach(index => {
                //     if (item.returnFeeId == index.id) {
                //       let tempindex = this.entiretyList.indexOf(index)
                //       this.entiretyList.splice(tempindex,0,item)
                //     }
                //   })
                // }
                // 处理全部账单加上冲帐单
                // console.log(this.entiretyList);
                // this.strikeList.forEach(item => {
                //   let tempindex = this.entiretyList.findIndex(index => index.id == item.returnFeeId)
                //   console.log(this.tempindex);
                //   this.entiretyList.splice(tempindex+1,0,item)
                // })
                //   // if (item == this.entiretyList.find(index => index.id = item.returnFeeId)) {
                //     templist.push(this.entiretyList.find(index => index.id = item.returnFeeId),item)
                  // }
                //   // if ()

                //   // this.strikeList.forEach(index => {
                //   //   if(item.id = index.returnFeeId) {
                //   //     templist.push(item,index)
                //   //   }
                //   // })
                // });
                this.tabChange(this.tabSelectRoomId)
              }
            }
          })
    },
    getCharge() {
      let ids = []
      this.model.livingRoomIds.forEach((item) => {
        if (item.roomName != '全部') {
          ids.push(item.livingOrder && item.livingOrder.id)
        }
      })
      console.log(ids, "ffffffffffffffffffffffffff")
      getAction('/rooms/houseLongRentCharges/getMeterCharge', { livingOrderId: ids.toString() }).then((res) => {
        if (res.success) {
          console.log(res.result.records, 'res.result.records')
        }
      })
    },
    tabFeeChange() {
      // this.
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.consumSelectedRowKeys = selectedRowKeys
      this.consumSelectedRows = selectionRows
    },
    refund() {
      let chooseRooms = [this.chooseLivingRoom]
      if (this.selectRoomId === '1') {
        chooseRooms = this.model.livingRoomIds.filter(e => e.roomId !== '1')
      }
      this.$refs.modalRefundForm.edit(chooseRooms)
      this.$refs.modalRefundForm.payTypeList = this.payTypeList
    },
    meterReading() {
      console.log(this.model, 'this.model')
      var index = this.model.livingRoomIds.findLastIndex(
          (t) => t.roomId == this.selectRoomId
      )
      var livingRoom = this.model.livingRoomIds[index]
      this.$refs.modalMeterReading.visible = true
      this.$refs.modalMeterReading.edit(livingRoom.roomId, livingRoom.livingOrder.id)
      this.$refs.modalMeterReading.title = '读表'
    },
    strikeBalance() {
      console.log(this.model)
      let map = new Map()
      this.model.livingRoomIds.forEach(e => {
        if (e.roomId !== '1') {
          map.set(e.roomId, e.roomName)
        }
      })
      let fees = JSON.parse(JSON.stringify(this.feesList))
      fees.forEach(e => {
        if (e.subjectType === 6 || e.subjectType === 7) {
          let returnNum = this.strikeList.filter(ele => ele.returnFeeId === e.id).reduce((sum, val) => {
            return sum + val.returnNum
          }, 0)
          e.feeGoodVo.num = e.feeGoodVo.num - returnNum;
        }
      })
      let vipId = {
        vipCustomerId: this.model.vipCustomerId
      }
      this.$refs.modalStrikeBalanceForm.edit(fees, map,vipId)
      this.$refs.modalStrikeBalanceForm.title = '冲账'
    },
    enterAccount() {
      let chooseRooms = [this.chooseLivingRoom]
      // vipCustomerId: livingRoom.livingOrder.vipCustomerId,
      if (this.selectRoomId === '1') {
        chooseRooms = this.model.livingRoomIds.filter(e => e.roomId !== '1')
      }
      this.$refs.enterAccountModel.title = '收款'
      this.$refs.enterAccountModel.add(chooseRooms)
    },
    async handleAdd() {
      var that = this
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      var livingRoom = this.model.livingRoomIds[index]
      if (livingRoom.isMain && livingRoom.roomId !== '1') {
        let livingRoomIdList = this.model.livingRoomIds.filter(item => item.livingOrder.settleType !== 1)
        if (livingRoomIdList.length > 2) {
          this.$message.warning('存在联房，主房不能单独结账')
          return
        }
      }
      if (livingRoom.roomId !== '1' && !livingRoom.isMain) {
        this.$confirm({
          title: '提示',
          content: '是否团队结账?',
          okText: '团队结账',
          cancelText: '单房结账',
          onOk: function () {
            that.tabSelectRoomId = '1'
            livingRoom = that.model.livingRoomIds.find((t) => t.roomId === '1')
            that.tabChange('1')
            that.settle(livingRoom)
          },
          onCancel: function () {
            that.settle(livingRoom)
          }
        })
      } else {
        that.settle(livingRoom)
      }
    },
    settle(livingRoom) {
      var that = this
      let livingOrderIds = ''
      if (livingRoom.roomId !== '1') {
        livingOrderIds = livingRoom.livingOrder.id
      } else {
        this.model.livingRoomIds.filter((t) => t.roomId != '1').forEach((t) => {livingOrderIds += t.livingOrder.id + ','})
      }
      /* 判断是否有租借物品未归还 */
      var count = 0
      getAction('/order/cesOrderLeaseGoods/checkReturnGoods', {
        livingOrderId: livingOrderIds
      }).then((res) => {
        if (res.success) {
          if (res.result && res.result > 0) {
            count = res.result
            that.$confirm({
              title: '提示',
              content: '您还有' + count + '件物品未归还，是否跳转物品租借页面?',
              onOk: function () {
                that.addLeaseGoods()
              },
              onCancel() {}
            })
          }
        }
      })
      if (count !== 0) {
        return
      }
      let differMoney = this.feesAmount - this.paymentAmount
      this.$refs.modalPayOrRefundForm.edit({
        money: differMoney,
        collection: this.paymentAmount,
        consumption: this.feesAmount,
        subjectType: 5,
        feeType: 2,
        bookingOrderId: this.model.orderInfo.id,
        livingOrderId:
            livingRoom.roomId !== '1' ? livingRoom.livingOrder.id : '',
        livingOrderIds: livingOrderIds,
        vipCustomerId: livingRoom.livingOrder.vipCustomerId,
        contractTeamId: livingRoom.livingOrder.contractTeamId,
        contractTeamProtocolId: livingRoom.livingOrder.contractTeamProtocolId,
        preferentialType: 1,
        couponFirstAmount: 0,
        discount: 9,
        isRefund: differMoney > 0
      })
      this.$refs.modalPayOrRefundForm.title = differMoney > 0 ? '结账收款' : '结账退款'
      this.$refs.modalPayOrRefundForm.disableSubmit = false
    },
    puls() {
      this.wakeService = true
      this.wakeTime.id = this.chooseLivingRoom.livingOrder.id
      this.wakeTime.date = moment(new Date()).add(1, 'days').format('YYYY-MM-DD')
      this.wakeTime.time = '07:00'
      // this.wakeList.push({});
    },
    remove(index) {
      this.wakeList.splice(index, 1)
    },
    moment,
    onChange(e) {
      console.log(e)
    },
    add(id, key, roomId) {
      this.key = key
      this.id = id
      this.roomId = roomId
      console.log(this.modelDefault)
      this.edit(this.modelDefault)
      this.getBookingOrderInfo()
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    handleLeaveNotSettle(isLeaveNotSettle) {
      if (this.selectRoomId === '1') {
        this.$message.warning('请先选择房间')
        return
      }
      var index = this.model.livingRoomIds.findLastIndex(
        (t) => t.roomId == this.selectRoomId
      )
      var find = this.model.livingRoomIds[index]

      console.log(find)
      let url = '/business/busRoomBookingOrders/cancel-leave-not-settle?bookingOrderId='
      if (isLeaveNotSettle) {
        url = '/business/busRoomBookingOrders/leave-not-settle?bookingOrderId='
      }
      this.$emit('changeLoading', true)
      httpAction(
        url + find.bookingOrdersId + '&livingOrderId=' + find.livingOrder.id,
        {},
        'post'
      ).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.getBookingOrderInfo()
          this.$emit('changeLoading', false)
          this.$emit('ok')
        } else {
          this.$message.warning(res.message)
          this.$emit('changeLoading', false)
        }
      }).finally(e => {
        this.$emit('changeLoading', false)
      })
    },
    handleLeaveSettle() {
      if (this.selectRoomId === '1') {
        this.$message.warning('请先选择房间')
        return
      }
      var index = this.model.livingRoomIds.findLastIndex(
          (t) => t.roomId == this.selectRoomId
      )
      var find = this.model.livingRoomIds[index]

      console.log(find)
      let url = '/business/busRoomBookingOrders/cancel-leave-settle?bookingOrderId='
      this.$emit('changeLoading', true)
      httpAction(
          url + find.bookingOrdersId + '&livingOrderId=' + find.livingOrder.id,
          {},
          'post'
      ).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.getBookingOrderInfo()
          this.$emit('changeLoading', false)
          this.$emit('ok')
        } else {
          this.$message.warning(res.message)
          this.$emit('changeLoading', false)
        }
      }).finally(e => {
        this.$emit('changeLoading', false)
      })
    },

    stayOver() {
      var index = this.model.livingRoomIds.findLastIndex(
          (t) => t.roomId === this.selectRoomId
      )
      var find = this.model.livingRoomIds[index]
      let obj = {
        name: find.roomName,
        id: find.roomId,
        layoutId: find.roomLayoutId,
        layoutName: find.layoutName,
        customerName: find.livingCustomers[0].customerName,
        phone: find.livingCustomers[0].phone,
        arriveTime: find.livingOrder.arrivalTime,
        dueOutTime: find.livingOrder.dueOutTime
      }
      this.$refs.continuedModalVue.makeCardInfo = obj
      let record = {
        livingOrderId: find.livingOrder.id,
        bookingOrderId: find.livingOrder.bookingOrderId
      }
      this.$refs.continuedModalVue.edit(record)
      this.$refs.continuedModalVue.title = '续住'
      this.$refs.continuedModalVue.disableSubmit = false
    },
    makeCard() {
      let index = this.model.livingRoomIds.findLastIndex(
          (t) => t.roomId === this.selectRoomId
      )
      let find = this.model.livingRoomIds[index]
      console.log(find, 'find')
      let obj = {
        name: find.roomName,
        id: find.roomId,
        layoutId: find.roomLayoutId,
        layoutName: find.layoutName,
        customerName: find.livingCustomers[0].customerName,
        phone: find.livingCustomers[0].phone,
        arriveTime: find.livingOrder.arrivalTime,
        dueOutTime: find.livingOrder.dueOutTime
      }
      this.$refs.batchCardModalCopy.edit([obj])
    },
    adjustPrice() {
      console.log(this.model.livingRoomIds, 'this.model.livingRoomIds')
      console.log(this.chooseLivingRoom)
      if (this.chooseLivingRoom.roomId === '1' && this.model.livingRoomIds.every(e => moment(e.livingOrder.dueOutTime).format('yyyy-MM-DD') <= new Date().format('yyyy-MM-dd'))) {
        this.$message.warning('当前时间已过预离时间，不能进行操作')
        return
      } else if (moment(this.chooseLivingRoom.livingOrder.dueOutTime).format('yyyy-MM-DD') <= new Date().format('yyyy-MM-dd')){
        this.$message.warning('当前时间已过预离时间，不能进行操作')
        return
      }
      let roomInfos = []
      if (this.selectRoomId === '1') {
        roomInfos = this.model.livingRoomIds.slice(1)
      } else {
        var index = this.model.livingRoomIds.findLastIndex(
            (t) => t.roomId == this.selectRoomId)
        var find = this.model.livingRoomIds[index]
        roomInfos.push(find)
      }
      this.$refs.adjustPriceForm.edit(roomInfos)
      this.$refs.adjustPriceForm.title = '批量调价'
    },
    submitForm() {
      const that = this
      that.$message.warning('未实现')
      return
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    }
  }
}
</script>
<style>
.tab_height .ant-table-wrapper{
  height: auto !important;
}
</style>

<style scoped>
/deep/ .ant-btn-link {
  flex: 1;
  color: rgba(255, 141, 26, 1) !important;
}

.disabled {
  pointer-events: none;
  opacity: 0.6;
  /* 可以根据需要添加其他样式 */
}
.menu {
  display: flex;
  flex-direction: column;
  flex: 1;
  color: #fff;
  margin-top: 12px;
}
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  /* top: 4px; */
  margin-left: 5px;
  font-size: 18px;
  color: #1890ff;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
/deep/ .noExpand .ant-table-row-expand-icon {
  display: none;

}
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

#livingCustomers-grid [class~="ant-col"] {
  border: #ccc 1px solid;
}
#livingCustomers-grid [class~="ant-col"]:last-child {
  border: 0;
}
.strike_style {
  color: red !important;
}
</style>
