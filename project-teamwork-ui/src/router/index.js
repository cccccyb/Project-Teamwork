import {createRouter, createWebHistory} from "vue-router";

// 创建路由规则
let routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login', // URL 地址
        name: 'login', // 名称
        component: () => import('../page/Login.vue')  // 渲染该组件
    },
    {
        path: '/route',
        name: 'route',
        component: () => import('../components/RouterTest.vue')
    },
    {
        path: '/main',
        name: 'main',
        component: () => import('../page/Main.vue'),
        redirect: '/main/c1', //重定向，默认为第一个子视图
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
    }
]

// 创建路由
const router = createRouter({
    // 使用 history 模式
    history: createWebHistory(),
    // 路由规则
    routes
})

// 将路由对象暴露出去
export default router