<template>
  <div>
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane label="迭代&nbsp;&nbsp;&nbsp;|" name="iteration"><ProjIteration/></el-tab-pane>
      <el-tab-pane label="全部事项" name="all">Config</el-tab-pane>
      <el-tab-pane label="需求" name="requirement">Role</el-tab-pane>
      <el-tab-pane label="任务" name="task">Task</el-tab-pane>
      <el-tab-pane label="缺陷" name="bug">Task</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ProjIteration from "@/components/project/details/Proj-Iteration.vue";
export default {
  components: {ProjIteration},
  data() {
    return {
      activeName:'iteration'
    }
  },
  methods: {
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