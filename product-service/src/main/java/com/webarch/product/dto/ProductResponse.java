package com.webarch.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.webarch.product.domain.ProductCategory;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stockQuantity,
    ProductCategory category,
    String imageURL,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
