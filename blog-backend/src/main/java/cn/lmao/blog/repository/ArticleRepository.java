package cn.lmao.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.lmao.blog.model.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 1. Repository 层查询实体
    @EntityGraph(attributePaths = "tags")
    @Query("SELECT a FROM Article a")
    Page<Article> findAllWithTags(Pageable pageable);
}
