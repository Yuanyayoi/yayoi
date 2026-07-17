
package com.example.gchc.dto;

import com.example.gchc.entity.Article;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String author;
    private String coverImage;
    private Long categoryId;
    private Integer views;
    private Boolean published;
    private Boolean showOnHome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static ArticleDTO fromEntity(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setSummary(article.getSummary());
        dto.setAuthor(article.getAuthor());
        dto.setCoverImage(article.getCoverImage());
        dto.setCategoryId(article.getCategoryId());
        dto.setViews(article.getViews());
        dto.setPublished(article.getPublished());
        dto.setShowOnHome(article.getShowOnHome());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());
        return dto;
    }
}
