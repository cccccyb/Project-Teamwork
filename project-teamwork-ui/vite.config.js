import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        AutoImport({
            resolvers: [ElementPlusResolver()],
        }),
        Components({
            resolvers: [ElementPlusResolver()],
        }),],
    server: {
        port: '8721'
    },
    //代理解决跨域问题
    // proxy: {
    //     '/': {
    //         target: "http://localhost:8621",//实际后端访问的ip
    //         changeOrigin: true,
    //         pathRewrite: {
    //             '^/': "" //实际访问的ip
    //         }
    //     },
    // }
})
