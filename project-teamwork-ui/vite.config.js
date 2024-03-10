import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import * as path from 'path'
import {createSvgIconsPlugin} from 'vite-plugin-svg-icons';

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
        createSvgIconsPlugin({
            // 需要自动导入的 svg 文件目录
            iconDirs: [path.resolve(process.cwd(), "src/assets/svg")],
            // 执行icon name的格式（可自行修改）
            symbolId: "icon-[dir]-[name]",
        })
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
