package cn.lmao.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 JWT 拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(    // 排除不需要验证的路径
                    "/api/article/**",   // 登录接口
                    "/api/user/login",   // 登录接口
                    "/api/user/register", // 注册接口
                    "/error",            // 错误页面
                    "/swagger-ui/**",    // Swagger UI
                    "/v3/api-docs/**"    // OpenAPI 文档
                ); 
    }
}