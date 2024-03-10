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
      <!--        <el-svg :size="SIZE_ICON_LG()">-->
      <!--          <svg-pinnacle-pinnacle :color="COLOR_PRODUCTION()"/>-->
      <!--        </el-svg>-->
      <SvgIcons width="32px" height="32px" color="#ffffff" icon-class="test" />
      <template #title>
        <span class="menu-production-name">CYB-TeamWork</span>
      </template>
    </el-menu-item>
    <template v-for="route in this.routers">
      <!--一级菜单-->
      <el-menu-item
          v-if="!route.meta.hasChildren || route.meta.isMenu"
          :key="route.path"
          :index="route.path? '/main/'+route.path: ''"
      >
        <!--          <el-svg>-->
        <!--            <component :is="route.meta.svg"/>-->
        <!--          </el-svg>-->
        <template #title>{{ route.meta.title }}</template>
      </el-menu-item>
      <!--二级菜单-->
      <el-sub-menu
          v-if="route.meta.isMenu && route.meta.hasChildren && route.meta.requireChildMenu"
          :key="route.path"
          :index="route.path? '/main/'+route.path: ''">
        <template #title>
          <!--            <el-svg>-->
          <!--              <component :is="route.meta.svg"/>-->
          <!--            </el-svg>-->
          <span>{{ route.meta.title }}</span>
        </template>
        <el-menu-item
            v-for="sub in route.children"
            :key="sub.path"
            :index="sub.path? route.path ? '/main/'+route.path + '/' + sub.path: '': ''"
        >
          <!--            <el-svg>-->
          <!--              <component :is="sub.meta.svg"/>-->
          <!--            </el-svg>-->
          <template #title>{{ sub.meta.title }}</template>
        </el-menu-item>
      </el-sub-menu>
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
import {useUserStore} from "@/store/user.js";
import {useSettingStore} from "@/store/setting.js"
import {mapState} from 'pinia'

export default {
  name: "sidebar",
  props:{isCollapse:Boolean},
  components: {Setting, Document, Location},
  data() {
    return {
      routers: [],
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
    }
  },
  computed: {
    SVG_ICONS() {
      return SVG_ICONS
    }
  },
  created() {
  },
  mounted() {
    this.routers = router.options.routes[2].children
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

.menu-production-name {
  width: 100%;
  padding: 0 5px;
  font-size: 18px;
  color: var(--main-color);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
/* el-menu相关样式 */
.el-menu {
  height: 100%;
  border-right-width: 0;
}

::v-deep .el-menu-item {
  height: 70px;
  line-height: 70px;
  font-size: 15px;
}

/* 必须设置 */
.sidebar:not(.el-menu--collapse) {
  width: 200px;
}

</style>