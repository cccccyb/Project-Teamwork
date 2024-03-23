<template>
  <div class="main">
    <div class="page_name">
      <h1>{{this.$route.meta.title}}</h1>
    </div>
    <div class="bt_add">
      <el-button
          size="large"
          type="primary"
          @click="openAddProjectDialog"
      >添加项目</el-button
      >
<!--      <el-button type="primary" :size="'large'" style="padding: 0 8px" @click="getLoading"-->
<!--      >-->
<!--        <el-icon size="25"><Refresh /></el-icon>刷新数据</el-button-->
<!--      >-->
    </div>
<!--添加项目对话框-->
    <el-dialog
        v-model="this.dialogAddVisible"
        center
        :close-on-click-modal="false"
        :before-close="handleDialogClose"
        style="min-width: 550px; max-width: 1000px;z-index: 10"
    >
      <template #header>
        <h2 style="color: rgba(71,138,173,0.85)">添加项目</h2>
      </template>
      <ProjectCommitForm ref="addForm"/>
    </el-dialog>
  </div>

</template>

<script>
import ProjectCommitForm from "@/components/project/ProjectCommitForm.vue";
import {useProjectStore} from "@/store/project.js";
import {mapState} from "pinia";
import {Refresh} from "@element-plus/icons-vue";
const projectStore=useProjectStore()

export default {
  components: {Refresh, ProjectCommitForm},
  data() {
    return {}
  },
  methods: {
    //刷新数据按钮
    // getLoading(){
    //   projectStore.getLoading(projectStore.getCurrentViewPage())
    // },
    // 打开添加项目对话框
    openAddProjectDialog() {
      projectStore.$state.dialogAddVisible=true
      this.$refs.addForm.resetForm()
    },
    handleDialogClose() {
      projectStore.$state.dialogAddVisible=false
      this.$refs.addForm.resetForm()
    }
  },
  computed:{
    ...mapState(useProjectStore,['dialogAddVisible'])
  },
  created() {
  }
}
</script>

<style scoped>
.main{
  display: flex;
  width: 100%;
  height: 100%;
  justify-content: flex-end;
  align-items: center;
  padding: 0 10px;
}
.page_name{
  margin-right: auto;
}
</style>