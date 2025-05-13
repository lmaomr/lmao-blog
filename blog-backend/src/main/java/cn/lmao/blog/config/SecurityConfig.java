// package cn.lmao.blog.config;

// import java.util.Arrays;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.web.cors.CorsConfiguration;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/api/user/login", "/login").permitAll() // 允许匿名访问
//                 .anyRequest().authenticated() // 其他请求需要认证
//             )
//             .formLogin(form -> form
//                 .loginPage("/login") // 自定义登录页（可选）
//                 .loginProcessingUrl("/api/user/login") // 登录处理接口
//                 .defaultSuccessUrl("/home") // 登录成功跳转页
//             )
//             .csrf(csrf -> csrf.disable()); // 禁用CSRF（API项目推荐）

//         return http.build();
//     }

//     protected void configureCors(HttpSecurity http) throws Exception {
//         CorsConfiguration corsConfiguration = new CorsConfiguration();
//         corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5174")); // 允许的源
//         corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 允许的请求方法
//         corsConfiguration.setAllowedHeaders(Arrays.asList("*")); // 允许的请求头
//         corsConfiguration.setAllowCredentials(true); // 允许携带凭证（如Cookie）

//         http.cors(cors -> cors.configurationSource(request -> corsConfiguration));
//     }
    
    
// }