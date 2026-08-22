package com.webarch.product.dto;

import jakarta.validation.constraints.NotNull;

public record StockRequest(@NotNull Integer delta) {
}
