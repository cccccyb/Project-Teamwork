import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {
    DATABASE_DELETE_ERROR,
    DATABASE_DELETE_OK,
    DATABASE_SELECT_OK,
    LOGIN_SUCCESS
} from "@/constants/Common.constants.js";
import router from "@/router/index.js";
import {ElMessage, ElMessageBox} from "element-plus";

export const useUserStore = defineStore('user', {
    state: () => ({
        isLogin: true,
        allUserList: [],
        total: 0,
        pageSize: 10,
        currentPage: 1,
        loading: false,
        switchLoading: false,
        selectData: [],
        multiDeleteSelection: [],
    }),
    getters: {},
    actions: {
        //添加睡眠时间
        sleep(d) {
            return new Promise((resolve) => setTimeout(resolve, d))
        },
        //重置刷新数据
        async getLoading() {
            this.loading = true
            await this.sleep(300)
            await this.getAllUserPage(
                this.currentPage,
                this.pageSize,
                '',
                '',
                '',
                ''
            )
        },
        //登录
        login(loginForm) {
            request.post('/user/login', loginForm).then(async response => {
                console.log(response)
                if (response.data.code === LOGIN_SUCCESS) {
                    //后端使用jwt生成token返回到前端，前端存储token
                    localStorage.setItem("username", loginForm.username)
                    localStorage.setItem("token", response.data.data)
                    localStorage.setItem('uid', response.data.msg)
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
        },
        //分页查询所有用户，及其用户组、角色及模糊查询
        getAllUserPage(currentPage, pageSize, username, searchRole, searchGroup, enable) {
            void request
                .get('/user/page', {
                    params: {
                        currentPage,
                        pageSize,
                        username,
                        searchRole,
                        searchGroup,
                        enable
                    }
                })
                .then((response) => {
                    if (response.data.code === DATABASE_SELECT_OK) {
                        this.selectData = response.data.data
                        this.total = parseInt(response.data.msg)
                        this.loading = false
                    } else {
                        this.loading = false
                        ElMessage({
                            message: response.data.msg,
                            type: 'error'
                        })
                    }
                })
        }
    }

})
