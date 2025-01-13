<template>
<div>
    <a-date-picker v-model="selectedDate" :disabledDate="disabledDate" @change="handleDateChange" />
    <a-table :columns="columns" :dataSource="dataSource" :pagination="false" />
</div>
</template>

<script>

export default {
  data() {
    return {
      selectedDate: new Date(),
      columns: [
        {
          title: '日期',
          dataIndex: 'date',
        },
        {
          title: '单人间',
          dataIndex: 'singleRoom',
        },
        {
          title: '双人间',
          dataIndex: 'doubleRoom',
        },
        {
          title: '三人间',
          dataIndex: 'tripleRoom',
        },
      ],
      dataSource: [],
    };
  },
  mounted() {
    this.handleDateChange(this.selectedDate);
  },
  methods: {
    disabledDate(current) {
      return current && current < new Date();
    },
    handleDateChange(date) {
      const dataSources = [];
      for (let i = 0; i < 30; i++) {
        const currentDate = new Date(date.getTime() + i * 24 * 60 * 60 * 1000);
        const singleRoom = {
          key: `singleRoom-${i}`,
          roomType: '单人间',
          roomNumber: `101-${i}`,
        };
        const doubleRoom = {
          key: `doubleRoom-${i}`,
          roomType: '双人间',
          roomNumber: `201-${i}`,
        };
        const tripleRoom = {
          key: `tripleRoom-${i}`,
          roomType: '三人间',
          roomNumber: `301-${i}`,
        };
        dataSources.push({
          key: currentDate.toLocaleDateString(),
          date: currentDate.toLocaleDateString(),
          singleRoom,
          doubleRoom,
          tripleRoom,
        });
      }
      this.dataSource = dataSources;
    },
  },
};
</script>