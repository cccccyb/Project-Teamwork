import { defineStore } from 'pinia';
import request from "@/util/request.js";
import {LOGIN_SUCCESS} from "@/constants/Common.constants.js";
import router  from "@/router/index.js";
import {ElMessage} from "element-plus";
export const useUserStore=defineStore('user',{
    state:()=>{
        return{
            isLogin:true
        }
    },
    persist:true,
    getters:{},
    actions:{
        //登录
        login(loginForm){
            request.post('/user/login', loginForm).then(async response => {
                console.log(response)
                if (response.data.code === LOGIN_SUCCESS) {
                    //后端使用jwt生成token返回到前端，前端存储token
                    localStorage.setItem("sysUser", loginForm.username)
                    localStorage.setItem("token", response.data.data)
                    await router.push({
                        path: "/main",
                        query: {username: loginForm.username}
                    });
                    ElMessage({
                        message: response.data.msg,
                        type: 'success'
                    });
                } else {
                    console.log(response.data);
                    ElMessage({
                        message: response.data.msg,
                        type: 'error'
                    });
                }
            })
        },
        logout() {
            localStorage.clear()
            router.replace("/login").then(r => {})
        },
    }
})
// // 动态导入拿到所有页面 eg: {/src/views/test/index.vue: () => import("/src/views/test/index.vue")}
// const views = import.meta.glob('@/views/**/**.vue');
// import { useRoute, useRouter } from 'vue-router';
// import store from '@/store';
//
// export const useUserStore = defineStore('user', () => {
//   const route = useRoute();
//   const router = useRouter();
//   let isLogin = ref(false);
//   let tokenObj = ref({});
//   let userObj = ref({});
//   let routerMap = ref({}); // 全路径'/system/user' -> 路由信息
//

//
//     // 跳转登录页
//     router.push(`/login?redirect=${route.fullPath}`);
//     // window.location.href = '/login';
//     location.reload(); // 强制刷新页面
//   }
//   const routerList = computed(() => {
//     // 拿到后台的权限数据
//     return generateRouterList({}, userObj.value.permissionTreeList);
//   });
//
//   // 生成侧边栏菜单 & 权限路由数据
//   function generateRouterList(parentObj, permList) {
//     let result = [];
//     if (!permList || permList.length === 0) {
//       return result;
//     }
//
//     for (let index = 0; index < permList.length; index++) {
//       let permItem = permList[index];
//
//       // 填充字段数据
//       if (!permItem.meta) {
//         permItem.meta = {};
//       }
//       if (!permItem.meta.isParentView) {
//         permItem.meta.isParentView = false;
//       }
//       if (!permItem.meta.sort) {
//         permItem.meta.sort = 10000;
//       }
//
//       let title = permItem.meta.title;
//       if (title) {
//         if (parentObj.meta) {
//           // [子级]
//           // 面包屑数据
//           permItem.meta.breadcrumbItemList = parentObj.meta.breadcrumbItemList.concat([title]);
//           // 全路径
//           permItem.meta.fullPath = parentObj.meta.fullPath + '/' + permItem.path;
//         } else {
//           // [顶级]
//           permItem.meta.breadcrumbItemList = [title];
//           permItem.meta.fullPath = permItem.path;
//         }
//       }
//
//       // 组件页面显示处理
//       permItem.component = views[`/src/views/${permItem.component}.vue`];
//
//       routerMap.value[permItem.meta.fullPath] = permItem;
//
//       // 递归处理
//       if (permItem.children.length > 0) {
//         permItem.children = generateRouterList(permItem, permItem.children);
//       }
//
//       result.push(permItem);
//     }
//
//     // 从小到大 升序
//     result = result.sort((a, b) => {
//       return a.meta.sort - b.meta.sort;
//     });
//     return result;
//   }
//
//   return { isLogin, login, logout, tokenObj, userObj, getUserInfo, routerList, routerMap };
// });
