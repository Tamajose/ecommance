package com.webarch.product.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.webarch.product.domain.Product;
import com.webarch.product.domain.ProductCategory;
import com.webarch.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataLoader {
    @Bean
    CommandLineRunner loadProducts(ProductRepository productRepository){
        return args -> {
            if(productRepository.count() > 0){
                return;
            }

            productRepository.save(
                Product.builder()
                    .name("MacBook Pro M5 Pro")
                    .description("Apple MacBook Pro with M5 Pro chip")
                    .price(new BigDecimal("275000.00"))
                    .stockQuantity(10)
                    .category(ProductCategory.ELECTRONICS)
                    .sellerUsername("seller")
                    .imageURL("https://example.com/macbook.jpg")
                    .active(true)
                    .build()
            );
        };
    }
}
