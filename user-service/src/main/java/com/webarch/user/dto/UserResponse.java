package com.webarch.user.dto;

import com.webarch.user.domain.Role;

import java.time.Instant;

public record UserResponse(
		Long id,
		String username,
		String email,
		Role role,
		boolean enabled,
		Instant createdAt
) {
}
