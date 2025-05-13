package cn.lmao.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.lmao.blog.model.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    // 这里可以添加自定义查询方法
    // 例如：List<Admin> findByUsername(String username);
    
    // 你可以在这里添加其他与管理员相关的查询方法

    
}