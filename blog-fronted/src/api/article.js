// src/utils/request.js

// 1. 导入axios
import request from '@/utils/http'

// 2. 创建axios实例
const getArticles = (params) => {
    return request({
        url: '/article',
        method: 'GET',
        params
    })
}

// 3. 请求拦截器
request.interceptors.request.use(
    (config) => {
        // 从本地存储获取token
        const token = localStorage.getItem('token')

        // 如果有token，就放在请求头里
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }

        return config
    },
    (error) => {
        // 如果请求出错了，返回一个被拒绝的Promise
        return Promise.reject(error)
    }
)



// 5. 导出request实例
export default getArticles