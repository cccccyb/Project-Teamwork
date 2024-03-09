import axios from 'axios'
import {ElMessage} from 'element-plus'
import 'element-plus/dist/index.css'
import {
    ACCESS_DENIED,
    DATABASE_CONNECT_ERROR,
    DATABASE_DATA_TO_LONG,
    DATABASE_DATA_VALIDATION_FAILED,
    LOGIN_USERNAME_PASSWORD_ERROR,
    SYSTEM_ERROR,
    TOKEN_Format_ERROR,
    TOKEN_HAS_EXPIRED,
    TOKEN_IS_BLANK,
    TOKEN_IS_ILLEGAL,
    TOKEN_Signature_ERROR
} from "../constants/Common.constants.js";
import router from "../router/index.js";

const request = axios.create({
    baseURL: `http://localhost:8621`, // 给 axios 设置请求根路径
    timeout: 30000,
    withCredentials: true    //设置跨域携带cookie
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(async config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let token =localStorage.getItem("token") ? localStorage.getItem("token"): null
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
            return response
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        switch (res.code) {
            //token过期
            case TOKEN_HAS_EXPIRED:
                localStorage.clear();
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: `<strong>登录过期，请重新登录</strong>`,
                    type: 'warning'
                });
                 router.replace("/login").then(r => {});
                return  Promise.reject(response?.data)
            // token格式有误
            case TOKEN_Format_ERROR:
                localStorage.clear();
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: "<strong>认证格式有误，请重新登录</strong>",
                    type: 'warning'
                });
                router.replace("/login").then(r => {});
                return  Promise.reject(response?.data)
            // token签名异常
            case TOKEN_Signature_ERROR:
                localStorage.clear();
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: "<strong>token签名异常，请重新登录</strong>",
                    type: 'warning'
                });
                router.replace("/login").then(r => {});
                return  Promise.reject(response?.data)
            //token为空
            case TOKEN_IS_BLANK:
                localStorage.clear();
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: "<strong>您尚未登录，请先进行登录</strong>",
                    type: 'warning'
                });
                router.replace("/login").then(r => {});
                return  Promise.reject(response?.data)
            //解析失败，token无效
            case TOKEN_IS_ILLEGAL:
                localStorage.clear();
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: "<strong>解析失败，token无效</strong>",
                    type: 'warning'
                });
                router.replace("/login").then(r => {});
                return  Promise.reject(response?.data)
            //用户不存在，请重新登录
            case LOGIN_USERNAME_PASSWORD_ERROR:
                localStorage.clear();
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: '<strong>用户不存在，请重新登录</strong>',
                    type: 'warning'
                });
                router.replace("/login").then(r => {});
                return  Promise.reject(response?.data)
            case ACCESS_DENIED:
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: "<strong>暂无权限操作</strong>",
                    type: 'error'
                });
                return  Promise.reject(response?.data)
            case DATABASE_DATA_TO_LONG:
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: '<strong>数据过长</strong>',
                    type:'error'
                })
                return  Promise.reject(response?.data)
            case DATABASE_DATA_VALIDATION_FAILED:
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: '<strong>数据验证失败</strong>',
                    type:'error'
                })
                return  Promise.reject(response?.data)
            case DATABASE_CONNECT_ERROR:
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: '<strong>数据库出错了，请稍后再试</strong>',
                    type:'error'
                })
                return  Promise.reject(response?.data)
            case SYSTEM_ERROR:
                ElMessage({
                    dangerouslyUseHTMLString: true,
                    message: '<strong>认证异常，请重新登录</strong>',
                    type:'error'
                })
                return  Promise.reject(response?.data)
        }

        return response;
    },
    async error => {
        ElMessage({
            dangerouslyUseHTMLString: true,
            message: '<strong>服务器出错</strong>，请稍后重试',
            type:'error'
        })
        return await Promise.reject(error?.response?.data)
    }
)


export default request