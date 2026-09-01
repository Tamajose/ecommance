package com.webarch.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.webarch.product.domain.Product;
import com.webarch.product.domain.ProductCategory;
import com.webarch.product.dto.ProductRequest;
import com.webarch.product.dto.ProductResponse;
import com.webarch.product.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request, String sellerUsername){
        Product product = Product.builder()
                            .name(request.name())
                            .description(request.description())
                            .price(request.price())
                            .stockQuantity(request.stockQuantity())
                            .category(request.category())
                            .imageURL(request.imageURL())
                            .sellerUsername(sellerUsername)
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
            product.getSellerUsername(),
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
    public List<ProductResponse> getProductsBySeller(String sellerUsername){
        return productRepository.findBySellerUsername(sellerUsername).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Product is found with Product ID: " + id)
        );

        return toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request, String principalUsername, boolean isAdmin){
        Product product = productRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Product is found with Product ID: " + id)
        );

        ensureOwnership(product, principalUsername, isAdmin);

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
    public void deleteProduct(Long id, String principalUsername, boolean isAdmin){
        Product product = productRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Product is found with Product ID: " + id)
        );

        ensureOwnership(product, principalUsername, isAdmin);

        product.setActive(false);
        productRepository.save(product);
    }

    private void ensureOwnership(Product product, String principalUsername, boolean isAdmin){
        if(isAdmin){
            return;
        }

        if(!principalUsername.equals(product.getSellerUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only manage your own product listings");
        }
    }

    @Transactional
    public ProductResponse adjustStock(Long id, int delta){
        Product product = productRepository.findById(id).orElseThrow(
            () -> new RuntimeException("No Product is found with Product ID: " + id)
        );

        int newStock = product.getStockQuantity() + delta;
        if(newStock < 0){
            throw new IllegalArgumentException("Insufficient stock for product " + id);
        }

        product.setStockQuantity(newStock);
        return toResponse(productRepository.save(product));
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
