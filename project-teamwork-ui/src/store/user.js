import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {DATABASE_DELETE_ERROR, DATABASE_DELETE_OK, LOGIN_SUCCESS} from "@/constants/Common.constants.js";
import router from "@/router/index.js";
import {ElMessage, ElMessageBox} from "element-plus";

export const useUserStore = defineStore('user', {
    state: () => ({

        isLogin: true,
        username: '',
        allUserList:[],
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
                    localStorage.setItem('uid',response.data.msg)
                    await router.push({
                        path: "/main",
                        query: {username: loginForm.username}
                    });
                    ElMessage({
                        message: '登录成功',
                        type: 'success'
                    });
                } else {
                    ElMessage({
                        message: '用户不存在，请重新登录！',
                        type: 'error'
                    })
                }
            })
        },
        //退出登录
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
        //获得所有用户
        async getAllUser() {
            await request.get('/user/getAll').then((response) => {
                this.allUserList = response.data.data
            })
        }
    }
})
