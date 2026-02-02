package com.shoppingmall.repository;

import com.shoppingmall.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    List<Product> findAll();
    Optional<Product> findById(String id);
}
