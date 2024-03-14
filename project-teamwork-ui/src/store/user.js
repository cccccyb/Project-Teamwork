import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {DATABASE_DELETE_ERROR, DATABASE_DELETE_OK, LOGIN_SUCCESS} from "@/constants/Common.constants.js";
import router from "@/router/index.js";
import {ElMessage, ElMessageBox} from "element-plus";

export const useUserStore = defineStore('user', {
    state: () => ({

        isLogin: true,
        username: ''
    }),
    getters: {},
    actions: {
        //登录
        login(loginForm) {
            request.post('/user/login', loginForm).then(async response => {
                console.log(response)
                if (response.data.code === LOGIN_SUCCESS) {
                    //后端使用jwt生成token返回到前端，前端存储token
                    localStorage.setItem("username", loginForm.username)
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
            ElMessageBox.confirm('确定是否要退出登录？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    this.isLogin = false
                    localStorage.clear()
                    router.replace("/login").then(r => {
                    })
                })
                .catch(() => {
                })
        },
    }
})
