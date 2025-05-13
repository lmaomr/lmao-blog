package cn.lmao.blog.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "view", nullable = false)
    private Integer view = 0;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @JsonManagedReference
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "article_tag",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @Column(name = "author", length = 10, nullable = false)
    private String author;

    @Column(columnDefinition = "datetime(0)", name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(columnDefinition = "datetime(0)", name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    // 新增或更新时自动设置时间
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        updateTime = createTime; // 创建时两者相同
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).truncatedTo(ChronoUnit.SECONDS); // 更新时仅修改updateTime
    }

}
