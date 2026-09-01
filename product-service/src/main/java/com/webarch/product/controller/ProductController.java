package com.webarch.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webarch.product.domain.ProductCategory;
import com.webarch.product.dto.ProductRequest;
import com.webarch.product.dto.StockRequest;
import com.webarch.product.dto.ProductResponse;
import com.webarch.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Value("${internal.api-key}")
    private String internalApiKey;

    @PostMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> adjustStock(@PathVariable Long id,
            @RequestHeader("X-Internal-Api-Key") String apiKey,
            @Valid @RequestBody StockRequest request) {
        if (!internalApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(productService.adjustStock(id, request.delta()));
    }

    private boolean isAdmin(Jwt jwt){
        String scope = jwt.getClaimAsString("scope");
        if(scope == null || scope.isBlank()){
            return false;
        }
        return java.util.List.of(scope.split(" ")).contains("ADMIN");
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request,
            @AuthenticationPrincipal Jwt jwt){
        ProductResponse response = productService.createProduct(request, jwt.getSubject());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
        @RequestParam(required = false) String name, @RequestParam(required = false) ProductCategory category){

        if(name != null && !name.isBlank()){
            return ResponseEntity.ok(productService.searchProducts(name));
        }

        if(category != null){
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProductResponse>> getMyProducts(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(productService.getProductsBySeller(jwt.getSubject()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request,
            @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(productService.updateProduct(id, request, jwt.getSubject(), isAdmin(jwt)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt){
        productService.deleteProduct(id, jwt.getSubject(), isAdmin(jwt));

        return ResponseEntity.noContent().build();
    }
}
