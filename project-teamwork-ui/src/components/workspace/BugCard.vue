<template>
    <el-card shadow="always" class="card">
      <template #header>
        <div class="card_head">
          <span style="font-size: 24px;color: rgba(71,138,173,0.85)">缺陷</span>
          <el-button text @click="showAdvance" style="color: #409dfd;font-size: 15px;text-decoration: underline ">查看更多</el-button>
        </div>
      </template>
      <div class="card_item" v-for="item in cardSelectData">
        <div style="display: flex;margin-right: auto">
          <SvgIcons width="30px" height="28px" color="#ffffff" icon-class="bug_menu"/>
          <p style="font-size: 18px;"><span style="font-weight: bold;margin-right: 5px">#{{item.id}}</span>{{ formatterTitle(item.title) }}</p>
        </div>
          <p style="font-size: 15px">
            <el-tag round style="font-size: 15px">
              {{item.creator.username}}
          </el-tag><span style="margin-left: 5px">创建于{{formatDate(item.createTime)}}</span></p>
      </div>
      <el-empty v-if="cardSelectData.length === 0" description="暂无数据" image-size="80" />
    </el-card>
</template>

<script>
import {useProjBugStore} from "@/store/ProjBug.js";
import {mapState} from "pinia";
import SvgIcons from "@/assets/svg/index.vue";
import router from "@/router/index.js";

const projBugStore = useProjBugStore();

export default {
  components: {SvgIcons},
  computed:{
    ...mapState(useProjBugStore,['cardSelectData'])
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
    }
  },
  created() {
    projBugStore.getCardSelectData()
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