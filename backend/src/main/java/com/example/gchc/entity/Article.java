package com.example.gchc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String author;
    private String coverImage;
    private Long categoryId;
    private Integer views = 0;
    private Boolean published = false;
    private Boolean showOnHome = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
