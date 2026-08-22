package com.webarch.order.dto;

import com.webarch.order.domain.OrderStatus;

public record OrderStatusUpdateRequest(
		OrderStatus status,
		String trackingNumber,
		String carrier
) {
}
