package com.webarch.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webarch.payment.domain.Payment;
import com.webarch.payment.domain.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List <Payment> findByUserId(Long userId);

    List<Payment> findByCartId(Long cartId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
}
