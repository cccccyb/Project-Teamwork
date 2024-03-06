import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createPinia } from 'pinia'
import './assets/css/base.css'
import './assets/css/common.css'
// import request from "./util/request.js";



const app = createApp(App)
// 全局挂载 axios
// app.config.globalProperties.$request = request
// app.config.globalProperties.$router = router

app.use(ElementPlus)
app.use(router) // 初始化路由插件
app.use(createPinia())
app.mount('#app')

