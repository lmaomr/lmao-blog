package cn.lmao.blog.model.entity;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 20, nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 10, nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "email", length = 20, nullable = false)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "bio", length = 50)
    private String bio;

    @JsonIgnore
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
    
}
