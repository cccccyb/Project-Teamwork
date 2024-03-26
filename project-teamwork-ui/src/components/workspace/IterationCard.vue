<template>
  <el-card shadow="always" class="card">
    <template #header>
      <div class="card_head">
        <span style="font-size: 24px;color: rgba(204,95,70,0.85)">迭代</span>
        <el-button text @click="showAdvance" style="color: #409dfd;font-size: 15px;text-decoration: underline ">查看更多</el-button>
      </div>
    </template>
    <div class="card_item" v-for="item in cardSelectData">
      <div style="display: flex;margin-right: auto">
        <SvgIcons width="24px" height="24px" color="#ffffff" icon-class="iteration_menu"/>
        <p style="font-size: 18px;margin-left: 3px;font-weight: bold">{{ formatterTitle(item.name) }}</p>
      </div>
      <p style="font-size: 16px">
        <span style="margin-right: 5px">{{item.project.name}}项目</span></p>
      <el-tag
          disable-transitions
          size="default"
          effect="dark"
          style="font-size: 15px"
          :type="
                        item.status === 0
                            ? 'primary'
                            : item.status === 1
                            ? 'warning'
                            : 'success'
                    "
      >
        {{ formatStatus(item.status) }}
      </el-tag>
    </div>
    <el-empty v-if="cardSelectData.length === 0" description="暂无数据" image-size="80" />
  </el-card>
</template>

<script>
import {useProjIterationStore} from "@/store/ProjIteration.js";
import {mapState} from "pinia";
import SvgIcons from "@/assets/svg/index.vue";
import router from "@/router/index.js";

const projIterationStore = useProjIterationStore();

export default {
  components: {SvgIcons},
  computed:{
    ...mapState(useProjIterationStore,['cardSelectData'])
  },
  data() {
    return {}
  },
  methods: {
    formatterTitle(title) {
      if (title.length > 20) {
        return title.substring(0, 20) + ' ...'
      } else {
        return title
      }
    },
    formatDate(time) {
      return new Date(time).toLocaleString()
    },
    showAdvance(){
      router.push('/main/projectManage/projectAll')
    },
    formatStatus(status) {
      return status === 0 ? '未开始' : status === 1 ? '进行中' : '已完成'
    }
  },
  created() {
    projIterationStore.getCardSelectData()
  }
}
</script>

<style scoped>
.card{
  height: 100%;
  border-radius: 5px 5px 10px 10px;
  border-top: #0052cb 4px solid;
}

/deep/ .el-card__header{
  padding: 15px 18px;
  height: 55px;
  min-height: 20px;
}
/deep/ .el-card__body{
  padding: 0;
}
.card_head{
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bolder;
}
.card_item{
  display: flex;
  height: 7vh;
  min-height: 30px;
  align-items: center;
  justify-content: flex-end;
  padding: 0 18px;
}
.card_item:hover{
  background: rgba(204, 201, 201, 0.3);
}
</style>