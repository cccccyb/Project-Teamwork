<template>
  <el-scrollbar style="height: 100vh;width: 100vw">
    <div class="main">
      <!-- 左侧 -->
      <div id="LayoutLeft" class="layout-left">
        <DetSidebar class="sidebar" :isCollapse="isCollapse"/>
        <!-- 控制菜单的收缩 -->
        <div @click="shrinkMenu" class="shrink">
          <el-icon :size="25"><Expand v-if="isCollapse"/><Fold v-else/></el-icon>
        </div>
      </div>
      <!-- 右侧 -->
      <div id="LayoutRight" class="layout-right">
        <div id="top" style="height: 60px">
          <!-- 顶部导航栏 -->
          <DetNavbar/>
        </div>
        <div class="layout-content">
          <!-- 主页面 -->
          <DetAppMain/>
        </div>
      </div>
    </div>
  </el-scrollbar>
</template>

<script>
import DetSidebar from "@/page/project/details/components/det-sidebar.vue";
import DetNavbar from "@/page/project/details/components/det-navbar.vue";
import DetAppMain from "@/page/project/details/components/det-main.vue";
import {useUserStore} from "@/store/user.js";
import {useSettingStore} from "@/store/setting.js"
import {useDetailProjectStore} from "@/store/detailProject.js";
import {mapState} from 'pinia'
import router from "@/router/index.js";
import {Expand, Fold} from "@element-plus/icons-vue";

const userStore = useUserStore()
const settingStore=useSettingStore()
const detailProjectStore=useDetailProjectStore()

export default {
  components: {DetAppMain, Expand, Fold,DetNavbar, DetSidebar},
  data() {
    return {
    }
  },
  methods: {
    shrinkMenu() {
      this.isCollapse = !this.isCollapse;
    }
  },
  watch: {
  },
  created() {
    detailProjectStore.getCurrentProject(this.$route.query.pid)
  },
  mounted() {
  },
  updated() {
  },
  computed: {
    ...mapState(useUserStore, ['isLogin']),
    ...mapState(useSettingStore, ["isShowMenu", 'isCollapse']),
    isCollapse: {
      get() {
        return settingStore.$state.isCollapse
      },
      set(val) {
        // settingStore.$patch({
        //   isCollapse : val
        // })
        settingStore.$state.isCollapse=val
      }
    }
  }
}

</script>
<style lang="scss" scoped>
.main {
  /* flex布局 */
  display: flex;
  width: 100vw;
  height: 100%;
  overflow: hidden;
}

.layout-left {
  /* 左侧不设置宽度 */
  width: auto;
  height: 100vh;
  min-height: 1000px;
  position: relative;
}
.layout-right {
  height: 100%;
  /* flex关键代码，让右侧占满剩余宽度 */
  flex: 1;
  background: #dfe2e5;
  padding: 15px;
  box-sizing: border-box;
  min-width: 0;  /*右边内容超出会影响布局*/
}
/* 业务页面容器 */
.layout-content {
  width: 100%;
  height: 100%;
  margin-top: 10px;
  padding: 10px 20px;
  background: #fff;
  min-height: 900px;
}

/* 控制菜单收缩 */
.shrink {
  position: absolute;
  top: 50%;
  right: -25px;
  transform: translateY(-50%);
  width: 25px;
  height: 30px;
  border: 1px solid #b3c0d1;
  background: #b3c0d1;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 3px;
  cursor: pointer;
  z-index: 9999;
}
</style>
