import { request } from "@/utils/http";

const getUserCloud = async() => {
    try {
        await request({
            url: '/user/cloud',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
    }catch (error) {
        console.error('服务器异常:', error)
        throw error  // 重新抛出错误让调用方处理
    }
}

export default getUserCloud
