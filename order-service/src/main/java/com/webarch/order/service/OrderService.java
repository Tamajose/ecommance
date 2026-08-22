package com.webarch.order.service;

import com.webarch.order.client.ProductClient;
import com.webarch.order.domain.Order;
import com.webarch.order.domain.OrderItem;
import com.webarch.order.domain.OrderStatus;
import com.webarch.order.dto.*;
import com.webarch.order.exception.ForbiddenException;
import com.webarch.order.repository.OrderItemRepository;
import com.webarch.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductClient productClient;

	private static final Map<OrderStatus, List<OrderStatus>> TRANSITIONS = Map.of(
			OrderStatus.PAID, List.of(OrderStatus.PROCESSING, OrderStatus.CANCELLED),
			OrderStatus.PROCESSING, List.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
			OrderStatus.SHIPPED, List.of(OrderStatus.DELIVERED),
			OrderStatus.DELIVERED, List.of(),
			OrderStatus.CANCELLED, List.of()
	);

	@Transactional
	public OrderResponse create(String username, OrderRequest request) {
		BigDecimal total = BigDecimal.ZERO;
		List<ProductClient.ProductSnapshot> snapshots = new ArrayList<>();

		for (OrderItemRequest item : request.items()) {
			ProductClient.ProductSnapshot product;
			try {
				product = productClient.getProduct(item.productId());
			} catch (RestClientException e) {
				throw new IllegalStateException("Unable to reach product-service for product " + item.productId());
			}
			if (product == null || product.stockQuantity() == null) {
				throw new IllegalArgumentException("Product not found: " + item.productId());
			}
			if (product.stockQuantity() < item.quantity()) {
				throw new IllegalArgumentException("Insufficient stock for product " + item.productId());
			}
			total = total.add(product.price().multiply(BigDecimal.valueOf(item.quantity())));
			snapshots.add(product);
		}

		try {
			for (OrderItemRequest item : request.items()) {
				productClient.adjustStock(item.productId(), -item.quantity());
			}
		} catch (RestClientException e) {
			for (OrderItemRequest item : request.items()) {
				try {
					productClient.adjustStock(item.productId(), item.quantity());
				} catch (RestClientException ignored) {
				}
			}
			throw new IllegalStateException("Stock deduction failed; order was not placed");
		}

		Order order = Order.builder()
				.username(username)
				.status(OrderStatus.PAID)
				.totalAmount(total)
				.recipientName(request.recipientName())
				.shippingLine(request.shippingAddress().line())
				.shippingCity(request.shippingAddress().city())
				.shippingPostalCode(request.shippingAddress().postalCode())
				.shippingCountry(request.shippingAddress().country())
				.build();
		Order saved = orderRepository.save(order);

		List<OrderItem> items = new ArrayList<>();
		for (int i = 0; i < request.items().size(); i++) {
			OrderItemRequest req = request.items().get(i);
			ProductClient.ProductSnapshot snap = snapshots.get(i);
			items.add(OrderItem.builder()
					.order(saved)
					.productId(snap.id())
					.productName(snap.name())
					.unitPrice(snap.price())
					.quantity(req.quantity())
					.build());
		}
		orderItemRepository.saveAll(items);

		return toResponse(saved);
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> getMine(String username) {
		return orderRepository.findByUsername(username).stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> getAll() {
		return orderRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public OrderResponse getById(Long id, String username, boolean isAdmin) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		if (!isAdmin && !order.getUsername().equals(username)) {
			throw new ForbiddenException("Not authorized to view this order");
		}
		return toResponse(order);
	}

	@Transactional
	public OrderResponse updateStatus(Long id, OrderStatusUpdateRequest request) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		OrderStatus target = request.status();
		if (!TRANSITIONS.getOrDefault(order.getStatus(), List.of()).contains(target)) {
			throw new IllegalArgumentException(
					"Invalid status transition: " + order.getStatus() + " -> " + target);
		}
		order.setStatus(target);
		if (target == OrderStatus.SHIPPED) {
			order.setTrackingNumber(request.trackingNumber());
			order.setCarrier(request.carrier());
		}
		return toResponse(orderRepository.save(order));
	}

	@Transactional
	public OrderResponse cancel(Long id, String username, boolean isAdmin) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		if (!isAdmin && !order.getUsername().equals(username)) {
			throw new ForbiddenException("Not authorized to cancel this order");
		}
		if (order.getStatus() != OrderStatus.PAID && order.getStatus() != OrderStatus.PROCESSING) {
			throw new IllegalArgumentException("Cannot cancel order in status " + order.getStatus());
		}
		for (OrderItem item : orderItemRepository.findByOrderId(order.getId())) {
			try {
				productClient.adjustStock(item.getProductId(), item.getQuantity());
			} catch (RestClientException ignored) {
			}
		}
		order.setStatus(OrderStatus.CANCELLED);
		return toResponse(orderRepository.save(order));
	}

	private OrderResponse toResponse(Order order) {
		List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
		List<OrderItemResponse> itemResponses = items.stream().map(i -> new OrderItemResponse(
				i.getProductId(),
				i.getProductName(),
				i.getUnitPrice(),
				i.getQuantity(),
				i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
		)).toList();

		return new OrderResponse(
				order.getId(),
				order.getUsername(),
				order.getStatus(),
				order.getTotalAmount(),
				order.getRecipientName(),
				order.getShippingLine(),
				order.getShippingCity(),
				order.getShippingPostalCode(),
				order.getShippingCountry(),
				order.getTrackingNumber(),
				order.getCarrier(),
				order.getCreatedAt(),
				order.getUpdatedAt(),
				itemResponses
		);
	}
}
