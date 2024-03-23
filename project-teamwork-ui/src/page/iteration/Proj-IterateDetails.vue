<template>
  <div class="main">
    <div class="iterate_head">
        <SvgIcons width="40px" height="48px" color="rgb(52, 69, 99)" icon-class="goBack" style="cursor: pointer" @click="goBack"/>
        <h2 style="font-size: 23px;margin-left: 10px">{{this.currentIteration.name}}&nbsp&nbsp&nbsp|</h2>
    </div>
    <div class="search_tab">
<!--      <el-input-->
<!--          v-model="input_search"-->
<!--          style="width: 300px;height: 40px"-->
<!--          placeholder="搜索迭代"-->
<!--          class="input_search"-->
<!--      >-->
<!--        <template #append>-->
<!--          <el-button>-->
<!--            <el-icon style="align-content: center" size="20" color="#0052cb">-->
<!--              <Search/>-->
<!--            </el-icon>-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </el-input>-->
      <el-tabs v-model="activeName" type="card">
        <el-tab-pane label="全部" name="all"><ProjectItemTab/></el-tab-pane>
        <el-tab-pane label="需求" name="requirement"><ProjectRequireTab/></el-tab-pane>
        <el-tab-pane label="任务" name="task"><ProjectTaskTab/></el-tab-pane>
        <el-tab-pane label="缺陷" name="bug"><ProjectBugTab/></el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";
import {useProjIterationStore} from "@/store/ProjIteration.js";
import ProjectRequireTab from "@/components/require/ProjectRequireTab.vue";
import ProjectTaskTab from "@/components/task/ProjectTaskTab.vue";
import ProjectBugTab from "@/components/bug/ProjectBugTab.vue";
import ProjectItemTab from "@/components/item/ProjectItemTab.vue";
import {mapState} from "pinia";
const projectIterationStore=useProjIterationStore()

export default {
  components: {ProjectRequireTab,ProjectItemTab,ProjectTaskTab,ProjectBugTab},
  data() {
    return {
      activeName: 'all',
      input_search:''
    }
  },
  methods: {
    goBack(){
      localStorage.removeItem('iterateId')
      router.push('/projectDetail/projectTeamwork')
    }
  },
  created() {
    let iterateId=localStorage.getItem('iterateId')===null?this.$route.query.itId:localStorage.getItem('iterateId')
    localStorage.setItem('iterateId',iterateId)
    projectIterationStore.getCurrentIteration(iterateId)
  },
  beforeRouteLeave(to,from,next) {
    localStorage.removeItem('iterateId')
    next()
  },
  computed:{
    ...mapState(useProjIterationStore,['currentIteration'])
  }
}
</script>

<style scoped>
::v-deep .el-tabs__item.is-active {
  color: #f2f2f3;
  font-size: 20px;
  background: rgb(52, 69, 99);
}

::v-deep .el-tabs__item {
  font-size: 18px;
  margin-right: 25px;
  border-radius: 5px;
  background: rgb(242, 243, 245);
}

/* 鼠标悬浮时样式 */
::v-deep .el-tabs__item:hover {
  color: rgba(71, 138, 173, 0.85);
}

/deep/ .el-tabs__nav{
  border: none !important;
}
/deep/.el-tabs--card > .el-tabs__header{
  border: none !important;
}
.main{
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.iterate_head {
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  border-bottom: #dddfe5 2px solid;
  margin-top: 10px;
  padding: 5px 0;
}

.search_tab {
  width: 100%;
  height: 100%;
  margin-top: 20px;
  padding: 0 10px;
}
</style>