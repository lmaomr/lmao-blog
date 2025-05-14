import request from '@/utils/http'
import { useUserStore } from '@/stores/userStore'

const login = async ({ username, password }) => {
    try {
        const response = await request({
            url: '/user/login',
            method: 'POST',
            headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
            data: {
                username: username,
                password: password
            }
        })

        // 在函数内部初始化 store
        const userStore = useUserStore()
        userStore.login(response)  // 假设后端返回的数据在 response 中
        return response.data
    } catch (error) {
        console.error('登录失败:', error)
        throw error  // 重新抛出错误让调用方处理
    }
}

export default login