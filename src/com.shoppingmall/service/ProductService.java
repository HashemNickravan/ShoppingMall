package com.shoppingmall.service;

import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchByName(String keyword) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
