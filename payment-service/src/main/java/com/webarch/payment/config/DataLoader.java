package com.webarch.payment.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.webarch.payment.domain.Payment;
import com.webarch.payment.domain.PaymentMethod;
import com.webarch.payment.domain.PaymentStatus;
import com.webarch.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataLoader {
    @Bean
    CommandLineRunner loadPayments(PaymentRepository paymentRepository){
        return args -> {
            if(paymentRepository.count() > 0){
                return;
            }

            paymentRepository.save(
                Payment.builder()
                    .userId(1L)
                    .cartId(1L)
                    .amount(new BigDecimal("275000.00"))
                    .paymentMethod(PaymentMethod.CASH_ON_DELIVERY)
                    .paymentStatus(PaymentStatus.PENDING)
                    .build()
            );
        };
    }
}
