package com.webarch.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.webarch.payment.domain.PaymentMethod;
import com.webarch.payment.domain.PaymentStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record PaymentResponse (
    Long paymentId,
    Long userId,
    Long cartId,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
