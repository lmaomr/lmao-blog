package cn.lmao.blog.service;

// import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lmao.blog.dto.ArticleDTO;
import cn.lmao.blog.model.entity.Article;
import cn.lmao.blog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagService tagService;
    private final CommentService commentService;

    public Page<ArticleDTO> getAllArticles(Pageable pageable) {
        Page<Article> articles = articleRepository.findAllWithTags(pageable);
        return articles.map(article -> new ArticleDTO(
            article.getId(),
            article.getTitle(),
            article.getContent(),
            article.getAuthor(),
            article.getView(),
            article.getCreateTime(),
            article.getUpdateTime(),
            article.getTags() // 直接传递List<Tag>
        ));
}

    public Article getArticleById(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        article.setTags(tagService.getAllTagsByArticleId(article.getId()));
        article.setComments(commentService.getCommentById(article.getId()));
        return article;
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public boolean deleteArticle(Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Article updateArticle(Article article) {
        return articleRepository.save(article);
    }

}
