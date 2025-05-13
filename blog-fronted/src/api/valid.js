import request from '@/utils/http';

/**
 * 本地校验 Token 是否过期
 * @returns { valid: boolean, reason?: string }
 */
const validateToken = (token) => {
    if (!token) return { valid: false, reason: "No token provided" };
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const isExpired = Date.now() >= payload.exp * 1000;
        return { 
            valid: !isExpired,
            reason: isExpired ? "Token expired" : "Valid (local check)"
        };
    } catch (e) {
        return { valid: false, reason: "Invalid token format" };
    }
}

/**
 * 综合验证 Token（前端 + 后端）
 * @returns { valid: boolean, reason?: string }
 */
export async function isTokenValid(token) {
    // 1. 检查本地是否有效
    const localCheck = validateToken(token);
    if (!localCheck.valid) return localCheck;
    console.log(token)
    // 2. 请求后端严格验证
    try {
        const response = await request('/auth/validate', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return response.data; // 假设后端返回 { valid, reason }
    } catch (error) {
        return { 
            valid: false, 
            reason: error.response?.data?.reason || "Network error" 
        };
    }
}