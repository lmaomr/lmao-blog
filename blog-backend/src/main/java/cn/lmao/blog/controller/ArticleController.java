package cn.lmao.blog.controller;

import java.net.URI;
// import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cn.lmao.blog.dto.ArticleDTO;
import cn.lmao.blog.model.entity.Article;
import cn.lmao.blog.service.ArticleService;
import cn.lmao.blog.service.TagService;
import cn.lmao.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final TagService tagService;
    private final CommentService commentService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> findAllArticles(Pageable pageable) {
        //计算查询时间
        long startTime = System.currentTimeMillis();
        Map<String, Object> response = new HashMap<>();
        Page<ArticleDTO> articles = articleService.getAllArticles(pageable);
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", articles);

        long endTime = System.currentTimeMillis();
        System.out.println("查询时间: " + (endTime - startTime) + "ms");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getArticleById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        Article article = articleService.getArticleById(id);
        article.setTags(tagService.getAllTagsByArticleId(id));
        article.setComments(commentService.getCommentById(id));
        response.put("data", article);
        return ResponseEntity.ok(response);
    }

    

    @PostMapping("/admin/create")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(article.getId())
                .toUri();
        return articleService.createArticle(article) != null
                ? ResponseEntity.created(location).body(article)
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id) ? ResponseEntity.status(204).build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/admin/update/{id}")
    public Article updateArticle(@PathVariable Long id, @RequestBody Article updateArticle) {
        Article existingArticle = articleService.getArticleById(id);
        if (existingArticle == null) {
            System.out.println("Article not found with id: " + id);
            return null; // 或者抛出异常
        }
        updateArticle.setCreateTime(existingArticle.getCreateTime()); // 保持原有创建时间
        return articleService.updateArticle(updateArticle);
    }

}
