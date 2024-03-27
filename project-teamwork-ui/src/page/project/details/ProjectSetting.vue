<template>
  <div>
    <div class="font_head">
      <h2 style="font-size: 23px;margin-left: 10px;color: #646161">项目与成员&nbsp&nbsp&nbsp|</h2>
    </div>
    <div class="tab_head">
      <el-tabs v-model="activeName" @tab-click="handleTabClick">
        <el-tab-pane label="基本信息" name="info"><ProjectInfo v-if="infoTab"/></el-tab-pane>
        <el-tab-pane label="项目成员" name="members"><ProjectMembers v-if="membersTab"/></el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import ProjectInfo from "@/components/project/details/ProjectInfo.vue";
import ProjectMembers from "@/components/project/details/ProjectMembers.vue";

export default {
  components: {ProjectInfo,ProjectMembers},
  data() {
    return {
      activeName:'info',
      infoTab: true,
      membersTab: false
    }
  },
  methods: {
    handleTabClick(tab) {
      switch (tab.props.name) {
        case 'info':
          this.infoTab = true
          this.membersTab = false
          break;
        case 'members':
          this.infoTab = false
          this.membersTab = true
          break;
      }
    }
  },
  created() {
  }
}
</script>

<style scoped>
.font_head{
  width: 100%;
  height: 50px;
  margin-top: 10px;
  padding: 5px 0;
}
.tab_head{
  width: 100%;
  height: 100%;
  padding: 0 10px;
}
::v-deep .el-tabs__item.is-active {
  color: #0052cb;
  font-size: 21px;
}
::v-deep .el-tabs__item {
  font-size: 19px;
}
/* 下划线切换高亮 */
::v-deep .el-tabs__active-bar {
  background-color: #0052cb;
  height: 3px;
}
</style>