package cn.lmao.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.lmao.blog.model.entity.Tag;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query(value = "SELECT t.* FROM tag t JOIN article_tag at ON t.id = at.tag_id WHERE at.article_id = :articleId", nativeQuery = true)
    List<Tag> findByArticleId(@Param("articleId") Long articleId);
}
