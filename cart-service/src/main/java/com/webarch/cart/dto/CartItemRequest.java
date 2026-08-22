package com.webarch.cart.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(
    @NotNull
    Long productId,

    String productName,

    @NotNull
    @DecimalMin(value = "0.01")
    BigDecimal unitPrice,

    @NotNull
    @Min(1)
    Integer quantity
) {
}
