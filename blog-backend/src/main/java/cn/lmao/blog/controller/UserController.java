package cn.lmao.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import cn.lmao.blog.service.UserService;
import cn.lmao.blog.model.entity.User;  
import cn.lmao.blog.util.JwtUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    
    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginMap) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.findByUsernameAndPassword(loginMap.get("username"), loginMap.get("password"));
        System.out.println(loginMap);
        System.out.println(loginMap.get("username")+ loginMap.get("password"));
        if (user == null) {
            response.put("code", 400);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.badRequest().body(response);
        };
        response.put("code", 200);
        response.put("message", "登录成功");
        response.put("data", user);
        response.put("token", jwtUtil.generateToken(user.getUsername()));
        System.out.println(response);
        return ResponseEntity.ok(response);
    }
}
