import {createRouter, createWebHistory} from "vue-router";
import request from "@/util/request.js";
import {SYSTEM_OK} from "@/constants/Common.constants.js";

// 创建路由规则
let routes = [
    {
        path: '/',
        redirect: '/login',
        meta:{
            isParentView: true
        }
    },
    {
        path: '/login', // URL 地址
        name: 'login', // 名称
        component: () => import('../page/Login.vue'),  // 渲染该组件
        meta:{
            isParentView: true
        }
    },
    {
        path: '/main',
        name: 'main',
        component: () => import('@/layout/index.vue'),
        redirect: '/main/c1', //重定向，默认为第一个子视图
        meta:{
            isParentView: true
        },
        children: [
            {
                path: 'c1',
                name: 'c1'
            },
            {
                path: 'c2',
                name: 'c2'
            }
        ]
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../page/404.vue'),
        meta:{
            isParentView: true
        }
    },
    {
        path: "/:catchAll(.*)",
        redirect: '/404',
        meta:{
            isParentView: true
        }
    }
]

// 创建路由
const router = createRouter({
    // 使用 history 模式
    history: createWebHistory(),
    // 路由规则
    routes
})

const inWhiteList=['/','/login', '/404'];
//全局前置守卫
router.beforeEach( (to, from, next) => {
    const token = localStorage.getItem("token")
    //白名单 放行
    if (inWhiteList.includes(to.path)) {
        next()
    } else {
        //用户已登录
        if (token!=null) {
            //如果判断存在token的话，就请求后端的接口，这样就经过jwt拦截器的验证
             request.post("/user/authentication").then(res => {
                if (res.data.code === SYSTEM_OK) {
                    // 如果请求成功，前端就next()
                    next()
                }
            })
        } else {
            next(`/login`)
        }
    }
})

// 将路由对象暴露出去
export default router