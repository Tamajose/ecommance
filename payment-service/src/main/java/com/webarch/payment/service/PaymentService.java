package com.webarch.payment.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.webarch.payment.client.OrderClient;
import com.webarch.payment.domain.Payment;
import com.webarch.payment.domain.PaymentStatus;
import com.webarch.payment.dto.PaymentRequest;
import com.webarch.payment.dto.PaymentResponse;
import com.webarch.payment.exception.ForbiddenException;
import com.webarch.payment.repository.PaymentRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    private static final Map<PaymentStatus, List<PaymentStatus>> TRANSITIONS = Map.of(
            PaymentStatus.PENDING, List.of(PaymentStatus.COMPLETED, PaymentStatus.FAILED),
            PaymentStatus.COMPLETED, List.of(PaymentStatus.REFUNDED),
            PaymentStatus.FAILED, List.of(),
            PaymentStatus.REFUNDED, List.of()
    );

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request){
        paymentRepository.findByOrderId(request.orderId()).ifPresent(existing -> {
            throw new IllegalStateException("Payment already exists for order " + request.orderId());
        });

        Payment payment = Payment.builder()
                            .username(request.username())
                            .orderId(request.orderId())
                            .amount(request.amount())
                            .paymentMethod(request.paymentMethod())
                            .paymentStatus(PaymentStatus.PENDING)
                            .build();

        Payment savedPayment = paymentRepository.save(payment);

        return toResponse(savedPayment);
    }

    private PaymentResponse toResponse(Payment payment){
        return new PaymentResponse(
            payment.getPaymentId(),
            payment.getUsername(),
            payment.getOrderId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getPaymentStatus(),
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
    }

    private Payment findOrThrow(Long id){
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Payment Details can be found with Payment ID: " + id));
    }

    private void checkOwnerOrAdmin(Payment payment, String username, boolean isAdmin){
        if (!isAdmin && !payment.getUsername().equals(username)) {
            throw new ForbiddenException("Not authorized to view this payment");
        }
    }

    private Payment transition(Long id, PaymentStatus target){
        Payment payment = findOrThrow(id);
        if (!TRANSITIONS.getOrDefault(payment.getPaymentStatus(), List.of()).contains(target)) {
            throw new IllegalStateException(
                    "Invalid payment status transition: " + payment.getPaymentStatus() + " -> " + target);
        }
        payment.setPaymentStatus(target);
        Payment saved = paymentRepository.save(payment);

        try {
            orderClient.syncOrderStatus(saved.getOrderId(), target.name());
        } catch (RestClientException ignored) {
            // order-service being unreachable shouldn't block the payment status change itself
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments(){
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id, String username, boolean isAdmin){
        Payment payment = findOrThrow(id);
        checkOwnerOrAdmin(payment, username, isAdmin);
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse processPayment(Long id){
        return toResponse(transition(id, PaymentStatus.COMPLETED));
    }

    @Transactional
    public PaymentResponse failPayment(Long id){
        return toResponse(transition(id, PaymentStatus.FAILED));
    }

    @Transactional
    public PaymentResponse refundPayment(Long id){
        return toResponse(transition(id, PaymentStatus.REFUNDED));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUsername(String username){
        return paymentRepository.findByUsername(username).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrder(Long orderId, String username, boolean isAdmin){
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No payment found for order: " + orderId));
        checkOwnerOrAdmin(payment, username, isAdmin);
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentByStatus(PaymentStatus paymentStatus){
        return paymentRepository.findByPaymentStatus(paymentStatus).stream().map(this::toResponse).toList();
    }
}
