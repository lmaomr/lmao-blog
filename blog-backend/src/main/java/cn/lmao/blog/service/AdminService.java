package cn.lmao.blog.service;


import org.springframework.beans.factory.annotation.Autowired;

import cn.lmao.blog.repository.AdminRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminService {
    
    @Autowired
    private final AdminRepository adminRepository;

    public boolean yzsf(String username, String password) {
        // 这里可以添加自定义查询方法
        // 例如：List<Admin> findByUsername(String username);
        // 你可以在这里添加其他与管理员相关的查询方法
        return adminRepository.findAll().stream()
                .anyMatch(admin -> admin.getUsername().equals(username) && admin.getPassword().equals(password));
    }

}
