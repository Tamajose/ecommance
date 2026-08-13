package com.webarch.user.config;

import com.webarch.user.domain.Role;
import com.webarch.user.domain.User;
import com.webarch.user.dto.UserRequest;
import com.webarch.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

	private final UserService userService;

	public DataLoader(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(String... args) {
		try {
			userService.createUser(new UserRequest(
					"admin",
					"admin@ecommance.com",
					"admin123",
					Role.ADMIN
			));
		} catch (IllegalArgumentException ignored) {
			// admin already exists
		}
	}
}
