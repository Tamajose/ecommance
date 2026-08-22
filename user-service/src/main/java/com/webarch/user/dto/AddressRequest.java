package com.webarch.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
		@NotBlank @Size(max = 255) String line,
		@NotBlank @Size(max = 100) String city,
		@NotBlank @Size(max = 20) String postalCode,
		@NotBlank @Size(max = 100) String country
) {
}
