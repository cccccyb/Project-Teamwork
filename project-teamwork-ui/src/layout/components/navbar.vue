<template>
  <div class="app p-x-10">
    <!--面包屑-->
    <div style="margin-right: auto;min-width: 600px">
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item v-for="breadcrumb in breadcrumbList" :key="breadcrumb.path">
          <a v-if="!(breadcrumb.redirect && breadcrumb.meta.isMenu)" :href="breadcrumb.path">{{ breadcrumb.meta.title }}</a>
          <span v-else>{{ breadcrumb.meta.title }}</span>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>


    <el-dropdown trigger="hover" placement="bottom">
      <div style="display: flex" class="user_info">
        <div class="user">
          <SvgIcons width="100%" height="100%" color="#304156" icon-class="user" />
        </div>
        <div class="username">
          <span>{{username}}</span>
        </div>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <router-link to="/main">
            <el-dropdown-item>首页</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click="logout">退出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <div class="bt_logout">
      <SvgIcons width="40px" height="35px" color="#304156" icon-class="logout" @click="logout"/>
    </div>
  </div>
</template>
<script>
import {ArrowRight} from "@element-plus/icons-vue";
import {useSettingStore} from "@/store/setting.js";
import {useUserStore} from "@/store/user.js";
import {mapState} from 'pinia'

const settingStore = useSettingStore()
const userStore = useUserStore()
export default {
  components: {ArrowRight},
  data() {
    return {}
  },
  methods: {
    logout(){
      userStore.logout()
    }
  },
  computed: {
    ...mapState(useUserStore,['username']),
    ArrowRight() {
      return ArrowRight
    },
    breadcrumbList() {
      return this.$route.matched
    }
  },
  created() {
    userStore.$state.username=localStorage.getItem('username')
  }
}
</script>
<style lang="scss" scoped>
.app {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100%;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
}

.el-breadcrumb {
  font-size: 22px;
  margin-left: 14px;
}
.user_info:focus-visible {
  outline: unset;
}
.user{
  width: 40px;
  height: 40px;
  border-radius: 20px;
  background: #304156;
}
.username{
  margin-left: 8px;
  font-size: 17px;
  line-height: 40px;
  font-weight: bold;
}
.bt_logout{
  cursor: pointer;
  margin: 0 20px;
}
</style>
