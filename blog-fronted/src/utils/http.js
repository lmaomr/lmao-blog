import axios from "axios";
import { ElMessage } from 'element-plus'  // 导入Element Plus的消息提示组件
import { useUserStore } from '@/stores/user'  // 导入用户状态管理的store

const httpInstance = axios.create({
    // 设置基础URL，这样后面写请求的时候可以省略这部分
    baseURL: '/api',
    // 设置超时时间
    timeout: 5000
})


//拦截器
httpInstance.interceptors.request.use(config => {
    return config
}, e => Promise.reject(e))

//axios响应式拦截器
httpInstance.interceptors.response.use(res => res.data, e => {
    //统一错误提示
    ElMessage({
        type: 'warning',
        message: e.response.data.message
    })
    return Promise.reject(e)
})

// 4. 响应拦截器
// httpInstance.interceptors.response.use(
//     (response) => {
//         const userStore = useUserStore()  // 创建用户状态管理的store实例
//         userStore.login(response)  // 检查登录状态
//         // 从响应中获取数据
//         const res = response.data
//         // 如果状态码是200，说明请求成功
//         if (res?.code === 200 && res?.message === 'success') {
//             return res.data
//         }
//         ElMessage.error(res.message || '请求失败')
//         return Promise.reject(new Error(res.message || '请求失败'))
//     },
//     (error) => {
//         // 如果状态码是200，说明请求成功
//         switch (error.response?.status) {
//             case 400:
//                 // 错误请求的处理逻辑
//                 ElMessage.error('错误请求')
//                 break
//             case 401:
//                 // 未授权的处理逻辑
//                 ElMessage.error('未授权，请登录')
//                 // 清除token
//                 localStorage.removeItem('token')
//                 // 跳转到登录页
//                 setTimeout(() => {
//                     window.location.href = '/'
//                 }, 3000)
//                 break
//             case 403:
//                 // 禁止访问的处理逻辑
//                 ElMessage.error('禁止访问')
//                 break
//             case 404:
//                 // 找不到资源的处理逻辑
//                 ElMessage.error('请求的资源不存在')
//                 break
//             default:
//                 // 其他错误的处理逻辑
//                 ElMessage.error(res.message || '请求失败')
//         }
//         return Promise.reject(error)
//     }
// )

export default httpInstance