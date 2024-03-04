import axios from 'axios'
import {ElMessage} from 'element-plus'
import 'element-plus/dist/index.css'

const request = axios.create({
    baseURL: `http://localhost:8621`, // 给 axios 设置请求根路径
    timeout: 30000,
    withCredentials:true    //设置跨域携带cookie
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let token = sessionStorage.getItem("token") ? sessionStorage.getItem("token") : null
    if (token) {
        config.headers['token'] = token;  // 设置请求头
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 当认证验证不通过的时候给出提示
        if (res.code === 20017) {
            ElMessage({
                message: res.msg,
                type: 'error'
            });
            this.$router.replace("/login")
        }
        return res;
    },
    error => {
        console.log('err:' + error) // for debug
        return Promise.reject(error)
    }
)


export default request