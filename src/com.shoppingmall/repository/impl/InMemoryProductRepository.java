package com.shoppingmall.repository.impl;

import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;

import java.util.*;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> products = new HashMap<>();

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void deleteById(String id) {
        products.remove(id);
    }
}
