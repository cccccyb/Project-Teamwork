import {createRouter, createWebHistory} from "vue-router";
import request from "@/util/request.js";
import {SYSTEM_OK} from "@/constants/Common.constants.js";
import {ElMessage} from "element-plus";

// 创建路由规则
let routes = [
    {
        path: '/',
        redirect: '/login',
        meta: {
            title: '',
            hasChildren: false,
            isShow: true,
            isMenu: false
        }
    },
    {
        path: '/login', // URL 地址
        name: 'login', // 名称
        component: () => import('@/page/Login.vue'),  // 渲染该组件
        meta: {
            title: '登录',
            hasChildren: false,
            isShow: true,
            isMenu: false
        }
    },
    {
        path: '/main',
        name: 'main',
        component: () => import('@/layout/index.vue'),
        redirect: '/main/workspace', //默认加载第一个children
        meta: {
            title: '首页',
            hasChildren: false,
            requireChildMenu: false,
            isShow: true,
            isMenu: false
        },
        children: [
            {
                path: 'workspace',
                name: 'workspace',
                component: () => import('@/page/workspace/WorkSpace.vue'),
                meta: {
                    title: '个人工作台',
                    icon: 'workspace',
                    hasChildren: false,
                    isShow: true,
                    isMenu: true
                }
            },
            {
                path: 'projectManage',
                name: 'projectManage',
                component:()=>import('@/page/project/ProjectParent.vue'),
                redirect:'/main/projectManage/projectPerson',
                meta: {
                    title: '项目管理',
                    icon: 'project_parent_menu',
                    requireChildMenu: true,
                    hasChildren: true,
                    isShow: true,
                    isMenu: true
                },
                children: [
                    {
                        path: 'projectPerson',
                        name: 'projectPerson',
                        component: () => import('@/page/project/ProjectPerson.vue'),
                        meta: {
                            title: '我参与的',
                            icon: 'project_person',
                            hasChildren: false,
                            isShow: true,
                            isMenu: true
                        }
                    },
                    {
                        path: 'projectMyManage',
                        name: 'projectMyManage',
                        component: () => import('@/page/project/ProjectMyManage.vue'),
                        meta: {
                            title: '我管理的',
                            icon: 'project_myManage',
                            hasChildren: false,
                            isShow: true,
                            isMenu: true
                        }
                    },
                    {
                        path: 'projectAll',
                        name: 'projectAll',
                        component: () => import('@/page/project/ProjectAll.vue'),
                        meta: {
                            title: '所有项目',
                            icon: 'project_all',
                            hasChildren: false,
                            isShow: true,
                            isMenu: true
                        }
                    }
                ]
            },
            // {
            //     path: 'requireManage',
            //     name: 'requireManage',
            //     component: () => import('@/page/require/RequireManage.vue'),
            //     meta: {
            //         title: '需求管理',
            //         icon: 'require_menu',
            //         hasChildren: false,
            //         isShow: true,
            //         isMenu: true
            //     }
            // },
            // {
            //     path: 'iterationManage',
            //     name: 'iterationManage',
            //     component: () => import('@/page/iteration/IterationManage.vue'),
            //     meta: {
            //         title: '迭代管理',
            //         icon: 'iteration_menu',
            //         hasChildren: true,
            //         requireChildMenu: false,
            //         isShow: true,
            //         isMenu: true
            //     },
            //     children: [
            //         {
            //             path: 'iterationTask',
            //             name: 'iterationTask',
            //             component: () => import('@/page/iteration/IterationTask.vue'),
            //             meta: {
            //                 title: '迭代所属任务',
            //                 hasChildren: false,
            //                 isShow: true,
            //                 isMenu: false
            //             }
            //         },
            //         {
            //             path: 'iterationRequire',
            //             name: 'iterationRequire',
            //             component: () => import('@/page/iteration/IterationRequire.vue'),
            //             meta: {
            //                 title: '迭代所属需求',
            //                 hasChildren: false,
            //                 isShow: true,
            //                 isMenu: false
            //             }
            //         },
            //         {
            //             path: 'iterationBug',
            //             name: 'iterationBug',
            //             component: () => import('@/page/iteration/IterationBug.vue'),
            //             meta: {
            //                 title: '迭代所属缺陷',
            //                 hasChildren: false,
            //                 isShow: true,
            //                 isMenu: false
            //             }
            //         }
            //     ]
            // },
            // {
            //     path: 'bugManage',
            //     name: 'bugManage',
            //     component: () => import('@/page/bug/BugManage.vue'),
            //     meta: {
            //         title: '缺陷管理',
            //         icon: 'bug_menu',
            //         hasChildren: false,
            //         isShow: true,
            //         isMenu: true
            //     }
            // },
            // {
            //     path: 'taskManage',
            //     name: 'taskManage',
            //     component: () => import('@/page/task/TaskManage.vue'),
            //     meta: {
            //         title: '任务管理',
            //         icon: 'task_menu',
            //         hasChildren: false,
            //         isShow: true,
            //         isMenu: true
            //     }
            // },
            {
                path: 'dashboard',
                name: 'dashboard',
                component: () => import('@/page/dashboard/Dashboard.vue'),
                meta: {
                    title: '仪表盘',
                    icon: 'dashboard_menu',
                    hasChildren: false,
                    isShow: true,
                    isMenu: true
                }
            },
            {
                path: 'userManage',
                name: 'userManage',
                component: () => import('@/page/user/UserManage.vue'),
                meta: {
                    title: '用户管理',
                    icon: 'user_manage',
                    hasChildren: false,
                    isShow: true,
                    isMenu: true
                }
            },
            {
                path: 'groupManage',
                name: 'groupManage',
                component: () => import('@/page/group/GroupManage.vue'),
                meta: {
                    title: '用户组管理',
                    icon: 'group_menu',
                    hasChildren: false,
                    isShow: true,
                    isMenu: true
                }
            },
            {
                path: 'roleManage',
                name: 'roleManage',
                component: () => import('@/page/role/RoleManage.vue'),
                meta: {
                    title: '角色管理',
                    icon: 'role_menu',
                    hasChildren: false,
                    isShow: true,
                    isMenu: true
                }
            }
        ]
    },
    {
        path: '/projectDetail',
        name: 'projectDetail',
        component: () => import('@/page/project/details/det-index.vue'),
        redirect: '/projectDetail/projectTeamwork',
        meta: {
            title: '项目详情',
            requireChildMenu: false,
            hasChildren: true,
            isShow: true,
            isMenu: false
        },
        children: [
            {
                path: 'projectTeamwork',
                name: 'projectTeamwork',
                component: () => import('@/page/project/details/ProjectTeamwork.vue'),
                meta: {
                    title: '项目协同',
                    icon: 'projectTeamwork',
                    hasChildren: false,
                    isShow: true,
                    isMenu: true
                }
            },
            {
                path: 'projectSetting',
                name: 'projectSetting',
                component: () => import('@/page/project/details/ProjectSetting.vue'),
                meta: {
                    title: '项目设置',
                    icon: 'projectSetting',
                    hasChildren: false,
                    isShow: true,
                    isMenu: true
                }
            },
            {
                path: 'projectIteration',
                name: 'projectIteration',
                component: () => import('@/page/iteration/Proj-IterateDetails.vue'),
                meta: {
                    title: '项目具体迭代详情',
                    hasChildren: false,
                    isShow: true,
                    isMenu: false
                }
            },
        ]
    },
    {
        path: '/404',
        name: '404',
        component: () => import('@/page/404.vue'),
    },
    {
        path: "/:catchAll(.*)",
        redirect: '/404',
    }
]

// 创建路由
const router = createRouter({
    // 使用 history 模式
    history: createWebHistory(),
    // 路由规则
    routes
})

const inWhiteList = ['/', '/login', '/404'];
//全局前置守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem("token")
    //白名单 放行
    if (inWhiteList.includes(to.path)) {
        next()
    } else {
        //用户已登录
        if (token != null) {
            //如果判断存在token的话，就请求后端的接口，这样就经过jwt拦截器的验证
            request.post("/user/authentication").then(res => {
                if (res.data.code === SYSTEM_OK) {
                    // 如果请求成功，前端就next()
                    next()
                }
            })
        } else {
            ElMessage({
                dangerouslyUseHTMLString: true,
                message: `<strong>您尚未登录，请先进行登录！</strong>`,
                type: 'warning'
            });
            next(`/login`)
        }
    }
})

// 将路由对象暴露出去
export default router