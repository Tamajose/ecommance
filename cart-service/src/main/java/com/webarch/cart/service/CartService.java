package com.webarch.cart.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webarch.cart.domain.Cart;
import com.webarch.cart.domain.CartItem;
import com.webarch.cart.dto.CartItemRequest;
import com.webarch.cart.dto.CartItemResponse;
import com.webarch.cart.dto.CartResponse;
import com.webarch.cart.repository.CartItemRepository;
import com.webarch.cart.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartResponse getCart(Long userId){
        Cart cart = getOrCreateCart(userId);

        return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
    }

    @Transactional
    public CartResponse addItem(Long userId, CartItemRequest request){
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId())
                .orElse(null);

        if(item != null){
            item.setQuantity(item.getQuantity() + request.quantity());
            item.setUnitPrice(request.unitPrice());
            item.setProductName(request.productName());
        } else {
            item = CartItem.builder()
                    .cart(cart)
                    .productId(request.productId())
                    .productName(request.productName())
                    .unitPrice(request.unitPrice())
                    .quantity(request.quantity())
                    .build();
        }

        cartItemRepository.save(item);
        cartRepository.save(cart);

        return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
    }

    @Transactional
    public CartResponse updateItemQuantity(Long userId, Long productId, Integer quantity){
        Cart cart = findCartOrThrow(userId);
        CartItem item = findItemOrThrow(cart.getId(), productId);

        item.setQuantity(quantity);
        cartItemRepository.save(item);
        cartRepository.save(cart);

        return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
    }

    @Transactional
    public CartResponse removeItem(Long userId, Long productId){
        Cart cart = findCartOrThrow(userId);
        CartItem item = findItemOrThrow(cart.getId(), productId);

        cartItemRepository.delete(item);
        cartRepository.save(cart);

        return toResponse(cart, cartItemRepository.findByCartId(cart.getId()));
    }

    @Transactional
    public void clearCart(Long userId){
        Cart cart = findCartOrThrow(userId);

        cartItemRepository.deleteByCartId(cart.getId());
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(Long userId){
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).build()));
    }

    private Cart findCartOrThrow(Long userId){
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No cart found for user ID: " + userId));
    }

    private CartItem findItemOrThrow(Long cartId, Long productId){
        return cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new RuntimeException("No item found for product ID: " + productId));
    }

    private CartResponse toResponse(Cart cart, List<CartItem> items){
        List<CartItemResponse> itemResponses = items.stream().map(this::toItemResponse).toList();

        int totalItems = itemResponses.stream().mapToInt(CartItemResponse::quantity).sum();
        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
            cart.getId(),
            cart.getUserId(),
            itemResponses,
            totalItems,
            totalAmount,
            cart.getCreatedAt(),
            cart.getUpdatedAt()
        );
    }

    private CartItemResponse toItemResponse(CartItem item){
        return new CartItemResponse(
            item.getId(),
            item.getProductId(),
            item.getProductName(),
            item.getUnitPrice(),
            item.getQuantity(),
            item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
