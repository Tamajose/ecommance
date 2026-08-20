package com.webarch.user.dto;

public record AuthResponse(
		String token,
		String username,
		String role
) {
}