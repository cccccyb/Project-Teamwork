import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createPinia } from 'pinia'
import './assets/css/base.css'
import './assets/css/common.css'
import axios from "axios";


let app = createApp(App)
app.use(ElementPlus)
app.use(router) // 初始化路由插件
app.use(createPinia())
app.mount('#app')

