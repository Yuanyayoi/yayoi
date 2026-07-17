
package com.example.gchc.dto;

import com.example.gchc.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    
    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private Long categoryId;
    private Integer stock;
    private Boolean enabled;
    private Boolean showOnHome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImage());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategoryId());
        dto.setStock(product.getStock());
        dto.setEnabled(product.getEnabled());
        dto.setShowOnHome(product.getShowOnHome());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
