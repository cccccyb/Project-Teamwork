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
      <el-button type="primary" :size="'large'"
      >
<!--        <el-icon :size="SIZE_ICON_MD()" style="color: white; margin-right: 3px">-->
<!--        <icon-pinnacle-reset /> </el-icon>-->
        刷新数据</el-button
      >
    </div>
<!--添加项目对话框-->
    <el-dialog
        v-model="dialogAddVisible"
        center
        :close-on-click-modal="false"
        :before-close="handleDialogClose"
        style="min-width: 450px; max-width: 900px"
    >
      <template #header>
        <h2 style="color: rgba(71,138,173,0.85)">添加项目</h2>
      </template>
      <ProjectCommitForm ref="addForm"/>
    </el-dialog>
  </div>

</template>

<script>
import ProjectCommitForm from "@/page/project/ProjectCommitForm.vue";
import {useProjectStore} from "@/store/project.js";
import {mapState} from "pinia";
import {SIZE_ICON_MD} from "@/constants/Common.constants.js";
const projectStore=useProjectStore()

export default {
  components: {ProjectCommitForm},
  data() {
    return {}
  },
  methods: {
    SIZE_ICON_MD() {
      return SIZE_ICON_MD
    },
    // 打开添加项目对话框
    openAddProjectDialog() {
      projectStore.$patch({
        dialogAddVisible : true,
        editFlag : false
      })
    },
    handleDialogClose() {
      projectStore.$patch({
        dialogEditVisible : false,
        dialogAddVisible : false,
        dialogShowVisible : false,
        editFlag : false,
        hackReset : false
      })
      this.$refs.addForm.$refs.addData.resetFields()
    },
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
}
.page_name{
  margin-right: auto;
}
</style>