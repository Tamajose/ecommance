package com.webarch.order.controller;

import com.webarch.order.dto.OrderRequest;
import com.webarch.order.dto.OrderResponse;
import com.webarch.order.dto.OrderStatusUpdateRequest;
import com.webarch.order.dto.PaymentStatusSyncRequest;
import com.webarch.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${internal.api-key}")
	private String internalApiKey;

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

	@GetMapping("/seller/me")
	public ResponseEntity<List<OrderResponse>> getForSeller(@AuthenticationPrincipal Jwt jwt) {
		return ResponseEntity.ok(orderService.getForSeller(usernameOf(jwt)));
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

	@PostMapping("/{id}/sync-payment-status")
	public ResponseEntity<Void> syncPaymentStatus(@PathVariable Long id,
			@RequestHeader("X-Internal-Api-Key") String apiKey,
			@RequestBody PaymentStatusSyncRequest request) {
		if (!internalApiKey.equals(apiKey)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		orderService.syncFromPaymentStatus(id, request.paymentStatus());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<OrderResponse> cancel(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
		return ResponseEntity.ok(orderService.cancel(id, usernameOf(jwt), isAdmin(jwt)));
	}
}
