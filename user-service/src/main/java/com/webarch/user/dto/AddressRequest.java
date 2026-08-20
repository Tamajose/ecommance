package com.webarch.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
		@NotBlank @Size(max = 255) String line1,
		@Size(max = 255) String line2,
		@NotBlank @Size(max = 100) String city,
		@NotBlank @Size(max = 100) String state,
		@NotBlank @Size(max = 20) String postalCode,
		@NotBlank @Size(max = 100) String country,
		boolean isDefault
) {
}