package com.webarch.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ShippingAddress(
		@NotBlank @Size(max = 255) String line,
		@NotBlank @Size(max = 100) String city,
		@NotBlank @Size(max = 20) String postalCode,
		@NotBlank @Size(max = 100) String country
) {
}
