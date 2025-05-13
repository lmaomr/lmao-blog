package cn.lmao.blog.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@Entity
@Data
@Table(name = "tag")
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name", length = 10, nullable = false)
    private String name;
    
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
    
    /**
     * 创建前自动设置创建时间
     */
    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
}
