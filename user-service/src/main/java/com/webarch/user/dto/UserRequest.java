package com.webarch.user.dto;

import com.webarch.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
		@NotBlank @Size(min = 3, max = 50) String username,
		@NotBlank @Email String email,
		@NotBlank @Size(min = 8) String password,
		Role role
) {
}
