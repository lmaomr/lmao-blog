package cn.lmao.blog.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cn.lmao.blog.model.entity.Tag;
import lombok.Data;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Integer view;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Tag> tags = new ArrayList<>();

    public ArticleDTO(Long id, String title, String content, String author, Integer view, LocalDateTime createTime,
            LocalDateTime updateTime, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.view = view;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.tags = tags;
    }

    public ArticleDTO() {
    }

}
