package com.example.gchc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private Long categoryId;
    private Integer stock = 0;
    private Boolean enabled = true;
    private Boolean showOnHome = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
