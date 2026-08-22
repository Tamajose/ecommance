package com.webarch.payment.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.webarch.payment.domain.Payment;
import com.webarch.payment.domain.PaymentStatus;
import com.webarch.payment.dto.PaymentRequest;
import com.webarch.payment.dto.PaymentResponse;
import com.webarch.payment.repository.PaymentRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request){
        Payment payment = Payment.builder()
                            .userId(request.userId())
                            .cartId(request.cartId())
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
            payment.getUserId(),
            payment.getCartId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getPaymentStatus(),
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments(){
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id){
        Payment payment = paymentRepository.findById(id)
                            .orElseThrow(
                                () -> new RuntimeException("No Payment Details can be found with Payment ID: " + id)
                            );
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse processPayment(Long id){
        Payment payment = paymentRepository.findById(id)
                            .orElseThrow(
                                () -> new RuntimeException("No Payment Details can be found with Payment ID: " + id)
                            );

        payment.setPaymentStatus(PaymentStatus.COMPLETED);

        Payment updatedPayment = paymentRepository.save(payment);
        return toResponse(updatedPayment);
    }

    @Transactional
    public PaymentResponse failPayment(Long id){
        Payment payment = paymentRepository.findById(id)
                            .orElseThrow(
                                () -> new RuntimeException("No Payment Details can be found with Payment ID: " + id)
                            );

        payment.setPaymentStatus(PaymentStatus.FAILED);

        Payment updatedPayment = paymentRepository.save(payment);
        return toResponse(updatedPayment);
    }

    @Transactional
    public PaymentResponse refundPayment(Long id){
        Payment payment = paymentRepository.findById(id)
                            .orElseThrow(
                                () -> new RuntimeException("No Payment Details can be found with Payment ID: " + id)
                            );

        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        Payment updatedPayment = paymentRepository.save(payment);
        return toResponse(updatedPayment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUser(Long userId){
        return paymentRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByCart(Long cartId){
        return paymentRepository.findByCartId(cartId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentByStatus(PaymentStatus paymentStatus){
        return paymentRepository.findByPaymentStatus(paymentStatus).stream().map(this::toResponse).toList();
    }
}