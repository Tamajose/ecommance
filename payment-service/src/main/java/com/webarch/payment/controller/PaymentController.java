package com.webarch.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webarch.payment.dto.PaymentRequest;
import com.webarch.payment.dto.PaymentResponse;
import com.webarch.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

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
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestHeader("X-Internal-Api-Key") String apiKey,
            @Valid @RequestBody PaymentRequest request){
        if (!internalApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments(){
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id){
        return ResponseEntity.ok(paymentService.getPaymentById(id, usernameOf(jwt), isAdmin(jwt)));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.processPayment(id));
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<PaymentResponse> failPayment(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.failPayment(id));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUser(@AuthenticationPrincipal Jwt jwt,
            @PathVariable String username){
        if (!isAdmin(jwt) && !usernameOf(jwt).equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(paymentService.getPaymentsByUsername(username));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(@AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId){
        return ResponseEntity.ok(paymentService.getPaymentByOrder(orderId, usernameOf(jwt), isAdmin(jwt)));
    }
}
