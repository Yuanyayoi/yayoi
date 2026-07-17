package com.example.gchc.dto;

import com.example.gchc.entity.NewsCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Integer sortOrder;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NewsCategoryDTO fromEntity(NewsCategory entity) {
        NewsCategoryDTO dto = new NewsCategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSortOrder(entity.getSortOrder());
        dto.setEnabled(entity.getEnabled());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public NewsCategory toEntity() {
        NewsCategory entity = new NewsCategory();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setSortOrder(this.sortOrder);
        entity.setEnabled(this.enabled);
        return entity;
    }
}
