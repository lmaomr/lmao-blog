package cn.lmao.blog.config;

import cn.lmao.blog.util.JwtUtil;
import org.springframework.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 放行 OPTIONS 请求，确保跨域预检请求可以正常通过
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        
        // 获取请求头中的 token
        String token = request.getHeader("Authorization");
        
        // 如果请求头中没有 token，返回未授权错误
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        // 验证 token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        return true;
    }
}