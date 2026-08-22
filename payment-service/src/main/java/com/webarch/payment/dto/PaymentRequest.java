package com.webarch.payment.dto;

import java.math.BigDecimal;

import com.webarch.payment.domain.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
    @NotNull
    Long userId,

    @NotNull
    Long cartId,

    @NotNull
    @DecimalMin(value = "0.01")
    BigDecimal amount,

    @NotNull
    PaymentMethod paymentMethod
) {
}