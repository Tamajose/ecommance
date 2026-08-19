package com.webarch.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.webarch.product.domain.Product;
import com.webarch.product.domain.ProductCategory;
import com.webarch.product.dto.ProductRequest;
import com.webarch.product.dto.ProductResponse;
import com.webarch.product.repository.ProductRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request){
        Product product = Product.builder()
                            .name(request.name())
                            .description(request.description())
                            .price(request.price())
                            .stockQuantity(request.stockQuantity())
                            .category(request.category())
                            .imageURL(request.imageURL())
                            .active(true)
                            .build();

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    private ProductResponse toResponse(Product product){
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getCategory(),
            product.getImageURL(),
            product.getActive(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(){
        return productRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(
            () -> new RuntimeException("No Product is found with Product ID: " + id)
        );

        return toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id).orElseThrow(
            () -> new RuntimeException("No Product is found with Product ID: " + id)
        );

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.category());
        product.setImageURL(request.imageURL());

        Product updatedProduct = productRepository.save(product);

        return toResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(
            () -> new RuntimeException("No Product is found with Product ID: " + id)
        );

        product.setActive(false);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String name){
        return productRepository.findByNameContainingIgnoreCase(name).stream().filter(Product::getActive).map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(ProductCategory category){
        return productRepository.findByCategory(category).stream().filter(Product::getActive).map(this::toResponse).toList();
    }
}
