
package com.example.gchc.service;

import com.example.gchc.dto.ArticleDTO;
import com.example.gchc.entity.Article;
import com.example.gchc.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Page<ArticleDTO> getAllArticles(Pageable pageable) {
        List<ArticleDTO> articles = articleRepository.findAllPage(pageable.getOffset(), pageable.getPageSize())
                .stream()
                .map(ArticleDTO::fromEntity)
                .toList();
        return new PageImpl<>(articles, pageable, articleRepository.countAll());
    }

    public Page<ArticleDTO> getPublishedArticles(Pageable pageable) {
        List<ArticleDTO> articles = articleRepository.findPublishedPage(pageable.getOffset(), pageable.getPageSize())
                .stream()
                .map(ArticleDTO::fromEntity)
                .toList();
        return new PageImpl<>(articles, pageable, articleRepository.countPublished());
    }

    public Page<ArticleDTO> getPublishedArticlesByCategory(Long categoryId, Pageable pageable) {
        List<ArticleDTO> articles = articleRepository.findPublishedByCategoryIdPage(categoryId, pageable.getOffset(), pageable.getPageSize())
                .stream()
                .map(ArticleDTO::fromEntity)
                .toList();
        return new PageImpl<>(articles, pageable, articleRepository.countPublishedByCategoryId(categoryId));
    }

    public List<ArticleDTO> getLatestArticles() {
        return articleRepository.findTop5ByPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(ArticleDTO::fromEntity)
                .toList();
    }

    public List<ArticleDTO> getHomeArticles() {
        return articleRepository.findHomeArticles()
                .stream()
                .map(ArticleDTO::fromEntity)
                .toList();
    }

    public Optional<ArticleDTO> getArticleById(Long id) {
        return articleRepository.findById(id).map(article -> {
            article.setViews(article.getViews() + 1);
            return ArticleDTO.fromEntity(articleRepository.save(article));
        });
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setSummary(articleDTO.getSummary());
        article.setAuthor(articleDTO.getAuthor());
        article.setCoverImage(articleDTO.getCoverImage());
        article.setCategoryId(articleDTO.getCategoryId());
        article.setViews(0);
        article.setPublished(articleDTO.getPublished() != null ? articleDTO.getPublished() : false);
        article.setShowOnHome(articleDTO.getShowOnHome() != null ? articleDTO.getShowOnHome() : false);
        return ArticleDTO.fromEntity(articleRepository.save(article));
    }

    public Optional<ArticleDTO> updateArticle(Long id, ArticleDTO articleDTO) {
        return articleRepository.findById(id).map(article -> {
            if (articleDTO.getTitle() != null) article.setTitle(articleDTO.getTitle());
            if (articleDTO.getContent() != null) article.setContent(articleDTO.getContent());
            if (articleDTO.getSummary() != null) article.setSummary(articleDTO.getSummary());
            if (articleDTO.getAuthor() != null) article.setAuthor(articleDTO.getAuthor());
            if (articleDTO.getCoverImage() != null) article.setCoverImage(articleDTO.getCoverImage());
            if (articleDTO.getCategoryId() != null) article.setCategoryId(articleDTO.getCategoryId());
            if (articleDTO.getPublished() != null) article.setPublished(articleDTO.getPublished());
            if (articleDTO.getShowOnHome() != null) article.setShowOnHome(articleDTO.getShowOnHome());
            return ArticleDTO.fromEntity(articleRepository.save(article));
        });
    }

    public boolean deleteArticle(Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
