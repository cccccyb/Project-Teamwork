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

// 检查是否存在于免登陆白名单
function inWhiteList(toPath) {
    const whiteList = ['/','/user/login', '/404']
    //find()回调函数会在每个元素上调用，直到它返回一个“真”值
    const path = whiteList.find((value) => {
        // 使用正则匹配
        const reg = new RegExp('^' + value)
        return reg.test(toPath)
    })
    //双感叹号=>强制改成布尔类型
    return !!path
}

//全局前置守卫
router.beforeEach((to, from, next) => {
    const token = sessionStorage.getItem("token")

    if (inWhiteList(to.path)) {
        next()
    } else {
        //用户已登录
        if (token) {
            //如果判断存在token的话，就请求后端的接口，这样就经过jwt拦截器的验证
            this.$request.post("/user/authentication").then(res=>{
                if(res.code===20000){
                    // 如果请求成功，前端就next()
                    next()
                }else{
                    // 验证失败就跳转到登录页
                    next("/login")
                }
            })
        } else {
            next(`/login`)
        }
    }
})

// 将路由对象暴露出去
export default router