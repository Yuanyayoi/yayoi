
package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import com.example.gchc.dto.ArticleDTO;
import com.example.gchc.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Page<ArticleDTO>>> getPublishedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleDTO> articles = categoryId != null
                ? articleService.getPublishedArticlesByCategory(categoryId, pageable)
                : articleService.getPublishedArticles(pageable);
        return ResponseEntity.ok(ApiResponse.success(articles));
    }

    @GetMapping("/public/latest")
    public ResponseEntity<ApiResponse<List<ArticleDTO>>> getLatestArticles() {
        List<ArticleDTO> articles = articleService.getLatestArticles();
        return ResponseEntity.ok(ApiResponse.success(articles));
    }

    @GetMapping("/public/home")
    public ResponseEntity<ApiResponse<List<ArticleDTO>>> getHomeArticles() {
        List<ArticleDTO> articles = articleService.getHomeArticles();
        return ResponseEntity.ok(ApiResponse.success(articles));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<ArticleDTO>> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(article -> ResponseEntity.ok(ApiResponse.success(article)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ArticleDTO>>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleDTO> articles = articleService.getAllArticles(pageable);
        return ResponseEntity.ok(ApiResponse.success(articles));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleDTO>> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticleDTO createdArticle = articleService.createArticle(articleDTO);
        return ResponseEntity.ok(ApiResponse.success("创建成功", createdArticle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ArticleDTO>> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        return articleService.updateArticle(id, articleDTO)
                .map(article -> ResponseEntity.ok(ApiResponse.success("更新成功", article)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long id) {
        if (articleService.deleteArticle(id)) {
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        }
        return ResponseEntity.notFound().build();
    }
}
