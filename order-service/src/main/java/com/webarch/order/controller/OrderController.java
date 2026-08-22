package com.webarch.order.controller;

import com.webarch.order.dto.OrderRequest;
import com.webarch.order.dto.OrderResponse;
import com.webarch.order.dto.OrderStatusUpdateRequest;
import com.webarch.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	private String usernameOf(Jwt jwt) {
		return jwt.getSubject();
	}

	private boolean isAdmin(Jwt jwt) {
		String scope = jwt.getClaimAsString("scope");
		return scope != null && List.of(scope.split(" ")).contains("ADMIN");
	}

	@PostMapping
	public ResponseEntity<OrderResponse> create(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody OrderRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(usernameOf(jwt), request));
	}

	@GetMapping("/me")
	public ResponseEntity<List<OrderResponse>> getMine(@AuthenticationPrincipal Jwt jwt) {
		return ResponseEntity.ok(orderService.getMine(usernameOf(jwt)));
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> getAll() {
		return ResponseEntity.ok(orderService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
		return ResponseEntity.ok(orderService.getById(id, usernameOf(jwt), isAdmin(jwt)));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
			@Valid @RequestBody OrderStatusUpdateRequest request) {
		return ResponseEntity.ok(orderService.updateStatus(id, request));
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<OrderResponse> cancel(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
		return ResponseEntity.ok(orderService.cancel(id, usernameOf(jwt), isAdmin(jwt)));
	}
}
