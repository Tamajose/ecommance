package com.webarch.product.dto;

import java.math.BigDecimal;

import com.webarch.product.domain.ProductCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequest (
    @NotBlank(message = "Product Name Required")
    String name,

    String description,

    @NotNull(message = "Price Required")
    @Positive(message = "Price must be Greater then Zero")
    BigDecimal price,

    @NotNull(message = "Stock Quantity Required")
    @PositiveOrZero(message = "Stock Quantity cannot be Negative")
    Integer stockQuantity,

    @NotNull(message = "Category Required")
    ProductCategory category,

    String imageURL
){
}
