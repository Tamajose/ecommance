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
		createUserIfAbsent(new UserRequest(
				"admin",
				"admin@ecommance.com",
				"admin123",
				"Admin",
				"+10000000000",
				Role.ADMIN
		));
		createUserIfAbsent(new UserRequest(
				"seller",
				"seller@ecommance.com",
				"seller123",
				"Seller User",
				"+10000000001",
				Role.SELLER
		));
		createUserIfAbsent(new UserRequest(
				"buyer",
				"buyer@ecommance.com",
				"buyer123",
				"Buyer User",
				"+10000000002",
				Role.BUYER
		));
	}

	private void createUserIfAbsent(UserRequest request) {
		try {
			userService.createUser(request);
		} catch (IllegalArgumentException ignored) {
			// user already exists
		}
	}
}
