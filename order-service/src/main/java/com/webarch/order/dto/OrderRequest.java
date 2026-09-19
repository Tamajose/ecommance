package com.webarch.order.dto;

import com.webarch.order.domain.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
		@NotNull @Valid List<@Valid OrderItemRequest> items,
		@NotBlank String recipientName,
		@NotNull @Valid ShippingAddress shippingAddress,
		@NotNull PaymentMethod paymentMethod
) {
}
