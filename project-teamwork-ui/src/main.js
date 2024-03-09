import {createApp} from 'vue'
import App from '@/App.vue'
import router from '@/router'
import 'element-plus/dist/index.css'
import {createPinia} from 'pinia'
import '@/assets/css/index.scss'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


const app = createApp(App)
// 导入所有图标并进行全局注册
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 全局挂载 axios
// app.config.globalProperties.$request = request
// app.config.globalProperties.$router = router

app.use(router) // 初始化路由插件
app.use(createPinia())
app.mount('#app')

