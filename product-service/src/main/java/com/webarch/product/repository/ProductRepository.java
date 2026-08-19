package com.webarch.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webarch.product.domain.Product;
import com.webarch.product.domain.ProductCategory;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByActive();

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByName(String name);
}
