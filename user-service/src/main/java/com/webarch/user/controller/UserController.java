package com.webarch.user.controller;

import com.webarch.user.dto.AddressRequest;
import com.webarch.user.dto.ProfileAddressSyncRequest;
import com.webarch.user.dto.ProfileUpdateRequest;
import com.webarch.user.dto.UserRequest;
import com.webarch.user.dto.UserResponse;
import com.webarch.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@Value("${internal.api-key}")
	private String internalApiKey;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
		return ResponseEntity.ok(userService.getByUsername(usernameOf(jwt)));
	}

	@PatchMapping("/me")
	public ResponseEntity<UserResponse> updateMyProfile(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody ProfileUpdateRequest request) {
		return ResponseEntity.ok(userService.updateProfile(usernameOf(jwt), request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PatchMapping("/{id}/address")
	public ResponseEntity<UserResponse> updateAddress(@PathVariable Long id,
			@Valid @RequestBody AddressRequest request) {
		return ResponseEntity.ok(userService.updateAddress(id, request));
	}

	@PatchMapping("/profile-address")
	public ResponseEntity<Void> syncProfileAddress(@RequestHeader("X-Internal-Api-Key") String apiKey,
			@Valid @RequestBody ProfileAddressSyncRequest request) {
		if (!internalApiKey.equals(apiKey)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		userService.updateAddressByUsername(request.username(), request.address());
		return ResponseEntity.noContent().build();
	}

	private String usernameOf(Jwt jwt) {
		return jwt.getSubject();
	}
}
