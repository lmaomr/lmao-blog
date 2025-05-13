package cn.lmao.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.lmao.blog.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username); // Find user by username

    User findByEmail(String email); // Find user by email

    User findByUsernameAndPassword(String username, String password); // Find user by username and password

    User findByEmailAndPassword(String email, String password); // Find user by email and password
}
