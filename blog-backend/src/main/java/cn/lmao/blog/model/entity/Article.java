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

/**
 * 文章实体类
 * 对应数据库中的 article 表
 */
@Entity // 标记为JPA实体类，表示该类对应数据库中的一张表
@Data // Lombok注解，自动生成getter/setter/toString等方法
@Table(name = "article") // 指定对应的数据库表名
public class Article {
    
    /**
     * 主键ID
     * 自增策略生成
     */
    @Id // 标识为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略为自增
    @Column(name = "id") // 映射到表中的id列
    private Long id;

    /**
     * 文章标题
     * 最大长度100，非空
     */
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    /**
     * 浏览量
     * 默认值为0，非空
     */
    @Column(name = "view", nullable = false)
    private Integer view = 0;

    /**
     * 文章内容
     * 使用text类型存储，非空
     */
    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    /**
     * 文章评论列表
     * 一对多关系，由Comment实体中的article属性维护关系
     * 延迟加载，使用JsonManagedReference避免JSON序列化时的循环引用问题
     */
    @JsonManagedReference // 处理JSON序列化时的循环引用，标记为"主"方
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY) // 一对多，由对方维护关系，延迟加载
    private List<Comment> comments = new ArrayList<>();
    
    /**
     * 文章标签列表
     * 多对多关系，通过中间表article_tag关联
     * 中间表包含article_id和tag_id两个外键
     */
    @ManyToMany // 多对多关系
    @JoinTable(
        name = "article_tag", // 中间表名
        joinColumns = @JoinColumn(name = "article_id"), // 当前实体在中间表中的外键
        inverseJoinColumns = @JoinColumn(name = "tag_id") // 对方实体在中间表中的外键
    )
    private List<Tag> tags = new ArrayList<>();

    /**
     * 作者
     * 最大长度10，非空
     */
    @Column(name = "author", length = 10, nullable = false)
    private String author;

    /**
     * 创建时间
     * 使用datetime(0)类型，精确到秒
     * 非空，不可更新
     */
    @Column(columnDefinition = "datetime(0)", name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 使用datetime(0)类型，精确到秒
     * 非空
     */
    @Column(columnDefinition = "datetime(0)", name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /**
     * 持久化前的回调方法
     * 在实体被插入数据库前自动调用
     * 设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        // 使用亚洲/上海时区
        createTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        // 创建时更新时间与创建时间相同
        updateTime = createTime; 
    }

    /**
     * 更新前的回调方法
     * 在实体被更新前自动调用
     * 只更新updateTime字段
     */
    @PreUpdate
    protected void onUpdate() {
        // 更新时只修改updateTime，精确到秒
        updateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).truncatedTo(ChronoUnit.SECONDS); 
    }
}