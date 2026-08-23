package com.webarch.cart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
		@NotBlank String recipientName,
		@NotNull @Valid ShippingAddress shippingAddress
) {
}
