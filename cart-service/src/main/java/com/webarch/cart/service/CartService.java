package com.webarch.cart.service;

import com.webarch.cart.client.OrderClient;
import com.webarch.cart.client.ProductClient;
import com.webarch.cart.domain.Cart;
import com.webarch.cart.domain.CartItem;
import com.webarch.cart.dto.CartItemRequest;
import com.webarch.cart.dto.CartItemResponse;
import com.webarch.cart.dto.CartResponse;
import com.webarch.cart.dto.CheckoutRequest;
import com.webarch.cart.repository.CartItemRepository;
import com.webarch.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductClient productClient;
	private final OrderClient orderClient;

	@Transactional
	public CartResponse getCart(String username) {
		Cart cart = cartRepository.findByUsername(username).orElse(null);
		if (cart == null || cart.getItems().isEmpty()) {
			return new CartResponse(null, username, List.of(), 0, BigDecimal.ZERO, null, null);
		}
		return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
	}

	@Transactional
	public CartResponse addItem(String username, CartItemRequest request) {
		Cart cart = getOrCreateCart(username);

		ProductClient.ProductSnapshot product;
		try {
			product = productClient.getProduct(request.productId());
		} catch (RestClientException e) {
			throw new IllegalStateException("Unable to reach product-service for product " + request.productId());
		}
		if (product == null || product.stockQuantity() == null) {
			throw new IllegalArgumentException("Product not found: " + request.productId());
		}
		if (product.stockQuantity() < request.quantity()) {
			throw new IllegalArgumentException("Insufficient stock for product " + request.productId());
		}

		Optional<CartItem> existing = cart.getItems().stream()
				.filter(i -> i.getProductId().equals(request.productId()))
				.findFirst();
		if (existing.isPresent()) {
			existing.get().setQuantity(existing.get().getQuantity() + request.quantity());
		} else {
			cart.getItems().add(CartItem.builder()
					.cart(cart)
					.productId(product.id())
					.productName(product.name())
					.unitPrice(product.price())
					.quantity(request.quantity())
					.build());
		}
		cartRepository.save(cart);
		return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
	}

	@Transactional
	public CartResponse updateItemQuantity(String username, Long productId, Integer quantity) {
		Cart cart = findCartOrThrow(username);
		CartItem item = findItemOrThrow(cart.getId(), productId);

		ProductClient.ProductSnapshot product;
		try {
			product = productClient.getProduct(productId);
		} catch (RestClientException e) {
			throw new IllegalStateException("Unable to reach product-service for product " + productId);
		}
		if (product != null && product.stockQuantity() != null && product.stockQuantity() < quantity) {
			throw new IllegalArgumentException("Insufficient stock for product " + productId);
		}

		item.setQuantity(quantity);
		cartItemRepository.save(item);
		cartRepository.save(cart);
		return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
	}

	@Transactional
	public CartResponse removeItem(String username, Long productId) {
		Cart cart = findCartOrThrow(username);
		CartItem item = findItemOrThrow(cart.getId(), productId);
		cartItemRepository.delete(item);
		cartRepository.save(cart);
		return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
	}

	@Transactional
	public void clearCart(String username) {
		Cart cart = findCartOrThrow(username);
		cartItemRepository.deleteByCartId(cart.getId());
		cartRepository.save(cart);
	}

	@Transactional
	public Long checkout(String username, String token, CheckoutRequest request) {
		Cart cart = findCartOrThrow(username);
		if (cart.getItems().isEmpty()) {
			throw new IllegalArgumentException("Cart is empty");
		}
		List<OrderClient.OrderItemRequest> items = cart.getItems().stream()
				.map(i -> new OrderClient.OrderItemRequest(i.getProductId(), i.getQuantity()))
				.toList();
		OrderClient.OrderRequest orderRequest = new OrderClient.OrderRequest(
				items, request.recipientName(), request.shippingAddress());
		Long orderId = orderClient.createOrder(token, orderRequest).id();
		cartItemRepository.deleteAll(cart.getItems());
		cart.getItems().clear();
		return orderId;
	}

	private Cart getOrCreateCart(String username) {
		return cartRepository.findByUsername(username)
				.orElseGet(() -> cartRepository.save(Cart.builder().username(username).build()));
	}

	private Cart findCartOrThrow(String username) {
		return cartRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + username));
	}

	private CartItem findItemOrThrow(Long cartId, Long productId) {
		return cartItemRepository.findByCartIdAndProductId(cartId, productId)
				.orElseThrow(() -> new IllegalArgumentException("No item found for product: " + productId));
	}

	private CartResponse toResponse(Cart cart, List<CartItem> items) {
		List<CartItemResponse> itemResponses = items.stream().map(this::toItemResponse).toList();
		int totalItems = itemResponses.stream().mapToInt(CartItemResponse::quantity).sum();
		BigDecimal totalAmount = itemResponses.stream()
				.map(CartItemResponse::lineTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return new CartResponse(cart.getId(), cart.getUsername(), itemResponses, totalItems, totalAmount,
				cart.getCreatedAt(), cart.getUpdatedAt());
	}

	private CartItemResponse toItemResponse(CartItem item) {
		return new CartItemResponse(item.getId(), item.getProductId(), item.getProductName(),
				item.getUnitPrice(), item.getQuantity(),
				item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
	}
}
