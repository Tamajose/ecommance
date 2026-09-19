package com.webarch.payment.dto;

import java.math.BigDecimal;

import com.webarch.payment.domain.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
    @NotBlank
    String username,

    @NotNull
    Long orderId,

    @NotNull
    @DecimalMin(value = "0.01")
    BigDecimal amount,

    @NotNull
    PaymentMethod paymentMethod
) {
}
