package cn.lmao.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.lmao.blog.model.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {   

    List<Comment> findByArticleId(Long articleId);
}
