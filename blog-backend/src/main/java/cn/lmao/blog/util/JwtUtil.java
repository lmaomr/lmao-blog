package cn.lmao.blog.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.lmao.blog.service.UserService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long expiration;
    private static final String TOKEN_PREFIX = "Bearer ";
    // 添加UserService依赖
    private final UserService userService;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long expiration,
            UserService userService) {  // 注入UserService
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expiration = expiration;
        this.userService = userService;  // 注入UserService
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        System.out.println("当前时间: " + new Date());
        System.out.println("token过期时间: " + new Date(System.currentTimeMillis() + expiration));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public long getUserIdFromToken(String authHeader) {
        String token = extractToken(authHeader);
        String username = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
        if (StringUtils.isBlank(username)) {
            throw new JwtException("Token中没有用户名");
        }
        System.out.println("提取的用户名: " + username);
        Long userId = userService.getUserIdByUsername(username);
        return userId;
    }

    public String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            throw new JwtException("无效的Authorization头");
        }
        return authHeader.substring(TOKEN_PREFIX.length());
    }

    /**
     * 验证JWT Token的有效性
     * 
     * @param token 需要验证的JWT Token
     * @return 如果Token有效返回true，否则返回false
     */
    public boolean validateToken(String authHeader) {
        try {
            String token = extractToken(authHeader);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println("Token 验证成功");
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token 已过期"); // 明确过期
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token 无效: " + e.getMessage()); // 其他错误
            return false;
        }
    }

}