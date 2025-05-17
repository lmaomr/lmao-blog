package cn.lmao.blog.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String name; // 文件名称

    @ColumnDefault("0") // 使用Hibernate的默认值注解
    @Column(name= "file_size", nullable = false)
    private long size; // 文件大小，单位字节

    @Column(name= "file_type")
    private String type; // 文件类型（扩展名）
    
    @Column(name= "file_url", nullable = false)
    private String url; // 文件访问地址

    @Column(name= "file_hash", nullable = false, length = 64)
    private String hash; // 文件哈希值(SHA-256)

    @Column(columnDefinition = "datetime(0)", name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(columnDefinition = "datetime(0)", name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY) // 添加延迟加载
    @JoinColumn(name = "cloud_id", nullable = false)
    @JsonBackReference // 防止序列化递归
    private Cloud cloud;

    // 新增文件状态标识
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private FileStatus status = FileStatus.ACTIVE;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        createTime = now;
        updateTime = now;
        
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"))
                .truncatedTo(ChronoUnit.SECONDS);
    }

    // 文件状态枚举
    public enum FileStatus {
        ACTIVE,    // 活跃可用
        DELETED,   // 已删除
        ARCHIVED   // 已归档
    }

    public File() {
        // 默认构造函数
    }

    public File(File file) {
        this.name = file.getName();
        this.size = file.getSize();
        this.type = file.getType();
        this.name = file.getName();
        this.hash = file.getHash();
        this.url = file.getUrl();
        this.createTime = file.getCreateTime();
        this.updateTime = file.getUpdateTime();
        this.status = file.getStatus();
    }
}