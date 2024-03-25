<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane label="迭代&nbsp;&nbsp;&nbsp;|" name="iteration">
        <ProjIteration v-if="iterationTab"/>
      </el-tab-pane>
      <el-tab-pane label="全部事项" name="all">
        <ProjectItemTab v-if="allTab"/>
      </el-tab-pane>
      <el-tab-pane label="需求" name="requirement">
        <ProjectRequireTab v-if="requireTab"/>
      </el-tab-pane>
      <el-tab-pane label="任务" name="task">
        <ProjectTaskTab v-if="taskTab"/>
      </el-tab-pane>
      <el-tab-pane label="缺陷" name="bug">
        <ProjectBugTab v-if="bugTab"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ProjIteration from "@/components/iteration/Proj-Iteration.vue";
import ProjectRequireTab from "@/components/require/ProjectRequireTab.vue";
import ProjectTaskTab from "@/components/task/ProjectTaskTab.vue";
import ProjectBugTab from "@/components/bug/ProjectBugTab.vue";
import ProjectItemTab from "@/components/item/ProjectItemTab.vue";

export default {
  components: {ProjIteration, ProjectItemTab, ProjectRequireTab, ProjectTaskTab, ProjectBugTab},
  data() {
    return {
      activeName: 'iteration',
      iterationTab: true,
      allTab: false,
      requireTab: false,
      taskTab: false,
      bugTab: false
    }
  },
  methods: {
    handleTabClick(tab) {
      switch (tab.props.name) {
        case 'iteration':
          this.iterationTab = true
          this.allTab = false
          this.requireTab = false
          this.taskTab = false
          this.bugTab = false
          break;
        case 'all':
          this.iterationTab = false
          this.allTab = true
          this.requireTab = false
          this.taskTab = false
          this.bugTab = false
          break;
        case 'requirement':
          this.iterationTab = false
          this.allTab = false
          this.requireTab = true
          this.taskTab = false
          this.bugTab = false
          break;
        case 'task':
          this.iterationTab = false
          this.allTab = false
          this.requireTab = false
          this.taskTab = true
          this.bugTab = false
          break;
        case 'bug':
          this.iterationTab = false
          this.allTab = false
          this.requireTab = false
          this.taskTab = false
          this.bugTab = true
          break;
      }
    }
  },
  created() {
  }
}
</script>

<style scoped>
::v-deep .el-tabs__item.is-active {
  color: #0052cb;
  font-size: 23px;
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