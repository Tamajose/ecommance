package com.webarch.order.dto;

import com.webarch.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
		Long id,
		String username,
		OrderStatus status,
		BigDecimal totalAmount,
		String recipientName,
		String shippingLine,
		String shippingCity,
		String shippingPostalCode,
		String shippingCountry,
		String trackingNumber,
		String carrier,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		List<OrderItemResponse> items
) {
}
