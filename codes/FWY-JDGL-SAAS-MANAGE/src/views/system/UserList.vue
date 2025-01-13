<template>
  <a-card style="width: 100%; height: 100%">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1">
        <span slot="tab"> 角色管理 </span>
        <role-list></role-list>
      </a-tab-pane>
      <a-tab-pane key="2">
        <span slot="tab"> 员工管理 </span>
        <yuangong-list></yuangong-list>
      </a-tab-pane>
      <a-tab-pane key="3">
        <span slot="tab"> 业主管理 </span>
        <yezhu-list></yezhu-list>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
import YuangongList from "./YuangongList.vue";
import YezhuList from "./YezhuList.vue";
import RoleList from './RoleList'
export default {
  components: {
    RoleList,
    YuangongList,
    YezhuList,
  },
  data() {
    return {};
  },
};
</script>

<style scoped>
.main {
  height: 70% !important;
}
</style>
