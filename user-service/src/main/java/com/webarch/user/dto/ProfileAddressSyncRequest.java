package com.webarch.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ProfileAddressSyncRequest(
		@NotBlank String username,
		@Valid AddressRequest address
) {
}
