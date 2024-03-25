<template>
<!--    <el-scrollbar>-->
  <el-menu
      :collapse="isCollapse"
      :unique-opened="true"
      :collapse-transition="true"
      :default-active="$route.path"
      :router="true"
      class="menu"
      :background-color="COLOR_MENU_BACKGROUND()"
      :text-color="COLOR_FONT_MAIN()"
      :active-text-color="COLOR_PRODUCTION()"
  >
    <!--项目名-->
    <el-menu-item
        :disabled="true"
        style="padding:0 10px;cursor: pointer;opacity: 1;border-bottom: 2px #ddd solid;"
    >
      <SvgIcons width="48px" height="48px" color="#ffffff" icon-class="goBack" @click="goBack"/>
      <template #title>
        <span class="menu-project-name">{{this.currentProject.name}}</span>
      </template>
    </el-menu-item>
    <template v-for="route in this.routers">
      <!--一级菜单-->
      <el-menu-item
          v-if=" !route.meta.hasChildren && route.meta.isMenu"
          :key="route.path"
          :index="route.path? '/projectDetail/'+route.path: ''"
      >
        <SvgIcons width="28px" height="28px" color="#ffaa00" :icon-class="route.meta.icon" />
        <template #title><span style="margin-left: 10px">{{ route.meta.title }}</span></template>
      </el-menu-item>
    </template>
  </el-menu>
<!--    </el-scrollbar>-->
</template>
<script>
import router from "@/router/index.js";
import {Document, Location, Setting} from "@element-plus/icons-vue";
import {
  COLOR_FONT_MAIN,
  COLOR_MENU_BACKGROUND,
  COLOR_PRODUCTION
} from "@/constants/Common.constants.js";
import {mapState} from 'pinia'
import {useDetailProjectStore} from "@/store/detailProject.js";

const detailProjectStore=useDetailProjectStore()
export default {
  name: "sidebar",
  props:{isCollapse:Boolean},
  components: {Setting, Document, Location},
  data() {
    return {
      // routers: [],
      activePath: ''
    }
  },
  methods: {
    COLOR_MENU_BACKGROUND() {
      return COLOR_MENU_BACKGROUND
    },
    COLOR_PRODUCTION() {
      return COLOR_PRODUCTION
    },
    COLOR_FONT_MAIN() {
      return COLOR_FONT_MAIN
    },
    goBack(){
      localStorage.removeItem('pid')
      router.push('/main/projectManage/projectPerson')
      // router.go(-1)
    }
  },
  computed: {
    routers(){
      return router.options.routes[3].children
    },
    ...mapState(useDetailProjectStore,['currentProject','currentPid'])
  },
  created() {
  },
  mounted() {
    // this.routers = router.options.routes[3].children
  }
}
</script>
<style scoped lang="scss">
.menu{
  height: 100vh;
}
.menu:not(.el-menu--collapse) {
  width: calc(10rem + 5vw);
}

.menu-top > * {
  padding: 0;
}

.menu-project-name {
  width: 100%;
  height: 65%;
  padding: 0 5px;
  font-size: 18px;
  margin-left: 10px;
  color: var(--main-color);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border: rgba(178, 152, 141, 0.85) 2px solid;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  font-weight: bold;
}
/* el-menu相关样式 */
.el-menu {
  height: 100%;
  border-right-width: 0;
}

::v-deep .el-menu-item,{
  height: 75px;
  line-height: 75px;
  font-size: 16px;
}

/* 必须设置 */
.sidebar:not(.el-menu--collapse) {
  width: 200px;
}


</style>