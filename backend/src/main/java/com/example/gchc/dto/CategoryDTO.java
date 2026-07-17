
package com.example.gchc.dto;

import com.example.gchc.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private Integer sortOrder;
    private Boolean enabled;
    private LocalDateTime createdAt;
    
    public static CategoryDTO fromEntity(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setParentId(category.getParentId());
        dto.setSortOrder(category.getSortOrder());
        dto.setEnabled(category.getEnabled());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }
}
