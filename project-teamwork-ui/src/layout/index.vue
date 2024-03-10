<template>
  <div class="main">
      <!-- 左侧 -->
      <div id="LayoutLeft" class="layout-left">
        <sidebar class="sidebar" :isCollapse="isCollapse"/>
        <!-- 控制菜单的收缩 -->
        <div @click="shrinkMenu" class="shrink">
          <el-icon :size="25"><Expand v-if="isCollapse"/><Fold v-else/></el-icon>
        </div>
      </div>
      <!-- 右侧 -->
      <div id="LayoutRight" class="layout-right">
        <div id="top">
          <!-- 顶部导航栏 -->
          <navbar style="height: 50px"/>
          <!-- Tabs标签页 -->
          <div style="height: 20px">
          </div>
        </div>
        <div class="layout-content">
          <!-- 主页面 -->
          <app-main/>
        </div>
      </div>
  </div>
</template>

<script>
import Sidebar from "@/layout/components/sidebar.vue";
import Navbar from "@/layout/components/navbar.vue";
import AppMain from "@/layout/components/app-main.vue";
import {useUserStore} from "@/store/user.js";
import {useSettingStore} from "@/store/setting.js"
import {mapState} from 'pinia'
import router from "@/router/index.js";
import {Expand, Fold} from "@element-plus/icons-vue";

const userStore = useUserStore()
const settingStore=useSettingStore()

export default {
  components: {AppMain, Expand, Fold,Navbar, Sidebar},
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
}

.layout-left {
  /* 左侧不设置宽度 */
  width: auto;
  height: 100vh;
  position: relative;
}
.layout-right {
  height: 100%;
  /* flex关键代码，让右侧占满剩余宽度 */
  flex: 1;
  background: #dbe9f8;
  padding: 15px;
  box-sizing: border-box;
}
/* 业务页面容器 */
.layout-content {
  width: 100%;
  height: 100%;
  background: #fff;
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
