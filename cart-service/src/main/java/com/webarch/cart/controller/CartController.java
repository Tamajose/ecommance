package com.webarch.cart.controller;

import com.webarch.cart.dto.CartItemRequest;
import com.webarch.cart.dto.CartResponse;
import com.webarch.cart.dto.CheckoutRequest;
import com.webarch.cart.dto.UpdateQuantityRequest;
import com.webarch.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	private String usernameOf(Jwt jwt) {
		return jwt.getSubject();
	}

	@GetMapping
	public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal Jwt jwt) {
		return ResponseEntity.ok(cartService.getCart(usernameOf(jwt)));
	}

	@PostMapping("/items")
	public ResponseEntity<CartResponse> addItem(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody CartItemRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(usernameOf(jwt), request));
	}

	@PatchMapping("/items/{productId}")
	public ResponseEntity<CartResponse> updateItemQuantity(@AuthenticationPrincipal Jwt jwt,
			@PathVariable Long productId, @Valid @RequestBody UpdateQuantityRequest request) {
		return ResponseEntity.ok(cartService.updateItemQuantity(usernameOf(jwt), productId, request.quantity()));
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<CartResponse> removeItem(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId) {
		return ResponseEntity.ok(cartService.removeItem(usernameOf(jwt), productId));
	}

	@DeleteMapping
	public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Jwt jwt) {
		cartService.clearCart(usernameOf(jwt));
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/checkout")
	public ResponseEntity<Long> checkout(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CheckoutRequest request) {
		Long orderId = cartService.checkout(usernameOf(jwt), jwt.getTokenValue(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
	}
}
