<template>
  <!-- <h1>{{ route.meta }}</h1> -->
  <div class="flex h-full w-full">
    <!-- 侧边栏菜单 -->
    <sidebar v-show="isShowMenu" id="sidebar" style="width: 200px"/>

    <div class="flex-1 flex-column">
      <div id="top">
        <!-- 顶部导航栏 -->
        <navbar style="height: 50px"/>
        <!-- Tabs标签页 -->
        <div :style="{ width: appMainWidth + 'px' }">
<!--          <tabs-view/>-->
        </div>
      </div>

      <!-- 主页面 -->
      <app-main class="flex-1 app-main" :height="appMainHeight + 'px'" :width="appMainWidth + 'px'"/>
    </div>
  </div>

  <div v-if="!isLogin || (isLogin && $route.meta.isParentView)" class="h-full">
    <router-view/>
  </div>
</template>

<script>
import Sidebar from "@/layout/components/sidebar.vue";
import Navbar from "@/layout/components/navbar.vue";
import TabsView from "@/layout/components/tabs-view.vue";
import AppMain from "@/layout/components/app-main.vue";
import {useUserStore} from "@/store/user.js";
import {useSettingStore} from "@/store/setting.js"
import {mapState} from 'pinia'

const userStore = useUserStore()

export default {
  components: {AppMain, TabsView, Navbar, Sidebar},
  data() {
    return {
      appMainWidth: 0,
      appMainHeight: 0
    }
  },
  methods: {
    calWidthAndHeight() {
      let sidebar = document.getElementById('sidebar');
      let sidebarW = sidebar ? sidebar.offsetWidth : 0;
      this.appMainWidth = window.innerWidth - sidebarW;

      let top = document.getElementById('top');
      let topH = top ? top.offsetHeight : 0;
      this.appMainHeight = window.innerHeight - topH;
    },

  },
  watch: {
    isLogin: {
      immediate: false,
      deep: true,
      handler(newValue) {
        this.calWidthAndHeight();
      },
    },
    isShowMenu: {
      immediate: false,
      deep: true,
      handler(newValue) {
        this.calWidthAndHeight();
      },
    }
  },
  mounted() {
    // 窗口宽高变化时触发 -- tips：window.onresize只能在项目内触发1次
    window.addEventListener('resize', () => {
      this.calWidthAndHeight()
    })
  },
  updated() {
    // 注册一个回调函数，在组件因为响应式状态变更而更新其 DOM 树之后调用。
    this.calWidthAndHeight()
  },
  computed: {
    ...mapState(useUserStore,['isLogin']),
    ...mapState(useSettingStore,["isShowMenu"])
  }
}

</script>
<style lang="scss" scoped>
.app-main {
  // height: calc(100vh - 50px); // 满屏 - navbar
}
</style>
