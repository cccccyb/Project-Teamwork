<template>
  <!-- {{ route.meta }} -->
  <div class="app flex-between-center p-x-10">
    <el-button type="primary">
      <el-icon style="vertical-align: middle">
        <Expand />
      </el-icon>
    </el-button>
    <h1>navbar</h1>
<!--    <div class="flex-center-center">-->
<!--      <div class="m-r-10" style="cursor: pointer" @click="proxy.$store.settings.useSettingsStore().update">-->
<!--        <el-svg :size="22">-->
<!--          <component :is="proxy.$store.settings.useSettingsStore().isShowMenu ? 'Fold' : 'Expand'" />-->
<!--        </el-svg>-->
<!--      </div>-->

<!--      <el-breadcrumb :separator-svg="ArrowRight">-->
<!--        <el-breadcrumb-item :to="{ path: '/' }">home</el-breadcrumb-item>-->
<!--        <el-breadcrumb-item v-for="item in route.meta.breadcrumbItemList" :key="item">-->
<!--          <span class="text-color-grey">{{ item }}</span>-->
<!--        </el-breadcrumb-item>-->
<!--      </el-breadcrumb>-->
<!--    </div>-->

<!--    <wx-mp-account />-->

<!--    <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">-->
<!--      <div class="flex-center-center">-->
<!--        <el-avatar class="" :size="32" :src="userObj.avatarUrl" />-->
<!--        <div class="flex-center-center">-->
<!--          <span class="m-l-6"> {{ userObj.nickname }} </span>-->
<!--          <el-svg :size="20" style="width: 20px">-->
<!--            <ArrowDown />-->
<!--          </el-svg>-->
<!--        </div>-->
<!--      </div>-->
<!--      <template #dropdown>-->
<!--        <el-dropdown-menu>-->
<!--          <router-link to="/">-->
<!--            <el-dropdown-item>首页</el-dropdown-item>-->
<!--          </router-link>-->
<!--          <router-link to="/system/personal-center">-->
<!--            <el-dropdown-item>个人中心</el-dropdown-item>-->
<!--          </router-link>-->
<!--          <a target="_blank" href="https://gitee.com/zhengqingya">-->
<!--            <el-dropdown-item>Gitee</el-dropdown-item>-->
<!--          </a>-->
<!--          <el-dropdown-item @click="handleSettings">系统设置</el-dropdown-item>-->
<!--          <el-dropdown-item divided @click="logout">退出</el-dropdown-item>-->
<!--        </el-dropdown-menu>-->
<!--      </template>-->
<!--    </el-dropdown>-->

<!--    <settings ref="settingsRef" />-->
  </div>
</template>
<script>
// import { useRoute } from 'vue-router';
// const route = useRoute();
// import { ArrowRight } from '@element-plus/svg-vue';
// import { getCurrentInstance, toRefs } from 'vue';
// const { proxy } = getCurrentInstance();
// import settings from './settings.vue';
//
// let useUserStore = proxy.$store.user.useUserStore();
// let { logout } = useUserStore;
// let { userObj } = toRefs(useUserStore);
//
// function handleSettings() {
//   proxy.$refs.settingsRef.open();
// }
import {Expand} from "@element-plus/icons-vue";
import {useSettingStore} from "@/store/setting.js";
import {mapState} from 'pinia'

const settingStore=useSettingStore()
export default {
  components: {Expand},
  data(){
    return{

    }
  },
  methods:{
    // changeCollapsed(){
    //   settingStore.$state.isCollapsed=!this.isCollapsed
    //   localStorage.setItem('menuCollapsed', this.isCollapsed.toString())
    // }
  },
  computed:{

  }
}
</script>
<style lang="scss" scoped>
.app {
  // -webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
}
</style>
