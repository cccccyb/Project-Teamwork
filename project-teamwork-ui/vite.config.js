import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import * as path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        AutoImport({
            resolvers: [ElementPlusResolver()],
            // 自动导入 Vue 相关函数，如：ref, reactive, toRef 等
            imports: ['vue', 'vue-router'],
        }),
        Components({
            resolvers: [ElementPlusResolver()],
        }),
    ],
    server: {
        port: '8721'
    },
    resolve: {
        // 配置路径别名
        alias: [
            // @代替src
            {
                find: '@',
                replacement: path.resolve('./src'),
            },
        ],
    },
})
