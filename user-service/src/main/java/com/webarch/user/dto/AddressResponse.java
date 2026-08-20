package com.webarch.user.dto;

public record AddressResponse(
		Long id,
		Long userId,
		String line1,
		String line2,
		String city,
		String state,
		String postalCode,
		String country,
		boolean isDefault
) {
}