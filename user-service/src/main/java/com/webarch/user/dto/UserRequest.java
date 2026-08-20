package com.webarch.user.dto;

import com.webarch.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
		@NotBlank @Size(min = 3, max = 50) String username,
		@NotBlank @Email String email,
		@NotBlank @Size(min = 8) String password,
		@NotBlank @Size(min = 1, max = 100) String name,
		@Pattern(regexp = "^[+]?[0-9]{7,15}$", message = "Invalid phone number") String phone,
		Role role
) {
}
