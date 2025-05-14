/**
 * 仅本地验证 Token 是否过期（不主动请求后端）
 * @returns { valid: boolean, reason?: string }
 */
export function isTokenValid(token) {
    if (!token) return { valid: false, reason: "没有提供Token！" };
    try {
        const isExpired = Date.now() >= JSON.parse(atob(token.split('.')[1])).exp * 1000;
        return { 
            valid: !isExpired,
            reason: isExpired ? "Token已过期！" : "Token有效！(本地检查)"
        };
    } catch (e) {
        return { valid: false, reason: "Invalid token format" };
    }
}