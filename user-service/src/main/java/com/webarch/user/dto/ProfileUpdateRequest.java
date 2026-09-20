package com.webarch.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfileUpdateRequest(
		@NotBlank String name,
		String phone,
		String addressLine,
		String addressCity,
		String addressPostalCode,
		String addressCountry
) {
}
