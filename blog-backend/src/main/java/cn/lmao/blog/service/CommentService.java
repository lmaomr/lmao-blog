package cn.lmao.blog.service;

import org.springframework.stereotype.Service;

import cn.lmao.blog.model.entity.Comment;
import cn.lmao.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentById(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }
    
}
