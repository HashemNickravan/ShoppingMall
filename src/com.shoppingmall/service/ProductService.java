package com.shoppingmall.service;

import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(String name, long price, int stock) {
        Product product = new Product(
                UUID.randomUUID().toString(),
                name,
                price,
                stock,
                null
        );
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }
}
