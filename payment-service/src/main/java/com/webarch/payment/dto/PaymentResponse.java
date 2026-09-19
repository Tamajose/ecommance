package com.webarch.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.webarch.payment.domain.PaymentMethod;
import com.webarch.payment.domain.PaymentStatus;

public record PaymentResponse (
    Long paymentId,
    String username,
    Long orderId,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
